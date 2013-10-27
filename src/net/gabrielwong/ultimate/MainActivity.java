package net.gabrielwong.ultimate;

import net.gabrielwong.ultimate.game.GameLogic;
import net.gabrielwong.ultimate.graphics.BoardView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	BoardView graphicView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GameLogic logic = new GameLogic();
		graphicView = new BoardView(this);
		logic.addStateChangeListener(graphicView);
		graphicView.addMoveListener(logic);
		
		setContentView(graphicView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
