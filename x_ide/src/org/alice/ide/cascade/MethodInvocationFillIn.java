/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.cascade;

/**
 * @author Dennis Cosgrove
 */
public class MethodInvocationFillIn extends cascade.FillIn< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > {
	private edu.cmu.cs.dennisc.alice.ast.Expression expression;
	private edu.cmu.cs.dennisc.alice.ast.AbstractMethod method;
//	private String description;
	public MethodInvocationFillIn( edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
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
	protected javax.swing.JComponent createMenuProxy() {
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation incompleteMethodInvocation = org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( this.expression, this.method );
		return (javax.swing.JComponent)org.alice.ide.IDE.getSingleton().getPreviewFactory().createExpressionPane( incompleteMethodInvocation );
	}
	
	@Override
	public edu.cmu.cs.dennisc.alice.ast.MethodInvocation getValue() {
		java.util.List< cascade.Node > nodes = this.getChildren();
		final int N = nodes.size();
		edu.cmu.cs.dennisc.alice.ast.Expression[] argumentExpressions = new edu.cmu.cs.dennisc.alice.ast.Expression[ N ];
		
		for( int i=0; i<N; i++ ) {
			argumentExpressions[ i ] = (edu.cmu.cs.dennisc.alice.ast.Expression)((cascade.Blank)nodes.get( i )).getSelectedFillIn().getValue();
		}
		
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation rv = org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( null, this.method );
		org.alice.ide.ast.NodeUtilities.completeMethodInvocation( rv, this.expression, argumentExpressions );
		return rv;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.MethodInvocation getTransientValue() {
		java.util.List< cascade.Node > nodes = this.getChildren();
		final int N = nodes.size();
		edu.cmu.cs.dennisc.alice.ast.Expression[] argumentExpressions = new edu.cmu.cs.dennisc.alice.ast.Expression[ N ];
		
		for( int i=0; i<N; i++ ) {
			argumentExpressions[ i ] = (edu.cmu.cs.dennisc.alice.ast.Expression)((cascade.Blank)nodes.get( i )).getSelectedFillIn().getTransientValue();
		}
		
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation rv = org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( null, this.method );
		org.alice.ide.ast.NodeUtilities.completeMethodInvocation( rv, this.expression, argumentExpressions );
		return rv;
	}
	
}
