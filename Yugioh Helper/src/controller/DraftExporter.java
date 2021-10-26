package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonString;

import model.Options;
import model.YGODeck;
import view.ErrorDialog;
import view.GraphicalConsole;

public class DraftExporter {
	private static File draftFolder = new File(Options.paths.getString("draft folder"));
	private static File deckFolder = new File(Options.paths.getString("deck folder"));
	private static JsonObject exporter = Options.draftExporter;

	private DraftExporter() {
	}

	public static void test() {
//		JsonArray jArr = Options.draftExporter.getJsonArray("generated decks");
		JsonArrayBuilder jArrBuilder = Json.createArrayBuilder(Options.draftExporter.getJsonArray("generated decks"));
		JsonString jStr = Json.createValue("penis");
		System.out.println(jStr);
		jArrBuilder.add(jStr);
		System.out.println(jArrBuilder.build());
	}

	public static void exportAsMultipleWhitelist() throws IOException {
		try (Stream<Path> paths = Files.walk(Paths.get(draftFolder.getCanonicalPath()))) {
			paths.filter(Files::isRegularFile).filter(path -> {
				boolean whitelisted = true;
				// Check if file is whitelisted
				for (String s : exporter.getJsonArray("whitelist").getValuesAs(JsonString::getString)) {
					whitelisted = false;
					if (path.toString().contains(s)) {
						whitelisted = true;
					}
				}
				if (!whitelisted)
					return false;
				// If there was no whitelist or the file is whitelisted -> Check if file is
				// blacklisted
				for (String s : exporter.getJsonArray("blacklist").getValuesAs(JsonString::getString)) {
					if (path.toString().contains(s))
						return false;
				}
				return true;
			}).forEach(path -> {
				try {
					File out = new File(deckFolder + "\\" + exporter.getString("prefix") + path.toFile().getName());
					GraphicalConsole.add("Exported : " + path.toFile() + "\nas whitelist to\n" + out + "\n");
					ConverterYGODeckToWhitelist.convert(ConverterYDKToYGODeck.convert(path.toFile()));
				} catch (IOException e) {
					ErrorDialog ed = new ErrorDialog(e.getMessage());
					ed.showDialog();
				}
			});
			if (GraphicalConsole.getMessage() != "") {
				GraphicalConsole.showDialog();
				GraphicalConsole.flush();
			} else
				GraphicalConsole.showDialog("Nothing to export...");
		}
	}

	public static void exportAsMergedDeck() throws IOException {
		HashSet<File> files = new HashSet<>();
		try (Stream<Path> paths = Files.walk(Paths.get(draftFolder.getCanonicalPath()))) {
			paths.filter(Files::isRegularFile).forEach(path -> {
				files.add(path.toFile());
			});
			if (!files.isEmpty()) {
				File draftExportFile = new File(Options.paths.getString("draft export"));
				FileMerger.mergeTo(draftExportFile, files);
				YGODeck cards = ConverterYDKToYGODeck.convert(draftExportFile);

				DeckWriter.writeCards(cards);

				ConverterYGODeckToWhitelist.convert(cards);

				if (Options.displayInfos)
					GraphicalConsole.showDialog("- Merged files in: " + draftFolder + "\nto " + draftExportFile + "\n"
							+ "- Converted 'draft export' cleaned up and trimmed to 3 cards max to 'cards'\n"
							+ "- Generated " + Options.whitelistName + " using 'cards' /nto: "
							+ Options.paths.getString("whitelist folder"));
			} else
				GraphicalConsole.showDialog("Nothing to export...");
		}
	}

	public static void clearExports() throws IOException {
		try (Stream<Path> paths = Files.walk(Paths.get(deckFolder.getCanonicalPath()))) {
			paths.filter(Files::isRegularFile).filter(path -> {
				boolean filter = true;
				filter = filter && path.toFile().getName().substring(0, exporter.getString("prefix").length())
						.equals(exporter.getString("prefix"));
				filter = filter && path.toString().contains(exporter.getString("keyword"));
				return filter;
			}).forEach(path -> {
				GraphicalConsole.add("deleted: " + path.toFile().getName());
				path.toFile().delete();
			});
			if (GraphicalConsole.getMessage() != "") {
				GraphicalConsole.showDialog();
				GraphicalConsole.flush();
			} else
				GraphicalConsole.showDialog("Nothing to delete...");
		}
	}
}
