package com.choonster.mcmoddecompiler;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.choonster.mcmoddecompiler.commandline.Arguments;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		Arguments arguments = new Arguments();

		JCommander jCommander = new JCommander();
		jCommander.setProgramName("java -jar MinecraftModDecompiler.jar");
		jCommander.addObject(arguments);

		try {
			jCommander.parse(args);
		} catch (ParameterException e) {
			System.out.printf("Error: %s\n\n", e.getMessage());
			jCommander.usage();
			exit();
		}


		ModDecompiler decompiler = null;
		try {
			decompiler = new ModDecompiler(arguments.modDirectory, arguments.outputDirectory, arguments.mappingsDirectory);
		} catch (IOException e) {
			System.err.println("Exception initialising MCP data");
			e.printStackTrace();
			exit();
		}

		List<Path> errors = decompiler.decompile();
		if (errors.size() > 0) {
			System.err.println("Errored mods:");
			System.err.println(errors.toString());
			exit();
		}
	}

	private static void exit() {
		System.exit(-1);
	}
}
