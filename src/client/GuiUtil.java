package client;

import javax.swing.JComponent;

public class GuiUtil {
    /**
     * Calls the add method multiple times on parent for each child
     */
    public static void addChildren(JComponent parent, JComponent... children) {
        for (JComponent child : children) {
            parent.add(child);
        }
    }
}