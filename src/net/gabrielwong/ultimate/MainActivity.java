
package net.gabrielwong.ultimate;

import net.gabrielwong.ultimate.game.AI;
import net.gabrielwong.ultimate.game.GameLogic;
import net.gabrielwong.ultimate.game.RandomAI;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;

import com.google.example.games.basegameutils.BaseGameActivity;

public class MainActivity extends BaseGameActivity implements
	MainMenuFragment.Listener, MultiplayerMenuFragment.Listener, GameplayFragment.Listener{
	
	// Debug stuff
	final boolean ENABLE_DEBUG = true;
	final String TAG = "MainActivity";
	
	final static int RC_UNUSED = 5001;
	
	final String BACK_TAG = "asdf123";
	
	GameLogic logic = null;
	AI ai = null;
	
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
           
            switchToFragment(mMainFragment, false);
    }
	
	void switchToFragment(Fragment newFrag){
		switchToFragment(newFrag, true);
	}
	
	void switchToFragment(Fragment newFrag, boolean addToBackStack){
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, newFrag);
		if (addToBackStack)
			transaction.addToBackStack(null);
		transaction.commit();
	}
	
	void startLocalMultiplayerGame(){
		logic = new GameLogic();
		logic.addStateChangeListener(mGameplayFragment);
		mGameplayFragment.addMoveListener(logic);
		switchToFragment(mGameplayFragment);
	}
	
	void startSingleplayerGame(){
		logic = new GameLogic();
		logic.addStateChangeListener(mGameplayFragment);
		mGameplayFragment.addMoveListener(logic);
		
		ai = new RandomAI();
		ai.setPlayerId(1);
		ai.setMoveListener(logic);
		logic.addStateChangeListener(ai);
		
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
		mMainFragment.setSignedIn(isSignedIn());
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
		if (fm.getBackStackEntryCount() > 0)
			fm.popBackStack();
		else
			super.onBackPressed();
	}

	@Override
	public void onAchievementsButtonClicked() {
		if (isSignedIn()) {
            startActivityForResult(getGamesClient().getAchievementsIntent(), RC_UNUSED);
        } else {
            showAlert(getString(R.string.achievements_not_available));
        }
	}

	@Override
	public void onEndGame() {
		mGameplayFragment.removeMoveListener(logic);
		if (ai != null){
			ai.setMoveListener(null);
			ai = null;
		}
		logic = null;
		getFragmentManager().popBackStack();
	}

	@Override
	public void onSingleplayerButtonClicked() {
		startSingleplayerGame();
	}

	@Override
	public void onOnlineMultiplayerButtonClicked() {
		// TODO Auto-generated method stub
		
	}
}
