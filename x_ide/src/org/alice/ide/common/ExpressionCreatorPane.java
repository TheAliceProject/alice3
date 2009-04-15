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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionCreatorPane extends org.alice.ide.common.ExpressionLikeSubstance {
//	@Override
//	protected boolean isActuallyPotentiallyActive() {
//		return getIDE().isDragInProgress() == false;
//	}
//	@Override
//	protected boolean isActuallyPotentiallySelectable() {
//		return false;
//	}
//	@Override
//	protected boolean isActuallyPotentiallyDraggable() {
//		return true;
//	}
	public ExpressionCreatorPane() {
		this.setCursor( java.awt.Cursor.getPredefinedCursor( java.awt.Cursor.HAND_CURSOR ) );
	}
	
	@Override
	public void setActive( boolean isActive ) {
		super.setActive( isActive );
		if( isActive ) {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle AccessiblePane setActive " );
			getIDE().showStencilOver( this, getExpressionType() );
		} else {
			getIDE().hideStencil();
		}
	}
	
	public abstract void createExpression( final zoot.event.DragAndDropEvent e, final edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, final edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver );
	@Override
	protected boolean isKnurlDesired() {
		return true;
	}
}
