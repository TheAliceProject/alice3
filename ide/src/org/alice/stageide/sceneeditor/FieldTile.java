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
public class FieldTile extends org.alice.ide.common.ExpressionLikeSubstance {
	private String text;
	private edu.cmu.cs.dennisc.alice.ast.AbstractField field;
	private org.alice.ide.operations.ast.SelectFieldActionOperation selectOperation;
	private edu.cmu.cs.dennisc.zoot.DefaultPopupActionOperation popupOperation;
	private class NamePropertyAdapter implements edu.cmu.cs.dennisc.property.event.PropertyListener {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			FieldTile.this.updateLabel();
		}
	}
	public FieldTile( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		assert field != null;
		this.field = field;
		this.selectOperation = new org.alice.ide.operations.ast.SelectFieldActionOperation( this.field );
		this.popupOperation = new edu.cmu.cs.dennisc.zoot.DefaultPopupActionOperation( this.createPopupOperations() );
		this.setLeftButtonPressOperation( this.selectOperation );
		this.setPopupOperation( this.popupOperation );
		if( this.field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
			((edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field).name.addPropertyListener( new NamePropertyAdapter() );
		}
		this.updateLabel();
	}

	@Override
	protected int getInternalInsetLeft() {
		return super.getInternalInsetLeft() + 2;
	}
	@Override
	protected int getInsetRight() {
		return super.getInsetRight() + 4;
	}
	protected java.util.List< edu.cmu.cs.dennisc.zoot.Operation > updatePopupOperations( java.util.List< edu.cmu.cs.dennisc.zoot.Operation > rv ) {
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldInAlice = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)this.getField();
		edu.cmu.cs.dennisc.alice.ast.AbstractType fieldType = fieldInAlice.getValueType();
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
			rv.add( new org.alice.stageide.operations.ast.EditPersonActionOperation( fieldInAlice ) );
		}
		return rv;
	}
	private java.util.List< edu.cmu.cs.dennisc.zoot.Operation > createPopupOperations() {
		return this.updatePopupOperations( new java.util.LinkedList< edu.cmu.cs.dennisc.zoot.Operation >() );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		if( this.field != null ) {
			return this.field.getValueType();
		} else {
			return null;
		}
	}
	public edu.cmu.cs.dennisc.alice.ast.AbstractField getField() {
		return this.field;
	}
//	public void setField( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
//		this.field = field;
//		this.updateLabel();
//	}
	protected java.awt.Color calculateColor() {
		org.alice.ide.IDE ide = getIDE();
		java.awt.Color color = ide.getColorFor( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class );
		if( this.field == ide.getFieldSelection() ) {
			color = java.awt.Color.YELLOW;
		} else {
			if( ide.isFieldInScope( this.field ) ) {
				color = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0, 0.75, 0.75 );
			} else {
				color = java.awt.Color.GRAY;
			}
			//color = edu.cmu.cs.dennisc.awt.ColorUtilities.setAlpha( color, 191 );
		}
		return color;
	}

	public void updateLabel() {
		String prevText = this.text;
		if( this.field != null ) {
			String text = getIDE().getInstanceTextForField( this.field, false );
			this.setBackground( this.calculateColor() );
			this.text = text;
		} else {
			this.setBackground( java.awt.Color.RED );
			this.text = "null";
		}
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( prevText, this.text ) ) {
			//pass
		} else {
			this.revalidate();
		}
		this.repaint();
	}
	protected boolean isInScope() {
		return getIDE().isFieldInScope( field );
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		java.awt.Dimension rv = super.getPreferredSize();
		java.awt.Graphics g = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.getGraphics();
		java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( this.text, g );
		rv.width += (int)( bounds.getWidth()+0.5 );
		rv.height += (int)( bounds.getHeight()+0.5 );
		return rv;
	}
	
	@Override
	protected edu.cmu.cs.dennisc.java.awt.BevelState getBevelState() {
		return edu.cmu.cs.dennisc.java.awt.BevelState.FLUSH;
	}
	@Override
	protected boolean isExpressionTypeFeedbackDesired() {
		return true;
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		java.awt.Insets insets = this.getBorder().getBorderInsets( this );
		java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( this.text, g );
		g.drawString( this.text, insets.left-(int)bounds.getX(), insets.top-(int)bounds.getY() );
	}
	@Override
	public void paint( java.awt.Graphics g ) {
		super.paint( g );
		if( isInScope() ) {
			//pass
		} else {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			g2.setPaint( edu.cmu.cs.dennisc.zoot.PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
}
