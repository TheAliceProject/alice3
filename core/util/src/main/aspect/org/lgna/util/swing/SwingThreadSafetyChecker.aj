package org.lgna.util.swing;

public aspect SwingThreadSafetyChecker {

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
		boolean internalTesting = Boolean.valueOf( System.getProperty( "org.alice.ide.internalTesting", "false" ) );
		if( internalTesting && !java.awt.EventQueue.isDispatchThread() ) {
			System.err.println( "warning: Swing EDT violation: " + thisJoinPoint.getSignature() + " (" + thisJoinPoint.getSourceLocation() + ")" );
		}
	}
}
