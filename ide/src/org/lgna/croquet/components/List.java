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

package org.lgna.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public class List<T> extends ItemSelectable<javax.swing.JList, T, org.lgna.croquet.ListSelectionState<T>> {
	public enum LayoutOrientation {
		VERTICAL( javax.swing.JList.VERTICAL ),
		VERTICAL_WRAP( javax.swing.JList.VERTICAL_WRAP ),
		HORIZONTAL_WRAP( javax.swing.JList.HORIZONTAL_WRAP );
		private int internal;

		private LayoutOrientation( int internal ) {
			this.internal = internal;
		}
	}

	private static class DefaultEmptyListPainter<T> implements javax.swing.Painter<List<T>> {
		private static final java.util.Map<java.awt.font.TextAttribute, Object> mapDeriveFont;
		static {
			mapDeriveFont = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
			mapDeriveFont.put( java.awt.font.TextAttribute.POSTURE, java.awt.font.TextAttribute.POSTURE_OBLIQUE );
			mapDeriveFont.put( java.awt.font.TextAttribute.WEIGHT, java.awt.font.TextAttribute.WEIGHT_LIGHT );
		}

		public void paint( java.awt.Graphics2D g2, List<T> listView, int width, int height ) {
			org.lgna.croquet.ListSelectionState<T> state = listView.getModel();
			org.lgna.croquet.PlainStringValue emptyConditionText = state.getEmptyConditionText();
			String text = emptyConditionText.getText();
			if( ( text != null ) && ( text.length() > 0 ) ) {
				edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setRenderingHint( g2, java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
				g2.setPaint( java.awt.Color.DARK_GRAY );
				g2.setFont( g2.getFont().deriveFont( mapDeriveFont ) );
				g2.drawString( text, 0, g2.getFontMetrics().getAscent() );
			}
		}
	}

	private javax.swing.Painter<List<T>> emptyConditionPainter = new DefaultEmptyListPainter<T>();

	public List( org.lgna.croquet.ListSelectionState<T> model ) {
		super( model );
		this.getAwtComponent().setModel( model.getSwingModel().getComboBoxModel() );
		this.getAwtComponent().setSelectionModel( model.getSwingModel().getListSelectionModel() );
	}

	private final edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter mouseAdapter = new edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter() {
		@Override
		protected void mouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote ) {
			if( quoteClickCountUnquote == 2 ) {
				AbstractWindow<?> window = List.this.getRoot();
				if( window != null ) {
					org.lgna.croquet.components.Button defaultButton = window.getDefaultButton();
					if( defaultButton != null ) {
						defaultButton.doClick();
					}
				}
			}
		}
	};

	public void enableClickingDefaultButtonOnDoubleClick() {
		this.addMouseListener( this.mouseAdapter );
		this.addMouseMotionListener( this.mouseAdapter );
	}

	public void disableClickingDefaultButtonOnDoubleClick() {
		this.removeMouseMotionListener( this.mouseAdapter );
		this.removeMouseListener( this.mouseAdapter );
	}

	protected class JDefaultList extends javax.swing.JList {
		@Override
		public java.awt.Dimension getPreferredSize() {
			return constrainPreferredSizeIfNecessary( super.getPreferredSize() );
		}

		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			super.paintComponent( g );
			javax.swing.ListModel model = this.getModel();
			if( model.getSize() == 0 ) {
				if( emptyConditionPainter != null ) {
					emptyConditionPainter.paint( (java.awt.Graphics2D)g, List.this, this.getWidth(), this.getHeight() );
				}
			}
		}
	}

	@Override
	protected javax.swing.JList createAwtComponent() {
		return new JDefaultList();
	}

	public javax.swing.Painter<List<T>> getEmptyConditionPainter() {
		return this.emptyConditionPainter;
	}

	public void setEmptyConditionPainter( javax.swing.Painter<List<T>> emptyConditionPainter ) {
		this.emptyConditionPainter = emptyConditionPainter;
	}

	@Override
	public TrackableShape getTrackableShapeFor( T item ) {
		//todo
		return this;
	}

	public javax.swing.ListCellRenderer getCellRenderer() {
		return this.getAwtComponent().getCellRenderer();
	}

	public void setCellRenderer( javax.swing.ListCellRenderer listCellRenderer ) {
		this.getAwtComponent().setCellRenderer( listCellRenderer );
	}

	public int getVisibleRowCount() {
		return this.getAwtComponent().getVisibleRowCount();
	}

	public void setVisibleRowCount( int visibleRowCount ) {
		this.getAwtComponent().setVisibleRowCount( visibleRowCount );
	}

	public void setLayoutOrientation( LayoutOrientation layoutOrientation ) {
		this.getAwtComponent().setLayoutOrientation( layoutOrientation.internal );
	}
}
