package net.gabrielwong.ultimate.game;

/**
 * A representation of an Ultimate Tic-Tac-Toe board. It is a standard board that also contains 9 smaller boards.
 * @author Gabriel
 *
 */
public class BigBoard extends Board {
	/**
	 * Indicates that there is current active board.
	 */
	public static final int NO_ACTIVE_BOARD = -1;
	
	/**
	 * An array containing the Boards that make up the BigBoard.
	 */
	private Board[] smallBoards;
	
	/**
	 * The index of the small board that must be played on.
	 */
	private int activeBoard = NO_ACTIVE_BOARD;
	
	
	/**
	 * Creates an empty BigBoard.
	 */
	public BigBoard(){
		smallBoards = new Board[SIDE_LENGTH * SIDE_LENGTH];
		for (int i = 0; i < smallBoards.length; i++){
			smallBoards[i] = new Board();
		}
	}
	
	
	/**
	 * Creates a deep copy of b
	 * @param b the BigBoard to be cloned
	 */
	public BigBoard(BigBoard b){
		smallBoards = new Board[SIDE_LENGTH * SIDE_LENGTH];
		for (int i = 0; i < smallBoards.length; i++){
			smallBoards[i] = new Board(b.getSmallBoard(i));
		}
		this.activeBoard = b.activeBoard;
	}
	
	/**
	 * Returns the small board at index
	 * @param index
	 * @return
	 */
	public Board getSmallBoard(int index){
		return smallBoards[index];
	}
	
	
	/**
	 * Set the small board at index
	 * @param index
	 * @param smallBoard
	 */
	public void setSmallBoard(int index, Board smallBoard){
		smallBoards[index] = smallBoard;
	}
	
	/**
	 * Returns the current active small board. If none is currently active (all are valid) it returns @link NO_ACTIVE_BOARD.
	 * @return
	 */
	public int getActiveBoard(){
		return activeBoard;
	}
	
	/**
	 * Sets the currently active small board. Setting the active small board to @link NO_ACTIVE_BOARD will cause hasActiveBoard() to return false.
	 * @param activeBoard
	 */
	public void setActiveBoard(int activeBoard){
		this.activeBoard = activeBoard;
	}
	
	/**
	 * Returns whether there is an active board.
	 * @return
	 */
	public boolean hasActiveBoard(){
		return activeBoard != NO_ACTIVE_BOARD;
	}
}
