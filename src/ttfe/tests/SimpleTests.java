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
import ttfe.PlayerInterface;
import ttfe.UserInterface;

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
	public void testInitialGamePoints() {
		assertEquals("The initial game did not have zero points", 0,
				game.getPoints());
	}

	@Test
	public void testWrongPoints1(){
		game.performMove(MoveDirection.SOUTH);
        game.performMove(MoveDirection.WEST);
        assertTrue("The no. of points should be zero but is not.",game.getPoints() > 0);
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
    public void testPerformMove() {
        assertTrue("Move was not possible when expected", game.performMove(MoveDirection.NORTH));
        assertTrue("Move was not possible when expected", game.performMove(MoveDirection.SOUTH));
        assertTrue("Move was not possible when expected", game.performMove(MoveDirection.WEST));
        assertTrue("Move was not possible when expected", game.performMove(MoveDirection.EAST));

        while (game.isSpaceLeft()) {
            game.addPiece();
        }
        assertFalse("Expected no move to be possible", game.performMove(MoveDirection.NORTH));
    }

    @Test
    public void testIsMovePossible() {
        assertTrue("Expected move to be possible", game.isMovePossible());

        while (game.isSpaceLeft()) {
            game.addPiece();
        }
        assertFalse("Expected no move to be possible", game.isMovePossible());
    }

    @Test
    public void testIsMovePossibleInDirection() {
        assertTrue("Expected move to be possible in direction", game.isMovePossible(MoveDirection.NORTH));
        assertTrue("Expected move to be possible in direction", game.isMovePossible(MoveDirection.SOUTH));
        assertTrue("Expected move to be possible in direction", game.isMovePossible(MoveDirection.WEST));
        assertTrue("Expected move to be possible in direction", game.isMovePossible(MoveDirection.EAST));
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
        while (game.isSpaceLeft()) {
            game.addPiece();
        }
        assertThrows("Expected IllegalStateException to be thrown", IllegalStateException.class, () -> game.addPiece());
    }

    @Test
    public void testWrongGetNumMoves1() {
		game.performMove(MoveDirection.SOUTH);
        game.performMove(MoveDirection.WEST);
        assertEquals(2, game.getNumMoves());   
	}

    @Test
    public void testWrongGetNumPieces1() {
        // Attempt to get number of pieces without adding any piece
        assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.getNumPieces());
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
    public void testWrongMovePossible1() {
    assertTrue(game.isMovePossible());
	game.performMove(MoveDirection.SOUTH);
    game.performMove(MoveDirection.WEST);
	for(int i=0; i< game.getBoardHeight(); i++){
		for (int j=0; j< game.getBoardWidth(); j++){
			if((i+j)%2== 0){
				game.setPieceAt(i, j, 4);
			}else {
				game.setPieceAt(i, j, 2);
			}
		}
	}  
	assertFalse("Expected no move to be possible", game.isMovePossible());
	}

    @Test
    public void testWrongPerformMove1() {
		assertThrows("Expected IllegalArgumentException",IllegalArgumentException.class, () ->game.performMove(MoveDirection.NORTH));;
    }

    @Test
    public void testWrongPerformMove2() {
        // Add a piece to the board
        game.addPiece();

        // Attempt to perform move in a null direction
        assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.performMove(null));
    }

	@Test
    public void testPlayerMove() {
        // Mock PlayerInterface for testing
        PlayerInterface mockPlayer = new PlayerInterface() {
            @Override
            public MoveDirection getPlayerMove(SimulatorInterface game, UserInterface ui) {
                return MoveDirection.NORTH; // Mocking player to always move NORTH
            }
        };

        // Mock UserInterface for testing (minimal implementation)
        UserInterface mockUI = new UserInterface() {
            @Override
            public String getUserInput(String question, String[] possibleAnswers) {
                return null; // Not needed for this test
            }

            @Override
            public MoveDirection getUserMove() {
                return null; // Not needed for this test
            }

            @Override
            public void showGameOverScreen(SimulatorInterface game) {
                // Not needed for this test
            }

            @Override
            public void showMessage(String msg) {
                // Not needed for this test
            }

            @Override
            public void updateScreen(SimulatorInterface game) {
                // Not needed for this test
            }
        };

        // Perform move using mock player and verify
        MoveDirection moveDirection = mockPlayer.getPlayerMove(game, mockUI);
        assertTrue("Expected move to be possible in direction", game.isMovePossible(moveDirection));
        assertTrue("Expected move to be performed", game.performMove(moveDirection));
    }

    @Test
    public void testUpdateScreen() {
        // Mock UserInterface for testing updateScreen method
        UserInterface mockUI = new UserInterface() {
            @Override
            public String getUserInput(String question, String[] possibleAnswers) {
                return null; // Not needed for this test
            }

            @Override
            public MoveDirection getUserMove() {
                return null; // Not needed for this test
            }

            @Override
            public void showGameOverScreen(SimulatorInterface game) {
                // Not needed for this test
            }

            @Override
            public void showMessage(String msg) {
                // Not needed for this test
            }

            @Override
            public void updateScreen(SimulatorInterface game) {
                // Verify updateScreen method updates the screen
                game.addPiece(); // Add a piece to trigger update
                assertEquals("Expected board height to match", 4, game.getBoardHeight());
                assertEquals("Expected board width to match", 4, game.getBoardWidth());
            }
        };

        // Call updateScreen with mock UI and verify
        mockUI.updateScreen(game);
    }

    @Test
    public void testGameInitialization() {
        // Verify initial board dimensions
        assertEquals("Initial board width should be 4", 4, game.getBoardWidth());
        assertEquals("Initial board height should be 4", 4, game.getBoardHeight());

        // Verify initial points and moves
        assertEquals("Initial points should be 0", 0, game.getPoints());
        assertEquals("Initial number of moves should be 0", 0, game.getNumMoves());
    }

    @Test
    public void testGameOverScreen() {
        // Mock UserInterface for testing showGameOverScreen method
        UserInterface mockUI = new UserInterface() {
            @Override
            public String getUserInput(String question, String[] possibleAnswers) {
                return null; // Not needed for this test
            }

            @Override
            public MoveDirection getUserMove() {
                return null; // Not needed for this test
            }

            @Override
            public void showGameOverScreen(SimulatorInterface game) {
                // Verify showGameOverScreen method
                assertEquals("Expected points to be 0", 0, game.getPoints());
                assertEquals("Expected number of moves to be 0", 0, game.getNumMoves());
            }

            @Override
            public void showMessage(String msg) {
                // Not needed for this test
            }

            @Override
            public void updateScreen(SimulatorInterface game) {
                // Not needed for this test
            }
        };

        // Call showGameOverScreen with mock UI and verify
        mockUI.showGameOverScreen(game);
    }

}