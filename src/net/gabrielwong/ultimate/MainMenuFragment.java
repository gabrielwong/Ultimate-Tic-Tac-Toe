package net.gabrielwong.ultimate;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


public class MainMenuFragment extends Fragment implements OnClickListener{

	private Listener mListener;
	boolean mSignedIn = false;
	
	public interface Listener {
		public void onSignInButtonClicked();
		public void onSignOutButtonClicked();
		public void onSingleplayerButtonClicked();
		public void onMultiplayerButtonClicked();
		public void onAchievementsButtonClicked();
		public void onSettingsButtonClicked();
	}

	public MainMenuFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main_menu, container,
				false);

		v.findViewById(R.id.sign_in_button).setOnClickListener(this);
	    v.findViewById(R.id.sign_out_button).setOnClickListener(this);
	    v.findViewById(R.id.singleplayer_button).setOnClickListener(this);
	    v.findViewById(R.id.multiplayer_button).setOnClickListener(this);
	    v.findViewById(R.id.settings_button).setOnClickListener(this);
	    v.findViewById(R.id.achievements_button).setOnClickListener(this);
	    
	    return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (Listener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}
	
	@Override
	public void onStart(){
		super.onStart();
		updateUi();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.sign_in_button:
			mListener.onSignInButtonClicked();
			break;
		case R.id.sign_out_button:
			mListener.onSignOutButtonClicked();
			break;
		case R.id.multiplayer_button:
			mListener.onMultiplayerButtonClicked();
			break;
		case R.id.achievements_button:
			mListener.onAchievementsButtonClicked();
			break;
		case R.id.settings_button:
			mListener.onSettingsButtonClicked();
			break;
		case R.id.singleplayer_button:
			mListener.onSingleplayerButtonClicked();
			break;
		}
	}
	
	public void setSignedIn(boolean isSignedIn){
		mSignedIn = isSignedIn;
		updateUi();
	}
	
	public void updateUi(){
		if (getView() == null)
			return;
		int showWhenSignedIn = mSignedIn ? View.VISIBLE : View.GONE;
		int showWhenSignedOut = mSignedIn ? View.GONE : View.VISIBLE;
		getView().findViewById(R.id.sign_in_button).setVisibility(showWhenSignedOut);
	    getView().findViewById(R.id.sign_out_button).setVisibility(showWhenSignedIn);
	    getView().findViewById(R.id.achievements_button).setVisibility(showWhenSignedIn);
	}
	
}
