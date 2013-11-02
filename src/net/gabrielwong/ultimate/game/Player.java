package net.gabrielwong.ultimate.game;

import net.gabrielwong.ultimate.game.event.MoveListener;
import net.gabrielwong.ultimate.game.event.StateChangeListener;

public interface Player extends StateChangeListener {
	public void setMoveListener(MoveListener l);
	public MoveListener getMoveListener();
}
