package com.choonster.mcmoddecompiler;

import jd.core.Decompiler;
import jd.core.DecompilerException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ModDecompiler {
	private Path modDirectory;
	private Path outputDirectory;
	private Decompiler decompiler;
	private MCPData mcpData;

	public ModDecompiler(Path modDirectory, Path outputDirectory, Path mappingsDirectory) throws IOException {
		this.modDirectory = modDirectory;
		this.outputDirectory = outputDirectory;

		this.decompiler = new Decompiler();
		this.mcpData = new MCPData(mappingsDirectory);
	}

	public List<Path> decompile() {
		List<Path> errors = new ArrayList<>();

		System.out.printf("Starting decompilation of mods in %s to %s\n", modDirectory.toString(), outputDirectory.toString());
		decompileDirectory(modDirectory, errors);
		System.out.printf("Decompilation complete.");

		return errors;
	}

	private void decompileDirectory(Path directory, List<Path> errors) {
		System.out.printf("\n\nEntering directory %s...\n", modDirectory.relativize(directory).toString());

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
			for (Path entry : stream) {
				if (Files.isDirectory(entry)) {
					decompileDirectory(entry, errors);
				} else if (hasExtension(entry, ".jar") || hasExtension(entry, ".zip")) {
					try {
						decompileJAR(entry);
					} catch (Exception e) {
						errors.add(entry);
						System.err.printf("Exception decompiling %s:\n", entry.getFileName().toString());
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Exception iterating directory:");
			e.printStackTrace();
		}
	}

	private boolean hasExtension(Path path, String extension) {
		return path.toString().endsWith(extension);
	}

	private void decompileJAR(Path jar) throws IOException, DecompilerException {
		String jarName = jar.getFileName().toString();

		System.out.printf("\n\nDecompiling %s...\n", jarName);
		Map<String, String> pathToSrc = decompiler.decompile(jar.toString());

		System.out.println("Deobfuscating...");
		for (Iterator<Map.Entry<String, String>> iterator = pathToSrc.entrySet().iterator(); iterator.hasNext(); ) {
			Map.Entry<String, String> entry = iterator.next();
			String path = entry.getKey();
			if (path.endsWith(".java")) {
				pathToSrc.put(path, mcpData.deobfuscate(entry.getValue())); // Deobfuscate each Java file
			} else {
				iterator.remove(); // Remove non-Java files
			}
		}

		decompiler.decompileToDir(outputDirectory.toString(), pathToSrc);
		System.out.println("Complete");
	}
}
