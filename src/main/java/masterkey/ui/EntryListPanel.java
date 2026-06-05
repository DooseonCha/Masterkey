package masterkey.ui;

import masterkey.domain.Backup;
import masterkey.domain.Entry;
import masterkey.service.AlertService;
import masterkey.service.BackupService;
import masterkey.service.EntryService;
import masterkey.service.PasswordService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.util.List;

public class EntryListPanel extends JPanel {
    private final MainFrame mainFrame;
    private final EntryService entryService;
    private final PasswordService passwordService;
    private final AlertService alertService;
    private final BackupService backupService;

    private final DefaultTableModel tableModel;
    private final JTable entryTable;

    public EntryListPanel(MainFrame mainFrame, EntryService entryService, PasswordService passwordService,
                          AlertService alertService, BackupService backupService) {
        this.mainFrame = mainFrame;
        this.entryService = entryService;
        this.passwordService = passwordService;
        this.alertService = alertService;
        this.backupService = backupService;

        this.tableModel = new DefaultTableModel(new Object[]{"Site", "URL", "User ID", "Strength", "Modified"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.entryTable = new JTable(tableModel);

        buildUi();
    }

    private void buildUi() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Entry");
        JButton deleteButton = new JButton("Delete Entry");
        JButton historyButton = new JButton("Password History");
        JButton generateButton = new JButton("Generate Password");
        JButton copyButton = new JButton("Copy Password");
        JButton autofillButton = new JButton("Autofill");
        JButton alertButton = new JButton("Last Change Alert");
        JButton backupButton = new JButton("Backup");
        JButton autoBackupButton = new JButton("Auto Backup");
        JButton refreshButton = new JButton("Refresh");
        JButton lockButton = new JButton("Lock");
        JButton closeFileButton = new JButton("Close File");

        topPanel.add(addButton);
        topPanel.add(deleteButton);
        topPanel.add(historyButton);
        topPanel.add(generateButton);
        topPanel.add(copyButton);
        topPanel.add(autofillButton);
        topPanel.add(alertButton);
        topPanel.add(backupButton);
        topPanel.add(autoBackupButton);
        topPanel.add(refreshButton);
        topPanel.add(lockButton);
        topPanel.add(closeFileButton);

        addButton.addActionListener(e -> addEntry());
        deleteButton.addActionListener(e -> deleteEntry());
        historyButton.addActionListener(e -> showHistory());
        generateButton.addActionListener(e -> generatePassword());
        copyButton.addActionListener(e -> copyPassword());
        autofillButton.addActionListener(e -> autofill());
        alertButton.addActionListener(e -> showAlertDialog());
        backupButton.addActionListener(e -> backup());
        autoBackupButton.addActionListener(e -> autoBackupSetting());
        refreshButton.addActionListener(e -> refreshTable());
        lockButton.addActionListener(e -> mainFrame.lockDatabase());
        closeFileButton.addActionListener(e -> mainFrame.closeFile());

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(entryTable), BorderLayout.CENTER);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);

        for (Entry entry : entryService.getEntries()) {
            tableModel.addRow(new Object[]{
                    entry.getSiteName(),
                    entry.getSiteUrl(),
                    entry.getUserId(),
                    entry.getPassword().getStrengthLevel(),
                    entry.getModifiedAt()
            });
        }
    }

    private Entry getSelectedEntry() {
        int selectedRow = entryTable.getSelectedRow();
        if (selectedRow < 0) {
            mainFrame.showError("Selection Required", "Please select an entry.");
            return null;
        }

        List<Entry> entries = entryService.getEntries();
        if (selectedRow >= entries.size()) {
            return null;
        }
        return entries.get(selectedRow);
    }

    private void addEntry() {
        EntryFormDialog dialog = new EntryFormDialog(mainFrame);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            try {
                entryService.addEntry(
                        dialog.getSiteNameValue(),
                        dialog.getSiteUrlValue(),
                        dialog.getUserIdValue(),
                        dialog.getPasswordValue()
                );
                refreshTable();
            } catch (Exception e) {
                mainFrame.showError("Add Entry Failed", e.getMessage());
            }
        }
    }

    private void deleteEntry() {
        Entry entry = getSelectedEntry();
        if (entry == null) {
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "Delete selected entry?",
                "Delete Entry",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            try {
                entryService.deleteEntry(entry.getEntryId());
                refreshTable();
            } catch (Exception e) {
                mainFrame.showError("Delete Failed", e.getMessage());
            }
        }
    }

    private void showHistory() {
        Entry entry = getSelectedEntry();
        if (entry == null) {
            return;
        }

        HistoryDialog dialog = new HistoryDialog(mainFrame, entry);
        dialog.setVisible(true);
    }

    private void generatePassword() {
        PasswordGeneratorDialog dialog = new PasswordGeneratorDialog(mainFrame, passwordService);
        dialog.setVisible(true);
    }

    private void copyPassword() {
        Entry entry = getSelectedEntry();
        if (entry == null) {
            return;
        }

        passwordService.copyToClipboard(entry.getPassword().getPasswordValue());
        mainFrame.showInfo("Copied", "Password copied to clipboard.");
    }

    private void autofill() {
        Entry entry = getSelectedEntry();
        if (entry == null) {
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "After pressing OK, focus the target login field quickly. Continue?",
                "Autofill",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                passwordService.autofill(entry.getUserId(), entry.getPassword().getPasswordValue());
            } catch (Exception e) {
                mainFrame.showError("Autofill Failed", e.getMessage());
            }
        }
    }

    private void showAlertDialog() {
        AlertSettingsDialog dialog = new AlertSettingsDialog(mainFrame, alertService);
        dialog.setVisible(true);
    }

    private void backup() {
        JFileChooser chooser = new JFileChooser(new File("file"));
        chooser.setDialogTitle("Save Backup File");
        chooser.setSelectedFile(new File("masterkey_backup.mkdb"));
        chooser.setFileFilter(new FileNameExtensionFilter("MasterKey Database Backup (*.mkdb)", "mkdb"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Backup backup = backupService.backup(chooser.getSelectedFile());
                mainFrame.showInfo("Backup Completed", "Backup file was created.");
                refreshTable();
            } catch (Exception e) {
                mainFrame.showError("Backup Failed", e.getMessage());
            }
        }
    }

    private void autoBackupSetting() {
        JCheckBox enabled = new JCheckBox("Enable auto backup");
        boolean current = mainFrame.getDatabaseService()
                .getCurrentDatabase()
                .getUserSettings()
                .isAutoBackupEnabled();
        enabled.setSelected(current);

        int result = JOptionPane.showConfirmDialog(
                this,
                enabled,
                "Auto Backup Setting",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                alertService.updateAutoBackup(enabled.isSelected());
                mainFrame.showInfo("Auto Backup", "Auto backup setting was updated.");
            } catch (Exception e) {
                mainFrame.showError("Setting Failed", e.getMessage());
            }
        }
    }
}
