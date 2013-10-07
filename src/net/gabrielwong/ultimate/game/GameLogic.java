package net.gabrielwong.ultimate.game;

/**
 * Performs all the logic for the game.
 * @author Gabriel
 *
 */
public class GameLogic {
	private GameState state;
	public boolean isValidMove(Move move){
		// It's not the turn of the player that attempted the move
		if (state.getPlayerId() != move.getPlayerId())
			return false;
		
		BigBoard board = state.getBoard();
		int bigIndex = move.getBigIndex();
		
		// The mini-board is already won
		if (board.isOccupied(bigIndex))
			return false;
		
		// Player played on the wrong mini-board
		if (board.getActiveBoard() != BigBoard.NO_ACTIVE_BOARD && board.getActiveBoard() != bigIndex)
			return false;
		
		// The square is occupied
		if (board.getSmallBoard(bigIndex).isOccupied(move.getSmallIndex()))
			return false;
		
		return true;
	}
}
