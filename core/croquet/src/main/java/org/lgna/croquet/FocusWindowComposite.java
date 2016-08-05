/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class FocusWindowComposite extends AbstractComposite<org.lgna.croquet.views.Panel> {
	private class LaunchFocusWindowOperation extends Operation {
		private final org.lgna.croquet.views.Frame window = new org.lgna.croquet.views.Frame();
		private final java.awt.event.WindowFocusListener windowFocusListener = new java.awt.event.WindowFocusListener() {
			@Override
			public void windowGainedFocus( java.awt.event.WindowEvent e ) {
			}

			@Override
			public void windowLostFocus( java.awt.event.WindowEvent e ) {
				javax.swing.JFrame window = (javax.swing.JFrame)e.getComponent();
				if( window.isUndecorated() ) {
					//note: dialog operations or browser operations will need to be addressed 
					e.getComponent().setVisible( false );
				}
			}
		};

		private final java.awt.event.ActionListener decoratedListener = new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				window.setUndecorated( window.isUndecorated() == false );
			}
		};

		public LaunchFocusWindowOperation() {
			super( Application.INFORMATION_GROUP, java.util.UUID.fromString( "91cee3e8-4f91-4eb2-be0e-fb86498a2031" ) );
			window.getAwtComponent().addWindowFocusListener( this.windowFocusListener );
			window.getAwtComponent().setUndecorated( true );
			window.getAwtComponent().getRootPane().registerKeyboardAction( this.decoratedListener, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F2, 0 ), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW );
		}

		@Override
		protected Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return FocusWindowComposite.this.getClassUsedForLocalization();
		}

		@Override
		protected void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			org.lgna.croquet.views.SwingComponentView<?> view = FocusWindowComposite.this.getRootComponent();
			if( view.getParent() == null ) {
				window.getContentPane().addCenterComponent( view );
			}
			window.pack();
			if( trigger instanceof org.lgna.croquet.triggers.EventObjectTrigger ) {
				org.lgna.croquet.triggers.EventObjectTrigger<java.util.EventObject> eventObjectTrigger = (org.lgna.croquet.triggers.EventObjectTrigger<java.util.EventObject>)trigger;
				java.util.EventObject eventObject = eventObjectTrigger.getEvent();
				Object source = eventObject.getSource();
				if( source instanceof java.awt.Component ) {
					java.awt.Component awtSource = (java.awt.Component)source;
					window.setLocation( calculateLocationOnScreenForFocusWindow( awtSource, window ) );
				}
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( trigger );
			}
			window.setVisible( true );
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					window.getAwtComponent().getRootPane().requestFocus();
				}
			} );
		}
	}

	private final LaunchFocusWindowOperation launchOperation = new LaunchFocusWindowOperation();

	public FocusWindowComposite( java.util.UUID migrationId ) {
		super( migrationId );
	}

	public Operation getLaunchOperation() {
		return this.launchOperation;
	}

	protected java.awt.Point calculateLocationOnScreenForFocusWindow( java.awt.Component awtSource, org.lgna.croquet.views.Frame focusWindow ) {
		java.awt.Point p = awtSource.getLocationOnScreen();
		final int PAD = 8;
		return new java.awt.Point( p.x + awtSource.getWidth() + PAD, p.y );
	}
}
