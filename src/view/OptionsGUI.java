package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.OptionsHandler;
import model.Options;

public class OptionsGUI extends JFrame {
	/**
	 * Create the frame.
	 */
	public OptionsGUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 300);
		JOptionPane contentPane = new JOptionPane();
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

		JRadioButton displayInfosRadioButton = new JRadioButton("display infos", OptionsHandler.options.displayInfos);
		displayInfosRadioButton.setHorizontalTextPosition(SwingConstants.LEFT);
		displayInfosRadioButton.addActionListener(e -> {
			OptionsHandler.options.displayInfos = !OptionsHandler.options.displayInfos;
			try {
				OptionsHandler.serializeOptions();
			} catch (IOException ex) {
				new ErrorDialog(ex.getMessage()).showDialog();
			}
		});
		GridBagConstraints gbc_displayInfosRadioButton = new GridBagConstraints();
		gbc_displayInfosRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_displayInfosRadioButton.gridx = 0;
		gbc_displayInfosRadioButton.gridy = 0;
		contentPane.add(displayInfosRadioButton, gbc_displayInfosRadioButton);

		JRadioButton applyBanlistRadioButton = new JRadioButton("apply banlist", OptionsHandler.options.applyBanlist);
		applyBanlistRadioButton.setHorizontalTextPosition(SwingConstants.LEFT);
		applyBanlistRadioButton.addActionListener(e -> {
			OptionsHandler.options.applyBanlist = !OptionsHandler.options.applyBanlist;
			try {
				OptionsHandler.serializeOptions();
			} catch (IOException ex) {
				new ErrorDialog(ex.getMessage()).showDialog();
			}
		});
		GridBagConstraints gbc_applyBanlistRadioButton = new GridBagConstraints();
		gbc_applyBanlistRadioButton.insets = new Insets(0, 0, 5, 0);
		gbc_applyBanlistRadioButton.gridx = 2;
		gbc_applyBanlistRadioButton.gridy = 0;
		contentPane.add(applyBanlistRadioButton, gbc_applyBanlistRadioButton);

		JLabel lblNewLabel = new JLabel("Paths");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 2;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		JLabel banlistLabel = new JLabel("banlist:");
		GridBagConstraints gbc_banlistLabel = new GridBagConstraints();
		gbc_banlistLabel.insets = new Insets(0, 0, 5, 5);
		gbc_banlistLabel.gridx = 0;
		gbc_banlistLabel.gridy = 7;
		contentPane.add(banlistLabel, gbc_banlistLabel);

		JLabel banlistPathLabel = new JLabel(OptionsHandler.options.paths.get("banlist"));
		GridBagConstraints gbc_banlistPathLabel = new GridBagConstraints();
		gbc_banlistPathLabel.insets = new Insets(0, 0, 5, 5);
		gbc_banlistPathLabel.gridx = 1;
		gbc_banlistPathLabel.gridy = 7;
		contentPane.add(banlistPathLabel, gbc_banlistPathLabel);

		JButton banlistChooseButton = new JButton("Choose");
		banlistChooseButton.addActionListener(e -> {
			try {
				OptionsHandler.options.paths.put("banlist", Objects.requireNonNull(FileChooserWindow
						.singleFilePick(new File(OptionsHandler.options.paths.get("banlist")))).getPath());
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

		JLabel goodcardsLabel = new JLabel("goodcards:");
		GridBagConstraints gbc_goodcardsLabel = new GridBagConstraints();
		gbc_goodcardsLabel.insets = new Insets(0, 0, 5, 5);
		gbc_goodcardsLabel.gridx = 0;
		gbc_goodcardsLabel.gridy = 8;
		contentPane.add(goodcardsLabel, gbc_goodcardsLabel);

		JLabel goodcardsPathLabel = new JLabel(OptionsHandler.options.paths.get("goodcards"));
		GridBagConstraints gbc_goodcardsPathLabel = new GridBagConstraints();
		gbc_goodcardsPathLabel.insets = new Insets(0, 0, 5, 5);
		gbc_goodcardsPathLabel.gridx = 1;
		gbc_goodcardsPathLabel.gridy = 8;
		contentPane.add(goodcardsPathLabel, gbc_goodcardsPathLabel);

		JButton goodcardsChooseButton = new JButton("Choose");
		goodcardsChooseButton.addActionListener(e -> {
			try {
				OptionsHandler.options.paths.put("goodcards", Objects.requireNonNull(FileChooserWindow
						.singleFilePick(new File(OptionsHandler.options.paths.get("goodcards")))).getPath());
				OptionsHandler.serializeOptions();
			} catch (IOException ioExc) {
				ioExc.printStackTrace();
				ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
				ed.showDialog();
			}
		});
		GridBagConstraints gbc_goodcardsChooseButton = new GridBagConstraints();
		gbc_goodcardsChooseButton.insets = new Insets(0, 0, 5, 0);
		gbc_goodcardsChooseButton.gridx = 2;
		gbc_goodcardsChooseButton.gridy = 8;
		contentPane.add(goodcardsChooseButton, gbc_goodcardsChooseButton);

		JLabel whitelistFolderLabel = new JLabel("whitelist folder:");
		GridBagConstraints gbc_whitelistFolderLabel = new GridBagConstraints();
		gbc_whitelistFolderLabel.insets = new Insets(0, 0, 5, 5);
		gbc_whitelistFolderLabel.gridx = 0;
		gbc_whitelistFolderLabel.gridy = 9;
		contentPane.add(whitelistFolderLabel, gbc_whitelistFolderLabel);

		JLabel whitelistFolderPathLabel = new JLabel(OptionsHandler.options.paths.get("whitelist folder"));
		GridBagConstraints gbc_whitelistFolderPathLabel = new GridBagConstraints();
		gbc_whitelistFolderPathLabel.insets = new Insets(0, 0, 5, 5);
		gbc_whitelistFolderPathLabel.gridx = 1;
		gbc_whitelistFolderPathLabel.gridy = 9;
		contentPane.add(whitelistFolderPathLabel, gbc_whitelistFolderPathLabel);

		JButton whitelistFolderChooseButton = new JButton("Choose");
		whitelistFolderChooseButton.addActionListener(e -> {
			try {
				OptionsHandler.options.paths.put("whitelist folder", Objects.requireNonNull(FileChooserWindow
						.singleFolderPick(new File(OptionsHandler.options.paths.get("whitelist folder")))).getPath());
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
		gbc_whitelistFolderChooseButton.gridy = 9;
		contentPane.add(whitelistFolderChooseButton, gbc_whitelistFolderChooseButton);

		JLabel draftFolderLabel = new JLabel("draft folder:");
		GridBagConstraints gbc_draftFolderLabel = new GridBagConstraints();
		gbc_draftFolderLabel.insets = new Insets(0, 0, 0, 5);
		gbc_draftFolderLabel.gridx = 0;
		gbc_draftFolderLabel.gridy = 10;
		contentPane.add(draftFolderLabel, gbc_draftFolderLabel);

		JLabel draftFolderPathLabel = new JLabel(OptionsHandler.options.paths.get("draft folder"));
		GridBagConstraints gbc_draftFolderPathLabel = new GridBagConstraints();
		gbc_draftFolderPathLabel.insets = new Insets(0, 0, 0, 5);
		gbc_draftFolderPathLabel.gridx = 1;
		gbc_draftFolderPathLabel.gridy = 10;
		contentPane.add(draftFolderPathLabel, gbc_draftFolderPathLabel);

		JButton draftFolderChooseButton = new JButton("Choose");
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
