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
package edu.cmu.cs.dennisc.alice.ide.editors.type;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProcedureTemplatePane extends MemberStatementTemplatePane {
	public ProcedureTemplatePane( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		super( method );
		assert method.isProcedure();
		Class< ? extends edu.cmu.cs.dennisc.alice.ast.Node > cls = edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class;
		this.setBackground( edu.cmu.cs.dennisc.alice.ide.IDE.getColorForASTClass( cls ) );
		this.add( this.getInstanceOrTypeExpressionPane() );
		this.add( javax.swing.Box.createHorizontalStrut( 4 ) );
		this.add( this.getNameLabel() );
		boolean isJava = "java".equals( edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton().getLocale().getVariant() );
		boolean isCommaDesired = false;
		if( isJava ) {
			this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( "( " ) );
		}
		for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : method.getParameters() ) {
			if( isCommaDesired ) {
				this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( ", " ) );
			}
			this.add( javax.swing.Box.createHorizontalStrut( 4 ) );
			//this.add( new edu.cmu.cs.dennisc.alice.ide.editors.code.EmptyExpressionPane( parameter.getValueType() ) );
			this.add( new EmptyParameterPane( parameter ) );
			if( isJava ) {
				isCommaDesired = true;
			}
		}
		if( isJava ) {
			this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( " )" ) );
		}
		this.add( javax.swing.Box.createHorizontalGlue() );
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractMethod getMethod() {
		return (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)this.getMember();
	}

	
	protected abstract void promptUserForMethodInvocation( edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > taskObserver, edu.cmu.cs.dennisc.alice.ide.editors.common.DropAndDropEvent e );
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Statement createStatement( final edu.cmu.cs.dennisc.alice.ide.editors.common.DropAndDropEvent e ) {
		assert javax.swing.SwingUtilities.isEventDispatchThread() == false;
		edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > taskObserver = new edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.MethodInvocation >() {
			@Override
			public void run() {
				ProcedureTemplatePane.this.promptUserForMethodInvocation( getIDE().createInstanceExpression(), this, e );
			}
		};
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = taskObserver.getResult();
		edu.cmu.cs.dennisc.alice.ast.ExpressionStatement rv;
		if( methodInvocation != null ) {
			rv = new edu.cmu.cs.dennisc.alice.ast.ExpressionStatement( methodInvocation );
		} else {
			rv = null;
		}
		return rv;
	}
}
