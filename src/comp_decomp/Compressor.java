package comp_decomp;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class Compressor {

    // Method for compressing the file with progress update
    public static void compressFile(File file, JProgressBar progressBar, JLabel statusLabel) throws IOException {
        String fileDirectory = file.getParent();  // Get file directory
        String fileName = file.getName();  // Get original file name
        String compressedFileName = fileDirectory + "/" + fileName + ".gz";  // Append .gz to the original name
        
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(compressedFileName);
        GZIPOutputStream gzip = new GZIPOutputStream(fos);

        byte[] buffer = new byte[1024];  // Buffer for reading the file
        long fileSize = file.length();   // Get the file size for progress calculation
        long totalRead = 0;
        int len;

        // Start compression and update progress bar
        while ((len = fis.read(buffer)) != -1) {
            gzip.write(buffer, 0, len);
            totalRead += len;
            int progress = (int) ((totalRead * 100) / fileSize);  // Calculate progress percentage
            progressBar.setValue(progress);  // Update progress bar
        }
        
        // Close streams
        gzip.close();
        fos.close();
        fis.close();

        // Update UI after completion
        progressBar.setValue(100);
        statusLabel.setText("File compressed successfully to: " + compressedFileName);
        JOptionPane.showMessageDialog(null, "File compressed successfully!");
    }

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("File Compressor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout());

        // Create a panel for UI components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a label to display instructions
        JLabel instructionLabel = new JLabel("Select a file to compress:");
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(instructionLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));  // Add spacing

        // Create a button to select the file
        JButton selectFileButton = new JButton("Select File");
        panel.add(selectFileButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));  // Add spacing

        // Create a progress bar
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        panel.add(progressBar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));  // Add spacing

        // Create a label to display the status
        JLabel statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(statusLabel);

        // Add the panel to the main frame
        frame.add(panel, BorderLayout.CENTER);

        // Add action listener to the "Select File" button
        selectFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a file to compress");

            // Show the file chooser dialog and get the result
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                progressBar.setValue(0);  // Reset progress bar
                statusLabel.setText("Compressing...");  // Update status

                // Compress the selected file and handle exceptions
                SwingUtilities.invokeLater(() -> {
                    try {
                        compressFile(selectedFile, progressBar, statusLabel);
                    } catch (IOException ex) {
                        statusLabel.setText("Error compressing file: " + ex.getMessage());
                    }
                });
            }
        });

        // Display the frame
        frame.setVisible(true);
    }
}
