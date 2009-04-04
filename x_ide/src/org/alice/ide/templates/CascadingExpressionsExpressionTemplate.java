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
package org.alice.ide.templates;

/**
 * @author Dennis Cosgrove
 */
public abstract class CascadingExpressionsExpressionTemplate extends ExpressionTemplate {
	public CascadingExpressionsExpressionTemplate( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		super( expression );
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType[] getBlankExpressionTypes();
	protected abstract edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions );
	@Override
	public final void createExpression( final zoot.event.DragAndDropEvent e, final edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, final edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
		final edu.cmu.cs.dennisc.alice.ast.AbstractType[] types = getBlankExpressionTypes();
		if( types != null && types.length > 0 ) {
			class Worker extends org.jdesktop.swingworker.SwingWorker< edu.cmu.cs.dennisc.alice.ast.Expression[], Void > {
				@Override
				protected edu.cmu.cs.dennisc.alice.ast.Expression[] doInBackground() throws java.lang.Exception {
					edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] > expressionsTaskObserver = new edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] >() {
						@Override
						public void run() {
							getIDE().promptUserForExpressions( types, e.getEndingMouseEvent(), this );
						}
					};
					return expressionsTaskObserver.getResult();
				}
				@Override
				protected void done() {
					edu.cmu.cs.dennisc.alice.ast.Expression[] expressions = null;
					try {
						expressions = this.get();
					} catch( InterruptedException ie ) {
						throw new RuntimeException( ie );
					} catch( java.util.concurrent.ExecutionException ee ) {
						throw new RuntimeException( ee );
					} finally {
						if( expressions != null ) {
							taskObserver.handleCompletion( createExpression( expressions ) );
						} else {
							taskObserver.handleCancelation();
						}
					}
				}
			}
			Worker worker = new Worker();
			worker.execute();
		} else {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					edu.cmu.cs.dennisc.alice.ast.Expression expression = createExpression();
					taskObserver.handleCompletion( expression );
				}
			} );
		}
	}
}
