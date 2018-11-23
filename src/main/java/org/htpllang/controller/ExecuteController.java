package org.htpllang.controller;

import org.htpllang.exception.CompileException;
import org.htpllang.exception.InvalidRootElementException;
import org.htpllang.exception.SyntaxException;
import org.htpllang.service.ExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("execute")
public class ExecuteController {
	
	private final ExecuteService executeService;
	
	@Autowired
	public ExecuteController(ExecuteService executeService) {
		this.executeService = executeService;
	}
	
	@PostMapping
	public String compile(@RequestBody String htplCode) throws InvalidRootElementException, SyntaxException, CompileException {
		return executeService.execute(htplCode);
	}
	
}
