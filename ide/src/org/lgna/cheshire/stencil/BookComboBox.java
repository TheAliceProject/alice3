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
public class BookComboBox extends org.lgna.croquet.components.JComponent< javax.swing.JComboBox > {
	private BookComboBoxModel comboBoxModel;
	private boolean isLightWeightPopupEnabled;
	public BookComboBox( BookComboBoxModel comboBoxModel, boolean isLightWeightPopupEnabled ) {
		this.comboBoxModel = comboBoxModel;
		this.isLightWeightPopupEnabled = isLightWeightPopupEnabled;
	}
	@Override
	protected javax.swing.JComboBox createAwtComponent() {
		javax.swing.JComboBox rv = new javax.swing.JComboBox( this.comboBoxModel ) {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( super.getPreferredSize(), 320 );
			}
		};
		//todo: find a better way
		//warning monumentally brittle code below
		rv.addPopupMenuListener( new javax.swing.event.PopupMenuListener() {
			public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
				javax.swing.JComboBox box = (javax.swing.JComboBox)e.getSource();
				javax.accessibility.Accessible accessible = box.getUI().getAccessibleChild( box, 0 );
				if( accessible instanceof javax.swing.JPopupMenu ) {
					javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)accessible;
					java.awt.Component component = jPopupMenu.getComponent( 0 );
					if( component instanceof javax.swing.JScrollPane ) {
						javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane)component;
						java.awt.Dimension size = scrollPane.getPreferredSize();

						javax.swing.JViewport viewport = scrollPane.getViewport();
						java.awt.Component view = viewport.getView();
						java.awt.Dimension viewportSize = view.getPreferredSize();
						size.width = viewportSize.width;
						scrollPane.setPreferredSize( size );
						scrollPane.setMaximumSize( size );
					}
				}
			}
			public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
			}
			public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
			}
		} );

		rv.setMaximumRowCount( 20 );
		if( this.isLightWeightPopupEnabled ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.javax.swing.PopupFactoryUtilities.forceHeavyWeightPopups( rv );
		}
		ChapterCellRenderer stepCellRenderer = new ChapterCellRenderer( this.comboBoxModel.getTransactionsModel(), StencilsPresentation.CONTROL_COLOR );
		rv.setRenderer( stepCellRenderer );
		return rv;
	}
};
