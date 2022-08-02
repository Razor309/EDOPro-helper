package controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import model.Options;

public class OptionsHandler {
	public static Options options;
	public static File source;

	private OptionsHandler() {

	}

	public static void deserializeOption(File in) throws IOException {
		source = in;
		FileReader fr = new FileReader(source);
		options = JsonbBuilder.create().fromJson(fr, Options.class);
		String str = JsonbBuilder.create().toJson(options);
		System.out.println(str);
		fr.close();
	}

	public static void serializeOptions() throws IOException {
		JsonbConfig config = new JsonbConfig().withFormatting(true);
		FileWriter fw = new FileWriter(source);
		JsonbBuilder.create(config).toJson(options, fw);
		fw.close();
	}
}
