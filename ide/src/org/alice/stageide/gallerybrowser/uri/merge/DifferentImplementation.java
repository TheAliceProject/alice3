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
public final class DifferentImplementation<M extends org.lgna.project.ast.Member> extends PotentialNameChanger {
	private final MemberHubWithNameState<M> importHub;
	private final MemberHubWithNameState<M> projectHub;

	private final DifferentImplementationCardOwnerComposite importCardOwnerComposite;
	private final DifferentImplementationCardOwnerComposite projectCardOwnerComposite;

	public DifferentImplementation( M importMember, M projectMember ) {
		final String POSTFIX = "<br><em>(different implementation)</em>";
		this.importHub = new MemberHubWithNameState<M>( importMember, false, "replace/add  ", POSTFIX ) {
			@Override
			public org.alice.stageide.gallerybrowser.uri.merge.ActionStatus getActionStatus() {
				if( getIsAddDesiredState().getValue() ) {
					if( getIsKeepDesiredState().getValue() ) {
						if( isRenameRequired() ) {
							return ActionStatus.RENAME_REQUIRED;
						} else {
							return ActionStatus.ADD_AND_RENAME;
						}
					} else {
						return ActionStatus.REPLACE_OVER_ORIGINAL;
					}
				} else {
					if( getIsKeepDesiredState().getValue() ) {
						return ActionStatus.OMIT_IN_FAVOR_OF_ORIGINAL;
					} else {
						return ActionStatus.SELECTION_REQUIRED;
					}
				}
			}
		};

		this.projectHub = new MemberHubWithNameState<M>( projectMember, false, "keep ", POSTFIX ) {
			@Override
			public org.alice.stageide.gallerybrowser.uri.merge.ActionStatus getActionStatus() {
				if( getIsAddDesiredState().getValue() ) {
					if( getIsKeepDesiredState().getValue() ) {
						if( isRenameRequired() ) {
							return ActionStatus.RENAME_REQUIRED;
						} else {
							return ActionStatus.KEEP_AND_RENAME;
						}
					} else {
						return ActionStatus.DELETE_IN_FAVOR_OF_REPLACEMENT;
					}
				} else {
					if( getIsKeepDesiredState().getValue() ) {
						return ActionStatus.KEEP_AND_RENAME;
					} else {
						return ActionStatus.SELECTION_REQUIRED;
					}
				}
			}
		};

		this.importCardOwnerComposite = new DifferentImplementationCardOwnerComposite.Builder( this )
				.neither( new ActionMustBeTakenCard( this ) )
				.replace( new ReplacePositiveImplementationCard( this ) )
				.rename( new AddAndRenameImplementationCard( this ) )
				.build();
		this.projectCardOwnerComposite = new DifferentImplementationCardOwnerComposite.Builder( this )
				.neither( new ActionMustBeTakenCard( this ) )
				.keep( new KeepImplementationCard( this ) )
				.replace( new ReplaceNegativeImplementationCard( this ) )
				.rename( new KeepAndRenameImplementationCard( this ) )
				.build();
	}

	public IsMemberDesiredState<M> getIsAddDesiredState() {
		return this.importHub.getIsDesiredState();
	}

	public IsMemberDesiredState<M> getIsKeepDesiredState() {
		return this.projectHub.getIsDesiredState();
	}

	public DifferentImplementationCardOwnerComposite getImportCardOwnerComposite() {
		return this.importCardOwnerComposite;
	}

	public DifferentImplementationCardOwnerComposite getProjectCardOwnerComposite() {
		return this.projectCardOwnerComposite;
	}

	public MemberNameState<M> getImportNameState() {
		return this.importHub.getNameState();
	}

	public MemberNameState<M> getProjectNameState() {
		return this.projectHub.getNameState();
	}

	public M getImportMember() {
		return this.importHub.getMember();
	}

	public M getProjectMember() {
		return this.projectHub.getMember();
	}

	public MemberPopupCoreComposite getImportPopup() {
		return this.importHub.getPopup();
	}

	public MemberPopupCoreComposite getProjectPopup() {
		return this.projectHub.getPopup();
	}

	@Override
	protected boolean isRenameRequired() {
		if( this.getIsAddDesiredState().getValue() ) {
			if( this.getIsKeepDesiredState().getValue() ) {
				//todo
				return this.getProjectNameState().getValue().contentEquals( this.getImportNameState().getValue() );
			}
		}
		return false;
	}

	public void appendStatusPreRejectorCheck( StringBuffer sb, org.lgna.croquet.history.CompletionStep<?> step ) {
		boolean isAddDesired = this.getIsAddDesiredState().getValue();
		boolean isKeepDesired = this.getIsKeepDesiredState().getValue();
		if( isAddDesired ) {
			if( isKeepDesired ) {
				if( this.isRenameRequired() ) {
					sb.append( "must not have same name: \"" );
					sb.append( this.getImportMember().getName() );
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
				sb.append( this.getImportMember().getName() );
				sb.append( "\" (replace, keep, or add-and-keep-with-rename)." );
			}
		}
	}
}
