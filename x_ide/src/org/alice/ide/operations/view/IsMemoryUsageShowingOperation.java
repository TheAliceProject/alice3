package org.alice.ide.operations.view;

public class IsMemoryUsageShowingOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
	public IsMemoryUsageShowingOperation() {
		super( false );
		this.putValue( javax.swing.Action.NAME, "Memory Usage" );
	}
	todo
	@Override
	protected void handleStateChange(boolean value) {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.getContentPane().add( new edu.cmu.cs.dennisc.memory.MemoryPane() );
		frame.setSize( 300, 80 );
		frame.setDefaultCloseOperation( javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		frame.setVisible( true );
	}
	@Override
	public boolean isSignificant() {
		return false;
	}
	
}
