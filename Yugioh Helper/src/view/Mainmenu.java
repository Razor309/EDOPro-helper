package view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import controller.DeckHandler;
import controller.OptionsHandler;
import controller.WhitelistGenerator;
import model.PressableButton;

public class Mainmenu extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int WINDOW_HEIGHT = 800;
	private static final int WINDOW_WIDTH = 800;
	private static Rectangle MID;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OptionsHandler.deserializeOption(new File("./options.json"));
					Mainmenu frame = new Mainmenu();
					frame.setVisible(true);
				} catch (Exception e) {
					ErrorDialog ed = new ErrorDialog(e.getMessage());
					ed.showDialog();
					e.printStackTrace();
					System.exit(0);
				}
			}
		});
	}

	public Mainmenu() throws IOException {
		Toolkit t = Toolkit.getDefaultToolkit();

		Dimension screen = t.getScreenSize();
		MID = new Rectangle(screen.width / 2 - WINDOW_WIDTH / 2, screen.height / 2 - WINDOW_HEIGHT / 2, WINDOW_WIDTH,
				WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(MID);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 10));

		PressableButton theUsualBtn = new PressableButton("Business as usual");
		theUsualBtn.setLayoutDarkGray();

		theUsualBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					DeckHandler.generateDraftExport();
					DeckHandler.generateCards();
					WhitelistGenerator.generateMain();
					if (GraphicalConsole.getMessage() != null) {
						GraphicalConsole.showDialog();
						GraphicalConsole.flush();
					}
				} catch (IOException ioExc) {
					ioExc.printStackTrace();
					ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
					ed.showDialog();
				}
			}
		});

		getContentPane().add(theUsualBtn);

		PressableButton generateTrimmedDeckBtn = new PressableButton("Generate Trimmed Deck");
		generateTrimmedDeckBtn.setLayoutDarkGray();

		generateTrimmedDeckBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					DeckHandler.generateTrimmedDeck();
					if (GraphicalConsole.getMessage() != null) {
						GraphicalConsole.showDialog();
						GraphicalConsole.flush();
					}
				} catch (IOException ioExc) {
					ioExc.printStackTrace();
					ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
					ed.showDialog();
				}
			}
		});

		getContentPane().add(generateTrimmedDeckBtn);

		PressableButton optionsBtn = new PressableButton("Options");
		optionsBtn.setLayoutDarkGray();

		optionsBtn.addActionListener(e -> {
			OptionsGUI opgui = new OptionsGUI();
			opgui.setVisible(true);
		});

		getContentPane().add(optionsBtn);

		PressableButton exitBtn = new PressableButton("Exit");
		exitBtn.setLayoutDarkGray();
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				System.exit(0);
			}
		});

		getContentPane().add(exitBtn);
	}
}
