package net.gabrielwong.ultimate.game;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.gabrielwong.ultimate.game.event.MoveEvent;
import net.gabrielwong.ultimate.game.event.MoveListener;
import net.gabrielwong.ultimate.game.event.StateChangeEvent;
import net.gabrielwong.ultimate.game.event.StateChangeListener;
import android.util.Log;

/**
 * Performs all the logic for the game.
 * @author Gabriel
 *
 */
public class GameLogic implements MoveListener{
	private GameState state;
	private ArrayList<StateChangeListener> stateChangeListeners;
	
	private ConcurrentLinkedQueue<Move> moveQueue = null;
	private Lock moveQueueLock = null;
	
	public GameLogic(){
		state = new GameState();
		stateChangeListeners = new ArrayList<StateChangeListener>();

		moveQueue = new ConcurrentLinkedQueue<Move>();
		moveQueueLock = new ReentrantLock();
	}
	
	@Override
	public void movePerformed(MoveEvent evt){
		final Move move = evt.getMove();
		
		addMoveToQueue(move);
		processMoveQueue();
	}
	
	/**
	 * Add a move to the move queue.
	 * @param move
	 */
	private void addMoveToQueue(Move move){
		moveQueue.add(move);
	}
	
	/**
	 * Processes each move in the queue until there is one that is successful.
	 * All subsequent moves are then discarded.
	 */
	private void processMoveQueue(){
		(new Thread(){
			public void run(){
				boolean lockSuccess = moveQueueLock.tryLock();
				if (! lockSuccess)
					return;
				
				while (!moveQueue.isEmpty()){
					Move move = moveQueue.poll();
					boolean moveSuccess = processMove(move);
					
					// Empty the queue
					if (moveSuccess){
						while (!moveQueue.isEmpty()){
							moveQueue.poll();
						}
					}
				}
				
				moveQueueLock.unlock();
			}
		}).run();
	}
	
	/**
	 * Process a move in the queue.
	 * @param move
	 * @return Whether the move was successful.
	 */
	private boolean processMove(Move move){
		if (move.getPlayerId() == GameState.NO_PLAYER)
			move.setPlayerId(state.getPlayerId());
		Status status = doMove(move);
		if (status != null){
			state.setStatus(status);
			sendStateChangeEvent();
			return true;
		}
		return false;
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
		Log.d("GameLogic", "Sending stage change event");
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
		if (board.getStatus(move.getSmallIndex()) == Status.PLAYABLE)
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
		return isValidMove(move, state);
	}
	
	public static boolean isValidMove(Move move, GameState state){
		// It's not the turn of the player that attempted the move
		if (!isPlayerTurn(move.getPlayerId(), state))
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
		if (board.getMoveCount() >= Board.N_SQUARES)
			return Status.TIE;

		return Status.PLAYABLE;
	}
	
	/**
	 * Returns whether it is the player's turn.
	 * @param playerId
	 * @return
	 */
	public boolean isPlayerTurn(int playerId){
		return isPlayerTurn(playerId, state);
	}
	
	public static boolean isPlayerTurn(int playerId, GameState state){
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
	
	public void removeAllListeners(){
		stateChangeListeners = new ArrayList<StateChangeListener>();
	}
}
