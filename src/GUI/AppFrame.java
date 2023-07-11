package GUI;

import comp_decomp.Compressor;
import comp_decomp.Decompressor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AppFrame extends JFrame implements ActionListener {
    JButton compressButton;
    JButton decompressButton;

    AppFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout()); // Use BorderLayout for the frame

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 265)); // Use FlowLayout for horizontal alignment
        buttonPanel.setBackground(Color.darkGray); // Set the background color of the button panel

        compressButton = new JButton("Select File to Compress");
        compressButton.addActionListener(this);

        decompressButton = new JButton("Select File to Decompress");
        decompressButton.addActionListener(this);

        buttonPanel.add(compressButton);
        buttonPanel.add(decompressButton);

        this.add(buttonPanel, BorderLayout.CENTER); // Add the button panel to the center of the frame
        this.setSize(800,630);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==compressButton){
            JFileChooser filechoose = new JFileChooser();
            int response = filechoose.showSaveDialog(null);
            if(response==JFileChooser.APPROVE_OPTION){
                File file = new File(filechoose.getSelectedFile().getAbsolutePath());
                System.out.print(file);
                try{
                    Compressor.method(file);
                }
                catch (Exception ee){
                    JOptionPane.showMessageDialog(null,ee.toString());
                }
            }
        }
        if(e.getSource()==decompressButton){
            JFileChooser filechoose = new JFileChooser();
            int response = filechoose.showSaveDialog(null);
            if(response==JFileChooser.APPROVE_OPTION){
                File file = new File(filechoose.getSelectedFile().getAbsolutePath());
                System.out.print(file);
                try{
                    Decompressor.method(file);
                }
                catch (Exception ee){
                    JOptionPane.showMessageDialog(null,ee.toString());
                }
            }
        }
    }
}
