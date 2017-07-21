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

import org.alice.ide.ast.type.merge.help.diffimp.croquet.DifferentImplementationHelpComposite;
import org.alice.ide.ast.type.merge.help.diffimp.croquet.FieldDifferentImplementationHelpComposite;
import org.alice.ide.ast.type.merge.help.diffimp.croquet.MethodDifferentImplementationHelpComposite;

/**
 * @author Dennis Cosgrove
 */
public final class DifferentImplementation<M extends org.lgna.project.ast.Member> extends PotentialNameChanger<M> {
	private final MemberHubWithNameState<M> importHub;
	private final MemberHubWithNameState<M> projectHub;

	private final DifferentImplementationCardOwner importCardOwner;
	private final DifferentImplementationCardOwner projectCardOwner;

	private final DifferentImplementationHelpComposite<M> helpComposite;

	public DifferentImplementation( java.net.URI uriForDescriptionPurposesOnly, M importMember, M projectMember ) {
		super( uriForDescriptionPurposesOnly );
		this.importHub = new MemberHubWithNameState<M>( importMember, false ) {
			@Override
			public org.alice.ide.ast.type.merge.croquet.ActionStatus getActionStatus() {
				if( importHub.getIsDesiredState().getValue() ) {
					if( projectHub.getIsDesiredState().getValue() ) {
						if( isRenameRequired() ) {
							return ActionStatus.RENAME_REQUIRED;
						} else {
							return ActionStatus.ADD_AND_RENAME;
						}
					} else {
						return ActionStatus.REPLACE_OVER_ORIGINAL;
					}
				} else {
					if( projectHub.getIsDesiredState().getValue() ) {
						return ActionStatus.OMIT_IN_FAVOR_OF_ORIGINAL;
					} else {
						return ActionStatus.SELECTION_REQUIRED;
					}
				}
			}
		};

		this.projectHub = new MemberHubWithNameState<M>( projectMember, false ) {
			@Override
			public org.alice.ide.ast.type.merge.croquet.ActionStatus getActionStatus() {
				if( importHub.getIsDesiredState().getValue() ) {
					if( projectHub.getIsDesiredState().getValue() ) {
						if( isRenameRequired() ) {
							return ActionStatus.RENAME_REQUIRED;
						} else {
							return ActionStatus.KEEP_AND_RENAME;
						}
					} else {
						return ActionStatus.DELETE_IN_FAVOR_OF_REPLACEMENT;
					}
				} else {
					if( projectHub.getIsDesiredState().getValue() ) {
						return ActionStatus.KEEP_AND_RENAME;
					} else {
						return ActionStatus.SELECTION_REQUIRED;
					}
				}
			}
		};

		this.importHub.setOtherIsDesiredState( this.projectHub.getIsDesiredState() );
		this.projectHub.setOtherIsDesiredState( this.importHub.getIsDesiredState() );

		this.importCardOwner = new DifferentImplementationCardOwner.Builder( this )
				.neither( new ActionMustBeTakenCard( this ) )
				.replace( new ReplacePositiveImplementationCard( this ) )
				.rename( new RenameCard( this.importHub, this.getForegroundCustomizer() ) )
				.build();
		this.projectCardOwner = new DifferentImplementationCardOwner.Builder( this )
				.neither( new ActionMustBeTakenCard( this ) )
				.keep( new KeepImplementationCard( this ) )
				.replace( new ReplaceNegativeImplementationCard( this ) )
				.rename( new RenameCard( this.projectHub, this.getForegroundCustomizer() ) )
				.build();

		//todo
		if( importMember instanceof org.lgna.project.ast.UserMethod ) {
			this.helpComposite = (DifferentImplementationHelpComposite<M>)new MethodDifferentImplementationHelpComposite( (DifferentImplementation<org.lgna.project.ast.UserMethod>)this );
		} else if( importMember instanceof org.lgna.project.ast.UserField ) {
			this.helpComposite = (DifferentImplementationHelpComposite<M>)new FieldDifferentImplementationHelpComposite( (DifferentImplementation<org.lgna.project.ast.UserField>)this );
		} else {
			//todo
			this.helpComposite = null;
		}
	}

	@Override
	public MemberHubWithNameState<M> getImportHub() {
		return this.importHub;
	}

	@Override
	public MemberHubWithNameState<M> getProjectHub() {
		return this.projectHub;
	}

	public DifferentImplementationCardOwner getImportCardOwner() {
		return this.importCardOwner;
	}

	public DifferentImplementationCardOwner getProjectCardOwner() {
		return this.projectCardOwner;
	}

	public DifferentImplementationHelpComposite<M> getHelpComposite() {
		return this.helpComposite;
	}

	@Override
	protected boolean isRenameRequired() {
		if( this.importHub.getIsDesiredState().getValue() ) {
			if( this.projectHub.getIsDesiredState().getValue() ) {
				//todo
				return this.projectHub.getNameState().getValue().contentEquals( this.importHub.getNameState().getValue() );
			}
		}
		return false;
	}

	public void appendStatusPreRejectorCheck( StringBuffer sb, org.lgna.croquet.history.CompletionStep<?> step ) {
		boolean isAddDesired = this.importHub.getIsDesiredState().getValue();
		boolean isKeepDesired = this.projectHub.getIsDesiredState().getValue();
		if( isAddDesired ) {
			if( isKeepDesired ) {
				if( this.isRenameRequired() ) {
					sb.append( "must not have same name: \"" );
					sb.append( this.getImportHub().getMember().getName() );
					sb.append( "\"." );
				} else {
					//pass
				}
			} else {
				//pass
			}
		} else {
			if( isKeepDesired ) {
				//pass
			} else {
				sb.append( "must take action on \"" );
				sb.append( this.getImportHub().getMember().getName() );
				sb.append( "\" (replace, keep, or add-and-keep-with-rename)." );
			}
		}
	}
}
