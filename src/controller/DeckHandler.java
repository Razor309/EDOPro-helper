package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.YGODeck;
import view.ErrorDialog;
import view.GraphicalConsole;

public class DeckHandler {
	public final static Path draftFolder = Paths.get(OptionsHandler.options.paths.get("draft folder"));
	private static YGODeck allcardsDeck;
	private static YGODeck goodcardsDeck;
	private static boolean generated;

	private DeckHandler() {
	}

	public static void generateDraftWhitelists() throws IOException {
		generated = false;
		try (Stream<Path> paths = Files.walk(draftFolder)) {
			paths.filter(Files::isRegularFile).filter(path -> {
				boolean whitelisted = true;
				if (OptionsHandler.options.draftExporterFilter.get("whitelist").size() != 0) {
					// Check if file is whitelisted
					for (String s : OptionsHandler.options.draftExporterFilter.get("whitelist")) {
						//
						whitelisted = path.toString().contains(s);
					}
				}
				// if the file is not in the non-empty whitelist return false
				if (!whitelisted)
					return false;
				// If there was no whitelist or the file is whitelisted -> Check if file is
				// blacklisted
				for (String s : OptionsHandler.options.draftExporterFilter.get("blacklist")) {
					if (path.toString().contains(s))
						return false;
				}
				return true;
			}).forEach(path -> {
				try {
					Path out = Paths.get(draftFolder + "\\" + OptionsHandler.options.draftExporterExtensions.get("prefix")
							+ path.getFileName() + ".conf" + OptionsHandler.options.draftExporterExtensions.get("suffix"));
					GraphicalConsole.add("Exported : " + path.toFile() + "\nas whitelist to\n" + out + "\n");
					WhitelistGenerator.generateTo(out, DeckHandler.importFromFile(path));
					generated = true;
				} catch (Exception e) {
					ErrorDialog ed = new ErrorDialog(e.getMessage());
					ed.showDialog();
				}
			});
			if (!generated)
				GraphicalConsole.add("There was nothing to generate...");
		}

	}

	private static Stream<Path> getYdkPaths(Path dir) throws IOException {
		return Files.walk(dir)
				.filter(Files::isRegularFile)
				.filter(path -> path.toString().startsWith(".ydk", path.toString().length() - 4));
	}

	private static Stream<Integer> getCardCodes(Stream<String> lines){
		return lines.map(line -> {
			try {
				return Integer.parseInt(line.trim());
			} catch (NumberFormatException ignored) {
				return null;
			}
		}).filter(Objects::nonNull);
	}

	public static YGODeck getIntersectingDeck(YGODeck d1, YGODeck d2) {
		Map<Integer, Integer> map = d1.keySet().stream().filter(d2::containsKey).collect(Collectors.toMap(k -> k, d1::get));
		return YGODeck.getCastedInstance((HashMap<Integer, Integer>) map);
	}

	public static YGODeck getAllcardsDeck() throws IOException {
		if (allcardsDeck == null)
			allcardsDeck = importFromDir(draftFolder);
		return allcardsDeck;
	}

	public static YGODeck getGoodcardsDeck() throws IOException {
		if (goodcardsDeck == null)
			goodcardsDeck = importFromDir(draftFolder);
		return goodcardsDeck;
	}

	public static YGODeck importFromDir(Path dir) throws IOException {
		System.out.println(dir.toFile().getCanonicalFile());
		YGODeck deck = new YGODeck(dir);
		Stream<Path> ydkPathStream = getYdkPaths(dir);
		if (ydkPathStream == null) {
			new ErrorDialog("No items in " + dir).showDialog();
		} else {
			System.out.println(ydkPathStream);
		}
		assert ydkPathStream != null;
		ydkPathStream.forEach(path -> {
			System.out.println(path);
			try {
				getCardCodes(Files.lines(path))
						.forEach(cardCode -> {
							if (deck.containsKey(cardCode))
								deck.put(cardCode, deck.get(cardCode) + 1);
							else
								deck.put(cardCode, 1);
						});
			} catch (IOException e) {
				new ErrorDialog(e.getMessage()).showDialog();
			}
		});
		return deck;
	}

	public static YGODeck importFromFile(Path path) throws IOException {
		YGODeck deck = new YGODeck(path);
		getCardCodes(Files.lines(path))
				.forEach(cardCode -> {
					if (deck.containsKey(cardCode))
						deck.put(cardCode, deck.get(cardCode) + 1);
					else
						deck.put(cardCode, 1);});
		return deck;
	}
}
