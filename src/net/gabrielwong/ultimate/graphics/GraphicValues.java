package net.gabrielwong.ultimate.graphics;

import android.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class GraphicValues {

	private static int bigWidthSquare;
	private static int smallWidthSquare;
	
	public static void initValues(Canvas canvas)
	{
		setBigWidthSquare(canvas);
		setSmallWidthSquare();
	}
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
	
	/**
	 * Define color of Bitmap as blue
	 * @param square
	 */
	public static void getBlueSquare(Bitmap square)
	{
		square = BitmapFactory.decodeFile(R.drawable.bluesquare);
	}
	
	/**
	 * Define color of Bitmap as green
	 * @param square
	 */
	public static void getGreenSquare(Bitmap square)
	{
		square = BitmapFactory.decodeFile(R.drawable.greensquare);
	}
	
	/**
	 * Define color of Bitmap as red
	 * @param square
	 */
	public static void getRedSquare(Bitmap square)
	{
		square = BitmapFactory.decodeFile(R.drawable.redsquare);
	}
}
