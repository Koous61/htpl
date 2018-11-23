package org.htpllang.service;

import org.dom4j.DocumentException;
import org.htpllang.exception.CompileException;
import org.htpllang.exception.InvalidRootElementException;
import org.htpllang.exception.SyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ExecuteService {
	
	private final HtplParseService htplParseService;
	private final CompileService compileService;
	
	
	@Autowired
	public ExecuteService(HtplParseService htplParseService, CompileService compileService) {
		this.htplParseService = htplParseService;
		this.compileService = compileService;
	}
	
	public String execute(String htplCode) throws InvalidRootElementException, SyntaxException, CompileException {
		try {
			String pythonCode = htplParseService.parse(htplCode);
			return compileService.compile(pythonCode);
		} catch (DocumentException e) {
			throw new SyntaxException(e.getMessage());
		}
	}
	
}
