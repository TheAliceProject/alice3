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
package org.alice.ide.memberseditor.templates;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionStatementTemplate extends org.alice.ide.templates.CascadingExpressionsStatementTemplate {
	private edu.cmu.cs.dennisc.alice.ast.Expression incompleteExpression;
	private java.awt.Component incompleteExpressionPane;
	public ExpressionStatementTemplate( edu.cmu.cs.dennisc.alice.ast.Expression incompleteExpression ) {
		super( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class );
		this.incompleteExpression = incompleteExpression;
	}
	@Override
	public void addNotify() {
		if( this.incompleteExpressionPane != null ) {
			//pass
		} else {
			this.incompleteExpressionPane = getIDE().getTemplatesFactory().createComponent( this.incompleteExpression );
			this.add( this.incompleteExpressionPane );
			this.revalidate();
		}
		super.addNotify();
	}
	
	protected abstract edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions );
	@Override
	protected final edu.cmu.cs.dennisc.alice.ast.Statement createStatement( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		edu.cmu.cs.dennisc.alice.ast.Expression expression = this.createExpression( expressions );
		if( expression != null ) {
			return new edu.cmu.cs.dennisc.alice.ast.ExpressionStatement( createExpression( expressions ) );
		} else {
			return null;
		}
	}
	
}
