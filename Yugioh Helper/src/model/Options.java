package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Options {
	public static JsonObject rootObject, paths, draftExporter;
	public static String mainDeckKeyword, extraDeckKeyword, sideDeckKeyword, whitelistName;
	public static boolean displayInfos, compareWithAllCards;

	private Options() {

	}

	public static void setRoot(File in) throws FileNotFoundException {
		JsonReader optionsReader = Json.createReader(new FileReader(in));
		rootObject = optionsReader.readObject();
		paths = rootObject.getJsonObject("paths");
		draftExporter = rootObject.getJsonObject("draft exporter");
		mainDeckKeyword = rootObject.getString("maindeckkeyword");
		extraDeckKeyword = rootObject.getString("extradeckkeyword");
		sideDeckKeyword = rootObject.getString("sidedeckkeyword");
		whitelistName = rootObject.getString("whitelistname");
		displayInfos = rootObject.getBoolean("display infos");
		compareWithAllCards = rootObject.getBoolean("compare with allcards");
		optionsReader.close();
	}
}
