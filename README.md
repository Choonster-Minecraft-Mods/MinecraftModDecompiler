# Minecraft Mod Decompiler
Decompiles and deobfuscates all Minecraft Forge mods in a specified directory and outputs the source files to another directory.

Please respect the individual mod licenses when using this tool.

## Supported Platforms
This tool uses [JD-Core-java](https://github.com/nviennot/jd-core-java) for decompilation.

JD itself supports:
- Linux 32/64-bit
- Windows 32/64-bit
- Mac OSX 32/64-bit on x86 hardware

## Build
Clone the repository and run `gradlew build`. This will download the required dependencies and generate a JAR file in the `build/libs` directory.

You can also run `gradlew shadowJar` to generate a stand-alone JAR with all dependencies included in it.

The `idea` and `eclipse` Gradle plugins are included in the build script, so you can run `gradlew idea` or `gradlew eclipse` to generate an IDE project.

## Usage
```shell
java -jar MinecraftModDecompiler.jar [options]
  Options:
  * -mappingsDirectory
       The directory containing the MCP mapping files (fields.csv and methods.csv)
  * -modDirectory
       The directory containing the mods to decompile
  * -outputDirectory
       The directory to output the decompiled classes to
```

For convenience, the included `MCModDecompiler` batch/shell scripts can be run with the same arguments after building in a cloned repository.

The MCP mapping files can be found in the [MCP releases](http://www.modcoderpack.com/website/releases) under the `conf` directory. If you've set up a ForgeGrqadle workspace, you can also find the mapping files in your Gradle cache (`~/.gradle/caches/minecraft/net/minecraftforge/forge/<forge_version>/unpacked/conf` for Minecraft 1.7.2 and later).

## License
MinecraftModDecompiler is released under the MIT license. See `LICENSE.txt` for details.

JD-IntelliJ (included with JD-Core-java) is free for non-commercial use. This means that JD-IntelliJ shall not be included or embedded into commercial software products. Nevertheless, this tool may be freely used for personal needs in a commercial or non-commercial environments.