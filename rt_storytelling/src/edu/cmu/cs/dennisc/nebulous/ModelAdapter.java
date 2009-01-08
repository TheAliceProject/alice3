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
package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public class ModelAdapter< E extends Model > extends edu.cmu.cs.dennisc.lookingglass.opengl.GeometryAdapter< E > {
	@Override
	protected boolean isDisplayListDesired() {
		return false;
	}
	@Override
	public boolean isAlphaBlended() {
		//todo
		return false;
	}
	@Override
	protected void pickGeometry( edu.cmu.cs.dennisc.lookingglass.opengl.PickContext pc, boolean isSubElementRequired ) {
		m_element.pick();
	}
	@Override
	protected void renderGeometry( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc ) {
		m_element.render();
	}
}
