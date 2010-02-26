package org.alice.ide.operations.window;

public abstract class IsFrameShowingOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
	private javax.swing.JFrame frame;
	public IsFrameShowingOperation( boolean initialValue ) {
		super( initialValue );
		//todo
		if( initialValue ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					handleStateChange( true );
				}
			} );
		}
	}
	public IsFrameShowingOperation() {
		this( false );
	}
	private javax.swing.JFrame getFrame() {
		if( this.frame != null ) {
			//pass
		} else {
			this.frame = new javax.swing.JFrame();
			this.frame.setTitle( this.getTitle() );
			this.frame.getContentPane().add( this.createPane() );
			this.frame.setDefaultCloseOperation( javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE );
			this.frame.addWindowListener( new java.awt.event.WindowListener() {
				public void windowOpened(java.awt.event.WindowEvent e) {
				} 
				public void windowActivated(java.awt.event.WindowEvent e) {
				}
				public void windowDeiconified(java.awt.event.WindowEvent e) {
				}
				public void windowIconified(java.awt.event.WindowEvent e) {
				}
				public void windowDeactivated(java.awt.event.WindowEvent e) {
				}
				public void windowClosing(java.awt.event.WindowEvent e) {
					IsFrameShowingOperation.this.getButtonModel().setSelected( false );
				}
				public void windowClosed(java.awt.event.WindowEvent e) {
				}
			} );
			this.frame.pack();
		}
		return this.frame;
	}
	protected abstract java.awt.Component createPane();
	protected abstract String getTitle();
	@Override
	protected final void handleStateChange(boolean value) {
		javax.swing.JFrame frame = this.getFrame();
		//frame.setLocation( 1024, 0 );
		frame.setVisible( value );
	}
}
