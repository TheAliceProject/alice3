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
public abstract class AbstractSceneEditor extends org.alice.ide.Editor<edu.cmu.cs.dennisc.alice.ast.AbstractType> implements org.alice.ide.event.IDEListener, edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = null;
	
	public abstract void setRenderingEnabled( boolean isRenderingEnabled );
	public abstract void handleExpandContractChange( boolean isExpanded );
	public abstract void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses );
	
	@Deprecated
	public abstract void handleFieldCreation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance );
	
	public abstract void generateCodeForSetUp( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty );
	protected edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine getVM() {
		return getIDE().getVirtualMachineForSceneEditor();
	}
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getSceneTypeDeclaredInAlice() {
		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.sceneField.getDeclaringType();
	}
	public void addFieldToSceneType( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
		getSceneTypeDeclaredInAlice().fields.add( field );
	}
	protected edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getSceneField() {
		return this.sceneField;
	}
	protected void setSceneField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		if( this.sceneField != null ) {
			this.sceneField.removeListPropertyListener( this );
		}
		this.sceneField = sceneField;
		if( this.sceneField != null ) {
			this.sceneField.addListPropertyListener( this );
		}
	}
	protected void setProgramType( edu.cmu.cs.dennisc.alice.ast.AbstractType programType ) {
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldInAlice;
		if( programType != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractField field = programType.getDeclaredFields().get( 0 );
			if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
				fieldInAlice = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field;
			} else {
				fieldInAlice = null;
			}
		} else {
			fieldInAlice = null;
		}
		setSceneField( fieldInAlice );
	}
	public void fieldSelectionChanging( org.alice.ide.event.FieldSelectionEvent e ) {
	}
	public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
	}

	public void localeChanging( org.alice.ide.event.LocaleEvent e ) {
	}
	public void localeChanged( org.alice.ide.event.LocaleEvent e ) {
	}


	public void focusedCodeChanging( org.alice.ide.event.FocusedCodeChangeEvent e ) {
	}
	public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
	}


	public void projectOpening( org.alice.ide.event.ProjectOpenEvent e ) {
	}
	public void projectOpened( org.alice.ide.event.ProjectOpenEvent e ) {
		edu.cmu.cs.dennisc.alice.Project project = e.getNextValue();
		this.setProgramType( project.getProgramType() );
		this.revalidate();
		this.repaint();
	}


	public void transientSelectionChanging( org.alice.ide.event.TransientSelectionEvent e ) {
	}
	public void transientSelectionChanged( org.alice.ide.event.TransientSelectionEvent e ) {
	}
	
	public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
}
