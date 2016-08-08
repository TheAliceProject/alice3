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

package org.alice.ide.croquet.models.cascade;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionFillIn<F extends org.lgna.project.ast.Expression, B> extends org.lgna.croquet.ImmutableCascadeFillIn<F, B> {
	private String text;

	public ExpressionFillIn( java.util.UUID id, org.lgna.croquet.CascadeBlank<B>... blanks ) {
		super( id, blanks );
	}

	@Override
	protected void localize() {
		super.localize();
		this.text = this.findDefaultLocalizedText();
	}

	protected javax.swing.Icon getLeadingIcon( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> step ) {
		return null;
	}

	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> step ) {
		org.lgna.project.ast.Expression expression = this.getTransientValue( step );

		javax.swing.Icon leadingIcon = this.getLeadingIcon( step );
		javax.swing.JLabel trailingLabel;
		if( ( this.text != null ) && ( this.text.length() > 0 ) ) {
			trailingLabel = edu.cmu.cs.dennisc.javax.swing.LabelUtilities.createLabel( this.text, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT );
		} else {
			trailingLabel = null;
		}
		javax.swing.JComponent expressionPane = org.alice.ide.x.PreviewAstI18nFactory.getInstance().createExpressionPane( expression ).getAwtComponent();
		if( ( leadingIcon != null ) || ( trailingLabel != null ) ) {
			edu.cmu.cs.dennisc.javax.swing.components.JLineAxisPane rv = new edu.cmu.cs.dennisc.javax.swing.components.JLineAxisPane();
			if( leadingIcon != null ) {
				rv.add( new javax.swing.JLabel( leadingIcon ) );
			}
			rv.add( expressionPane );
			if( trailingLabel != null ) {
				trailingLabel.setBorder( new edu.cmu.cs.dennisc.javax.swing.border.EmptyBorder( 0, 16, 0, 0 ) );
				rv.add( trailingLabel );
			}
			return rv;
		} else {
			return expressionPane;
		}
	}
	//	@Override
	//	public final javax.swing.Icon getMenuItemIcon( org.lgna.croquet.cascade.ItemNode< ? super F, B > step ) {
	//		return super.getMenuItemIcon( step );
	//	}
	//	@Override
	//	public final String getMenuItemText( org.lgna.croquet.cascade.ItemNode< ? super F, B > step ) {
	//		return super.getMenuItemText( step );
	//	}
}
