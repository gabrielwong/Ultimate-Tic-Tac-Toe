package net.gabrielwong.ultimate.graphics;

import net.gabrielwong.ultimate.R;
import net.gabrielwong.ultimate.game.GameLogic;
import net.gabrielwong.ultimate.game.GameState;
import net.gabrielwong.ultimate.game.Status;

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
public class BoardView extends View{

	private Drawable bigBoard[] = new Drawable[9];
	private static Drawable smallBoard[][] = new Drawable[9][9];
	
	// During focus
	private static Drawable tempBoard[] = new Drawable[9];
	private static Drawable backBoard;
	
	private Status status;
	private GameState state;
	private GraphicRenderer render;
	private static boolean zoomBoard = false;
	

	
	public BoardView(Context context)
	{
		super(context);
		render = new GraphicRenderer(context);
		
		backBoard = context.getResources().getDrawable(R.drawable.board);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		state = new GameState();
		state.getBoard().setStatus(0, Status.PLAYER_ONE);
		
		// Setting the values of the squares
		bigBoard = GraphicRenderer.initializeBoard(canvas, bigBoard, status, state);
		
		for (int i=0;i<smallBoard.length;i++)
			smallBoard[i] = GraphicRenderer.initializeSmallBoard(canvas, smallBoard[i],i, status, state);
		
		GraphicRenderer.drawBigBoard(bigBoard, canvas);
		GraphicRenderer.drawSmallBoard(smallBoard, canvas);
		// Check whether the board should be focused or not
		if (zoomBoard)
		{
		//	backBoard = DrawGraphics.resizeBitmap(canvas.getWidth()/2, backBoard);
			GraphicRenderer.drawFocusBoard(tempBoard); 
		}
		
		//Checks for any changes needed to be made
		invalidate();
	}
	
	// EVENTS ARE ALL BELOW
	



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