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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public abstract class AccessiblePane extends org.alice.ide.ast.ExpressionLikeSubstance {
	@Override
	protected boolean isActuallyPotentiallyActive() {
		return getIDE().isDragInProgress() == false;
	}
	@Override
	protected boolean isActuallyPotentiallySelectable() {
		return false;
	}
	@Override
	protected boolean isActuallyPotentiallyDraggable() {
		return true;
	}
	
	@Override
	public void setActive( boolean isActive ) {
		super.setActive( isActive );
		if( isActive ) {
			getIDE().showStencilOver( this, getExpressionType() );
		} else {
			getIDE().hideStencil();
		}
	}
	
	public abstract edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.ide.ast.DropAndDropEvent e );
}
