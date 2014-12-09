/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public abstract class ToolBarView extends MigPanel {
	//	protected static class ViewConstraitsPair {
	//		private final JComponent<?> view;
	//		private final String constraints;
	//
	//		public ViewConstraitsPair( JComponent<?> view, String constraints ) {
	//			this.view = view;
	//			this.constraints = constraints;
	//		}
	//
	//		public JComponent<?> getView() {
	//			return this.view;
	//		}
	//
	//		public String getConstraints() {
	//			return this.constraints;
	//		}
	//	}

	public ToolBarView( org.lgna.croquet.ToolBarComposite composite ) {
		super( composite, "insets 0 0 2 0, gap 0", "", "" );
		String constraints = "";
		for( org.lgna.croquet.Element element : composite.getSubElements() ) {
			constraints = this.addViewForElement( element, constraints );
		}

		this.setBorder( javax.swing.BorderFactory.createMatteBorder( 0, 0, 1, 0, java.awt.Color.DARK_GRAY ) );
		//this.setBackgroundColor( FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
	}

	protected String addViewForElement( org.lgna.croquet.Element element, String constraints ) {
		String nextConstraints;
		if( element == org.lgna.croquet.GapToolBarSeparator.getInstance() ) {
			nextConstraints = "gap 10";
		} else if( element == org.lgna.croquet.PushToolBarSeparator.getInstance() ) {
			this.addComponent( new Label(), "push" );
			nextConstraints = "";
		} else {
			SwingComponentView<?> component;
			if( element instanceof org.lgna.croquet.Operation ) {
				org.lgna.croquet.Operation operation = (org.lgna.croquet.Operation)element;
				Button button = operation.createButton();
				if( operation.isToolBarTextClobbered() ) {
					button.setToolTipText( operation.getName() );
					button.setClobberText( "" );
				}
				button.tightenUpMargin();
				component = button;
			} else if( element instanceof org.lgna.croquet.SingleSelectListState<?, ?> ) {
				org.lgna.croquet.SingleSelectListState<?, ?> listSelectionState = (org.lgna.croquet.SingleSelectListState<?, ?>)element;
				ComboBox<?> comboBox = listSelectionState.getPrepModel().createComboBoxWithItemCodecListCellRenderer();
				component = comboBox;
			} else if( element instanceof org.lgna.croquet.Composite<?> ) {
				org.lgna.croquet.Composite<?> subComposite = (org.lgna.croquet.Composite<?>)element;
				component = subComposite.getView();
			} else if( element instanceof org.lgna.croquet.PlainStringValue ) {
				org.lgna.croquet.PlainStringValue stringValue = (org.lgna.croquet.PlainStringValue)element;
				component = stringValue.createLabel();
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( element );
				component = null;
			}
			if( component != null ) {
				this.addComponent( component, constraints );
			}
			nextConstraints = "";
		}
		return nextConstraints;
	}

}
