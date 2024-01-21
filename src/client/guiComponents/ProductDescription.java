package client.guiComponents;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import client.GuiUtil;

public class ProductDescription extends JPanel {
    public JTextArea field;

    public ProductDescription() {
        super();
        this.field = new JTextArea(3, 45);
        GuiUtil.addChildren(this, new JLabel("Description"), this.field);
    }
}
