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
package org.alice.ide.cascade;

/**
 * @author Dennis Cosgrove
 */
public class IncompleteMethodInvocationFillIn extends IncompleteExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > {
	private edu.cmu.cs.dennisc.alice.ast.Expression expression;
	private edu.cmu.cs.dennisc.alice.ast.AbstractMethod method;
//	private String description;
	public IncompleteMethodInvocationFillIn( edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		assert expression != null;
		assert method != null;
		this.expression = expression;
		this.method = method;
	}
//	public String getDescription() {
//		return this.description;
//	}
//	public void setDescription( String description ) {
//		this.description = description;
//	}
	@Override
	protected void addChildren() {
		for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : this.method.getParameters() ) {
			this.addBlank( new ParameterBlank( parameter ) );
		}
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.MethodInvocation createIncomplete() {
		return org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( this.expression, this.method );
	}
	
	@Override
	public edu.cmu.cs.dennisc.alice.ast.MethodInvocation getValue() {
		java.util.List< edu.cmu.cs.dennisc.cascade.Node > nodes = this.getChildren();
		final int N = nodes.size();
		edu.cmu.cs.dennisc.alice.ast.Expression[] argumentExpressions = new edu.cmu.cs.dennisc.alice.ast.Expression[ N ];
		
		for( int i=0; i<N; i++ ) {
			argumentExpressions[ i ] = (edu.cmu.cs.dennisc.alice.ast.Expression)((edu.cmu.cs.dennisc.cascade.Blank)nodes.get( i )).getSelectedFillIn().getValue();
		}
		
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation rv = org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( null, this.method );
		org.alice.ide.ast.NodeUtilities.completeMethodInvocation( rv, this.expression, argumentExpressions );
		return rv;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.MethodInvocation getTransientValue() {
		java.util.List< edu.cmu.cs.dennisc.cascade.Node > nodes = this.getChildren();
		final int N = nodes.size();
		edu.cmu.cs.dennisc.alice.ast.Expression[] argumentExpressions = new edu.cmu.cs.dennisc.alice.ast.Expression[ N ];
		
		for( int i=0; i<N; i++ ) {
			argumentExpressions[ i ] = (edu.cmu.cs.dennisc.alice.ast.Expression)((edu.cmu.cs.dennisc.cascade.Blank)nodes.get( i )).getSelectedFillIn().getTransientValue();
		}
		
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation rv = org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( null, this.method );
		org.alice.ide.ast.NodeUtilities.completeMethodInvocation( rv, this.expression, argumentExpressions );
		return rv;
	}
	
}
