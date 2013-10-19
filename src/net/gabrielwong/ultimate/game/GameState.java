package net.gabrielwong.ultimate.game;

/**
 * Holds all the information in any game of Ultimate Tic-Tac-Toe.
 * A game should be able to be replicated completely with the information in this class.
 * @author Gabriel
 *
 */
public class GameState {
	private BigBoard board;
	int currentTurnPlayerId = 0;
	
	/**
	 * Construct the state of a new game.
	 */
	public GameState(){
		board = new BigBoard();
	}
	
	
	/**
	 * Create a copy of state.
	 * @param state
	 */
	public GameState(GameState state){
		board = new BigBoard(state.getBoard());
		setPlayerId(state.getPlayerId());
	}
	
	/**
	 * Returns the id of the player whose turn it currently is.
	 * @return
	 */
	public int getPlayerId(){
		return currentTurnPlayerId;
	}
	
	/**
	 * Set the id of the player whose turn it currently is.
	 * @param id
	 */
	public void setPlayerId(int id){
		currentTurnPlayerId = id;
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
}
