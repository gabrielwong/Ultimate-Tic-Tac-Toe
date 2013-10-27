package net.gabrielwong.ultimate.game;

import java.util.ArrayList;

import net.gabrielwong.ultimate.game.event.MoveEvent;
import net.gabrielwong.ultimate.game.event.MoveListener;
import net.gabrielwong.ultimate.game.event.StateChangeEvent;
import net.gabrielwong.ultimate.game.event.StateChangeListener;

/**
 * Performs all the logic for the game.
 * @author Gabriel
 *
 */
public class GameLogic implements MoveListener{
	private GameState state;
	private ArrayList<StateChangeListener> stateChangeListeners;
	
	public GameLogic(){
		state = new GameState();
		stateChangeListeners = new ArrayList<StateChangeListener>();
	}
	
	@Override
	public void movePerformed(MoveEvent evt){
		final Move move = evt.getMove();
		
		// To avoid hanging any event dispatch threads
		Thread thread = new Thread(){
			public void run(){
				processMove(move);
				System.out.println(move.getBigIndex() + "," + move.getSmallIndex());
			}
		};
		thread.start();
	}
	
	/**
	 * Called in response to a move event.
	 * @param move
	 */
	private void processMove(Move move){
		if (move.getPlayerId() == GameState.NO_PLAYER)
			move.setPlayerId(state.getPlayerId());
		Status status = doMove(move);
		state.setStatus(status);
		sendStateChangeEvent();
	}
	
	/**
	 * Adds the state change listener. The listener will be notified whenever there is an update to the game state.
	 * @param listener
	 */
	public void addStateChangeListener(StateChangeListener listener){
		if (stateChangeListeners.indexOf(listener) == -1)
			stateChangeListeners.add(listener);
	}
	
	/**
	 * Removes the listener given.
	 * @param listener
	 */
	public void removeStateChangeListener(StateChangeListener listener){
		stateChangeListeners.remove(listener);
	}
	
	/**
	 * Sends the new GameState to all StateChangeListener
	 */
	private void sendStateChangeEvent(){
		StateChangeEvent evt = new StateChangeEvent(state);
		for (StateChangeListener listener : stateChangeListeners){
			listener.stateChanged(evt);
		}
	}
	
	/**
	 * Performs the move specified.
	 * @param move The move that was performed.
	 * @return The condition of the game after the move. Null if the move is invalid.
	 */
	private Status doMove(Move move){
		// Check validity
		if (!isValidMove(move))
			return null;
		
		BigBoard board = state.getBoard();
		Board sBoard = board.getSmallBoard(move.getBigIndex());
		
		// Place the piece
		Status piece = getPlayerPiece(move.getPlayerId());
		placePiece(sBoard, move.getSmallIndex(), piece);
		alternateTurn();
		
		Status status = checkBoard(sBoard, move.getSmallIndex(), piece);
		// A miniboard was won or full
		if (status != Status.PLAYABLE){
			
			placePiece(board, move.getBigIndex(), status);
			
			// Check for overall win or tie
			Status overallStatus = checkBoard(board, move.getBigIndex(), status);
			
			if (overallStatus != Status.PLAYABLE)
				return overallStatus;
			
		}
		
		// Set the active board
		if (board.getStatus(move.getBigIndex()) == Status.PLAYABLE)
			board.setActiveBoard(move.getSmallIndex());
		else
			board.setActiveBoard(BigBoard.NO_ACTIVE_BOARD);
		
		return Status.PLAYABLE;
	}
	
	/**
	 * Checks for the validity of a move in the current game state.
	 * @param move
	 * @return
	 */
	public boolean isValidMove(Move move){
		// It's not the turn of the player that attempted the move
		if (!isPlayerTurn(move.getPlayerId()))
			return false;
		
		BigBoard board = state.getBoard();
		int bigIndex = move.getBigIndex();
		
		// The mini-board is already won or full
		if (board.getStatus(bigIndex) != Status.PLAYABLE)
			return false;
		
		// Player played on the wrong mini-board
		if (board.getActiveBoard() != BigBoard.NO_ACTIVE_BOARD && board.getActiveBoard() != bigIndex)
			return false;
		
		// The precise square is occupied
		if (board.getSmallBoard(bigIndex).getStatus(move.getSmallIndex()) != Status.PLAYABLE)
			return false;
		
		return true;
	}
	
	/**
	 * Checks a board to determine if it is playable, won or full.
	 * @param board The board to check
	 * @param pos The index of where the last piece was played.
	 * @param status The last status set.
	 * @return
	 */
	public Status checkBoard(Board board, int pos, Status status){
		int row = getRow(pos);
		int col = getCol(pos);
		
		// Check for a win
		if (status == Status.PLAYER_ZERO || status == Status.PLAYER_ONE){
			// Check row
			for (int i = 0; i < Board.SIDE_LENGTH; i++){
				int index = getIndex(row, i);
				if (board.getStatus(index) != status) // Not a win
					break;
				if (i == Board.SIDE_LENGTH - 1)
					return status;
			}
			
			// Check column
			for (int i = 0; i < Board.SIDE_LENGTH; i++){
				int index = getIndex(i, col);
				if (board.getStatus(index) != status) // Not a win
					break;
				if (i == Board.SIDE_LENGTH - 1)
					return status;
			}
			
			// Check main diagonal
			if (row == col){
				for (int i = 0; i < Board.SIDE_LENGTH; i++){
					int index = getIndex(i, i);
					if (board.getStatus(index) != status) // Not a win
						break;
					if (i == Board.SIDE_LENGTH - 1)
						return status;
				}
			}
			
			// Check anti-diagonal
			if (row + col == (Board.SIDE_LENGTH - 1)){
				for (int i = 0; i < Board.SIDE_LENGTH; i++){
					int index = getIndex(Board.SIDE_LENGTH - 1 - i, i);
					if (board.getStatus(index) != status) // Not a win
						break;
					if (i == Board.SIDE_LENGTH - 1)
						return status;
				}			
			}
		}
		
		// Check if board is full
		if (board.getMoveCount() >= Board.SIDE_LENGTH * Board.SIDE_LENGTH)
			return Status.TIE;

		return Status.PLAYABLE;
	}
	
	/**
	 * Returns whether it is the player's turn.
	 * @param playerId
	 * @return
	 */
	public boolean isPlayerTurn(int playerId){
		return state.getPlayerId() == playerId;
	}
	
	/**
	 * Returns an index to be used in Board for the given row and column of the board.
	 * @param row
	 * @param col
	 * @return
	 */
	public static int getIndex(int row, int col){
		return row * Board.SIDE_LENGTH + col;
	}
	
	/**
	 * Returns the row that a board index points to.
	 * @param index
	 * @return
	 */
	public static int getRow(int index){
		return index / Board.SIDE_LENGTH;
	}
	
	/**
	 * Returns the column that a board index points to.
	 * @param index
	 * @return
	 */
	public static int getCol(int index){
		return index % Board.SIDE_LENGTH;
	}
	
	/**
	 * Returns the state.
	 * @return
	 */
	public GameState getGameState(){
		return state;
	}

	/**
	 * Set the game state to something else.
	 * @param state
	 */
	public void setGameState(GameState state){
		this.state = state;
	}
	
	/**
	 * Makes it the next player's turn.
	 */
	private void alternateTurn(){
		state.setPlayerId((state.getPlayerId() + 1) % 2);
	}

	/**
	 * Returns the Status that represents a piece played by the specified player.
	 * @param playerId The player who played the piece.
	 * @return A representation of the player's piece.
	 */
	private static Status getPlayerPiece(int playerId){
		return playerId == 0 ? Status.PLAYER_ZERO : Status.PLAYER_ONE;
	}

	/**
	 * Puts a piece on the board and increments the piece count.
	 * @param board The board to put the piece on.
	 * @param index The location in which to place the piece.
	 * @param piece The piece to place.
	 */
	private void placePiece(Board board, int index, Status piece){
		board.setStatus(index, piece);
		board.incrementMoveCount();
	}
}
