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
package edu.cmu.cs.dennisc.javax.swing;

/**
 * @author Dennis Cosgrove
 */
public abstract class ApplicationFrame extends javax.swing.JFrame {
	public ApplicationFrame() {
		this.setDefaultCloseOperation( javax.swing.JFrame.DO_NOTHING_ON_CLOSE );
		this.addWindowListener( new java.awt.event.WindowListener() {
			public void windowOpened( java.awt.event.WindowEvent e ) {
				ApplicationFrame.this.handleWindowOpened( e );
			}
			public void windowClosed( java.awt.event.WindowEvent e ) {
			}
			public void windowClosing( java.awt.event.WindowEvent e ) {
				ApplicationFrame.this.handleQuit( e );
			}
			public void windowActivated( java.awt.event.WindowEvent e ) {
			}
			public void windowDeactivated( java.awt.event.WindowEvent e ) {
			}
			public void windowIconified( java.awt.event.WindowEvent e ) {
			}
			public void windowDeiconified( java.awt.event.WindowEvent e ) {
			}
		} );
		edu.cmu.cs.dennisc.apple.AppleUtilities.addApplicationListener( new edu.cmu.cs.dennisc.apple.event.ApplicationListener() {
			public void handleAbout( java.util.EventObject e ) {
			}
			public void handlePreferences( java.util.EventObject e ) {
			}
			public void handleQuit( java.util.EventObject e ) {
				ApplicationFrame.this.handleQuit( e );
			}
		} );
		
	}
	public void maximize() {
		this.setExtendedState( this.getExtendedState() | java.awt.Frame.MAXIMIZED_BOTH );
	}
	
	protected abstract void handleWindowOpened( java.awt.event.WindowEvent e );
	//protected abstract void handleWindowClosing();
	protected abstract void handleAbout( java.util.EventObject e );
	protected abstract void handlePreferences( java.util.EventObject e );
	protected abstract void handleQuit( java.util.EventObject e );
}
