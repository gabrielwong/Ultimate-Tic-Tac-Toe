package net.gabrielwong.ultimate.graphics;

import net.gabrielwong.ultimate.game.BigBoard;
import net.gabrielwong.ultimate.game.Board;
import net.gabrielwong.ultimate.game.GameLogic;
import net.gabrielwong.ultimate.game.GameState;
import net.gabrielwong.ultimate.game.Status;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class TestView extends View {

	private GameState state;
	private BoardRenderer renderer;
	
	private int zoomBoard = -1;
	
	
	private static final int FOCUS_RATIO = 5;
	private static final int FOCUS_MARGIN = 1;
	private int focusWidth;
	
	public TestView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		state = new GameState();
		renderer = new BoardRenderer(getContext());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		renderer.drawBigBoard(canvas, state.getBoard());
		
		if (zoomBoard != -1)
			renderer.drawBoard(canvas, 
					state.getBoard().getSmallBoard(zoomBoard), 
					getWidth()*FOCUS_MARGIN/FOCUS_RATIO, 
					getWidth()*FOCUS_MARGIN/FOCUS_RATIO,
					getWidth()*(FOCUS_RATIO-2*FOCUS_MARGIN),
					getWidth()*(FOCUS_RATIO-2*FOCUS_MARGIN));
	}

	
	/**
	 * Check when the screen is touched. If touched spot is 
	 * the same as lifted spot, take index of the board
	 * 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int focusBoardMargin = getWidth() * FOCUS_MARGIN / FOCUS_RATIO;
	    int focusBoardWidth = getWidth() * (FOCUS_RATIO - 2 * FOCUS_MARGIN);
	    
		if (zoomBoard == -1)
	    {
			int startX = (int)event.getX();
		    int startY = (int)event.getY();
		    
		    int currentX=0;
		    int currentY=0;
		    
		    

		    if (event.getAction() == MotionEvent.ACTION_UP)
		    {
		    	currentX = (int)event.getX();
		    	currentY = (int)event.getY();
		    }
		    
		    // Check if clicked spot same as lifted spot
		    if (startX/getWidth() == currentX/getWidth()
		    	&& startY/getWidth()== currentY/getWidth())
		    	{
		    		int row = startX/getWidth();
		    		int col = startY/getWidth();
		    		
		    		// Focus on the board since clicked
		    		focusBoard(GameLogic.getIndex(row, col));
		    	}
	    }
	    
	    // Check if touched the focused board
	    else if (// Check left side
	    		(int) event.getX() >= focusBoardMargin
	    		&& // Check right side
	    		(int) event.getX() <= focusBoardMargin + focusBoardWidth
	    		&& // Check top 
	    		(int) event.getY() >= focusBoardMargin
	    		&& // Check bottom
	    		(int) event.getY() <= focusBoardMargin + focusBoardWidth
	    		)
	    	// Check which piece touched
	    {
	    	focusWidth = focusBoardWidth / Board.SIDE_LENGTH;
	    	int xTouch = (int)event.getX() - focusBoardMargin;
	    	int yTouch = (int)event.getY() - focusBoardMargin;
	    	
	    	int moveIndex = GameLogic.getIndex(xTouch/(focusWidth/Board.SIDE_LENGTH), yTouch/(focusWidth/Board.SIDE_LENGTH)); 
	    	
	    	// RUN MOVE TO INDICATE MOVE
	    }
	    else
	    	zoomBoard = -1;
	return false;
	}	

	/**
	 * Call this method when the board is clicked
	 * @param index
	 */
	public void focusBoard(int index)
	{
		zoomBoard = index;
		invalidate();
	}
}
