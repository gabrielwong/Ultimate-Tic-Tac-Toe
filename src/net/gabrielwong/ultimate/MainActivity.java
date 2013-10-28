package net.gabrielwong.ultimate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.google.example.games.basegameutils.BaseGameActivity;

public class MainActivity extends BaseGameActivity {
	
	// Debug stuff
	final boolean ENABLE_DEBUG = true;
	final String TAG = "MainActivity";
	
	// Fragments
	GameplayFragment gameplayFragment = null;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            enableDebugLog(ENABLE_DEBUG, TAG);
            setContentView(R.layout.activity_main);
            
            gameplayFragment = new GameplayFragment();
            
            switchToFragment(gameplayFragment);
    }
	
	void switchToFragment(Fragment newFrag){
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFrag)
        	.commit();
	}
	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
    }

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		
	}
}
