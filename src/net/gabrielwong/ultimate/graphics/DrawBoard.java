package net.gabrielwong.ultimate.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public class DrawBoard extends View {

	private Rect bigBoard[];
	
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
