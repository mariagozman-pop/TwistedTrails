import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class UIManager extends JFrame {
    // Class attributes
    private final Level[] levels; // Array of levels in the game
    private int currentLevelIndex; // Index of the current level
    private final JPanel mazePanel; // Panel for rendering the maze
    private final JPanel sidePanel; // Side panel for additional options and information
    private static final int DESIRED_WINDOW_SIZE = 775; // Desired size for the window
    private final BackgroundSound b_sound; // Background sound manager
    private final WinningSounds w_sound; // Sounds for winning conditions
    private int playerX, playerY; // Player's position in the maze
    private int collectedItemsCount; // Number of items collected by the player

    // Images for player, collectibles, walls, passages, and finish line
    private Image playerImage, mirroredPlayerImage, collectibleImage, wallImage, passageImage, finishImage;
    private boolean imageLoadedP, imageLoadedC, imageLoadedW, imageLoadedB, imageLoadedF; // Flags to check if images are loaded
    private boolean isMirrored; // Flag to check if player image is mirrored
    private MainMenu mainMenu; // Reference to the main menu
    private UserAccount user; // User account details
    private final CardLayout cardLayout; // Layout for switching between different views
    private final JPanel cardPanel; // Panel that holds different views

    public UIManager(Level[] levels, int startingLevelIndex, MainMenu mainMenu, UserAccount user) {
        this.levels = levels;
        this.currentLevelIndex = 0;
        this.mainMenu = mainMenu;
        this.user = user;

        setTitle("Twisted Trails");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        b_sound = new BackgroundSound();
        w_sound = new WinningSounds();
        // Initialize containerPanel with BorderLayout
        JPanel containerPanel = new JPanel(new BorderLayout());

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/images/Screenshot 2024-01-04 210921.png")));

        Image image = icon.getImage();
        setIconImage(image);

        // Initialize mazePanel
        mazePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMaze(g);
                drawPlayer(g);
            }
        };
        mazePanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                mazePanel.requestFocusInWindow();
            }
        });

        // Initialize cardPanel with CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Add mazePanel to cardPanel
        mazePanel.setPreferredSize(new Dimension(DESIRED_WINDOW_SIZE, DESIRED_WINDOW_SIZE));
        cardPanel.add(mazePanel, "Maze");

        // Add FriendsProgressUI to cardPanel

        // Add cardPanel to containerPanel
        containerPanel.add(cardPanel, BorderLayout.CENTER);

        // Initialize sidePanel and add buttons for switching views
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(200, 50));
        sidePanel.setBackground(new Color(200, 200, 200));
        JButton switchToMazeButton = new JButton("Maze View");
        switchToMazeButton.addActionListener(e -> cardLayout.show(cardPanel, "Maze"));
        sidePanel.add(switchToMazeButton);
        JButton switchToFriendsButton = new JButton("Friends View");
        switchToFriendsButton.addActionListener(e -> cardLayout.show(cardPanel, "FriendsProgress"));
        sidePanel.add(switchToFriendsButton);

        // Add sidePanel to containerPanel
        containerPanel.add(sidePanel, BorderLayout.SOUTH);

        // Add containerPanel to JFrame
        add(containerPanel);

        // Load images, level, and set up key listener
        loadPlayerImage(); // Load the player image
        loadCurrentLevel(); // Load the initial level
        setupKeyListener(); // Set up the key listener for player movement

        // Configure window size and visibility
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Change level if starting level index is not zero
        if (startingLevelIndex != 0) {
            changeLevel(startingLevelIndex);
        } else {
            loadCurrentLevel();
        }
    }

    public void setUsername(UserAccount user)
    {
        this.user = user;
    }

    private void loadPlayerImage() {
        try {
            playerImage = ImageIO.read(getClass().getResource("resources/images/player.png"));
            mirroredPlayerImage = getMirroredImage(playerImage); // Get the mirrored image initially
            imageLoadedP = true;
        } catch (IOException e) {
            playerImage = null;
            mirroredPlayerImage = null;
            imageLoadedP = false;
        }
    }

    private void loadCoinImage() {
        try {
            collectibleImage = ImageIO.read(getClass().getResource("resources/images/collectible.png"));
            imageLoadedC = true;
        } catch (IOException e) {
            collectibleImage = null;
            imageLoadedC = false;
        }
    }

    private void loadWallImage() {
        try {
            wallImage = ImageIO.read(getClass().getResource("resources/images/bush2.png"));
            imageLoadedW = true;
        } catch (IOException e) {
            wallImage = null;
            imageLoadedW = false;
        }
    }

    private void loadPassageImage() {
        try {
            passageImage = ImageIO.read(getClass().getResource("resources/images/pavement.png"));
            imageLoadedB = true;
        } catch (IOException e) {
            passageImage = null;
            imageLoadedB = false;
        }
    }

    private void loadFinishImage() {
        try {
            finishImage = ImageIO.read(getClass().getResource("resources/images/finishline.png"));
            imageLoadedF = true;
        } catch (IOException e) {
            finishImage = null;
            imageLoadedF = false;
        }
    }

    public void changeLevel(int levelIndex) {
        if (levelIndex >= 0 && levelIndex < levels.length) {
            currentLevelIndex = levelIndex;
            loadCurrentLevel();
        } else {
            System.out.println("Invalid level index.");
        }
    }

    private Image getMirroredImage(Image img) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-img.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter((BufferedImage) img, null);
    }

    private void loadCurrentLevel() {
        Level currentLevel = levels[currentLevelIndex];
        playerX = currentLevel.getStartX();
        playerY = currentLevel.getStartY();
        collectedItemsCount = 0;

        mazePanel.setPreferredSize(new Dimension(currentLevel.getWidth() * calculateCellSize(),
                currentLevel.getHeight() * calculateCellSize()));

        loadFinishImage();

        updateSideMenu();
        mazePanel.repaint();
    }

    private void setupKeyListener() {
        mazePanel.setFocusable(true);
        mazePanel.requestFocus();
        mazePanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                int dx = 0, dy = 0;

                if (keyCode == KeyEvent.VK_UP) {
                    dy = -1;
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    dy = 1;
                } else if (keyCode == KeyEvent.VK_LEFT) {
                    dx = -1;
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    dx = 1;
                }
                movePlayer(dx, dy);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    private void movePlayer(int dx, int dy) {
        Level currentLevel = levels[currentLevelIndex];
        int newX = playerX + dx;
        int newY = playerY + dy;

        if (currentLevel.isValidMove(newX, newY)) {
            playerX = newX;
            playerY = newY;
            mazePanel.repaint();
            checkReachedFinish(user);

            // Check if the player is moving left or right to toggle mirroring
            if (dx < 0 && !isMirrored) {
                playerImage = mirroredPlayerImage; // Use the mirrored image
                isMirrored = true;
            } else if (dx > 0 && isMirrored) {
                playerImage = getMirroredImage(playerImage); // Get a new mirrored image
                isMirrored = false;
            }

            if (currentLevel.collectItemAt(playerX, playerY)) {
                collectedItemsCount++;
                updateSideMenu();
            }
        }
    }

    private void drawMaze(Graphics g) {
        Level currentLevel = levels[currentLevelIndex];
        int cellSize = calculateCellSize();

        loadCoinImage(); // Load collectible image
        loadWallImage(); // Load wall image
        loadPassageImage(); // Load passage image
        loadFinishImage(); // Load finish line image

        for (int i = 0; i < currentLevel.getHeight(); i++) {
            for (int j = 0; j < currentLevel.getWidth(); j++) {
                if (currentLevel.getCell(j, i) == Cell.COLLECTIBLE) {
                    if (collectibleImage != null && imageLoadedC) {
                        g.drawImage(collectibleImage, j * cellSize, i * cellSize, cellSize, cellSize, null);
                    } else {
                        g.setColor(Color.ORANGE);
                        g.fillOval(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                } else if (currentLevel.getCell(j, i) == Cell.WALL) {
                    if (wallImage != null && imageLoadedW) {
                        g.drawImage(wallImage, j * cellSize, i * cellSize, cellSize, cellSize, null);
                    } else {
                        // Draw default wall shape or color if image isn't loaded
                        g.setColor(new Color(5, 71, 42));
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                } else if (currentLevel.getCell(j, i) == Cell.FINISH) {
                    if (finishImage != null && imageLoadedF) {
                        g.drawImage(finishImage, j * cellSize, i * cellSize, cellSize, cellSize, null);
                    } else {
                        // Draw default finish line shape or color if image isn't loaded
                        g.setColor(Color.RED);
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                } else {
                    if (passageImage != null && imageLoadedB) {
                        g.drawImage(passageImage, j * cellSize, i * cellSize, cellSize, cellSize, null);
                    } else {
                        // Draw default passage shape or color if image isn't loaded
                        g.setColor(Color.WHITE);
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                }
            }
        }
    }

    private void drawPlayer(Graphics g) {
        int cellSize = calculateCellSize();

        if (imageLoadedP && playerImage != null) {
            g.drawImage(playerImage, playerX * cellSize, playerY * cellSize, cellSize, cellSize, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillOval(playerX * cellSize, playerY * cellSize, cellSize, cellSize);
        }
    }

    private int calculateCellSize() {
        Level currentLevel = levels[currentLevelIndex];
        int maxMazeDimension = Math.max(currentLevel.getWidth(), currentLevel.getHeight());
        return DESIRED_WINDOW_SIZE / maxMazeDimension;
    }

    private void updateSideMenu() {
        sidePanel.removeAll();
        sidePanel.setLayout(new BorderLayout());

        // Content panel for level and collectibles
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(1, 32, 20)); // Dark brown background color

        JLabel levelLabel = new JLabel("Level " + (currentLevelIndex + 1));
        levelLabel.setFont(new Font("Arial", Font.BOLD, 16));
        levelLabel.setForeground(Color.WHITE);
        contentPanel.add(levelLabel);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setPreferredSize(new Dimension(180, 5));
        contentPanel.add(separator);

        JLabel collectedLabel = new JLabel("You have collected " + collectedItemsCount + " items so far");
        collectedLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        collectedLabel.setForeground(Color.WHITE);
        contentPanel.add(collectedLabel);

        // Panel to hold the content and buttons
        JPanel contentAndButtonPanel = new JPanel(new BorderLayout());
        contentAndButtonPanel.add(contentPanel, BorderLayout.CENTER);

        // Back to Main Menu button
        JButton backToMainMenuButton = new JButton("Back to Main Menu");
        customizeButton(backToMainMenuButton);
        backToMainMenuButton.addActionListener(e -> {
            navigateToMainMenu();
            mazePanel.requestFocusInWindow(); // Set focus back to the game panel
        });

        // Friends button
        JButton friendsButton = new JButton("Friends");
        customizeButton(friendsButton);
        friendsButton.addActionListener(e -> {
            openFriendManagement();
            mazePanel.requestFocusInWindow(); // Set focus back to the game panel
        });

        // Change Track button
        JButton changeTrackButton = new JButton("Change Track");
        customizeButton(changeTrackButton);
        changeTrackButton.addActionListener(e -> {
            changeBackgroundTrack();
            mazePanel.requestFocusInWindow(); // Set focus back to the game panel
        });

        // Panel to hold the buttons and align them to the right
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(friendsButton);
        buttonPanel.add(backToMainMenuButton);
        buttonPanel.add(changeTrackButton); // Add the "Change Track" button

        contentAndButtonPanel.add(buttonPanel, BorderLayout.EAST);

        sidePanel.add(contentAndButtonPanel, BorderLayout.CENTER);
        sidePanel.revalidate();
        sidePanel.repaint();
    }

    private void customizeButton(JButton button) {
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        button.setFont(buttonFont);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(1, 32, 20));
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 100, 0)); // Dark green on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(1, 32, 20)); // Dark brown on exit
            }
        });
    }

    private void changeBackgroundTrack() {
        if (b_sound != null) {
            b_sound.changeTrack();
        }
    }

    private void openFriendManagement() {
        FriendManagementUI friendManagementUI = new FriendManagementUI(user);
        friendManagementUI.setVisible(true);
    }

    private void navigateToMainMenu(){
        b_sound.stop();
        mainMenu = new MainMenu();
        mainMenu.setVisible(true);
    }
    private void checkReachedFinish(UserAccount user) {
        Level currentLevel = levels[currentLevelIndex];
        if (currentLevel.isPlayerAtFinish(playerX, playerY) && allCollectiblesCollected(currentLevel)) {
            JOptionPane.showMessageDialog(this, "Congratulations! You reached the end of the maze!");

            if (currentLevelIndex < levels.length - 1) {
                w_sound.playLevelFinishedSound();
                currentLevelIndex++;
                mainMenu.updateUserProgress(currentLevelIndex + 1, user);
                loadCurrentLevel();
            } else {
                JOptionPane.showMessageDialog(this, "Oh, wow, look at you! You've completed all levels! Thank you for playing!");
                int choice = JOptionPane.showConfirmDialog(this,
                        "The mazes are randomly generated, this assures a unique experience each time you play.\nDo you want to restart the game?",
                        "Game Completed", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    restartGame(user);
                } else {
                    JOptionPane.showMessageDialog(this, "Aw, that's alright! Thank you again for playing! Goodbye!");
                    CredentialsManager.deleteAccount(user.getUsername());
                    this.dispose();
                }
            }
        }
    }

    private void restartGame(UserAccount user) {
        currentLevelIndex = 0;
        mainMenu.updateUserProgress(1, user);
        loadCurrentLevel();
    }

    private boolean allCollectiblesCollected(Level level) {
        return level.countRemainingItems() == 0;
    }
}