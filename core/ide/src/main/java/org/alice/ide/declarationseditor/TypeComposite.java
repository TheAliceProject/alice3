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

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.IDE;
import org.alice.ide.ast.declaration.AddFunctionComposite;
import org.alice.ide.ast.declaration.AddProcedureComposite;
import org.alice.ide.ast.export.ExportTypeToFileDialogOperation;
import org.alice.ide.ast.type.croquet.ImportTypeIteratingOperation;
import org.alice.ide.declarationseditor.type.ConstructorsToolPaletteCoreComposite;
import org.alice.ide.declarationseditor.type.FieldsToolPaletteCoreComposite;
import org.alice.ide.declarationseditor.type.FunctionsToolPaletteCoreComposite;
import org.alice.ide.declarationseditor.type.ProceduresToolPaletteCoreComposite;
import org.alice.ide.declarationseditor.type.components.TypeDeclarationView;
import org.lgna.croquet.Model;
import org.lgna.croquet.views.BooleanStateButton;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserType;

import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class TypeComposite extends DeclarationComposite<NamedUserType, TypeDeclarationView> {
	private static Map<NamedUserType, TypeComposite> map = Maps.newHashMap();

	public static synchronized TypeComposite getInstance( NamedUserType type ) {
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

	private final ConstructorsToolPaletteCoreComposite constructorsToolPaletteCoreComposite;
	private final ProceduresToolPaletteCoreComposite proceduresToolPaletteCoreComposite;
	private final FunctionsToolPaletteCoreComposite functionsToolPaletteCoreComposite;
	private final FieldsToolPaletteCoreComposite fieldsToolPaletteCoreComposite;
	private final ExportTypeToFileDialogOperation exportOperation;
	private final ImportTypeIteratingOperation importOperation;

	private TypeComposite( NamedUserType type ) {
		super( UUID.fromString( "ff057bea-73cc-4cf2-8bb3-b02e35b4b965" ), type, NamedUserType.class );
		this.constructorsToolPaletteCoreComposite = this.registerSubComposite( new ConstructorsToolPaletteCoreComposite( type ) );
		this.proceduresToolPaletteCoreComposite = this.registerSubComposite( new ProceduresToolPaletteCoreComposite( type ) );
		this.functionsToolPaletteCoreComposite = this.registerSubComposite( new FunctionsToolPaletteCoreComposite( type ) );
		this.fieldsToolPaletteCoreComposite = this.registerSubComposite( new FieldsToolPaletteCoreComposite( type ) );
		this.importOperation = new ImportTypeIteratingOperation( type );
		this.exportOperation = new ExportTypeToFileDialogOperation( type );
	}

	public ConstructorsToolPaletteCoreComposite getConstructorsToolPaletteCoreComposite() {
		return this.constructorsToolPaletteCoreComposite;
	}

	public ProceduresToolPaletteCoreComposite getProceduresToolPaletteCoreComposite() {
		return this.proceduresToolPaletteCoreComposite;
	}

	public FunctionsToolPaletteCoreComposite getFunctionsToolPaletteCoreComposite() {
		return this.functionsToolPaletteCoreComposite;
	}

	public FieldsToolPaletteCoreComposite getFieldsToolPaletteCoreComposite() {
		return this.fieldsToolPaletteCoreComposite;
	}

	public ImportTypeIteratingOperation getImportOperation() {
		return this.importOperation;
	}

	public ExportTypeToFileDialogOperation getExportOperation() {
		return this.exportOperation;
	}

	@Override
	public boolean contains( Model model ) {
		if( super.contains( model ) ) {
			return true;
		} else {
			//todo: this should really leverage Composite.contains and create sub composites and models
			if( ( model == AddProcedureComposite.getInstance( this.getType() ).getLaunchOperation() )
					||
					( model == AddFunctionComposite.getInstance( this.getType() ).getLaunchOperation() ) ) {
				return true;
			} else {
				//todo
				return false;
			}
		}
	}

	@Override
	public UserType<?> getType() {
		return this.getDeclaration();
	}

	@Override
	public void customizeTitleComponentAppearance( BooleanStateButton<?> button ) {
		super.customizeTitleComponentAppearance( button );
		button.scaleFont( 1.8f );
	}

	@Override
	public boolean isCloseable() {
		DeclarationTabState tabState = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();
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
		Logger.todo( "check to see if type is in project" );
		return true;
	}

	@Override
	protected TypeDeclarationView createView() {
		return new TypeDeclarationView( this );
	}
}
