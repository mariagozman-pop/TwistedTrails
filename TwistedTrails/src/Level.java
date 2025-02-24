import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {
    private Cell[][] grid;
    private final int width;
    private final int height;
    private final int startX;
    private final int startY;
    private final int finishX;
    private final int finishY;
    private final int collectible;
    private final WinningSounds w_sound;

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
        this.startX = 1;
        this.startY = 1;
        this.finishX = width - 1;
        this.finishY = height - 2;
        this.collectible = width/10;
        w_sound = new WinningSounds();
        generateMaze();
    }

    public boolean collectItemAt(int x, int y) {
        if (getCell(x, y) == Cell.COLLECTIBLE) {
            grid[y][x] = Cell.PASSAGE; // Change the cell to PASSAGE after collection
            w_sound.playItemCollectedSound();
            return true;
        }
        return false;
    }

    public int countRemainingItems() {
        int remainingItems = 0;

        // Iterate through the maze grid and count remaining collectible items
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x] == Cell.COLLECTIBLE) {
                    remainingItems++;
                }
            }
        }

        return remainingItems;
    }

    private void generateMaze() {
        MazeGenerator mazeGenerator = new MazeGenerator();
        this.grid = mazeGenerator.generateMaze(width, height);
        markEntranceAndExit();
        placeCollectibleItems(collectible);
    }

    private void markEntranceAndExit() {
        grid[startY][startX] = Cell.PASSAGE; // Entrance
        grid[finishY][finishX] = Cell.PASSAGE; // Exit
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public boolean isValidMove(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[y][x] != Cell.WALL;
        }
        return false;
    }

    public boolean isPlayerAtFinish(int playerX, int playerY) {
        return playerX == finishX && playerY == finishY;
    }

    public Cell getCell(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            Cell cell = grid[y][x];
            if (x == finishX && y == finishY) {
                return Cell.FINISH; // Identify finish cell
            }
            return cell;
        }
        return Cell.OUT_OF_BOUNDS;
    }

    public void placeCollectibleItems(int numberOfItems) {
        List<Point> availableLocations = identifyOpenPassages();

        // Shuffle the availableLocations list to randomize the placement
        Collections.shuffle(availableLocations);

        // Place collectible items based on the number requested
        for (int i = 0; i < numberOfItems && i < availableLocations.size(); i++) {
            Point location = availableLocations.get(i);
            grid[location.y][location.x] = Cell.COLLECTIBLE;
        }
    }

    private List<Point> identifyOpenPassages() {
        List<Point> openPassages = new ArrayList<>();

        // Iterate through the maze grid
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x] == Cell.PASSAGE) {
                    openPassages.add(new Point(x, y)); // Add open passages to the list
                }
            }
        }

        return openPassages;
    }
}
