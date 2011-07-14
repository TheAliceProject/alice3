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

package org.alice.apis.moveandturn;

import java.util.LinkedList;
import java.util.List;

import org.alice.interact.PickHint;
import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;

import edu.cmu.cs.dennisc.alice.annotations.MethodTemplate;
import edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author Dennis Cosgrove
 */
public abstract class Marker extends Transformable 
{
	protected SingleAppearance sgFrontFacingAppearance = new SingleAppearance();
	
	protected List<Visual> sgVisuals = new LinkedList<Visual>();
	protected boolean isShowing = true;
	
	protected boolean displayVisuals = true;
	
	public Marker()
	{
		super();
		sgFrontFacingAppearance.diffuseColor.setValue( this.getDefaultMarkerColor() );
		sgFrontFacingAppearance.opacity.setValue( new Float(this.getDefaultMarkerOpacity()) );
		createVisuals();
		this.getSGTransformable().putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.MARKERS );
		this.setShowing(this.getDisplayVisuals());
	}
	
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isShowing() {
		return this.isShowing;
	}
	
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public void setShowing( Boolean isShowing ) {	
		if (this.isShowing != isShowing)
		{
			this.isShowing = isShowing;
//			System.out.println("Setting visibility of "+this+":"+this.hashCode());
			if (this.getDisplayVisuals())
			{
				for (Visual v : this.sgVisuals)
				{
	//				System.out.println("  Setting "+v+":"+v.hashCode()+"->"+v.getParent()+"->"+v.getParent().getRoot()+", showing to "+this.isShowing);
					v.isShowing.setValue(this.isShowing);
				}
			}
		}
	}
	
	public boolean getDisplayVisuals()
	{
		return this.displayVisuals;
	}
	
	public void setDisplayVisuals(boolean useDisplay)
	{
		this.displayVisuals = useDisplay;
		if (!this.displayVisuals)
		{
			for (Visual v : this.sgVisuals)
			{
				v.isShowing.setValue(false);
			}
		}
	}
	
	@Override
	public void setName(String name) 
	{
		super.setName(name);
		Color color = MoveAndTurnSceneEditor.getColorForMarkerName(name);
		if (color != null)
		{
			this.setMarkerColor(color.getInternal());
		}	
	}
	
	protected abstract void createVisuals();
	
	public Color4f getMarkerColor()
	{
		return sgFrontFacingAppearance.diffuseColor.getValue();
	}
	
	public void setMarkerColor( Color4f color )
	{
		sgFrontFacingAppearance.diffuseColor.setValue( color );
	}
	
	protected Color4f getDefaultMarkerColor()
	{
		return Color4f.CYAN;
	}
	
	public float getDefaultMarkerOpacity()
	{
		return 1;
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.scenegraph.SingleAppearance getSGSingleAppearance() {
		return sgFrontFacingAppearance;
	}
	
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Color getColor() {
		return new Color( sgFrontFacingAppearance.diffuseColor.getValue() );
	}

	public void setColor( final Color color, Number duration, final Style style) {
		final double actualDuration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( actualDuration, RIGHT_NOW ) ) {
			Marker.this.setMarkerColor( color.getInternal() );
		} else {
			perform( new edu.cmu.cs.dennisc.color.animation.Color4fAnimation( actualDuration, style, getColor().getInternal(), color.getInternal() ) {
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.color.Color4f color ) {
					Marker.this.setMarkerColor( color );
				}
			} );
		}
	}
	
	public void setColor( Color color, Number duration ) {
		setColor( color, duration, DEFAULT_STYLE );
	}
	public void setColor( Color color ) {
		setColor( color, DEFAULT_DURATION );
	}
	
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Double getOpacity() {
		float actualValue = sgFrontFacingAppearance.opacity.getValue();
		double scaledValue = actualValue / this.getDefaultMarkerOpacity();
		return scaledValue;
	}

	protected void setModelOpacity(float opacity)
	{
		float scaledValue = opacity * this.getDefaultMarkerOpacity();
		sgFrontFacingAppearance.opacity.setValue(scaledValue);
	}
	
	public void setOpacity( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=Portion.class )
			final Number opacity,
			Number duration, 
			final Style style
		) {
		final double actualDuration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( actualDuration, RIGHT_NOW ) ) {
			Marker.this.setModelOpacity( opacity.floatValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.FloatAnimation( actualDuration, style, getOpacity().floatValue(), opacity.floatValue() ) {
				@Override
				protected void updateValue( Float opacity ) {
					Marker.this.setModelOpacity( opacity );
				}
			} );
		}
	}
	
	public void setOpacity( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=Portion.class )
			Number opacity,
			Number duration 
		) {
		setOpacity( opacity, duration, DEFAULT_STYLE );
	}
	public void setOpacity( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=Portion.class )
			Number opacity
		) {
		setOpacity( opacity, DEFAULT_DURATION );
	}
	
}
