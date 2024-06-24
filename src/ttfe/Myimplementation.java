package ttfe;
import java.util.Random;

public class Myimplementation implements SimulatorInterface{

    private int width;
    private int height;
    private int points;
    private int numMoves;
    private int [][] board;
    private Random random;

    public Myimplementation (int width, int height, Random random){
        if (width< 2|| height<2 || random == null){
            throw new IllegalArgumentException("Invalid board dimensions or random = 0");
        }
        this.width = width;
        this.height = height;
        this.points = 0;
        this.numMoves = 0;
        this.board = new int[width][height];
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
        
        board [y][x]= random.nextBoolean() ? 2 : 4; // 10% chance of 4, 90% chance of 2

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
       int num = 0;
       for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
            if (board[i][j] != 0) {
                num++;
                }
            }
        }
        return num ;
    }

    @Override
    public int getPieceAt(int x, int y) {
        if (x <0 || y< 0 || x >= getBoardWidth() || y >= getBoardWidth()){
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
       for (int i = 0; i < width; i++) {
        for (int j = 0; j< height; j++){
            if ((i < width -1 && board [i][j]== board [i+1][j]) || (j< height-1 && board[i][j]== board [i][j+1]) ){
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
                if (board[i][j] != 0 && (board[i][j - 1] == 0 || board[i][j - 1] == board[i][j])) {
                    return true;
                }
            }
        }
        break;
    
        case SOUTH:
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[i][j] != 0 && (board[i][j + 1] == 0 || board[i][j +1] == board[i][j])) {
                    return true;
                }
            }
        }
        break;

        case EAST:
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[i][j] != 0 && (board[i+1][j] == 0 || board[i+1][j] == board[i][j])) {
                    return true;
                }
            }
        }
        break;  
            
        case WEST:
        for (int i = 0; i < width; i++) {
            for (int j = 1; j < height; j++) {
                if (board[i][j] != 0 && (board[i-1][j] == 0 || board[i-1][j] == board[i][j])) {
                    return true;
                }
            }
        }
        break;    
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
    if (direction == null) throw new IllegalArgumentException("Invalid direction 'null'")  ;
    if (isMovePossible()) return false;

    boolean done = false;
    switch (direction) {
        case NORTH :
        for (int x = 0; x < width; x++) {
            for (int y = 1; y < height; y++) {
                if (board[x][y] != 0) {
                 int currentY = y;
                    while (currentY > 0 && board[x][currentY - 1] == 0) {
                        board[x][currentY - 1] = board[x][currentY];
                        board[x][currentY] = 0;
                        currentY--;
                        done = true;
                    }
                    if (currentY > 0 && board[x][currentY - 1] == board[x][currentY]) {
                        board[x][currentY - 1] *= 2;
                        points += board[x][currentY - 1];
                        board[x][currentY] = 0;
                        done = true;
                    }
                }
            }
        }
        break; 
        
        case SOUTH:
        for (int x = 0; x < width; x++) {
            for (int y = height - 2; y >= 0; y--) {
                if (board[x][y] != 0) {
                int currentY = y;
                    while (currentY < height - 1 && board[x][currentY + 1] == 0) {
                        board[x][currentY + 1] = board[x][currentY];
                        board[x][currentY] = 0;
                        currentY++;
                        done = true;
                    }
                    if (currentY < height - 1 && board[x][currentY + 1] == board[x][currentY]) {
                        board[x][currentY + 1] *= 2;
                        points += board[x][currentY + 1];
                        board[x][currentY] = 0;
                        done = true;
                    }
                }
            }
        }
        break;

        case EAST:
        for (int y = 0; y < height; y++) {
            for (int x = width - 2; x >= 0; x--) {
                if (board[x][y] != 0) {
                 int currentX = x;
                    while (currentX < width - 1 && board[currentX + 1][y] == 0) {
                        board[currentX + 1][y] = board[currentX][y];
                        board[currentX][y] = 0;
                        currentX++;
                        done = true;
                    }
                    if (currentX < width - 1 && board[currentX + 1][y] == board[currentX][y]) {
                        board[currentX + 1][y] *= 2;
                        points += board[currentX + 1][y];
                        board[currentX][y] = 0;
                        done = true;
                    }
                }
            }
        }
        break;

        case WEST:
        for (int y = 0; y < height; y++) {
            for (int x = 1; x < width; x++) {
                if (board[x][y] != 0) {
                int currentX = x;
                    while (currentX > 0 && board[currentX - 1][y] == 0) {
                        board[currentX - 1][y] = board[currentX][y];
                        board[currentX][y] = 0;
                        currentX--;
                        done = true;
                    }
                    if (currentX > 0 && board[currentX - 1][y] == board[currentX][y]) {
                        board[currentX - 1][y] *= 2;
                        points += board[currentX - 1][y];
                        board[currentX][y] = 0;
                        done = true;
                    }
                }
            }
        }
        break;
        }

        if (done){
            numMoves ++;
            addPiece();
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
            if (performMove(direction)){
                ui.updateScreen(this);
            }
        }
        ui.showGameOverScreen(this);
    }

    @Override
    public void setPieceAt(int x, int y, int piece) {
        if (x <0 || y< 0 || x >= getBoardWidth() || y >= getBoardHeight() || (piece % 2 != 0)|| piece < 0){
            throw new IllegalArgumentException("Invalid piece value or invalid board dimensions");
            }
             else {
               board [y][x]= piece;
             }     
    } 
}
