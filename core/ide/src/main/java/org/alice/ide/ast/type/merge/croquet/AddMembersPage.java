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
package org.alice.ide.ast.type.merge.croquet;

import org.alice.ide.ast.type.merge.core.MergeUtilities;

/**
 * @author Dennis Cosgrove
 */
public class AddMembersPage extends org.lgna.croquet.WizardPageComposite<org.lgna.croquet.views.Panel, org.alice.ide.ast.type.croquet.ImportTypeWizard> {

	public static String modifyFilenameLocalizedText( String s, java.net.URI uri ) {
		java.io.File file = new java.io.File( uri );
		return s.replaceAll( "</filename/>", file.getName() );
	}

	private final java.net.URI uriForDescriptionPurposesOnly;

	private final org.lgna.project.ast.NamedUserType importedRootType;
	private final java.util.Set<org.lgna.common.Resource> importedResources;

	private final org.lgna.project.ast.NamedUserType srcType;
	private final org.lgna.project.ast.NamedUserType dstType;

	private final ProceduresToolPalette addProceduresComposite;
	private final FunctionsToolPalette addFunctionsComposite;
	private final FieldsToolPalette addFieldsComposite;

	private final ErrorStatus actionItemsRemainingError = this.createErrorStatus( "actionItemsRemainingError" );

	private final org.lgna.croquet.Operation acceptAllDifferentImplementationsOperation = this.createActionOperation( "acceptAllDifferentImplementationsOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			acceptAllDifferentImplementations();
			return null;
		}
	} );
	private final org.lgna.croquet.Operation rejectAllDifferentImplementationsOperation = this.createActionOperation( "rejectAllDifferentImplementationsOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			rejectAllDifferentImplementations();
			return null;
		}
	} );

	private final org.lgna.croquet.PlainStringValue differentImplementationsHeader = this.createStringValue( "differentImplementationsHeader" );
	private final org.lgna.croquet.PlainStringValue differentImplementationsSubHeader = this.createStringValue( "differentImplementationsSubHeader" );

	private boolean isManagementLevelAppropriate( org.lgna.project.ast.UserMethod method ) {
		org.lgna.project.ast.ManagementLevel managementLevel = method.getManagementLevel();
		return ( managementLevel == null ) || ( managementLevel == org.lgna.project.ast.ManagementLevel.NONE );
	}

	public AddMembersPage( org.alice.ide.ast.type.croquet.ImportTypeWizard wizard, java.net.URI uriForDescriptionPurposesOnly, org.lgna.project.ast.NamedUserType importedRootType, java.util.Set<org.lgna.common.Resource> importedResources, org.lgna.project.ast.NamedUserType srcType, org.lgna.project.ast.NamedUserType dstType ) {
		super( java.util.UUID.fromString( "d00d925e-0a2c-46c7-b6c8-0d3d1189bc5c" ), wizard );
		this.uriForDescriptionPurposesOnly = uriForDescriptionPurposesOnly;
		this.importedRootType = importedRootType;
		this.importedResources = importedResources;
		this.srcType = srcType;
		this.dstType = dstType;

		if( this.dstType != null ) {
			java.util.List<org.lgna.project.ast.UserMethod> projectProcedures = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			java.util.List<org.lgna.project.ast.UserMethod> projectFunctions = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			for( org.lgna.project.ast.UserMethod projectMethod : this.dstType.methods ) {
				if( isManagementLevelAppropriate( projectMethod ) ) {
					if( projectMethod.isProcedure() ) {
						projectProcedures.add( projectMethod );
					} else {
						projectFunctions.add( projectMethod );
					}
				}
			}
			this.addProceduresComposite = this.registerSubComposite( new ProceduresToolPalette( this.uriForDescriptionPurposesOnly, projectProcedures ) );
			this.addFunctionsComposite = this.registerSubComposite( new FunctionsToolPalette( this.uriForDescriptionPurposesOnly, projectFunctions ) );

			java.util.List<org.lgna.project.ast.UserField> dstFields = this.dstType.getDeclaredFields();
			java.util.List<org.lgna.project.ast.UserField> projectFields = edu.cmu.cs.dennisc.java.util.Lists.newArrayListWithInitialCapacity( dstFields.size() );
			projectFields.addAll( dstFields );
			this.addFieldsComposite = this.registerSubComposite( new FieldsToolPalette( this.uriForDescriptionPurposesOnly, projectFields ) );

			for( org.lgna.project.ast.UserMethod importMethod : this.srcType.methods ) {
				if( isManagementLevelAppropriate( importMethod ) ) {
					MethodsToolPalette<?> addMethodsComposite = importMethod.isProcedure() ? this.addProceduresComposite : this.addFunctionsComposite;
					org.lgna.project.ast.UserMethod projectMethod = MergeUtilities.findMethodWithMatchingName( importMethod, dstType );
					if( projectMethod != null ) {
						if( MergeUtilities.isEquivalent( importMethod, projectMethod ) ) {
							addMethodsComposite.addIdenticalMembers( importMethod, projectMethod );
						} else {
							if( MergeUtilities.isHeaderEquivalent( importMethod, projectMethod ) ) {
								addMethodsComposite.addDifferentImplementationMembers( importMethod, projectMethod );
							} else {
								addMethodsComposite.addDifferentSignatureMembers( importMethod, projectMethod );
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
					if( MergeUtilities.isEquivalent( importField, projectField ) ) {
						this.addFieldsComposite.addIdenticalMembers( importField, projectField );
					} else {
						if( MergeUtilities.isValueTypeEquivalent( importField, projectField ) ) {
							this.addFieldsComposite.addDifferentImplementationMembers( importField, projectField );
						} else {
							this.addFieldsComposite.addDifferentSignatureMembers( importField, projectField );
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

	@Override
	protected org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired() {
		return null;
	}

	public ProceduresToolPalette getAddProceduresComposite() {
		return this.addProceduresComposite;
	}

	public FunctionsToolPalette getAddFunctionsComposite() {
		return this.addFunctionsComposite;
	}

	public FieldsToolPalette getAddFieldsComposite() {
		return this.addFieldsComposite;
	}

	@Override
	protected org.alice.ide.ast.type.merge.croquet.views.AddMembersPane createView() {
		return new org.alice.ide.ast.type.merge.croquet.views.AddMembersPane( this );
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

	private void addRenameIfNecessary( java.util.List<org.alice.ide.ast.type.merge.croquet.edits.RenameMemberData> renames, MemberNameState<? extends org.lgna.project.ast.Member> nameState, org.lgna.project.ast.Member member ) {
		String nextName = nameState.getValue();
		if( nextName.contentEquals( member.getName() ) ) {
			//pass
		} else {
			renames.add( new org.alice.ide.ast.type.merge.croquet.edits.RenameMemberData( member, nextName ) );
		}
	}

	private void addRenameIfNecessary( java.util.List<org.alice.ide.ast.type.merge.croquet.edits.RenameMemberData> renames, MemberNameState<? extends org.lgna.project.ast.Member> nameState ) {
		addRenameIfNecessary( renames, nameState, nameState.getMember() );
	}

	private <M extends org.lgna.project.ast.Member> M createImportCopy( M original ) {
		return org.lgna.project.ast.AstUtilities.createCopy( original, this.importedRootType );
	}

	private <M extends org.lgna.project.ast.Member> void addMembersAndRenames( java.util.List<M> membersToAdd, java.util.List<M> membersToRemove, java.util.List<org.alice.ide.ast.type.merge.croquet.edits.RenameMemberData> renames, MembersToolPalette<?, M> addMembersComposite ) {
		for( ImportOnly<M> importOnly : addMembersComposite.getImportOnlys() ) {
			if( importOnly.getImportHub().getIsDesiredState().getValue() ) {
				membersToAdd.add( this.createImportCopy( importOnly.getImportHub().getMember() ) );
			}
		}

		for( DifferentSignature<M> differentSignature : addMembersComposite.getDifferentSignatures() ) {
			if( differentSignature.getImportHub().getIsDesiredState().getValue() ) {
				M member = this.createImportCopy( differentSignature.getImportHub().getMember() );
				membersToAdd.add( member );
				addRenameIfNecessary( renames, differentSignature.getImportHub().getNameState(), member );
				addRenameIfNecessary( renames, differentSignature.getProjectHub().getNameState() );
			}
		}

		for( DifferentImplementation<M> differentImplementation : addMembersComposite.getDifferentImplementations() ) {
			if( differentImplementation.getImportHub().getIsDesiredState().getValue() ) {
				M member = this.createImportCopy( differentImplementation.getImportHub().getMember() );
				membersToAdd.add( member );
				if( differentImplementation.getProjectHub().getIsDesiredState().getValue() ) {
					addRenameIfNecessary( renames, differentImplementation.getImportHub().getNameState(), member );
					addRenameIfNecessary( renames, differentImplementation.getProjectHub().getNameState() );
				} else {
					membersToRemove.add( differentImplementation.getProjectHub().getMember() );
				}
			} else {
				if( differentImplementation.getProjectHub().getIsDesiredState().getValue() ) {
					//pass
				} else {
					//should not happen
				}
			}
		}
	}

	public org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		if( this.dstType != null ) {
			java.util.List<org.alice.ide.ast.type.merge.croquet.edits.RenameMemberData> renames = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

			java.util.List<org.lgna.project.ast.UserMethod> methodsToAdd = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			java.util.List<org.lgna.project.ast.UserMethod> methodsToRemove = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			for( MethodsToolPalette<?> addMethodsComposite : new MethodsToolPalette[] { this.addProceduresComposite, this.addFunctionsComposite } ) {
				addMembersAndRenames( methodsToAdd, methodsToRemove, renames, addMethodsComposite );
			}

			java.util.List<org.lgna.project.ast.UserField> fieldsToAdd = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			java.util.List<org.lgna.project.ast.UserField> fieldsToRemove = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			addMembersAndRenames( fieldsToAdd, fieldsToRemove, renames, this.addFieldsComposite );
			return new org.alice.ide.ast.type.merge.croquet.edits.ImportTypeEdit( completionStep, this.uriForDescriptionPurposesOnly, this.dstType, methodsToAdd, methodsToRemove, fieldsToAdd, fieldsToRemove, renames );
		} else {
			return null;
		}
	}

	private <M extends org.lgna.project.ast.Member> java.util.List<MemberHub<M>> getPreviewMemberHubs( MembersToolPalette<?, M> addMembersComposite ) {
		if( this.dstType != null ) {
			boolean isIncludingAll = getOwner().getPreviewPage().getIsIncludingAllState().getValue();
			java.util.List<MemberHub<M>> hubs = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			for( ImportOnly<M> importOnly : addMembersComposite.getImportOnlys() ) {
				if( isIncludingAll || importOnly.getImportHub().getIsDesiredState().getValue() ) {
					hubs.add( importOnly.getImportHub() );
				}
			}

			for( DifferentSignature<M> differentSignature : addMembersComposite.getDifferentSignatures() ) {
				if( isIncludingAll || differentSignature.getImportHub().getIsDesiredState().getValue() ) {
					hubs.add( differentSignature.getImportHub() );
				}
				hubs.add( differentSignature.getProjectHub() );
			}

			for( DifferentImplementation<M> differentImplementation : addMembersComposite.getDifferentImplementations() ) {
				if( isIncludingAll || differentImplementation.getImportHub().getIsDesiredState().getValue() ) {
					hubs.add( differentImplementation.getImportHub() );
				}
				if( isIncludingAll || differentImplementation.getProjectHub().getIsDesiredState().getValue() ) {
					hubs.add( differentImplementation.getProjectHub() );
				}
			}

			for( Identical<M> identical : addMembersComposite.getIdenticals() ) {
				hubs.add( identical.getProjectHub() );
			}

			for( ProjectOnly<M> projectOnly : addMembersComposite.getProjectOnlys() ) {
				hubs.add( projectOnly.getProjectHub() );
			}

			return hubs;
		} else {
			return java.util.Collections.emptyList();
		}
	}

	public java.util.List<MemberHub<org.lgna.project.ast.UserMethod>> getPreviewProcedureHubs() {
		return getPreviewMemberHubs( this.addProceduresComposite );
	}

	public java.util.List<MemberHub<org.lgna.project.ast.UserMethod>> getPreviewFunctionHubs() {
		return getPreviewMemberHubs( this.addFunctionsComposite );
	}

	public java.util.List<MemberHub<org.lgna.project.ast.UserField>> getPreviewFieldHubs() {
		return getPreviewMemberHubs( this.addFieldsComposite );
	}

	@Override
	public Status getPageStatus( org.lgna.croquet.history.CompletionStep<?> step ) {
		//todo: remove; icons and components rely on repaint being called
		this.getView().repaint();
		//

		StringBuffer sb = new StringBuffer();
		for( MembersToolPalette<?, ?> addMembersComposite : new MembersToolPalette[] { this.addProceduresComposite, this.addFunctionsComposite, this.addFieldsComposite } ) {
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
		for( MembersToolPalette<?, ?> addMembersComposite : new MembersToolPalette[] { this.getAddProceduresComposite(), this.getAddFunctionsComposite(), this.getAddFieldsComposite() } ) {
			for( DifferentImplementation<?> differentImplementation : addMembersComposite.getDifferentImplementations() ) {
				differentImplementation.getImportHub().getIsDesiredState().setValueTransactionlessly( isAccept );
				differentImplementation.getProjectHub().getIsDesiredState().setValueTransactionlessly( isAccept == false );
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
		for( MembersToolPalette<?, ?> addMembersComposite : new MembersToolPalette[] { this.getAddProceduresComposite(), this.getAddFunctionsComposite(), this.getAddFieldsComposite() } ) {
			if( addMembersComposite.getDifferentImplementations().size() > 0 ) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void resetData() {
	}
}
