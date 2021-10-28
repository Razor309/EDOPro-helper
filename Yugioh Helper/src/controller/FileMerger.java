package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class FileMerger {
	private FileMerger() {
	}

	public static void mergeTo(File out, Collection<File> files) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(out));
		for (File f : files) {
			writer.write(f.getName() + "\n");
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line = reader.readLine();
			while (line != null) {
				writer.write(line + "\n");
				line = reader.readLine();
			}
			reader.close();
		}
		writer.close();
	}
}
