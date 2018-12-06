package org.htpllang.service;

import org.htpllang.exception.CompileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class CompileService {
	
	@Value("${python.interpreter.path}")
	private String pyPath;
	
	@Value("${python.compiled.scripts.path}")
	private String compiledPath;
	
	public String compile(String code) throws CompileException {
		return compile(code, false);
	}
	
	public String compile(String code, boolean deleteFile) throws CompileException {
		try {
			String fileName = generateFileName();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(compiledPath + fileName), StandardCharsets.UTF_8
			));
			out.write(code);
			out.close();
			ProcessBuilder builder = new ProcessBuilder(pyPath,
					compiledPath + fileName);
			builder.redirectErrorStream(true);
			Process p = builder.start();
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
			
			if (deleteFile) new File(compiledPath + fileName).delete();
			
			return in.lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			throw new CompileException(e.getMessage());
		}
	}
	
	private String generateFileName() {
		return LocalDateTime.now().toString().replaceAll(":", "_") + ".py";
	}
	
}
