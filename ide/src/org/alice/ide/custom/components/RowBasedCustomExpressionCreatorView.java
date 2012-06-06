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

package org.alice.ide.custom.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class RowBasedCustomExpressionCreatorView extends CustomExpressionCreatorView {
	protected static class Row {
		private final org.lgna.croquet.StringValue labelStringValue;
		private final org.lgna.croquet.components.JComponent< ? > component;
		private final org.lgna.croquet.components.VerticalAlignment labelVerticalAlignment;
		public Row( org.lgna.croquet.StringValue labelStringValue, org.lgna.croquet.components.JComponent< ? > component, org.lgna.croquet.components.VerticalAlignment labelVerticalAlignment ) {
			this.labelStringValue = labelStringValue;
			this.component = component;
			this.labelVerticalAlignment = labelVerticalAlignment;
		}
		public Row( org.lgna.croquet.StringValue labelStringValue, org.lgna.croquet.components.JComponent< ? > component ) {
			this( labelStringValue, component, org.lgna.croquet.components.VerticalAlignment.CENTER );
		}
		private org.lgna.croquet.components.JComponent<?> createImmutableTextField() {
			if( this.labelStringValue != null ) {
				org.lgna.croquet.components.ImmutableTextField textField = this.labelStringValue.createImmutableTextField();
				textField.setHorizontalAlignment( org.lgna.croquet.components.HorizontalAlignment.TRAILING );
				if( this.labelVerticalAlignment == org.lgna.croquet.components.VerticalAlignment.CENTER ) {
					return textField;
				} else {
					Constraint constraint;
					if( this.labelVerticalAlignment == org.lgna.croquet.components.VerticalAlignment.TOP ) {
						constraint = Constraint.PAGE_START;
					} else {
						constraint = Constraint.PAGE_END;
					}
					org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel();
					rv.addComponent( textField, constraint );
					rv.setMaximumSizeClampedToPreferredSize( true );
					return rv;
				}
			} else {
				return new org.lgna.croquet.components.Label();
			}
		}
		public org.lgna.croquet.components.JComponent< ? >[] createComponentArray() {
			org.lgna.croquet.components.JComponent< ? >[] rv = { 
				this.createImmutableTextField(),
				new org.lgna.croquet.components.LineAxisPanel( 
						this.component,
						org.lgna.croquet.components.BoxUtilities.createHorizontalGlue()
				)
			};
			return rv;
		}
	}
	public RowBasedCustomExpressionCreatorView( org.alice.ide.custom.CustomExpressionCreatorComposite<?> composite ) {
		super( composite );
	}
	@Override
	public org.alice.ide.custom.CustomExpressionCreatorComposite<?> getComposite() {
		return (org.alice.ide.custom.CustomExpressionCreatorComposite<?>)super.getComposite();
	}
	protected abstract void appendRows( java.util.List< Row > rows );
	@Override
	public org.lgna.croquet.components.RowsSpringPanel createMainComponent() {
		org.lgna.croquet.components.RowsSpringPanel rowsSpringPanel = new org.lgna.croquet.components.RowsSpringPanel() {
			@Override
			protected java.util.List<org.lgna.croquet.components.Component<?>[]> updateComponentRows(java.util.List<org.lgna.croquet.components.Component<?>[]> rv) {
				java.util.List< Row > rows = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				RowBasedCustomExpressionCreatorView.this.appendRows( rows );
				for( Row row : rows ) {
					rv.add( row.createComponentArray() ); 
				}
				return rv;
			}
		};
		return rowsSpringPanel;
	}
}
