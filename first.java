import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.swing.Timer;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;

 class BankingSystem extends JFrame {
    // Colors
    private final Color PRIMARY_COLOR = new Color(0, 120, 215);
    private final Color SECONDARY_COLOR = new Color(240, 240, 240);
    private final Color ACCENT_COLOR = new Color(0, 153, 51);
    
    // Account data storage
    private Map<String, Account> accounts = new HashMap<>();
    private List<String> transactionHistory = new ArrayList<>();
    
    // UI Components
    private JPanel mainPanel, cardPanel;
    private CardLayout cardLayout;
    private JLabel titleLabel;
    private Timer animationTimer;
    private int yOffset = 0;
    private boolean direction = true;
    
    public BankingSystem() {
        setTitle("Modern Banking System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Initialize UI
        initUI();
        
        // Start background animation
        startAnimation();
    }
    
    private void initUI() {
        // Main panel with gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, new Color(230, 240, 255), 
                              getWidth(), getHeight(), new Color(180, 210, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Animated circles
                g2d.setColor(new Color(255, 255, 255, 30));
                for (int i = 0; i < 5; i++) {
                    int size = 100 + i * 50;
                    g2d.fillOval(i * 150 - 50, yOffset + i * 80 - 50, size, size);
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        
        // Title label with modern font
        titleLabel = new JLabel("Modern Banking System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Card panel for different screens
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        
        // Create all screens
        createMainMenu();
        createAccountScreen();
        createDepositScreen();
        createWithdrawScreen();
        createBalanceScreen();
        createHistoryScreen();
        
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Show main menu first
        cardLayout.show(cardPanel, "MainMenu");
    }
    
    private void startAnimation() {
        animationTimer = new Timer(30, e -> {
            if (direction) {
                yOffset++;
                if (yOffset > 20) direction = false;
            } else {
                yOffset--;
                if (yOffset < -20) direction = true;
            }
            mainPanel.repaint();
        });
        animationTimer.start();
    }
    
    private void createMainMenu() {
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 40, 100));
        
        String[] buttons = {
            "Create Account", "Deposit", "Withdraw", 
            "Check Balance", "Transaction History", "Exit"
        };
        
        for (String text : buttons) {
            JButton button = createStyledButton(text);
            button.addActionListener(e -> {
                switch (text) {
                    case "Create Account":
                        cardLayout.show(cardPanel, "CreateAccount");
                        break;
                    case "Deposit":
                        cardLayout.show(cardPanel, "Deposit");
                        break;
                    case "Withdraw":
                        cardLayout.show(cardPanel, "Withdraw");
                        break;
                    case "Check Balance":
                        cardLayout.show(cardPanel, "Balance");
                        break;
                    case "Transaction History":
                        cardLayout.show(cardPanel, "History");
                        break;
                    case "Exit":
                        exitApplication();
                        break;
                }
            });
            menuPanel.add(button);
        }
        
        cardPanel.add(menuPanel, "MainMenu");
    }
    
    private void createAccountScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel title = new JLabel("Create New Account", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);
        
        String[] labels = {"Full Name:", "Account Number:", "Initial Deposit:", "Password:"};
        JTextField[] fields = new JTextField[labels.length];
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i+1; gbc.gridwidth = 1;
            panel.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1;
            fields[i] = new JTextField(20);
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panel.add(fields[i], gbc);
        }
        
        JButton createBtn = createStyledButton("Create Account");
        gbc.gridx = 0; gbc.gridy = labels.length+1; gbc.gridwidth = 2;
        panel.add(createBtn, gbc);
        
        JButton backBtn = createStyledButton("Back to Menu");
        gbc.gridy = labels.length+2;
        panel.add(backBtn, gbc);
        
        createBtn.addActionListener(e -> {
            try {
                String name = fields[0].getText().trim();
                String accNumber = fields[1].getText().trim();
                double initialDeposit = Double.parseDouble(fields[2].getText().trim());
                String password = fields[3].getText().trim();
                
                if (name.isEmpty() || accNumber.isEmpty() || password.isEmpty()) {
                    showMessage("All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (accounts.containsKey(accNumber)) {
                    showMessage("Account number already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (initialDeposit < 500) {
                    showMessage("Initial deposit must be at least 500", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Account newAccount = new Account(name, accNumber, password, initialDeposit);
                accounts.put(accNumber, newAccount);
                
                // Add to transaction history
                String transaction = String.format("[%s] Account Created: %s (%s) with initial deposit: ₹%.2f", 
                    getCurrentDateTime(), name, accNumber, initialDeposit);
                transactionHistory.add(transaction);
                
                // Clear fields
                for (JTextField field : fields) field.setText("");
                
                showMessage("Account created successfully!\nAccount Number: " + accNumber, 
                           "Success", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException ex) {
                showMessage("Please enter valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "MainMenu"));
        
        cardPanel.add(panel, "CreateAccount");
    }
    
    private void createDepositScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel title = new JLabel("Deposit Money", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);
        
        String[] labels = {"Account Number:", "Password:", "Amount:"};
        JTextField[] fields = new JTextField[labels.length];
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i+1; gbc.gridwidth = 1;
            panel.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1;
            fields[i] = new JTextField(20);
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            if (i == 2) fields[i].setToolTipText("Enter amount to deposit");
            panel.add(fields[i], gbc);
        }
        
        JButton depositBtn = createStyledButton("Deposit");
        gbc.gridx = 0; gbc.gridy = labels.length+1; gbc.gridwidth = 2;
        panel.add(depositBtn, gbc);
        
        JButton backBtn = createStyledButton("Back to Menu");
        gbc.gridy = labels.length+2;
        panel.add(backBtn, gbc);
        
        depositBtn.addActionListener(e -> {
            try {
                String accNumber = fields[0].getText().trim();
                String password = fields[1].getText().trim();
                double amount = Double.parseDouble(fields[2].getText().trim());
                
                if (!accounts.containsKey(accNumber)) {
                    showMessage("Account not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Account account = accounts.get(accNumber);
                if (!account.password.equals(password)) {
                    showMessage("Invalid password!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (amount <= 0) {
                    showMessage("Amount must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                account.balance += amount;
                
                // Add to transaction history
                String transaction = String.format("[%s] Deposit: %s deposited ₹%.2f. New balance: ₹%.2f", 
                    getCurrentDateTime(), account.name, amount, account.balance);
                transactionHistory.add(transaction);
                
                // Clear amount field
                fields[2].setText("");
                
                showMessage(String.format("₹%.2f deposited successfully!\nNew Balance: ₹%.2f", 
                    amount, account.balance), "Success", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException ex) {
                showMessage("Please enter valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        backBtn.addActionListener(e -> {
            for (JTextField field : fields) field.setText("");
            cardLayout.show(cardPanel, "MainMenu");
        });
        
        cardPanel.add(panel, "Deposit");
    }
    
    private void createWithdrawScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel title = new JLabel("Withdraw Money", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);
        
        String[] labels = {"Account Number:", "Password:", "Amount:"};
        JTextField[] fields = new JTextField[labels.length];
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i+1; gbc.gridwidth = 1;
            panel.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1;
            fields[i] = new JTextField(20);
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            if (i == 2) fields[i].setToolTipText("Enter amount to withdraw");
            panel.add(fields[i], gbc);
        }
        
        JButton withdrawBtn = createStyledButton("Withdraw");
        gbc.gridx = 0; gbc.gridy = labels.length+1; gbc.gridwidth = 2;
        panel.add(withdrawBtn, gbc);
        
        JButton backBtn = createStyledButton("Back to Menu");
        gbc.gridy = labels.length+2;
        panel.add(backBtn, gbc);
        
        withdrawBtn.addActionListener(e -> {
            try {
                String accNumber = fields[0].getText().trim();
                String password = fields[1].getText().trim();
                double amount = Double.parseDouble(fields[2].getText().trim());
                
                if (!accounts.containsKey(accNumber)) {
                    showMessage("Account not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Account account = accounts.get(accNumber);
                if (!account.password.equals(password)) {
                    showMessage("Invalid password!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (amount <= 0) {
                    showMessage("Amount must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (account.balance < amount) {
                    showMessage("Insufficient balance!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                account.balance -= amount;
                
                // Add to transaction history
                String transaction = String.format("[%s] Withdrawal: %s withdrew ₹%.2f. New balance: ₹%.2f", 
                    getCurrentDateTime(), account.name, amount, account.balance);
                transactionHistory.add(transaction);
                
                // Clear amount field
                fields[2].setText("");
                
                showMessage(String.format("₹%.2f withdrawn successfully!\nNew Balance: ₹%.2f", 
                    amount, account.balance), "Success", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException ex) {
                showMessage("Please enter valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        backBtn.addActionListener(e -> {
            for (JTextField field : fields) field.setText("");
            cardLayout.show(cardPanel, "MainMenu");
        });
        
        cardPanel.add(panel, "Withdraw");
    }
    
    private void createBalanceScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel title = new JLabel("Check Balance", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);
        
        String[] labels = {"Account Number:", "Password:"};
        JTextField[] fields = new JTextField[labels.length];
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i+1; gbc.gridwidth = 1;
            panel.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1;
            fields[i] = new JTextField(20);
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panel.add(fields[i], gbc);
        }
        
        JLabel balanceLabel = new JLabel("", SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        balanceLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = labels.length+1; gbc.gridwidth = 2;
        panel.add(balanceLabel, gbc);
        
        JButton checkBtn = createStyledButton("Check Balance");
        gbc.gridx = 0; gbc.gridy = labels.length+2; gbc.gridwidth = 2;
        panel.add(checkBtn, gbc);
        
        JButton backBtn = createStyledButton("Back to Menu");
        gbc.gridy = labels.length+3;
        panel.add(backBtn, gbc);
        
        checkBtn.addActionListener(e -> {
            String accNumber = fields[0].getText().trim();
            String password = fields[1].getText().trim();
            
            if (!accounts.containsKey(accNumber)) {
                showMessage("Account not found!", "Error", JOptionPane.ERROR_MESSAGE);
                balanceLabel.setText("");
                return;
            }
            
            Account account = accounts.get(accNumber);
            if (!account.password.equals(password)) {
                showMessage("Invalid password!", "Error", JOptionPane.ERROR_MESSAGE);
                balanceLabel.setText("");
                return;
            }
            
            balanceLabel.setText(String.format("Current Balance: ₹%.2f", account.balance));
            
            // Add to transaction history
            String transaction = String.format("[%s] Balance Check: %s checked balance. Current: ₹%.2f", 
                getCurrentDateTime(), account.name, account.balance);
            transactionHistory.add(transaction);
        });
        
        backBtn.addActionListener(e -> {
            fields[0].setText("");
            fields[1].setText("");
            balanceLabel.setText("");
            cardLayout.show(cardPanel, "MainMenu");
        });
        
        cardPanel.add(panel, "Balance");
    }
    
    private void createHistoryScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JLabel title = new JLabel("Transaction History", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        
        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        historyArea.setBackground(new Color(240, 240, 240));
        historyArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton refreshBtn = createStyledButton("Refresh");
        JButton backBtn = createStyledButton("Back to Menu");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        refreshBtn.addActionListener(e -> {
            if (transactionHistory.isEmpty()) {
                historyArea.setText("No transactions recorded yet.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (String transaction : transactionHistory) {
                    sb.append(transaction).append("\n\n");
                }
                historyArea.setText(sb.toString());
            }
        });
        
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "MainMenu"));
        
        cardPanel.add(panel, "History");
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(PRIMARY_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(PRIMARY_COLOR.brighter());
                } else {
                    g2.setColor(PRIMARY_COLOR);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(200, 50));
        
        return button;
    }
    
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    private String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
    
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to exit?", 
            "Exit Confirmation", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            animationTimer.stop();
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set modern look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            BankingSystem bankingSystem = new BankingSystem();
            bankingSystem.setVisible(true);
        });
    }
    
    // Inner class for Account
    private static class Account {
        String name;
        String accountNumber;
        String password;
        double balance;
        
        public Account(String name, String accountNumber, String password, double balance) {
            this.name = name;
            this.accountNumber = accountNumber;
            this.password = password;
            this.balance = balance;
        }
    }
}