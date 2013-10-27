package net.gabrielwong.ultimate.graphics;

import net.gabrielwong.ultimate.R;
import net.gabrielwong.ultimate.game.BigBoard;
import net.gabrielwong.ultimate.game.Board;
import net.gabrielwong.ultimate.game.GameLogic;
import net.gabrielwong.ultimate.game.Status;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class BoardRenderer {
	
	private Context context = null;
	private static Bitmap[] statusBitmaps;
	private static Bitmap background;
	
	public BoardRenderer(Context context){
		this.context = context;
		loadBitmaps();
	}
	
	private void loadBitmaps(){
		statusBitmaps = new Bitmap[Status.values().length];
		for (Status s : Status.values()){
			int id = 0;
			
			switch(s){
			case PLAYABLE:
				id = R.drawable.playable;
				System.out.println("PLAYABLE");
				break;
			case PLAYER_ONE:
				id = R.drawable.playerone;
				System.out.println("PLAYER ONE");
				break;
			case PLAYER_ZERO:
				id = R.drawable.playerzero;
				System.out.println("PLAYER ZERO");
				break;
			case TIE:
				id = R.drawable.tie;
				System.out.println("TIE");
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
	 * Draws a board onto a canvas.
	 * @param canvas The canvas on which to draw.
	 * @param board The board that is drawn.
	 * @param left The x value of the top left corner of the area of the canvas on which to draw.
	 * @param top The y value of the top left corner of the area of the canvas on which to draw.
	 * @param width The width of the area of the canvas on which to draw.
	 * @param height The height of the area of the canvas on which to draw.
	 */
	public void drawBoard(Canvas canvas, Board board, int left, int top, int width, int height){
		// Draw the background
		Rect backgroundBounds = new Rect(left, top, left + width, top + height);
		
		
		// Draw each square
		for (int i = 0; i < Board.N_SQUARES; i++){
			Bitmap b = getStatusBitmap(board.getStatus(i));
			canvas.drawBitmap(b, null, getSquareBounds(left, top, width, height, i), null);
		}
		canvas.drawBitmap(background, null, backgroundBounds, null);
	}
	
	public void drawBoard(Canvas canvas, Board board){
		drawBoard(canvas, board, 0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	public void drawBigBoard(Canvas canvas, BigBoard bigboard, int left, int top, int width, int height){
		// Draw the background
		Rect backgroundBounds = new Rect(left, top, left + width, top + height);
		
		
		// Draw big squares
		for (int i = 0; i < Board.N_SQUARES; i++){
			Status status = bigboard.getStatus(i);
			Rect bounds = getSquareBounds(left, top, width, height, i);
			// Draw boards if playable
			if (status == Status.PLAYABLE){
				drawBoard(canvas,
						  bigboard.getSmallBoard(i),
						  bounds.left,
						  bounds.top,
						  bounds.width(),
						  bounds.height());
			} else{ // Draw the status of the board
				Bitmap b = getStatusBitmap(status);
				canvas.drawBitmap(b, null, getSquareBounds(left, top, width, height, i), null);
			}
		}
		canvas.drawBitmap(background, null, backgroundBounds, null);
	}
	
	public void drawBigBoard(Canvas canvas, BigBoard bigboard){
		int size = canvas.getWidth();
		if (canvas.getWidth()>canvas.getHeight())
			size = canvas.getHeight();
		drawBigBoard(canvas, bigboard, 0, 0, size, size);
	}
	
	public void drawFocusBoard(Canvas canvas, Status status)
	{
		
	}
	
	/**
	 * Returns the bounds for a Drawable at index.
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @return
	 */
	private Rect getSquareBounds(int left, int top, int width, int height, int index){
		int row = GameLogic.getRow(index);
		int col = GameLogic.getCol(index);
		int squareWidth = width / Board.SIDE_LENGTH;
		int squareHeight = height / Board.SIDE_LENGTH;
		return new Rect(left + col * squareWidth,
						top + row * squareHeight,
						left + (col + 1) * squareWidth,
						top + (row + 1) *squareHeight);
	}
}
