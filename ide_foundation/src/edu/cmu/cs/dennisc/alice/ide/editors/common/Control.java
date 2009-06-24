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
package edu.cmu.cs.dennisc.alice.ide.editors.common;

/**
 * @author Dennis Cosgrove
 */
public abstract class Control<E> extends edu.cmu.cs.dennisc.moot.ZRenderedControl<E> {
	public Control( int axis ) {
		super( axis );
	}
	protected edu.cmu.cs.dennisc.alice.ide.IDE getIDE() {
		return edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton();
	}
	//protected abstract edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape();
	protected void fillBounds( java.awt.Graphics2D g2 ) {
		edu.cmu.cs.dennisc.moot.Renderer renderer = this.getRenderer();
		if( renderer!=null ) {
			renderer.fillBounds( this.getContext(), this, g2, 0, 0, getWidth(), getHeight() );
		}
	}
	@Override
	protected boolean isActuallyPotentiallyActive() {
		return super.isActuallyPotentiallyActive() && getIDE().isDragInProgress()==false;
	}
//	@Override
//	protected boolean isActuallyPotentiallySelectable() {
//		return super.isActuallyPotentiallySelectable() && getIDE().isDragInProgress()==false;
//	}
}
