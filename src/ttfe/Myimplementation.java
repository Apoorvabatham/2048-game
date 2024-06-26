package ttfe;
import java.util.Random;

public class Myimplementation implements SimulatorInterface{

    private int width;
    private int height;
    private int points;
    private int numMoves;
    private int [][] board;
    private Random random;
    private int numPieces;

    public Myimplementation (int width, int height, Random random){
        if (width< 2|| height<2 || random == null){
            throw new IllegalArgumentException("Invalid board dimensions or random = 0");
        }
        this.width = width;
        this.height = height;
        this.points = 0;
        this.numMoves = 0;
        this.numPieces =0;
        this.board = new int[height][width];
        this.random= random;
        addPiece();
        addPiece();
    }

    @Override
    public void addPiece() {
        if (!isSpaceLeft()){
            throw new IllegalStateException("Board is full");
        }
        int x, y;

        do {
            x= random.nextInt(width);
            y= random.nextInt(height);
            } while (board[y][x] != 0);
        
        board [y][x]= random.nextDouble() <0.9 ? 2 : 4; 
            numPieces++;
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
        return numMoves;
    }

    @Override
    public int getNumPieces() {
        return numPieces ;
    }

    @Override
    public int getPieceAt(int x, int y) {
        if (x <0 || y< 0 || x >= width || y >= height){
            throw new IllegalArgumentException("Coordinates out of dimensions");
        }
         return board[y][x];
    }   

    @Override
    public int getPoints() {
       return points;
    }

    @Override
    public boolean isMovePossible() {
       if (isSpaceLeft()) return true;
       for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            if ((i < height -1 && board [i][j]== board [i+1][j]) || (j< width-1 && board[i][j]== board [i][j+1]) || (board[i][j]== 0) ){
                return true;
            }
        }
       }
       return false;
    }

    @Override
    public boolean isMovePossible(MoveDirection direction) {
    if (direction == null){
        throw new IllegalArgumentException("Invalid Direction 'null'");
    }   
    switch (direction) {
        case NORTH:
        for (int i = 0; i < width; i++) {
            for (int j = 1; j < height; j++) {
                if (board[j][i] != 0 && (board[j-1][i] == 0 || board[j-1][i] == board[j][i])) {
                    return true;
                }
            }
        }
        break;
    
        case SOUTH:
        for (int i = 0; i < width; i++) {
            for (int j = height -2; j >= 0; j--) {
                if (board[j][i] != 0 && (board[j+1][i] == 0 || board[j+1][i] == board[j][i])) {
                    return true;
                }
            }
        }
        break;

        case EAST:
        for (int i = width -2; i >= 0; i--) {
            for (int j = 0; j < height; j++) {
                if (board[j][i] != 0 && (board[j][i+1] == 0 || board[j][i+1] == board[j][i])) {
                    return true;
                }
            }
        }
        break;  
            
        case WEST:
        for (int i = 1; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[j][i] != 0 && (board[j][i-1] == 0 || board[j][i-1] == board[j][i])) {
                    return true;
                }
            }
        }
        break; 
        
        default :
                    return false;
    }
    return false;
    }

    @Override
    public boolean isSpaceLeft() {
        for (int i = 0; i< getBoardHeight(); i ++){
            for (int j = 0 ; j< getBoardWidth(); j++){
                if (board[i][j] == 0) return true;
            }
        }
        return false;
    }

    @Override
    public boolean performMove(MoveDirection direction) {
        if (direction == null) throw new IllegalArgumentException("Invalid direction 'null'");
        if (!isMovePossible()) return false;

        boolean done = false;
        int[][] newBoard = new int[height][width];

        switch (direction) {
            case NORTH:
                for (int x = 0; x < width; x++) {
                    int writeIndex = 0;
                    int lastMergedIndex = -1;
                    for (int y = 0; y < height; y++) {
                        if (board[y][x] != 0) {
                            if (writeIndex > 0 && newBoard[writeIndex-1][x] == board[y][x] && writeIndex-1 > lastMergedIndex) {
                                newBoard[writeIndex-1][x] *= 2;
                                points += newBoard[writeIndex-1][x];
                                lastMergedIndex = writeIndex - 1;
                                numPieces--;
                                done = true;
                            } else {
                                newBoard[writeIndex][x] = board[y][x];
                                if (writeIndex != y) done = true;
                                writeIndex++;
                            }
                        }
                    }
                }
                break;
            
            case SOUTH:
                for (int x = 0; x < width; x++) {
                    int writeIndex = height - 1;
                    int lastMergedIndex = height;
                    for (int y = height - 1; y >= 0; y--) {
                        if (board[y][x] != 0) {
                            if (writeIndex < height - 1 && newBoard[writeIndex+1][x] == board[y][x] && writeIndex+1 < lastMergedIndex) {
                                newBoard[writeIndex+1][x] *= 2;
                                points += newBoard[writeIndex+1][x];
                                lastMergedIndex = writeIndex + 1;
                                numPieces--;
                                done = true;
                            } else {
                                newBoard[writeIndex][x] = board[y][x];
                                if (writeIndex != y) done = true;
                                writeIndex--;
                            }
                        }
                    }
                }
                break;

            case EAST:
                for (int y = 0; y < height; y++) {
                    int writeIndex = width - 1;
                    int lastMergedIndex = width;
                    for (int x = width - 1; x >= 0; x--) {
                        if (board[y][x] != 0) {
                            if (writeIndex < width - 1 && newBoard[y][writeIndex+1] == board[y][x] && writeIndex+1 < lastMergedIndex) {
                                newBoard[y][writeIndex+1] *= 2;
                                points += newBoard[y][writeIndex+1];
                                lastMergedIndex = writeIndex + 1;
                                numPieces--;
                                done = true;
                            } else {
                                newBoard[y][writeIndex] = board[y][x];
                                if (writeIndex != x) done = true;
                                writeIndex--;
                            }
                        }
                    }
                }
                break;
                
            case WEST:
                for (int y = 0; y < height; y++) {
                    int writeIndex = 0;
                    int lastMergedIndex = -1;
                    for (int x = 0; x < width; x++) {
                        if (board[y][x] != 0) {
                            if (writeIndex > 0 && newBoard[y][writeIndex-1] == board[y][x] && writeIndex-1 > lastMergedIndex) {
                                newBoard[y][writeIndex-1] *= 2;
                                points += newBoard[y][writeIndex-1];
                                lastMergedIndex = writeIndex - 1;
                                numPieces--;
                                done = true;
                            } else {
                                newBoard[y][writeIndex] = board[y][x];
                                if (writeIndex != x) done = true;
                                writeIndex++;
                            }
                        }
                    }
                }
                break;

            default:
                return false;
        }

        if (done) {
            board = newBoard;
            numMoves++;
        }

        return done;
    }

    @Override
    public void run(PlayerInterface player, UserInterface ui) {
        if (player == null || ui == null){
            throw new IllegalArgumentException ("Invalid player/UI 'null'");
        }   
        
        ui.updateScreen(this);
        while (isMovePossible()){
            MoveDirection direction = player.getPlayerMove(this, ui);
            if (direction != null && performMove(direction)){
               addPiece();
            }
            ui.updateScreen(this);
        }
        ui.showGameOverScreen(this);
    }

    @Override
    public void setPieceAt(int x, int y, int piece) {
        if (x <0 || y< 0 || x >= getBoardWidth() || y >= getBoardHeight() || (piece != 0 && piece % 2 != 0)|| piece < 0){
            throw new IllegalArgumentException("Invalid piece value or invalid board dimensions");
            }else if (board[y][x] != 0 && piece == 0){
                numPieces--;
            }else if (board[y][x]== 0 && piece != 0 ){
                numPieces++;
            }

    board [y][x]= piece;
    }
}
