package net.gabrielwong.ultimate;
import java.util.ArrayList;

import net.gabrielwong.ultimate.game.GameState;
import net.gabrielwong.ultimate.game.Status;
import net.gabrielwong.ultimate.game.event.MoveEvent;
import net.gabrielwong.ultimate.game.event.MoveListener;
import net.gabrielwong.ultimate.game.event.StateChangeEvent;
import net.gabrielwong.ultimate.game.event.StateChangeListener;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

// Code really needs to be cleaned....

public class GameplayFragment extends Fragment implements MoveListener, StateChangeListener,
	OnClickListener{
	
	private BoardView boardView = null;
	
	private ArrayList<MoveListener> moveListeners = null;

	Listener mListener = null;

	private static View backBoard;
	private TransitionDrawable backgroundTransition;
	private FrameLayout frame;
	private View buttonView;
	
	public interface Listener{
		public void onEndGame();
		public void menuClick();
		public void undoClick();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		backBoard = getActivity().findViewById(R.id.fragment_container);
		backBoard.setBackgroundResource(R.drawable.background_transition);
		backgroundTransition = (TransitionDrawable) backBoard.getBackground();
		
		buttonView = inflater.inflate(R.layout.view_game_button, container, false);
		
        boardView = new BoardView(inflater.getContext());
        boardView.setMoveListener(this);
        
		frame = new FrameLayout(getActivity());
		FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
        frame.setLayoutParams(frameParams);
        frame.addView(boardView);
        frame.addView(buttonView);
        
        buttonView.findViewById(R.id.menu_button).setOnClickListener(this);
        buttonView.findViewById(R.id.undo_button).setOnClickListener(this);
        
        return frame;
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
		boardView.stateChanged(event);
		Status status = event.getState().getStatus();
		updateBackgroundColor(event.getState());
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

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.menu_button:
			mListener.menuClick();
			return;
		case R.id.undo_button:
			mListener.undoClick();
			return;
		}
		return;
	}
	
	public void updateBackgroundColor(GameState state)
	{
		int playerId = state.getPlayerId();
		switch(playerId)
		{
		case 0:
			backgroundTransition.startTransition(500);
		case 1:
			backgroundTransition.reverseTransition(500);
		}
	}
}
