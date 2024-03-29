package client;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import messages.ProductEditRequest;
import messages.ProductListRequest;
import utils.Product;


class ClientGui {
    final ClientSock conn;
    private JFrame frame;
    private CardLayout card;
    private Dimension windowSize = new Dimension(1000, 700);
    private final int textFieldCols = 15;
    private JPanel productsGrid;

    // Fields for editing a product, these fields are put in editPage by makeEditPage()
    private JLabel editingId = new JLabel();
    private JTextField editingNameField = new JTextField(this.textFieldCols);
    private JTextField editingQtyField = new JTextField(this.textFieldCols / 2);
    private JTextArea editingDescArea = new JTextArea(3, this.textFieldCols * 3);

    /**
     * Initializes client connection and sets up main GUI frame and its layout
     */
    ClientGui(ClientSock conn) {
        this.conn = conn;

        this.frame = new JFrame("Inventory Management System");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setMinimumSize(this.windowSize);
        this.frame.setSize(this.windowSize);
        this.card = new CardLayout();
        this.productsGrid = new JPanel(new GridLayout(0, 3, 10, 10));
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
     * Fills the product list in GUI, by making a panel for each given
     * product
     */
    void refillProductsPanel(List<Product> products) {
        this.productsGrid.removeAll();
        for (Product prod : products) {
            this.productsGrid.add(makeProductPanel(prod));
        }
        this.productsGrid.revalidate();
        this.productsGrid.repaint();
    }

    /**
     * Makes the main page of GUI. This the first view. It contains buttons for doing
     * other operations and changing views using the card layout used by main frame.
     */
    private JPanel makeMainPage() {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        addPadding(container, 5);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton newProdButton = new JButton("+ New");
        JButton refreshButton = new JButton("Refresh");
        newProdButton.addActionListener(e -> showPage("addPage"));
        refreshButton.addActionListener(e -> this.conn.sendMessage(new ProductListRequest()));
        addChildren(controlPanel, newProdButton, refreshButton);

        // Scrollable grid for showing products
        JScrollPane productList = new JScrollPane(
            this.productsGrid,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        productList.setBorder(BorderFactory.createTitledBorder("Products"));

        container.add(controlPanel, BorderLayout.NORTH);
        container.add(productList, BorderLayout.CENTER);
        return container;
    }

    /**
     * Makes a panel with given product's information
     */
    private JPanel makeProductPanel(Product prod) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(prod.name);
        name.setFont(new Font("Sans", Font.PLAIN, 16));
        JLabel qty = new JLabel(Integer.toString(prod.quantity) + " piece(s)");
        JLabel desc = new JLabel(prod.description);
        Dimension descSize = desc.getPreferredSize();
        descSize.width = 300;
        desc.setPreferredSize(descSize);

        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        edit.addActionListener(e -> editProduct(prod));
        delete.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this.frame, "Sure?", "Warning", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                this.conn.sendProductDeleteRequest(prod.id);
            }
        });

        addChildren(
            panel,
            makePanelWith(name),
            makePanelWith(qty),
            makePanelWith(desc),
            makePanelWith(edit, delete)
        );
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        return panel;
    }

    /**
     * Opens up the editPage by setting the editing fields with given product's
     * information
     */
    private void editProduct(Product prod) {
        this.editingId.setText(Integer.toString(prod.id));
        this.editingNameField.setText(prod.name);
        this.editingQtyField.setText(Integer.toString(prod.quantity));
        this.editingDescArea.setText(prod.description);
        showPage("editPage");
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
            if (prod != null) {
                this.conn.sendProductAddRequest(prod);
                nameField.setText("");
                qtyField.setText("");
                descArea.setText("");
            }
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
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            int prodId = Integer.parseInt(editingId.getText().trim());
            Product newProd = getProductFromFields(
                this.editingNameField, this.editingQtyField, this.editingDescArea
            );
            if (newProd != null)
                this.conn.sendMessage(new ProductEditRequest(prodId, newProd));
        });

        return makePanelWith(
            makePanelWith(new JLabel("Product Id:"), this.editingId),
            makePanelWith(new JLabel("Name"), this.editingNameField),
            makePanelWith(new JLabel("Quantity"), this.editingQtyField),
            makePanelWith(new JLabel("Description"), this.editingDescArea),
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
    private void showPage(String pageLabel) {
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

    private void addPadding(JPanel panel, int pad) {
        panel.setBorder(BorderFactory.createEmptyBorder(pad, pad, pad, pad));
    }
}