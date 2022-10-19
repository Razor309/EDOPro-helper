package view;

import javax.swing.*;
import java.util.Objects;

public class ErrorDialog {
    private String message;

    public ErrorDialog(String message) {
        this.message = message;
    }

    public void add(Object m) {
        if (Objects.equals(this.message, ""))
            this.message = "" + m;
        else
            this.message = this.message + "\n" + m;
    }

    public void showDialog() {
        JOptionPane.showMessageDialog(new JFrame(), this.message, "Dialog", JOptionPane.ERROR_MESSAGE);
    }
}