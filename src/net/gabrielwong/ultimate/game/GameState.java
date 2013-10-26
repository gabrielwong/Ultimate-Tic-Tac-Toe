package net.gabrielwong.ultimate.game;

/**
 * Holds all the information in any game of Ultimate Tic-Tac-Toe.
 * A game should be able to be replicated completely with the information in this class.
 * @author Gabriel
 *
 */
public class GameState {
	private BigBoard board;
	private boolean currentTurnPlayerId;
	private Status status;
	
	/**
	 * Represents a non-existant player.
	 */
	public static final int NO_PLAYER = -1;
	
	/**
	 * Construct the state of a new game.
	 */
	public GameState(){
		board = new BigBoard();
		currentTurnPlayerId = false;
		status = Status.PLAYABLE;
	}
	
	
	/**
	 * Create a copy of state.
	 * @param state
	 */
	public GameState(GameState state){
		board = new BigBoard(state.getBoard());
		setPlayerId(state.getPlayerId());
		setStatus(state.getStatus());
	}
	
	/**
	 * Returns the id of the player whose turn it currently is.
	 * @return
	 */
	public int getPlayerId(){
		return currentTurnPlayerId ? 1 : 0;
	}
	
	/**
	 * Set the id of the player whose turn it currently is.
	 * @param id
	 */
	public void setPlayerId(int id){
		currentTurnPlayerId = id != 0;
	}
	
	/**
	 * Returns the BigBoard of the game.
	 * @return
	 */
	public BigBoard getBoard(){
		return board;
	}
	
	/**
	 * Set the BigBoard
	 * @param bigBoard
	 */
	public void setBoard(BigBoard bigBoard){
		this.board = bigBoard;
	}
	
	/**
	 * Returns the game's status (win, tie, incomplete).
	 * @return
	 */
	public Status getStatus(){
		return status;
	}
	
	/**
	 * Sets the game's status.
	 * @param status
	 */
	public void setStatus(Status status){
		this.status = status;
	}
}
