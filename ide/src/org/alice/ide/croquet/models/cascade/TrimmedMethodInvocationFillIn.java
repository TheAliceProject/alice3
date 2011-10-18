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

package org.alice.ide.croquet.models.cascade;

/**
 * @author Dennis Cosgrove
 */
@Deprecated
public class TrimmedMethodInvocationFillIn extends org.lgna.croquet.CascadeFillIn< org.lgna.project.ast.MethodInvocation, org.lgna.project.ast.Expression > {
	private final org.lgna.project.ast.MethodInvocation transientValue;
	public TrimmedMethodInvocationFillIn( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractMethod method ) {
		super( java.util.UUID.fromString( "4efeb642-4a7c-4de7-a736-9f8950f2836a" ) );
		this.transientValue = org.alice.ide.ast.IncompleteAstUtilities.createIncompleteMethodInvocation( expression, method );
		for( org.lgna.project.ast.AbstractParameter parameter : method.getRequiredParameters() ) {
			ParameterBlank parameterBlank = ParameterBlank.getInstance( parameter );
			this.addBlank( parameterBlank );
		}
	}
	private static final String MAC_LOOK_AND_FEEL_DESCRIPTION = "Aqua Look and Feel for Mac OS X";
	@Override
	public String getMenuItemText( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.MethodInvocation, org.lgna.project.ast.Expression > step ) {
		if( MAC_LOOK_AND_FEEL_DESCRIPTION.equals( javax.swing.UIManager.getLookAndFeel().getDescription() ) ) {
			return this.transientValue.method.getValue().getName();
		} else {
			return null;
		}
	}
	@Override
	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.cascade.ItemNode step ) {
		return null;
	}
	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.MethodInvocation, org.lgna.project.ast.Expression > step ) {
		throw new AssertionError();
//		javax.swing.JLabel label = new javax.swing.JLabel( this.transientValue.method.getValue().getName() );
//		javax.swing.JPanel panel = new javax.swing.JPanel();
//		panel.setLayout( new java.awt.BorderLayout() );
//		panel.add( label, java.awt.BorderLayout.LINE_END );
//		return label;
//		return null;
	}
	
	private org.lgna.project.ast.MethodInvocation createValue( org.lgna.project.ast.Expression[] expressions ) {
		return org.lgna.project.ast.AstUtilities.createMethodInvocation( this.transientValue.expression.getValue(), this.transientValue.method.getValue(), expressions );
	}
	@Override
	public org.lgna.project.ast.MethodInvocation createValue( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.MethodInvocation, org.lgna.project.ast.Expression > step ) {
		return this.createValue( this.createFromBlanks( step, org.lgna.project.ast.Expression.class ) );
	}
	@Override
	public org.lgna.project.ast.MethodInvocation getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.MethodInvocation, org.lgna.project.ast.Expression > step ) {
		return this.transientValue;
	}
}
