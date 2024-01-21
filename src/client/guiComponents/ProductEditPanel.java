package client.guiComponents;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.GuiUtil;

public class ProductEditPanel extends JPanel {
    public ProductName name;
    public ProductQuantity qty;
    public ProductDescription desc;
    public JButton button;
    public JTextField id = new JTextField(10);

    public ProductEditPanel() {
        super();
        this.id = new JTextField(10);
        this.name = new ProductName();
        this.qty = new ProductQuantity();
        this.desc = new ProductDescription();
        this.button = new JButton("Edit");
        GuiUtil.addChildren(this, new JLabel("Product Id"), this.id, this.name, this.desc, this.button);
    }
}
