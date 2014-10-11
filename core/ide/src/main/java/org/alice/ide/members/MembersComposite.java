/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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

package org.alice.ide.members;

/**
 * @author Dennis Cosgrove
 */
public class MembersComposite extends org.lgna.croquet.SimpleComposite<org.alice.ide.members.components.MembersView> {
	private static class SingletonHolder {
		private static MembersComposite instance = new MembersComposite();
	}

	public static MembersComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final org.alice.ide.member.ProcedureTabComposite procedureTabComposite = new org.alice.ide.member.ProcedureTabComposite();
	private final org.alice.ide.member.FunctionTabComposite functionTabComposite = new org.alice.ide.member.FunctionTabComposite();
	private final org.alice.ide.member.ControlFlowTabComposite controlStructureTabComposite;
	private final org.lgna.croquet.ImmutableDataTabState<org.alice.ide.member.MemberOrControlFlowTabComposite<?>> tabState;

	private MembersComposite() {
		super( java.util.UUID.fromString( "10225a3f-f05d-42f3-baaf-f6bd0f8a7c68" ) );
		org.alice.ide.member.MemberOrControlFlowTabComposite<?>[] tabComposites;
		if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
			this.controlStructureTabComposite = null;
			tabComposites = new org.alice.ide.member.MemberOrControlFlowTabComposite[] { this.procedureTabComposite, this.functionTabComposite };
		} else {
			this.controlStructureTabComposite = new org.alice.ide.member.ControlFlowTabComposite();
			tabComposites = new org.alice.ide.member.MemberOrControlFlowTabComposite[] { this.procedureTabComposite, this.functionTabComposite, this.controlStructureTabComposite };
		}
		this.tabState = (org.lgna.croquet.ImmutableDataTabState)this.createImmutableTabState( "tabState", 0, org.alice.ide.member.MemberOrControlFlowTabComposite.class, tabComposites );
	}

	public org.lgna.croquet.ImmutableDataTabState<org.alice.ide.member.MemberOrControlFlowTabComposite<?>> getTabState() {
		return this.tabState;
	}

	public org.alice.ide.member.ProcedureTabComposite getProcedureTabComposite() {
		return this.procedureTabComposite;
	}

	public org.alice.ide.member.FunctionTabComposite getFunctionTabComposite() {
		return this.functionTabComposite;
	}

	public org.alice.ide.member.ControlFlowTabComposite getControlStructureTabComposite() {
		return this.controlStructureTabComposite;
	}

	@Override
	protected org.alice.ide.members.components.MembersView createView() {
		return new org.alice.ide.members.components.MembersView( this );
	}
}
