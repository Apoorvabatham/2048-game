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
import ttfe.UserInterface;
import ttfe.PlayerInterface;

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
	public void testConstructor_invalidBoardDimensions() {
		assertThrows("Expected IllegalArgumentException for invalid board dimensions",
				IllegalArgumentException.class, () -> TTFEFactory.createSimulator(1, 1, new Random(0)));
				assertThrows("Expected IllegalArgumentException for invalid board dimensions",
				IllegalArgumentException.class, () -> TTFEFactory.createSimulator(0, 0, new Random(0)));	
				assertThrows("Expected IllegalArgumentException for invalid board dimensions",
				IllegalArgumentException.class, () -> TTFEFactory.createSimulator(0, 4, new Random(0)));
				assertThrows("Expected IllegalArgumentException for invalid board dimensions",
				IllegalArgumentException.class, () -> TTFEFactory.createSimulator(4, 0, new Random(0)));	
				assertThrows("Expected IllegalArgumentException for invalid board dimensions",
				IllegalArgumentException.class, () -> TTFEFactory.createSimulator(-1, 4, new Random(0)));
				assertThrows("Expected IllegalArgumentException for invalid board dimensions",
				IllegalArgumentException.class, () -> TTFEFactory.createSimulator(4, -1, new Random(0)));
	}

	@Test
	public void testConstructor_zero_RandomGenerator() {
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> TTFEFactory.createSimulator(4, 4, null));
	}

	@Test
    public void testConstructor_differentRandomSeeds() {
        SimulatorInterface game1 = TTFEFactory.createSimulator(4, 4, new Random(0));
        SimulatorInterface game2 = TTFEFactory.createSimulator(4, 4, new Random(1));

        assertTrue("Expected games with different seeds to have different initial states",(game1 !=game2));
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
	public void test_points_power_2(){
		game.performMove(MoveDirection.SOUTH);
        game.performMove(MoveDirection.WEST);
		int points = game.getPoints();
		boolean ans ;
		if ( (points %2) == 0) {
			ans = true;
		}else {ans = false;}
        assertTrue("The no. of points should be power of 2", ans);
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
        while (game.isSpaceLeft()) {
            game.addPiece();
        }
        assertFalse("Expected no space to be left on board", game.isSpaceLeft());

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
	public void testGetPiece_invalidCoordinates() {
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.getPieceAt(-1, 0));
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.getPieceAt(0, -1));
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.getPieceAt(4, 0));
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.getPieceAt(0, 4));
	}

	@Test
	public void testSetPiece_invalidCoordinates() {
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.setPieceAt(-1, 0, 2));
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.setPieceAt(0, -1, 2));
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.setPieceAt(4, 0, 2));
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.setPieceAt(0, 4, 2));
	}

	@Test
	public void testSetPiece_of_invalidValue() {
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.setPieceAt(0, 0, -1));
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
	public void test_piece_power_2(){
		game.performMove(MoveDirection.SOUTH);
        game.performMove(MoveDirection.WEST);
		int points= game.getPieceAt(0,0);
		boolean ans ;
		if ( (points == 0) || (points == 2 )|| (points == 4) || (points ==8)|| (points == 16) || (points == 32) || (points == 64) || (points == 128)|| (points == 256)|| (points ==512)  || (points == 1024) || (points == 2048)) {
			ans = true;
		}else {ans = false;}
        assertTrue("The no. of piece should be power of 2", ans);
	}

    @Test
    public void testWrongIsSpaceLeft2_full_board() {
        assertTrue("Expected space to be left on board", game.isSpaceLeft());

        while (game.isSpaceLeft()) {
            game.addPiece();
        }

        assertFalse("Expected no space to be left on board", game.isSpaceLeft());
    }

	@Test
    public void testIsMovePossible_In_any_Direction() {
        assertTrue("Expected move to be possible in direction", game.isMovePossible(MoveDirection.NORTH));
        assertTrue("Expected move to be possible in direction", game.isMovePossible(MoveDirection.SOUTH));
        assertTrue("Expected move to be possible in direction", game.isMovePossible(MoveDirection.WEST));
        assertTrue("Expected move to be possible in direction", game.isMovePossible(MoveDirection.EAST));
		assertTrue(game.isMovePossible());
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
		assertTrue(game.isMovePossible());
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
		assertTrue(game.isMovePossible());
	}

	@Test
    public void testWrongMove_N_W_possible_S_E() {
		int [] [] nw ={
			{8,0,0,0},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0}
		};
		makeboard(nw);
		assertTrue(game.isMovePossible(MoveDirection.EAST));
		assertFalse(game.isMovePossible(MoveDirection.WEST));
		assertFalse(game.isMovePossible(MoveDirection.NORTH));
		assertTrue(game.isMovePossible(MoveDirection.SOUTH));
		assertTrue(game.isMovePossible());
	}

	@Test
    public void testWrongMove_N_E_possible_S_W() {
		int [] [] ne ={
			{0,0,0,2},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0}
		};
		makeboard(ne);
		assertFalse(game.isMovePossible(MoveDirection.EAST));
		assertTrue(game.isMovePossible(MoveDirection.WEST));
		assertFalse(game.isMovePossible(MoveDirection.NORTH));
		assertTrue(game.isMovePossible(MoveDirection.SOUTH));
		assertTrue(game.isMovePossible());
	}

	@Test
    public void testWrongMove_S_W_possible_N_E() {
		int [] [] sw ={
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0},
			{2,0,0,0}
		};
		makeboard(sw);
		assertTrue(game.isMovePossible(MoveDirection.EAST));
		assertFalse(game.isMovePossible(MoveDirection.WEST));
		assertTrue(game.isMovePossible(MoveDirection.NORTH));
		assertFalse(game.isMovePossible(MoveDirection.SOUTH));
		assertTrue(game.isMovePossible());
	}

	@Test
    public void testWrongMove_S_E_possible_N_W() {
		int [] [] se ={
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,2}
		};
		makeboard(se);
		assertFalse(game.isMovePossible(MoveDirection.EAST));
		assertTrue(game.isMovePossible(MoveDirection.WEST));
		assertTrue(game.isMovePossible(MoveDirection.NORTH));
		assertFalse(game.isMovePossible(MoveDirection.SOUTH));
		assertTrue(game.isMovePossible());
	}

	@Test
    public void testNoMove_possible() {
		int [] [] no ={
			{2,4,2,4},
			{4,2,4,2},
			{2,4,2,4},
			{4,2,4,2}
		};
		makeboard(no);
		assertFalse(game.isMovePossible(MoveDirection.EAST));
		assertFalse(game.isMovePossible(MoveDirection.WEST));
		assertFalse(game.isMovePossible(MoveDirection.NORTH));
		assertFalse(game.isMovePossible(MoveDirection.SOUTH));
		assertFalse(game.isMovePossible());
	}

	@Test
    public void testEveryMove_possible_2everywhere() {
		int [] [] ev ={
			{2,2,2,2},
			{2,2,2,2},
			{2,2,2,2},
			{2,2,2,2}
		};
		makeboard(ev);
		assertTrue(game.isMovePossible(MoveDirection.EAST));
		assertTrue(game.isMovePossible(MoveDirection.WEST));
		assertTrue(game.isMovePossible(MoveDirection.NORTH));
		assertTrue(game.isMovePossible(MoveDirection.SOUTH));
		assertTrue(game.isMovePossible());
	}

	@Test
    public void testEveryMove_possible() {
		int [] [] e ={
			{2,2,4,4},
			{2,4,8,8},
			{8,8,16,16},
			{8,16,32,32}
		};
		makeboard(e);
		assertTrue(game.isMovePossible(MoveDirection.EAST));
		assertTrue(game.isMovePossible(MoveDirection.WEST));
		assertTrue(game.isMovePossible(MoveDirection.NORTH));
		assertTrue(game.isMovePossible(MoveDirection.SOUTH));
		assertTrue(game.isMovePossible());
	}

	@Test
	public void testIsMovePossible_MixedValuesBoard() {
		int[][] mixed = {
			{2, 0, 2, 4},
			{0, 2, 4, 8},
			{2, 4, 0, 16},
			{4, 8, 16, 0}
		};
		makeboard(mixed);
		assertTrue(game.isMovePossible(MoveDirection.NORTH));
		assertTrue(game.isMovePossible(MoveDirection.SOUTH));
		assertTrue(game.isMovePossible(MoveDirection.WEST));
		assertTrue(game.isMovePossible(MoveDirection.EAST));
		assertTrue(game.isMovePossible());
	}


	public void testEveryMove_BoardEmpty() {
		int[][] empty = {
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}
		};
		makeboard(empty);
		assertTrue(game.isMovePossible(MoveDirection.NORTH));
		assertTrue(game.isMovePossible(MoveDirection.SOUTH));
		assertTrue(game.isMovePossible(MoveDirection.WEST));
		assertTrue(game.isMovePossible(MoveDirection.EAST));
		assertTrue(game.isMovePossible());
	}

	@Test
	public void testMovePossible_isolated_tiles() {
		int[][] isolated = {
			{0, 0, 0, 0},
			{0, 2, 0, 0},
			{0, 0, 4, 0},
			{0, 0, 0, 8}
		};
		makeboard(isolated);
		assertTrue(game.isMovePossible(MoveDirection.NORTH));
		assertTrue(game.isMovePossible(MoveDirection.SOUTH));
		assertTrue(game.isMovePossible(MoveDirection.WEST));
		assertTrue(game.isMovePossible(MoveDirection.EAST));
		assertTrue(game.isMovePossible());
	}

	@Test
	public void testIsMovePossible_OnlyOnePiece() {
		int[][] one = {
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 2, 0, 0},
			{0, 0, 0, 0}
		};
		makeboard(one);
		assertTrue(game.isMovePossible(MoveDirection.NORTH));
		assertTrue(game.isMovePossible(MoveDirection.SOUTH));
		assertTrue(game.isMovePossible(MoveDirection.WEST));
		assertTrue(game.isMovePossible(MoveDirection.EAST));
		assertTrue(game.isMovePossible());
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
        game.addPiece();
        assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.performMove(null));
    }

	@Test
	public void testRun_invalid_user_interface() {
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.run(null, null));
	}

	@Test
	public void testRUn_NULL_player (){
		UserInterface ui = TTFEFactory.createUserInterface(game);
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.run(null, ui));
	}

	@Test
	public void testRUn_NULL_ui (){
		PlayerInterface player = TTFEFactory.createPlayer(false);
		assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> game.run(player, null));
	}

	@Test
    public void testRun_moves() {
        UserInterface ui = TTFEFactory.createUserInterface(game);
        PlayerInterface player = new PlayerInterface() {
            private int count = 0;
            	@Override
            	public MoveDirection getPlayerMove(SimulatorInterface game, UserInterface ui) {
                MoveDirection[] moves = {MoveDirection.NORTH, MoveDirection.EAST, MoveDirection.SOUTH, MoveDirection.WEST};
                return moves[count++ % moves.length];
            }
        };
        game.run(player, ui);
        assertTrue("Moves should be performed", game.getNumMoves() > 0);
    }

	@Test
    public void testRun_GAMEOVER_case() {
        UserInterface ui = TTFEFactory.createUserInterface(game);
        PlayerInterface player = new PlayerInterface() {
            @Override
            public MoveDirection getPlayerMove(SimulatorInterface game, UserInterface ui) {
               return MoveDirection.SOUTH;
            }
        };

        int[][] end = {
            {2, 4, 8, 16},
            {32, 64, 128, 256},
            {512, 1024, 16, 8},
            {32, 64, 128, 0} 
        };
        makeboard(end);
        game.run(player, ui);
        assertFalse("GAME should be over as no more is possible.", game.isMovePossible());
    }

	@Test
    public void testRun_increase_in_points() {
        UserInterface ui = TTFEFactory.createUserInterface(game);
        PlayerInterface player = new PlayerInterface() {
        private int count = 0;
            @Override
            public MoveDirection getPlayerMove(SimulatorInterface game, UserInterface ui) {
                MoveDirection[] moves = {MoveDirection.NORTH, MoveDirection.EAST, MoveDirection.SOUTH, MoveDirection.WEST};
                return moves[count++ % moves.length];
            }
        };
        game.run(player, ui);
        assertTrue("Points should be more than 0.", game.getPoints() > 0);
    }

	@Test
    public void testRun_gameRunsWell() {
        UserInterface ui = TTFEFactory.createUserInterface(game);
        PlayerInterface player = new PlayerInterface() {
            private int count = 0;
            @Override
            public MoveDirection getPlayerMove(SimulatorInterface game, UserInterface ui) {
                MoveDirection[] moves = {MoveDirection.NORTH, MoveDirection.EAST, MoveDirection.SOUTH, MoveDirection.WEST};
                return moves[count++ % moves.length];
            }
        };
        game.run(player, ui);
        while (game.isMovePossible()) {
            game.run(player, ui);
        }
        assertFalse("GAME should stop, when no move possible.", game.isMovePossible());
    }

	@Test
    public void testRun_randomMoves() {
        UserInterface ui = TTFEFactory.createUserInterface(game);
        PlayerInterface player = new PlayerInterface() {
            private Random random = new Random();

            @Override
            public MoveDirection getPlayerMove(SimulatorInterface game, UserInterface ui) {
                MoveDirection[] moves = MoveDirection.values();
                return moves[random.nextInt(moves.length)];
            }
        };
        game.run(player, ui);
		assertTrue("Moves should be performed.", game.getNumMoves() > 0);
    }

    @Test
    public void testRun_isolated_board() {
        UserInterface ui = TTFEFactory.createUserInterface(game);
        PlayerInterface player = TTFEFactory.createPlayer(false);

        int[][] isolate = {
         {0,0,0,0},
         {0,2,0,0},
         {0,0,4,0},
         {0,0,0,8}
        };
        makeboard(isolate);
        game.run(player, ui);
        assertTrue("Moves should be performed.", game.getNumMoves() > 0);
    }

    @Test
    public void testRun_invalid_moves() {
        UserInterface ui = TTFEFactory.createUserInterface(game);
        PlayerInterface player = new PlayerInterface() {
            @Override
            public MoveDirection getPlayerMove(SimulatorInterface game, UserInterface ui) {
            return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> game.run(player, ui));
    }

	private void makeboard (int [] [] board){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				game.setPieceAt(j, i, board[i][j]);
	}}}
}
