package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.border.Border;

import messages.ProductListRequest;
import utils.Product;

class ClientGui {
    final ClientSock conn;
    private JFrame frame;
    private CardLayout card;
    private final int height = 500;
    private final int width = 800;
    private final int textFieldCols = 15;
    private final Border border = BorderFactory.createLineBorder(Color.black);

    ClientGui(ClientSock conn) {
        this.conn = conn;
    }

    /**
     * Shows the gui by showing main frame
     */
    void show() {
        this.frame = new JFrame("Inventory Management System");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(this.width, this.height);

        this.card = new CardLayout();
        this.frame.setLayout(this.card);

        // Normal panel (First view)
        JPanel mainPanel = makeMainPanel();
        this.frame.add("mainPanel", mainPanel);

        // Panel for adding new product in inventory
        JPanel addPanel = makeAddPanel();
        this.frame.add("addPanel", addPanel);

        // Close the connection when window closes
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                conn.close();
            }
        });

        this.frame.setVisible(true);
    }

    /**
     * Closes the GUI by closing main frame
     */
    void close() {
        this.frame.dispose();
    }

    private JPanel makeMainPanel() {
        JPanel container = new JPanel();
        JButton newProductButton = new JButton("+ New Product");
        JButton refreshButton = new JButton("Refresh");
        newProductButton.addActionListener(e -> this.card.show(this.frame.getContentPane(), "addPanel"));
        refreshButton.addActionListener(e -> this.conn.sendMessage(new ProductListRequest()));

        addChildren(container, newProductButton, refreshButton, new JLabel("Main panel"));
        return container;
    }

    private JPanel makeAddPanel() {
        JPanel container = new JPanel();

        JPanel namePanel = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel("Product Name");
        JTextField nameField = new JTextField(textFieldCols);
        addChildren(namePanel, nameLabel, nameField);

        JPanel qtyPanel = new JPanel(new FlowLayout());
        JLabel qtyLabel = new JLabel("Quantity");
        JTextField qtyField = new JTextField(textFieldCols / 2);
        addChildren(qtyPanel, qtyLabel, qtyField);

        JPanel descPanel = new JPanel(new FlowLayout());
        JLabel descLabel = new JLabel("Description");
        JTextArea descArea = new JTextArea(3, textFieldCols * 3);
        addChildren(descPanel, descLabel, descArea);

        // Button for adding new product to inventory
        JButton addButton = new JButton("Add");
        addButton.addActionListener(ev -> {
            Product prod = getProductFromFields(nameField, qtyField, descArea);
            if (prod != null)
                this.conn.sendProductAddRequest(prod);
        });

        // Button to go back to main panel
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> this.card.show(this.frame.getContentPane(), "mainPanel"));

        addChildren(container, namePanel, qtyPanel, descPanel, addButton, backButton);
        return container;
    }

    /**
     * Makes a {@code Product} object from provided input fields. In case of errors,
     * null is returned.
     */
    private Product getProductFromFields(JTextField nameField, JTextField qtyField, JTextArea descArea) {
        Product prod = null;
        try {
            String name = nameField.getText().trim();
            String desc = descArea.getText().trim();
            if (name.isEmpty())
                throw new Exception("Invalid name");
            int quantity = Integer.parseInt(qtyField.getText());
            if (quantity < 1)
                throw new Exception("Invalid quantity");
            prod =  new Product(name, quantity, desc);
        } catch (Exception e) {
            String msg = e instanceof NumberFormatException ? "Invalid quantity" : e.getMessage();
            showDialog(msg);
        }
        return prod;
    }

    void showDialog(String message) {
        JOptionPane.showMessageDialog(this.frame, message);
    }

    /**
     * Calls the add method multiple times on parent for each child
     */
    private void addChildren(JComponent parent, JComponent... children) {
        for (JComponent child : children) {
            parent.add(child);
        }
    }
}