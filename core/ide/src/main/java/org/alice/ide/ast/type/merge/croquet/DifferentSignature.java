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

import org.alice.ide.ast.type.merge.help.diffsig.croquet.DifferentSignatureHelpComposite;
import org.alice.ide.ast.type.merge.help.diffsig.croquet.FieldDifferentSignatureHelpComposite;
import org.alice.ide.ast.type.merge.help.diffsig.croquet.MethodDifferentSignatureHelpComposite;

/**
 * @author Dennis Cosgrove
 */
public final class DifferentSignature<M extends org.lgna.project.ast.Member> extends PotentialNameChanger<M> {
	private static final String METHOD_POST_FIX = "<br><em>(different signature)</em>";
	private static final String FIELD_POST_FIX = "<br><em>(different value class)</em>";
	private final MemberHubWithNameState<M> importHub;
	private final MemberHubWithNameState<M> projectHub;
	private final ProjectDifferentSignatureCardOwner projectCardOwner;
	private final DifferentSignatureHelpComposite<M> helpComposite;

	public DifferentSignature( java.net.URI uriForDescriptionPurposesOnly, M importMember, M projectMember ) {
		super( uriForDescriptionPurposesOnly );
		this.importHub = new MemberHubWithNameState<M>( importMember, true ) {
			@Override
			public org.alice.ide.ast.type.merge.croquet.ActionStatus getActionStatus() {
				if( importHub.getIsDesiredState().getValue() ) {
					if( isRenameRequired() ) {
						return ActionStatus.RENAME_REQUIRED;
					} else {
						return ActionStatus.ADD_AND_RENAME;
					}
				} else {
					return ActionStatus.OMIT;
				}
			}
		};

		this.projectHub = new MemberHubWithNameState<M>( projectMember, true ) {
			@Override
			public org.alice.ide.ast.type.merge.croquet.ActionStatus getActionStatus() {
				if( importHub.getIsDesiredState().getValue() ) {
					if( isRenameRequired() ) {
						return ActionStatus.RENAME_REQUIRED;
					} else {
						return ActionStatus.KEEP_AND_RENAME;
					}
				} else {
					return ActionStatus.KEEP_OVER_DIFFERENT_SIGNATURE;
				}
			}
		};

		this.importHub.setOtherIsDesiredState( this.projectHub.getIsDesiredState() );
		this.projectHub.setOtherIsDesiredState( this.importHub.getIsDesiredState() );

		this.projectHub.getIsDesiredState().setEnabled( false );
		this.projectCardOwner = new ProjectDifferentSignatureCardOwner( this );

		//todo
		if( importMember instanceof org.lgna.project.ast.UserMethod ) {
			this.helpComposite = (DifferentSignatureHelpComposite<M>)new MethodDifferentSignatureHelpComposite( (DifferentSignature<org.lgna.project.ast.UserMethod>)this );
		} else if( importMember instanceof org.lgna.project.ast.UserField ) {
			this.helpComposite = (DifferentSignatureHelpComposite<M>)new FieldDifferentSignatureHelpComposite( (DifferentSignature<org.lgna.project.ast.UserField>)this );
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

	public ProjectDifferentSignatureCardOwner getProjectCardOwner() {
		return this.projectCardOwner;
	}

	public DifferentSignatureHelpComposite<M> getHelpComposite() {
		return this.helpComposite;
	}

	@Override
	protected boolean isRenameRequired() {
		if( this.importHub.getIsDesiredState().getValue() ) {
			//todo
			return this.projectHub.getNameState().getValue().contentEquals( this.importHub.getNameState().getValue() );
		}
		return false;
	}

	public void appendStatusPreRejectorCheck( StringBuffer sb, org.lgna.croquet.history.CompletionStep<?> step ) {
		if( this.isRenameRequired() ) {
			sb.append( "must not have same name: \"" );
			sb.append( this.importHub.getMember().getName() );
			sb.append( "\"." );
		}
	}
}
