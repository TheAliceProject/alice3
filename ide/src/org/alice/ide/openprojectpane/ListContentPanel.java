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

package org.alice.ide.openprojectpane;

/**
 * @author Dennis Cosgrove
 */
public abstract class ListContentPanel extends TabContentPanel {
	private edu.cmu.cs.dennisc.croquet.ListSelectionState<java.net.URI> uriSelection = new edu.cmu.cs.dennisc.croquet.ListSelectionState<java.net.URI>( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "68a17b6d-353d-4473-abd3-1c78ff88e1cd" ), new edu.cmu.cs.dennisc.croquet.Codec< java.net.URI >() {
		public StringBuilder appendRepresentation( StringBuilder rv, java.net.URI value, java.util.Locale locale ) {
			rv.append( value );
			return rv;
		}
		public java.net.URI decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			throw new RuntimeException( "todo" );
		}
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, java.net.URI uri ) {
			throw new RuntimeException( "todo" );
		}
	} );
//	register( new edu.cmu.cs.dennisc.croquet.List<java.net.URI>() {
//		@Override
//		protected javax.swing.JList createAwtComponent() {
//			javax.swing.JList rv = new javax.swing.JList() {
//				@Override
//				public void paint(java.awt.Graphics g) {
//					super.paint( g );
//					if( this.getModel().getSize() > 0 ) {
//						//pass
//					} else {
//						java.awt.Font font = this.getFont();
//						font = font.deriveFont( java.awt.Font.ITALIC );
//						g.setFont( font );
//						edu.cmu.cs.dennisc.awt.GraphicsUtilities.drawCenteredText( g, ListContentPanel.this.getTextForZeroProjects(), this.getSize() );
//					}
//				}
//			};
//			//rv.setVisibleRowCount( -1 );
//			return rv;
//		}
//	} );

//	private edu.cmu.cs.dennisc.croquet.List<java.net.URI> list = new edu.cmu.cs.dennisc.croquet.List<java.net.URI>();
	
	public ListContentPanel() {
		this.refresh();
		final edu.cmu.cs.dennisc.croquet.List<java.net.URI> list = this.uriSelection.createList();
		list.setBackgroundColor( null );
		list.setCellRenderer( new ProjectSnapshotListCellRenderer() );
		list.setLayoutOrientation( edu.cmu.cs.dennisc.croquet.List.LayoutOrientation.HORIZONTAL_WRAP );
		list.setVisibleRowCount( -1 );
		
		edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter mouseAdapter = new edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter() {
			@Override
			protected void mouseQuoteClickedUnquote(java.awt.event.MouseEvent e, int quoteClickCountUnquote ) {
				if( quoteClickCountUnquote == 2 ) {
					edu.cmu.cs.dennisc.croquet.Button defaultButton = list.getRoot().getDefaultButton();
					if( defaultButton != null ) {
						defaultButton.doClick();
					}
				}
			}
		};
		list.addMouseListener( mouseAdapter );
		list.addMouseMotionListener( mouseAdapter );
		list.addKeyListener( new java.awt.event.KeyListener() {
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
//		list.addListSelectionListener( new javax.swing.event.ListSelectionListener() {
//			public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
//				if( e.getValueIsAdjusting() ) {
//					//pass
//				} else {
//					ListContentPanel.this.updateOKButton();
//				}
//			}
//		} );
		this.addComponent(  list, Constraint.CENTER );
	}
	protected abstract String getTextForZeroProjects();
	
	protected abstract java.net.URI[] getURIs();
	public void refresh() {
		this.uriSelection.setListData( -1, this.getURIs() );
	}
	@Override
	public java.net.URI getSelectedURI() {
		return this.uriSelection.getSelectedItem();
	}
}

