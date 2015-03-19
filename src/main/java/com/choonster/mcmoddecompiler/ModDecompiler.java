package com.choonster.mcmoddecompiler;

import jd.core.Decompiler;
import jd.core.DecompilerException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ModDecompiler {
	private Path modDirectory;
	private Path outputDirectory;
	private Path mappingsDirectory;
	private Decompiler decompiler;
	private MCPData mcpData;

	public ModDecompiler(Path modDirectory, Path outputDirectory, Path mappingsDirectory) throws IOException {
		this.modDirectory = modDirectory;
		this.outputDirectory = outputDirectory;
		this.mappingsDirectory = mappingsDirectory;

		this.decompiler = new Decompiler();
		this.mcpData = new MCPData(mappingsDirectory);
	}

	public void decompile() throws IOException {
		System.out.printf("Starting decompilation of mods in %s to %s\n", modDirectory.toString(), outputDirectory.toString());
		decompileDirectory(modDirectory);
		System.out.printf("Decompilation complete.");
	}

	private void decompileDirectory(Path directory) throws IOException {
		System.out.printf("Entering directory %s...\n", modDirectory.relativize(directory).toString());

		DirectoryStream<Path> stream = Files.newDirectoryStream(directory);
		for (Path entry : stream) {
			if (Files.isDirectory(entry)) {
				decompileDirectory(entry);
			} else {
				try {
					decompileJAR(entry);
				} catch (DecompilerException e) {
					System.err.printf("Exception decompiling %s:", entry.getFileName().toString());
					e.printStackTrace();
				}
			}
		}
	}

	private void decompileJAR(Path jar) throws IOException, DecompilerException {
		Map<String, String> pathToSrc = decompiler.decompile(jar.toString());

		for (Map.Entry<String, String> entry : pathToSrc.entrySet()) {
			String path = entry.getKey();
			if (path.endsWith(".java")) {
				pathToSrc.put(path, mcpData.deobfuscate(entry.getValue())); // Deobfuscate each Java file
			}
		}

		decompiler.decompileToDir(outputDirectory.toString(), pathToSrc);

		System.out.printf("Decompiled %s...\n", jar.getFileName().toString());
	}
}
