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
public class PreviewComponent extends org.lgna.croquet.components.JComponent< javax.swing.JComponent > {
	private static final java.awt.Stroke STROKE = new java.awt.BasicStroke( 3.0f );
	@Override
	protected javax.swing.JComponent createAwtComponent() {
		return new javax.swing.JComponent() {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				super.paintComponent( g );
				org.lgna.cheshire.Presentation presentation = DocWizardsesquePresentation.getInstance();
				org.lgna.cheshire.Chapter chapter = presentation.getBook().getSelectedChapter();
				if( chapter instanceof org.lgna.cheshire.TransactionChapter ) {
					org.lgna.cheshire.TransactionChapter transactionChapter = (org.lgna.cheshire.TransactionChapter)chapter;
					org.lgna.croquet.history.Step< ? > step0 = transactionChapter.getTransaction().getChildStepAt( 0 );
					if( step0 != null ) {
						org.lgna.croquet.Model model = step0.getModel();
//						if( step0 instanceof org.lgna.croquet.steps.StandardPopupOperationPrepStep ) {
//							org.lgna.croquet.steps.StandardPopupOperationPrepStep standardPopupOperationPrepStep = (org.lgna.croquet.steps.StandardPopupOperationPrepStep)step0;
//							model = standardPopupOperationPrepStep.getStandardPopupOperation();
//						}
						if( model != null ) {
							org.lgna.croquet.components.Component component = org.lgna.croquet.ComponentManager.getFirstComponent( model );
							if( component != null ) {
								if( component instanceof org.lgna.croquet.components.FolderTabbedPane ) {
									org.lgna.croquet.components.FolderTabbedPane folderTabbedPane = (org.lgna.croquet.components.FolderTabbedPane)component;
									component = ((org.lgna.croquet.components.JComponent)((org.lgna.croquet.components.JComponent)folderTabbedPane.getComponent( 0 )).getComponent( 0 )).getComponent( 0 );
								}
								java.awt.Dimension size = component.getAwtComponent().getSize();
								java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
								java.awt.Paint paint = g2.getPaint();
								java.awt.Stroke stroke = g2.getStroke();
								final int INSET = 0;
								int x = INSET - component.getX();
								int y = INSET - component.getY();
								g2.translate( +x, +y );
								try {
									component.getAwtComponent().getParent().printAll( g );
									g2.setPaint( java.awt.Color.RED );
									g2.setStroke( STROKE );
									g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
								} finally {
									g2.translate( -x, -y );
									g2.drawOval( 0, 0, size.width+INSET*2, size.height+INSET*2 );
									g2.setPaint( paint );
									g2.setStroke( stroke );
								}
							}
						}
					}
				}
			}
		};
	}
}
