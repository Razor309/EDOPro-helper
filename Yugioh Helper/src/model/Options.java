package model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.json.bind.annotation.JsonbProperty;

public class Options {
	public boolean displayInfos, compareWithAllCards;
	public String whitelistName;
	public HashMap<String, String> paths, keywords;

	@JsonbProperty("draft exporter extensions")
	public HashMap<String, String> draftExporterExtensions;

	@JsonbProperty("draft exporter filter")
	public HashMap<String, ArrayList<String>> draftExporterFilter;
}
