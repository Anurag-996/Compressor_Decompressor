package GUI;

import comp_decomp.Compressor;
import comp_decomp.Decompressor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AppFrame extends JFrame
        implements ActionListener, Compressor.ProgressListener, Decompressor.ProgressListener {
    private JButton compressButton;
    private JButton decompressButton;
    private JButton cancelButton;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private BackgroundPanel backgroundPanel;
    private SwingWorker<Void, Integer> worker;

    public AppFrame() {
        // Frame configuration
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Compressor & Decompressor");
        this.setLayout(new BorderLayout());

        // Create and set up the background panel
        backgroundPanel = new BackgroundPanel(); // Assuming BackgroundPanel is defined elsewhere
        this.setContentPane(backgroundPanel); // Set background panel as content pane

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);

        // Set up a panel for buttons using GridBagLayout for centering
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // Make the panel transparent
        buttonPanel.setBorder(BorderFactory.createTitledBorder("File Operations"));

        // Configure GridBagConstraints for centering the buttons
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around the buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Center the buttons

        // Compress Button
        compressButton = new JButton("Compress");
        compressButton.setFont(new Font("Arial", Font.BOLD, 14));
        compressButton.setPreferredSize(new Dimension(200, 50));
        compressButton.setToolTipText("Click to select a file and compress it");
        compressButton.addActionListener(this);
        buttonPanel.add(compressButton, gbc); // Add to panel

        // Decompress Button
        gbc.gridy = 1; // Move to the next row
        decompressButton = new JButton("Decompress");
        decompressButton.setFont(new Font("Arial", Font.BOLD, 14));
        decompressButton.setPreferredSize(new Dimension(200, 50));
        decompressButton.setToolTipText("Click to select a compressed file and decompress it");
        decompressButton.addActionListener(this);
        buttonPanel.add(decompressButton, gbc); // Add to panel

        // Cancel Button
        gbc.gridy = 2; // Move to the next row
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(200, 50));
        cancelButton.setToolTipText("Click to cancel the current operation");
        cancelButton.addActionListener(e -> cancelOperation());
        cancelButton.setVisible(false); // Initially hidden
        buttonPanel.add(cancelButton, gbc); // Add to panel

        // Add button panel to center of the frame
        this.add(buttonPanel, BorderLayout.CENTER);

        // Progress Bar and Status Label for feedback
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.GREEN);
        progressBar.setToolTipText("Shows the progress of compression/decompression");

        statusLabel = new JLabel("Status: Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Create a status panel and add components
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setOpaque(false); // Make the status panel transparent
        statusPanel.setBorder(BorderFactory.createTitledBorder("Progress"));
        statusPanel.add(progressBar);
        statusPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between progress bar and label
        statusPanel.add(statusLabel);

        // Add the status panel to the bottom of the frame
        this.add(statusPanel, BorderLayout.SOUTH);

        // Set frame properties
        this.setSize(600, 500);
        this.setLocationRelativeTo(null); // Center the frame on the screen
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == compressButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a File to Compress");

            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                startCompression(file);
            }
        }

        if (e.getSource() == decompressButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a File to Decompress");

            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                startDecompression(file);
            }
        }
    }

    private void startCompression(File file) {
        progressBar.setValue(0);
        statusLabel.setText("Compressing...");
        cancelButton.setVisible(true);

        // Start the compression process with a listener and store the worker
        worker = Compressor.compressFile(file, this);
    }

    private void startDecompression(File file) {
        progressBar.setValue(0);
        statusLabel.setText("Decompressing...");
        cancelButton.setVisible(true);

        // Start the decompression process with a listener and store the worker
        worker = Decompressor.decompressFile(file, this);
    }

    private void cancelOperation() {
        if (worker != null && !worker.isDone()) {
            worker.cancel(true); // Cancel the SwingWorker
            progressBar.setValue(0); // Clear the progress bar
            statusLabel.setText("Operation cancelled.");
            cancelButton.setVisible(false); // Hide cancel button after canceling
        }
    }

    private void resetProgress() {
        progressBar.setValue(0); // Clear progress bar
        statusLabel.setText("Status: Ready"); // Set status to "Ready"
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppFrame::new); // Launch the application in the Event Dispatch Thread
    }

    // Implement the progress listener methods for compressor
    @Override
    public void onProgressUpdate(int progress) {
        progressBar.setValue(progress); // Update progress bar
    }

    @Override
    public void onCompletion(String message) {
        cancelButton.setVisible(false);
        progressBar.setValue(100); // Set to complete after the task is done
        statusLabel.setText(message);

        // Show a popup message that indicates success
        JOptionPane.showMessageDialog(AppFrame.this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        resetProgress(); // Reset progress regardless of success or failure
    }

    @Override
    public void onError(String error) {
        cancelButton.setVisible(false);
        statusLabel.setText("Operation Failed.");

        // Show a popup message with the error
        JOptionPane.showMessageDialog(AppFrame.this, error, "Error", JOptionPane.ERROR_MESSAGE);
        resetProgress(); // Reset progress regardless of success or failure
    }
}
