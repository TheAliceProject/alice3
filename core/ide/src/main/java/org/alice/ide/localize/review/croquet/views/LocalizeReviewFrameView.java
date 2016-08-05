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
package org.alice.ide.localize.review.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class LocalizeReviewFrameView extends org.lgna.croquet.views.BorderPanel {
	public LocalizeReviewFrameView( org.alice.ide.localize.review.croquet.LocalizeReviewFrame composite ) {
		super( composite );
		org.lgna.croquet.views.ComboBox<java.util.Locale> comboBox = composite.getLocaleState().getPrepModel().createComboBox();
		comboBox.setRenderer( new javax.swing.DefaultListCellRenderer() {
			@Override
			public java.awt.Component getListCellRendererComponent( javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
				super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
				java.util.Locale locale = (java.util.Locale)value;
				this.setText( locale.getDisplayName( locale ) + " (" + locale.getDisplayName( java.util.Locale.US ) + ")" );
				return this;
			}
		} );

		this.addPageStartComponent( new org.lgna.croquet.views.LineAxisPanel(
				composite.getLocaleState().getSidekickLabel().createLabel(),
				comboBox,
				composite.getIsIncludingUntranslatedState().createCheckBox()
				) );
		jTable = new javax.swing.JTable( composite.getTableModel() );
		javax.swing.table.TableColumnModel tableColumnModel = jTable.getColumnModel();
		javax.swing.table.TableColumn columnIndex = tableColumnModel.getColumn( 0 );
		columnIndex.setHeaderValue( "index" );
		columnIndex.setMaxWidth( 64 );
		javax.swing.table.TableColumn columnContext = tableColumnModel.getColumn( 1 );
		columnContext.setHeaderValue( "Context" );
		javax.swing.table.TableColumn columnOriginal = tableColumnModel.getColumn( 2 );
		columnOriginal.setHeaderValue( "Original text" );
		javax.swing.table.TableColumn columnTranslated = tableColumnModel.getColumn( 3 );
		columnTranslated.setHeaderValue( "Translated text" );

		javax.swing.table.TableColumn columnReview = tableColumnModel.getColumn( 4 );
		columnReview.setHeaderValue( "Review" );

		final javax.swing.JLabel label = new javax.swing.JLabel( "<html><a href=\"\">review</a> [web]</html>" );
		columnReview.setCellRenderer( new javax.swing.table.TableCellRenderer() {
			@Override
			public java.awt.Component getTableCellRendererComponent( javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
				return label;
			}
		} );
		javax.swing.JScrollPane jScrollPane = new javax.swing.JScrollPane( jTable );
		this.getAwtComponent().add( jScrollPane, java.awt.BorderLayout.CENTER );
	}

	@Override
	public void handleCompositePreActivation() {
		this.jTable.addMouseListener( this.mouseListener );
		super.handleCompositePreActivation();
	}

	@Override
	public void handleCompositePostDeactivation() {
		super.handleCompositePostDeactivation();
		this.jTable.removeMouseListener( this.mouseListener );
	}

	private final javax.swing.JTable jTable;
	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		@Override
		public void mousePressed( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseReleased( java.awt.event.MouseEvent e ) {
			java.awt.Point pt = e.getPoint();
			int columnIndex = jTable.columnAtPoint( pt );
			if( columnIndex == 4 ) {
				int rowIndex = jTable.rowAtPoint( pt );
				if( rowIndex >= 0 ) {
					org.alice.ide.localize.review.croquet.LocalizeReviewFrame composite = (org.alice.ide.localize.review.croquet.LocalizeReviewFrame)getComposite();
					java.net.URI uri = composite.createUri( rowIndex );
					try {
						edu.cmu.cs.dennisc.java.awt.DesktopUtilities.browse( uri );
					} catch( Exception exc ) {
						throw new RuntimeException( exc );
					}
				}
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( rowIndex, columnIndex, jTable.getModel().getValueAt( rowIndex, columnIndex ) );
			}
		}

		@Override
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseExited( java.awt.event.MouseEvent e ) {
		}
	};
}
