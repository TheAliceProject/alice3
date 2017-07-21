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

package org.lgna.story;

import org.lgna.project.annotations.GetterTemplate;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.ValueTemplate;

/**
 * @author Dennis Cosgrove
 */
public class SGround extends SThing implements MutableRider, VisualWithPaint {
	public static enum SurfaceAppearance implements ImagePaint {
		GRASS( "grass" ),
		DARK_GRASS( "dark_grass" ),
		DRY_GRASS( "dry_grass" ),
		DIRT( "dirt" ),
		SAND( "sand" ),
		MARS( "mars" ),
		DESERT( "desert" ),
		SANDY_DESERT( "desert_brown" ),
		SNOW( "snow" ),
		ICE( "ice" ),
		SWAMP( "swamp" ),
		SWAMP_WATER( "swamp_water" ),
		POISON_SWAMP( "swamp_poison" ),
		JUNGLE( "jungle" ),
		OCEAN( "ocean" ),
		OCEAN_NIGHT( "ocean_night" ),
		WATER( "water" ),
		OCEAN_FLOOR( "underwater" ),
		MOON( "moon" ),
		BROWN_SAVANA_GRASS( "brown_savana_grass" ),
		GREEN_SAVANA_GRASS( "green_savana_grass" ),
		FOREST_FLOOR( "forest_floor_brown" ),
		RED_FOREST_FLOOR( "forest_floor_red" ),
		DARK_FOREST_FLOOR( "forest_floor_dark" ),
		GOLD_COINS( "gold_coins" ),
		ROCKY_SAND( "rocky_sand" ),
		ROCKY_BROWN( "rocky_brown" ),
		ROCKY_RED( "rocky_red" ),
		WOOD( "wood" ),
		BRICK( "brick" );
		private String resourceName;

		SurfaceAppearance( String resourceName ) {
			this.resourceName = resourceName;
		}

		@Override
		public java.net.URL getResource() {
			return SGround.class.getResource( "resources/grounds/" + this.resourceName + ".png" );
		}
	}

	private final org.lgna.story.implementation.GroundImp implementation = new org.lgna.story.implementation.GroundImp( this );

	@Override
			/* package-private */org.lgna.story.implementation.GroundImp getImplementation() {
		return this.implementation;
	}

	@Override
	public void setVehicle( SThing vehicle ) {
		this.getImplementation().setVehicle( vehicle != null ? vehicle.getImplementation() : null );
	}

	@Override
	@MethodTemplate( )
	@GetterTemplate( isPersistent = true )
	@ValueTemplate( detailsEnumCls = org.lgna.story.annotation.GroundSurfaceAppearanceDetails.class )
	public Paint getPaint() {
		return this.getImplementation().paint.getValue();
	}

	@Override
	@MethodTemplate( )
	public void setPaint( Paint paint, SetPaint.Detail... details ) {
		this.getImplementation().paint.animateValue( paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@Override
	@MethodTemplate( )
	@GetterTemplate( isPersistent = true )
	@ValueTemplate( detailsEnumCls = org.lgna.story.annotation.PortionDetails.class )
	public Double getOpacity() {
		return (double)this.getImplementation().opacity.getValue();
	}

	@Override
	@MethodTemplate( )
	public void setOpacity( Number opacity, SetOpacity.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsBetween0and1( opacity, 0 );
		this.getImplementation().opacity.animateValue( opacity.floatValue(), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}
}
