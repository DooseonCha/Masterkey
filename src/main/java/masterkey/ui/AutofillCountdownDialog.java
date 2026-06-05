package masterkey.ui;

import masterkey.service.PasswordService;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Font;

public class AutofillCountdownDialog extends JDialog {
    private final MainFrame owner;
    private final PasswordService passwordService;
    private final String userId;
    private final String password;
    private final JLabel countLabel;
    private int count;

    public AutofillCountdownDialog(MainFrame owner, PasswordService passwordService, String userId, String password) {
        super(owner, "자동 채우기 카운트다운", false);
        this.owner = owner;
        this.passwordService = passwordService;
        this.userId = userId;
        this.password = password;
        this.countLabel = new JLabel("3", SwingConstants.CENTER);
        this.count = 3;

        buildUi();
        setSize(360, 230);
        setLocationRelativeTo(owner);
        setAlwaysOnTop(true);
    }

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel("카운트다운이 끝나기 전에 로그인 입력칸을 클릭하세요.", SwingConstants.CENTER);
        countLabel.setFont(countLabel.getFont().deriveFont(Font.BOLD, 72f));

        root.add(messageLabel, BorderLayout.NORTH);
        root.add(countLabel, BorderLayout.CENTER);
        setContentPane(root);
    }

    public void startCountdown() {
        passwordService.prepareAutofill(userId, password);
        setVisible(true);

        Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            count--;
            if (count > 0) {
                countLabel.setText(String.valueOf(count));
                return;
            }

            timer.stop();
            countLabel.setText("입력");

            Thread pasteThread = new Thread(() -> {
                try {
                    Thread.sleep(250);
                    passwordService.pasteFromClipboard();
                    SwingUtilities.invokeLater(this::dispose);
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        dispose();
                        owner.showError("자동 채우기 실패", ex.getMessage());
                    });
                }
            });
            pasteThread.setDaemon(true);
            pasteThread.start();
        });
        timer.setInitialDelay(1000);
        timer.start();
    }
}
