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
package sceneeditor;

import org.alice.apis.moveandturn.Element;

import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class PickCondition {
	
	enum PickType
	{
		NOTHING,
		MOVEABLE_OBJECT,
		ANYTHING,
		GROUND;
		
		public boolean accepts( PickType toCheck )
		{
			switch (this)
			{
				case NOTHING : 
				{
					return toCheck == NOTHING;
				}
				case ANYTHING : 
				{
					return true;
				}
				case MOVEABLE_OBJECT : 
				{
					return toCheck == MOVEABLE_OBJECT;
				}
				case GROUND : 
				{
					return toCheck == GROUND;
				}
				default :
				{
					return false;
				}
			}
		}
	}
	
	protected boolean isNot = false;
	protected PickType pickType; 
	protected PickCondition nextCondition = null;
	
	public PickCondition( PickType pickType )
	{
		this.pickType = pickType;
	}
	
	public PickCondition( PickType pickType, PickCondition previousCondition )
	{
		this( pickType );
		previousCondition.setNextCondition( this );
	}
	
	public void setNextCondition( PickCondition nextCondition )
	{
		this.nextCondition = nextCondition;
	}
	
	public static PickType getPickType( Element pickedObject )
	{
		boolean isNull = pickedObject == null;
		if (isNull)
		{
			return PickType.NOTHING;
		}
		else
		{
			if (!org.alice.apis.moveandturn.gallery.environments.grounds.GrassyGround.class.isAssignableFrom(pickedObject.getClass()))
			{
				return PickType.MOVEABLE_OBJECT;
			}
			else
			{
				return PickType.GROUND;
			}
		}
	}
	
	public static PickType getPickType( edu.cmu.cs.dennisc.lookingglass.PickResult pickObject )
	{
		boolean isNull = pickObject == null || pickObject.getGeometry() == null || pickObject.getVisual() == null;
		if (isNull)
		{
			return PickType.NOTHING;
		}
		else
		{
			return getPickType( pickObject.getVisual() );
		}
	}
	
	public static PickType getPickType( edu.cmu.cs.dennisc.scenegraph.Element pickedObject )
	{
		boolean isNull = pickedObject == null;
		if (isNull)
		{
			return PickType.NOTHING;
		}
		else
		{
			Element element = Element.getElement( pickedObject );
			return getPickType(element);
		}
	}
	
	public boolean evaluateObject(edu.cmu.cs.dennisc.lookingglass.PickResult pickObject)
	{
		boolean result = this.pickType.accepts( getPickType( pickObject ) );
		if (isNot)
		{
			result = !result;
		}
		return result;
	}
	
	public boolean evalutateChain(edu.cmu.cs.dennisc.lookingglass.PickResult pickObject)
	{
		if ( this.nextCondition == null )
		{
			return this.evaluateObject( pickObject );
		}
		else
		{
			return this.evaluateObject( pickObject ) && this.nextCondition.evalutateChain( pickObject ) ;
		}
	}
	
}
