package masterkey.ui;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.net.URL;

public class StartPanel extends JPanel {
    private final MainFrame mainFrame;

    public StartPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUi();
    }

    private void buildUi() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(45, 120, 60, 120));

        JPanel titlePanel = new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("MasterKey", JLabel.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(36f));

        JLabel iconLabel = new JLabel(loadStartIcon(), JLabel.CENTER);
        iconLabel.setPreferredSize(new Dimension(160, 170));

        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(iconLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 120;
        gbc.ipady = 12;
        gbc.gridy = 0;

        JButton createButton = new JButton("새 데이터베이스 생성");
        JButton openButton = new JButton("기존 데이터베이스 열기");
        JButton exitButton = new JButton("종료");

        createButton.addActionListener(e -> createDatabase());
        openButton.addActionListener(e -> openDatabase());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(createButton, gbc);
        gbc.gridy++;
        buttonPanel.add(openButton, gbc);
        gbc.gridy++;
        buttonPanel.add(exitButton, gbc);

        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private ImageIcon loadStartIcon() {
        URL iconUrl = getClass().getResource("/icon.png");
        if (iconUrl == null) {
            return null;
        }

        Image image = new ImageIcon(iconUrl).getImage();
        Image scaledImage = image.getScaledInstance(130, 155, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void createDatabase() {
        String databaseName = JOptionPane.showInputDialog(this, "데이터베이스 이름:", "새 데이터베이스 생성", JOptionPane.PLAIN_MESSAGE);
        if (databaseName == null || databaseName.isBlank()) {
            return;
        }

        JPasswordField passwordField = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(this, passwordField, "마스터 비밀번호", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String password = new String(passwordField.getPassword());
        if (password.length() < 4) {
            mainFrame.showError("비밀번호 오류", "마스터 비밀번호는 4자 이상이어야 합니다.\n확인을 누른 뒤 다시 입력하세요.");
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "마스터 비밀번호를 잃어버리면 복구키 없이는 데이터베이스를 열 수 없습니다.\n"
                        + "마스터 비밀번호와 복구키를 안전한 곳에 보관하세요.",
                "마스터 비밀번호 주의",
                JOptionPane.WARNING_MESSAGE
        );

        String recoveryKey = mainFrame.getDatabaseService().generateRecoveryKey();
        if (!showRecoveryKeyDialog(recoveryKey)) {
            return;
        }

        File defaultDirectory = new File("file");
        if (!defaultDirectory.exists()) {
            defaultDirectory.mkdirs();
        }

        JFileChooser chooser = new JFileChooser(defaultDirectory);
        chooser.setDialogTitle("데이터베이스 파일 저장");
        chooser.setSelectedFile(new File(databaseName + ".mkdb"));
        chooser.setFileFilter(new FileNameExtensionFilter("MasterKey Database (*.mkdb)", "mkdb"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = ensureMkdbExtension(chooser.getSelectedFile());
            mainFrame.createDatabase(databaseName, file, password, recoveryKey);
        }
    }

    private boolean showRecoveryKeyDialog(String recoveryKey) {
        JTextArea keyArea = new JTextArea(recoveryKey, 3, 36);
        keyArea.setLineWrap(true);
        keyArea.setWrapStyleWord(true);
        keyArea.setEditable(false);

        JButton copyButton = new JButton("복구키 복사");
        copyButton.addActionListener(e -> {
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(recoveryKey), null);
        });

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(new JLabel("아래 복구키를 반드시 따로 보관하세요."), BorderLayout.NORTH);
        panel.add(new JScrollPane(keyArea), BorderLayout.CENTER);
        panel.add(copyButton, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "복구키 만들기",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        return result == JOptionPane.OK_OPTION;
    }

    private void openDatabase() {
        File defaultDirectory = new File("file");
        if (!defaultDirectory.exists()) {
            defaultDirectory.mkdirs();
        }

        JFileChooser chooser = new JFileChooser(defaultDirectory);
        chooser.setDialogTitle("데이터베이스 파일 열기");
        chooser.setFileFilter(new FileNameExtensionFilter("MasterKey Database (*.mkdb)", "mkdb"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            mainFrame.showUnlock(chooser.getSelectedFile());
        }
    }

    private File ensureMkdbExtension(File file) {
        if (file.getName().toLowerCase().endsWith(".mkdb")) {
            return file;
        }
        return new File(file.getParentFile(), file.getName() + ".mkdb");
    }
}
