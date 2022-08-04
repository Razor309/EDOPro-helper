package view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import controller.OptionsHandler;
import controller.WhitelistGenerator;
import model.PressableButton;

public class Mainmenu extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int WINDOW_HEIGHT = 800;
	private static final int WINDOW_WIDTH = 800;

	public static void execute() {
		EventQueue.invokeLater(() -> {
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
		});
	}

	public Mainmenu() {
		Toolkit t = Toolkit.getDefaultToolkit();

		Dimension screen = t.getScreenSize();
		Rectangle MID = new Rectangle(screen.width / 2 - WINDOW_WIDTH / 2, screen.height / 2 - WINDOW_HEIGHT / 2, WINDOW_WIDTH,
				WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(MID);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 10));

		PressableButton theUsualBtn = new PressableButton("Business as usual");
		theUsualBtn.setLayoutDarkGray();

		theUsualBtn.addActionListener(e -> {
			try {
				WhitelistGenerator.generateGeneral();
				WhitelistGenerator.generateGoodcards();
				WhitelistGenerator.generateTrimmedGoodcards();
				if (GraphicalConsole.getMessage() != null) {
					GraphicalConsole.showDialog();
					GraphicalConsole.flush();
				}
			} catch (IOException ioExc) {
				ioExc.printStackTrace();
				ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
				ed.showDialog();
			}
		});

		getContentPane().add(theUsualBtn);

		PressableButton generateDraftWhitelistBtn = new PressableButton("Generate draft whitelists");
		generateDraftWhitelistBtn.setLayoutDarkGray();

		generateDraftWhitelistBtn.addActionListener(e -> {
			try {
				WhitelistGenerator.generateDraftWhitelists();
				if (GraphicalConsole.getMessage() != null) {
					GraphicalConsole.showDialog();
					GraphicalConsole.flush();
				}
			} catch (IOException ioExc) {
				ioExc.printStackTrace();
				ErrorDialog ed = new ErrorDialog(ioExc.getMessage());
				ed.showDialog();
			}
		});

		getContentPane().add(generateDraftWhitelistBtn);

		PressableButton optionsBtn = new PressableButton("Options");
		optionsBtn.setLayoutDarkGray();

		optionsBtn.addActionListener(e -> {
			OptionsGUI opgui = new OptionsGUI();
			opgui.setVisible(true);
		});

		getContentPane().add(optionsBtn);

		PressableButton exitBtn = new PressableButton("Exit");
		exitBtn.setLayoutDarkGray();
		exitBtn.addActionListener(e -> System.exit(0));

		getContentPane().add(exitBtn);
	}
}
