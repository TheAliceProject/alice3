/**
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
package org.alice.interact.handle;

import org.alice.interact.InputState;
import org.alice.interact.PickHint;
import org.alice.interact.manipulator.AbstractManipulator;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public interface ManipulationHandle extends Cloneable {
	
	public void setHandleManager(HandleManager handleManager);
	
	public HandleManager getHandleManager();
	
	public HandleSet getHandleSet();
	
	public boolean isAlwaysVisible();
	
	public void addToSet( HandleSet handleSet );
	
	public void addToGroup( HandleSet.HandleGroup group );
	
	public void addToGroups( HandleSet.HandleGroup...groups);
	
	public boolean isMemberOf( HandleSet set );
	
	public boolean isMemberOf( HandleSet.HandleGroup group );
	
	public Transformable getManipulatedObject();

	public void setSelectedObject( Transformable manipulatedObject );
	
	public void setAnimator( Animator animator );
	
	public void setHandleRollover(boolean rollover);
	
	public void setHandleVisible(boolean visible);
	
	public void setHandleActive(boolean active);
	
	public boolean isRenderable();
	
	public HandleState getHandleStateCopy();
	
	public ManipulationHandle clone();
	
	public AbstractManipulator getManipulation( InputState input );
	
	public void setManipulation( AbstractManipulator manipulation );
	
	public PickHint getPickHint();
}
