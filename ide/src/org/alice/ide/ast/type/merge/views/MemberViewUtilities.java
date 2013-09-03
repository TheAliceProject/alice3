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
package org.alice.ide.ast.type.merge.views;

/**
 * @author Dennis Cosgrove
 */
public class MemberViewUtilities {
	public static java.awt.Color ACTION_MUST_BE_TAKEN_COLOR = new java.awt.Color( 170, 0, 0 );

	//	public static org.alice.stageide.gallerybrowser.uri.merge.views.icons.ActionStatusIcon KEEP_ICON = new org.alice.stageide.gallerybrowser.uri.merge.views.icons.ActionStatusIcon() {
	//		@Override
	//		protected ActionStatus getActionStatus() {
	//			return ActionStatus.KEEP;
	//		}
	//	};

	//
	//	private static javax.swing.Icon DELETE_ICON = new org.alice.stageide.gallerybrowser.uri.merge.views.icons.ActionStatusIcon() {
	//		@Override
	//		protected ActionStatus getActionStatus() {
	//			return ActionStatus.DELETE;
	//		}
	//	};

	private static org.lgna.croquet.components.AbstractLabel createMemberLabel( org.lgna.project.ast.Member member, String prefix, String postfix, javax.swing.Icon icon ) {
		return new org.lgna.croquet.components.Label( "<html>" + prefix + member.getName() + postfix + "</html>", icon );
	}

	public static org.lgna.croquet.components.AbstractLabel createAddMemberLabel( org.lgna.project.ast.Member member ) {
		return createMemberLabel( member, "", "", null );
	}

	public static org.lgna.croquet.components.AbstractLabel createDeleteMemberLabel( org.lgna.project.ast.Member member ) {
		org.lgna.croquet.components.AbstractLabel rv = createMemberLabel( member, "<strike>", "</strike>", null );
		rv.getAwtComponent().setEnabled( false );
		return rv;
	}

	public static org.lgna.croquet.components.AbstractLabel createReplaceMemberLabel( org.lgna.project.ast.Member member ) {
		return createMemberLabel( member, "", " <em>(replace with version from class file)</em>", null );
	}

	public static org.lgna.croquet.components.AbstractLabel createKeepInsteadOfReplaceMemberLabel( org.lgna.project.ast.Member member ) {
		return createMemberLabel( member, "", " <em>(keep version already in project)</em>", null );
	}

	public static org.lgna.croquet.components.AbstractLabel createKeepIdenticalMemberLabel( org.lgna.project.ast.Member member ) {
		return createMemberLabel( member, "", " <em>(identical)</em>", null );
	}

	public static org.lgna.croquet.components.AbstractLabel createKeepUniqueMemberLabel( org.lgna.project.ast.Member member ) {
		return createMemberLabel( member, "", "", null );
	}

	public static org.lgna.croquet.components.AbstractLabel createActionMustBeTakeMemberLabel( org.lgna.project.ast.Member member ) {
		org.lgna.croquet.components.AbstractLabel rv = createMemberLabel( member, "", "", null );
		rv.setForegroundColor( ACTION_MUST_BE_TAKEN_COLOR );
		return rv;
	}

	public static org.lgna.croquet.components.TextField createTextField( org.lgna.croquet.StringState state, edu.cmu.cs.dennisc.javax.swing.ColorCustomizer foregroundCustomizer ) {
		org.lgna.croquet.components.TextField rv = state.createTextField();
		rv.enableSelectAllWhenFocusGained();
		rv.getAwtComponent().setForegroundCustomizer( foregroundCustomizer );
		rv.getAwtComponent().setColumns( 24 );
		return rv;
	}

	//	public static <M extends org.lgna.project.ast.Member> org.lgna.croquet.components.HoverPopupView createPopupView( org.alice.stageide.gallerybrowser.uri.merge.AddMembersComposite<?, M> composite, M member, javax.swing.Icon icon ) {
	//		org.lgna.croquet.components.HoverPopupView rv = composite.getPopupMemberFor( member ).getHoverPopupElement().createHoverPopupView();
	//		rv.getAwtComponent().setIcon( icon );
	//		return rv;
	//	}
	public static org.lgna.croquet.components.HoverPopupView createPopupView( org.alice.ide.ast.type.merge.MemberPopupCoreComposite popup ) {
		org.lgna.croquet.components.HoverPopupView rv = popup.getHoverPopupElement().createHoverPopupView();
		rv.getAwtComponent().setIcon( popup.getIcon() );
		return rv;
	}
}
