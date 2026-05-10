package dao;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Output {

    private static JTextArea area;

    public static void setArea(JTextArea a) {
        area = a;
    }

    public static void println(String text) {
        if (area != null) {
            if (SwingUtilities.isEventDispatchThread()) {
                area.append(text + "\n");
            } else {
                SwingUtilities.invokeLater(() -> area.append(text + "\n"));
            }
        } else {
            System.out.println(text);
        }
    }

    public static void clear() {
        if (area != null) {
            if (SwingUtilities.isEventDispatchThread()) {
                area.setText("");
            } else {
                SwingUtilities.invokeLater(() -> area.setText(""));
            }
        }
    }
}