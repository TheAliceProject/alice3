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

package edu.cmu.cs.dennisc.scenegraph.graphics;

public abstract class Text extends edu.cmu.cs.dennisc.scenegraph.Graphic {
	public edu.cmu.cs.dennisc.property.StringProperty text = new edu.cmu.cs.dennisc.property.StringProperty( this, "" );
	public edu.cmu.cs.dennisc.color.property.Color4fProperty textColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, this.getDefaultTextColor(), false );
	public edu.cmu.cs.dennisc.property.InstanceProperty< java.awt.Font > font = new edu.cmu.cs.dennisc.property.InstanceProperty< java.awt.Font >( this, this.getDefaultFont() );
	protected abstract edu.cmu.cs.dennisc.color.Color4f getDefaultTextColor();
	protected abstract java.awt.Font getDefaultFont();
}
