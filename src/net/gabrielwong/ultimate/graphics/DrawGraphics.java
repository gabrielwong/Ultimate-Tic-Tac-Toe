package net.gabrielwong.ultimate.graphics;

import net.gabrielwong.ultimate.R;
import net.gabrielwong.ultimate.game.GameLogic;
import net.gabrielwong.ultimate.game.GameState;
import net.gabrielwong.ultimate.game.Status;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/*
 * This file will only be run once to create the graphics
 * It defines all of the constants depending on screen size
 * It initializes all of the bitmap images
 * All of the functions to dynamically modify the screen is
 * in DrawGraphics.java
 */




public class DrawGraphics {

	final static int ROWS = 3;
	
	private static Resources res;
	
	public final static int transparency = 125;
	public static int canvasWidth; 
	
	/**
	 * Get the context from the Constructor of GraphicRenderer
	 * @param context
	 */
	public static void getResource(Context context)
	{
		res = context.getResources();
	}
	
	/**
	 * Initialize all of the values. Called the first time by the drawBoard View
	 * @param canvas
	 * @param bigSquare
	 * @param smallSquare
	 */
	public static Drawable[] initializeBoard(Canvas canvas, Drawable bigSquare[], Status status, GameState state)
	{	
		canvasWidth = canvas.getWidth();
		
		for (int i=0;i<9;i++)
		{
			status = state.getBoard().getStatus(i);
			bigSquare[i] = getIcon(status);
		}
		
		for (int i=0;i<9;i++)
		{
			// Resize each square depending on the screen size
			bigSquare[i] = resizeBitmap(canvas.getWidth()/ROWS, bigSquare[i]);
		}
		return bigSquare;
	}

	public static Drawable[] initializeSmallBoard(Canvas canvas, Drawable smallSquare[], int index, Status status, GameState state)
	{	
		for (int j=0;j<smallSquare.length;j++)
		{
			status = state.getBoard().getSmallBoard(index).getStatus(j);
			smallSquare[j] = getIcon(status);
		}
		
		for (int j=0;j<smallSquare.length;j++)
		{
			// Resize each square depending on the screen size
			smallSquare[j] = resizeBitmap(canvas.getWidth()/ROWS/ROWS,
					smallSquare[j]);
		}
		return smallSquare;
	}
	
	/**
	 * Checks the value of the status and returns an image based on the value
	 * @param status
	 * @return
	 */
	public static Drawable getIcon(Status status){
		int id = 0;
		
		switch(status){
		case PLAYABLE:
			id = R.drawable.playable;
			break;
		case PLAYER_ONE:
			id = R.drawable.playerone;
			break;
		case PLAYER_ZERO:
			id = R.drawable.playerzero;
			break;
		case TIE:
			id = R.drawable.tie;
			break;
		}
		return res.getDrawable(id);
	}
	
	/**
	 * Draws the big board and fill it with playable bitmaps
	 * @param canvas
	 * @param square
	 * @param playableSquare
	 */
	public static void drawBigBoard(Canvas canvas, Bitmap square[], Bitmap playableSquare)
	{
		for (int i=0;i<9;i++)
		{
			square[i] = playableSquare; 
			canvas.drawBitmap(square[i], 
					GameLogic.getCol(i)*canvas.getWidth()/ROWS,
					GameLogic.getRow(i)*canvas.getWidth()/ROWS,
					null);
		}
	}
	
	/**
	 * Resizing the bitmap using height and width
	 * @param height
	 * @param width
	 * @param image
	 */
	@SuppressWarnings("deprecation")
	public static Drawable resizeBitmap(int height, Drawable image)
	{
		Bitmap b = ((BitmapDrawable)image).getBitmap();
		Bitmap bitmapResized = Bitmap.createScaledBitmap(b, height, height, false);
		return new BitmapDrawable(bitmapResized);
	}
	
	
	/**
	 * Converts Drawable to Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	
	public static void drawFocusBoard(Drawable board[])
	{
		
	}
}
