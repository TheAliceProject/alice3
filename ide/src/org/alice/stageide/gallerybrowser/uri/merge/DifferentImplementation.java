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
	private final MemberHub<M> importHub;
	private final MemberHub<M> projectHub;

	private final MemberNameState<M> importNameState;
	private final MemberNameState<M> projectNameState;

	private final DifferentImplementationCardOwnerComposite importCardOwnerComposite;
	private final DifferentImplementationCardOwnerComposite projectCardOwnerComposite;

	public DifferentImplementation( M importMember, M projectMember ) {
		final String POSTFIX = "<br><em>(different implementation)</em>";
		this.importHub = new MemberHub<M>( importMember, false, "replace/add  ", POSTFIX ) {
			@Override
			public org.alice.stageide.gallerybrowser.uri.merge.ActionStatus getActionStatus() {
				if( isActionRequired() ) {
					return ActionStatus.ERROR;
				} else {
					if( getIsAddDesiredState().getValue() ) {
						if( getIsKeepDesiredState().getValue() ) {
							return ActionStatus.ADD;
						} else {
							return ActionStatus.REPLACE;
						}
					} else {
						return ActionStatus.IGNORE;
					}
				}
			}
		};

		this.projectHub = new MemberHub<M>( projectMember, false, "keep ", POSTFIX ) {
			@Override
			public org.alice.stageide.gallerybrowser.uri.merge.ActionStatus getActionStatus() {
				if( isActionRequired() ) {
					return ActionStatus.ERROR;
				} else {
					if( getIsDesiredState().getValue() ) {
						return ActionStatus.KEEP;
					} else {
						return ActionStatus.IGNORE;
					}
				}
			}
		};

		this.projectNameState = new MemberNameState<M>( projectMember );
		this.importNameState = new MemberNameState<M>( importMember );

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

	public MemberNameState<M> getImportNameState() {
		return this.importNameState;
	}

	public MemberNameState<M> getProjectNameState() {
		return this.projectNameState;
	}

	public DifferentImplementationCardOwnerComposite getImportCardOwnerComposite() {
		return this.importCardOwnerComposite;
	}

	public DifferentImplementationCardOwnerComposite getProjectCardOwnerComposite() {
		return this.projectCardOwnerComposite;
	}

	public M getImportMember() {
		return this.importNameState.getMember();
	}

	public M getProjectMember() {
		return this.projectNameState.getMember();
	}

	public MemberPopupCoreComposite getImportPopup() {
		return this.importHub.getPopup();
	}

	public MemberPopupCoreComposite getProjectPopup() {
		return this.projectHub.getPopup();
	}

	@Override
	public boolean isActionRequired() {
		boolean isAddDesired = this.getIsAddDesiredState().getValue();
		boolean isKeepDesired = this.getIsKeepDesiredState().getValue();
		if( isAddDesired ) {
			if( isKeepDesired ) {
				//todo
				if( this.projectNameState.getValue().contentEquals( this.importNameState.getValue() ) ) {
					return true;
				}
			} else {
				//pass
			}
		} else {
			if( isKeepDesired ) {
				//pass
			} else {
				return true;
			}
		}
		return false;
	}

	public void appendStatusPreRejectorCheck( StringBuffer sb, org.lgna.croquet.history.CompletionStep<?> step ) {
		boolean isAddDesired = this.getIsAddDesiredState().getValue();
		boolean isKeepDesired = this.getIsKeepDesiredState().getValue();
		if( isAddDesired ) {
			if( isKeepDesired ) {
				//todo
				if( this.projectNameState.getValue().contentEquals( this.importNameState.getValue() ) ) {
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
