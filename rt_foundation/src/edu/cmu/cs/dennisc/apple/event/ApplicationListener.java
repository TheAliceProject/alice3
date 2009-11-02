package edu.cmu.cs.dennisc.apple.event;

public interface ApplicationListener {
	public void handlePreferences( java.util.EventObject e );
	public void handleAbout( java.util.EventObject e );
	public void handleQuit( java.util.EventObject e );
}
