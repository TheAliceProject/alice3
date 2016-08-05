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
package org.alice.ide.ast.type.croquet;

import org.alice.ide.ast.type.merge.core.MergeUtilities;
import org.alice.ide.ast.type.merge.croquet.AddMembersPage;
import org.alice.ide.ast.type.preview.croquet.PreviewPage;

/**
 * @author Dennis Cosgrove
 */
public class ImportTypeWizard extends org.lgna.croquet.SimpleOperationWizardDialogCoreComposite {
	private final AddMembersPage addMembersPage;
	private final PreviewPage previewPage;

	public ImportTypeWizard( java.net.URI uriForDescriptionPurposesOnly, org.lgna.project.ast.NamedUserType importedRootType, java.util.Set<org.lgna.common.Resource> importedResources, org.lgna.project.ast.NamedUserType srcType, org.lgna.project.ast.NamedUserType dstType ) {
		super( java.util.UUID.fromString( "30a4572a-53e9-464b-a8a3-cdebac13372f" ), org.lgna.croquet.Application.PROJECT_GROUP );
		this.addMembersPage = new AddMembersPage( this, uriForDescriptionPurposesOnly, importedRootType, importedResources, srcType, dstType );
		this.previewPage = new PreviewPage( this );
		this.addPage( this.addMembersPage );
		this.addPage( this.previewPage );
	}

	public AddMembersPage getAddMembersPage() {
		return this.addMembersPage;
	}

	public PreviewPage getPreviewPage() {
		return this.previewPage;
	}

	@Override
	protected boolean isAdornmentDesired() {
		return false;
	}

	@Override
	protected GoldenRatioPolicy getGoldenRatioPolicy() {
		return null;
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		return this.addMembersPage.createEdit( completionStep );
	}

	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );
		new org.lgna.croquet.simple.SimpleApplication();

		java.io.File projectFile = new java.io.File( args[ 0 ] );

		org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject( projectFile );
		org.alice.ide.ProjectStack.pushProject( project );

		java.io.File typeFile = new java.io.File( args[ 1 ] );

		org.lgna.project.io.TypeResourcesPair typeResourcesPair = org.lgna.project.io.IoUtilities.readType( typeFile );
		org.lgna.project.ast.NamedUserType importedRootType = typeResourcesPair.getType();
		java.util.Set<org.lgna.common.Resource> importedResources = typeResourcesPair.getResources();
		org.lgna.project.ast.NamedUserType srcType = importedRootType;
		org.lgna.project.ast.NamedUserType dstType = MergeUtilities.findMatchingTypeInExistingTypes( srcType );
		if( dstType != null ) {
			//pass
		} else {
			dstType = new org.lgna.project.ast.NamedUserType();
			dstType.name.setValue( importedRootType.getName() );
		}
		ImportTypeWizard wizard = new ImportTypeWizard( typeFile.toURI(), importedRootType, importedResources, srcType, dstType );
		wizard.getLaunchOperation().fire();
		System.exit( 0 );
	}
}
