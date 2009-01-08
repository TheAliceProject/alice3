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

/**
 * @author Dennis Cosgrove
 */

public abstract class Light extends Transformable {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > LIGHT_BRIGHTNESS_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( Light.class, "LightBrightness" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Color > LIGHT_COLOR_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Color >( Light.class, "LightColor" );

	public Light() {
		getSGLight().setParent( getSGTransformable() );
	}
	protected abstract edu.cmu.cs.dennisc.scenegraph.Light getSGLight();

	public Color getLightColor() {
		return new Color( getSGLight().color.getValue() );
	}
	public void setLightColor( Color color, Number duration, Style style ) {
//		if( color == null ) {
//			color = getColor();
//		}
		perform( new edu.cmu.cs.dennisc.color.animation.Color4fAnimation( adjustDurationIfNecessary( duration ), style, getLightColor().getInternal(), color.getInternal() ) {
			@Override
			protected void updateValue( edu.cmu.cs.dennisc.color.Color4f c ) {
				getSGLight().color.setValue( c );
			}
		} );
	}
	public void setLightColor( Color color, Number duration ) {
		setLightColor( color, duration, DEFAULT_STYLE );
	}
	public void setLightColor( Color color ) {
		setLightColor( color, DEFAULT_DURATION );
	}

	public Double getLightBrightness() {
		return (double)getSGLight().brightness.getValue();
	}
	public void setLightBrightness( Number brightness, Number duration, Style style ) {
		perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getLightBrightness(), brightness.doubleValue() ) {
			@Override
			protected void updateValue( Double d ) {
				getSGLight().brightness.setValue( d.floatValue() );
			}
		} );
	}
	public void setLightBrightness( Number brightness, Number duration ) {
		setLightBrightness( brightness, duration, DEFAULT_STYLE );
	}
	public void setLightBrightness( Number brightness ) {
		setLightBrightness( brightness, DEFAULT_DURATION );
	}
}
