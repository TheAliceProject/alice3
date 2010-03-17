package edu.cmu.cs.dennisc.croquet;

public abstract class KRoot {
	protected abstract java.awt.Window getAWTWindow();
	protected abstract java.awt.Frame getAWTFrame();
	protected abstract java.awt.Dialog getAWTDialog();
	
	protected abstract java.awt.Container getContentPane();
	public void addToContentPane( KComponent component, edu.cmu.cs.dennisc.croquet.KBorderPanel.KCardinalDirection cardinalDirection ) {
		this.getContentPane().add( component.getJComponent(), cardinalDirection.getInternal() );
	}
}
