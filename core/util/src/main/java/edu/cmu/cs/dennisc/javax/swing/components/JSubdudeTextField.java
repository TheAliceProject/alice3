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
package edu.cmu.cs.dennisc.javax.swing.components;

/**
 * @author Dennis Cosgrove
 */
//TODO: provide custom java.awt.FocusTraversalPolicy to skip JSubtleTextFields?
public class JSubdudeTextField extends edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField {
	public JSubdudeTextField() {
		javax.swing.InputMap inputMap = this.getInputMap();
		inputMap.put( javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_ESCAPE, 0 ), this.transferFocusAction );
		inputMap.put( javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_ENTER, 0 ), this.transferFocusAction );
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		java.awt.Insets insets = this.getInsets();
		return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( super.getPreferredSize(), 24 + insets.left + insets.right );
	}

	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}

	private void installListeners() {
		if( edu.cmu.cs.dennisc.java.util.Arrays.contains( this.getMouseListeners(), this.mouseListener ) ) {
			//pass
		} else {
			this.addMouseListener( this.mouseListener );
			this.addFocusListener( this.focusListener );
			this.getDocument().addDocumentListener( this.documentListener );
		}
	}

	private void uninstallListeners() {
		if( edu.cmu.cs.dennisc.java.util.Arrays.contains( this.getMouseListeners(), this.mouseListener ) ) {
			this.getDocument().removeDocumentListener( this.documentListener );
			this.removeFocusListener( this.focusListener );
			this.removeMouseListener( this.mouseListener );
		} else {
			//pass
		}
	}

	@Override
	public void addNotify() {
		super.addNotify();
		this.installListeners();
	}

	@Override
	public void removeNotify() {
		this.uninstallListeners();
		super.removeNotify();
	}

	@Override
	public void paint( java.awt.Graphics g ) {
		if( this.hasFocus() ) {
			super.paint( g );
		} else {
			edu.cmu.cs.dennisc.java.awt.GraphicsContext gc = edu.cmu.cs.dennisc.java.awt.GraphicsContext.getInstanceAndPushGraphics( g );
			gc.pushAndSetTextAntialiasing( true );
			gc.pushFont();
			gc.pushPaint();
			java.awt.Color backgroundColor = this.getParent().getBackground();
			if( this.buttonModel.isRollover() ) {
				backgroundColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( backgroundColor, 1.0, 0.9, 0.9 );
			}
			g.setColor( backgroundColor );
			g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
			g.setColor( this.getForeground() );
			g.setFont( this.getFont() );
			java.awt.FontMetrics fm = g.getFontMetrics();
			java.awt.Insets insets = this.getInsets();
			java.awt.ComponentOrientation componentOrientation = this.getComponentOrientation();
			String text = this.getText();
			int x;
			if( componentOrientation.isLeftToRight() ) {
				x = insets.left;
			} else {
				x = this.getWidth() - insets.right;
				java.awt.geom.Rectangle2D bounds = fm.getStringBounds( text, g );
				x -= (int)( Math.ceil( bounds.getWidth() ) );
			}
			int y = insets.top + fm.getAscent();

			g.drawString( text, x, y );
			gc.popAll();
		}
	}

	private final java.awt.event.AWTEventListener globalListener = new java.awt.event.AWTEventListener() {
		@Override
		public void eventDispatched( java.awt.AWTEvent e ) {
			if( e instanceof java.awt.event.MouseEvent ) {
				java.awt.event.MouseEvent mouseEvent = (java.awt.event.MouseEvent)e;
				if( mouseEvent.getID() == java.awt.event.MouseEvent.MOUSE_PRESSED ) {
					if( mouseEvent.getComponent() == JSubdudeTextField.this ) {
						//pass
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( mouseEvent );
					} else {
						transferFocus();
					}
				}
			}
		}
	};

	private final java.awt.event.FocusListener focusListener = new java.awt.event.FocusListener() {
		@Override
		public void focusGained( java.awt.event.FocusEvent e ) {
			java.awt.Toolkit.getDefaultToolkit().addAWTEventListener( globalListener, java.awt.AWTEvent.MOUSE_EVENT_MASK );
		}

		@Override
		public void focusLost( java.awt.event.FocusEvent e ) {
			java.awt.Toolkit.getDefaultToolkit().removeAWTEventListener( globalListener );
		}
	};

	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		@Override
		public void mouseEntered( java.awt.event.MouseEvent e ) {
			buttonModel.setRollover( true );
			repaint();
		}

		@Override
		public void mouseExited( java.awt.event.MouseEvent e ) {
			buttonModel.setRollover( false );
			repaint();
		}

		@Override
		public void mousePressed( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseReleased( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
	};

	private final javax.swing.Action transferFocusAction = new javax.swing.AbstractAction() {
		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			transferFocus();
		}
	};

	private final javax.swing.event.DocumentListener documentListener = new javax.swing.event.DocumentListener() {
		private void update() {
			java.awt.Container parent = getParent();
			if( parent instanceof javax.swing.JComponent ) {
				( (javax.swing.JComponent)parent ).revalidate();
				parent.repaint();
			}
		}

		@Override
		public void changedUpdate( javax.swing.event.DocumentEvent e ) {
			this.update();
		}

		@Override
		public void insertUpdate( javax.swing.event.DocumentEvent e ) {
			this.update();
		}

		@Override
		public void removeUpdate( javax.swing.event.DocumentEvent e ) {
			this.update();
		}
	};
	private final javax.swing.ButtonModel buttonModel = new javax.swing.DefaultButtonModel();
}
