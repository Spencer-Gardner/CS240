package ui;

public class RenderBoard {
    private static final int BOARD_SIZE = 8;

    public static void drawChessBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    System.out.print("\u001b[48;5;222m");
                } else {
                    System.out.print("\u001b[48;5;95m");
                }
                System.out.print(EscapeSequences.EMPTY);
            }
            System.out.println("\u001B[0m");
        }
    }
}
