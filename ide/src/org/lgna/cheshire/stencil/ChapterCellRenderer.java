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
package org.lgna.cheshire.stencil;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/class ChapterCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< org.lgna.cheshire.Chapter > {
	private static javax.swing.Icon BLANK_ICON = new javax.swing.Icon() {
		public int getIconWidth() {
			return 32;
		}
		public int getIconHeight() {
			return 32;
		}
		public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		}
	};
	private static final javax.swing.Icon CURRENT_ICON = new javax.swing.ImageIcon( org.lgna.cheshire.Presentation.class.getResource( "images/current.png" ) );
	private static final javax.swing.Icon PERFECT_MATCH_ICON = new javax.swing.ImageIcon( org.lgna.cheshire.Presentation.class.getResource( "images/perfectMatch.png" ) );
	private static final javax.swing.Icon SHOULD_BE_FINE_ICON = new javax.swing.ImageIcon( org.lgna.cheshire.Presentation.class.getResource( "images/shouldBeFine.png" ) );
	private static final javax.swing.Icon DEVIATION_ICON = new javax.swing.ImageIcon( org.lgna.cheshire.Presentation.class.getResource( "images/deviation.png" ) );
	private org.lgna.cheshire.Book book;
	private java.awt.Color background;
	private java.awt.Color disabledBackground;
	private java.awt.Color disabledForeground;

	public ChapterCellRenderer( org.lgna.cheshire.Book book, java.awt.Color background ) {
		this.book = book;
		this.background = background;
		this.disabledBackground = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( this.background, 1.0, 0.5, 1.25 );
		this.disabledForeground = java.awt.Color.LIGHT_GRAY;
		this.setHorizontalTextPosition( javax.swing.SwingConstants.TRAILING );
	}
	@Override
	protected javax.swing.JLabel getListCellRendererComponent(javax.swing.JLabel rv, javax.swing.JList list, org.lgna.cheshire.Chapter value, int index, boolean isSelected, boolean cellHasFocus) {
		assert list != null;
		StringBuilder sb = new StringBuilder();
		org.lgna.croquet.ReplacementAcceptability replacementAcceptability;
		if( value != null) {
			int i;
			if (index >= 0) {
				i = index;
			} else {
				i = this.book.getSelectedIndex();
				// if( i >= 0 ) {
				// assert value == stepsModel.getElementAt( i ) :
				// stepsModel.getElementAt( i );
				// }
			}
			sb.append("<html>");
			//sb.append("<strong>");
			//sb.append("Chapter ");
			sb.append(i + 1);
			sb.append(":    ");
			//sb.append("</strong>");
			sb.append(value.getTitle());
			sb.append("</html>");
			replacementAcceptability = value.getReplacementAcceptability();
		} else {
			sb.append( "null" );
			replacementAcceptability = null;
		}
		rv.setText(sb.toString());

		String toolTipText = null;
		javax.swing.Icon icon;
		if( index != -1 ) {
			if( index <= this.book.getSelectedIndex() ) {
				if( replacementAcceptability != null ) {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( replacementAcceptability );
					if( replacementAcceptability == org.lgna.croquet.ReplacementAcceptability.PERFECT_MATCH || replacementAcceptability == org.lgna.croquet.ReplacementAcceptability.TO_BE_HONEST_I_DIDNT_EVEN_REALLY_CHECK ) {
						icon = PERFECT_MATCH_ICON;
					} else {
						if( replacementAcceptability.isDeviation() ) {
							if( replacementAcceptability.getDeviationSeverity() == org.lgna.croquet.ReplacementAcceptability.DeviationSeverity.SHOULD_BE_FINE ) {
								icon = SHOULD_BE_FINE_ICON;
							} else {
								icon = DEVIATION_ICON;
							}
							if( isSelected ) {
								StringBuilder sbToolTip = new StringBuilder();
								sbToolTip.append( replacementAcceptability.getDeviationSeverity().getRepr( StencilsPresentation.getInstance().getUserInformation() ) );
								sbToolTip.append( ": " );
								sbToolTip.append( replacementAcceptability.getDeviationDescription() );
								toolTipText = sbToolTip.toString();
							}
						} else {
							icon = BLANK_ICON;
						}
					}
				} else {
					if( index == this.book.getSelectedIndex() ) {
						icon = CURRENT_ICON;
					} else {
						icon = BLANK_ICON;
					}
				}
			} else {
				icon = BLANK_ICON;
			}
		} else {
			icon = CURRENT_ICON;
		}

		rv.setToolTipText( toolTipText );
		rv.setIcon( icon );
		
		if( this.book.isIndexAccessible( index ) ) {
			if( isSelected || cellHasFocus ) {
				//pass
			} else {
				rv.setBackground( this.background );
			}
		} else {
			rv.setBackground( this.disabledBackground );
			rv.setForeground( this.disabledForeground );
		}
		return rv;
	}
}
