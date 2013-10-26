package net.gabrielwong.ultimate.game;

/**
 * Represents a move by a player.
 * @author Gabriel
 *
 */
public class Move {
	private int playerId, bigIndex, smallIndex;
	public Move(){
		this(0,0,0);
	}
	public Move(int playerId, int bigIndex, int smallIndex){
		setPlayerId(playerId);
		setBigIndex(bigIndex);
		setSmallIndex(smallIndex);
	}
	
	/**
	 * Returns the player that performed the move.
	 * @return
	 */
	public int getPlayerId(){
		return playerId;
	}
	
	/**
	 * Sets the player that performed the move to a specified index.
	 * @param playerId
	 */
	public void setPlayerId(int playerId){
		this.playerId = playerId;
	}
	
	/**
	 * Returns the index of the mini-board on which a move was played.
	 * @return
	 */
	public int getBigIndex(){
		return bigIndex;
	}
	
	/**
	 * Sets the index of the mini-board on which a move was played.
	 * @param bigIndex
	 */
	public void setBigIndex(int bigIndex){
		this.bigIndex = bigIndex;
	}
	
	/**
	 * Returns the index of where a piece was played on the mini-board.
	 * @return
	 */
	public int getSmallIndex(){
		return smallIndex;
	}
	
	/**
	 * Sets the index of where a piece was played on the mini-board.
	 * @param smallIndex
	 */
	public void setSmallIndex(int smallIndex){
		this.smallIndex = smallIndex;
	}
}
