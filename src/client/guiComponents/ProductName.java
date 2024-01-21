package client.guiComponents;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.GuiUtil;

public class ProductName extends JPanel {
    public JTextField field;

    public ProductName() {
        super();
        this.field = new JTextField(15);
        GuiUtil.addChildren(this, new JLabel("Product Name"), this.field);
    }
}