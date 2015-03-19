package com.choonster.mcmoddecompiler.commandline;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryPathValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		Path path = Paths.get(value);
		if (!Files.isDirectory(path)) {
			throw new ParameterException("Parameter " + name + " must be a directory");
		}
	}
}
