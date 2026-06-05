package masterkey.ui;

import masterkey.service.PasswordService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;

public class PasswordGeneratorDialog extends JDialog {
    private final MainFrame owner;
    private final PasswordService passwordService;
    private final Consumer<String> usePasswordConsumer;
    private final JSpinner lengthSpinner;
    private final JCheckBox uppercaseBox;
    private final JCheckBox lowercaseBox;
    private final JCheckBox numbersBox;
    private final JCheckBox specialBox;
    private final JTextField excludedField;
    private final JTextField resultField;

    public PasswordGeneratorDialog(MainFrame owner, PasswordService passwordService) {
        this(owner, passwordService, null);
    }

    public PasswordGeneratorDialog(MainFrame owner, PasswordService passwordService, Consumer<String> usePasswordConsumer) {
        super(owner, "비밀번호 생성", true);
        this.owner = owner;
        this.passwordService = passwordService;
        this.usePasswordConsumer = usePasswordConsumer;
        this.lengthSpinner = new JSpinner(new SpinnerNumberModel(16, 4, 128, 1));
        this.uppercaseBox = new JCheckBox("대문자", true);
        this.lowercaseBox = new JCheckBox("소문자", true);
        this.numbersBox = new JCheckBox("숫자", true);
        this.specialBox = new JCheckBox("특수문자", true);
        this.excludedField = new JTextField(20);
        this.resultField = new JTextField(28);
        this.resultField.setEditable(false);

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

        gbc.gridx = 0;
        formPanel.add(new JLabel("길이:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lengthSpinner, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        formPanel.add(new JLabel("옵션:"), gbc);
        gbc.gridx = 1;
        JPanel optionPanel = new JPanel();
        optionPanel.add(uppercaseBox);
        optionPanel.add(lowercaseBox);
        optionPanel.add(numbersBox);
        optionPanel.add(specialBox);
        formPanel.add(optionPanel, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        formPanel.add(new JLabel("제외 문자:"), gbc);
        gbc.gridx = 1;
        formPanel.add(excludedField, gbc);
        gbc.gridy++;

        gbc.gridx = 0;
        formPanel.add(new JLabel("생성 결과:"), gbc);
        gbc.gridx = 1;
        formPanel.add(resultField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton generateButton = new JButton("생성");
        JButton copyButton = new JButton("복사");
        JButton useButton = new JButton("이 비밀번호 사용");
        JButton closeButton = new JButton("닫기");

        generateButton.addActionListener(e -> generate());
        copyButton.addActionListener(e -> {
            passwordService.copyToClipboard(resultField.getText());
            owner.showInfo("복사 완료", "생성된 비밀번호를 클립보드에 복사했습니다.");
        });
        useButton.addActionListener(e -> usePassword());
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(generateButton);
        buttonPanel.add(copyButton);
        if (usePasswordConsumer != null) {
            buttonPanel.add(useButton);
        }
        buttonPanel.add(closeButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void generate() {
        try {
            String password = passwordService.generatePassword(
                    (Integer) lengthSpinner.getValue(),
                    uppercaseBox.isSelected(),
                    lowercaseBox.isSelected(),
                    numbersBox.isSelected(),
                    specialBox.isSelected(),
                    excludedField.getText()
            );
            resultField.setText(password);
        } catch (Exception e) {
            owner.showError("비밀번호 생성 실패", e.getMessage());
        }
    }

    private void usePassword() {
        if (resultField.getText().isBlank()) {
            generate();
        }
        if (!resultField.getText().isBlank() && usePasswordConsumer != null) {
            usePasswordConsumer.accept(resultField.getText());
            dispose();
        }
    }
}
