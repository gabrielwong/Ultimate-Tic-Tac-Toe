package net.gabrielwong.ultimate;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


public class PauseMenuFragment extends Fragment implements OnClickListener {

	private Listener mListener;
	
	public interface Listener {
		public void onResumeButtonClicked();
		public void onSettingsButtonClicked();
		public void onMainMenuButtonClicked();
	}

	public PauseMenuFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_pause_menu, container,
				false);

		v.findViewById(R.id.resume_button).setOnClickListener(this);
	    v.findViewById(R.id.settings_button).setOnClickListener(this);
	    v.findViewById(R.id.main_menu_button).setOnClickListener(this);
	    
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
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.resume_button:
			mListener.onResumeButtonClicked();
			break;
		case R.id.settings_button:
			mListener.onSettingsButtonClicked();
			break;
		case R.id.main_menu_button:
			mListener.onMainMenuButtonClicked();
			break;
		}
	}
}
