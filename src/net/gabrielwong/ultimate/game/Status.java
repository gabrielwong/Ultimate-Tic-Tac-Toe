package net.gabrielwong.ultimate.game;

/**
 * Representation of possible pieces on the board. A location on the board can be empty (PLAYABLE), won by a player
 * (PLAYER_ZERO or PLAYER_ONE) or the big board can have a board that is full but not won (TIE). This class also
 * represents the condition of the game as a whole (ongoing, won or tied).
 * @author Gabriel
 *
 */
public enum Status {
	PLAYABLE,
	TIE,
	PLAYER_ZERO,
	PLAYER_ONE;
}
