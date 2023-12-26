import java.util.Scanner;
import java.util.Random;

public class Main {
    private static final int ROW_COUNT = 3;
    private static final int COL_COUNT = 3;
    private static final String CELL_EMPTY = " ";
    private static final String CELL_X = "X";
    private static final String CELL_O = "O";
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static final String GAME_STATE_X_WON = "X won";
    private static final String GAME_STATE_O_WON = "O won";
    private static final String GAME_STATE_DRAW = "Draw";
    private static final String GAME_STATE_CONTINUE = "Continue";

    public static void main(String[] args) {
        boolean continuePlaying = true;

        while (continuePlaying) {
            continuePlaying = startGameRound();
        }
    }

    private static boolean startGameRound() {
        String[][] board = createBoard();
        return playGame(board);
    }

    private static String[][] createBoard() {
        String[][] board = new String[ROW_COUNT][COL_COUNT];
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                board[row][col] = CELL_EMPTY;
            }
        }
        return board;
    }

    private static boolean playGame(String[][] board) {
        boolean playerTurn = true;
        String gameState = GAME_STATE_CONTINUE;

        while (GAME_STATE_CONTINUE.equals(gameState)) {
            printBoard(board);
            if (playerTurn) {
                makePlayerTurn(board);
            } else {
                makeBotTurn(board);
            }
            playerTurn = !playerTurn;
            gameState = checkGameState(board);
        }

        printBoard(board);
        System.out.println(gameState);

        return askToPlayAgain();
    }

    private static void makePlayerTurn(String[][] board) {
        System.out.println("Player's turn. Enter row and column (0-2):");
        int[] coordinates = inputCellCoordinates(board);
        board[coordinates[0]][coordinates[1]] = CELL_X;
    }

    private static int[] inputCellCoordinates(String[][] board) {
        while (true) {
            try {
                String input = scanner.nextLine();
                String[] parts = input.split("\\s+");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Please enter two numbers separated by space.");
                }

                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                if (row < 0 || row >= ROW_COUNT || col < 0 || col >= COL_COUNT) {
                    System.out.println("Invalid coordinates. Try again:");
                } else if (!CELL_EMPTY.equals(board[row][col])) {
                    System.out.println("Cell already taken. Try again:");
                } else {
                    return new int[]{row, col};
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numbers only:");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void makeBotTurn(String[][] board) {
        System.out.println("Bot's turn.");
        int[] coordinates = getRandomEmptyCellCoordinates(board);
        board[coordinates[0]][coordinates[1]] = CELL_O;
    }

    private static int[] getRandomEmptyCellCoordinates(String[][] board) {
        while (true) {
            int row = random.nextInt(ROW_COUNT);
            int col = random.nextInt(COL_COUNT);
            if (CELL_EMPTY.equals(board[row][col])) {
                return new int[]{row, col};
            }
        }
    }

    private static String checkGameState(String[][] board) {
        if (hasWon(CELL_X, board)) {
            return GAME_STATE_X_WON;
        } else if (hasWon(CELL_O, board)) {
            return GAME_STATE_O_WON;
        } else if (isBoardFull(board)) {
            return GAME_STATE_DRAW;
        } else {
            return GAME_STATE_CONTINUE;
        }
    }

    private static boolean hasWon(String player, String[][] board) {
        for (int i = 0; i < 3; i++) {
            if (player.equals(board[i][0]) && player.equals(board[i][1]) && player.equals(board[i][2])) return true;
            if (player.equals(board[0][i]) && player.equals(board[1][i]) && player.equals(board[2][i])) return true;
        }
        if (player.equals(board[0][0]) && player.equals(board[1][1]) && player.equals(board[2][2])) return true;
        if (player.equals(board[0][2]) && player.equals(board[1][1]) && player.equals(board[2][0])) return true;
        return false;
    }

    private static boolean isBoardFull(String[][] board) {
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                if (CELL_EMPTY.equals(board[row][col])) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void printBoard(String[][] board) {
        System.out.println("---------");
        for (int row = 0; row < ROW_COUNT; row++) {
            System.out.print("| ");
            for (int col = 0; col < COL_COUNT; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    private static boolean askToPlayAgain() {
        System.out.println("Play again? (yes/no):");
        String answer = scanner.nextLine().trim();
        return answer.equalsIgnoreCase("yes");
    }
}
