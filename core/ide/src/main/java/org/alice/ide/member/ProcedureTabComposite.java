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
package org.alice.ide.member;

/**
 * @author Dennis Cosgrove
 */
public final class ProcedureTabComposite extends MemberTabComposite<org.alice.ide.member.views.ProcedureTabView> {
	private final org.lgna.croquet.ImmutableDataSingleSelectListState<String> sortState = this.createImmutableListState( "sortState", String.class, org.alice.ide.croquet.codecs.StringCodec.SINGLETON, 0, this.findLocalizedText( GROUP_BY_CATEGORY_KEY ), this.findLocalizedText( SORT_ALPHABETICALLY_KEY ) );

	public ProcedureTabComposite() {
		super( java.util.UUID.fromString( "cdc6fb94-34ef-4992-b3d0-2ad90bd0179c" ), org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ? null : new AddProcedureMenuModel() );
	}

	@Override
	public org.lgna.croquet.ImmutableDataSingleSelectListState<String> getSortState() {
		return this.sortState;
	}

	@Override
	protected org.alice.ide.member.views.ProcedureTabView createView() {
		return new org.alice.ide.member.views.ProcedureTabView( this );
	}

	@Override
	protected org.alice.ide.member.UserMethodsSubComposite getUserMethodsSubComposite( org.lgna.project.ast.NamedUserType type ) {
		return UserProceduresSubComposite.getInstance( type );
	}

	@Override
	protected boolean isAcceptable( org.lgna.project.ast.AbstractMethod method ) {
		return method.isProcedure();
	}

	@Override
	protected java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> getPotentialCategorySubComposites() {
		return org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().getCategoryProcedureSubComposites();
	}

	@Override
	protected java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> getPotentialCategoryOrAlphabeticalSubComposites() {
		return org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().getCategoryOrAlphabeticalProcedureSubComposites();
	}

	@Override
	protected UnclaimedJavaMethodsComposite getUnclaimedJavaMethodsComposite() {
		return UnclaimedJavaProceduresComposite.getInstance();
	}
}
