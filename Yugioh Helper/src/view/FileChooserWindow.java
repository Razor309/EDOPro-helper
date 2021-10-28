package view;

import java.io.File;

import javax.swing.JFileChooser;

public class FileChooserWindow {
	private FileChooserWindow() {
	}

	public static File singleFolderPick() {
		return singleFolderPick(new File("."));
	}

	public static File singleFolderPick(File here) {
		JFileChooser fc = new JFileChooser(here);

		fc.setDialogTitle("Specify a file to save");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int userSelection = fc.showOpenDialog(fc);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fc.getSelectedFile();
			System.out.println(fileToSave.toString());
			return fileToSave;
		}
		return null;
	}

	public static File singleFilePick() {
		return singleFilePick(new File("."));
	}

	public static File singleFilePick(File here) {
		JFileChooser fc = new JFileChooser(here);

		fc.setDialogTitle("Specify a file to save");

		int userSelection = fc.showOpenDialog(fc);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fc.getSelectedFile();
			System.out.println(fileToSave.toString());
			return fileToSave;
		}
		return null;
	}

	public static File[] multiFilePick() {
		return multiFilePick(new File("."));
	}

	public static File[] multiFilePick(File here) {
		JFileChooser fc = new JFileChooser(here);

		fc.setDialogTitle("Specify a file to save");
		fc.setMultiSelectionEnabled(true);

		int userSelection = fc.showOpenDialog(fc);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File[] filesToSave = fc.getSelectedFiles();
			System.out.println(filesToSave.toString());
			return filesToSave;
		}
		return null;
	}

}
