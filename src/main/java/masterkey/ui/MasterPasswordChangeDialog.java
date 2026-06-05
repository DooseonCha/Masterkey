package masterkey.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class MasterPasswordChangeDialog extends JDialog {
    private final MainFrame owner;
    private final JTextField recoveryKeyField;
    private final JPasswordField newPasswordField;
    private final JPasswordField confirmPasswordField;

    public MasterPasswordChangeDialog(MainFrame owner) {
        super(owner, "마스터 비밀번호 변경", true);
        this.owner = owner;
        this.recoveryKeyField = new JTextField(28);
        this.newPasswordField = new JPasswordField(24);
        this.confirmPasswordField = new JPasswordField(24);

        buildUi();
        pack();
        setLocationRelativeTo(owner);
    }

    private void buildUi() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;

        addRow(formPanel, gbc, "복구키:", recoveryKeyField);
        addRow(formPanel, gbc, "새 비밀번호:", newPasswordField);
        addRow(formPanel, gbc, "새 비밀번호 확인:", confirmPasswordField);

        JPanel buttonPanel = new JPanel();
        JButton changeButton = new JButton("변경");
        JButton cancelButton = new JButton("취소");

        changeButton.addActionListener(e -> changePassword());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(changeButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, String label, java.awt.Component field) {
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
        gbc.gridy++;
    }

    private void changePassword() {
        String recoveryKey = recoveryKeyField.getText();
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!newPassword.equals(confirmPassword)) {
            owner.showError("비밀번호 확인 오류", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return;
        }

        try {
            owner.getDatabaseService().changeMasterPassword(recoveryKey, newPassword);
            owner.showInfo("변경 완료", "마스터 비밀번호가 변경되었습니다.");
            dispose();
        } catch (Exception e) {
            owner.showError("마스터 비밀번호 변경 실패", e.getMessage());
        }
    }
}
