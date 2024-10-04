package comp_decomp;

import javax.swing.*;
import java.io.*;
import java.util.zip.GZIPOutputStream;

public class Compressor {

    // Define a listener interface for progress updates
    public interface ProgressListener {
        void onProgressUpdate(int progress);
        void onCompletion(String message);
        void onError(String error);
    }

    // Method for compressing the file with progress update
    public static SwingWorker<Void, Integer> compressFile(File file, ProgressListener listener) {
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws IOException {
                String fileDirectory = file.getParent();
                String fileName = file.getName();
                String compressedFileName = fileDirectory + "/" + fileName + ".gz";

                try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
                     FileOutputStream fos = new FileOutputStream(compressedFileName);
                     GZIPOutputStream gzip = new GZIPOutputStream(fos)) {

                    byte[] buffer = new byte[8192]; // Buffer for reading files
                    long fileSize = file.length();
                    long totalRead = 0;
                    int len;

                    // Start compression and update progress bar
                    while ((len = fis.read(buffer)) != -1) {
                        if (isCancelled()) { // Check if cancellation is requested
                            listener.onError("Compression cancelled.");
                            return null; // Exit if cancelled
                        }

                        gzip.write(buffer, 0, len);
                        totalRead += len;
                        int progress = (int) ((totalRead * 100) / fileSize);
                        publish(progress); // Publish progress updates
                    }

                    // Notify completion
                    listener.onCompletion("File compressed successfully.");
                } catch (IOException e) {
                    listener.onError("Error compressing file: " + e.getMessage());
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
                    get(); // Handle any exceptions thrown in doInBackground
                } catch (Exception ex) {
                    listener.onError("Error compressing file: " + ex.getMessage());
                }
            }
        };

        worker.execute(); // Start the worker
        return worker; // Return the worker
    }
}
