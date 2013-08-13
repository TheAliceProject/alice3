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
package org.alice.stageide.gallerybrowser.uri.merge;

/**
 * @author Dennis Cosgrove
 */
public class ImportTypeComposite extends org.lgna.croquet.OperationInputDialogCoreComposite<org.alice.stageide.gallerybrowser.uri.merge.views.MergeTypeView> {
	private final java.net.URI uriForDescriptionPurposesOnly;

	private final org.lgna.project.ast.NamedUserType importedRootType;
	private final java.util.Set<org.lgna.common.Resource> importedResources;

	private final org.lgna.project.ast.NamedUserType srcType;
	private final org.lgna.project.ast.NamedUserType dstType;

	private final java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod>> isProcedureImportDesiredStates;
	private final java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod>> isFunctionImportDesiredStates;
	private final java.util.List<org.lgna.project.ast.UserMethod> methodsToWarnAboutOverload;
	private final java.util.List<org.lgna.project.ast.UserMethod> methodsToMerge;
	private final java.util.List<org.lgna.project.ast.UserMethod> methodsToIgnore;

	private final java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserField>> isFieldImportDesiredStates;
	private final java.util.List<org.lgna.project.ast.UserField> fieldsToWarnAboutValueTypeDifference;
	private final java.util.List<org.lgna.project.ast.UserField> fieldsToChooseInitializer;
	private final java.util.List<org.lgna.project.ast.UserField> fieldsToIgnore;

	private final org.lgna.croquet.PlainStringValue proceduresHeader = this.createStringValue( this.createKey( "proceduresHeader" ) );
	private final org.lgna.croquet.PlainStringValue functionsHeader = this.createStringValue( this.createKey( "functionsHeader" ) );
	private final org.lgna.croquet.PlainStringValue fieldsHeader = this.createStringValue( this.createKey( "fieldsHeader" ) );

	public ImportTypeComposite( java.net.URI uriForDescriptionPurposesOnly, org.lgna.project.ast.NamedUserType importedRootType, java.util.Set<org.lgna.common.Resource> importedResources, org.lgna.project.ast.NamedUserType srcType, org.lgna.project.ast.NamedUserType dstType ) {
		super( java.util.UUID.fromString( "d00d925e-0a2c-46c7-b6c8-0d3d1189bc5c" ), org.alice.ide.IDE.PROJECT_GROUP );
		this.uriForDescriptionPurposesOnly = uriForDescriptionPurposesOnly;
		this.importedRootType = importedRootType;
		this.importedResources = importedResources;
		this.srcType = srcType;
		this.dstType = dstType;

		if( this.dstType != null ) {
			this.isProcedureImportDesiredStates = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			this.isFunctionImportDesiredStates = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			this.methodsToWarnAboutOverload = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			this.methodsToMerge = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			this.methodsToIgnore = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( org.lgna.project.ast.UserMethod srcMethod : this.srcType.methods ) {
				org.lgna.project.ast.UserMethod dstMethod = MergeUtilities.findMethodWithMatchingName( srcMethod, dstType );
				java.util.List<org.lgna.project.ast.UserMethod> list;
				if( dstMethod != null ) {
					if( MergeUtilities.isEquivalent( srcMethod, dstMethod ) ) {
						list = this.methodsToIgnore;
					} else {
						if( MergeUtilities.isHeaderEquivalent( srcMethod, dstMethod ) ) {
							list = this.methodsToMerge;
						} else {
							list = this.methodsToWarnAboutOverload;
						}
					}
				} else {
					list = null;
					IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod> state = new IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod>( srcMethod );
					if( srcMethod.isProcedure() ) {
						this.isProcedureImportDesiredStates.add( state );
					} else {
						this.isFunctionImportDesiredStates.add( state );
					}
				}
				if( list != null ) {
					list.add( srcMethod );
				}
			}

			this.isFieldImportDesiredStates = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			this.fieldsToWarnAboutValueTypeDifference = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			this.fieldsToChooseInitializer = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			this.fieldsToIgnore = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

			for( org.lgna.project.ast.UserField srcField : this.srcType.fields ) {
				org.lgna.project.ast.UserField dstField = dstType.getDeclaredField( srcField.getName() );
				java.util.List<org.lgna.project.ast.UserField> list;
				if( dstField != null ) {
					if( MergeUtilities.isEquivalent( srcField, dstField ) ) {
						list = this.fieldsToIgnore;
					} else {
						if( MergeUtilities.isValueTypeEquivalent( srcField, dstField ) ) {
							list = this.fieldsToChooseInitializer;
						} else {
							list = this.fieldsToWarnAboutValueTypeDifference;
						}
					}
				} else {
					this.isFieldImportDesiredStates.add( new IsDeclarationImportDesiredState<org.lgna.project.ast.UserField>( srcField ) );
					list = null;
				}
				if( list != null ) {
					list.add( srcField );
				}
			}

		} else {
			this.isProcedureImportDesiredStates = null;
			this.isFunctionImportDesiredStates = null;
			this.methodsToWarnAboutOverload = null;
			this.methodsToMerge = null;
			this.methodsToIgnore = null;
			this.isFieldImportDesiredStates = null;
			this.fieldsToWarnAboutValueTypeDifference = null;
			this.fieldsToChooseInitializer = null;
			this.fieldsToIgnore = null;
		}
	}

	@Override
	protected org.alice.stageide.gallerybrowser.uri.merge.views.MergeTypeView createView() {
		return new org.alice.stageide.gallerybrowser.uri.merge.views.MergeTypeView( this );
	}

	public org.lgna.project.ast.NamedUserType getImportedType() {
		return this.importedRootType;
	}

	public java.util.Set<org.lgna.common.Resource> getImportedResources() {
		return this.importedResources;
	}

	public org.lgna.project.ast.NamedUserType getDstType() {
		return this.dstType;
	}

	public java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod>> getIsProcedureImportDesiredStates() {
		return this.isProcedureImportDesiredStates;
	}

	public java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod>> getIsFunctionImportDesiredStates() {
		return this.isFunctionImportDesiredStates;
	}

	public java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserField>> getIsFieldImportDesiredStates() {
		return this.isFieldImportDesiredStates;
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		if( this.dstType != null ) {
			java.util.List<org.lgna.project.ast.UserMethod> methods = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

			for( java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod>> list : new java.util.List[] { this.isProcedureImportDesiredStates, this.isFunctionImportDesiredStates } ) {
				for( IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod> methodState : list ) {
					if( methodState.getValue() ) {
						methods.add( org.lgna.project.ast.AstUtilities.createCopy( methodState.getDeclaration(), this.importedRootType ) );
					}
				}
			}
			java.util.List<org.lgna.project.ast.UserField> fields = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( IsDeclarationImportDesiredState<org.lgna.project.ast.UserField> fieldState : this.isFieldImportDesiredStates ) {
				if( fieldState.getValue() ) {
					fields.add( org.lgna.project.ast.AstUtilities.createCopy( fieldState.getDeclaration(), this.importedRootType ) );
				}
			}
			return new org.alice.stageide.gallerybrowser.uri.merge.edits.ImportTypeEdit( completionStep, this.uriForDescriptionPurposesOnly, this.dstType, methods, fields );
		} else {
			return null;
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}

	public org.lgna.croquet.PlainStringValue getProceduresHeader() {
		return this.proceduresHeader;
	}

	public org.lgna.croquet.PlainStringValue getFunctionsHeader() {
		return this.functionsHeader;
	}

	public org.lgna.croquet.PlainStringValue getFieldsHeader() {
		return this.fieldsHeader;
	}
}
