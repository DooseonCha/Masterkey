package masterkey.ui;

import masterkey.domain.Entry;
import masterkey.domain.PasswordHistory;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

public class HistoryDialog extends JDialog {
    public HistoryDialog(MainFrame owner, Entry entry) {
        super(owner, "Password History - " + entry.getSiteName(), true);
        buildUi(entry);
        setSize(520, 320);
        setLocationRelativeTo(owner);
    }

    private void buildUi(Entry entry) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Previous Password", "Changed At"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (PasswordHistory history : entry.getPasswordHistories()) {
            model.addRow(new Object[]{history.getPreviousPassword(), history.getChangedAt()});
        }

        JTable table = new JTable(model);
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        root.add(new JScrollPane(table), BorderLayout.CENTER);
        root.add(closeButton, BorderLayout.SOUTH);

        setContentPane(root);
    }
}
