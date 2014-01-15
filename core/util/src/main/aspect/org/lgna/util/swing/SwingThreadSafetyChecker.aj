package org.lgna.util.swing;

public aspect SwingThreadSafetyChecker {
	
	private static final boolean isInternalTesting = Boolean.valueOf( System.getProperty( "org.alice.ide.internalTesting", "false" ) );

	pointcut swingMethods() : call (* javax.swing..*.*(..)) || 
		call (javax.swing..*.new(..));

	pointcut safeSwingMethods() : call (* javax.swing..*.add*Listener(..)) ||
		call (* javax.swing..*.remove*Listener(..)) ||
		call (* javax.swing..*.getListeners(..)) ||
		call (* javax.swing..*.revalidate()) ||
		call (* javax.swing..*.invalidate()) ||
		call (* javax.swing..*.repaint()) ||
		target (javax.swing.SwingWorker) ||
		call (* javax.swing.SwingUtilities.invoke*(..)) ||
		call (* javax.swing.SwingUtilities.isEventDispatchThread()) ||
		call (void javax.swing.JComponent.setText(java.lang.String));

	before() : swingMethods() && !safeSwingMethods() && !within(SwingThreadSafetyChecker) {
		if( SwingThreadSafetyChecker.isInternalTesting && !java.awt.EventQueue.isDispatchThread() ) {
			StringBuilder edtWarning = new StringBuilder();
			edtWarning.append( "warning: Swing EDT violation: " ).append( thisJoinPointStaticPart.getSignature() ).append( "\n" );
			
			boolean firstSkipped = false;
			for ( StackTraceElement element : new Throwable().getStackTrace() ) {
				if ( firstSkipped ) {
					edtWarning.append( "\t at " ).append( element.toString() ).append( "\n" );
				} else {
					firstSkipped = true;
				}
			}
			System.err.print( edtWarning );
		}
	}
}
