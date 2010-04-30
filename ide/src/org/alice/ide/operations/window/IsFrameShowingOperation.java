/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.operations.window;

public abstract class IsFrameShowingOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
	private javax.swing.JFrame frame;
	public IsFrameShowingOperation( java.util.UUID groupUUID, java.util.UUID individualId, boolean initialValue, String trueAndFalseText ) {
		super( groupUUID, individualId, initialValue, trueAndFalseText );
		//todo
		if( initialValue ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					handleStateChange( true );
				}
			} );
		}
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
