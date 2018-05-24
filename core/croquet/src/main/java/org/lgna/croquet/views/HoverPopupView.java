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
package org.lgna.croquet.views;

import org.lgna.croquet.HoverPopupElement;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JWindow;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Dennis Cosgrove
 */
public class HoverPopupView extends SwingComponentView<javax.swing.AbstractButton> {
	private final MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseEntered( MouseEvent e ) {
			//javax.swing.SwingUtilities.invokeLater( new Runnable() {
			//	public void run() {
			showWindow();
			//	}
			//} );
		}

		@Override
		public void mouseExited( MouseEvent e ) {
			//javax.swing.SwingUtilities.invokeLater( new Runnable() {
			//	public void run() {
			hideWindow();
			//	}
			//} );
		}

		@Override
		public void mousePressed( MouseEvent e ) {
		}

		@Override
		public void mouseReleased( MouseEvent e ) {
		}

		@Override
		public void mouseClicked( MouseEvent e ) {
		}
	};

	private final JWindow window;
	private final HoverPopupElement element;

	public HoverPopupView( HoverPopupElement element ) {
		this.element = element;
		this.window = new JWindow();
		this.window.setAlwaysOnTop( true );
	}

	private void showWindow() {
		this.element.getComposite().handlePreActivation();
		synchronized( this.window.getTreeLock() ) {
			assert this.window.isVisible() == false;
			Point p = this.getLocationOnScreen();
			this.window.getContentPane().add( this.element.getComposite().getRootComponent().getAwtComponent() );
			this.window.setLocation( p.x + this.getWidth() + 16, ( p.y + this.getHeight() ) - 4 );
			this.window.pack();
			this.window.setVisible( true );
			//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "show", this );
		}
	}

	private void hideWindow() {
		synchronized( this.window.getTreeLock() ) {
			//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "hide", this );
			this.window.setVisible( false );
			this.window.getContentPane().removeAll();
			final boolean EPIC_HACK_isAccountingForHideNotAlwaysWorking = true;
			if( EPIC_HACK_isAccountingForHideNotAlwaysWorking ) {
				this.window.pack();
			} else {
				this.window.setSize( 1000, 1000 );
			}
		}
		this.element.getComposite().handlePostDeactivation();
	}

	@Override
	protected void handleAddedTo( AwtComponentView<?> parent ) {
		this.addMouseListener( this.mouseListener );
		super.handleAddedTo( parent );
	}

	@Override
	protected void handleRemovedFrom( AwtComponentView<?> parent ) {
		super.handleRemovedFrom( parent );
		this.removeMouseListener( this.mouseListener );
	}

	private class JHoverPopupView extends JButton {
		public JHoverPopupView() {
			this.setRolloverEnabled( true );
			this.setOpaque( false );
		}

		@Override
		public void updateUI() {
			this.setUI( BasicButtonUI.createUI( this ) );
		}

		@Override
		public Icon getRolloverIcon() {
			return super.getRolloverIcon();
		}
	}

	@Override
	protected javax.swing.AbstractButton createAwtComponent() {
		return new JHoverPopupView();
	}
}
