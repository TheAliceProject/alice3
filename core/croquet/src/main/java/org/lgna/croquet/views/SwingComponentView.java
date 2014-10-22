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

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public abstract class SwingComponentView<J extends javax.swing.JComponent> extends AwtContainerView<J> {

	@Override
	public void setBackgroundColor( java.awt.Color color ) {
		super.setBackgroundColor( color );
		this.getAwtComponent().setOpaque( color != null );
	}

	public void setAlignmentX( float alignmentX ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setAlignmentX( alignmentX );
	}

	public void setAlignmentY( float alignmentY ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setAlignmentY( alignmentY );
	}

	@Override
	public java.awt.Rectangle getVisibleRectangle() {
		return this.getAwtComponent().getVisibleRect();
	}

	public java.awt.Insets getInsets() {
		return this.getAwtComponent().getInsets();
	}

	private void scrollRectToVisible( java.awt.Rectangle rect ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().scrollRectToVisible( rect );
	}

	public final void scrollToVisible() {
		this.scrollRectToVisible( javax.swing.SwingUtilities.getLocalBounds( this.getAwtComponent() ) );
	}

	public String getToolTipText() {
		return this.getAwtComponent().getToolTipText();
	}

	public void setToolTipText( String toolTipText ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setToolTipText( toolTipText );
	}

	public javax.swing.border.Border getBorder() {
		return this.getAwtComponent().getBorder();
	}

	public void setBorder( javax.swing.border.Border border ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setBorder( border );
	}

	public void setOpaque( boolean isOpaque ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setOpaque( isOpaque );
	}

	public static enum Condition {
		WHEN_FOCUSED( javax.swing.JComponent.WHEN_FOCUSED ),
		WHEN_IN_FOCUSED_WINDOW( javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW ),
		WHEN_ANCESTOR_OF_FOCUSED_COMPONENT( javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
		private int internal;

		private Condition( int internal ) {
			this.internal = internal;
		}

		private int getInternal() {
			return this.internal;
		}

		public static Condition valueOf( int constant ) {
			switch( constant ) {
			case javax.swing.JComponent.WHEN_FOCUSED:
				return WHEN_FOCUSED;
			case javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW:
				return WHEN_IN_FOCUSED_WINDOW;
			case javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT:
				return WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
			default:
				return null;
			}
		}
	}

	public void registerKeyboardAction( java.awt.event.ActionListener actionListener, javax.swing.KeyStroke keyStroke, Condition condition ) {
		this.getAwtComponent().registerKeyboardAction( actionListener, keyStroke, condition.getInternal() );
	}

	public void unregisterKeyboardAction( javax.swing.KeyStroke keyStroke ) {
		this.getAwtComponent().unregisterKeyboardAction( keyStroke );
	}

	private void revalidate() {
		this.getAwtComponent().revalidate();
	}

	public void revalidateAndRepaint() {
		this.revalidate();
		this.repaint();
	}
}
