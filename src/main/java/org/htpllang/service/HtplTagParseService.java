package org.htpllang.service;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.htpllang.exception.SyntaxException;
import org.htpllang.functional.ThrowableFunction;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
		tagMap.put("call", parseCallTag());
		tagMap.put("if", parseIfTag());
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
		return element -> {
			Attribute nameAttribute = element.attribute("name");
			if (nameAttribute != null) {
				return nameAttribute.getValue();
			} else {
				return element.getText();
			}
		};
	}
	
	private ThrowableFunction<Element, String, SyntaxException> parseConstTag() {
		return element -> element.attribute("value").getValue();
	}
	
	@SuppressWarnings("unchecked")
	private ThrowableFunction<Element, String, SyntaxException> parseArrayTag() {
		return element -> {
			String arrayName = Optional.ofNullable(element.attribute("name"))
					.orElseThrow(() -> new SyntaxException("attribute \"name\" is required")).getValue();
			
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
	
	@SuppressWarnings("unchecked")
	private ThrowableFunction<Element, String, SyntaxException> parseCallTag() {
		return element -> {
			String functionName = Optional.ofNullable(element.attribute("name"))
					.orElseThrow(() -> new SyntaxException("attribute \"name\" is required")).getValue();
			
			StringBuilder code = new StringBuilder();
			code.append(functionName)
					.append("(");
			element.content()
					.stream()
					.filter(e -> e instanceof Element)
					.forEach(e -> code.append(parseTag((Element) e, false, "const", "val"))
							.append(", "));
			code.append(")");
			
			return code.toString();
		};
	}
	
	@SuppressWarnings("unchecked")
	private ThrowableFunction<Element, String, SyntaxException> parseIfTag() {
		return element -> {
			if (element.content().size() < 2 && element.content().size() > 3)
				throw new SyntaxException("tags <cond> and <if-true> are required within <if>");
			if (((Element) element.content().get(0)).getName().equals("cond"))
				throw new SyntaxException("tag <cond> is required within <if>");
			if (((Element) element.content().get(1)).getName().equals("if-true"))
				throw new SyntaxException("tag <if-true> is required within <if>");
			
			StringBuilder code = new StringBuilder();
			code.append("if ");
			element.content()
					.stream()
					.filter(e -> e instanceof Element)
					.forEach(e -> {
						if (((Element) e).getName().equals("cond")) {
							code.append(parseTag((Element) e, false)).append(":\n");
						}
						if (((Element) e).getName().equals("if-true")) {
							code.append(INDENT).append(parseTag((Element) e, true)).append("pass\n");
						}
						if (((Element) e).getName().equals("if-else")) {
							code.append(INDENT).append(parseTag((Element) e, true)).append("pass\n");
						}
					});
			code.append(")");
			
			return code.toString();
		};
	}
	
}
