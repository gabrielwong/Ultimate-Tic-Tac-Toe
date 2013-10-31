package net.gabrielwong.ultimate.game;

import net.gabrielwong.ultimate.game.event.MoveEvent;
import net.gabrielwong.ultimate.game.event.MoveListener;
import net.gabrielwong.ultimate.game.event.StateChangeEvent;
import net.gabrielwong.ultimate.game.event.StateChangeListener;
import android.util.Log;

public abstract class AI implements StateChangeListener {
	MoveListener mListener = null;
	private int playerId;
	
	public void setMoveListener(MoveListener l){
		mListener = l;
	}
	
	protected void sendMove(Move move){
		MoveEvent event = new MoveEvent(move);
		mListener.movePerformed(event);
	}
	
	@Override
	public void stateChanged(StateChangeEvent event){
		if (event.getState().getStatus() == Status.PLAYABLE && 
				event.getState().getPlayerId() == getPlayerId()){
			sendMove(getMove(event));
			Log.d("AI", "Send move" + event.getState().getStatus());
		}
	}
	
	public void setPlayerId(int playerId){
		this.playerId = playerId;
	}
	
	public int getPlayerId(){
		return playerId;
	}
	
	protected abstract Move getMove(StateChangeEvent event);
}
