package net.gabrielwong.ultimate;

import net.gabrielwong.ultimate.game.GameLogic;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.example.games.basegameutils.BaseGameActivity;

public class MainActivity extends BaseGameActivity implements
	MainMenuFragment.Listener, MultiplayerMenuFragment.Listener{
	
	// Debug stuff
	final boolean ENABLE_DEBUG = true;
	final String TAG = "MainActivity";
	
	final String BACK_TAG = "asdf123";
	
	// Fragments
	GameplayFragment mGameplayFragment = null;
	MultiplayerMenuFragment mMultiplayerFragment = null;
	MainMenuFragment mMainFragment = null;
	SettingsFragment mSettingsFragment = null;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            enableDebugLog(ENABLE_DEBUG, TAG);
            setContentView(R.layout.activity_main);
            
            // Create fragments
            mGameplayFragment = new GameplayFragment();
            mMultiplayerFragment = new MultiplayerMenuFragment();
            mMainFragment = new MainMenuFragment();
            mSettingsFragment = new SettingsFragment();
           
            switchToFragment(mMainFragment);
    }
	
	void switchToFragment(Fragment newFrag){
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, newFrag).addToBackStack(null).commit();
	}
	
	void startLocalMultiplayerGame(){
		GameLogic logic = new GameLogic();
		logic.addStateChangeListener(mGameplayFragment);
		mGameplayFragment.addMoveListener(logic);
		switchToFragment(mGameplayFragment);
	}
	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
    }

	@Override
	public void onSignInFailed() {
		mMainFragment.setSignedIn(false);
	}

	@Override
	public void onSignInSucceeded() {
		mMainFragment.setSignedIn(true);
	}

	@Override
	public void onSignInButtonClicked() {
		beginUserInitiatedSignIn();
	}

	@Override
	public void onSignOutButtonClicked() {
		signOut();
		mMainFragment.setSignedIn(false);
	}

	@Override
	public void onLocalMultiplayerButtonClicked() {
		startLocalMultiplayerGame();
	}

	@Override
	public void onMultiplayerButtonClicked() {
		switchToFragment(mMultiplayerFragment);
	}

	@Override
	public void onSettingsButtonClicked() {
		switchToFragment(mSettingsFragment);
	}
	
	@Override
	public void onBackPressed(){
		FragmentManager fm = getFragmentManager();
		System.out.println(fm.getBackStackEntryCount());
		if (fm.getBackStackEntryCount() > 1)
			fm.popBackStack();
	}
}
