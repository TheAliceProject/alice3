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
package org.alice.stageide.sceneeditor;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/ class FieldTile extends edu.cmu.cs.dennisc.croquet.BooleanStateButton<javax.swing.AbstractButton>{
	private edu.cmu.cs.dennisc.alice.ast.Accessible accessible;
//	private class NamePropertyAdapter implements edu.cmu.cs.dennisc.property.event.PropertyListener {
//		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
//		}
//		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
//		}
//	}
//	public static FieldTile createInstance( edu.cmu.cs.dennisc.alice.ast.AbstractField accessible ) {
//		FieldSelectedState state = FieldSelectedState.getInstance(accessible);
//		return state.register( new FieldTile( accessible ) );
//	}
	public FieldTile( edu.cmu.cs.dennisc.alice.ast.Accessible accessible ) {
		super( new edu.cmu.cs.dennisc.croquet.BooleanState( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "d3e50396-64c2-4a7d-b255-067aa212bca7" ), false ) );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: FIELD_TILE_BOOLEAN_STATE" );
		assert accessible != null;
		this.accessible = accessible;
		//this.setOpaque( false );
		
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0,0,0,4 ) );
		this.setPopupMenuOperation( new edu.cmu.cs.dennisc.croquet.AbstractPopupMenuOperation( java.util.UUID.fromString( "8e3989b2-34d6-44cf-998c-dda26662b3a0" ) ) {
			@Override
			protected void handlePopupMenuCreation( edu.cmu.cs.dennisc.croquet.PopupMenu popupMenu ) {
				super.handlePopupMenuCreation( popupMenu );
				edu.cmu.cs.dennisc.croquet.Application.addMenuElements( popupMenu, FieldTile.this.createPopupOperations() );
			}
		});
		this.updateLabel();
	}

	private edu.cmu.cs.dennisc.property.event.PropertyListener namePropertyListener = new edu.cmu.cs.dennisc.property.event.PropertyListener(){
		public void propertyChanged(edu.cmu.cs.dennisc.property.event.PropertyEvent e) {
			FieldTile.this.updateLabel();
		}
		public void propertyChanging(edu.cmu.cs.dennisc.property.event.PropertyEvent e) {
		}
	};
	
	private edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver valueObserver = new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
		public void changing(boolean nextValue) {
		}
		public void changed(boolean nextValue) {
			FieldTile.this.updateLabel();
		}
	};
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo(parent);
		org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState.getInstance().addAndInvokeValueObserver( this.valueObserver );
		edu.cmu.cs.dennisc.property.StringProperty nameProperty = accessible.getNamePropertyIfItExists();
		if (nameProperty != null)
		{
			nameProperty.addPropertyListener(this.namePropertyListener);
		}
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState.getInstance().removeValueObserver( this.valueObserver );
		edu.cmu.cs.dennisc.property.StringProperty nameProperty = accessible.getNamePropertyIfItExists();
		if (nameProperty != null)
		{
			nameProperty.removePropertyListener(this.namePropertyListener);
		}
		super.handleRemovedFrom(parent);
	}
	@Override
	protected javax.swing.AbstractButton createAwtComponent() {
		final java.awt.Color SELECTED_COLOR = java.awt.Color.YELLOW;
		final java.awt.Color UNSELECTED_COLOR = new java.awt.Color( 191, 191, 191, 127 );
		return new javax.swing.JRadioButton() {
			@Override
			public java.awt.Color getBackground() {
				if( this.getModel().isSelected() ) {
					return SELECTED_COLOR;
				} else {
					return UNSELECTED_COLOR;
				}
			}
			@Override
			public boolean isFocusable() {
				return false;
			}
			@Override
			public boolean isOpaque() {
				return false;
			}
			@Override
			protected void paintComponent(java.awt.Graphics g) {
				g.setColor( this.getBackground() );
				g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 8, 8 );
				super.paintComponent(g);
			}
		};
	}

	protected java.util.List< edu.cmu.cs.dennisc.croquet.Model > updatePopupOperations( java.util.List< edu.cmu.cs.dennisc.croquet.Model > rv ) {
		if( this.accessible instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldInAlice = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)this.accessible;
			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> fieldType = fieldInAlice.getValueType();
			rv.add( new org.alice.ide.operations.ast.RenameFieldOperation( fieldInAlice ) );
			if( fieldType.isAssignableTo( org.alice.apis.moveandturn.Transformable.class ) ) {
				if( fieldType.isAssignableTo( org.alice.apis.moveandturn.AbstractCamera.class ) ) {
					//pass
				} else {
					rv.add( new org.alice.ide.operations.ast.DeleteFieldOperation( fieldInAlice ) );
					rv.add( new org.alice.stageide.operations.ast.OrientToUprightActionOperation( fieldInAlice ) );
				}
			}
			if( fieldType.isAssignableTo( org.alice.apis.moveandturn.Model.class ) ) {
				rv.add( new org.alice.stageide.operations.ast.PlaceOnTopOfGroundActionOperation( fieldInAlice ) );
			}
			if( fieldType.isAssignableTo( org.alice.apis.stage.Person.class ) ) {
				rv.add( org.alice.stageide.croquet.models.gallerybrowser.EditPersonOperation.getInstance( fieldInAlice ) );
			}
		}
		return rv;
	}
	private java.util.List< edu.cmu.cs.dennisc.croquet.Model > createPopupOperations() {
		return this.updatePopupOperations( new java.util.LinkedList< edu.cmu.cs.dennisc.croquet.Model >() );
	}

	protected java.awt.Color calculateColor() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		java.awt.Color color = ide.getColorFor( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class );
		if( this.accessible == ide.getAccessibleListState().getSelectedItem() ) {
			color = java.awt.Color.YELLOW;
		} else {
			if( ide.isAccessibleInScope( this.accessible ) ) {
				color = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0, 0.75, 0.75 );
			} else {
				color = java.awt.Color.GRAY;
			}
			//color = edu.cmu.cs.dennisc.awt.ColorUtilities.setAlpha( color, 191 );
		}
		return color;
	}

	public edu.cmu.cs.dennisc.alice.ast.Accessible getAccessible()
	{
		return this.accessible;
	}
	
//	protected boolean isInScope() {
//		return org.alice.ide.IDE.getSingleton().isAccessibleInScope( accessible );
//	}
	
	/*package-private*/ void updateLabel() {
		String prevText = this.getModel().getTrueText();
		String nextText;
		if( this.accessible != null ) {
			nextText = this.accessible.getValidName();//org.alice.ide.IDE.getSingleton().getInstanceTextForAccessible( this.accessible );
			this.setBackgroundColor( this.calculateColor() );
		} else {
			this.setBackgroundColor( java.awt.Color.RED );
			nextText = org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().getTextForNull();
		}
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( prevText, nextText ) ) {
			//pass
		} else {
			this.getModel().setTextForBothTrueAndFalse( nextText );
			//this.revalidateAndRepaint();
		}
		
		//todo?
		this.repaint();
	}
}
//public class FieldTile extends org.alice.ide.common.ExpressionLikeSubstance {
//	private String text;
//	private edu.cmu.cs.dennisc.alice.ast.AbstractField field;
//	private org.alice.ide.operations.ast.SelectFieldActionOperation selectOperation;
//	private class NamePropertyAdapter implements edu.cmu.cs.dennisc.property.event.PropertyListener {
//		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
//		}
//		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
//			FieldTile.this.updateLabel();
//		}
//	}
//	
//	public FieldTile( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
//		assert field != null;
//		this.field = field;
//		this.selectOperation = new org.alice.ide.operations.ast.SelectFieldActionOperation( this.field );
//		this.setLeftButtonPressOperation( this.selectOperation );
//		this.setPopupOperation( new edu.cmu.cs.dennisc.croquet.PopupMenuOperation( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, java.util.UUID.fromString( "1b079c62-dd58-41dd-93cf-d85ab03a4d23" ), this.createPopupOperations() ) );
//		if( this.field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
//			((edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field).name.addPropertyListener( new NamePropertyAdapter() );
//		}
//		this.updateLabel();
//	}
//
//	@Override
//	protected int getInternalInsetLeft() {
//		return super.getInternalInsetLeft() + 2;
//	}
//	@Override
//	protected int getInsetRight() {
//		return super.getInsetRight() + 4;
//	}
//	protected java.util.List< edu.cmu.cs.dennisc.croquet.Operation > updatePopupOperations( java.util.List< edu.cmu.cs.dennisc.croquet.Operation > rv ) {
//		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldInAlice = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)this.getField();
//		edu.cmu.cs.dennisc.alice.ast.AbstractType fieldType = fieldInAlice.getValueType();
//		rv.add( new org.alice.ide.operations.ast.RenameFieldOperation( fieldInAlice ) );
//		if( fieldType.isAssignableTo( org.alice.apis.moveandturn.Transformable.class ) ) {
//			if( fieldType.isAssignableTo( org.alice.apis.moveandturn.AbstractCamera.class ) ) {
//				//pass
//			} else {
//				rv.add( new org.alice.ide.operations.ast.DeleteFieldOperation( fieldInAlice ) );
//				rv.add( new org.alice.stageide.operations.ast.OrientToUprightActionOperation( fieldInAlice ) );
//			}
//		}
//		if( fieldType.isAssignableTo( org.alice.apis.moveandturn.Model.class ) ) {
//			rv.add( new org.alice.stageide.operations.ast.PlaceOnTopOfGroundActionOperation( fieldInAlice ) );
//		}
//		if( fieldType.isAssignableTo( org.alice.apis.stage.Person.class ) ) {
//			rv.add( new org.alice.stageide.operations.ast.EditPersonActionOperation( fieldInAlice ) );
//		}
//		return rv;
//	}
//	private java.util.List< edu.cmu.cs.dennisc.croquet.Operation > createPopupOperations() {
//		return this.updatePopupOperations( new java.util.LinkedList< edu.cmu.cs.dennisc.croquet.Operation >() );
//	}
//	@Override
//	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
//		if( this.field != null ) {
//			return this.field.getValueType();
//		} else {
//			return null;
//		}
//	}
//	public edu.cmu.cs.dennisc.alice.ast.AbstractField getField() {
//		return this.field;
//	}
////	public void setField( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
////		this.field = field;
////		this.updateLabel();
////	}
//	protected java.awt.Color calculateColor() {
//		org.alice.ide.IDE ide = getIDE();
//		java.awt.Color color = ide.getColorFor( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class );
//		if( this.field == ide.getFieldSelection() ) {
//			color = java.awt.Color.YELLOW;
//		} else {
//			if( ide.isFieldInScope( this.field ) ) {
//				color = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0, 0.75, 0.75 );
//			} else {
//				color = java.awt.Color.GRAY;
//			}
//			//color = edu.cmu.cs.dennisc.awt.ColorUtilities.setAlpha( color, 191 );
//		}
//		return color;
//	}
//
//	public void updateLabel() {
//		String prevText = this.text;
//		if( this.field != null ) {
//			String text = getIDE().getInstanceTextForField( this.field, false );
//			this.setBackgroundColor( this.calculateColor() );
//			this.text = text;
//		} else {
//			this.setBackgroundColor( java.awt.Color.RED );
//			this.text = "null";
//		}
//		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( prevText, this.text ) ) {
//			//pass
//		} else {
//			this.revalidateAndRepaint();
//		}
//		
//		//todo?
//		this.repaint();
//	}
//	protected boolean isInScope() {
//		return getIDE().isFieldInScope( field );
//	}
//	@Override
//	protected java.awt.Dimension getPreferedSize( java.awt.Dimension jPreferedSize ) {
//		java.awt.Dimension rv = new java.awt.Dimension( jPreferedSize );
//		java.awt.Graphics g = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.getGraphics();
//		java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( this.text, g );
//		rv.width += (int)( bounds.getWidth()+0.5 );
//		rv.height += (int)( bounds.getHeight()+0.5 );
//		return rv;
//	}
//	
//	@Override
//	protected edu.cmu.cs.dennisc.java.awt.BevelState getBevelState() {
//		return edu.cmu.cs.dennisc.java.awt.BevelState.FLUSH;
//	}
//	@Override
//	protected boolean isExpressionTypeFeedbackDesired() {
//		return true;
//	}
//	@Override
//	protected void paintComponent( java.awt.Graphics g ) {
//		super.paintComponent( g );
//		java.awt.Insets insets = this.getBorder().getBorderInsets( this.getJComponent() );
//		java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( this.text, g );
//		g.drawString( this.text, insets.left-(int)bounds.getX(), insets.top-(int)bounds.getY() );
//	}
//	@Override
//	public void paint( java.awt.Graphics g ) {
//		super.paint( g );
//		if( isInScope() ) {
//			//pass
//		} else {
//			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//			g2.setPaint( edu.cmu.cs.dennisc.zoot.PaintUtilities.getDisabledTexturePaint() );
//			this.fillBounds( g2 );
//		}
//	}
//}
