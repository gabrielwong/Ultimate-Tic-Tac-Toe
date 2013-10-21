package net.gabrielwong.ultimate.graphics;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public class DrawBoard extends View {

	private Rect bigBoard[];
	
	// 4 for playable, tied, player one and player 2
	private Bitmap bigSquare[] = new Bitmap[4];
	private Bitmap smallSquare[] = new Bitmap[4];
	
	
	public DrawBoard(Context context)
	{
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		// Setting the values of the squares
		GraphicValues.setBigWidthSquare(canvas);
		GraphicValues.setSmallWidthSquare();
		
		
	}
}
