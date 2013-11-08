/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.issue.swing;

/**
 * @author Dennis Cosgrove
 */
public class JExceptionSubPane extends javax.swing.JPanel {
	private Thread thread;
	private Throwable throwable;

	protected Thread getThread() {
		return this.thread;
	}

	protected Throwable getThrowable() {
		return this.throwable;
	}

	public void setThreadAndThrowable( final Thread thread, final Throwable throwable ) {
		assert thread != null;
		assert throwable != null;
		this.thread = thread;
		this.throwable = throwable;
		this.removeAll();
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
		edu.cmu.cs.dennisc.javax.swing.components.JFauxHyperlink vcShowStackTrace = new edu.cmu.cs.dennisc.javax.swing.components.JFauxHyperlink( new javax.swing.AbstractAction( "show complete stack trace..." ) {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				edu.cmu.cs.dennisc.javax.swing.SwingUtilities.showMessageDialogInScrollableUneditableTextArea( JExceptionSubPane.this, edu.cmu.cs.dennisc.java.lang.ThrowableUtilities.getStackTraceAsString( throwable ), "Stack Trace", javax.swing.JOptionPane.INFORMATION_MESSAGE );
			}
		} );

		StringBuffer sb = new StringBuffer();
		sb.append( throwable.getClass().getSimpleName() );
		String message = throwable.getLocalizedMessage();
		if( ( message != null ) && ( message.length() > 0 ) ) {
			sb.append( "[" );
			sb.append( message );
			sb.append( "]" );
		}
		sb.append( " in " );
		sb.append( thread.getClass().getSimpleName() );
		sb.append( "[" );
		sb.append( thread.getName() );
		sb.append( "]" );

		this.add( new javax.swing.JLabel( sb.toString() ) );
		StackTraceElement[] elements = throwable.getStackTrace();
		if( elements.length > 0 ) {
			StackTraceElement e0 = elements[ 0 ];
			this.add( new javax.swing.JLabel( "class: " + e0.getClassName() ) );
			this.add( new javax.swing.JLabel( "method: " + e0.getMethodName() ) );
			this.add( new javax.swing.JLabel( "in file " + e0.getFileName() + " at line number " + e0.getLineNumber() ) );
		}
		this.add( vcShowStackTrace );
	}
}
