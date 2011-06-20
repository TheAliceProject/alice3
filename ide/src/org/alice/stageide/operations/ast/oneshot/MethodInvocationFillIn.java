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

package org.alice.stageide.operations.ast.oneshot;

/**
 * @author Dennis Cosgrove
 */
public abstract class MethodInvocationFillIn extends org.lgna.croquet.CascadeFillIn< MethodInvocationEditFactory, edu.cmu.cs.dennisc.alice.ast.Expression > {
	private final edu.cmu.cs.dennisc.alice.ast.MethodInvocation transientValue;
	public MethodInvocationFillIn( java.util.UUID id, edu.cmu.cs.dennisc.alice.ast.AbstractField field, edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		super( id );
		this.transientValue = org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( method );
		this.transientValue.expression.setValue( new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), field ) );
		for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : method.getParameters() ) {
			this.addBlank( org.alice.ide.croquet.models.cascade.ParameterBlank.getInstance( parameter ) );
		}
	}
	private edu.cmu.cs.dennisc.alice.ast.AbstractMethod getMethod() {
		return this.transientValue.method.getValue();
	}
	private edu.cmu.cs.dennisc.alice.ast.AbstractField getField() {
		return ((edu.cmu.cs.dennisc.alice.ast.FieldAccess)this.transientValue.expression.getValue()).field.getValue();
	}
	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode< ? super MethodInvocationEditFactory, edu.cmu.cs.dennisc.alice.ast.Expression > itemNode ) {
		return org.alice.ide.IDE.getSingleton().getPreviewFactory().createStatementPane( new edu.cmu.cs.dennisc.alice.ast.ExpressionStatement( this.transientValue ) ).getAwtComponent();
	}
	protected abstract MethodInvocationEditFactory createMethodInvocationEditFactory( edu.cmu.cs.dennisc.alice.ast.AbstractField field, edu.cmu.cs.dennisc.alice.ast.AbstractMethod method, edu.cmu.cs.dennisc.alice.ast.Expression[] argumentExpressions );
	@Override
	public MethodInvocationEditFactory createValue( org.lgna.croquet.cascade.ItemNode< ? super MethodInvocationEditFactory, edu.cmu.cs.dennisc.alice.ast.Expression > itemNode ) {
		edu.cmu.cs.dennisc.alice.ast.Expression[] argumentExpressions = this.createFromBlanks( itemNode, edu.cmu.cs.dennisc.alice.ast.Expression.class );
		return this.createMethodInvocationEditFactory( this.getField(), this.getMethod(), argumentExpressions );
	}
	@Override
	public MethodInvocationEditFactory getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super MethodInvocationEditFactory, edu.cmu.cs.dennisc.alice.ast.Expression > itemNode ) {
		return null;
	}
	@Override
	protected String getTutorialItemText() {
		return this.transientValue.method.getValue().getName();
	}
}
