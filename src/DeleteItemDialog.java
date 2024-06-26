import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DeleteItemDialog extends JDialog {
    private ArrayList<Item> inventory;
    private DefaultListModel<String> listModel;
    private JList<String> itemList;

    public DeleteItemDialog(JFrame parent, ArrayList<Item> inventory, DefaultListModel<String> listModel) {
        super(parent, "Delete Item", true);
        this.inventory = inventory;
        this.listModel = listModel;
        setLayout(new BorderLayout());
        setSize(300, 200);
        setMinimumSize(new Dimension(300, 200));
        setMaximumSize(new Dimension(300, 600));
        setResizable(true);

        JPanel panel = new JPanel(new BorderLayout());

        itemList = new JList<>(listModel);
        panel.add(new JScrollPane(itemList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!inventory.isEmpty()) {
                    int selectedIndex = itemList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        int option = JOptionPane.showConfirmDialog(DeleteItemDialog.this,
                                "Are you sure you want to delete this item?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            inventory.remove(selectedIndex);
                            listModel.remove(selectedIndex);
                            JOptionPane.showMessageDialog(DeleteItemDialog.this,
                                    "Item deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            setVisible(false);
                        }
                    } else {
                        JOptionPane.showMessageDialog(DeleteItemDialog.this,
                                "Select an item to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (inventory.isEmpty()){
                    JOptionPane.showMessageDialog(DeleteItemDialog.this,
                            "There is no item to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        setLocationRelativeTo(parent);
    }
}
