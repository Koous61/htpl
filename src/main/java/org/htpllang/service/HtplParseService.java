package org.htpllang.service;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.htpllang.exception.InvalidRootElementException;
import org.htpllang.exception.SyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringReader;

@Service
public class HtplParseService {
	
	private final HtplTagParseService htplTagParseService;
	
	@Autowired
	public HtplParseService(HtplTagParseService htplTagParseService) {
		this.htplTagParseService = htplTagParseService;
	}
	
	public String parse(String htplCode) throws DocumentException, SyntaxException {
		Document document = new SAXReader().read(new StringReader(htplCode));
		
		Element htplTag = document.getRootElement();
		return parse(htplTag).trim();
	}
	
	private String parse(Element element) throws SyntaxException {
		return htplTagParseService.parseTag(element);
	}
	
}
