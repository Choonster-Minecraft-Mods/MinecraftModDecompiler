package com.choonster.mcmoddecompiler;

import com.opencsv.CSVReader;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MCPData {

	private final MappingData data;

	public MCPData(Path mcpMappingsDirectory) throws IOException {
		MappingData methods = readCSV(mcpMappingsDirectory.resolve("methods.csv"));
		MappingData fields = readCSV(mcpMappingsDirectory.resolve("fields.csv"));

		data = MappingData.join(methods, fields);
	}

	public String deobfuscate(String sourceCode) {
		return StringUtils.replaceEach(sourceCode, data.srgNames, data.deobfNames);
	}

	private MappingData readCSV(Path path) throws IOException {
		List<String> srgNames = new ArrayList<>(), deobfNames = new ArrayList<>();
		CSVReader parser = new CSVReader(Files.newBufferedReader(path));

		for (String[] line : parser) {
			srgNames.add(line[0]);
			deobfNames.add(line[1]);
		}

		return new MappingData(srgNames.toArray(ArrayUtils.EMPTY_STRING_ARRAY), deobfNames.toArray(ArrayUtils.EMPTY_STRING_ARRAY));
	}

	private static class MappingData {
		public final String[] srgNames;
		public final String[] deobfNames;

		public MappingData(String[] srgNames, String[] deobfNames) {
			this.srgNames = srgNames;
			this.deobfNames = deobfNames;
		}

		public static MappingData join(MappingData data1, MappingData data2) {
			String[] srgNames = ArrayUtils.addAll(data1.srgNames, data2.srgNames);
			String[] deobfName = ArrayUtils.addAll(data1.deobfNames, data2.deobfNames);

			return new MappingData(srgNames, deobfName);
		}
	}
}
