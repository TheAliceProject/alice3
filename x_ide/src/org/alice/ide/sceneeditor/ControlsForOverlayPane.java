/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.sceneeditor;

/**
 * @author Dennis Cosgrove
 */
public class ControlsForOverlayPane extends edu.cmu.cs.dennisc.swing.CornerSpringPane {

	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > fieldsAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >() {

		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			ControlsForOverlayPane.this.refreshFields();
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			ControlsForOverlayPane.this.refreshFields();
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			ControlsForOverlayPane.this.refreshFields();
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			ControlsForOverlayPane.this.refreshFields();
		}
	};
	private FieldTile rootFieldTile = createFieldTile( null );
	private java.util.List< FieldTile > declaredFieldTiles = new java.util.LinkedList< FieldTile >();
	private zoot.ZCheckBoxMenuItem isSceneEditorExpandedCheckBox;
	
	public ControlsForOverlayPane( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField ) {
		this.setNorthWestComponent( this.rootFieldTile );
		this.setOpaque( false );
		this.setRootField( rootField );
	}
	
	public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
		this.refreshFields();
	}
	public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		//edu.cmu.cs.dennisc.alice.ast.AbstractType type = e.getNextValue().getDeclaringType();
//		synchronized( rootFieldTile ) {
			rootFieldTile.updateLabel();
			for( FieldTile fieldTile : ControlsForOverlayPane.this.declaredFieldTiles ) {
				fieldTile.updateLabel();
			}
//		}
	}

	@Override
	public void addNotify() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
//		ide.addIDEListener( this.ideAdapter );
		this.isSceneEditorExpandedCheckBox = new zoot.ZCheckBoxMenuItem( ide.getIsSceneEditorExpandedOperation() );
		this.setSouthEastComponent( this.isSceneEditorExpandedCheckBox );
		super.addNotify();
	}
	

	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getRootTypeDeclaredInAlice() {
		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)getRootField().valueType.getValue();
	}
	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getRootField() {
		return (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)this.rootFieldTile.getField();
	}
	public void setRootField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField ) {
		if( this.getRootField() != null ) {
			getRootTypeDeclaredInAlice().fields.removeListPropertyListener( this.fieldsAdapter );
		}
		this.rootFieldTile.setField( rootField );
		if( this.getRootField() != null ) {
			getRootTypeDeclaredInAlice().fields.addListPropertyListener( this.fieldsAdapter );
		}
		this.refreshFields();
	}
	protected FieldTile createFieldTile( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return new FieldTile( field );
	}
	private void refreshFields() {
		synchronized( this.rootFieldTile ) {
			javax.swing.SpringLayout springLayout = this.getSpringLayout();
			for( FieldTile fieldTile : this.declaredFieldTiles ) {
				springLayout.removeLayoutComponent( fieldTile );
				this.remove( fieldTile );
			}
			this.declaredFieldTiles.clear();
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField = this.getRootField();
			this.rootFieldTile.updateLabel();
			java.awt.Component prev = this.rootFieldTile;
			if( rootField != null ) {
				for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : rootField.valueType.getValue().getDeclaredFields() ) {
					FieldTile fieldTile = createFieldTile( field );
					this.add( fieldTile );
					this.declaredFieldTiles.add( fieldTile );
					springLayout.putConstraint( javax.swing.SpringLayout.NORTH, fieldTile, 2, javax.swing.SpringLayout.SOUTH, prev );
					springLayout.putConstraint( javax.swing.SpringLayout.WEST, fieldTile, 10, javax.swing.SpringLayout.WEST, this.rootFieldTile );
					prev = fieldTile;
				}
			}
			revalidate();
			repaint();
		}
	}
}
