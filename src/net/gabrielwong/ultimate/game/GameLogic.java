package net.gabrielwong.ultimate.game;

/**
 * Performs all the logic for the game.
 * @author Gabriel
 *
 */
public class GameLogic {
	private GameState state;
	
	/**
	 * Performs the move specified.
	 * @param move
	 * @return The condition of the game after the move. Null if the move is invalid.
	 */
	public Status doMove(Move move){
		// Check validity
		if (!isValidMove(move))
			return null;
		
		BigBoard board = state.getBoard();
		Board sBoard = board.getSmallBoard(move.getBigIndex());
		
		// Place the piece
		Status piece = getPlayerPiece(move.getPlayerId());
		placePiece(sBoard, move.getSmallIndex(), piece);
		
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
		if (!(board.getStatus(bigIndex) == Status.PLAYABLE))
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
		
		if (status == Status.PLAYER_ZERO || status == Status.PLAYER_ONE){
			// Check rows
			for (int i = 0; i < Board.SIDE_LENGTH; i++){
				int index = getIndex(row, i);
				if (board.getStatus(index) != status) // Not a win
					break;
				if (i == Board.SIDE_LENGTH - 1)
					return status;
			}
			
			// Check columns
			for (int i = 0; i < Board.SIDE_LENGTH; i++){
				int index = getIndex(col, i);
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
	
	private Status getPlayerPiece(int playerId){
		return playerId == 0 ? Status.PLAYER_ZERO : Status.PLAYER_ONE;
	}
	
	public int getIndex(int row, int col){
		return row * Board.SIDE_LENGTH + col;
	}
	
	public int getRow(int index){
		return index / Board.SIDE_LENGTH;
	}
	
	public int getCol(int index){
		return index % Board.SIDE_LENGTH;
	}
	
	private void placePiece(Board board, int index, Status piece){
		board.setStatus(index, piece);
		board.incrementMoveCount();
	}
}
