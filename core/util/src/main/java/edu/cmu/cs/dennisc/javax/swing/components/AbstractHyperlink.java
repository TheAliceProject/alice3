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
public abstract class AbstractHyperlink extends javax.swing.JLabel {
	private javax.swing.Action action;
	private java.awt.Color defaultColor = java.awt.Color.BLACK;
	private java.awt.Color armedColor = java.awt.Color.BLUE;

	public AbstractHyperlink() {
		this.addMouseListener( new java.awt.event.MouseListener() {
			@Override
			public void mouseClicked( java.awt.event.MouseEvent e ) {
				assert AbstractHyperlink.this.action != null;
				AbstractHyperlink.this.action.actionPerformed( null );
			}

			@Override
			public void mouseEntered( java.awt.event.MouseEvent e ) {
				AbstractHyperlink.this.setForeground( armedColor );
			}

			@Override
			public void mouseExited( java.awt.event.MouseEvent e ) {
				AbstractHyperlink.this.setForeground( defaultColor );
			}

			@Override
			public void mousePressed( java.awt.event.MouseEvent e ) {
			}

			@Override
			public void mouseReleased( java.awt.event.MouseEvent e ) {
			}
		} );
	}

	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}

	public java.awt.Color getDefaultColor() {
		return this.defaultColor;
	}

	public void setDefaultColor( java.awt.Color defaultColor ) {
		this.defaultColor = defaultColor;
	}

	public java.awt.Color getArmedColor() {
		return this.armedColor;
	}

	public void setArmedColor( java.awt.Color armedColor ) {
		this.armedColor = armedColor;
	}

	public javax.swing.Action getAction() {
		return this.action;
	}

	public void setAction( javax.swing.Action action ) {
		assert action != null;
		this.setContentText( (String)action.getValue( javax.swing.Action.NAME ) );
		this.action = action;
	}

	public void setContentText( String contentText ) {
		this.setText( "<html><u>" + contentText + "</u></html>" );
	}

	//	@Override
	//	protected void paintComponent( java.awt.Graphics g ) {
	//		java.awt.Font font = g.getFont();
	//		java.util.Map< java.awt.font.TextAttribute, Object > attributes = new java.util.HashMap< java.awt.font.TextAttribute, Object >();
	//		attributes.put( java.awt.font.TextAttribute.UNDERLINE, java.awt.font.TextAttribute.UNDERLINE_ON );
	//		g.setFont( font.deriveFont( attributes ) );
	//		super.paintComponent( g );
	//	}
}
