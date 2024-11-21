package DataStreams;

import javax.swing.SwingUtilities;

public class DataStreamsRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DataStreamsApp(); // Initialize the GUI application
        });
    }
}
