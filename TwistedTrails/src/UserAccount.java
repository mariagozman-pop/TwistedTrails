import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserAccount {
    private final String username;
    private final int startingLevel;
    private final List<String> friendList;

    public UserAccount(String username, String password, int startingLevel) {
        this.username = username;
        this.friendList = new ArrayList<>();
        this.startingLevel = startingLevel;
        loadFriendList();
    }

    public String getUsername() {
        return username;
    }

    public int getLevel() {
        return startingLevel;
    }

    public void addFriend(String friendUsername) {
        if (!friendList.contains(friendUsername)) {
            friendList.add(friendUsername);
            saveFriendList();
        }
    }

    public void removeFriend(String friendUsername) {
        if (friendList.remove(friendUsername)) {
            saveFriendList();
        }
    }

    public List<String> getFriends() {
        return new ArrayList<>(friendList);
    }

    public void saveFriendList() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("src/resources/friends/" + username + "_friends.txt"))) {
            for (String friend : friendList) {
                writer.println(friend);
            }
        } catch (IOException e) {
            System.err.println("!Error saving friend list: " + e.getMessage());
        }
    }

    public void loadFriendList() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/friends/" + username + "_friends.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                friendList.add(line);
            }
        } catch (IOException e) {
            // Friend list file does not exist, no need to print an error
        }
    }
}