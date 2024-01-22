package client.guiComponents;

import javax.swing.JButton;
import javax.swing.JPanel;

import client.GuiUtil;

public class ProductAddPanel extends JPanel {
    public ProductName name;
    public ProductQuantity qty;
    public ProductDescription desc;
    public JButton button;

    public ProductAddPanel() {
        super();
        this.name = new ProductName();
        this.qty = new ProductQuantity();
        this.desc = new ProductDescription();
        this.button = new JButton("Add");
        GuiUtil.addChildren(this, this.name, this.qty, this.desc, this.button);
    }
}
