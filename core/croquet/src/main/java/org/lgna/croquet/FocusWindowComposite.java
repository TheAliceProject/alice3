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

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.triggers.EventObjectTrigger;
import org.lgna.croquet.triggers.Trigger;
import org.lgna.croquet.views.Frame;
import org.lgna.croquet.views.Panel;
import org.lgna.croquet.views.SwingComponentView;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.EventObject;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class FocusWindowComposite extends AbstractComposite<Panel> {
	private class LaunchFocusWindowOperation extends Operation {
		private final Frame window = new Frame();
		private final WindowFocusListener windowFocusListener = new WindowFocusListener() {
			@Override
			public void windowGainedFocus( WindowEvent e ) {
			}

			@Override
			public void windowLostFocus( WindowEvent e ) {
				JFrame window = (JFrame)e.getComponent();
				if( window.isUndecorated() ) {
					//note: dialog operations or browser operations will need to be addressed
					e.getComponent().setVisible( false );
				}
			}
		};

		private final ActionListener decoratedListener = new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				window.setUndecorated( window.isUndecorated() == false );
			}
		};

		public LaunchFocusWindowOperation() {
			super( Application.INFORMATION_GROUP, UUID.fromString( "91cee3e8-4f91-4eb2-be0e-fb86498a2031" ) );
			window.getAwtComponent().addWindowFocusListener( this.windowFocusListener );
			window.getAwtComponent().setUndecorated( true );
			window.getAwtComponent().getRootPane().registerKeyboardAction( this.decoratedListener, KeyStroke.getKeyStroke( KeyEvent.VK_F2, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );
		}

		@Override
		protected Class<? extends Element> getClassUsedForLocalization() {
			return FocusWindowComposite.this.getClassUsedForLocalization();
		}

		@Override
		protected void performInActivity( UserActivity userActivity ) {
			SwingComponentView<?> view = FocusWindowComposite.this.getRootComponent();
			if( view.getParent() == null ) {
				window.getContentPane().addCenterComponent( view );
			}
			window.pack();
			final Trigger trigger = userActivity.getTrigger();
			if( trigger instanceof EventObjectTrigger ) {
				EventObjectTrigger<EventObject> eventObjectTrigger = (EventObjectTrigger<EventObject>)trigger;
				EventObject eventObject = eventObjectTrigger.getEvent();
				Object source = eventObject.getSource();
				if( source instanceof Component ) {
					Component awtSource = (Component)source;
					window.setLocation( calculateLocationOnScreenForFocusWindow( awtSource, window ) );
				}
			} else {
				Logger.outln( trigger );
			}
			window.setVisible( true );
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					window.getAwtComponent().getRootPane().requestFocus();
				}
			} );
		}
	}

	private final LaunchFocusWindowOperation launchOperation = new LaunchFocusWindowOperation();

	public FocusWindowComposite( UUID migrationId ) {
		super( migrationId );
	}

	public Operation getLaunchOperation() {
		return this.launchOperation;
	}

	protected Point calculateLocationOnScreenForFocusWindow( Component awtSource, Frame focusWindow ) {
		Point p = awtSource.getLocationOnScreen();
		final int PAD = 8;
		return new Point( p.x + awtSource.getWidth() + PAD, p.y );
	}
}
