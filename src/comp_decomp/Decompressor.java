package comp_decomp;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Decompressor {

    // Method to decompress file with progress updates
    public static void decompressFile(File file, JProgressBar progressBar, JLabel statusLabel) throws IOException {
        String fileDirectory = file.getParent();
        String fileName = file.getName();

        // Ensure the file ends with ".gz" and remove this extension
        if (!fileName.endsWith(".gz")) {
            throw new IOException("The selected file is not a valid .gz file.");
        }

        // Remove the ".gz" extension to get the base file name
        String baseFileName = fileName.substring(0, fileName.length() - 3); // Remove ".gz"
        File outputFile = new File(fileDirectory, baseFileName);

        // Check if the output file already exists
        if (outputFile.exists()) {
            int userChoice = JOptionPane.showOptionDialog(null,
                    "The file '" + outputFile.getName() + "' already exists. What would you like to do?",
                    "File Exists",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Overwrite", "Rename", "Cancel"},
                    null);

            if (userChoice == JOptionPane.YES_OPTION) {
                // User chose to overwrite
                // Do nothing, just proceed with the current outputFile
            } else if (userChoice == JOptionPane.NO_OPTION) {
                // User chose to rename (create a new incremented file)
                int count = 1;
                while (outputFile.exists()) {
                    String newFileName = baseFileName + " (" + count + ")" + baseFileName.substring(baseFileName.lastIndexOf("."));
                    outputFile = new File(fileDirectory, newFileName);
                    count++;
                }
            } else {
                // User chose to cancel the operation
                return; // Exit the method
            }
        }

        // Proceed with decompression
        FileInputStream fis = new FileInputStream(file);
        GZIPInputStream gzip = new GZIPInputStream(fis);
        FileOutputStream fos = new FileOutputStream(outputFile);

        byte[] buffer = new byte[1024];
        long fileSize = file.length();
        long totalRead = 0;
        int len;

        // Update progress while decompressing
        while ((len = gzip.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            totalRead += len;
            int progress = (int) ((totalRead * 100) / fileSize);
            progressBar.setValue(progress);
        }

        // Close streams
        gzip.close();
        fis.close();
        fos.close();

        // Update UI after completion
        progressBar.setValue(100);
        statusLabel.setText("File decompressed successfully to: " + outputFile.getAbsolutePath());
        JOptionPane.showMessageDialog(null, "File decompressed successfully to: " + outputFile.getAbsolutePath());
    }

    public static void main(String[] args) {
        // Use JFileChooser to dynamically select a compressed file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a GZipped File to Decompress");
        
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Create dummy progress bar and status label for testing
            JProgressBar progressBar = new JProgressBar(0, 100);
            JLabel statusLabel = new JLabel("Decompressing...");
            
            // Call decompression method
            try {
                decompressFile(selectedFile, progressBar, statusLabel);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error during decompression: " + e.getMessage(),
                        "Decompression Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
