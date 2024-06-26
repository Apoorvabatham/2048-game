package ttfe;

public class AI implements PlayerInterface {
    
    @Override
    public MoveDirection getPlayerMove(SimulatorInterface game, UserInterface ui) {
 
        int width = game.getBoardWidth();
        int height = game.getBoardHeight();
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (game.getPieceAt(x, y) == 0) {
                    game.setPieceAt(x, y, 2048);
                }
            }
        }
        return MoveDirection.NORTH;
    }

}
