package net.gabrielwong.ultimate;
import java.util.ArrayList;

import net.gabrielwong.ultimate.game.event.MoveEvent;
import net.gabrielwong.ultimate.game.event.MoveListener;
import net.gabrielwong.ultimate.game.event.StateChangeEvent;
import net.gabrielwong.ultimate.game.event.StateChangeListener;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GameplayFragment extends Fragment implements MoveListener, StateChangeListener{
	
	private GameplayView view = null;
	
	private ArrayList<MoveListener> moveListeners = null;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = new GameplayView(inflater.getContext());
        view.setMoveListener(this);
        
        return view;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Override
	public void stateChanged(StateChangeEvent event) {
		view.stateChanged(event);
	}

	private void sendMoveEvent(MoveEvent event){
		for (MoveListener listener : moveListeners)
			listener.movePerformed(event);
	}
	
	@Override
	public void movePerformed(MoveEvent event) {
		sendMoveEvent(event);
	}
	
	public void addMoveListener(MoveListener listener){
		if (moveListeners == null)
			moveListeners = new ArrayList<MoveListener>();
		if (moveListeners.indexOf(listener) == -1)
			moveListeners.add(listener);
	}
	
	public void removeMoveListener(MoveListener listener){
		if (moveListeners != null)
			moveListeners.remove(listener);
	}
}
