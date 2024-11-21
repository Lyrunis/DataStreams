package DataStreams;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;

public class DataStreamsApp extends JFrame {
    private JTextArea originalTextArea;
    private JTextArea filteredTextArea;
    private JTextField searchField;
    private JButton loadButton;
    private JButton searchButton;
    private JButton quitButton;
    private JLabel fileNameLabel;

    private final FileProcessor fileProcessor;

    public DataStreamsApp() {
        fileProcessor = new FileProcessor();

        setTitle("Data Streams Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new BorderLayout());

        // Create the main components
        originalTextArea = createTextArea();
        filteredTextArea = createTextArea();

        // Scroll panes for the text areas
        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, originalScrollPane, filteredScrollPane);
        splitPane.setDividerLocation(400); // Default split location (half)

        // TextField for search
        JPanel topPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        topPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);

        // Buttons on the right side
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        loadButton = new JButton("Load File");
        searchButton = new JButton("Search File");
        quitButton = new JButton("Quit");

        fileNameLabel = new JLabel("No file selected");
        fileNameLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel loadButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loadButtonPanel.add(loadButton);
        loadButtonPanel.add(fileNameLabel);

        buttonPanel.add(loadButtonPanel);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(searchButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(quitButton);

        // Add components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);

        // Add button actions
        loadButton.addActionListener(new LoadFileAction());
        searchButton.addActionListener(new SearchFileAction());
        quitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true); // Enable text wrapping
        textArea.setWrapStyleWord(true); // Wrap on word boundaries
        return textArea;
    }

    private class LoadFileAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(DataStreamsApp.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                Path filePath = fileChooser.getSelectedFile().toPath();
                fileProcessor.setFilePath(filePath);
                fileNameLabel.setText(filePath.getFileName().toString());
                loadFile();
            }
        }

        private void loadFile() {
            try {
                originalTextArea.setText("");
                for (String line : fileProcessor.readAllLines()) {
                    originalTextArea.append(line + "\n");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(DataStreamsApp.this, "Error loading file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class SearchFileAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (fileProcessor == null) {
                JOptionPane.showMessageDialog(DataStreamsApp.this, "No file loaded. Please load a file first.",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String searchString = searchField.getText().trim();
            if (searchString.isEmpty()) {
                JOptionPane.showMessageDialog(DataStreamsApp.this, "Please enter a search string.",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                filteredTextArea.setText("");
                for (String line : fileProcessor.filterLines(searchString)) {
                    filteredTextArea.append(line + "\n");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(DataStreamsApp.this, "Error processing file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
