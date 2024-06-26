import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

// Eugene Andreu J. Cuello && Maria Criselli Sophia G. Almosara
// BSCS - A123
// Mininventory: A minimal and efficient inventory management system
public class InventoryManager extends JFrame {
    private ArrayList<Item> inventory; // Stores the list of 'Item' objects
    private DefaultListModel<String> listModel; // Manages the data for the 'JList'
    private JList<String> itemList; // Displays the list of item names
    private JLabel imageLabel;  // Label to display the image
    private JTextArea descriptionArea;  // Area to display the description

    // Labels for displaySubPanel
    private JLabel nameLabel;
    private JLabel amountLabel;
    private JLabel statusLabel;
    private JLabel clientLabel;

    public InventoryManager() {
        inventory = new ArrayList<>();
        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        ImageIcon icon = new ImageIcon("src/mininventoryico.png");
        Image miniIcon = icon.getImage();

        // Main
        setTitle("Mininventory");
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setIconImage(miniIcon);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Launch the program to show up on the center of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int x = (screenWidth - getWidth()) / 2;
        int y = (screenHeight - getHeight()) / 2;
        setLocation(x, y);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open file");
        JMenuItem saveItem = new JMenuItem("Save file");
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Panel for buttons
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints btngbc = new GridBagConstraints();
        btngbc.insets = new Insets(3, 3, 3, 3);
        btngbc.fill = GridBagConstraints.HORIZONTAL;
        btngbc.gridx = 4;
        btngbc.gridy = 0;
        buttonsPanel.add(new JLabel("Manage Items: "), btngbc);
        btngbc.gridx = 0;
        buttonsPanel.add(new JLabel("Sort by: "), btngbc);

            // Add Item Button\
            JButton addItemButton = new JButton("Add Item");
            btngbc.gridx = 5;
            buttonsPanel.add(addItemButton, btngbc);
            addItemButton.addActionListener(new AddItemListener());


            // Edit Item Button
            JButton editItemButton = new JButton("Edit Item");
            editItemButton.addActionListener(new EditItemListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedIndex = itemList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Item selectedItem = inventory.get(selectedIndex);
                        EditItemDialog editItemDialog = new EditItemDialog(InventoryManager.this, selectedItem);
                        editItemDialog.setVisible(true);

                        // After dialog is closed, update the listModel with the edited item name
                        listModel.setElementAt(selectedItem.getName(), selectedIndex);
                        updateDisplay(selectedItem); // Update display after edit
                    } else {
                        JOptionPane.showMessageDialog(InventoryManager.this,
                                "Please select an item to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btngbc.gridx = 6;
            buttonsPanel.add(editItemButton, btngbc);

            // Delete Item Button
            JButton deleteItemButton = new JButton("Delete Item");
            btngbc.gridx = 7;
            buttonsPanel.add(deleteItemButton, btngbc);
            deleteItemButton.addActionListener(new DeleteItemListener());

            // Sort by Name Button
            JButton sortByNameButton = new JButton("Name");
            btngbc.gridx = 1;
            buttonsPanel.add(sortByNameButton, btngbc);
            sortByNameButton.addActionListener(new SortNameListener());

            // Sort by Amount Button
            JButton sortByAmountButton = new JButton("Amount");
            btngbc.gridx = 2;
            buttonsPanel.add(sortByAmountButton, btngbc);
            sortByAmountButton.addActionListener(new SortAmountListener());

            // Sort by Status Button
            JButton sortByStatusButton = new JButton("Status");
            btngbc.gridx = 3;
            buttonsPanel.add(sortByStatusButton, btngbc);
            sortByStatusButton.addActionListener(new SortStatusListener());

        // Sub-panel for displayPanel to show Name, Amount, Status, and Client
        JPanel displaySubPanel = new JPanel();
        displaySubPanel.setLayout(new GridLayout(0, 4));
        nameLabel = new JLabel("Name: ");
        amountLabel = new JLabel("Amount: ");
        statusLabel = new JLabel("Status: ");
        clientLabel = new JLabel("Client: ");
        displaySubPanel.add(nameLabel);
        displaySubPanel.add(amountLabel);
        displaySubPanel.add(statusLabel);
        displaySubPanel.add(clientLabel);

        // Panel to display image and description
        JPanel displayPanel = new JPanel(new BorderLayout());
        JPanel nestedPanel = new JPanel(new BorderLayout());
        Font font = new Font("Verdana", Font.PLAIN, 12); // Font set as Verdana

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 300));

        descriptionArea = new JTextArea(10, 40); // Rows and Columns for TextArea
        descriptionArea.setFont(font);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);

        TitledBorder imageLabelBorder = BorderFactory.createTitledBorder("Image");
        imageLabelBorder.setTitleJustification(TitledBorder.CENTER);
        imageLabel.setBorder(imageLabelBorder);

        TitledBorder nestedPanelBorder = BorderFactory.createTitledBorder("Item information");
        nestedPanelBorder.setTitleJustification(TitledBorder.CENTER);
        nestedPanel.setBorder(nestedPanelBorder);

        nestedPanel.add(displaySubPanel, BorderLayout.NORTH);
        nestedPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        displayPanel.add(imageLabel, BorderLayout.NORTH);
        displayPanel.add(nestedPanel, BorderLayout.SOUTH);

        // Selection Pane / Item List
        JScrollPane selectionPane = new JScrollPane(itemList);
        selectionPane.setPreferredSize(new Dimension(150, 0));
        TitledBorder itemListBorder = BorderFactory.createTitledBorder("Items: ");
        itemListBorder.setTitleJustification(TitledBorder.CENTER);
        selectionPane.setBorder(itemListBorder);

        // Main Frame
        add(selectionPane, BorderLayout.WEST);
        add(displayPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.NORTH);

        // Open and Save File Actions
        openItem.addActionListener(new OpenFileListener());
        saveItem.addActionListener(new SaveFileListener());

        // List selection listener to update the display
        itemList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = itemList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Item selectedItem = inventory.get(selectedIndex);
                    updateDisplay(selectedItem);
                }
            }
        });
    }

    // Listener for Adding Items
    private class AddItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            AddItemDialog addItemDialog = new AddItemDialog(InventoryManager.this);
            addItemDialog.setVisible(true);
            Item newItem = addItemDialog.getItem();
            if (newItem != null) {
                inventory.add(newItem);
                listModel.addElement(newItem.getName());
            }
        }
    }

    // Listener for Editing Items
    private class EditItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = itemList.getSelectedIndex();
            if (selectedIndex != -1) {
                Item selectedItem = inventory.get(selectedIndex);
                EditItemDialog editItemDialog = new EditItemDialog(InventoryManager.this, selectedItem); // Pass reference
                editItemDialog.setVisible(true);

                // After dialog is closed, update the listModel with the edited item name
                listModel.setElementAt(selectedItem.getName(), selectedIndex);
                updateDisplay(selectedItem); // Optionally update display
            } else {
                JOptionPane.showMessageDialog(InventoryManager.this,
                        "Please select an item to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Listener for Deleting Items
    private class DeleteItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!inventory.isEmpty()) {
            DeleteItemDialog deleteItemDialog = new DeleteItemDialog(InventoryManager.this, inventory, listModel);
            deleteItemDialog.setVisible(true);

            // After deletion, update display
            int selectedIndex = itemList.getSelectedIndex();
            if (selectedIndex == -1) {
                updateDisplay(null); // Clear display if no item is selected
            } else {
                Item selectedItem = inventory.get(selectedIndex);
                updateDisplay(selectedItem);
            }
            } else {
                JOptionPane.showMessageDialog(InventoryManager.this,
                        "There is no item to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Listener for Opening Files
    private class OpenFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(InventoryManager.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    inventory = (ArrayList<Item>) ois.readObject();
                    listModel.clear();
                    for (Item item : inventory) {
                        listModel.addElement(item.getName());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Listener for Saving Files
    private class SaveFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(InventoryManager.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                    oos.writeObject(inventory);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Listener for Sorting by Name
    private class SortNameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            inventory.sort(Comparator.comparing(Item::getName));
            updateItemList();
        }
    }

    // Listener for Sorting by Amount
    private class SortAmountListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            inventory.sort(Comparator.comparing(Item::getAmount));
            updateItemList();
        }
    }

    // Listener for Sorting by Status
    private class SortStatusListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            inventory.sort(Comparator.comparing(Item::getStatus));
            updateItemList();
        }
    }

    // Method to update the item list
    private void updateItemList() {
        listModel.clear();
        for (Item item : inventory) {
            listModel.addElement(item.getName());
        }
    }

    // Method to update the display panel with the selected item's image and description
    private void updateDisplay(Item item) {
        if (item != null) {
            descriptionArea.setText(item.getDescription());
            resizeAndSetImage(item.getImagePath());

            // Update the labels in displaySubPanel
            nameLabel.setText("Name: " + item.getName());
            amountLabel.setText("Amount: " + item.getAmount());
            statusLabel.setText("Status: " + item.getStatus());
            clientLabel.setText("Client: " + item.getClient());
        } else {
            descriptionArea.setText("");
            imageLabel.setIcon(null); // Clear the image

            // Clear the labels in displaySubPanel
            nameLabel.setText("Name: ");
            amountLabel.setText("Amount: ");
            statusLabel.setText("Status: ");
            clientLabel.setText("Client: ");
        }
    }

    // Helper method to resize and set image
    private void resizeAndSetImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon imageIcon = new ImageIcon(imagePath);
            Image image = imageIcon.getImage();

            int labelWidth = imageLabel.getWidth();
            int labelHeight = imageLabel.getHeight();

            // Use preferred size if actual size is not set
            if (labelWidth == 0 || labelHeight == 0) {
                labelWidth = imageLabel.getPreferredSize().width;
                labelHeight = imageLabel.getPreferredSize().height;
            }

            // Original dimensions
            int originalWidth = image.getWidth(null);
            int originalHeight = image.getHeight(null);

            // Calculate new dimensions while maintaining aspect ratio
            double aspectRatio = (double) originalWidth / originalHeight;

            int newWidth = labelWidth;
            int newHeight = (int) (newWidth / aspectRatio);

            if (newHeight > labelHeight) {
                newHeight = labelHeight;
                newWidth = (int) (newHeight * aspectRatio);
            }

            Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            imageIcon.setImage(scaledImage);
            imageLabel.setIcon(imageIcon);
        } else {
            imageLabel.setIcon(null); // Clear the image
        }
    }

    // Start
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InventoryManager().setVisible(true);
        });
    }

}
