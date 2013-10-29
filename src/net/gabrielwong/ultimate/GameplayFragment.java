package net.gabrielwong.ultimate;
import java.util.ArrayList;

import net.gabrielwong.ultimate.MainMenuFragment.Listener;
import net.gabrielwong.ultimate.game.Status;
import net.gabrielwong.ultimate.game.event.MoveEvent;
import net.gabrielwong.ultimate.game.event.MoveListener;
import net.gabrielwong.ultimate.game.event.StateChangeEvent;
import net.gabrielwong.ultimate.game.event.StateChangeListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GameplayFragment extends Fragment implements MoveListener, StateChangeListener{
	
	private GameplayView view = null;
	
	private ArrayList<MoveListener> moveListeners = null;
	
	Listener mListener = null;
	
	public interface Listener{
		public void onEndGame();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = new GameplayView(inflater.getContext());
        view.setMoveListener(this);
        
        return view;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (Listener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Override
	public void stateChanged(StateChangeEvent event) {
		view.stateChanged(event);
		Status status = event.getState().getStatus();
		if (status != Status.PLAYABLE && status != null)
			notifyGameEnd(status);
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
	
	private void notifyGameEnd(Status status){
		if (mListener != null) mListener.onEndGame();
	}
}
