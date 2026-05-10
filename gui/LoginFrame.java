package gui;

import dao.LoginDao;
import db.ConnectionDb;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.Connection;

public class LoginFrame extends JFrame {

    private static final Color SIDEBAR = new Color(30,  60, 114);
    private static final Color ACCENT  = new Color(52, 152, 219);

    private final LoginDao loginDao = new LoginDao();

    public LoginFrame() {
        setTitle("Library Management System — Login");
        setSize(420, 340);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // ── Top bar ──────────────────────────────────────────────
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        topBar.setBackground(SIDEBAR);
        JLabel title = new JLabel("📚  Library Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        topBar.add(title);
        add(topBar, BorderLayout.NORTH);

        // ── Form card ─────────────────────────────────────────────
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(24, 32, 24, 32));

        GridBagConstraints g = new GridBagConstraints();
        g.fill    = GridBagConstraints.HORIZONTAL;
        g.insets  = new Insets(6, 0, 6, 0);
        g.weightx = 1.0;

        // Subtitle
        JLabel sub = new JLabel("Sign in to continue");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(100, 110, 130));
        g.gridx = 0; g.gridy = 0;
        card.add(sub, g);

        // Username label
        JLabel userLbl = new JLabel("Username");
        userLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.gridy = 1;
        card.add(userLbl, g);

        // Username field
        JTextField userField = new JTextField();
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userField.setPreferredSize(new Dimension(0, 36));
        userField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 210, 225), 1),
                new EmptyBorder(5, 8, 5, 8)));
        g.gridy = 2;
        card.add(userField, g);

        // Password label
        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.gridy = 3;
        card.add(passLbl, g);

        // Password field
        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passField.setPreferredSize(new Dimension(0, 36));
        passField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 210, 225), 1),
                new EmptyBorder(5, 8, 5, 8)));
        g.gridy = 4;
        card.add(passField, g);

        // Error label
        JLabel errLbl = new JLabel(" ");
        errLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errLbl.setForeground(new Color(200, 50, 50));
        g.gridy = 5;
        card.add(errLbl, g);

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginBtn.setBackground(ACCENT);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.setPreferredSize(new Dimension(0, 38));
        g.gridy = 6;
        card.add(loginBtn, g);

        add(card, BorderLayout.CENTER);

        // ── Login action ─────────────────────────────────────────
        Runnable doLogin = () -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                errLbl.setText("Please enter username and password.");
                return;
            }

            String fullName = loginDao.login(username, password);
            if (fullName != null) {
                dispose();
                SwingUtilities.invokeLater(main::new);
            } else {
                errLbl.setText("Invalid username or password.");
                passField.setText("");
            }
        };

        loginBtn.addActionListener(e -> doLogin.run());
        passField.addActionListener(e -> doLogin.run());

        setVisible(true);
    }

    public static void main(String[] args) {
        Connection conn = ConnectionDb.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null,
                "❌ Database connection failed!", "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}