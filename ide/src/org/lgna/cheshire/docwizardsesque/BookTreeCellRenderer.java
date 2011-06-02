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

package org.lgna.cheshire.docwizardsesque;

/**
 * @author Dennis Cosgrove
 */
public class BookTreeCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.TreeCellRenderer< Object > {
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
	private static javax.swing.Icon STEP_ICON = new javax.swing.Icon() {
		public int getIconWidth() {
			return 32;
		}
		public int getIconHeight() {
			return 0;
		}
		public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		}
	};
	private static final javax.swing.Icon CURRENT_ICON = new javax.swing.ImageIcon( org.lgna.cheshire.Presentation.class.getResource( "images/current.png" ) );
	private static final javax.swing.Icon PERFECT_MATCH_ICON = new javax.swing.ImageIcon( org.lgna.cheshire.Presentation.class.getResource( "images/perfectMatch.png" ) );
	private static final javax.swing.Icon SHOULD_BE_FINE_ICON = new javax.swing.ImageIcon( org.lgna.cheshire.Presentation.class.getResource( "images/shouldBeFine.png" ) );
	private static final javax.swing.Icon DEVIATION_ICON = new javax.swing.ImageIcon( org.lgna.cheshire.Presentation.class.getResource( "images/deviation.png" ) );
	private static final java.awt.Font TRANSACTION_FONT = new java.awt.Font( null, java.awt.Font.BOLD, 16 ); 
	private static final java.awt.Font STEP_FONT = new java.awt.Font( null, java.awt.Font.PLAIN, 12 ); 
	@Override
	protected javax.swing.JLabel updateListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
		if( value instanceof org.lgna.cheshire.TransactionChapter ) {
			org.lgna.cheshire.TransactionChapter transactionChapter = (org.lgna.cheshire.TransactionChapter)value;
			rv.setText( "<html><strong>" + transactionChapter.getTitle() + "</strong></html>");
			
			javax.swing.Icon icon;
			if( isSelected ) { //index == this.book.getSelectedIndex() ) {
				icon = CURRENT_ICON;
			} else {
				icon = BLANK_ICON;
			}
			org.lgna.croquet.edits.ReplacementAcceptability replacementAcceptability = transactionChapter.getReplacementAcceptability();
			if( replacementAcceptability != null ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( replacementAcceptability );
				if( replacementAcceptability == org.lgna.croquet.edits.ReplacementAcceptability.PERFECT_MATCH || replacementAcceptability == org.lgna.croquet.edits.ReplacementAcceptability.TO_BE_HONEST_I_DIDNT_EVEN_REALLY_CHECK ) {
					icon = PERFECT_MATCH_ICON;
				} else {
					if( replacementAcceptability.isDeviation() ) {
						if( replacementAcceptability.getDeviationSeverity() == org.lgna.croquet.edits.ReplacementAcceptability.DeviationSeverity.SHOULD_BE_FINE ) {
							icon = SHOULD_BE_FINE_ICON;
						} else {
							icon = DEVIATION_ICON;
						}
					} else {
						icon = null;
					}
				}
			}
			rv.setIcon( icon );
			rv.setFont( TRANSACTION_FONT );
		} else if( value instanceof org.lgna.cheshire.MessageChapter ) {
			org.lgna.cheshire.MessageChapter messageChapter = (org.lgna.cheshire.MessageChapter)value;
			rv.setText( "<html>" + messageChapter.getTitle() + "</html>");
			rv.setIcon( STEP_ICON );
		} else if( value instanceof org.lgna.croquet.steps.Step< ? > ) {
			org.lgna.croquet.steps.Step< ? > step = (org.lgna.croquet.steps.Step< ? >)value;
			org.lgna.croquet.edits.Edit< ? > edit = step.getParent().getEdit();
			rv.setText( "<html>" + step.getTutorialNoteText( edit, null ) + "</html>" );
			rv.setFont( STEP_FONT );
			rv.setIcon( STEP_ICON );
		}
		return rv;
	}
}
