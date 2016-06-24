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

public abstract class JExpandPane extends javax.swing.AbstractButton {
	class ToggleButton extends javax.swing.JToggleButton {
		@Override
		public java.awt.Dimension getPreferredSize() {
			java.awt.Dimension rv = super.getPreferredSize();
			java.awt.Font font = this.getFont();
			if( font != null ) {
				java.awt.Graphics g = edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.getGraphics();
				java.awt.FontMetrics fm = g.getFontMetrics( font );
				for( String s : new String[] { JExpandPane.this.getExpandedButtonText(), JExpandPane.this.getCollapsedButtonText() } ) {
					java.awt.geom.Rectangle2D bounds = fm.getStringBounds( s, g );
					rv.width = Math.max( rv.width, (int)bounds.getWidth() + 16 );
					rv.height = Math.max( rv.height, (int)bounds.getHeight() + 4 );
				}
			}
			return rv;
		}

		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			super.paintComponent( g );
			String text;
			if( this.isSelected() ) {
				text = JExpandPane.this.getExpandedButtonText();
			} else {
				text = JExpandPane.this.getCollapsedButtonText();
			}
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g, text, this.getSize() );
		}
	}

	private javax.swing.JLabel label = this.createLabel();
	private ToggleButton toggle = new ToggleButton();
	private javax.swing.JComponent center;

	public JExpandPane() {
		this.setModel( new javax.swing.DefaultButtonModel() );
		this.toggle.addActionListener( new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				JExpandPane.this.setSelected( toggle.isSelected() );
			}
		} );
		this.addItemListener( new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged( java.awt.event.ItemEvent e ) {
				JExpandPane.this.handleToggled( e );
			}
		} );
		this.label.setText( this.getCollapsedLabelText() );
		//this.setBorder( javax.swing.BorderFactory.createLineBorder( java.awt.Color.GRAY ) );
		this.setLayout( new java.awt.BorderLayout() );
		this.add( this.createTopPane(), java.awt.BorderLayout.NORTH );
		this.center = this.createCenterPane();
	}

	//todo: rename
	public javax.swing.JComponent getCenterComponent() {
		return this.center;
	}

	protected javax.swing.JLabel createLabel() {
		return new javax.swing.JLabel();
	}

	protected abstract String getExpandedLabelText();

	protected abstract String getCollapsedLabelText();

	protected String getExpandedButtonText() {
		return "V";
	}

	protected String getCollapsedButtonText() {
		return ">>>";
	}

	private void handleToggled( java.awt.event.ItemEvent e ) {
		if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
			this.add( this.center, java.awt.BorderLayout.CENTER );
			this.label.setText( this.getExpandedLabelText() );
		} else {
			this.remove( this.center );
			this.label.setText( this.getCollapsedLabelText() );
		}
		//this.center.revalidate();
		this.revalidate();
		this.repaint();
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( this );
		if( root instanceof java.awt.Window ) {
			java.awt.Window window = (java.awt.Window)root;
			window.pack();
		}
	}

	protected javax.swing.JComponent createTopPane() {
		javax.swing.JPanel rv = new javax.swing.JPanel();
		rv.setLayout( new java.awt.BorderLayout() );
		rv.add( this.label, java.awt.BorderLayout.CENTER );
		rv.add( this.toggle, java.awt.BorderLayout.EAST );
		return rv;
	}

	protected abstract javax.swing.JComponent createCenterPane();
}
