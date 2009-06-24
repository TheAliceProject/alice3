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
package edu.cmu.cs.dennisc.alice.ide.editors.ubiquitous;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionRequiringUbiquitousStatementTemplatePane extends UbiquitousStatementTemplatePane {
	public ExpressionRequiringUbiquitousStatementTemplatePane( /*Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls,*/ edu.cmu.cs.dennisc.alice.ast.Statement emptyStatement ) {
		super( /*cls, */emptyStatement );
	}
	protected abstract void promptUserForExpression( edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver, edu.cmu.cs.dennisc.alice.ide.editors.common.DropAndDropEvent e );
	
	protected abstract edu.cmu.cs.dennisc.alice.ast.Statement createUbiquitousStatement( edu.cmu.cs.dennisc.alice.ast.Expression expression );
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Statement createStatement( final edu.cmu.cs.dennisc.alice.ide.editors.common.DropAndDropEvent e ) {
		assert javax.swing.SwingUtilities.isEventDispatchThread() == false;
		edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver = new edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression >(){
			@Override
			public void run() {
				ExpressionRequiringUbiquitousStatementTemplatePane.this.promptUserForExpression( this, e );
			}
		};
		edu.cmu.cs.dennisc.alice.ast.Expression expression = taskObserver.getResult();
		if( expression != null ) {
			return this.createUbiquitousStatement( expression );
		} else {
			return null;
		}
	}
}
