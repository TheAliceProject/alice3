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

class StatementTemplateDragAndDropOperation extends org.alice.ide.operations.AbstractDragAndDropOperation {
	@Override
	protected zoot.ActionOperation createDropOperation() {
		return new zoot.AbstractActionOperation() {
			public void perform( zoot.ActionContext actionContext ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo" );
			}
		};
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class StatementTemplate extends org.alice.ide.ast.StatementLikeSubstance {
	public StatementTemplate( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls ) {
		super( cls );
	}
	protected zoot.DragAndDropOperation dragAndDropOperation;
	@Override
	protected boolean isPressed() {
		return false;
	}
	//	protected zoot.ActionOperation createPopupOperation() {
	//		return new zoot.AbstractActionOperation() {
	//			public void perform( zoot.ActionContext actionContext ) {
	//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle popupOperation" );
	//			}
	//		};
	//	}
	protected zoot.DragAndDropOperation createDragAndDropOperation() {
		return new StatementTemplateDragAndDropOperation();
	}
	@Override
	public void addNotify() {
		if( this.dragAndDropOperation != null ) {
			//pass
		} else {
			this.dragAndDropOperation = this.createDragAndDropOperation();
		}
		this.setDragAndDropOperation( this.dragAndDropOperation );
		super.addNotify();
	}
	@Override
	public void removeNotify() {
		super.removeNotify();
		this.setPopupOperation( null );
	}

	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType[] getBlankExpressionTypes();
	protected edu.cmu.cs.dennisc.alice.ast.Expression[] promptUserForExpressions( final zoot.event.DragAndDropEvent e, final edu.cmu.cs.dennisc.alice.ast.AbstractType... types ) {
		edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] > taskObserver = new edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] >() {
			@Override
			public void run() {
				getIDE().promptUserForExpressions( types, e.getEndingMouseEvent(), this );
			}
		};
		return taskObserver.getResult();
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.Statement createStatement( edu.cmu.cs.dennisc.alice.ast.Expression... expressions );
	public final void createStatement( zoot.event.DragAndDropEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement > taskObserver ) { 
		edu.cmu.cs.dennisc.alice.ast.AbstractType[] types = getBlankExpressionTypes();
		if( types != null && types.length > 0 ) {
			edu.cmu.cs.dennisc.alice.ast.Expression[] expressions = promptUserForExpressions( e, types );
			if( expressions != null ) {
				taskObserver.handleCompletion( createStatement( expressions ) );
			} else {
				taskObserver.handleCancelation();
			}
		} else {
			taskObserver.handleCompletion( createStatement() );
		}
	}
}
