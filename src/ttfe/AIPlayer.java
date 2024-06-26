package ttfe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer implements PlayerInterface {
    private static final int SIMULATIONS = 1000;
    private static final double EXPLORATION_PARAMETER = Math.sqrt(2);
    private Random random;

    public AIPlayer() {
        this.random = new Random();
    }

    @Override
    public MoveDirection getPlayerMove(SimulatorInterface game, UserInterface ui) {
        Node rootNode = new Node(null, null);
        for (int i = 0; i < SIMULATIONS; i++) {
            Node node = selectNode(rootNode);
            int score = simulateRandomPlayout(node.game);
            backpropagate(node, score);
        }
        return getBestMove(rootNode);
    }

    private Node selectNode(Node node) {
        while (!node.isLeaf()) {
            if (node.isFullyExpanded()) {
                node = UCT.findBestNodeWithUCT(node);
            } else {
                return expand(node);
            }
        }
        return node;
    }

    private Node expand(Node node) {
        List<MoveDirection> untriedMoves = node.getUntriedMoves();
        MoveDirection move = untriedMoves.get(random.nextInt(untriedMoves.size()));
        SimulatorInterface nextState = cloneAndMove(node.game, move);
        Node childNode = new Node(move, nextState);
        childNode.parent = node;
        node.children.add(childNode);
        return childNode;
    }

    private int simulateRandomPlayout(SimulatorInterface game) {
        SimulatorInterface tempGame = cloneGame(game);
        while (tempGame.isMovePossible()) {
            List<MoveDirection> possibleMoves = getPossibleMoves(tempGame);
            MoveDirection randomMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
            tempGame.performMove(randomMove);
            tempGame.addPiece();
        }
        return tempGame.getPoints();
    }

    private void backpropagate(Node node, int score) {
        while (node != null) {
            node.visits++;
            node.totalScore += score;
            node = node.parent;
        }
    }

    private MoveDirection getBestMove(Node rootNode) {
        Node bestChild = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (Node child : rootNode.children) {
            double childScore = child.totalScore / child.visits;
            if (childScore > bestScore) {
                bestScore = childScore;
                bestChild = child;
            }
        }
        return bestChild != null ? bestChild.move : MoveDirection.values()[random.nextInt(4)];
    }

    private SimulatorInterface cloneAndMove(SimulatorInterface game, MoveDirection move) {
        SimulatorInterface clonedGame = cloneGame(game);
        clonedGame.performMove(move);
        clonedGame.addPiece();
        return clonedGame;
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

    private List<MoveDirection> getPossibleMoves(SimulatorInterface game) {
        List<MoveDirection> possibleMoves = new ArrayList<>();
        for (MoveDirection move : MoveDirection.values()) {
            if (game.isMovePossible(move)) {
                possibleMoves.add(move);
            }
        }
        return possibleMoves;
    }

    private static class Node {
        MoveDirection move;
        SimulatorInterface game;
        Node parent;
        List<Node> children;
        int visits;
        double totalScore;

        Node(MoveDirection move, SimulatorInterface game) {
            this.move = move;
            this.game = game;
            this.children = new ArrayList<>();
            this.visits = 0;
            this.totalScore = 0;
        }

        boolean isLeaf() {
            return children.isEmpty();
        }

        boolean isFullyExpanded() {
            return getUntriedMoves().isEmpty();
        }

        List<MoveDirection> getUntriedMoves() {
            List<MoveDirection> untriedMoves = new ArrayList<>();
            for (MoveDirection move : MoveDirection.values()) {
                if (game.isMovePossible(move) && !hasChild(move)) {
                    untriedMoves.add(move);
                }
            }
            return untriedMoves;
        }

        boolean hasChild(MoveDirection move) {
            for (Node child : children) {
                if (child.move == move) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class UCT {
        static Node findBestNodeWithUCT(Node node) {
            double bestScore = Double.NEGATIVE_INFINITY;
            Node bestChild = null;
            for (Node child : node.children) {
                double uctScore = calculateUCTScore(node, child);
                if (uctScore > bestScore) {
                    bestScore = uctScore;
                    bestChild = child;
                }
            }
            return bestChild;
        }

        private static double calculateUCTScore(Node parent, Node child) {
            return (child.totalScore / child.visits)
                    + EXPLORATION_PARAMETER * Math.sqrt(Math.log(parent.visits) / child.visits);
        }
    }
}