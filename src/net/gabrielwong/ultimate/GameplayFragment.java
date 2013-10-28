package net.gabrielwong.ultimate;
import net.gabrielwong.ultimate.game.GameLogic;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GameplayFragment extends Fragment{
	
	private GameplayView view = null;
	private GameLogic logic = null;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = new GameplayView(inflater.getContext());
        
        view.addMoveListener(logic);
        logic.addStateChangeListener(view);
        
        return view;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		logic = new GameLogic();
	}
}
