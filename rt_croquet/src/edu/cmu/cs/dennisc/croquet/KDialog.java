package edu.cmu.cs.dennisc.croquet;

public class KDialog extends KRoot {
	private javax.swing.JDialog jDialog;
	public KDialog() {
		this( null );
	}
	public KDialog( KRoot owner ) {
		if( owner != null ) {
			java.awt.Frame ownerFrame = owner.getAWTFrame();
			if( ownerFrame != null ) {
				this.jDialog = new javax.swing.JDialog( ownerFrame );
			} else {
				java.awt.Dialog ownerDialog = owner.getAWTDialog();
				if( ownerDialog != null ) {
					this.jDialog = new javax.swing.JDialog( ownerDialog );
				}
			}
		}
		if( this.jDialog != null ) {
			//pass
		} else {
			this.jDialog = new javax.swing.JDialog();
		}
	}
	@Override
	protected java.awt.Dialog getAWTDialog() {
		return this.jDialog;
	}
	@Override
	protected java.awt.Frame getAWTFrame() {
		return null;
	}
	@Override
	protected java.awt.Window getAWTWindow() {
		return this.jDialog;
	}
	@Override
	protected java.awt.Container getContentPane() {
		return this.jDialog.getContentPane();
	}
}
