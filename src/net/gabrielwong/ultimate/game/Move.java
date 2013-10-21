package net.gabrielwong.ultimate.game;

/**
 * Represents a move by a player.
 * @author Gabriel
 *
 */
public class Move {
	private int data = 0;
	
	private static final int PLAYER_ID_BIT = 0, 	// The start bit of set/get playerId
							 BIG_INDEX_BIT = 1, 	// The start bit of set/get bigIndex
							 SMALL_INDEX_BIT = 5; 	// The start bit of set/get smallIndex
	public Move(){}
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
		int mask = 0x1 << PLAYER_ID_BIT;
		return mask & data;
	}
	
	/**
	 * Sets the player that performed the move to a specified index.
	 * @param playerId
	 */
	public void setPlayerId(int playerId){
		int mask = 0x1 << PLAYER_ID_BIT;
		if (playerId == 0){
			data &= ~mask;
		} else if (playerId == 1){
			data |= mask;
		}
	}
	
	/**
	 * Returns the index of the mini-board on which a move was played.
	 * @return
	 */
	public int getBigIndex(){
		int mask = 0x1111 << BIG_INDEX_BIT;
		return (mask & data) >>> BIG_INDEX_BIT;
	}
	
	/**
	 * Sets the index of the mini-board on which a move was played.
	 * @param bigIndex
	 */
	public void setBigIndex(int bigIndex){
		data &= ~(0x1111 << BIG_INDEX_BIT); // Clear bits
		data |= bigIndex << BIG_INDEX_BIT; // Set bits
	}
	
	/**
	 * Returns the index of where a piece was played on the mini-board.
	 * @return
	 */
	public int getSmallIndex(){
		int mask = 0x1111 << SMALL_INDEX_BIT;
		return (mask & data) >>> SMALL_INDEX_BIT;
	}
	
	/**
	 * Sets the index of where a piece was played on the mini-board.
	 * @param smallIndex
	 */
	public void setSmallIndex(int smallIndex){
		data &= ~(0x1111 << SMALL_INDEX_BIT);
		data |= smallIndex << SMALL_INDEX_BIT;
	}
}
