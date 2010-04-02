/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
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
	public static HandleSet MAIN_ORTHOGRAPHIC_CAMERA_CONTROLS = new HandleSet(HandleGroup.ORTHOGRAPHIC_CAMERA, HandleGroup.MAIN_CAMERA);
	public static HandleSet MAIN_PERSPECTIVE_CAMERA_CONTROLS = new HandleSet(HandleGroup.PERSPECTIVE_CAMERA, HandleGroup.MAIN_CAMERA);
	
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
		X_AND_Z_AXIS(13),
		X_AND_Y_AXIS(14),
		CAMERA(15),
		ORTHOGRAPHIC_CAMERA(16),
		PERSPECTIVE_CAMERA(17),
		MAIN_CAMERA(18),
		TOP_LEFT_CAMERA(19),
		TOP_RIGHT_CAMERA(20),
		BOTTOM_LEFT_CAMERA(21),
		BOTTOM_RIGHT_CAMERA(22);
		
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
	
	
	public static String getStringForSet(HandleSet set)
	{
		if (set == RESIZE_INTERACTION)
		{
			return "Resize";
		}
		else if (set == ROTATION_INTERACTION)
		{
			return "Rotation";
		}
		else if (set == TRANSLATION_INTERACTION)
		{
			return "Move";
		}
		else if (set == DEFAULT_INTERACTION)
		{
			return "Default";
		}
		else
		{
			return "NO STRING SET";
		}
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
