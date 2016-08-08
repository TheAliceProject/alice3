/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.declarationseditor;

/**
 * @author Dennis Cosgrove
 */
public class TypeComposite extends DeclarationComposite<org.lgna.project.ast.NamedUserType, org.alice.ide.declarationseditor.type.components.TypeDeclarationView> {
	private static java.util.Map<org.lgna.project.ast.NamedUserType, TypeComposite> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized TypeComposite getInstance( org.lgna.project.ast.NamedUserType type ) {
		if( type != null ) {
			TypeComposite rv = map.get( type );
			if( rv != null ) {
				//pass
			} else {
				rv = new TypeComposite( type );
				map.put( type, rv );
			}
			return rv;
		} else {
			return null;
		}
	}

	private final org.alice.ide.declarationseditor.type.ConstructorsToolPaletteCoreComposite constructorsToolPaletteCoreComposite;
	private final org.alice.ide.declarationseditor.type.ProceduresToolPaletteCoreComposite proceduresToolPaletteCoreComposite;
	private final org.alice.ide.declarationseditor.type.FunctionsToolPaletteCoreComposite functionsToolPaletteCoreComposite;
	private final org.alice.ide.declarationseditor.type.FieldsToolPaletteCoreComposite fieldsToolPaletteCoreComposite;
	private final org.alice.ide.ast.export.ExportTypeToFileDialogOperation exportOperation;
	private final org.alice.ide.ast.type.croquet.ImportTypeIteratingOperation importOperation;

	private TypeComposite( org.lgna.project.ast.NamedUserType type ) {
		super( java.util.UUID.fromString( "ff057bea-73cc-4cf2-8bb3-b02e35b4b965" ), type, org.lgna.project.ast.NamedUserType.class );
		this.constructorsToolPaletteCoreComposite = this.registerSubComposite( new org.alice.ide.declarationseditor.type.ConstructorsToolPaletteCoreComposite( type ) );
		this.proceduresToolPaletteCoreComposite = this.registerSubComposite( new org.alice.ide.declarationseditor.type.ProceduresToolPaletteCoreComposite( type ) );
		this.functionsToolPaletteCoreComposite = this.registerSubComposite( new org.alice.ide.declarationseditor.type.FunctionsToolPaletteCoreComposite( type ) );
		this.fieldsToolPaletteCoreComposite = this.registerSubComposite( new org.alice.ide.declarationseditor.type.FieldsToolPaletteCoreComposite( type ) );
		this.importOperation = new org.alice.ide.ast.type.croquet.ImportTypeIteratingOperation( type );
		this.exportOperation = new org.alice.ide.ast.export.ExportTypeToFileDialogOperation( type );
	}

	public org.alice.ide.declarationseditor.type.ConstructorsToolPaletteCoreComposite getConstructorsToolPaletteCoreComposite() {
		return this.constructorsToolPaletteCoreComposite;
	}

	public org.alice.ide.declarationseditor.type.ProceduresToolPaletteCoreComposite getProceduresToolPaletteCoreComposite() {
		return this.proceduresToolPaletteCoreComposite;
	}

	public org.alice.ide.declarationseditor.type.FunctionsToolPaletteCoreComposite getFunctionsToolPaletteCoreComposite() {
		return this.functionsToolPaletteCoreComposite;
	}

	public org.alice.ide.declarationseditor.type.FieldsToolPaletteCoreComposite getFieldsToolPaletteCoreComposite() {
		return this.fieldsToolPaletteCoreComposite;
	}

	public org.alice.ide.ast.type.croquet.ImportTypeIteratingOperation getImportOperation() {
		return this.importOperation;
	}

	public org.alice.ide.ast.export.ExportTypeToFileDialogOperation getExportOperation() {
		return this.exportOperation;
	}

	@Override
	public boolean contains( org.lgna.croquet.Model model ) {
		if( super.contains( model ) ) {
			return true;
		} else {
			//todo: this should really leverage Composite.contains and create sub composites and models
			if( ( model == org.alice.ide.ast.declaration.AddProcedureComposite.getInstance( this.getType() ).getLaunchOperation() )
					||
					( model == org.alice.ide.ast.declaration.AddFunctionComposite.getInstance( this.getType() ).getLaunchOperation() ) ) {
				return true;
			} else {
				//todo
				return false;
			}
		}
	}

	@Override
	public org.lgna.project.ast.UserType<?> getType() {
		return this.getDeclaration();
	}

	@Override
	public void customizeTitleComponentAppearance( org.lgna.croquet.views.BooleanStateButton<?> button ) {
		super.customizeTitleComponentAppearance( button );
		button.scaleFont( 1.8f );
	}

	@Override
	public boolean isCloseable() {
		DeclarationTabState tabState = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();
		for( DeclarationComposite tab : tabState ) {
			if( tab != null ) {
				if( ( tab != this ) && ( tab.getType() == this.getDeclaration() ) ) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean isPotentiallyCloseable() {
		return true;
	}

	@Override
	public boolean isValid() {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "check to see if type is in project" );
		return true;
	}

	@Override
	protected org.alice.ide.declarationseditor.type.components.TypeDeclarationView createView() {
		return new org.alice.ide.declarationseditor.type.components.TypeDeclarationView( this );
	}
}
