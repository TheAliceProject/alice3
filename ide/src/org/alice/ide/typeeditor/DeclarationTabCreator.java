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

package org.alice.ide.typeeditor;

/**
 * @author Dennis Cosgrove
 */
public enum DeclarationTabCreator implements org.lgna.croquet.TabSelectionState.TabCreator< org.alice.ide.croquet.models.typeeditor.DeclarationComposite > {
	SINGLETON;
	
	private static class NamePropertyListener implements edu.cmu.cs.dennisc.property.event.PropertyListener {
		private final org.lgna.croquet.BooleanState booleanState;
		public NamePropertyListener( org.lgna.croquet.BooleanState booleanState ) {
			this.booleanState = booleanState;
		}
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			this.booleanState.setTextForBothTrueAndFalse( (String)e.getTypedSource().getValue( e.getOwner() ) );
		}
	};
	private final java.util.Map< org.lgna.croquet.BooleanState, NamePropertyListener > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();  
	public java.util.UUID getId( org.alice.ide.croquet.models.typeeditor.DeclarationComposite item ) {
		return item.getDeclaration().getUUID();
	}
	public org.lgna.croquet.components.ScrollPane createScrollPane( org.alice.ide.croquet.models.typeeditor.DeclarationComposite item ) {
		return null;
	}
	public org.lgna.croquet.components.JComponent< ? > createMainComponent( org.alice.ide.croquet.models.typeeditor.DeclarationComposite item ) {
		org.lgna.project.ast.AbstractDeclaration declaration = item.getDeclaration();
		if( declaration instanceof org.lgna.project.ast.AbstractCode ) {
			return org.alice.ide.codeeditor.CodeEditor.getInstance( (org.lgna.project.ast.AbstractCode)declaration );
		} else if( declaration instanceof org.lgna.project.ast.NamedUserType ){
			//return new org.alice.ide.editorstabbedpane.EditTypePanel( (org.lgna.project.ast.NamedUserType)declaration, -1 );
			return new TypeDeclarationPane( (org.lgna.project.ast.NamedUserType)declaration );
		} else {
			return new org.lgna.croquet.components.Label( "todo" );
		}
	}
	
	public void customizeTitleComponent( org.lgna.croquet.BooleanState booleanState, final org.lgna.croquet.components.AbstractButton< ?, org.lgna.croquet.BooleanState > button, org.alice.ide.croquet.models.typeeditor.DeclarationComposite item ) {
		org.lgna.project.ast.AbstractDeclaration declaration = item.getDeclaration();
		booleanState.setTextForBothTrueAndFalse( declaration.getName() );
		button.scaleFont( 1.5f );

		edu.cmu.cs.dennisc.property.StringProperty nameProperty = declaration.getNamePropertyIfItExists();
		if( nameProperty != null ) {
			NamePropertyListener namePropertyListener = new NamePropertyListener( booleanState );
			nameProperty.addPropertyListener( namePropertyListener );
			map.put( booleanState, namePropertyListener );
		}
	}
	public void releaseTitleComponent( org.lgna.croquet.BooleanState booleanState, org.lgna.croquet.components.AbstractButton< ?, org.lgna.croquet.BooleanState > button, org.alice.ide.croquet.models.typeeditor.DeclarationComposite item ) {
		org.lgna.project.ast.AbstractDeclaration declaration = item.getDeclaration();
		edu.cmu.cs.dennisc.property.StringProperty nameProperty = declaration.getNamePropertyIfItExists();
		if( nameProperty != null ) {
			NamePropertyListener namePropertyListener = map.get( booleanState );
			nameProperty.removePropertyListener( namePropertyListener );
			map.remove( booleanState );
		}
	}
	public boolean isCloseable( org.alice.ide.croquet.models.typeeditor.DeclarationComposite item ) {
		return item.getDeclaration() instanceof org.lgna.project.ast.AbstractCode;
	}
}
