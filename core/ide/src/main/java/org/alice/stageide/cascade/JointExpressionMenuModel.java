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

package org.alice.stageide.cascade;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.x.PreviewAstI18nFactory;
import org.alice.stageide.ast.JointedTypeInfo;
import org.alice.stageide.joint.JointsSubMenu;
import org.alice.stageide.joint.JointsSubMenuManager;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeItemMenuCombo;
import org.lgna.croquet.CascadeMenuModel;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.MethodInvocation;

import javax.swing.JComponent;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class JointExpressionMenuModel extends CascadeMenuModel<Expression> {
	private final Expression expression;
	private final List<JointedTypeInfo> jointedTypeInfos;
	private final int index;
	private final boolean isOwnedByCascadeItemMenuCombo;

	public JointExpressionMenuModel( Expression expression, List<JointedTypeInfo> jointedTypeInfos, int index, boolean isOwnedByCascadeItemMenuCombo ) {
		super( UUID.fromString( "c70ca3a5-b1e0-4ed9-8648-14acd52a4091" ) );
		this.expression = expression;
		this.jointedTypeInfos = jointedTypeInfos;
		this.index = index;
		this.isOwnedByCascadeItemMenuCombo = isOwnedByCascadeItemMenuCombo;
	}

	@Override
	protected JComponent getMenuProxy( ItemNode<? super Expression, Expression> node ) {
		if( this.isOwnedByCascadeItemMenuCombo ) {
			return super.getMenuProxy( node );
		} else {
			JComponent expressionPane = PreviewAstI18nFactory.getInstance().createExpressionPane( this.expression ).getAwtComponent();
			return expressionPane;
		}
	}

	@Override
	protected final void updateBlankChildren( List<CascadeBlankChild> blankChildren, BlankNode<Expression> blankNode ) {
		JointedTypeInfo info = jointedTypeInfos.get( this.index );
		JointedModelTypeSeparator separator = JointedModelTypeSeparator.getInstance( info.getType() );
		CascadeBlankChild child;
		if( jointedTypeInfos.size() > ( this.index + 1 ) ) {
			child = new CascadeItemMenuCombo( separator, new JointExpressionMenuModel( this.expression, this.jointedTypeInfos, this.index + 1, true ) );
		} else {
			child = separator;
		}
		blankChildren.add( child );

		JointsSubMenu<MethodInvocation>[] subMenus = JointsSubMenuManager.getSubMenusForType( info.getType() );

		for( AbstractMethod method : info.getJointGetters() ) {
			JointExpressionFillIn fillIn = JointExpressionFillIn.getInstance( expression, method );
			if( fillIn != null ) {
				boolean isConsumed = false;
				for( JointsSubMenu<MethodInvocation> subMenu : subMenus ) {
					if( subMenu.consumeIfAppropriate( method, fillIn ) ) {
						isConsumed = true;
						break;
					}
				}
				if( isConsumed ) {
					//pass
				} else {
					blankChildren.add( fillIn );
				}
			} else {
				Logger.info( "no fillIn for", method );
			}
		}
		Collections.addAll( blankChildren, subMenus );
	}
}
