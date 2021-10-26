package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class PressableButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1118625206930429167L;
	private Color hoverBackgroundColor;
	private Color pressedBackgroundColor;
	private boolean state = true;
	private ActionListener a1, a2;

	public PressableButton() {
		this(null);
	}

	public PressableButton(String text) {
		super(text);
		super.setContentAreaFilled(false);
	}

	public void setLayoutDarkGray() {
		setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 60));
		setForeground(Color.WHITE);
		setBackground(Color.DARK_GRAY);
		setPressedBackgroundColor(Color.DARK_GRAY.darker());
		setHoverBackgroundColor(Color.DARK_GRAY.brighter());
		setFocusPainted(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (getModel().isPressed()) {
			if (this.pressedBackgroundColor != null)
				g.setColor(pressedBackgroundColor);
			else
				g.setColor(getBackground());
		} else if (getModel().isRollover()) {
			if (this.hoverBackgroundColor != null)
				g.setColor(hoverBackgroundColor);
			else
				g.setColor(getBackground());
		} else {
			g.setColor(getBackground());
		}
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	public void setActionListeners(ActionListener a1, ActionListener a2) {
		this.a1 = a1;
		this.a2 = a2;
	}

	public void update() {
		if (state) {
			removeActionListener(a1);
			addActionListener(a2);
			state = false;
		} else {
			removeActionListener(a2);
			addActionListener(a1);
			state = true;
		}
	}

	public Color getHoverBackgroundColor() {
		return hoverBackgroundColor;
	}

	public void setHoverBackgroundColor(Color hoverBackgroundColor) {
		this.hoverBackgroundColor = hoverBackgroundColor;
	}

	public Color getPressedBackgroundColor() {
		return pressedBackgroundColor;
	}

	public void setPressedBackgroundColor(Color pressedBackgroundColor) {
		this.pressedBackgroundColor = pressedBackgroundColor;
	}
}
