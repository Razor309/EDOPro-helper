package model;

import java.util.HashMap;
import java.util.HashSet;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.json.bind.config.PropertyOrderStrategy;

@JsonbPropertyOrder(PropertyOrderStrategy.ANY)
public class OptionsImpl {
	@JsonbProperty("paths")
	public HashMap<String, String> paths;

	@JsonbProperty("draft-amount of formats for drafts whitelist generation")
	public Integer draftExportQuantity;

	@JsonbProperty("draft-delete old drafts before generating new ones")
	public boolean deleteOldDrafts;

	@JsonbProperty("draft-exporter extensions")
	public HashMap<String, String> draftExporterExtensions;

	@JsonbProperty("zzz ignore generated draft whitelists")
	public HashSet<String> generatedDraftWhitlists;

	@JsonbProperty("display infos")
	public boolean displayInfos;

	@JsonbProperty("apply banlist")
	public boolean applyBanlist;

	@JsonbProperty("name of general whitelist")
	public String generalWhitelistName;

	@JsonbProperty("name of export banlist for edopro")
	public String exportBanlistName;

	@JsonbProperty("name of export banlist for edopro trimmed")
	public String exportBanlistTrimmedName;

	@JsonbProperty("name of goodcards whitelist")
	public String goodcardsWhitelistName;

	@JsonbProperty("name of trimmed goodcards whitelist")
	public String trimmedGoodcardsWhitelistName;

	@JsonbProperty("import filter")
	public HashMap<String, HashSet<String>> importFilter;
}
