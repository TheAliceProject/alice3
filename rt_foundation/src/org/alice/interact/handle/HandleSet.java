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

import java.util.BitSet;

/**
 * @author David Culyba
 */
public class HandleSet extends BitSet
{
	public static HandleSet GROUND_TRANSLATION_VISUALIZATION = new HandleSet(HandleGroup.TRANSLATION, HandleGroup.VISUALIZATION, HandleGroup.X_AND_Z_AXIS);
	public static HandleSet UP_DOWN_TRANSLATION_VISUALIZATION = new HandleSet(HandleGroup.TRANSLATION, HandleGroup.VISUALIZATION, HandleGroup.Y_AXIS);
	public static HandleSet DEFAULT_INTERACTION = new HandleSet(HandleGroup.DEFAULT, HandleGroup.INTERACTION);
	public static HandleSet RESIZE_INTERACTION = new HandleSet(HandleGroup.RESIZE, HandleGroup.INTERACTION);
	public static HandleSet ROTATION_INTERACTION = new HandleSet(HandleGroup.ROTATION, HandleGroup.INTERACTION);
	public static HandleSet TRANSLATION_INTERACTION = new HandleSet(HandleGroup.TRANSLATION, HandleGroup.INTERACTION);
	
	public enum HandleGroup {
		ROTATION(0),
		TRANSLATION(1),
		RESIZE(2),
		DEFAULT(3),
		LOCAL(4),
		STOOD_UP(5),
		ABSOLUTE(6),
		VISUALIZATION(7),
		INTERACTION(8),
		X_AXIS(9),
		Y_AXIS(10),
		Z_AXIS(11),
		RESIZE_AXIS(12),
		X_AND_Z_AXIS(13);
		
		private int index;
		
		private HandleGroup(int index)
		{
			this.index = index;
		}
		
		public int getIndex()
		{
			return this.index;
		}
	}
		 
	@Override
	//HandleSet intersection is true if all of the bits passed in are set on this
	//An empty set will never match
	public boolean intersects( BitSet set )
	{
		if (set == null)
		{
			return false;
		}
		boolean intersection = false;
		for (int i=0; i<set.length(); i++)
		{
			if (set.get( i ) && this.get( i ))
			{
				intersection = true;
			}
			else if (set.get( i ) && !this.get( i ))
			{
				return false;
			}
		}
		return intersection;
	}
	public void addSet( HandleSet set )
	{
		this.or( set );
	}
	
	
	public HandleSet( HandleGroup...groups)
	{
		addGroups(groups);
	}
	
	public void addGroup( HandleGroup group )
	{
		this.set( group.getIndex() );
	}
	
	public void addGroups( HandleGroup...groups )
	{
		for (int i=0; i<groups.length; i++)
		{
			this.set( groups[i].getIndex() );
		}
	}
	
}
