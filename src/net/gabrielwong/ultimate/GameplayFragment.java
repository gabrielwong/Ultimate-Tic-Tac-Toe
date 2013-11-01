package net.gabrielwong.ultimate;
import java.util.ArrayList;

import net.gabrielwong.ultimate.game.Status;
import net.gabrielwong.ultimate.game.event.MoveEvent;
import net.gabrielwong.ultimate.game.event.MoveListener;
import net.gabrielwong.ultimate.game.event.StateChangeEvent;
import net.gabrielwong.ultimate.game.event.StateChangeListener;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameplayFragment extends Fragment implements MoveListener, StateChangeListener, OnClickListener{
	
	private BoardView view = null;
	
	private ArrayList<MoveListener> moveListeners = null;
	private int buttonMargin = 100;
	private int buttonPadding = 50;
	private int buttonWidth;
	private int buttonHeight;
	private int navBarHeight = 96;
	
	private static final int blue = 0xFF33B5E5;
	private static final int red = 0xFFFF4444;
	
	Listener mListener = null;
	
	private Button menuButton;
	private Button undoButton;
	private TextView centerView;
	
	public interface Listener{
		public void onEndGame();
		public void menuClick();
		public void undoClick();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = new BoardView(inflater.getContext());
        view.setMoveListener(this);
        
        buttonWidth = (getActivity().getWindowManager().getDefaultDisplay().getWidth() - buttonMargin*2 )/3;
        buttonHeight = buttonWidth/4;
        
        View.OnClickListener menuEvent = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.menuClick();
			}
		};
    
		View.OnClickListener undoEvent = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.undoClick();
			}
		};
        
        FrameLayout frame = new FrameLayout(getActivity());
		FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        frame.setLayoutParams(frameParams);
        frame.addView(view);
        
        LinearLayout buttonContainer = new LinearLayout(inflater.getContext());
        buttonContainer.setPadding(buttonMargin, getActivity().getWindowManager().getDefaultDisplay().getHeight() - buttonHeight - navBarHeight, buttonMargin, 0);
        
        menuButton = new Button(getActivity());
        menuButton.setText("Menu");
        menuButton.setWidth(buttonWidth);
        menuButton.setHeight(buttonHeight);
        menuButton.getBackground().setColorFilter(blue, Mode.MULTIPLY);
        
        undoButton = new Button(getActivity());
        undoButton.setText("Undo");
        undoButton.setWidth(buttonWidth);
        undoButton.setHeight(buttonHeight);
        undoButton.getBackground().setColorFilter(red, Mode.MULTIPLY);
        
        centerView = new TextView(getActivity());
        centerView.setText(" ");
        
        LinearLayout.LayoutParams menuParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams undoParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams centerParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        menuParam.width =  buttonWidth;
        centerParam.width = buttonWidth;
        undoParam.width = buttonWidth;
        
        menuButton.setOnClickListener(menuEvent);
        undoButton.setOnClickListener(undoEvent);
        
        buttonContainer.addView(menuButton, menuParam);
        buttonContainer.addView(centerView, centerParam);
        buttonContainer.addView(undoButton, undoParam);
        frame.addView(buttonContainer);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		return;
	}
}
