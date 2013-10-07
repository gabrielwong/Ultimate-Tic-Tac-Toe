package net.gabrielwong.ultimate.game;

/**
 * Represents a board of Tic-Tac-Toe.
 * @author Gabriel
 *
 */
public class Board implements Cloneable{
	/**
	 * Represents the pieces on the board.
	 * The index-th bit indicates whether the square at index is occupied.
	 * The (9 + index)-th bit indicates the ID of the player who owns the piece.
	 */
	private int board = 0;
	
	/**
	 * Returned when asking for a non-existent player.
	 */
	public static final int NO_PLAYER = -1;
	protected static final int N_SQUARES = 9;
	
	/**
	 * Creates an empty Board.
	 */
	public Board(){}
	
	/**
	 * Creates a copy of b.
	 * @param b the Board to duplicate
	 */
	public Board(Board b){
		board = b.board;
	}
	
	/**
	 * Return whether there is a piece at index.
	 * @param index
	 * @return whether there is a piece at index
	 */
	public boolean isOccupied(int index){
		int mask = 1 << index;
		return (mask & board) != 0;
	}
	
	public void setOccupied(int index, boolean isOccupied){
		int mask = 1 << index;
		board |= mask;
		if (!isOccupied){
			board ^= mask; // true XOR a is equivalent to NOT a
		}
	}
	
	/**
	 * Returns the player ID of the owner of the piece at index or @link NO_PLAYER if the square index is not occupied.
	 * @param index
	 * @return the player ID of the piece at index
	 */
	public int getPiecePlayerId(int index){
		if (!isOccupied(index)){
			return NO_PLAYER;
		}
		int mask = 1 << (N_SQUARES + index);
		return ( (mask & board) == 0) ? 0 : 1;
	}
	
	/**
	 * Place a piece belonging to player id at index.
	 * @param index
	 * @param id
	 */
	public void setPiecePlayerId(int index, int id){
		if (id == NO_PLAYER){
			setOccupied(index, false);
		} else{
			setOccupied(index, true);
			int mask = 1 << (N_SQUARES + index);
			board |= mask;
			if (id == 0){
				board ^= mask; // true XOR a is equivalent to NOT a
			}
		}
		
	}
	
	@Override
	public Object clone(){
		return new Board(this);
	}
}
