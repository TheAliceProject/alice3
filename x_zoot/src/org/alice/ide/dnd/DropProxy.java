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
package org.alice.ide.dnd;
/**
 * @author Dennis Cosgrove
 */
class DropProxy extends Proxy {
	public DropProxy( PotentiallyDraggableComponent potentiallyDraggableAffordance ) {
		super( potentiallyDraggableAffordance );
	}
	@Override
	protected int getProxyWidth() {
		return this.getPotentiallyDraggablePane().getDropWidth();
	}
	@Override
	protected int getProxyHeight() {
		return this.getPotentiallyDraggablePane().getDropHeight();
	}
	@Override
	protected float getAlpha() {
		return 0.75f;
	}
	@Override
	protected void paintProxy( java.awt.Graphics2D g2 ) {
		this.getPotentiallyDraggablePane().paintDrop( g2, this.isOverDropAcceptor(), this.isCopyDesired() );
	}
}
