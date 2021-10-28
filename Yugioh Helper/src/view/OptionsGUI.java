package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.OptionsHandler;

public class OptionsGUI extends JFrame {

	private JOptionPane contentPane;
	private JLabel allcardsLabel;
	private JLabel cardsLabel;
	private JLabel draftExportLabel;
	private JLabel trimmeddeckLabel;
	private JLabel banlistLabel;
	private JLabel whitelistFolderLabel;
	private JLabel deckFolderLabel;
	private JLabel draftFolderLabel;
	private JLabel allcardsPathLabel;
	private JLabel cardsPathLabel;
	private JLabel draftExportPathLabel;
	private JLabel trimmeddeckPathLabel;
	private JLabel banlistPathLabel;
	private JLabel whitelistFolderPathLabel;
	private JLabel deckFolderPathLabel;
	private JLabel draftFolderPathLabel;
	private JLabel lblNewLabel;
	private JButton allcardsChooseButton;
	private JButton cardsChooseButton;
	private JButton draftExportChooseButton;
	private JButton trimmeddeckChooseButton;
	private JButton banlistChooseButton;
	private JButton whitelistFolderChooseButton;
	private JButton deckFolderChooseButton;
	private JButton draftFolderChooseButton;

	/**
	 * Create the frame.
	 */
	public OptionsGUI() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 389, 357);
		contentPane = new JOptionPane();
		contentPane.removeAll();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 21, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JRadioButton displayInfosRadioButton = new JRadioButton("display infos");
		displayInfosRadioButton.setHorizontalTextPosition(SwingConstants.LEFT);
		GridBagConstraints gbc_displayInfosRadioButton = new GridBagConstraints();
		gbc_displayInfosRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_displayInfosRadioButton.gridx = 0;
		gbc_displayInfosRadioButton.gridy = 0;
		contentPane.add(displayInfosRadioButton, gbc_displayInfosRadioButton);

		lblNewLabel = new JLabel("Paths");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 2;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		allcardsLabel = new JLabel("allcards:");
		GridBagConstraints gbc_allcardsLabel = new GridBagConstraints();
		gbc_allcardsLabel.insets = new Insets(0, 0, 5, 5);
		gbc_allcardsLabel.gridx = 0;
		gbc_allcardsLabel.gridy = 3;
		contentPane.add(allcardsLabel, gbc_allcardsLabel);

		allcardsPathLabel = new JLabel(OptionsHandler.options.paths.get("allcards"));
		GridBagConstraints gbc_allcardsPathLabel_9 = new GridBagConstraints();
		gbc_allcardsPathLabel_9.insets = new Insets(0, 0, 5, 5);
		gbc_allcardsPathLabel_9.gridx = 1;
		gbc_allcardsPathLabel_9.gridy = 3;
		contentPane.add(allcardsPathLabel, gbc_allcardsPathLabel_9);

		allcardsChooseButton = new JButton("Choose");
		allcardsChooseButton.addActionListener(e -> {
			try {
				OptionsHandler.options.paths.put("allcards", FileChooserWindow.singleFilePick().getPath());
				OptionsHandler.serializeOptions();
			} catch (IOException ioExc) {
				ioExc.printStackTrace();
				ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
				ed.showDialog();
			}
		});
		GridBagConstraints gbc_allcardsChooseButton = new GridBagConstraints();
		gbc_allcardsChooseButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_allcardsChooseButton.insets = new Insets(0, 0, 5, 0);
		gbc_allcardsChooseButton.gridx = 2;
		gbc_allcardsChooseButton.gridy = 3;
		contentPane.add(allcardsChooseButton, gbc_allcardsChooseButton);

		cardsLabel = new JLabel("cards:");
		GridBagConstraints gbc_cardsLabel = new GridBagConstraints();
		gbc_cardsLabel.insets = new Insets(0, 0, 5, 5);
		gbc_cardsLabel.gridx = 0;
		gbc_cardsLabel.gridy = 4;
		contentPane.add(cardsLabel, gbc_cardsLabel);

		cardsPathLabel = new JLabel(OptionsHandler.options.paths.get("cards"));
		GridBagConstraints gbc_cardsPathLabel = new GridBagConstraints();
		gbc_cardsPathLabel.insets = new Insets(0, 0, 5, 5);
		gbc_cardsPathLabel.gridx = 1;
		gbc_cardsPathLabel.gridy = 4;
		contentPane.add(cardsPathLabel, gbc_cardsPathLabel);

		cardsChooseButton = new JButton("Choose");
		cardsChooseButton.addActionListener(e -> {
			try {
				OptionsHandler.options.paths.put("cards", FileChooserWindow.singleFilePick().getPath());
				OptionsHandler.serializeOptions();
			} catch (IOException ioExc) {
				ioExc.printStackTrace();
				ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
				ed.showDialog();
			}
		});
		GridBagConstraints gbc_cardsChooseButton = new GridBagConstraints();
		gbc_cardsChooseButton.insets = new Insets(0, 0, 5, 0);
		gbc_cardsChooseButton.gridx = 2;
		gbc_cardsChooseButton.gridy = 4;
		contentPane.add(cardsChooseButton, gbc_cardsChooseButton);

		draftExportLabel = new JLabel("draft export:");
		GridBagConstraints gbc_draftExportLabel = new GridBagConstraints();
		gbc_draftExportLabel.insets = new Insets(0, 0, 5, 5);
		gbc_draftExportLabel.gridx = 0;
		gbc_draftExportLabel.gridy = 5;
		contentPane.add(draftExportLabel, gbc_draftExportLabel);

		draftExportPathLabel = new JLabel(OptionsHandler.options.paths.get("draft export"));
		GridBagConstraints gbc_draftExportPathLabel = new GridBagConstraints();
		gbc_draftExportPathLabel.insets = new Insets(0, 0, 5, 5);
		gbc_draftExportPathLabel.gridx = 1;
		gbc_draftExportPathLabel.gridy = 5;
		contentPane.add(draftExportPathLabel, gbc_draftExportPathLabel);

		draftExportChooseButton = new JButton("Choose");
		draftExportChooseButton.addActionListener(e -> {
			try {
				OptionsHandler.options.paths.put("draft export", FileChooserWindow.singleFilePick().getPath());
				OptionsHandler.serializeOptions();
			} catch (IOException ioExc) {
				ioExc.printStackTrace();
				ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
				ed.showDialog();
			}
		});
		GridBagConstraints gbc_draftExportChooseButton = new GridBagConstraints();
		gbc_draftExportChooseButton.fill = GridBagConstraints.BOTH;
		gbc_draftExportChooseButton.insets = new Insets(0, 0, 5, 0);
		gbc_draftExportChooseButton.gridx = 2;
		gbc_draftExportChooseButton.gridy = 5;
		contentPane.add(draftExportChooseButton, gbc_draftExportChooseButton);

		trimmeddeckLabel = new JLabel("trimmeddeck:");
		GridBagConstraints gbc_trimmeddeckLabel = new GridBagConstraints();
		gbc_trimmeddeckLabel.insets = new Insets(0, 0, 5, 5);
		gbc_trimmeddeckLabel.gridx = 0;
		gbc_trimmeddeckLabel.gridy = 6;
		contentPane.add(trimmeddeckLabel, gbc_trimmeddeckLabel);

		trimmeddeckPathLabel = new JLabel(OptionsHandler.options.paths.get("trimmeddeck"));
		GridBagConstraints gbc_trimmeddeckPathLabel = new GridBagConstraints();
		gbc_trimmeddeckPathLabel.insets = new Insets(0, 0, 5, 5);
		gbc_trimmeddeckPathLabel.gridx = 1;
		gbc_trimmeddeckPathLabel.gridy = 6;
		contentPane.add(trimmeddeckPathLabel, gbc_trimmeddeckPathLabel);

		trimmeddeckChooseButton = new JButton("Choose");
		trimmeddeckChooseButton.addActionListener(e -> {
			try {
				OptionsHandler.options.paths.put("trimmeddeck", FileChooserWindow.singleFilePick().getPath());
				OptionsHandler.serializeOptions();
			} catch (IOException ioExc) {
				ioExc.printStackTrace();
				ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
				ed.showDialog();
			}
		});
		GridBagConstraints gbc_trimmeddeckChooseButton = new GridBagConstraints();
		gbc_trimmeddeckChooseButton.insets = new Insets(0, 0, 5, 0);
		gbc_trimmeddeckChooseButton.gridx = 2;
		gbc_trimmeddeckChooseButton.gridy = 6;
		contentPane.add(trimmeddeckChooseButton, gbc_trimmeddeckChooseButton);

		banlistLabel = new JLabel("banlist:");
		GridBagConstraints gbc_banlistLabel = new GridBagConstraints();
		gbc_banlistLabel.insets = new Insets(0, 0, 5, 5);
		gbc_banlistLabel.gridx = 0;
		gbc_banlistLabel.gridy = 7;
		contentPane.add(banlistLabel, gbc_banlistLabel);

		banlistPathLabel = new JLabel(OptionsHandler.options.paths.get("banlist"));
		GridBagConstraints gbc_banlistPathLabel = new GridBagConstraints();
		gbc_banlistPathLabel.insets = new Insets(0, 0, 5, 5);
		gbc_banlistPathLabel.gridx = 1;
		gbc_banlistPathLabel.gridy = 7;
		contentPane.add(banlistPathLabel, gbc_banlistPathLabel);

		banlistChooseButton = new JButton("Choose");
		banlistChooseButton.addActionListener(e -> {
			try {
				OptionsHandler.options.paths.put("banlist", FileChooserWindow
						.singleFilePick(new File(OptionsHandler.options.paths.get("banlist"))).getPath());
				OptionsHandler.serializeOptions();
			} catch (IOException ioExc) {
				ioExc.printStackTrace();
				ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
				ed.showDialog();
			}
		});
		GridBagConstraints gbc_banlistChooseButton = new GridBagConstraints();
		gbc_banlistChooseButton.insets = new Insets(0, 0, 5, 0);
		gbc_banlistChooseButton.gridx = 2;
		gbc_banlistChooseButton.gridy = 7;
		contentPane.add(banlistChooseButton, gbc_banlistChooseButton);

		whitelistFolderLabel = new JLabel("whitelist folder:");
		GridBagConstraints gbc_whitelistFolderLabel = new GridBagConstraints();
		gbc_whitelistFolderLabel.insets = new Insets(0, 0, 5, 5);
		gbc_whitelistFolderLabel.gridx = 0;
		gbc_whitelistFolderLabel.gridy = 8;
		contentPane.add(whitelistFolderLabel, gbc_whitelistFolderLabel);

		whitelistFolderPathLabel = new JLabel(OptionsHandler.options.paths.get("whitelist folder"));
		GridBagConstraints gbc_whitelistFolderPathLabel = new GridBagConstraints();
		gbc_whitelistFolderPathLabel.insets = new Insets(0, 0, 5, 5);
		gbc_whitelistFolderPathLabel.gridx = 1;
		gbc_whitelistFolderPathLabel.gridy = 8;
		contentPane.add(whitelistFolderPathLabel, gbc_whitelistFolderPathLabel);

		whitelistFolderChooseButton = new JButton("Choose");
		whitelistFolderChooseButton.addActionListener(e -> {
			try {
				OptionsHandler.options.paths.put("whitelist folder", FileChooserWindow
						.singleFolderPick(new File(OptionsHandler.options.paths.get("whitelist folder"))).getPath());
				OptionsHandler.serializeOptions();
			} catch (IOException ioExc) {
				ioExc.printStackTrace();
				ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
				ed.showDialog();
			}
		});
		GridBagConstraints gbc_whitelistFolderChooseButton = new GridBagConstraints();
		gbc_whitelistFolderChooseButton.insets = new Insets(0, 0, 5, 0);
		gbc_whitelistFolderChooseButton.gridx = 2;
		gbc_whitelistFolderChooseButton.gridy = 8;
		contentPane.add(whitelistFolderChooseButton, gbc_whitelistFolderChooseButton);

		deckFolderLabel = new JLabel("deck folder:");
		GridBagConstraints gbc_deckFolderLabel = new GridBagConstraints();
		gbc_deckFolderLabel.insets = new Insets(0, 0, 5, 5);
		gbc_deckFolderLabel.gridx = 0;
		gbc_deckFolderLabel.gridy = 9;
		contentPane.add(deckFolderLabel, gbc_deckFolderLabel);

		deckFolderPathLabel = new JLabel(OptionsHandler.options.paths.get("deck folder"));
		GridBagConstraints gbc_deckFolderPathLabel = new GridBagConstraints();
		gbc_deckFolderPathLabel.insets = new Insets(0, 0, 5, 5);
		gbc_deckFolderPathLabel.gridx = 1;
		gbc_deckFolderPathLabel.gridy = 9;
		contentPane.add(deckFolderPathLabel, gbc_deckFolderPathLabel);

		deckFolderChooseButton = new JButton("Choose");
		deckFolderChooseButton.addActionListener(e -> {
			try {
				OptionsHandler.options.paths.put("deck folder", FileChooserWindow.singleFolderPick().getPath());
				OptionsHandler.serializeOptions();
			} catch (IOException ioExc) {
				ioExc.printStackTrace();
				ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
				ed.showDialog();
			}
		});
		GridBagConstraints gbc_deckFolderChooseButton = new GridBagConstraints();
		gbc_deckFolderChooseButton.insets = new Insets(0, 0, 5, 0);
		gbc_deckFolderChooseButton.gridx = 2;
		gbc_deckFolderChooseButton.gridy = 9;
		contentPane.add(deckFolderChooseButton, gbc_deckFolderChooseButton);

		draftFolderLabel = new JLabel("draft folder:");
		GridBagConstraints gbc_draftFolderLabel = new GridBagConstraints();
		gbc_draftFolderLabel.insets = new Insets(0, 0, 0, 5);
		gbc_draftFolderLabel.gridx = 0;
		gbc_draftFolderLabel.gridy = 10;
		contentPane.add(draftFolderLabel, gbc_draftFolderLabel);

		draftFolderPathLabel = new JLabel(OptionsHandler.options.paths.get("draft folder"));
		GridBagConstraints gbc_draftFolderPathLabel = new GridBagConstraints();
		gbc_draftFolderPathLabel.insets = new Insets(0, 0, 0, 5);
		gbc_draftFolderPathLabel.gridx = 1;
		gbc_draftFolderPathLabel.gridy = 10;
		contentPane.add(draftFolderPathLabel, gbc_draftFolderPathLabel);

		draftFolderChooseButton = new JButton("Choose");
		draftFolderChooseButton.addActionListener(e -> {
			try {
				OptionsHandler.options.paths.put("draft folder", FileChooserWindow.singleFolderPick().getPath());
				OptionsHandler.serializeOptions();
			} catch (IOException ioExc) {
				ioExc.printStackTrace();
				ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
				ed.showDialog();
			}
		});
		GridBagConstraints gbc_draftFolderChooseButton = new GridBagConstraints();
		gbc_draftFolderChooseButton.gridx = 2;
		gbc_draftFolderChooseButton.gridy = 10;
		contentPane.add(draftFolderChooseButton, gbc_draftFolderChooseButton);
	}

}
