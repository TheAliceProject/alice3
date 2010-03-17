package edu.cmu.cs.dennisc.croquet;

public class KWindow extends KRoot {
	private javax.swing.JWindow jWindow;
	public KWindow() {
		this( null );
	}
	public KWindow( KRoot owner ) {
		if( owner != null ) {
			java.awt.Frame ownerFrame = owner.getAWTFrame();
			if( ownerFrame != null ) {
				this.jWindow = new javax.swing.JWindow( ownerFrame );
			} else {
				java.awt.Window ownerWindow = owner.getAWTWindow();
				if( ownerWindow != null ) {
					this.jWindow = new javax.swing.JWindow( ownerWindow );
				}
			}
		}
		if( this.jWindow != null ) {
			//pass
		} else {
			this.jWindow = new javax.swing.JWindow();
		}
	}
	@Override
	protected java.awt.Dialog getAWTDialog() {
		return null;
	}
	@Override
	protected java.awt.Frame getAWTFrame() {
		return null;
	}
	@Override
	protected java.awt.Window getAWTWindow() {
		return this.jWindow;
	}
	@Override
	protected java.awt.Container getContentPane() {
		return this.jWindow.getContentPane();
	}
}
