package org.htpllang.service;

import org.htpllang.exception.CompileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class CompileService {
	
	@Value("${python.interpreter.path}")
	private String pyPath;
	
	@Value("${python.compiled.scripts.path}")
	private String compiledPath;
	
	public String compile(String code) throws CompileException {
		try {
			String fileName = generateFileName();
			BufferedWriter out = new BufferedWriter(new FileWriter(compiledPath + fileName));
			out.write(code);
			out.close();
			Process p = Runtime.getRuntime().exec(pyPath + " " + compiledPath + fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			return in.lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			throw new CompileException(e.getMessage());
		}
	}
	
	private String generateFileName() {
		return LocalDateTime.now().toString().replaceAll(":", "_") + ".py";
	}
	
}
