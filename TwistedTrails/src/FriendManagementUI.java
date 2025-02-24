import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FriendManagementUI extends JFrame {
    private JPanel friendsPanel;
    private JPanel suggestionsPanel;
    public UserAccount user;

    public FriendManagementUI(UserAccount user) {
        this.user = user;

        setTitle("Friend Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(800, 600));

        friendsPanel = new JPanel();
        suggestionsPanel = new JPanel();

        // Initialize and populate the friendsPanel
        initializeFriendsPanel();
        friendsPanel.setPreferredSize(new Dimension(400, 600));

        // Initialize and populate the suggestionsPanel with scrollable option
        initializeSuggestionsPanel();

        // Create a main panel to hold friendsPanel, suggestionsPanel, and progressPanel
        JPanel mainPanel = new JPanel(new GridLayout(1, 3));
        mainPanel.add(new JScrollPane(friendsPanel));
        mainPanel.add(new JScrollPane(suggestionsPanel));

        // Add the main panel to the content pane
        Container contentPane = getContentPane();
        contentPane.add(mainPanel);

        pack();
        setLocationRelativeTo(null);
    }

    private void initializeFriendsPanel() {
        friendsPanel.setLayout(new BoxLayout(friendsPanel, BoxLayout.Y_AXIS));

        JLabel friendsLabel = new JLabel("Your Friends:");
        friendsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        friendsPanel.add(friendsLabel);

        // Retrieve the user's friends from the user object
        List<String> friendsList = user.getFriends();

        if (friendsList.isEmpty()) {
            JLabel noFriendsLabel = new JLabel("You have no friends yet.");
            friendsPanel.add(noFriendsLabel);
        } else {
            // Display each friend's name, level, and remove button
            for (String friend : friendsList) {
                JPanel friendPanel = new JPanel();
                friendPanel.setLayout(new BorderLayout());

                String[] friendCredentials = CredentialsManager.getCredentials(friend);

                if (friendCredentials != null && friendCredentials.length >= 3) {
                    String friendInfo = "Your friend " + friend + " has reached level " + friendCredentials[2]+"!";
                    JLabel friendInfoLabel = new JLabel(friendInfo);
                    friendInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    friendPanel.add(friendInfoLabel, BorderLayout.WEST);
                } else {
                    JLabel friendNameLabel = new JLabel("Friend not found: " + friend);
                    friendNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    friendPanel.add(friendNameLabel, BorderLayout.WEST);
                }

                // Remove friend button
                JButton removeButton = new JButton("Remove");
                removeButton.setFont(new Font("Arial", Font.PLAIN, 12));
                removeButton.addActionListener(new RemoveFriendActionListener(friend));
                friendPanel.add(removeButton, BorderLayout.EAST);

                friendsPanel.add(friendPanel);
            }
        }
    }

    // Modify the initializeSuggestionsPanel method
    private void initializeSuggestionsPanel() {
        suggestionsPanel.setLayout(new BoxLayout(suggestionsPanel, BoxLayout.Y_AXIS));

        JLabel suggestionsLabel = new JLabel("Suggestions:");
        suggestionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        suggestionsPanel.add(suggestionsLabel);

        // Retrieve suggested friends (users not in the user's friend list)
        List<String> suggestedFriends = getSuggestedFriends(); // You need to implement this method

        if (suggestedFriends.isEmpty()) {
            JLabel noSuggestionsLabel = new JLabel("No suggested friends available.");
            suggestionsPanel.add(noSuggestionsLabel);
        } else {
            // Display each suggested friend's name and add button
            for (String suggestedFriend : suggestedFriends) {
                JPanel suggestedFriendPanel = new JPanel();
                suggestedFriendPanel.setLayout(new BorderLayout());

                String[] suggestedFriendCredentials = CredentialsManager.getCredentials(suggestedFriend);

                if (suggestedFriendCredentials != null && suggestedFriendCredentials.length >= 3) {
                    JLabel suggestedFriendNameLabel = new JLabel(suggestedFriend);
                    suggestedFriendNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    suggestedFriendPanel.add(suggestedFriendNameLabel, BorderLayout.WEST);

                    // Add friend button
                    JButton addButton = new JButton("Add");
                    addButton.setFont(new Font("Arial", Font.PLAIN, 12));
                    addButton.addActionListener(new AddFriendActionListener(suggestedFriend));
                    suggestedFriendPanel.add(addButton, BorderLayout.EAST);

                    suggestionsPanel.add(suggestedFriendPanel);
                }
            }
        }
    }

    private List<String> getSuggestedFriends() {
        List<String> suggestedFriends = new ArrayList<>();

        // Retrieve the user's username
        String userUsername = user.getUsername();

        // Retrieve all credentials from the credentials file
        List<String[]> allCredentials = CredentialsManager.loadCredentials();

        // Iterate through the credentials and add usernames to suggestedFriends
        for (String[] credential : allCredentials) {
            String username = credential[0];
            // Check if the username is not the user's own and is not already in the friend list
            if (!username.equals(userUsername) && !user.getFriends().contains(username)) {
                suggestedFriends.add(username);
            }
        }

        return suggestedFriends;
    }

    private class RemoveFriendActionListener implements ActionListener {
        private final String friendToRemove;

        public RemoveFriendActionListener(String friendToRemove) {
            this.friendToRemove = friendToRemove;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Remove the friend from the user's friend list
            user.removeFriend(friendToRemove);

            // Refresh the friends panel to remove the friend
            friendsPanel.removeAll();
            initializeFriendsPanel();
            friendsPanel.revalidate();
            friendsPanel.repaint();

            // Save the updated friend list
            user.saveFriendList();
        }
    }

    private class AddFriendActionListener implements ActionListener {
        private final String friendToAdd;

        public AddFriendActionListener(String friendToAdd) {
            this.friendToAdd = friendToAdd;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Add the suggested friend to the user's friend list
            user.addFriend(friendToAdd);

            // Refresh the friends panel to show the newly added friend
            friendsPanel.removeAll();
            initializeFriendsPanel();
            friendsPanel.revalidate();
            friendsPanel.repaint();

            // Remove the suggestion from the suggestions panel
            Component source = (Component) e.getSource();
            Container suggestionPanel = source.getParent();
            suggestionsPanel.remove(suggestionPanel);
            suggestionsPanel.revalidate();
            suggestionsPanel.repaint();

            // Save the updated friend list
            user.saveFriendList();
        }
    }
}