package client.guiComponents;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.GuiUtil;

public class ProductQuantity extends JPanel {
    public JTextField field;

    public ProductQuantity() {
        super();
        this.field = new JTextField(10);
        GuiUtil.addChildren(this, new JLabel("Quantity"), this.field);
    }
}
