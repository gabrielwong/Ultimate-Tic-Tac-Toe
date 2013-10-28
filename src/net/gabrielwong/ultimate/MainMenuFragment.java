package net.gabrielwong.ultimate;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link MainMenuFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link MainMenuFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class MainMenuFragment extends Fragment implements OnClickListener{
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private Listener mListener;
	
	public interface Listener {
		public void onSignInButtonClicked();
		public void onSignOutButtonClicked();
		public void onMultiplayerButtonClicked();
		public void onSettingsButtonClicked();
	}

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment MainMenuFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static MainMenuFragment newInstance(String param1, String param2) {
		MainMenuFragment fragment = new MainMenuFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public MainMenuFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main_menu, container,
				false);

		v.findViewById(R.id.sign_in_button).setOnClickListener(this);
	    v.findViewById(R.id.sign_out_button).setOnClickListener(this);
	    v.findViewById(R.id.multiplayer_button).setOnClickListener(this);
	    v.findViewById(R.id.settings_button).setOnClickListener(this);
	    
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
		case R.id.sign_in_button:
			mListener.onSignInButtonClicked();
			break;
		case R.id.sign_out_button:
			mListener.onSignOutButtonClicked();
			break;
		case R.id.multiplayer_button:
			mListener.onMultiplayerButtonClicked();
			break;
		case R.id.settings_button:
			mListener.onSettingsButtonClicked();
			break;
		}
	}
	
	public void setSignedIn(boolean isSignedIn){
		if (getView() == null)
			return;
		int showWhenSignedIn = isSignedIn ? View.VISIBLE : View.GONE;
		int showWhenSignedOut = isSignedIn ? View.GONE : View.VISIBLE;
		getView().findViewById(R.id.sign_in_button).setVisibility(showWhenSignedOut);
	    getView().findViewById(R.id.sign_out_button).setVisibility(showWhenSignedIn);
	}
}
