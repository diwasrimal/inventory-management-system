package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.border.Border;
import java.io.IOException;

class ClientGui {
    final int height = 500;
    final int width = 800;
    final ClientSock conn;
    final Border border = BorderFactory.createLineBorder(Color.black);

    ClientGui(ClientSock conn) {
        this.conn = conn;
    }

    void show() {
        JFrame frame = new JFrame("Inventory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);

        // Panel for adding new product in inventory
        JPanel addPanel = new JPanel();

        final int textFieldCols = 15;
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
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                try {
                    String name = nameField.getText().trim();
                    String desc = descArea.getText().trim();
                    if (name.isEmpty())
                        throw new Exception("Invalid name");
                    int quantity = Integer.parseInt(qtyField.getText());
                    if (quantity < 1)
                        throw new Exception("Invalid quantity");

                    ClientGui.this.conn.sendProductAddRequest(name, quantity, desc);

                } catch (Exception ex) {
                    String msg = ex instanceof NumberFormatException ? "Invalid quantity" : ex.getMessage();
                    JOptionPane.showMessageDialog(frame, msg);
                }
            }
        });

        // Add everything to main frame
        addChildren(addPanel, namePanel, qtyPanel, descPanel, addButton);

        // Close the connection when window closes
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                // Close the socket when the window is closing
                try {
                    conn.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Error while trying to close client socket from GUI");
                }
            }
        });

        frame.add(addPanel);
        frame.setVisible(true);
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