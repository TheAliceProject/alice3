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

import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;

import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Rectangle2D;

/**
 * @author Dennis Cosgrove
 */

public abstract class JExpandPane extends AbstractButton {
	class ToggleButton extends JToggleButton {
		@Override
		public Dimension getPreferredSize() {
			Dimension rv = super.getPreferredSize();
			Font font = this.getFont();
			if( font != null ) {
				Graphics g = GraphicsUtilities.getGraphics();
				FontMetrics fm = g.getFontMetrics( font );
				for( String s : new String[] { JExpandPane.this.getExpandedButtonText(), JExpandPane.this.getCollapsedButtonText() } ) {
					Rectangle2D bounds = fm.getStringBounds( s, g );
					rv.width = Math.max( rv.width, (int)bounds.getWidth() + 16 );
					rv.height = Math.max( rv.height, (int)bounds.getHeight() + 4 );
				}
			}
			return rv;
		}

		@Override
		protected void paintComponent( Graphics g ) {
			super.paintComponent( g );
			String text;
			if( this.isSelected() ) {
				text = JExpandPane.this.getExpandedButtonText();
			} else {
				text = JExpandPane.this.getCollapsedButtonText();
			}
			GraphicsUtilities.drawCenteredText( g, text, this.getSize() );
		}
	}

	private JLabel label = this.createLabel();
	private ToggleButton toggle = new ToggleButton();
	private JComponent center;

	public JExpandPane() {
		this.setModel( new DefaultButtonModel() );
		this.toggle.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				JExpandPane.this.setSelected( toggle.isSelected() );
			}
		} );
		this.addItemListener( new ItemListener() {
			@Override
			public void itemStateChanged( ItemEvent e ) {
				JExpandPane.this.handleToggled( e );
			}
		} );
		this.label.setText( this.getCollapsedLabelText() );
		//this.setBorder( javax.swing.BorderFactory.createLineBorder( java.awt.Color.GRAY ) );
		this.setLayout( new BorderLayout() );
		this.add( this.createTopPane(), BorderLayout.NORTH );
		this.center = this.createCenterPane();
	}

	//todo: rename
	public JComponent getCenterComponent() {
		return this.center;
	}

	protected JLabel createLabel() {
		return new JLabel();
	}

	protected abstract String getExpandedLabelText();

	protected abstract String getCollapsedLabelText();

	protected String getExpandedButtonText() {
		return "V";
	}

	protected String getCollapsedButtonText() {
		return ">>>";
	}

	private void handleToggled( ItemEvent e ) {
		if( e.getStateChange() == ItemEvent.SELECTED ) {
			this.add( this.center, BorderLayout.CENTER );
			this.label.setText( this.getExpandedLabelText() );
		} else {
			this.remove( this.center );
			this.label.setText( this.getCollapsedLabelText() );
		}
		//this.center.revalidate();
		this.revalidate();
		this.repaint();
		Component root = SwingUtilities.getRoot( this );
		if( root instanceof Window ) {
			Window window = (Window)root;
			window.pack();
		}
	}

	protected JComponent createTopPane() {
		JPanel rv = new JPanel();
		rv.setLayout( new BorderLayout() );
		rv.add( this.label, BorderLayout.CENTER );
		rv.add( this.toggle, BorderLayout.EAST );
		return rv;
	}

	protected abstract JComponent createCenterPane();
}
