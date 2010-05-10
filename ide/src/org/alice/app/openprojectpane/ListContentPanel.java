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

package org.alice.app.openprojectpane;

/**
 * @author Dennis Cosgrove
 */
public abstract class ListContentPanel extends TabContentPanel {
	private javax.swing.JList list = new javax.swing.JList() {
		@Override
		public void paint(java.awt.Graphics g) {
			super.paint( g );
			if( this.getModel().getSize() > 0 ) {
				//pass
			} else {
				java.awt.Font font = this.getFont();
				font = font.deriveFont( java.awt.Font.ITALIC );
				g.setFont( font );
				edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.drawCenteredText( g, ListContentPanel.this.getTextForZeroProjects(), this.getSize() );
			}
		}
		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.java.awt.DimensionUtilties.constrainToMinimumSize( super.getPreferredSize(), 400, 300 );
		}
//		@Override
//		public java.awt.Dimension getMaximumSize() {
//			return this.getPreferredSize();
//		}
	};
	
	private edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter mouseAdapter = new edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter() {
		@Override
		protected void mouseQuoteClickedUnquote(java.awt.event.MouseEvent e, int quoteClickCountUnquote ) {
			if( quoteClickCountUnquote == 2 ) {
				javax.swing.JButton defaultButton = ListContentPanel.this.list.getRootPane().getDefaultButton();
				if( defaultButton.isEnabled() ) {
					defaultButton.doClick();
				}
			}
		}
	};
	
	public ListContentPanel() {
		this.refresh();
		this.list.setOpaque( false );
		this.list.setCellRenderer( new ProjectSnapshotListCellRenderer() );
		this.list.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
		this.list.setVisibleRowCount( -1 );
		
		this.list.addMouseListener( this.mouseAdapter );
		this.list.addMouseMotionListener( this.mouseAdapter );
		this.list.addKeyListener( new java.awt.event.KeyListener() {
			public void keyPressed( java.awt.event.KeyEvent e ) {
				if( e.getKeyCode() == java.awt.event.KeyEvent.VK_F5 ) {
					ListContentPanel.this.refresh();
				}
			}
			public void keyReleased( java.awt.event.KeyEvent e ) {
			}
			public void keyTyped( java.awt.event.KeyEvent e ) {
			}
		} );
		this.list.addListSelectionListener( new javax.swing.event.ListSelectionListener() {
			public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
				if( e.getValueIsAdjusting() ) {
					//pass
				} else {
					ListContentPanel.this.updateOKButton();
				}
			}
		} );
		this.addComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter( this.list ), Constraint.CENTER );
	}
	protected abstract String getTextForZeroProjects();
	
	protected abstract java.net.URI[] getURIs();
	public void refresh() {
		this.list.setListData( this.getURIs() );
	}
	@Override
	public java.net.URI getSelectedURI() {
		Object selectedValue = this.list.getSelectedValue();
		if( selectedValue instanceof java.net.URI ) {
			java.net.URI uri = (java.net.URI)selectedValue;
			return uri;
		} else {
			return null;
		}
	}
}

