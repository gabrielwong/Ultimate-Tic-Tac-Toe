package net.gabrielwong.ultimate.game.event;

public abstract class Message {
	private final Object source;
	private final int type;
	
	public Message(Object source, int type){
		this.source = source;
		this.type = type;
	}
	
	public Object getSource(){
		return source;
	}
	
	public int getType(){
		return type;
	}
}
