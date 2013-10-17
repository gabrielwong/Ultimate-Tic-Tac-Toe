package net.gabrielwong.ultimate.game;

/**
 * Represents a board of Tic-Tac-Toe. Minimize space for network transfer.
 * @author Gabriel
 *
 */
public class Board implements Cloneable{
	/**
	 * Holds Status and move count in one int to save memory.
	 */
	private int data = 0;
	
	/**
	 * Returned when asking for a non-existent player.
	 */
	public static final int NO_PLAYER = -1;
	protected static final int PIECE_STATUS_START_BIT = 0,
							   MOVE_COUNT_START_BIT = 18;
	
	public static final int SIDE_LENGTH = 3;
	
	/**
	 * Creates an empty Board.
	 */
	public Board(){}
	
	/**
	 * Creates a copy of b.
	 * @param b the Board to duplicate
	 */
	public Board(Board b){
		data = b.data;
	}
	
	/**
	 * Returns the PieceStatus at the specified index;
	 * @param index
	 * @return
	 */
	public Status getStatus(int index){
		int shift = 2 * index + PIECE_STATUS_START_BIT;
		int ordinal = (data & (0x11 << shift)) >>> shift;
		return Status.values()[ordinal];
	}
	
	/**
	 * Sets the PieceStatus at the specified index;
	 * @param index
	 * @PieceStatus status
	 * @return
	 */
	public void setStatus(int index, Status status){
		int ordinal = status.ordinal();
		int shift = 2 * index + PIECE_STATUS_START_BIT;
		data &= ~(0x11 << shift); // Clear bits
		data |= ordinal << shift; // Set bits
	}
	
	/**
	 * Returns the number of non-playable squares.
	 * @return
	 */
	public int getMoveCount(){
		return (data & (0x1111 << MOVE_COUNT_START_BIT)) >>> MOVE_COUNT_START_BIT;
	}
	
	/**
	 * Sets the number of non-playable squares.
	 * @param count
	 */
	public void setMoveCount(int count){
		// Does not check if count > 15. Could be a problem if the side length size is increased.
		data &= ~(0x1111 << MOVE_COUNT_START_BIT);
		data |= count << MOVE_COUNT_START_BIT;
	}
	
	/**
	 * Increment the number of non-playable squares.
	 */
	public void incrementMoveCount(){
		setMoveCount(getMoveCount() + 1);
	}
	
	protected boolean getBit(int bitIndex){
		return (data & (1 << bitIndex)) != 0;
	}
	
	protected void setBit(int bitIndex, boolean value){
		if (value)
			data |= 1 << bitIndex;
		else
			data &= ~(1 << bitIndex);
	}
	
	@Override
	public Object clone(){
		return new Board(this);
	}
}
