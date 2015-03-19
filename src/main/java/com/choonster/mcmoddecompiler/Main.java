package com.choonster.mcmoddecompiler;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.choonster.mcmoddecompiler.commandline.Arguments;

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
			System.exit(-1);
		}


		try {
			new ModDecompiler(arguments.modDirectory, arguments.outputDirectory, arguments.mappingsDirectory).decompile();
		} catch (Exception e) {
			System.err.printf("Exception while decompiling:");
			e.printStackTrace();
		}
	}
}
