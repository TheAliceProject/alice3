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
package org.alice.interact.manipulator;

import org.alice.interact.PickHint;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.LinearScaleHandle;

import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.pattern.Criterion;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.scale.ScaleUtilities;

/**
 * @author David Culyba
 */
public class ScaleDragManipulator extends LinearDragManipulator {
	
	private static final double MIN_HANDLE_PULL = .1d;
	
	@Override
	protected void updateBasedOnHandlePull( double previousPull, double newPull )
	{
		
		double pullDif = (newPull) / (previousPull);
		LinearScaleHandle scaleHandle = (LinearScaleHandle)this.linearHandle;
		Vector3 scaleVector;
		if (scaleHandle.applyAlongAxis())
		{
			scaleVector = new Vector3(1.0d, 1.0d, 1.0d);
			if (scaleHandle.getDragAxis().x != 0.0d)
				scaleVector.x = Math.abs( scaleHandle.getDragAxis().x ) * pullDif;
			if (scaleHandle.getDragAxis().y != 0.0d)
				scaleVector.y = Math.abs( scaleHandle.getDragAxis().y ) * pullDif;
			if (scaleHandle.getDragAxis().z != 0.0d)
				scaleVector.z = Math.abs( scaleHandle.getDragAxis().z ) * pullDif;
		}
		else
		{
			scaleVector = new Vector3(pullDif, pullDif, pullDif);
		}
		
		//Don't scale if the handles are pulled past their origin
		if (previousPull <= MIN_HANDLE_PULL || newPull <= MIN_HANDLE_PULL)
		{
			scaleVector = new Vector3(1.0d, 1.0d, 1.0d);
		}
		
		
		ScaleUtilities.applyScale(this.manipulatedTransformable, scaleVector, new Criterion< Component >(){
			protected boolean isHandle(Component c)
			{
				if (c == null)
					return false;
				Object bonusData = c.getBonusDataFor( PickHint.PICK_HINT_KEY );
				if ( bonusData instanceof PickHint && ((PickHint)bonusData).intersects( PickHint.HANDLES ))
					return true;
				else
					return isHandle( c.getParent() );
			}
			
			public boolean accept(Component c)
			{
				return !isHandle(c);
			}
		});
		
	}
	
	@Override
	public String getUndoRedoDescription() {
		return "Object Resize";
	}
	
	@Override
	protected HandleSet getHandleSetToEnable() {
		return new HandleSet(this.linearHandle.getMovementDescription().direction.getHandleGroup(), HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.RESIZE);
	}

}
