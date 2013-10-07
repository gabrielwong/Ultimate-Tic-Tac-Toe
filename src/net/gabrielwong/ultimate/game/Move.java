package net.gabrielwong.ultimate.game;

/**
 * Represents a move by a player.
 * @author Gabriel
 *
 */
public class Move {
	private int data = 0;
	
	private static final int PLAYER_ID_BIT = 0,
							 BIG_INDEX_BIT = 1,
							 SMALL_INDEX_BIT = 5;
	public Move(){}
	public Move(int playerId, int bigIndex, int smallIndex){
		setPlayerId(playerId);
		setBigIndex(bigIndex);
		setSmallIndex(smallIndex);
	}
	
	public int getPlayerId(){
		int mask = 1 << PLAYER_ID_BIT;
		return mask & data;
	}
	
	public void setPlayerId(int playerId){
		int mask = 1 << PLAYER_ID_BIT;
		if (playerId == 0){
			data &= ~mask;
		} else if (playerId == 1){
			data |= mask;
		}
	}
	
	public int getBigIndex(){
		int mask = 1111 << BIG_INDEX_BIT;
		return (mask & data) >> BIG_INDEX_BIT;
	}
	
	public void setBigIndex(int bigIndex){
		int mask = 1111 << BIG_INDEX_BIT;
		data &= ~mask; // Clear bits
		data |= bigIndex << BIG_INDEX_BIT; // Set bits
	}
	
	public int getSmallIndex(){
		int mask = 1111 << SMALL_INDEX_BIT;
		return (mask & data) >> SMALL_INDEX_BIT;
	}
	
	public void setSmallIndex(int smallIndex){
		int mask = 1111 << SMALL_INDEX_BIT;
		data &= ~mask;
		data |= smallIndex << SMALL_INDEX_BIT;
	}
}
