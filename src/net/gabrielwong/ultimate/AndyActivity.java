package net.gabrielwong.ultimate;

import net.gabrielwong.ultimate.graphics.BoardView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AndyActivity extends Activity {
	
	BoardView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new BoardView(this);
		setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.andy, menu);
		return true;
	}

}
