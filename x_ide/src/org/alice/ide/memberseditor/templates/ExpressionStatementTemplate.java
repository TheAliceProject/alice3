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
	public ExpressionStatementTemplate() {
		super( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class );
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions );
	protected abstract edu.cmu.cs.dennisc.alice.ast.Expression createIncompleteExpression();
	@Override
	public void addNotify() {
		super.addNotify();
		this.refresh();
	}
	@Override
	public void removeNotify() {
		this.removeAll();
		super.removeNotify();
	}
	protected void refresh() {
		this.removeAll();
		edu.cmu.cs.dennisc.alice.ast.Expression incompleteExpression = this.createIncompleteExpression();
		this.setBackground( getIDE().getColorFor( incompleteExpression ) );
		this.add( getIDE().getTemplatesFactory().createExpressionPane( incompleteExpression ) );
	}

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
