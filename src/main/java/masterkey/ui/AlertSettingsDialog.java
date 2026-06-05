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
import java.util.List;

public class AlertSettingsDialog extends JDialog {
    private final MainFrame owner;
    private final AlertService alertService;
    private final JCheckBox enabledBox;
    private final JSpinner periodSpinner;
    private final JTextArea resultArea;

    public AlertSettingsDialog(MainFrame owner, AlertService alertService) {
        super(owner, "Last Change Alert", true);
        this.owner = owner;
        this.alertService = alertService;

        var settings = owner.getDatabaseService().getCurrentDatabase().getUserSettings();
        this.enabledBox = new JCheckBox("Enable alert", settings.isAlertEnabled());
        this.periodSpinner = new JSpinner(new SpinnerNumberModel(settings.getPasswordChangePeriodDays(), 1, 3650, 1));
        this.resultArea = new JTextArea(10, 42);
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
        settingPanel.add(new JLabel("Password change period days:"), gbc);
        gbc.gridx = 1;
        settingPanel.add(periodSpinner, gbc);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save Setting");
        JButton checkButton = new JButton("Check");
        JButton closeButton = new JButton("Close");

        saveButton.addActionListener(e -> saveSetting());
        checkButton.addActionListener(e -> checkAlerts());
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(checkButton);
        buttonPanel.add(closeButton);

        add(settingPanel, BorderLayout.NORTH);
        add(resultArea, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveSetting() {
        try {
            alertService.updateAlertSettings(enabledBox.isSelected(), (Integer) periodSpinner.getValue());
            owner.showInfo("Saved", "Alert setting was saved.");
        } catch (Exception e) {
            owner.showError("Save Failed", e.getMessage());
        }
    }

    private void checkAlerts() {
        saveSetting();
        List<Entry> expiredEntries = alertService.getExpiredEntries();

        if (expiredEntries.isEmpty()) {
            resultArea.setText("No expired passwords.");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (Entry entry : expiredEntries) {
            builder.append("- ")
                    .append(entry.getSiteName())
                    .append(" / ")
                    .append(entry.getUserId())
                    .append("\n");
        }
        resultArea.setText(builder.toString());
    }
}
