/** Copyright (c) 2017 Simon Knott
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.apple.eawt.Application;

import javax.swing.*;
import java.io.File;

/**
 * Created by skn0tt on 20.08.17.
 */

public class MagMerge {
    /**
     * Swing Components
     */
    private JTextField inputPathTxtFld;
    private JButton chooseInputBtn;
    private JPanel inputPanel;
    private JPanel outputPanel;
    private JTextField outputPathTxtFld;
    private JButton chooseOutputBtn;
    private JPanel informationPanel;
    private JTextField issueTxtFld;
    private JButton goBtn;
    private JPanel mainPanel;
    private JButton aboutBtn;

    private static JFrame frame;

    /**
     * Constants
     */
    private static final String appTitle = "magMerge";

    /**
     * main
     * @param args arguments of main method
     */
    public static void main(String[] args) {
        setupFrame();
    }

    /**
     * Setup the frame
     */
    private static void setupFrame() {
        frame = new JFrame(appTitle);
        frame.setContentPane(new MagMerge().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if (System.getProperty("os.name").toLowerCase().contains("mac")) setupOS_macOS();

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * System Specifics for macOS
     */
    private static void setupOS_macOS() {
        Application app = Application.getApplication();
        /*  TODO: Create Logo
        Image img = Toolkit.getDefaultToolkit().getImage("res/logo.png");
        app.setDockIconImage(img);
        */
        app.setAboutHandler(ae -> showAbout());
    }

    /**
     * Constructor
     * Adds ActionListeners
     */
    public MagMerge() {
        goBtn.addActionListener(e -> btnPressed_Go());
        chooseInputBtn.addActionListener(e -> btnPressed_ChooseInput());
        chooseOutputBtn.addActionListener(e -> btnPressed_ChooseOutput());
        aboutBtn.addActionListener(e -> btnPressed_aboutBtn());
    }

    /**
     * ActionHandler @aboutBtn
     */
    private void btnPressed_aboutBtn() {
        showAbout();
    }

    /**
     * Shows About Panel
     */
    private static void showAbout() {
        JOptionPane.showMessageDialog(frame,
            "magMerge\n" +
                    "Licensed under MIT License\n" +
                    "github.com/skn0tt/magMerge\n" +
                    "Maintained by Simon Knott (simonknott.de)",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * ActionHandler @chooseInputBtn
     */
    private void btnPressed_ChooseInput() {
        showFileChooserInput();
    }

    /**
     * ActionHandler @chooseInputBtn
     */
    private void btnPressed_ChooseOutput() {
        showFileChooserOutput();
    }

    /**
     * Shows JFileChooser for output folder
     */
    private void showFileChooserOutput() {
        JFileChooser chooser = new JFileChooser();

        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showSaveDialog(mainPanel);

        outputPathTxtFld.setText(chooser.getSelectedFile().toString());
    }

    /**
     * Shows JFileChooser for input .pdf
     */
    private void showFileChooserInput() {
        JFileChooser chooser = new JFileChooser();

        chooser.setFileFilter(new PDFFilter());
        chooser.showOpenDialog(mainPanel);

        inputPathTxtFld.setText(chooser.getSelectedFile().toString());
    }

    /**
     * Filters for PDF files
     */
    private class PDFFilter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".pdf");
        }

        @Override
        public String getDescription() {
            return ".pdf";
        }
    }

    /**
     * ActionHandler @goBtn
     */
    private void btnPressed_Go() {
        if (!checkLegal()) return;

        ImageKt.merge(inputPathTxtFld.getText(), outputPathTxtFld.getText(), Integer.parseInt(issueTxtFld.getText()));

        alert("Done!");
    }

    /**
     * Checks if all the input is legal.
     * @return true if all is legal.
     */
    private boolean checkLegal() {
        return (inputPathLegal() && outputPathLegal() && issueLegal());
    }

     /**
      * Checks if the inputPath is legal.
      * @return true if the path is legal.
      */
     private boolean inputPathLegal() {
        File file = new File(inputPathTxtFld.getText());

        if (!file.exists()) {
            alert("Input doesn't exist.");
            return false;
        }
        if (!file.getName().endsWith(".pdf")) {
            alert("Input isn't PDF.");
            return false;
        }

        return true;
     }

     /**
      * Checks if the outputPath is legal.
      * @return true if the path is legal.
      */
     private boolean outputPathLegal() {
        File file = new File(outputPathTxtFld.getText());

        if (!file.exists()) {
            alert("Output doesn't exist.");
            return false;
        }
        if (!file.isDirectory()) {
            alert("Output isn't a directory.");
            return false;
        }

        return true;
    }

     /**
      * Checks if the issue is legal.
      * @return true if the issue is legal.
      */
     private boolean issueLegal() {
        try {
            Integer.parseInt(issueTxtFld.getText());
            return true;
        } catch (NumberFormatException e) {
            alert("Issue isn't integer");
            return false;
        }
    }

     /**
      * Opens a messageDialog with a given text.
      * @param text to be shown
      */
     private void alert(String text) {
        JOptionPane.showMessageDialog(mainPanel, text);
    }
}
