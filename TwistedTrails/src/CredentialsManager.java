import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CredentialsManager {
    private static final String FILENAME = "config/credentials.txt";

    private static File getFile() {
        return new File(System.getProperty("user.dir"), FILENAME);
    }

    public static void saveCredentials(String username, String password, int level) {
        List<String[]> existingCredentials = loadCredentials();
        boolean usernameExists = existingCredentials.stream().anyMatch(cred -> cred[0].equals(username));

        if (!usernameExists) {
            existingCredentials.add(new String[]{username, password, String.valueOf(level)});
            try (PrintWriter writer = new PrintWriter(new FileWriter(getFile(), false))) {
                for (String[] credential : existingCredentials) {
                    writer.println(String.join(",", credential));
                }
            } catch (IOException e) {
                System.err.println("!Error saving credentials: " + e.getMessage());
            }
        } else {
            System.out.println("Username already exists. Choose a different username.");
        }
    }

    public static List<String[]> loadCredentials() {
        List<String[]> credentialsList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                credentialsList.add(credentials);
            }
        } catch (IOException e) {
            System.err.println("!Error loading credentials: " + e.getMessage());
        }
        return credentialsList;
    }

    public static void updateLevel(String username, int newLevel) {
        List<String[]> credentials = loadCredentials();
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILENAME))) {
            for (String[] credential : credentials) {
                if (credential[0].equals(username)) {
                    credential[2] = String.valueOf(newLevel);
                }
                writer.println(String.join(",", credential));
            }
        } catch (IOException e) {
            System.err.println("!Error updating level: " + e.getMessage());
        }
    }

    public static boolean usernameExists(String username) {
        List<String[]> existingCredentials = loadCredentials();
        for (String[] credential : existingCredentials) {
            if (credential[0].equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void deleteAccount(String username) {
        List<String[]> credentials = loadCredentials();
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILENAME))) {
            for (String[] credential : credentials) {
                if (!credential[0].equals(username)) {
                    writer.println(String.join(",", credential));
                }
            }
        } catch (IOException e) {
            System.err.println("!Error deleting account: " + e.getMessage());
        }
    }

    public static String[] getCredentials(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 3 && credentials[0].equals(username)) {
                    return credentials;
                }
            }
        } catch (IOException e) {
            System.err.println("!Error getting credentials: " + e.getMessage());
        }
        return null;
    }
}