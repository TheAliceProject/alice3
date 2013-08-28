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
public final class DifferentImplementation<M extends org.lgna.project.ast.Member> {
	private final IsMemberDesiredState<M> isAddDesiredState;
	private final IsMemberDesiredState<M> isKeepDesiredState;
	private final MemberNameState<M> importNameState;
	private final MemberNameState<M> projectNameState;

	private final DifferentImplementationCardOwnerComposite importCardOwnerComposite;
	private final DifferentImplementationCardOwnerComposite projectCardOwnerComposite;

	private final ReplaceImplementationCard replaceImplementationCard = new ReplaceImplementationCard( this );
	private final KeepImplementationCard keepImplementationCard = new KeepImplementationCard( this );
	private final AddAndRenameImplementationCard addAndRenameImplementationsCard = new AddAndRenameImplementationCard( this );
	private final KeepAndRenameImplementationCard keepAndRenameImplementationsCard = new KeepAndRenameImplementationCard( this );

	private static final String POST_FIX = "<br><em>(different implementation)</em>";

	private final javax.swing.Icon importIcon = new org.alice.stageide.gallerybrowser.uri.merge.views.icons.ActionStatusIcon() {
		@Override
		protected ActionStatus getActionStatus() {
			if( isActionRequired() ) {
				return ActionStatus.ERROR;
			} else {
				if( isAddDesiredState.getValue() ) {
					if( isKeepDesiredState.getValue() ) {
						return ActionStatus.ADD;
					} else {
						return ActionStatus.REPLACE;
					}
				} else {
					return ActionStatus.NO_ACTION;
				}
			}
		}
	};
	private final javax.swing.Icon projectIcon = new org.alice.stageide.gallerybrowser.uri.merge.views.icons.ActionStatusIcon() {
		@Override
		protected ActionStatus getActionStatus() {
			if( isActionRequired() ) {
				return ActionStatus.ERROR;
			} else {
				if( isKeepDesiredState.getValue() ) {
					return ActionStatus.KEEP;
				} else {
					return ActionStatus.NO_ACTION;
				}
			}
		}
	};

	public DifferentImplementation( M projectMember, M importMember ) {
		this.isAddDesiredState = new IsMemberDesiredState<M>( importMember, false, "replace/add ", POST_FIX );
		this.isKeepDesiredState = new IsMemberDesiredState<M>( projectMember, false, "keep ", POST_FIX );
		this.projectNameState = new MemberNameState<M>( projectMember );
		this.importNameState = new MemberNameState<M>( importMember );

		this.importCardOwnerComposite = new DifferentImplementationCardOwnerComposite.Builder( this )
				.neither( new ActionMustBeTakenCard( this ) )
				.replace( this.replaceImplementationCard )
				.rename( this.addAndRenameImplementationsCard )
				.build();
		this.projectCardOwnerComposite = new DifferentImplementationCardOwnerComposite.Builder( this )
				.neither( new ActionMustBeTakenCard( this ) )
				.keep( this.keepImplementationCard )
				.rename( this.keepAndRenameImplementationsCard )
				.build();
	}

	public IsMemberDesiredState<M> getIsAddDesiredState() {
		return this.isAddDesiredState;
	}

	public IsMemberDesiredState<M> getIsKeepDesiredState() {
		return this.isKeepDesiredState;
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

	public javax.swing.Icon getImportIcon() {
		return this.importIcon;
	}

	public javax.swing.Icon getProjectIcon() {
		return this.projectIcon;
	}

	public boolean isActionRequired() {
		if( this.isAddDesiredState.getValue() ) {
			if( this.isKeepDesiredState.getValue() ) {
				//todo
				if( this.projectNameState.getValue().contentEquals( this.importNameState.getValue() ) ) {
					return true;
				}
			} else {
				//pass
			}
		} else {
			if( this.isKeepDesiredState.getValue() ) {
				//pass
			} else {
				return true;
			}
		}
		return false;
	}

	public void appendStatusPreRejectorCheck( StringBuffer sb, org.lgna.croquet.history.CompletionStep<?> step ) {
		if( this.isAddDesiredState.getValue() ) {
			if( this.isKeepDesiredState.getValue() ) {
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
			if( this.isKeepDesiredState.getValue() ) {
				//pass
			} else {
				sb.append( "must take action on \"" );
				sb.append( this.getImportMember().getName() );
				sb.append( "\"." );
			}
		}
	}
}
