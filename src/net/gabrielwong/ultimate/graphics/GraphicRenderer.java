package net.gabrielwong.ultimate.graphics;

import net.gabrielwong.ultimate.R;
import net.gabrielwong.ultimate.game.GameLogic;
import net.gabrielwong.ultimate.game.GameState;
import net.gabrielwong.ultimate.game.Status;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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




public class GraphicRenderer {

	final static int ROWS = 3;
	private Context context = null;
	
	private static Resources res;
	private static Bitmap[] statusBitmaps;
	private static Bitmap background;
	
	public final static int transparency = 125;
	public static int canvasWidth; 
	

	public GraphicRenderer(Context context)
	{
		this.context = context;
		res = context.getResources();
		loadBitmaps();
	}
	
	private void loadBitmaps(){
		statusBitmaps = new Bitmap[Status.values().length];
		for (Status s : Status.values()){
			int id = 0;
			
			switch(s){
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
			statusBitmaps[s.ordinal()] = BitmapFactory.decodeResource(context.getResources(), id);
		}
		
		background = BitmapFactory.decodeResource(context.getResources(), R.drawable.board);
	}
	
	private Bitmap getStatusBitmap(Status s){
		return statusBitmaps[s.ordinal()];
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
			
			// Resize each square depending on the screen size
			smallSquare[j] = resizeBitmap(canvas.getWidth()/ROWS/ROWS,smallSquare[j]);
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
	

	/**
	 * Receives all 9 board pieces and places them on the canvas
	 * @param piece
	 * @param canvas
	 */
	static void drawBigBoard(Drawable[] piece, Canvas canvas)
	{
		for (int i=0;i<piece.length;i++)
		{
			canvas.drawBitmap(GraphicRenderer.drawableToBitmap(piece[i]),
				GameLogic.getCol(i)*canvas.getWidth()/GraphicRenderer.ROWS,
				GameLogic.getRow(i)*canvas.getWidth()/GraphicRenderer.ROWS,
				null);
		}
	}
	
	/**
	 * Draws the small board
	 * @param smallpiece
	 * @param canvas
	 */
	static void drawSmallBoard(Drawable[][] smallpiece, Canvas canvas)
	{
		int divisor = canvas.getWidth();
		int smallDivisor = divisor*divisor;
		for (int i=0;i<smallpiece.length;i++)
		{
			for (int j=0;j<smallpiece[0].length;j++)
			{
				canvas.drawBitmap(GraphicRenderer.drawableToBitmap(smallpiece[i][j]),
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
	
	public static void drawFocusBoard(Drawable board[])
	{
		
	}
}
