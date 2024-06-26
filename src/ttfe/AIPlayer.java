package ttfe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer implements PlayerInterface {
    private static final int MAX_DEPTH = 3;
    private final int[] dx = {0, 0, -1, 1};
    private final int[] dy = {-1, 1, 0, 0};
    private final Random random = new Random();

    @Override
    public MoveDirection getPlayerMove(SimulatorInterface game, UserInterface ui) {
        MoveDirection bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (MoveDirection move : MoveDirection.values()) {
            if (game.isMovePossible(move)) {
                SimulatorInterface clonedGame = cloneGame(game);
                clonedGame.performMove(move);
                int score = evaluateMove(clonedGame, MAX_DEPTH);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }

        return bestMove;
    }

    private int evaluateMove(SimulatorInterface game, int depth) {
        if (depth == 0 || !game.isMovePossible()) {
            return evaluateBoard(game);
        }

        int maxScore = Integer.MIN_VALUE;
        for (MoveDirection move : MoveDirection.values()) {
            if (game.isMovePossible(move)) {
                SimulatorInterface clonedGame = cloneGame(game);
                clonedGame.performMove(move);
                clonedGame.addPiece();
                int score = evaluateMove(clonedGame, depth - 1);
                maxScore = Math.max(maxScore, score);
            }
        }
        return maxScore;
    }

    private int evaluateBoard(SimulatorInterface game) {
        int score = game.getPoints();
        int emptyTiles = 0;
        int maxTile = 0;
        int smoothness = 0;

        for (int x = 0; x < game.getBoardWidth(); x++) {
            for (int y = 0; y < game.getBoardHeight(); y++) {
                int value = game.getPieceAt(x, y);
                if (value == 0) {
                    emptyTiles++;
                } else {
                    maxTile = Math.max(maxTile, value);
                    for (int i = 0; i < 4; i++) {
                        int nx = x + dx[i];
                        int ny = y + dy[i];
                        if (nx >= 0 && nx < game.getBoardWidth() && ny >= 0 && ny < game.getBoardHeight()) {
                            int neighborValue = game.getPieceAt(nx, ny);
                            if (neighborValue != 0) {
                                smoothness -= Math.abs(value - neighborValue);
                            }
                        }
                    }
                }
            }
        }

        return score + (emptyTiles * 10) + (maxTile * 2) + smoothness;
    }

    private SimulatorInterface cloneGame(SimulatorInterface game) {
        SimulatorInterface clonedGame = new FastSimulator(game.getBoardWidth(), game.getBoardHeight());
        for (int x = 0; x < game.getBoardWidth(); x++) {
            for (int y = 0; y < game.getBoardHeight(); y++) {
                clonedGame.setPieceAt(x, y, game.getPieceAt(x, y));
            }
        }
        return clonedGame;
    }

    private class FastSimulator implements SimulatorInterface {
        private final int width;
        private final int height;
        private final int[][] board;
        private int points = 0;

        public FastSimulator(int width, int height) {
            this.width = width;
            this.height = height;
            this.board = new int[height][width];
        }

        @Override
        public void addPiece() {
            List<int[]> emptyCells = new ArrayList<>();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (board[y][x] == 0) {
                        emptyCells.add(new int[]{x, y});
                    }
                }
            }
            if (!emptyCells.isEmpty()) {
                int[] cell = emptyCells.get(random.nextInt(emptyCells.size()));
                board[cell[1]][cell[0]] = random.nextDouble() < 0.9 ? 2 : 4;
            }
        }

        @Override
        public int getBoardHeight() {
            return height;
        }

        @Override
        public int getBoardWidth() {
            return width;
        }

        @Override
        public int getNumMoves() {
            return 0; 
        }

        @Override
        public int getNumPieces() {
            return 0; 
        }

        @Override
        public int getPieceAt(int x, int y) {
            return board[y][x];
        }

        @Override
        public int getPoints() {
            return points;
        }

        @Override
        public boolean isMovePossible() {
            for (MoveDirection dir : MoveDirection.values()) {
                if (isMovePossible(dir)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean isMovePossible(MoveDirection direction) {
            int[] d = getDirectionVector(direction);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (board[y][x] != 0) {
                        int nx = x + d[0], ny = y + d[1];
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            if (board[ny][nx] == 0 || board[ny][nx] == board[y][x]) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public boolean isSpaceLeft() {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (board[y][x] == 0) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean performMove(MoveDirection direction) {
            boolean moved = false;
            int[] d = getDirectionVector(direction);
            int[][] newBoard = new int[height][width];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int cx = direction == MoveDirection.EAST ? width - 1 - x : x;
                    int cy = direction == MoveDirection.SOUTH ? height - 1 - y : y;
                    if (board[cy][cx] != 0) {
                        int nx = cx, ny = cy;
                        while (true) {
                            int tx = nx + d[0], ty = ny + d[1];
                            if (tx < 0 || tx >= width || ty < 0 || ty >= height) break;
                            if (newBoard[ty][tx] == 0) {
                                nx = tx;
                                ny = ty;
                            } else if (newBoard[ty][tx] == board[cy][cx]) {
                                nx = tx;
                                ny = ty;
                                break;
                            } else {
                                break;
                            }
                        }
                        if (nx != cx || ny != cy) moved = true;
                        if (newBoard[ny][nx] == 0) {
                            newBoard[ny][nx] = board[cy][cx];
                        } else {
                            newBoard[ny][nx] *= 2;
                            points += newBoard[ny][nx];
                        }
                    }
                }
            }

            if (moved) {
                for (int y = 0; y < height; y++) {
                    System.arraycopy(newBoard[y], 0, board[y], 0, width);
                }
            }
            return moved;
        }

        @Override
        public void run(PlayerInterface player, UserInterface ui) {
            // Not needed for evaluation
        }

        @Override
        public void setPieceAt(int x, int y, int piece) {
            board[y][x] = piece;
        }

        private int[] getDirectionVector(MoveDirection direction) {
            switch (direction) {
                case NORTH: return new int[]{0, -1};
                case SOUTH: return new int[]{0, 1};
                case WEST: return new int[]{-1, 0};
                case EAST: return new int[]{1, 0};
                default: throw new IllegalArgumentException("Invalid direction");
            }
        }
    }
}