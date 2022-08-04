package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.json.bind.annotation.JsonbProperty;

public class Options {
	@JsonbProperty("display infos")
	public boolean displayInfos;

	@JsonbProperty("apply banlist")
	public boolean applyBanlist;

	@JsonbProperty("general whitelist name")
	public String generalWhitelistName;

	@JsonbProperty("goodcards whitelist name")
	public String goodcardsWhitelistName;

	@JsonbProperty("trimmed goodcards whitelist name")
	public String trimmedGoodcardsWhitelistName;

	public HashMap<String, String> paths, keywords;

	@JsonbProperty("draft exporter extensions")
	public HashMap<String, String> draftExporterExtensions;

	@JsonbProperty("draft exporter filter")
	public HashMap<String, HashSet<String>> draftExporterFilter;
}
