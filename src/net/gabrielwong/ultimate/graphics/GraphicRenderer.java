package net.gabrielwong.ultimate.graphics;

import net.gabrielwong.ultimate.game.GameLogic;
import net.gabrielwong.ultimate.game.GameState;
import net.gabrielwong.ultimate.game.Status;
import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

/**
 * Renders all of the Graphics
 * @author Andy
 *
 */
public class GraphicRenderer extends View{

	private Drawable bigBoard[] = new Drawable[9];
	private static Drawable smallBoard[][] = new Drawable[9][9];
	
	// During focus
	private static Drawable tempBoard[] = new Drawable[9];
	private static Drawable backBoard;
	
	private Status status;
	private GameState state;
	private static boolean zoomBoard = false;
	
	private static final int focusRatio = 5;
	private static final int focusMargin = 1;
	private static int focusWidth;
	
	public GraphicRenderer(Context context)
	{
		super(context);
		DrawGraphics.getResource(context);
		
		// backBoard = context.getResources().getDrawable(R.drawable.backBoard);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		// Setting the values of the squares
		bigBoard = DrawGraphics.initializeBoard(canvas, bigBoard, status, state);
		
		for (int i=0;i<smallBoard.length;i++)
			smallBoard[i] = DrawGraphics.initializeSmallBoard(canvas, smallBoard[i],i, status, state);
		
		drawBigBoard(bigBoard, canvas);
		
		// Check whether the board should be focused or not
		if (zoomBoard)
		{
		//	backBoard = DrawGraphics.resizeBitmap(canvas.getWidth()/2, backBoard);
			DrawGraphics.drawFocusBoard(tempBoard); 
		}
		
		//Checks for any changes needed to be made
		invalidate();
	}

	/**
	 * Receives all 9 board pieces and places them on the canvas
	 * @param piece
	 * @param canvas
	 */
	private void drawBigBoard(Drawable[] piece, Canvas canvas)
	{
		for (int i=0;i<piece.length;i++)
		{
			canvas.drawBitmap(DrawGraphics.drawableToBitmap(piece[i]),
				GameLogic.getCol(i)*canvas.getWidth()/DrawGraphics.ROWS,
				GameLogic.getRow(i)*canvas.getWidth()/DrawGraphics.ROWS,
				null);
		}
	}
	
	/**
	 * Draws the small board
	 * @param smallpiece
	 * @param canvas
	 */
	private void drawSmallBoard(Drawable[][] smallpiece, Canvas canvas)
	{
		int divisor = canvas.getWidth();
		int smallDivisor = divisor*divisor;
		for (int i=0;i<smallpiece.length;i++)
		{
			for (int j=0;j<smallpiece[0].length;j++)
			{
				canvas.drawBitmap(DrawGraphics.drawableToBitmap(smallpiece[i][j]),
						GameLogic.getCol(i)*canvas.getWidth()/divisor
						+
						GameLogic.getCol(j)*canvas.getWidth()/smallDivisor,
						GameLogic.getRow(i)*canvas.getWidth()/divisor
						+
						GameLogic.getRow(j)*canvas.getWidth()/smallDivisor,
						null
						);
			}
		}
	}

	
	/**
	 * Check when the screen is touched. If touched spot is 
	 * the same as lifted spot, take index of the board
	 * 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (!zoomBoard)
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
		    if (startX/DrawGraphics.canvasWidth/3 == currentX/DrawGraphics.canvasWidth/3
		    	&& startY/DrawGraphics.canvasWidth/3 == currentY/DrawGraphics.canvasWidth/3)
		    	{
		    		int row = startX/DrawGraphics.canvasWidth/DrawGraphics.ROWS;
		    		int col = startY/DrawGraphics.canvasWidth/DrawGraphics.ROWS;
		    		
		    		// Focus on the board since clicked
		    		focusBoard(GameLogic.getIndex(row, col));
		    	}
	    }
	    
	    // Check if touched the focused board
	    else if (// Check left side
	    		(int) event.getX() >= DrawGraphics.canvasWidth*focusMargin/focusRatio 
	    		&& // Check right side
	    		(int) event.getX() <= DrawGraphics.canvasWidth*(focusRatio - focusMargin) / focusRatio
	    		&& // Check top 
	    		(int) event.getY() >= DrawGraphics.canvasWidth*focusMargin/focusRatio
	    		&& // Check bottom
	    		(int) event.getY() <= DrawGraphics.canvasWidth*(focusRatio-focusMargin)/focusRatio
	    		)
	    	// Check which piece touched
	    {
	    	focusWidth = DrawGraphics.canvasWidth*(focusRatio-2*focusMargin)/focusRatio;
	    	int xTouch = (int)event.getX() - DrawGraphics.canvasWidth*focusMargin/focusRatio;
	    	int yTouch = (int)event.getY() - DrawGraphics.canvasWidth*focusMargin/focusRatio;
	    	
	    	int moveIndex = GameLogic.getIndex(xTouch/(focusWidth/DrawGraphics.ROWS), yTouch/(focusWidth/DrawGraphics.ROWS)); 
	    	// RUN MOVE TO INDICATE MOVE
	    }
	    else
	    	zoomBoard = false;
	return false;
	}	


	/**
	 * Call this method when the board is clicked
	 * @param index
	 */
	public static void focusBoard(int index)
	{
		zoomBoard = true;
		tempBoard = smallBoard[index];
	}
}