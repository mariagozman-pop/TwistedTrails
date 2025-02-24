import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// The MazeGenerator class is responsible for creating a maze layout
public class MazeGenerator {

    private Cell[][] grid; // A 2D array representing the grid of the maze

    // Constructor for the MazeGenerator
    public MazeGenerator() {}

    // Method to generate a maze with specified width and height
    public Cell[][] generateMaze(int width, int height) {
        grid = new Cell[height][width]; // Initializes the grid with the given dimensions
        initializeGrid(); // Sets all cells in the grid to be walls initially

        // Setting the start and finish points of the maze
        int startX = 0;
        int startY = 1;
        grid[startY][startX] = Cell.PASSAGE;
        int finishX = width - 1;
        int finishY = height - 2;
        grid[finishY][finishX] = Cell.FINISH;

        // Generates the maze using recursive backtracking from the start position
        generateRecursiveBacktracking(startX, startY);

        // Ensures the entrance and exit are marked properly
        markEntranceAndExit(width, height);

        return grid;
    }

    // Initializes the grid by setting all cells to be walls
    private void initializeGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = Cell.WALL;
            }
        }
    }

    // Recursive method to generate the maze using backtracking
    private void generateRecursiveBacktracking(int x, int y) {
        // Shuffles directions to ensure randomness in the maze generation
        List<Direction> directions = Direction.getShuffledDirections();

        // Iterates through each direction
        for (Direction direction : directions) {
            int newX = x + direction.getX() * 2;
            int newY = y + direction.getY() * 2;

            // Checks if the new cell position is valid for making a passage
            if (isValidCell(newX, newY)) {
                grid[newY][newX] = Cell.PASSAGE; // Marks the new cell as a passage
                grid[y + direction.getY()][x + direction.getX()] = Cell.PASSAGE;

                // Continues to generate maze from the new cell position
                generateRecursiveBacktracking(newX, newY);
            }
        }
    }

    // Checks if a cell is valid for becoming a passage in the maze
    private boolean isValidCell(int x, int y) {
        return x > 0 && x < grid[0].length - 1 && y > 0 && y < grid.length - 1 && grid[y][x] == Cell.WALL;
    }

    // Marks the entrance and exit of the maze
    private void markEntranceAndExit(int width, int height) {
        grid[1][0] = Cell.PASSAGE; // Entrance
        grid[height - 2][width - 1] = Cell.PASSAGE; // Exit
        // Additional cells near the exit to ensure it's reachable
        grid[height - 2][width - 2] = Cell.PASSAGE;
        grid[height - 2][width - 3] = Cell.PASSAGE;
    }

    // Enum representing the four possible directions in the maze
    enum Direction {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);

        private final int x; // Horizontal movement factor
        private final int y; // Vertical movement factor

        // Constructor for Direction, sets the movement factors for each direction
        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // Getter for the horizontal movement factor
        public int getX() {
            return x;
        }

        // Getter for the vertical movement factor
        public int getY() {
            return y;
        }

        // Static method to get a list of all directions in a random order
        // This is used to randomize the direction choices during maze generation
        public static List<Direction> getShuffledDirections() {
            List<Direction> directions = new ArrayList<>();
            Collections.addAll(directions, Direction.values()); // Add all enum values to the list
            Collections.shuffle(directions); // Shuffle the list to randomize the order
            return directions;
        }
    }
}