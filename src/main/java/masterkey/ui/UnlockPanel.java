package masterkey.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

public class UnlockPanel extends JPanel {
    private final MainFrame mainFrame;
    private final JLabel fileLabel;
    private final JPasswordField passwordField;

    public UnlockPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.fileLabel = new JLabel("선택된 파일 없음");
        this.passwordField = new JPasswordField(24);
        buildUi();
    }

    private void buildUi() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(80, 120, 80, 120));

        JLabel titleLabel = new JLabel("데이터베이스 잠금 해제", JLabel.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(28f));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        formPanel.add(new JLabel("데이터베이스 파일:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fileLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("마스터 비밀번호:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton openButton = new JButton("열기");
        JButton recoveryButton = new JButton("복구키로 열기");
        JButton cancelButton = new JButton("취소");
        JButton exitButton = new JButton("종료");

        openButton.addActionListener(e -> {
            mainFrame.openDatabase(new String(passwordField.getPassword()));
            passwordField.setText("");
        });
        recoveryButton.addActionListener(e -> openWithRecoveryKey());
        cancelButton.addActionListener(e -> mainFrame.showStart());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(openButton);
        buttonPanel.add(recoveryButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(exitButton);

        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void openWithRecoveryKey() {
        JTextArea recoveryArea = new JTextArea(4, 36);
        recoveryArea.setLineWrap(true);
        recoveryArea.setWrapStyleWord(true);

        int result = JOptionPane.showConfirmDialog(
                this,
                new JScrollPane(recoveryArea),
                "복구키 입력",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            mainFrame.openDatabaseWithRecoveryKey(recoveryArea.getText().trim());
        }
    }

    public void setDatabaseFile(File file) {
        fileLabel.setText(file == null ? "선택된 파일 없음" : file.getAbsolutePath());
        passwordField.setText("");
    }
}
