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
package org.alice.ide.operations.run;

/**
 * @author Dennis Cosgrove
 */
public class PreviewMethodOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	private org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate procedureInvocationTemplate;
	public PreviewMethodOperation( org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate procedureInvocationTemplate ) {
		this.putValue( javax.swing.Action.NAME, "Preview..." );
		this.procedureInvocationTemplate = procedureInvocationTemplate;
	}
	@Override
	protected void performInternal( final edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: PreviewMethodOperation" );
		java.awt.event.MouseEvent mouseEvent = new java.awt.event.MouseEvent( this.procedureInvocationTemplate, 0, 0, 0, this.procedureInvocationTemplate.getWidth(), this.procedureInvocationTemplate.getHeight(), 0, false );
		edu.cmu.cs.dennisc.zoot.event.DragAndDropEvent dragAndDropEvent = new edu.cmu.cs.dennisc.zoot.event.DragAndDropEvent( this.procedureInvocationTemplate, null, mouseEvent );
		this.procedureInvocationTemplate.createStatement( dragAndDropEvent, null, new edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement >() {
			public void handleCompletion( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
				edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( statement, edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class );
				edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( expressionStatement.expression.getValue(), edu.cmu.cs.dennisc.alice.ast.MethodInvocation.class );
				methodInvocation.expression.setValue( null );
				PreviewMethodOperation.this.getIDE().handlePreviewMethod( actionContext, methodInvocation );
				actionContext.commitAndInvokeRedoIfAppropriate();
			}
			public void handleCancelation() {
				actionContext.cancel();
			}
		} );
	}
}
