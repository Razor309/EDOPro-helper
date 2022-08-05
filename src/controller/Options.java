package controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import model.OptionsImpl;

public class Options {
	public static OptionsImpl optionsImpl;
	public static File source;

	private Options() {

	}

	public static void deserializeOption(File in) throws IOException {
		source = in;
		FileReader fr = new FileReader(source);
		optionsImpl = JsonbBuilder.create().fromJson(fr, OptionsImpl.class);
		fr.close();
	}

	public static void serializeOptions() throws IOException {
		JsonbConfig config = new JsonbConfig().withFormatting(true);
		FileWriter fw = new FileWriter(source);
		JsonbBuilder.create(config).toJson(optionsImpl, fw);
		fw.close();
	}
}
