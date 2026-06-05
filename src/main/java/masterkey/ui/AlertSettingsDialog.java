package masterkey.ui;

import masterkey.domain.Entry;
import masterkey.service.AlertService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AlertSettingsDialog extends JDialog {
    private final MainFrame owner;
    private final AlertService alertService;
    private final JCheckBox enabledBox;
    private final JSpinner periodSpinner;
    private final JTextArea resultArea;

    public AlertSettingsDialog(MainFrame owner, AlertService alertService) {
        super(owner, "비밀번호 변경 기한 알림", true);
        this.owner = owner;
        this.alertService = alertService;

        var settings = owner.getDatabaseService().getCurrentDatabase().getUserSettings();
        this.enabledBox = new JCheckBox("알림 사용", settings.isAlertEnabled());
        this.periodSpinner = new JSpinner(new SpinnerNumberModel(settings.getPasswordChangePeriodDays(), 1, 3650, 1));
        this.resultArea = new JTextArea(10, 48);
        this.resultArea.setEditable(false);

        buildUi();
        pack();
        setLocationRelativeTo(owner);
    }

    private void buildUi() {
        JPanel settingPanel = new JPanel(new GridBagLayout());
        settingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;

        gbc.gridx = 0;
        settingPanel.add(enabledBox, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        settingPanel.add(new JLabel("비밀번호 변경 주기일:"), gbc);
        gbc.gridx = 1;
        settingPanel.add(periodSpinner, gbc);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("설정 저장");
        JButton checkButton = new JButton("검사");
        JButton closeButton = new JButton("닫기");

        saveButton.addActionListener(e -> saveSetting(true));
        checkButton.addActionListener(e -> checkAlerts());
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(checkButton);
        buttonPanel.add(closeButton);

        add(settingPanel, BorderLayout.NORTH);
        add(resultArea, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveSetting(boolean showMessage) {
        try {
            alertService.updateAlertSettings(enabledBox.isSelected(), (Integer) periodSpinner.getValue());
            if (showMessage) {
                owner.showInfo("저장 완료", "알림 설정이 저장되었습니다.");
            }
        } catch (Exception e) {
            owner.showError("저장 실패", e.getMessage());
        }
    }

    private void checkAlerts() {
        saveSetting(false);
        List<Entry> expiredEntries = alertService.getExpiredEntries();

        if (expiredEntries.isEmpty()) {
            resultArea.setText("변경 기한이 지난 비밀번호가 없습니다.");
            return;
        }

        int periodDays = (Integer) periodSpinner.getValue();
        StringBuilder builder = new StringBuilder();
        builder.append("변경 기한이 지난 엔트리:\n");

        for (Entry entry : expiredEntries) {
            LocalDateTime lastChangedAt = entry.getPassword().getLastChangedAt();
            LocalDateTime dueDate = lastChangedAt.plusDays(periodDays);
            long exceededDays = Math.max(ChronoUnit.DAYS.between(dueDate, LocalDateTime.now()), 0);

            builder.append("- 사이트: ")
                    .append(entry.getSiteName())
                    .append(" / 사용자 ID: ")
                    .append(entry.getUserId())
                    .append(" / 초과: ")
                    .append(exceededDays)
                    .append("일")
                    .append(" / 마지막 변경일: ")
                    .append(lastChangedAt.toLocalDate())
                    .append("\n");
        }
        resultArea.setText(builder.toString());
    }
}
