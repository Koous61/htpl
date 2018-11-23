package org.htpllang.service;

import org.dom4j.Element;
import org.htpllang.exception.SyntaxException;
import org.htpllang.functional.ThrowableFunction;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HtplTagParseService {
	
	private final String INDENT = "    ";
	private Map<String, ThrowableFunction<Element, String, SyntaxException>> tagMap = new HashMap<>();
	
	@PostConstruct
	private void init() {
		tagMap.put("htpl", parseHtplTag());
		tagMap.put("var", parseVarTag());
		tagMap.put("val", parseValTag());
		tagMap.put("const", parseConstTag());
		tagMap.put("array", parseArrayTag());
	}
	
	public String parseTag(Element element) throws SyntaxException {
		return parseTag(element, true);
	}
	
	private String parseTag(Element element, boolean newLine, String... allowedTagNames) {
		if (allowedTagNames.length != 0 && Arrays.stream(allowedTagNames).noneMatch(n -> element.getName().equals(n)))
			throw new SyntaxException("this tag is not allowed here");
		
		String tagName = element.getName();
		if (!tagMap.containsKey(tagName)) throw new SyntaxException("invalid tag name");
		
		String code = tagMap.get(tagName).apply(element);
		if (newLine) code += "\n";
		return code;
	}
	
	//	all tags parsing here
	
	@SuppressWarnings("unchecked")
	private ThrowableFunction<Element, String, SyntaxException> parseHtplTag() {
		return element -> {
			StringBuilder code = new StringBuilder();
			element.content()
					.stream()
					.filter(e -> e instanceof Element)
					.forEach((e) -> code.append(parseTag((Element) e)));
			return code.toString();
		};
	}
	
	private ThrowableFunction<Element, String, SyntaxException> parseVarTag() {
		return element -> {
			String variableName = element.attribute("name").getValue();
			String variableValue = element.getText();
			
			return variableName + " = " + variableValue;
		};
	}
	
	private ThrowableFunction<Element, String, SyntaxException> parseValTag() {
		return Element::getText;
	}
	
	private ThrowableFunction<Element, String, SyntaxException> parseConstTag() {
		return element -> element.attribute("value").getValue();
	}
	
	@SuppressWarnings("unchecked")
	private ThrowableFunction<Element, String, SyntaxException> parseArrayTag() {
		return element -> {
			String arrayName = element.attribute("name").getValue();
			
			StringBuilder code = new StringBuilder();
			code.append(arrayName)
					.append(" = [");
			element.content()
					.stream()
					.filter(e -> e instanceof Element)
					.forEach(e -> code.append(parseTag((Element) e, false, "const", "val"))
							.append(", "));
			code.append("]");
			
			return code.toString();
		};
	}
	
}
