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
	
	protected static final int PIECE_STATUS_START_BIT = 0,
							   MOVE_COUNT_START_BIT = 18,
							   STATUS_MASK = 0x3,
							   MOVE_COUNT_MASK = 0x15;
	
	public static final int SIDE_LENGTH = 3,
							N_SQUARES = SIDE_LENGTH * SIDE_LENGTH;
	
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
		int ordinal = (data & (STATUS_MASK << shift)) >>> shift;
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
		data &= ~(STATUS_MASK << shift); // Clear bits
		data |= ordinal << shift; // Set bits
	}
	
	/**
	 * Returns the number of non-playable squares.
	 * @return
	 */
	public int getMoveCount(){
		return (data & (MOVE_COUNT_MASK << MOVE_COUNT_START_BIT)) >>> MOVE_COUNT_START_BIT;
	}
	
	/**
	 * Sets the number of non-playable squares.
	 * @param count
	 */
	public void setMoveCount(int count){
		// Does not check if count > 15. Could be a problem if the side length size is increased.
		data &= ~(MOVE_COUNT_MASK << MOVE_COUNT_START_BIT);
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
