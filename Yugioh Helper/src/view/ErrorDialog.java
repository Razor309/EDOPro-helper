package view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ErrorDialog {
	private String message;

	public ErrorDialog() {
	}

	public ErrorDialog(String message) {
		this.message = message;
	}

	public void add(Object m) {
		if (this.message == "")
			this.message = "" + m;
		else
			this.message = this.message + "\n" + m;
	}

	public void flush() {
		this.message = "";
	}

	public void showDialog() {
		JOptionPane.showMessageDialog(new JFrame(), this.message, "Dialog", JOptionPane.ERROR_MESSAGE);
	}
}