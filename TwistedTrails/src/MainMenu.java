import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;

public class MainMenu extends JFrame {
    private int userProgress;

    public MainMenu() {
        setTitle("Twisted Trails"); // Set the title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation
        setSize(400, 300); // Set size of the window
        setLocationRelativeTo(null); // Center the window on the screen

        // Main panel with custom painting and layout
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // Custom painting code can be added here
            }
        };
        mainPanel.setLayout(new GridBagLayout()); // Set layout manager
        GridBagConstraints gbc = new GridBagConstraints(); // Constraints for layout
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 50, 5, 50); // Insets for components

        // Welcome label
        JLabel welcomeLabel = new JLabel("<html><center>Welcome to Twisted Trails!<br>Start your adventure here!</center></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        gbc.insets = new Insets(10, 50, 10, 50);
        mainPanel.add(welcomeLabel, gbc); // Add label to panel

        // Load and set an icon for the frame
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/images/Screenshot 2024-01-04 210921.png")));
        Image image = icon.getImage();
        setIconImage(image);

        // Buttons for different actions
        JButton createAccountButton = new JButton("Create New Account");
        JButton loginButton = new JButton("Log In");
        JButton exitButton = new JButton("Exit");

        // Set fonts and add buttons to the panel
        Font buttonFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        createAccountButton.setFont(buttonFont);
        loginButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        // Add action listeners to handle button clicks
        createAccountButton.addActionListener(e -> handleCreateAccount());
        loginButton.addActionListener(e -> handleLogin());
        exitButton.addActionListener(e -> handleExit());

        // Customize appearance and behavior of buttons
        customizeButton(createAccountButton);
        customizeButton(loginButton);
        customizeButton(exitButton);

        // Add buttons to the main panel
        mainPanel.add(createAccountButton, gbc);
        mainPanel.add(loginButton, gbc);
        mainPanel.add(exitButton, gbc);

        // Add main panel to the frame
        add(mainPanel);
    }

    // Method to customize button appearance and behavior
    private void customizeButton(JButton button) {
        Font buttonFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        button.setFont(buttonFont);
        button.setBackground(new Color(0, 100, 0)); // Light green color
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false); // Remove focus border
        button.setBorderPainted(false); // Remove button border
        // Change color on mouse hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(34, 139, 34)); // Darker green on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 100, 0)); // Original color
            }
        });
    }

    // Updates user progress in the game
    public void updateUserProgress(int completedLevel, UserAccount user) {
        if (completedLevel >= userProgress) {
            userProgress = completedLevel;
            CredentialsManager.updateLevel(user.getUsername(), completedLevel); // Update level in credentials manager
        }
    }

    //Handle creating a new account
    private void handleCreateAccount() {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        JPasswordField pwdField = new JPasswordField(10);
        int action = JOptionPane.showConfirmDialog(this, pwdField, "Enter password:", JOptionPane.OK_CANCEL_OPTION);
        if (action != JOptionPane.OK_OPTION) {
            return;
        }
        String password = new String(pwdField.getPassword());

        if (username != null && !username.trim().isEmpty() && !password.trim().isEmpty()) {
            if (!CredentialsManager.usernameExists(username)) {
                UserAccount newUser = new UserAccount(username, password, 1);
                CredentialsManager.saveCredentials(username, password, 1);
                JOptionPane.showMessageDialog(this, "Account created successfully!");
                startGame(newUser); // Start the game after creating the account
            } else {
                int option = JOptionPane.showConfirmDialog(this, "Username already exists. Retry?");
                if (option == JOptionPane.YES_OPTION) {
                    handleCreateAccount(); // Retry creating an account
                } else {
                    MainMenu mainMenu = new MainMenu();
                    mainMenu.setVisible(true);
                }
            }
        }
    }

    //handle logging in
    private void handleLogin() {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        JPasswordField pwdField = new JPasswordField(10);
        int action = JOptionPane.showConfirmDialog(this, pwdField, "Enter password:", JOptionPane.OK_CANCEL_OPTION);
        if (action != JOptionPane.OK_OPTION) {
            return;
        }
        String password = new String(pwdField.getPassword());

        if (username != null) {
            // Check credentials
            List<String[]> credentials = CredentialsManager.loadCredentials();
            for (String[] credential : credentials) {
                if (username.equals(credential[0]) && password.equals(credential[1])) {
                    int savedLevel = Integer.parseInt(credential[2]);
                    UserAccount user = new UserAccount(username, password, savedLevel);
                    JOptionPane.showMessageDialog(this, "Login successful!");

                    startGame(user); // Start the game UI after successful login
                    return; // Exit the method after successful login
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    }

    //starts the game from the user's current level
    private void startGame(UserAccount user) {
        Level level1 = new Level(15, 15);
        Level level2 = new Level(20, 20);
        Level level3 = new Level(25, 25);
        Level level4 = new Level(30, 30);
        Level level5 = new Level(35, 35);
        Level level6 = new Level(40, 40);
        Level level7 = new Level(45, 45);
        Level level8 = new Level(50, 50);
        Level level9 = new Level(55, 55);
        Level level10 = new Level(60, 60);
        Level level11 = new Level(65, 65);
        Level level12 = new Level(70, 70);
        Level level13 = new Level(75, 75);
        Level level14 = new Level(80, 80);
        Level level15 = new Level(85, 85);
        Level level16 = new Level(90, 90);
        Level level17 = new Level(95, 95);
        Level level18 = new Level(100, 100);
        Level level19 = new Level(105, 105);
        Level level20 = new Level(110, 110);

        Level[] levels = {level1, level2, level3, level4, level5, level6, level7, level8, level9, level10,
                level11, level12, level13, level14, level15, level16, level17, level18, level19, level20};

        int startingLevel = user.getLevel();
        UIManager gameUI = new UIManager(levels, startingLevel - 1, this, user);
        gameUI.setUsername(user);
        gameUI.setVisible(true);
        this.dispose();
    }

    //handles exit
    private void handleExit() {
        int confirmed = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    //Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
    }
}