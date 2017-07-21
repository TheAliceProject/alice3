/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author dculyba
 * 
 */
public abstract class MarkerImp extends VisualScaleModelImp {
	protected MarkerImp( org.lgna.story.SMarker abstraction ) {
		super();
		this.initializeData();
		this.abstraction = abstraction;
		createVisuals();
		this.boundingBox = this.calculateBoundingBox();
		setMarkerColor( getDefaultMarkerColor() );
		setMarkerOpacity( getDefaultMarkerOpacity() );
		this.setShowing( false );
	}

	protected void initializeData() {

	}

	@Override
	public org.lgna.story.SMarker getAbstraction() {
		return this.abstraction;
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.scale.Resizer[] getResizers() {
		return new edu.cmu.cs.dennisc.scenegraph.scale.Resizer[] {};
	}

	@Override
	public double getValueForResizer( edu.cmu.cs.dennisc.scenegraph.scale.Resizer resizer ) {
		assert false : resizer;
		return Double.NaN;
	}

	@Override
	public void setValueForResizer( edu.cmu.cs.dennisc.scenegraph.scale.Resizer resizer, double value ) {
		assert false : resizer;
	}

	protected abstract void createVisuals();

	public boolean isShowing() {
		return this.isShowing;
	}

	@Override
	public void setSize( edu.cmu.cs.dennisc.math.Dimension3 size ) {
		setScale( getScaleForSize( size ) );
	}

	public void setShowing( boolean isShowing ) {
		if( this.isShowing != isShowing )
		{
			this.isShowing = isShowing;
			if( this.getDisplayEnabled() )
			{
				Visual[] visuals = this.getSgVisuals();
				if( ( visuals != null ) && ( visuals.length > 0 ) ) {
					for( Visual v : visuals )
					{
						v.isShowing.setValue( this.isShowing );
					}
				}
			}
		}
	}

	public boolean getDisplayEnabled()
	{
		return this.displayEnabled;
	}

	public void setDisplayVisuals( boolean useDisplay )
	{
		this.displayEnabled = useDisplay;
		if( !this.displayEnabled )
		{
			Visual[] visuals = this.getSgVisuals();
			if( ( visuals != null ) && ( visuals.length > 0 ) ) {
				for( Visual v : visuals )
				{
					v.isShowing.setValue( false );
				}
			}
		}
	}

	protected AxisAlignedBox calculateBoundingBox()
	{
		AxisAlignedBox bbox = new AxisAlignedBox();
		Visual[] visuals = this.getSgVisuals();
		if( ( visuals != null ) && ( visuals.length > 0 ) ) {
			for( Visual v : this.getSgVisuals() )
			{
				for( Geometry g : v.geometries.getValue() )
				{
					bbox.union( g.getAxisAlignedMinimumBoundingBox() );
				}
			}
		}
		return bbox;
	}

	protected Color4f getDefaultMarkerColor()
	{
		return Color4f.CYAN;
	}

	protected float getDefaultMarkerOpacity()
	{
		return 1;
	}

	public Color4f getMarkerColor()
	{
		edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] appearances = this.getSgPaintAppearances();
		if( ( appearances != null ) && ( appearances.length > 0 ) ) {
			return appearances[ 0 ].diffuseColor.getValue();
		}
		return Color4f.WHITE;
	}

	public void setMarkerColor( Color4f color )
	{
		edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] appearances = this.getSgPaintAppearances();
		if( ( appearances != null ) && ( appearances.length > 0 ) ) {
			for( edu.cmu.cs.dennisc.scenegraph.SimpleAppearance sgAppearance : this.getSgPaintAppearances() ) {
				sgAppearance.diffuseColor.setValue( color );
			}
		}
	}

	public float getMarkerOpacity() {
		edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] appearances = this.getSgOpacityAppearances();
		if( ( appearances != null ) && ( appearances.length > 0 ) ) {
			float actualValue = appearances[ 0 ].opacity.getValue();
			float scaledValue = actualValue / this.getDefaultMarkerOpacity();
			return scaledValue;
		}
		return 1;
	}

	protected void setMarkerOpacity( float opacity )
	{
		edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] appearances = this.getSgOpacityAppearances();
		float scaledValue = opacity * this.getDefaultMarkerOpacity();
		if( ( appearances != null ) && ( appearances.length > 0 ) ) {
			for( edu.cmu.cs.dennisc.scenegraph.SimpleAppearance sgAppearance : this.getSgOpacityAppearances() ) {
				sgAppearance.opacity.setValue( scaledValue );
			}
		}
	}

	private final org.lgna.story.SMarker abstraction;

	private boolean isShowing = true;
	private boolean displayEnabled = true;
	private AxisAlignedBox boundingBox;
}
