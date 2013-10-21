package net.gabrielwong.ultimate.graphics;

import android.graphics.Canvas;

public class GraphicValues {

	private static int bigWidthSquare;
	private static int smallWidthSquare;
	
	/**
	 * Set the constant bigWidthSquare to be the width of the screen
	 * @param canvas
	 */
	public static void setBigWidthSquare(Canvas canvas)
	{
		bigWidthSquare = canvas.getWidth();
	}
	
	/**
	 * Set the smallWidthSquare constant to be equal to a third of smallWidthSquare
	 */
	public static void setSmallWidthSquare()
	{
		smallWidthSquare = bigWidthSquare/3;
	}
}
