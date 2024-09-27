package GUI;

import comp_decomp.Compressor;
import comp_decomp.Decompressor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class AppFrame extends JFrame implements ActionListener {
    JButton compressButton;
    JButton decompressButton;
    JProgressBar progressBar;
    JLabel statusLabel;
    BackgroundPanel backgroundPanel;

    public AppFrame() {
        // Frame configuration
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Compressor & Decompressor");
        this.setLayout(new BorderLayout());

        // Create and set up the background panel
        backgroundPanel = new BackgroundPanel(); // No need to pass the image path
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

        // Compress Button without icon
        compressButton = new JButton("Compress");
        compressButton.setFont(new Font("Arial", Font.BOLD, 14));
        compressButton.setPreferredSize(new Dimension(200, 50));
        compressButton.setToolTipText("Click to select a file and compress it");
        compressButton.addActionListener(this);
        buttonPanel.add(compressButton, gbc); // Add to panel

        // Decompress Button without icon (Below the Compress button)
        gbc.gridy = 1; // Move to the next row
        decompressButton = new JButton("Decompress");
        decompressButton.setFont(new Font("Arial", Font.BOLD, 14));
        decompressButton.setPreferredSize(new Dimension(200, 50));
        decompressButton.setToolTipText("Click to select a compressed file and decompress it");
        decompressButton.addActionListener(this);
        buttonPanel.add(decompressButton, gbc); // Add to panel

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
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                progressBar.setValue(0);  // Reset progress bar
                statusLabel.setText("Compressing...");

                SwingUtilities.invokeLater(() -> {
                    try {
                        Compressor.compressFile(file, progressBar, statusLabel);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error compressing file: " + ex.getMessage(),
                                "Compression Error", JOptionPane.ERROR_MESSAGE);
                        statusLabel.setText("Compression Failed.");
                    }
                });
            }
        }

        if (e.getSource() == decompressButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a File to Decompress");

            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                progressBar.setValue(0);  // Reset progress bar
                statusLabel.setText("Decompressing...");

                SwingUtilities.invokeLater(() -> {
                    try {
                        Decompressor.decompressFile(file, progressBar, statusLabel);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error decompressing file: " + ex.getMessage(),
                                "Decompression Error", JOptionPane.ERROR_MESSAGE);
                        statusLabel.setText("Decompression Failed.");
                    }
                });
            }
        }
    }

    public static void main(String[] args) {
        new AppFrame();  // Launch the application
    }
}
