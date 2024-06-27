import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

public class AddItemDialog extends JDialog {
    // Fields
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField clientField;
    private JTextField imagePathField;
    private JTextField quantityField;
    private JComboBox<String> statusField;
    // Buttons
    private JButton browseButton;
    private JButton okButton;
    private JButton cancelButton;

    private Item item;

    public AddItemDialog(JFrame parent) {
        super(parent, "Add Item", true);
        setLayout(new BorderLayout());
        setSize(420, 400);
        setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int x = (screenWidth - getWidth()) / 2;
        int y = (screenHeight - getHeight()) / 2;
        setLocation(x, y);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        nameField = new JTextField(20);
        nameField.setText("Input item name");
        inputPanel.add(nameField, gbc);
        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("Input item name")) {
                    nameField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText("Input item name");
                }

            }
        });

        // Description area field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        inputPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setText("Input description of the item");
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        inputPanel.add(scrollPane, gbc);
        descriptionArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (descriptionArea.getText().equals("Input description of the item")) {
                    descriptionArea.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (descriptionArea.getText().isEmpty()) {
                    descriptionArea.setText("Input description of the item");
                }

            }
        });

        // Amount field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JLabel("Quantity:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        quantityField = new JTextField(20);
        quantityField.setText("0");
        inputPanel.add(quantityField, gbc);
        quantityField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (quantityField.getText().equals("0")) {
                    quantityField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (quantityField.getText().equals("")) {
                    quantityField.setText("0");
                }

            }
        });

        // Client field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Client:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        clientField = new JTextField(20);
        clientField.setText("Input client name");
        inputPanel.add(clientField, gbc);
        clientField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (clientField.getText().equals("Input client name")) {
                    clientField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (clientField.getText().isEmpty()) {
                    clientField.setText("Input client name");
                }

            }
        });

        // Status field
        String[] currentstatus = {"In stock", "On Delivery", "Delayed", "Unavailable"};
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        statusField = new JComboBox<>(currentstatus);
        inputPanel.add(statusField, gbc);

        // Image Path field
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JLabel("Image Path:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        imagePathField = new JTextField(15);
        inputPanel.add(imagePathField, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        browseButton = new JButton("Browse");
        inputPanel.add(browseButton, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        browseButton.addActionListener(new BrowseButtonListener());
        okButton.addActionListener(new OkButtonListener());
        cancelButton.addActionListener(e -> setVisible(false));
    }

    public Item getItem() {
        return item;
    }

    // Listener for Browse Button
    private class BrowseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(AddItemDialog.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                imagePathField.setText(file.getAbsolutePath());
            }
        }
    }
    // Listener for Ok Button
    private class OkButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String description = descriptionArea.getText().trim();
            String imagePath = imagePathField.getText().trim();
            String status = (String) statusField.getSelectedItem();
            String client = clientField.getText().trim();

            int quantity = 0; // Default value
            try {
                quantity = Integer.parseInt(quantityField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(AddItemDialog.this,
                        "Quantity must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method if parsing fails
            }

            if (!name.isEmpty() && !status.isEmpty()) {
                item = new Item(name, description, client, status, imagePath, quantity);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(AddItemDialog.this,
                        "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
