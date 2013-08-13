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
public class MergeTypeComposite extends org.lgna.croquet.OperationInputDialogCoreComposite<org.alice.stageide.gallerybrowser.uri.merge.views.MergeTypeView> {
	private final java.net.URI uri;

	private final org.lgna.project.ast.NamedUserType importedType;
	private final java.util.Set<org.lgna.common.Resource> importedResources;

	private final org.lgna.project.ast.NamedUserType existingType;

	private final java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod>> isProcedureImportDesiredStates;
	private final java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod>> isFunctionImportDesiredStates;
	private final java.util.List<org.lgna.project.ast.UserMethod> methodsToWarnAboutOverload;
	private final java.util.List<org.lgna.project.ast.UserMethod> methodsToMerge;
	private final java.util.List<org.lgna.project.ast.UserMethod> methodsToIgnore;

	private final java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserField>> isFieldImportDesiredStates;
	private final java.util.List<org.lgna.project.ast.UserField> fieldsToWarnAboutValueTypeDifference;
	private final java.util.List<org.lgna.project.ast.UserField> fieldsToChooseInitializer;
	private final java.util.List<org.lgna.project.ast.UserField> fieldsToIgnore;

	public MergeTypeComposite( java.net.URI uri ) {
		super( java.util.UUID.fromString( "d00d925e-0a2c-46c7-b6c8-0d3d1189bc5c" ), org.alice.ide.IDE.PROJECT_GROUP );
		this.uri = uri;
		edu.cmu.cs.dennisc.pattern.Tuple2<org.lgna.project.ast.NamedUserType, java.util.Set<org.lgna.common.Resource>> tuple;
		try {
			tuple = org.lgna.project.io.IoUtilities.readType( new java.io.File( this.uri ) );
		} catch( java.io.IOException ioe ) {
			tuple = null;
			edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( ioe, this.uri );
		} catch( org.lgna.project.VersionNotSupportedException vnse ) {
			tuple = null;
			edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( vnse, this.uri );
		}
		if( tuple != null ) {
			this.importedType = tuple.getA();
			this.importedResources = tuple.getB();
			this.existingType = MergeUtilities.findMatchingTypeInExistingTypes( this.importedType );

			if( this.existingType != null ) {
				this.isProcedureImportDesiredStates = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				this.isFunctionImportDesiredStates = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				this.methodsToWarnAboutOverload = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				this.methodsToMerge = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				this.methodsToIgnore = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				for( org.lgna.project.ast.UserMethod importedMethod : this.importedType.methods ) {
					org.lgna.project.ast.UserMethod existingMethod = MergeUtilities.findMethodWithMatchingName( importedMethod, existingType );
					java.util.List<org.lgna.project.ast.UserMethod> list;
					if( existingMethod != null ) {
						if( MergeUtilities.isEquivalent( importedMethod, existingMethod ) ) {
							list = this.methodsToIgnore;
						} else {
							if( MergeUtilities.isHeaderEquivalent( importedMethod, existingMethod ) ) {
								list = this.methodsToMerge;
							} else {
								list = this.methodsToWarnAboutOverload;
							}
						}
					} else {
						list = null;
						IsDeclarationImportDesiredState state = new IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod>( importedMethod );
						if( importedMethod.isProcedure() ) {
							this.isProcedureImportDesiredStates.add( state );
						} else {
							this.isFunctionImportDesiredStates.add( state );
						}
					}
					if( list != null ) {
						list.add( importedMethod );
					}
				}

				this.isFieldImportDesiredStates = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				this.fieldsToWarnAboutValueTypeDifference = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				this.fieldsToChooseInitializer = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				this.fieldsToIgnore = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

				for( org.lgna.project.ast.UserField importedField : this.importedType.fields ) {
					org.lgna.project.ast.UserField existingField = existingType.getDeclaredField( importedField.getName() );
					java.util.List<org.lgna.project.ast.UserField> list;
					if( existingField != null ) {
						if( MergeUtilities.isEquivalent( importedField, existingField ) ) {
							list = this.fieldsToIgnore;
						} else {
							if( MergeUtilities.isValueTypeEquivalent( importedField, existingField ) ) {
								list = this.fieldsToChooseInitializer;
							} else {
								list = this.fieldsToWarnAboutValueTypeDifference;
							}
						}
					} else {
						this.isFieldImportDesiredStates.add( new IsDeclarationImportDesiredState<org.lgna.project.ast.UserField>( importedField ) );
						list = null;
					}
					if( list != null ) {
						list.add( importedField );
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
		} else {
			this.importedType = null;
			this.importedResources = null;
			this.existingType = null;
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
		return this.importedType;
	}

	public org.lgna.project.ast.NamedUserType getExistingType() {
		return this.existingType;
	}

	public java.util.Set<org.lgna.common.Resource> getImportedResources() {
		return this.importedResources;
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
		if( this.existingType != null ) {
			java.util.List<org.lgna.project.ast.UserMethod> methods = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

			for( java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod>> list : new java.util.List[] { this.isProcedureImportDesiredStates, this.isFunctionImportDesiredStates } ) {
				for( IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod> methodState : list ) {
					if( methodState.getValue() ) {
						methods.add( org.lgna.project.ast.AstUtilities.createCopy( methodState.getDeclaration(), this.importedType ) );
					}
				}
			}
			java.util.List<org.lgna.project.ast.UserField> fields = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( IsDeclarationImportDesiredState<org.lgna.project.ast.UserField> fieldState : this.isFieldImportDesiredStates ) {
				if( fieldState.getValue() ) {
					fields.add( org.lgna.project.ast.AstUtilities.createCopy( fieldState.getDeclaration(), this.importedType ) );
				}
			}
			return new org.alice.stageide.gallerybrowser.uri.merge.edits.ImportTypeEdit( completionStep, this.uri, this.existingType, methods, fields );
		} else {
			return null;
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}
}
