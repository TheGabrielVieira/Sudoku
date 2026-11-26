package br.com.sudokuproject.ui.custom.buttom;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ResetButton extends JButton {

    public ResetButton(final ActionListener actionListener) {
        this.setText("Reset");
        this.addActionListener(actionListener);
    }
}
