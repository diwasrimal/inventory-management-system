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

    /**
     * Initializes client connection and sets up main GUI frame and its layout
     */
    ClientGui(ClientSock conn) {
        this.conn = conn;

        this.frame = new JFrame("Inventory Management System");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(this.width, this.height);
        this.card = new CardLayout();
        this.frame.setLayout(this.card);
    }

    /**
     * Shows the gui by showing main frame
     */
    void show() {
        JPanel mainPage = makeMainPage();
        JPanel addPage = makeAddPage();
        JPanel editPage = makeEditPage();

        this.frame.add("mainPage", mainPage);
        this.frame.add("addPage", addPage);
        this.frame.add("editPage", editPage);

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

    /**
     * Makes the main page of GUI. This the first view. It contains buttons for doing
     * other operations and changing views using the card layout used by main frame.
     */
    private JPanel makeMainPage() {
        JPanel container = new JPanel();
        JButton newProdButton = new JButton("+ New Product");
        JButton editProdButton = new JButton("Edit Product");
        JButton refreshButton = new JButton("Refresh");

        newProdButton.addActionListener(e -> showPage("addPage"));
        editProdButton.addActionListener(e -> showPage("editPage"));
        refreshButton.addActionListener(e -> this.conn.sendMessage(new ProductListRequest()));
        addChildren(container, newProdButton, editProdButton, refreshButton, new JLabel("Main panel"));
        return container;
    }

    /**
     * Makes a panel for adding a new product entry
     */
    private JPanel makeAddPage() {
        JTextField nameField = new JTextField(this.textFieldCols);
        JTextField qtyField = new JTextField(this.textFieldCols / 2);
        JTextArea descArea = new JTextArea(3, this.textFieldCols * 3);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            Product prod = getProductFromFields(nameField, qtyField, descArea);
            if (prod != null)
                this.conn.sendProductAddRequest(prod);
        });

        return makePanelWith(
            makePanelWith(new JLabel("Name"), nameField),
            makePanelWith(new JLabel("Quantity"), qtyField),
            makePanelWith(new JLabel("Description"), descArea),
            addButton,
            makeBackButton()
        );
    }

    /**
     * Makes a panel for editing existing product
     */
    private JPanel makeEditPage() {
        JTextField idField = new JTextField(this.textFieldCols / 2);
        JTextField nameField = new JTextField(this.textFieldCols);
        JTextField qtyField = new JTextField(this.textFieldCols / 2);
        JTextArea descArea = new JTextArea(3, this.textFieldCols * 3);

        JButton editButton = new JButton("Add");
        editButton.addActionListener(e -> {
            String prodId = idField.getText().trim();
            Product newProd = getProductFromFields(nameField, qtyField, descArea);
            // TODO: Send product edit request
        });

        return makePanelWith(
            makePanelWith(new JLabel("Product Id"), idField),
            makePanelWith(new JLabel("Name"), nameField),
            makePanelWith(new JLabel("Quantity"), qtyField),
            makePanelWith(new JLabel("Description"), descArea),
            editButton,
            makeBackButton()
        );
    }

    /**
     * Makes a button that navigates back to main page
     */
    private JButton makeBackButton() {
        JButton back = new JButton("Back");
        back.addActionListener(e -> showPage("mainPage"));
        return back;
    }

    /**
     * Shows one of the panels that were added to main frame
     * using card layout.
     */
    void showPage(String pageLabel) {
        this.card.show(this.frame.getContentPane(), pageLabel);
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
     * Adds the given children to parent, by calling add() method
     * multiple times
     */
    private void addChildren(JComponent parent, JComponent... children) {
        for (JComponent child : children) {
            parent.add(child);
        }
    }

    /**
     * Makes a new JPanel object with normal layout, adding the provided
     * components
     */
    private JPanel makePanelWith(JComponent... children) {
        JPanel parent = new JPanel();
        addChildren(parent, children);
        return parent;
    }
}