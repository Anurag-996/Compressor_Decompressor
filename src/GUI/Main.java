package GUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ensure the frame is created on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Create and show the AppFrame
            AppFrame frame = new AppFrame();
            frame.setTitle("File Compressor & Decompressor");  // Set a descriptive title
            frame.setSize(800, 600);  // Adjust the frame size
            frame.setLocationRelativeTo(null);  // Center the window on the screen
            frame.setVisible(true);  // Make the frame visible
        });
    }
}
