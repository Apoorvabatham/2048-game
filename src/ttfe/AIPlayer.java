package ttfe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer implements PlayerInterface {
    private static final int MAX_DEPTH = 5;
    private static final int SIMULATIONS = 500;
    private Random random;

    public AIPlayer() {
        this.random = new Random();
    }

    @Override
    public MoveDirection getPlayerMove(SimulatorInterface game, UserInterface ui) {
        List<MoveDirection> possibleMoves = getPossibleMoves(game);
        if (possibleMoves.isEmpty()) {
            return null;
        }

        MoveDirection bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (MoveDirection move : possibleMoves) {
            int totalScore = 0;
            for (int i = 0; i < SIMULATIONS; i++) {
                SimulatorInterface clonedGame = cloneGame(game);
                clonedGame.performMove(move);
                clonedGame.addPiece();
                totalScore += simulateRandomPlayout(clonedGame, MAX_DEPTH);
            }
            if (totalScore > bestScore) {
                bestScore = totalScore;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int simulateRandomPlayout(SimulatorInterface game, int depth) {
        if (depth == 0 || !game.isMovePossible()) {
            return evaluateBoard(game);
        }

        List<MoveDirection> possibleMoves = getPossibleMoves(game);
        if (possibleMoves.isEmpty()) {
            return evaluateBoard(game);
        }

        MoveDirection randomMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
        game.performMove(randomMove);
        game.addPiece();

        return simulateRandomPlayout(game, depth - 1);
    }

    private int evaluateBoard(SimulatorInterface game) {
        int score = game.getPoints();
        int emptyTiles = countEmptyTiles(game);
        int maxTile = getMaxTile(game);
        int smoothness = calculateSmoothness(game);

        return score + (emptyTiles * 10) + (maxTile * 2) + smoothness;
    }

    private int countEmptyTiles(SimulatorInterface game) {
        int count = 0;
        for (int x = 0; x < game.getBoardWidth(); x++) {
            for (int y = 0; y < game.getBoardHeight(); y++) {
                if (game.getPieceAt(x, y) == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private int getMaxTile(SimulatorInterface game) {
        int max = 0;
        for (int x = 0; x < game.getBoardWidth(); x++) {
            for (int y = 0; y < game.getBoardHeight(); y++) {
                max = Math.max(max, game.getPieceAt(x, y));
            }
        }
        return max;
    }

    private int calculateSmoothness(SimulatorInterface game) {
        int smoothness = 0;
        for (int x = 0; x < game.getBoardWidth(); x++) {
            for (int y = 0; y < game.getBoardHeight(); y++) {
                if (x < game.getBoardWidth() - 1) {
                    smoothness -= Math.abs(game.getPieceAt(x, y) - game.getPieceAt(x + 1, y));
                }
                if (y < game.getBoardHeight() - 1) {
                    smoothness -= Math.abs(game.getPieceAt(x, y) - game.getPieceAt(x, y + 1));
                }
            }
        }
        return smoothness;
    }

    private List<MoveDirection> getPossibleMoves(SimulatorInterface game) {
        List<MoveDirection> possibleMoves = new ArrayList<>();
        for (MoveDirection move : MoveDirection.values()) {
            if (game.isMovePossible(move)) {
                possibleMoves.add(move);
            }
        }
        return possibleMoves;
    }

    private SimulatorInterface cloneGame(SimulatorInterface game) {
        SimulatorInterface clonedGame = TTFEFactory.createSimulator(game.getBoardWidth(), game.getBoardHeight(), new Random());
        for (int x = 0; x < game.getBoardWidth(); x++) {
            for (int y = 0; y < game.getBoardHeight(); y++) {
                clonedGame.setPieceAt(x, y, game.getPieceAt(x, y));
            }
        }
        return clonedGame;
    }
}