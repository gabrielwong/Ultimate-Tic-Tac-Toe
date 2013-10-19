package net.gabrielwong.ultimate.game.event;

import net.gabrielwong.ultimate.game.GameState;

public class StateChangeEvent {
	private final GameState state;
	public StateChangeEvent(GameState state){
		this.state = state;
	}
	public GameState getState(){
		return state;
	}
}
