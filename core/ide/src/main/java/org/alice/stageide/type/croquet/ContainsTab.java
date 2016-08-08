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
package org.alice.stageide.type.croquet;

/**
 * @author Dennis Cosgrove
 */
public class ContainsTab extends org.lgna.croquet.SimpleTabComposite<org.lgna.croquet.views.Panel> {
	private final org.lgna.croquet.StringState filterState = this.createStringState( "filterState" );
	private final org.alice.stageide.type.croquet.data.MemberListData memberListData = new org.alice.stageide.type.croquet.data.MemberListData( this.filterState );
	private final org.lgna.croquet.RefreshableDataSingleSelectListState<org.lgna.project.ast.Member> memberListState = this.createRefreshableListState( "memberListState", this.memberListData, -1 );

	private final org.lgna.croquet.event.ValueListener<org.lgna.project.ast.Member> memberListener = new org.lgna.croquet.event.ValueListener<org.lgna.project.ast.Member>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.lgna.project.ast.Member> e ) {
			org.lgna.project.ast.Member nextValue = e.getNextValue();
			if( nextValue != null ) {
				dialog.getTypeTreeState().setValueTransactionlessly( dialog.getTypeNodeFor( nextValue.getDeclaringType() ) );
			}
		}
	};

	private final OtherTypeDialog dialog;

	public ContainsTab( OtherTypeDialog dialog ) {
		super( java.util.UUID.fromString( "f002ad52-4053-4f30-8460-752ad7394044" ), IsCloseable.FALSE );
		this.dialog = dialog;
	}

	public org.lgna.croquet.StringState getFilterState() {
		return this.filterState;
	}

	public org.alice.stageide.type.croquet.data.MemberListData getMemberListData() {
		return this.memberListData;
	}

	public org.lgna.croquet.RefreshableDataSingleSelectListState<org.lgna.project.ast.Member> getMemberListState() {
		return this.memberListState;
	}

	@Override
	protected org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired() {
		return null;
	}

	@Override
	protected org.lgna.croquet.views.Panel createView() {
		return new org.alice.stageide.type.croquet.views.ContainsTabPane( this );
	}

	@Override
	public void handlePreActivation() {
		this.memberListState.addAndInvokeNewSchoolValueListener( this.memberListener );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
		this.memberListState.removeNewSchoolValueListener( this.memberListener );
	}
}
