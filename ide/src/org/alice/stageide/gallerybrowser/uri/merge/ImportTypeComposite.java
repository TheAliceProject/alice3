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

	private final AddProceduresComposite addProceduresComposite;
	private final AddFunctionsComposite addFunctionsComposite;
	private final AddFieldsComposite addFieldsComposite;

	private final ErrorStatus actionItemsRemainingError = this.createErrorStatus( this.createKey( "actionItemsRemainingError" ) );

	private final org.lgna.croquet.Operation acceptAllDifferentImplementationsOperation = this.createActionOperation( this.createKey( "acceptAllDifferentImplementationsOperation" ), new Action() {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			acceptAllDifferentImplementations();
			return null;
		}
	} );
	private final org.lgna.croquet.Operation rejectAllDifferentImplementationsOperation = this.createActionOperation( this.createKey( "rejectAllDifferentImplementationsOperation" ), new Action() {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			rejectAllDifferentImplementations();
			return null;
		}
	} );

	private final org.lgna.croquet.PlainStringValue differentImplementationsHeader = this.createStringValue( this.createKey( "differentImplementationsHeader" ) );
	private final org.lgna.croquet.PlainStringValue differentImplementationsSubHeader = this.createStringValue( this.createKey( "differentImplementationsSubHeader" ) );

	private boolean isManagementLevelAppropriate( org.lgna.project.ast.UserMethod method ) {
		org.lgna.project.ast.ManagementLevel managementLevel = method.getManagementLevel();
		return ( managementLevel == null ) || ( managementLevel == org.lgna.project.ast.ManagementLevel.NONE );
	}

	public ImportTypeComposite( java.net.URI uriForDescriptionPurposesOnly, org.lgna.project.ast.NamedUserType importedRootType, java.util.Set<org.lgna.common.Resource> importedResources, org.lgna.project.ast.NamedUserType srcType, org.lgna.project.ast.NamedUserType dstType ) {
		super( java.util.UUID.fromString( "d00d925e-0a2c-46c7-b6c8-0d3d1189bc5c" ), org.alice.ide.IDE.PROJECT_GROUP );
		this.uriForDescriptionPurposesOnly = uriForDescriptionPurposesOnly;
		this.importedRootType = importedRootType;
		this.importedResources = importedResources;
		this.srcType = srcType;
		this.dstType = dstType;

		if( this.dstType != null ) {
			java.util.List<org.lgna.project.ast.UserMethod> projectProcedures = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			java.util.List<org.lgna.project.ast.UserMethod> projectFunctions = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( org.lgna.project.ast.UserMethod projectMethod : this.dstType.methods ) {
				if( isManagementLevelAppropriate( projectMethod ) ) {
					if( projectMethod.isProcedure() ) {
						projectProcedures.add( projectMethod );
					} else {
						projectFunctions.add( projectMethod );
					}
				}
			}
			this.addProceduresComposite = this.registerSubComposite( new AddProceduresComposite( this.uriForDescriptionPurposesOnly, projectProcedures ) );
			this.addFunctionsComposite = this.registerSubComposite( new AddFunctionsComposite( this.uriForDescriptionPurposesOnly, projectFunctions ) );

			java.util.List<org.lgna.project.ast.UserField> dstFields = this.dstType.getDeclaredFields();
			java.util.List<org.lgna.project.ast.UserField> projectFields = edu.cmu.cs.dennisc.java.util.Collections.newArrayListWithInitialCapacity( dstFields.size() );
			projectFields.addAll( dstFields );
			this.addFieldsComposite = this.registerSubComposite( new AddFieldsComposite( this.uriForDescriptionPurposesOnly, projectFields ) );

			for( org.lgna.project.ast.UserMethod importMethod : this.srcType.methods ) {
				if( isManagementLevelAppropriate( importMethod ) ) {
					AddMethodsComposite<?> addMethodsComposite = importMethod.isProcedure() ? this.addProceduresComposite : this.addFunctionsComposite;
					org.lgna.project.ast.UserMethod projectMethod = MergeUtilities.findMethodWithMatchingName( importMethod, dstType );
					if( projectMethod != null ) {
						if( MergeUtilities.isEquivalent( projectMethod, importMethod ) ) {
							addMethodsComposite.addIdenticalMembers( projectMethod, importMethod );
						} else {
							if( MergeUtilities.isHeaderEquivalent( importMethod, projectMethod ) ) {
								addMethodsComposite.addDifferentImplementationMembers( projectMethod, importMethod );
							} else {
								addMethodsComposite.addDifferentSignatureMembers( projectMethod, importMethod );
							}
						}
					} else {
						addMethodsComposite.addImportOnlyMember( importMethod );
					}
				}
			}

			this.addProceduresComposite.reifyProjectOnly();
			this.addFunctionsComposite.reifyProjectOnly();

			for( org.lgna.project.ast.UserField importField : this.srcType.fields ) {
				org.lgna.project.ast.UserField projectField = dstType.getDeclaredField( importField.getName() );
				if( projectField != null ) {
					if( MergeUtilities.isEquivalent( projectField, importField ) ) {
						this.addFieldsComposite.addIdenticalMembers( projectField, importField );
					} else {
						if( MergeUtilities.isValueTypeEquivalent( projectField, importField ) ) {
							this.addFieldsComposite.addDifferentImplementationMembers( projectField, importField );
						} else {
							this.addFieldsComposite.addDifferentSignatureMembers( projectField, importField );
						}
					}
				} else {
					this.addFieldsComposite.addImportOnlyMember( importField );
				}
			}

			this.addFieldsComposite.reifyProjectOnly();

		} else {
			this.addProceduresComposite = null;
			this.addFunctionsComposite = null;
			this.addFieldsComposite = null;
		}
	}

	public AddProceduresComposite getAddProceduresComposite() {
		return this.addProceduresComposite;
	}

	public AddFunctionsComposite getAddFunctionsComposite() {
		return this.addFunctionsComposite;
	}

	public AddFieldsComposite getAddFieldsComposite() {
		return this.addFieldsComposite;
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

	private void addRenameIfNecessary( java.util.List<org.alice.stageide.gallerybrowser.uri.merge.edits.RenameMemberData> renames, MemberNameState<? extends org.lgna.project.ast.Member> nameState, org.lgna.project.ast.Member member ) {
		String nextName = nameState.getValue();
		if( nextName.contentEquals( member.getName() ) ) {
			//pass
		} else {
			renames.add( new org.alice.stageide.gallerybrowser.uri.merge.edits.RenameMemberData( member, nextName ) );
		}
	}

	private void addRenameIfNecessary( java.util.List<org.alice.stageide.gallerybrowser.uri.merge.edits.RenameMemberData> renames, MemberNameState<? extends org.lgna.project.ast.Member> nameState ) {
		addRenameIfNecessary( renames, nameState, nameState.getMember() );
	}

	private <M extends org.lgna.project.ast.Member> M createImportCopy( M original ) {
		return org.lgna.project.ast.AstUtilities.createCopy( original, this.importedRootType );
	}

	private <M extends org.lgna.project.ast.Member> void addMembersAndRenames( java.util.List<M> membersToAdd, java.util.List<M> membersToRemove, java.util.List<org.alice.stageide.gallerybrowser.uri.merge.edits.RenameMemberData> renames, AddMembersComposite<?, M> addMembersComposite ) {
		for( ImportOnly<M> importOnly : addMembersComposite.getImportOnlys() ) {
			if( importOnly.getIsAddDesiredState().getValue() ) {
				membersToAdd.add( this.createImportCopy( importOnly.getImportMember() ) );
			}
		}

		for( DifferentSignature<M> differentSignature : addMembersComposite.getDifferentSignatures() ) {
			if( differentSignature.getIsAddDesiredState().getValue() ) {
				M member = this.createImportCopy( differentSignature.getImportMember() );
				membersToAdd.add( member );
				addRenameIfNecessary( renames, differentSignature.getImportNameState(), member );
			}
			addRenameIfNecessary( renames, differentSignature.getProjectNameState() );
		}

		for( DifferentImplementation<M> differentImplementation : addMembersComposite.getDifferentImplementations() ) {
			if( differentImplementation.getIsAddDesiredState().getValue() ) {
				M member = this.createImportCopy( differentImplementation.getImportMember() );
				membersToAdd.add( member );
				if( differentImplementation.getIsKeepDesiredState().getValue() ) {
					addRenameIfNecessary( renames, differentImplementation.getImportNameState(), member );
					addRenameIfNecessary( renames, differentImplementation.getProjectNameState() );
				} else {
					membersToRemove.add( differentImplementation.getProjectMember() );
				}
			} else {
				if( differentImplementation.getIsKeepDesiredState().getValue() ) {
					//pass
				} else {
					//should not happen
				}
			}
		}
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		if( this.dstType != null ) {
			java.util.List<org.alice.stageide.gallerybrowser.uri.merge.edits.RenameMemberData> renames = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

			java.util.List<org.lgna.project.ast.UserMethod> methodsToAdd = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			java.util.List<org.lgna.project.ast.UserMethod> methodsToRemove = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( AddMethodsComposite<?> addMethodsComposite : new AddMethodsComposite[] { this.addProceduresComposite, this.addFunctionsComposite } ) {
				addMembersAndRenames( methodsToAdd, methodsToRemove, renames, addMethodsComposite );
			}

			java.util.List<org.lgna.project.ast.UserField> fieldsToAdd = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			java.util.List<org.lgna.project.ast.UserField> fieldsToRemove = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			addMembersAndRenames( fieldsToAdd, fieldsToRemove, renames, this.addFieldsComposite );
			return new org.alice.stageide.gallerybrowser.uri.merge.edits.ImportTypeEdit( completionStep, this.uriForDescriptionPurposesOnly, this.dstType, methodsToAdd, methodsToRemove, fieldsToAdd, fieldsToRemove, renames );
		} else {
			return null;
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		//todo: remove; icons and components rely on repaint being called
		this.getView().repaint();
		//

		StringBuffer sb = new StringBuffer();
		for( AddMembersComposite<?, ?> addMembersComposite : new AddMembersComposite[] { this.addProceduresComposite, this.addFunctionsComposite, this.addFieldsComposite } ) {
			addMembersComposite.appendStatusPreRejectorCheck( sb, step );
		}
		if( sb.length() > 0 ) {
			this.actionItemsRemainingError.setText( sb.toString() );
			return this.actionItemsRemainingError;
		} else {
			return IS_GOOD_TO_GO_STATUS;
		}
	}

	public org.lgna.croquet.PlainStringValue getDifferentImplementationsHeader() {
		return this.differentImplementationsHeader;
	}

	public org.lgna.croquet.PlainStringValue getDifferentImplementationsSubHeader() {
		return this.differentImplementationsSubHeader;
	}

	public org.lgna.croquet.Operation getAcceptAllDifferentImplementationsOperation() {
		return this.acceptAllDifferentImplementationsOperation;
	}

	public org.lgna.croquet.Operation getRejectAllDifferentImplementationsOperation() {
		return this.rejectAllDifferentImplementationsOperation;
	}

	private void applyAllDifferentImplementations( boolean isAccept ) {
		for( AddMembersComposite<?, ?> addMembersComposite : new AddMembersComposite[] { this.getAddProceduresComposite(), this.getAddFunctionsComposite(), this.getAddFieldsComposite() } ) {
			for( DifferentImplementation<?> differentImplementation : addMembersComposite.getDifferentImplementations() ) {
				differentImplementation.getIsAddDesiredState().setValueTransactionlessly( isAccept );
				differentImplementation.getIsKeepDesiredState().setValueTransactionlessly( isAccept == false );
			}
		}
	}

	private void acceptAllDifferentImplementations() {
		this.applyAllDifferentImplementations( true );
	}

	private void rejectAllDifferentImplementations() {
		this.applyAllDifferentImplementations( false );
	}

	public boolean isContainingDifferentImplementations() {
		for( AddMembersComposite<?, ?> addMembersComposite : new AddMembersComposite[] { this.getAddProceduresComposite(), this.getAddFunctionsComposite(), this.getAddFieldsComposite() } ) {
			if( addMembersComposite.getDifferentImplementations().size() > 0 ) {
				return true;
			}
		}
		return false;
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}

		new org.lgna.croquet.simple.SimpleApplication();

		java.io.File projectFile = new java.io.File( args[ 0 ] );

		org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject( projectFile );
		org.alice.ide.ProjectStack.pushProject( project );

		java.io.File typeFile = new java.io.File( args[ 1 ] );

		edu.cmu.cs.dennisc.pattern.Tuple2<org.lgna.project.ast.NamedUserType, java.util.Set<org.lgna.common.Resource>> tuple = org.lgna.project.io.IoUtilities.readType( typeFile );
		org.lgna.project.ast.NamedUserType importedRootType = tuple.getA();
		java.util.Set<org.lgna.common.Resource> importedResources = tuple.getB();
		org.lgna.project.ast.NamedUserType srcType = importedRootType;
		org.lgna.project.ast.NamedUserType dstType = MergeUtilities.findMatchingTypeInExistingTypes( srcType );
		if( dstType != null ) {
			//pass
		} else {
			dstType = new org.lgna.project.ast.NamedUserType();
			dstType.name.setValue( importedRootType.getName() );
		}
		ImportTypeComposite mergeTypeComposite = new ImportTypeComposite( typeFile.toURI(), importedRootType, importedResources, srcType, dstType );
		mergeTypeComposite.getOperation().fire();
		System.exit( 0 );
	}
}
