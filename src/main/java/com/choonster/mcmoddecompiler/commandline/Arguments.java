package com.choonster.mcmoddecompiler.commandline;

import com.beust.jcommander.Parameter;

import java.nio.file.Path;

public class Arguments {

	@Parameter(names = "-modDirectory", description = "The directory containing the mods to decompile", required = true, converter = PathConverter.class, validateWith = DirectoryPathValidator.class)
	public Path modDirectory;

	@Parameter(names = "-outputDirectory", description = "The directory to output the decompiled classes to", required = true, converter = PathConverter.class, validateWith = DirectoryPathValidator.class)
	public Path outputDirectory;

	@Parameter(names = "-mappingsDirectory", description = "The directory containing the MCP mapping files (fields.csv and methods.csv)", required = true, converter = PathConverter.class, validateWith = DirectoryPathValidator.class)
	public Path mappingsDirectory;
}