package net.gabrielwong.ultimate.game;

import net.gabrielwong.ultimate.game.event.StateChangeEvent;
import android.util.Log;

public class RandomAI extends AI {

	@Override
	protected Move getMove(StateChangeEvent event) {
		GameState state = event.getState();
		BigBoard board = state.getBoard();
		Move move;
		do{
			int bigIndex = board.hasActiveBoard() ? board.getActiveBoard() : (int)(Math.random() * Board.N_SQUARES);
			int smallIndex = (int)(Math.random() * Board.N_SQUARES);
			move = new Move(getPlayerId(), bigIndex, smallIndex);
		} while(! GameLogic.isValidMove(move, state));
		return move;
	}

}
