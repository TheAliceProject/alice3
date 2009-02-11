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
package org.alice.interact;

import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.StandIn;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public enum MovementType {
	STOOD_UP(0),
	LOCAL(1),
	ABSOLUTE(2);

	private int index;
	private MovementType( int index ) {
		this.index = index;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return this.index;
	}
	
	public boolean isIndex(int index)
	{
		return (this.index == index);
	}
	
	public void applyTranslation( Transformable transformable, Point3 translateAmount)
	{
		switch (this)
		{
			case STOOD_UP:
			{
				StandIn standIn = new StandIn();
				standIn.vehicle.setValue( transformable );
				standIn.setAxesOnlyToStandUp();
				transformable.applyTranslation( translateAmount, standIn );
				break;
			}
			case LOCAL:
			{
				transformable.applyTranslation( translateAmount, transformable );
				break;
			}
			case ABSOLUTE:
			{
				transformable.applyTranslation( translateAmount, AsSeenBy.SCENE );
				break;
			}
		}
	}
	
	public void applyRotation( Transformable transformable, Vector3 rotationAxis, Angle rotation)
	{
		switch (this)
		{
			case STOOD_UP:
			{
				StandIn standIn = new StandIn();
				standIn.vehicle.setValue( transformable );
				standIn.setAxesOnlyToStandUp();
				transformable.applyRotationAboutArbitraryAxis( rotationAxis, rotation, standIn );
				break;
			}
			case LOCAL:
			{
				transformable.applyRotationAboutArbitraryAxis( rotationAxis, rotation, transformable );
				break;
			}
			case ABSOLUTE:
			{
				transformable.applyRotationAboutArbitraryAxis( rotationAxis, rotation, AsSeenBy.SCENE );
				break;
			}
		}
	}
	
	public static MovementType getMovementTypeForIndex( int index )
	{
		MovementType[] types = MovementType.values();
		for ( MovementType currentType : types )
		{
			if (currentType.isIndex( index ))
			{
				return currentType;
			}
		}
		return null;
	}
	
}
