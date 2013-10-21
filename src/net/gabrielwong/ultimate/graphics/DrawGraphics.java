package net.gabrielwong.ultimate.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class DrawGraphics {

	public static void drawBigBoard(Canvas canvas)
	{

	}
	
	/**
	 * Resizing the bitmap using height and width
	 * @param height
	 * @param width
	 * @param image
	 */
	public void resizeBitmap(int height, Bitmap image)
	{
		image = Bitmap.createScaledBitmap(image, height, height, true);
	}
	
}
