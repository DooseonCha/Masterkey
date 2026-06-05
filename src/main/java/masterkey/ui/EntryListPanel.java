package masterkey.ui;

import masterkey.domain.Backup;
import masterkey.domain.Entry;
import masterkey.domain.UserSettings;
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
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EntryListPanel extends JPanel {
    private final MainFrame mainFrame;
    private final EntryService entryService;
    private final PasswordService passwordService;
    private final AlertService alertService;
    private final BackupService backupService;

    private final DefaultTableModel tableModel;
    private final JTable entryTable;
    private JButton alertButton;
    private Color defaultButtonBackground;

    public EntryListPanel(MainFrame mainFrame, EntryService entryService, PasswordService passwordService,
                          AlertService alertService, BackupService backupService) {
        this.mainFrame = mainFrame;
        this.entryService = entryService;
        this.passwordService = passwordService;
        this.alertService = alertService;
        this.backupService = backupService;

        this.tableModel = new DefaultTableModel(new Object[]{"사이트", "URL", "사용자 ID", "강도", "수정일", "기한 상태", "초과 일수"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.entryTable = new JTable(tableModel);
        this.entryTable.setDefaultRenderer(Object.class, new ExpiredEntryRenderer());
        this.entryTable.getColumnModel().getColumn(3).setCellRenderer(new RoundedStrengthBarRenderer());
        this.entryTable.setRowHeight(28);

        buildUi();
    }

    private void buildUi() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));

        JButton addButton = new JButton("추가");
        JButton editButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");
        JButton historyButton = new JButton("비밀번호 이력");
        JButton refreshButton = new JButton("새로고침");

        JButton generateButton = new JButton("비밀번호 생성");
        JButton copyButton = new JButton("비밀번호 복사");
        JButton autofillButton = new JButton("자동 채우기");

        alertButton = new JButton("변경 기한 알림");
        JButton backupButton = new JButton("백업");
        JButton autoBackupButton = new JButton("자동 백업");
        JButton changeMasterPasswordButton = new JButton("마스터 비밀번호 변경");
        JButton lockButton = new JButton("잠금");

        changeMasterPasswordButton.setToolTipText("복구키와 새 비밀번호를 입력하여 마스터 비밀번호를 변경합니다.");

        defaultButtonBackground = alertButton.getBackground();

        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);
        topPanel.add(historyButton);
        topPanel.add(refreshButton);
        topPanel.add(generateButton);
        topPanel.add(copyButton);
        topPanel.add(autofillButton);
        topPanel.add(alertButton);
        topPanel.add(backupButton);
        topPanel.add(autoBackupButton);
        topPanel.add(changeMasterPasswordButton);
        topPanel.add(lockButton);

        addButton.addActionListener(e -> addEntry());
        editButton.addActionListener(e -> editEntry());
        deleteButton.addActionListener(e -> deleteEntry());
        historyButton.addActionListener(e -> showHistory());
        generateButton.addActionListener(e -> generatePassword());
        copyButton.addActionListener(e -> copyPassword());
        autofillButton.addActionListener(e -> autofill());
        alertButton.addActionListener(e -> showAlertDialog());
        backupButton.addActionListener(e -> backup());
        autoBackupButton.addActionListener(e -> autoBackupSetting());
        changeMasterPasswordButton.addActionListener(e -> changeMasterPassword());
        refreshButton.addActionListener(e -> refreshTable());
        lockButton.addActionListener(e -> mainFrame.lockDatabase());

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(entryTable), BorderLayout.CENTER);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        updateAlertButtonStyle();

        for (Entry entry : entryService.getEntries()) {
            long exceededDays = getExceededDays(entry);
            String status = exceededDays > 0 ? "기한 초과" : "정상";
            String exceededText = exceededDays > 0 ? exceededDays + "일" : "-";

            tableModel.addRow(new Object[]{
                    entry.getSiteName(),
                    entry.getSiteUrl(),
                    entry.getUserId(),
                    entry.getPassword().getStrengthLevel(),
                    entry.getModifiedAt(),
                    status,
                    exceededText
            });
        }
        entryTable.repaint();
    }

    private void updateAlertButtonStyle() {
        if (alertButton == null || mainFrame.getDatabaseService().getCurrentDatabase() == null) {
            return;
        }

        boolean enabled = mainFrame.getDatabaseService()
                .getCurrentDatabase()
                .getUserSettings()
                .isAlertEnabled();

        if (enabled) {
            alertButton.setBackground(Color.YELLOW);
            alertButton.setForeground(Color.BLACK);
            alertButton.setOpaque(true);
            alertButton.setContentAreaFilled(true);
            alertButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
            alertButton.setFocusPainted(false);
            alertButton.setToolTipText("비밀번호 변경 기한 알림이 켜져 있습니다.");
        } else {
            alertButton.setBackground(defaultButtonBackground != null ? defaultButtonBackground : UIManager.getColor("Button.background"));
            alertButton.setForeground(UIManager.getColor("Button.foreground"));
            alertButton.setOpaque(true);
            alertButton.setContentAreaFilled(true);
            alertButton.setBorder(UIManager.getBorder("Button.border"));
            alertButton.setFocusPainted(true);
            alertButton.setToolTipText("비밀번호 변경 기한 알림이 꺼져 있습니다.");
        }
    }

    private Entry getSelectedEntry() {
        int selectedRow = entryTable.getSelectedRow();
        if (selectedRow < 0) {
            mainFrame.showError("선택 필요", "엔트리를 선택해야 합니다.");
            return null;
        }

        int modelRow = entryTable.convertRowIndexToModel(selectedRow);
        List<Entry> entries = entryService.getEntries();
        if (modelRow >= entries.size()) {
            return null;
        }
        return entries.get(modelRow);
    }

    private void addEntry() {
        EntryFormDialog dialog = new EntryFormDialog(mainFrame, passwordService);
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
                mainFrame.showError("엔트리 추가 실패", e.getMessage());
            }
        }
    }

    private void editEntry() {
        Entry entry = getSelectedEntry();
        if (entry == null) {
            return;
        }

        EntryFormDialog dialog = new EntryFormDialog(mainFrame, passwordService, entry);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            try {
                entryService.updateEntry(
                        entry.getEntryId(),
                        dialog.getSiteNameValue(),
                        dialog.getSiteUrlValue(),
                        dialog.getUserIdValue(),
                        dialog.getPasswordValue()
                );
                refreshTable();
            } catch (Exception e) {
                mainFrame.showError("엔트리 수정 실패", e.getMessage());
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
                "선택한 엔트리를 삭제하시겠습니까?",
                "엔트리 삭제",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            try {
                entryService.deleteEntry(entry.getEntryId());
                refreshTable();
            } catch (Exception e) {
                mainFrame.showError("삭제 실패", e.getMessage());
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
        mainFrame.showInfo("복사 완료", "비밀번호를 클립보드에 복사했습니다.");
    }

    private void autofill() {
        Entry entry = getSelectedEntry();
        if (entry == null) {
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "확인을 누르면 3초 카운트다운이 시작됩니다.\n카운트다운이 끝나기 전에 로그인 입력칸을 클릭하세요.",
                "자동 채우기",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            AutofillCountdownDialog dialog = new AutofillCountdownDialog(
                    mainFrame,
                    passwordService,
                    entry.getUserId(),
                    entry.getPassword().getPasswordValue()
            );
            dialog.startCountdown();
        }
    }

    private void showAlertDialog() {
        AlertSettingsDialog dialog = new AlertSettingsDialog(mainFrame, alertService);
        dialog.setVisible(true);
        refreshTable();
    }

    private void backup() {
        File defaultDirectory = new File("file");
        if (!defaultDirectory.exists()) {
            defaultDirectory.mkdirs();
        }

        JFileChooser chooser = new JFileChooser(defaultDirectory);
        chooser.setDialogTitle("백업 파일 저장");
        chooser.setSelectedFile(new File("masterkey_backup.mkdb"));
        chooser.setFileFilter(new FileNameExtensionFilter("MasterKey Database Backup (*.mkdb)", "mkdb"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Backup backup = backupService.backup(chooser.getSelectedFile());
                mainFrame.showInfo("백업 완료", "백업 파일이 생성되었습니다.\n" + chooser.getSelectedFile().getAbsolutePath());
                refreshTable();
            } catch (Exception e) {
                mainFrame.showError("백업 실패", e.getMessage());
            }
        }
    }

    private void autoBackupSetting() {
        JCheckBox enabled = new JCheckBox("자동 백업 사용");
        boolean current = mainFrame.getDatabaseService()
                .getCurrentDatabase()
                .getUserSettings()
                .isAutoBackupEnabled();
        enabled.setSelected(current);

        int result = JOptionPane.showConfirmDialog(
                this,
                enabled,
                "자동 백업 설정",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                alertService.updateAutoBackup(enabled.isSelected());
                mainFrame.showInfo("자동 백업", "자동 백업 설정이 변경되었습니다.");
            } catch (Exception e) {
                mainFrame.showError("설정 실패", e.getMessage());
            }
        }
    }

    private void changeMasterPassword() {
        MasterPasswordChangeDialog dialog = new MasterPasswordChangeDialog(mainFrame);
        dialog.setVisible(true);
    }

    private boolean isExpiredEntry(int modelRow) {
        if (mainFrame.getDatabaseService().getCurrentDatabase() == null) {
            return false;
        }

        List<Entry> entries = entryService.getEntries();
        if (modelRow < 0 || modelRow >= entries.size()) {
            return false;
        }

        return getExceededDays(entries.get(modelRow)) > 0;
    }

    private long getExceededDays(Entry entry) {
        if (entry == null || mainFrame.getDatabaseService().getCurrentDatabase() == null) {
            return 0;
        }

        UserSettings settings = mainFrame.getDatabaseService().getCurrentDatabase().getUserSettings();
        if (!settings.isAlertEnabled() || entry.getPassword() == null || entry.getPassword().getLastChangedAt() == null) {
            return 0;
        }

        LocalDateTime dueDate = entry.getPassword().getLastChangedAt().plusDays(settings.getPasswordChangePeriodDays());
        long exceededDays = ChronoUnit.DAYS.between(dueDate, LocalDateTime.now());
        return Math.max(exceededDays, 0);
    }

    private class ExpiredEntryRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                int modelRow = table.convertRowIndexToModel(row);
                if (isExpiredEntry(modelRow)) {
                    component.setForeground(Color.RED);
                } else {
                    component.setForeground(Color.BLACK);
                }
            }

            return component;
        }
    }

    private class RoundedStrengthBarRenderer extends JPanel implements TableCellRenderer {
        private int strength;
        private boolean selected;

        public RoundedStrengthBarRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            int parsedStrength = 0;
            if (value instanceof Number number) {
                parsedStrength = number.intValue();
            }
            this.strength = Math.max(0, Math.min(parsedStrength, 5));
            this.selected = isSelected;
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int marginX = 10;
            int marginY = 7;
            int barWidth = Math.max(1, getWidth() - marginX * 2);
            int barHeight = Math.max(1, getHeight() - marginY * 2);
            int arc = barHeight;

            g2.setColor(selected ? new Color(230, 230, 230) : new Color(235, 235, 235));
            g2.fillRoundRect(marginX, marginY, barWidth, barHeight, arc, arc);

            int filledWidth = Math.round(barWidth * (strength / 5.0f));
            if (filledWidth > 0) {
                g2.setColor(getStrengthColor(strength));
                g2.fillRoundRect(marginX, marginY, filledWidth, barHeight, arc, arc);
            }

            String text = strength + " / 5";
            FontMetrics fm = g2.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(text)) / 2;
            int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(Color.BLACK);
            g2.drawString(text, textX, textY);

            g2.dispose();
        }

        private Color getStrengthColor(int strength) {
            if (strength <= 2) {
                return new Color(230, 90, 90);
            }
            if (strength <= 4) {
                return new Color(245, 185, 70);
            }
            return new Color(90, 180, 100);
        }
    }
}
