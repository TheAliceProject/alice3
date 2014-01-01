package org.lgna.util.swing;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SwingThreadSafetyChecker {

	@Pointcut( "call (* javax.swing..*.*(..)) || "
			+ "call (javax.swing..*.new(..))" )
	public void swingMethods() {
	}

	@Pointcut( "call (* javax.swing..*.add*Listener(..)) || "
			+ "call (* javax.swing..*.remove*Listener(..)) || "
			+ "call (* javax.swing..*.getListeners(..)) || "
			+ "call (* javax.swing..*.revalidate()) || "
			+ "call (* javax.swing..*.invalidate()) || "
			+ "call (* javax.swing..*.repaint()) || "
			+ "target (javax.swing.SwingWorker) || "
			+ "call (* javax.swing.SwingUtilities.invoke*(..)) || "
			+ "call (* javax.swing.SwingUtilities.isEventDispatchThread()) || "
			+ "call (void javax.swing.JComponent.setText(java.lang.String))" )
	public void safeSwingMethods() {
	}

	@Before( "swingMethods() && !safeSwingMethods() && !within(SwingThreadSafetyChecker)" )
	public void checkCallingThread( final JoinPoint.StaticPart thisJoinPointStatic ) {
		if( !java.awt.EventQueue.isDispatchThread() ) {
			System.err.println( "warning: Swing EDT violation: " + thisJoinPointStatic.getSignature() + " (" + thisJoinPointStatic.getSourceLocation() + ")" );
		}
	}
}
