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

package org.alice.apis.moveandturn;

import edu.cmu.cs.dennisc.alice.annotations.*; 

/**
 * @author Dennis Cosgrove
 */
@ClassTemplate(isFollowToSuperClassDesired = true, isConsumptionBySubClassDesired=true)
public abstract class AbstractModel extends Transformable {
	private edu.cmu.cs.dennisc.math.Matrix3x3 m_originalScale = edu.cmu.cs.dennisc.math.Matrix3x3.createIdentity();
	protected abstract edu.cmu.cs.dennisc.scenegraph.Visual getSGVisual();
	@Override
	protected void realize() {
		m_originalScale.setValue( getSGVisual().scale.getValue() );
		super.realize();
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.math.Matrix3x3 getOriginalScale() { 
		return m_originalScale;
	}
	
}
