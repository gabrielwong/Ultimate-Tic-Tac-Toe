package net.gabrielwong.ultimate.game.event;

import net.gabrielwong.ultimate.game.Move;

public class MoveEvent {
	private final Move move;
	public MoveEvent (Move move){
		this.move = move;
	}
	
	public Move getMove(){
		return move;
	}
}