package net.gabrielwong.ultimate.graphics;

import java.util.ArrayList;

import net.gabrielwong.ultimate.game.BigBoard;
import net.gabrielwong.ultimate.game.Board;
import net.gabrielwong.ultimate.game.GameLogic;
import net.gabrielwong.ultimate.game.GameState;
import net.gabrielwong.ultimate.game.Move;
import net.gabrielwong.ultimate.game.event.MoveEvent;
import net.gabrielwong.ultimate.game.event.MoveListener;
import net.gabrielwong.ultimate.game.event.StateChangeEvent;
import net.gabrielwong.ultimate.game.event.StateChangeListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View implements StateChangeListener {

	private GameState state;
	private BoardRenderer renderer;
	
	private int zoomBoard = -1;
	
	private ArrayList<MoveListener> moveListeners = new ArrayList<MoveListener>();
	
	// Size of margin of zoomed board as a ratio of the view's width
	private static final double ZOOMED_MARGIN_RATIO = 0.1;
	
	private Paint paint = new Paint();
	
	public BoardView(Context context) {
		super(context);
		state = new GameState();
		renderer = new BoardRenderer(getContext());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		renderer.drawBigBoard(canvas, state.getBoard());
		
		if (zoomBoard != -1){
			Rect bounds = getZoomedBounds();
			renderer.drawBoard(canvas, 
					state.getBoard().getSmallBoard(zoomBoard), 
					bounds.left, 
					bounds.top,
					bounds.width(),
					bounds.height());
		}
		String text = "Player: " + state.getPlayerId() + ", Board: " + state.getBoard().getActiveBoard();
		canvas.drawText(text, 0, getWidth(), paint);
	}

	
	/**
	 * Check when the screen is touched. If touched spot is 
	 * the same as lifted spot, take index of the board
	 * 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getActionMasked() != MotionEvent.ACTION_DOWN)
				return false;
		Rect zoomedBounds = getZoomedBounds();
		
		int zoomMargin = zoomedBounds.left;
	    int zoomWidth = zoomedBounds.width();
	    
	    int x = (int)event.getX();
	    int y = (int)event.getY();
	    
	    // No zoomed board
		if (zoomBoard == -1)
	    {
    		int row = Board.SIDE_LENGTH * y / getWidth();
    		int col = Board.SIDE_LENGTH * x /getWidth();
    		
    		// Focus on the board since clicked
    		zoomBoard = GameLogic.getIndex(row, col);
	    }
	    else{ // Consider a zoomed board
	    	boolean withinZoomedBoard = x >= zoomMargin && x <= zoomMargin + zoomWidth &&
	    			y >= zoomMargin && y <= zoomMargin + zoomWidth;
	    	if (withinZoomedBoard){
	    		int row = Board.SIDE_LENGTH * (y - zoomMargin) / zoomWidth;
	    		int col = Board.SIDE_LENGTH * (x - zoomMargin) / zoomWidth;
		    	
		    	int moveIndex = GameLogic.getIndex(row, col); 
		    	
		    	// RUN MOVE TO INDICATE MOVE
		    	sendMoveEvent(zoomBoard, moveIndex);
		    	zoomBoard = BigBoard.NO_ACTIVE_BOARD;
	    	}
	    	else{
		    	zoomBoard = BigBoard.NO_ACTIVE_BOARD;
	    	}
	    }
		invalidate();
		return true;
	}
	
	private Rect getZoomedBounds(){
		// X and Y are the same to make a square. Could change later.
		int left = (int)(getWidth() * ZOOMED_MARGIN_RATIO);
		int right = getWidth() - (int)(getWidth() * ZOOMED_MARGIN_RATIO);
		return new Rect(left, left, right, right);
	}
	
	private void sendMoveEvent(int bigIndex, int smallIndex){
		Move move = new Move(GameState.NO_PLAYER, bigIndex, smallIndex); // assume correct player for now
		MoveEvent event = new MoveEvent(move);
		for (MoveListener listener : moveListeners){
			listener.movePerformed(event);
		}
	}

	@Override
	public void stateChanged(StateChangeEvent event) {
		state = event.getState();
		invalidate();
	}
	
	public void addMoveListener(MoveListener listener){
		if (moveListeners.indexOf(listener) == -1)
			moveListeners.add(listener);
	}
	
	public void removeMoveListener(MoveListener listener){
		moveListeners.remove(listener);
	}
}
