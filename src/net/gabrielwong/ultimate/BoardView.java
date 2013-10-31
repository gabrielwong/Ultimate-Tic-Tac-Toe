package net.gabrielwong.ultimate;
import net.gabrielwong.ultimate.game.BigBoard;
import net.gabrielwong.ultimate.game.Board;
import net.gabrielwong.ultimate.game.GameLogic;
import net.gabrielwong.ultimate.game.GameState;
import net.gabrielwong.ultimate.game.Move;
import net.gabrielwong.ultimate.game.event.MoveEvent;
import net.gabrielwong.ultimate.game.event.MoveListener;
import net.gabrielwong.ultimate.game.event.StateChangeEvent;
import net.gabrielwong.ultimate.game.event.StateChangeListener;
import net.gabrielwong.ultimate.graphics.BoardRenderer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;


public class GameplayView extends View implements StateChangeListener{

	private GameState state = null;
	private BoardRenderer renderer;
	
	private int zoomBoard = -1;
	private int backgroundColor = 0xFFFFFFFF;
	private int activeColor = 0xFFDDDDDD;
	
	// Size of margin of zoomed board as a ratio of the view's width
	private static final double ZOOMED_MARGIN_RATIO = 0.1;
	
	private Paint backgroundPaint = null;
	private Paint activePaint = null;
	
	private MoveListener moveListener = null;
	
	public GameplayView(Context context) {
		super(context);
		
		state = new GameState();
		renderer = new BoardRenderer(getContext());
		initPaints();
	}
	
	private void initPaints(){
		backgroundPaint = new Paint();
		backgroundPaint.setColor(backgroundColor);
		
		activePaint = new Paint();
		activePaint.setColor(activeColor);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		if (state.getBoard().hasActiveBoard()){
			int row = GameLogic.getRow(state.getBoard().getActiveBoard());
			int col = GameLogic.getCol(state.getBoard().getActiveBoard());
			Rect activeBounds = new Rect(col * getWidth() / Board.SIDE_LENGTH,
										 row * getWidth() / Board.SIDE_LENGTH,
										 (col + 1) * getWidth() / Board.SIDE_LENGTH,
										 (row + 1) * getWidth() / Board.SIDE_LENGTH);
			canvas.drawRect(activeBounds, activePaint);
		}
		
		renderer.drawBigBoard(canvas, state.getBoard());
		
		if (zoomBoard != -1){
			Rect bounds = getZoomedBounds();
			canvas.drawRect(bounds, backgroundPaint);
			renderer.drawBoard(canvas, 
					state.getBoard().getSmallBoard(zoomBoard), 
					bounds.left, 
					bounds.top,
					bounds.width(),
					bounds.height());
		}
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
    		int col = Board.SIDE_LENGTH * x / getWidth();
    		
    		if (row >= Board.SIDE_LENGTH){
    			return true;
    		}
    		
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

	@Override
	public void stateChanged(StateChangeEvent event) {
		state = event.getState();
		postInvalidate();
	}
	
	public void setMoveListener(MoveListener listener){
		moveListener = listener;
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
		moveListener.movePerformed(event);
	}
}
