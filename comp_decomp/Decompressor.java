package comp_decomp;

import javax.swing.*;
import java.io.*;
import java.util.zip.GZIPInputStream;

public class Decompressor {

    // Define a listener interface for progress updates
    public interface ProgressListener {
        void onProgressUpdate(int progress);
        void onCompletion(String message);
        void onError(String error);
    }

    // Method to decompress file with progress updates
    public static SwingWorker<Void, Integer> decompressFile(File file, ProgressListener listener) {
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws IOException {
                String fileDirectory = file.getParent();
                String fileName = file.getName();

                // Ensure the file ends with ".gz" and remove this extension
                if (!fileName.endsWith(".gz")) {
                    throw new IOException("The selected file is not a valid .gz file.");
                }

                // Remove the ".gz" extension to get the base file name
                String baseFileName = fileName.substring(0, fileName.length() - 3);
                String originalExtension = baseFileName.lastIndexOf('.') != -1 
                                           ? baseFileName.substring(baseFileName.lastIndexOf(".")) 
                                           : "";

                baseFileName = originalExtension.isEmpty() 
                                ? baseFileName 
                                : baseFileName.substring(0, baseFileName.lastIndexOf("."));

                File outputFile = new File(fileDirectory, baseFileName + originalExtension);

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
                        // Proceed with overwriting
                    } else if (userChoice == JOptionPane.NO_OPTION) {
                        int count = 1;
                        while (outputFile.exists()) {
                            String newFileName = baseFileName + " (" + count + ")" + originalExtension;
                            outputFile = new File(fileDirectory, newFileName);
                            count++;
                        }
                    } else {
                        return null; // Cancel the operation
                    }
                }

                // Proceed with decompression using buffered streams
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                     GZIPInputStream gzip = new GZIPInputStream(bis);
                     BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {

                    byte[] buffer = new byte[8192]; // Increased buffer size for performance
                    long fileSize = file.length();
                    long totalRead = 0;
                    int len;

                    // Update progress while decompressing
                    while ((len = gzip.read(buffer)) != -1) {
                        // Handle cancellation request
                        if (isCancelled()) {
                            listener.onError("Decompression cancelled.");
                            return null; // Stop processing
                        }

                        bos.write(buffer, 0, len);
                        totalRead += len;
                        int progress = (int) ((totalRead * 100) / fileSize);
                        publish(progress); // Publish progress updates
                    }

                    // Notify completion
                    listener.onCompletion("File decompressed successfully to: " + outputFile.getAbsolutePath());
                } catch (IOException e) {
                    listener.onError("Error during decompression: " + e.getMessage());
                }

                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                for (Integer progress : chunks) {
                    listener.onProgressUpdate(progress); // Update progress through listener
                }
            }

            @Override
            protected void done() {
                // Check if the task was cancelled before invoking onError
                if (isCancelled()) {
                    return; // Do nothing if the task was cancelled
                }

                try {
                    get(); // Handle exceptions thrown in doInBackground
                } catch (Exception e) {
                    listener.onError("Error during decompression: " + e.getMessage());
                }
            }
        };

        worker.execute(); // Start the worker
        return worker; // Return the worker so it can be cancelled
    }
}
