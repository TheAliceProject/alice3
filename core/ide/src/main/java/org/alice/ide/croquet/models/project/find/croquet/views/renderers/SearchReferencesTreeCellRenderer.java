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
package org.alice.ide.croquet.models.project.find.croquet.views.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.alice.ide.croquet.models.project.find.croquet.tree.nodes.SearchTreeNode;
import org.alice.ide.x.PreviewAstI18nFactory;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.UserLambda;
import org.lgna.project.ast.UserMethod;

/**
 * @author Matt May
 */
public class SearchReferencesTreeCellRenderer extends DefaultTreeCellRenderer {
	@Override
	public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
		java.awt.Component rv = super.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
		assert value instanceof SearchTreeNode;
		SearchTreeNode node = (SearchTreeNode)value;
		if( node.getParent() != null ) {
			if( node.getIsLeaf() ) {
				Object astValue = node.getValue();
				assert astValue != null;
				assert astValue instanceof Expression : astValue.getClass();
				//note: creating component every time we render.  not as cell renderers are intended.
				rv = PreviewAstI18nFactory.getInstance().createComponent( (Expression)astValue ).getAwtComponent();
			} else {
				Object astValue = node.getValue();
				String nameValue = "";
				if( astValue instanceof UserMethod ) {
					nameValue = ( (UserMethod)astValue ).name.getValue();
				} else if( astValue instanceof UserLambda ) {
					nameValue = ( (UserLambda)astValue ).getFirstAncestorAssignableTo( MethodInvocation.class ).method.getValue().getName();
				} else {
					assert false : "unhandled AbstractDeclarationType: " + astValue.getClass();
				}
				assert rv instanceof JLabel;
				JLabel rvLabel = (JLabel)rv;
				rvLabel.setText( nameValue + " (" + node.getChildren().size() + ")" );
			}
		}
		return rv;
	}
}
