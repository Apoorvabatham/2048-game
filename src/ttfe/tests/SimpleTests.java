package ttfe.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ttfe.SimulatorInterface;
import ttfe.TTFEFactory;
import ttfe.MoveDirection;

/**
 * This class provides a very simple example of how to write tests for this project.
 * You can implement your own tests within this class or any other class within this package.
 * Tests in other packages will not be run and considered for completion of the project.
 */
public class SimpleTests {

	private SimulatorInterface game;

	@Before
	public void setUp() {
		game = TTFEFactory.createSimulator(4, 4, new Random(0));
	}

	@Test
	public void testInitialBoardHeight() {
		assertTrue("The initial game board did not have correct height",
				4 == game.getBoardHeight());
	}

	
	@Test
	public void testInitialBoardWidth() {
		assertTrue("The initial game board did not have correct height",
				4 == game.getBoardWidth());
	}

	@Test
	public void testInitialGamePoints() {
		assertEquals("The initial game did not have zero points", 0,
				game.getPoints());
	}

	@Test
	public void test_morethan_zero_Points(){
		game.performMove(MoveDirection.SOUTH);
        game.performMove(MoveDirection.WEST);
        assertTrue("The no. of points should be zero but is not.",game.getPoints() > 0);
	}

	@Test
	public void test_mergeFORtwo_two_Points(){
		game.setPieceAt(0, 0, 2);
		game.setPieceAt(0, 1, 2);
		game.performMove(MoveDirection.WEST);
        assertEquals("The no. of points should be zero but is not.",4,game.getPoints());
		assertEquals("Expected sum 4.",4, game.getPieceAt(0,0) );
	}
	
	@Test
    public void testAddPiece() {
        int initialNumPieces = game.getNumPieces();
        game.addPiece();
        assertEquals("Adding a piece did not increase the number of pieces", initialNumPieces + 1, game.getNumPieces());

        while (game.isSpaceLeft()) {
            game.addPiece();
        }
        assertThrows("Expected IllegalStateException to be thrown", IllegalStateException.class, () -> game.addPiece());
    }

	@Test
    public void testWrongIsSpaceLeft1() {
        // Fill the board completely
        while (game.isSpaceLeft()) {
            game.addPiece();
        }
        // This should return false
        assertFalse("Expected no space to be left on board", game.isSpaceLeft());

        // Attempt to add piece when no space is left
        assertThrows("Expected IllegalStateException to be thrown", IllegalStateException.class, () -> game.addPiece());
    }
	
    @Test
    public void testIsSpaceLeft() {
        assertTrue("Expected space to be left on board", game.isSpaceLeft());

        while (game.isSpaceLeft()) {
            game.addPiece();
        }
        assertFalse("Expected no space to be left on board", game.isSpaceLeft());
    }

    @Test
    public void testSetAndGetPieceAt() {
        game.setPieceAt(0, 0, 2);
        assertEquals("Expected piece value to match", 2, game.getPieceAt(0, 0));

        game.setPieceAt(1, 1, 4);
        assertEquals("Expected piece value to match", 4, game.getPieceAt(1, 1));

        game.setPieceAt(0, 0, 0);
        assertEquals("Expected piece value to be 0", 0, game.getPieceAt(0, 0));
    }

    @Test
    public void testWrongAddPiece1() {
		int sum =0;
		int sum2 =0;
		for(int i=0; i<game.getBoardHeight(); i++){
			for(int j=0; j<game.getBoardWidth(); j++){
				sum += game.getPieceAt(i,j);
			}
		}
		game.addPiece();
		for(int i=0; i<game.getBoardHeight(); i++){
			for(int j=0; j<game.getBoardWidth(); j++){
				sum2 += game.getPieceAt(i,j);
			}
		}

		assertTrue("Wrong way of adding points",(sum2 - sum == 2 || sum2 -sum == 4));
	}

    @Test
    public void testWrongIsSpaceLeft2() {
        // Ensure space is left initially
        assertTrue("Expected space to be left on board", game.isSpaceLeft());

        // Fill the board completely
        while (game.isSpaceLeft()) {
            game.addPiece();
        }

        // Space should no longer be left
        assertFalse("Expected no space to be left on board", game.isSpaceLeft());
    }

	@Test
    public void testIsMovePossibleInDirection() {
        assertTrue("Expected move to be possible in direction", game.isMovePossible(MoveDirection.NORTH));
        assertTrue("Expected move to be possible in direction", game.isMovePossible(MoveDirection.SOUTH));
        assertTrue("Expected move to be possible in direction", game.isMovePossible(MoveDirection.WEST));
        assertTrue("Expected move to be possible in direction", game.isMovePossible(MoveDirection.EAST));
    }

	@Test
    public void testWrongGetNumMoves1() {
		game.performMove(MoveDirection.SOUTH);
        game.performMove(MoveDirection.WEST);
        assertEquals(2, game.getNumMoves());   
	}

    @Test
    public void testWrongMove_E_W_possible_S_N() {
		int [] [] ns ={
			{2,4,2,4},
			{2,4,2,4},
			{8,64,8,64},
			{8,64,8,64}
		};
		makeboard(ns);
		assertTrue(game.isMovePossible(MoveDirection.SOUTH));
		assertTrue(game.isMovePossible(MoveDirection.NORTH));
		assertFalse(game.isMovePossible(MoveDirection.EAST));
		assertFalse(game.isMovePossible(MoveDirection.WEST));
	}

	@Test
    public void testWrongMove_N_S_possible_E_W() {
		int [] [] ns ={
			{8,8,2,2},
			{64,64,4,4},
			{8,8,2,2},
			{64,64,4,4}
		};
		makeboard(ns);
		assertTrue(game.isMovePossible(MoveDirection.EAST));
		assertTrue(game.isMovePossible(MoveDirection.WEST));
		assertFalse(game.isMovePossible(MoveDirection.NORTH));
		assertFalse(game.isMovePossible(MoveDirection.SOUTH));
	}

	@Test
    public void testWrongMove_N_W_possible_S_E() {
		int [] [] ns ={
			{8,0,0,0},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0}
		};
		makeboard(ns);
		assertTrue(game.isMovePossible(MoveDirection.EAST));
		assertFalse(game.isMovePossible(MoveDirection.WEST));
		assertFalse(game.isMovePossible(MoveDirection.NORTH));
		assertTrue(game.isMovePossible(MoveDirection.SOUTH));
	}

    @Test
    public void testWrongPerformMove1() {
		assertTrue("Not able to move in east",game.performMove(MoveDirection.EAST));
		assertTrue("Not able to move in north",game.performMove(MoveDirection.NORTH));
		assertTrue("Not able to move in west",game.performMove(MoveDirection.WEST));
		assertTrue("Not able to move in south",game.performMove(MoveDirection.SOUTH));
	}

    @Test
    public void testWrongPerformMove2() {
        // Add a piece to the board
        game.addPiece();

        // Attempt to perform move in a null direction
        assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.performMove(null));
    }

	private void makeboard (int [] [] board){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				game.setPieceAt(j, i, board[i][j]);
	}}}
}