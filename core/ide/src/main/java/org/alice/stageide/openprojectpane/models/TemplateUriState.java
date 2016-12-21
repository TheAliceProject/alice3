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

package org.alice.stageide.openprojectpane.models;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import org.alice.nonfree.NebulousIde;

/**
 * @author Dennis Cosgrove
 */
public class TemplateUriState extends org.lgna.croquet.ImmutableDataSingleSelectListState<java.net.URI> {
	public static final String SCHEME = "gen";

	public static enum Template {
		GRASS( org.lgna.story.SGround.SurfaceAppearance.GRASS, new org.lgna.story.Color( 150 / 255.0, 226 / 255.0, 252 / 255.0 ) ),
		SEA_FLOOR( org.lgna.story.SGround.SurfaceAppearance.OCEAN_FLOOR, new org.lgna.story.Color( 0.0, .431, .859 ), 0.3, org.lgna.story.Color.WHITE, new org.lgna.story.Color( 0, .549, .565 ) ),
		MOON( org.lgna.story.SGround.SurfaceAppearance.MOON, new org.lgna.story.Color( .11, .133, .178 ), 0, org.lgna.story.Color.WHITE, new org.lgna.story.Color( .0, .118, .396 ) ),
		MARS( org.lgna.story.SGround.SurfaceAppearance.MARS, new org.lgna.story.Color( .847, .69, .588 ), 0.25, org.lgna.story.Color.WHITE, new org.lgna.story.Color( .541, .2, .0 ) ),

		SNOW( org.lgna.story.SGround.SurfaceAppearance.SNOW, new org.lgna.story.Color( .82, .941, 1.0 ), 0.22, org.lgna.story.Color.WHITE, org.lgna.story.Color.WHITE ),
		ROOM( NebulousIde.nonfree.getFloorApperanceRedwood(), NebulousIde.nonfree.getWallApperanceYellow(), org.lgna.story.Color.WHITE ),
		WONDERLAND( org.lgna.story.SGround.SurfaceAppearance.DARK_GRASS, new org.lgna.story.Color( 0.0, .0941, .294 ), .1, org.lgna.story.Color.WHITE, new org.lgna.story.Color( .541, 0.0, .125 ) ),
		SEA_SURFACE( org.lgna.story.SGround.SurfaceAppearance.OCEAN, new org.lgna.story.Color( .659, .902, .922 ), .16, org.lgna.story.Color.WHITE, new org.lgna.story.Color( .0, .424, .761 ), .7 ),

		LAGOON_FLOOR( org.lgna.story.SGround.SurfaceAppearance.DESERT, new org.lgna.story.Color( .294, .863, 1.0 ), 0.16, org.lgna.story.Color.WHITE, new org.lgna.story.Color( 1.0, .851, 0.0 ) ),
		SWAMP( org.lgna.story.SGround.SurfaceAppearance.SWAMP, new org.lgna.story.Color( .0667, .118, .122 ), 0.27, org.lgna.story.Color.WHITE, new org.lgna.story.Color( .0, 1.0, .741 ), .7 ),
		DESERT( org.lgna.story.SGround.SurfaceAppearance.SANDY_DESERT, new org.lgna.story.Color( .886, .831, .51 ), 0.2, org.lgna.story.Color.WHITE, new org.lgna.story.Color( .322, .0745, .0 ) ),
		DIRT( org.lgna.story.SGround.SurfaceAppearance.ROCKY_BROWN, new org.lgna.story.Color( .514, .376, .278 ), 0, org.lgna.story.Color.WHITE, new org.lgna.story.Color( .384, .0, .22 ) ),

		SEA_SURFACE_NIGHT( org.lgna.story.SGround.SurfaceAppearance.OCEAN_NIGHT, new org.lgna.story.Color( .0, .11, .149 ), .2, org.lgna.story.Color.WHITE, new org.lgna.story.Color( .529, 1.0, .0 ), .5 ),
		ICE( org.lgna.story.SGround.SurfaceAppearance.ICE, new org.lgna.story.Color( .0, .278, .392 ), .15, org.lgna.story.Color.WHITE, new org.lgna.story.Color( .0, .859, 1.0 ), .7 ),
		AMAZON( org.lgna.story.SGround.SurfaceAppearance.JUNGLE, new org.lgna.story.Color( .918, 1.0, .471 ), .25, new org.lgna.story.Color( .341, .576, 1.0 ), new org.lgna.story.Color( .0, .00784, .38 ) ),
		NORTHWEST_ISLAND( org.lgna.story.SGround.SurfaceAppearance.OCEAN, new org.lgna.story.Color( .663, .718, .843 ), .15, org.lgna.story.Color.WHITE, new org.lgna.story.Color( 0, .235, .153 ), .5 ),

		NORTHWEST_FOREST( org.lgna.story.SGround.SurfaceAppearance.FOREST_FLOOR, new org.lgna.story.Color( .427, .533, .51 ), .2 ),
		MAGIC( org.lgna.story.SGround.SurfaceAppearance.SWAMP, new org.lgna.story.Color( .0667, .118, .125 ), .27, org.lgna.story.Color.WHITE, new org.lgna.story.Color( .169, .231, .29 ), .7 ),
		GRASSY_DESERT( org.lgna.story.SGround.SurfaceAppearance.DRY_GRASS, new org.lgna.story.Color( .835, .769, .518 ), .13 ),
		MARS_NIGHT( org.lgna.story.SGround.SurfaceAppearance.MARS, new org.lgna.story.Color( .11, .0824, .255 ), 0.25, org.lgna.story.Color.WHITE, new org.lgna.story.Color( .541, .2, .0 ) );
		public static Template getSurfaceAppearance( java.net.URI uri ) {
			if( isValidUri( uri ) ) {
				String fragment = uri.getFragment();
				if( fragment != null ) {
					return Template.valueOf( fragment );
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		public static boolean isValidUri( java.net.URI uri ) {
			if( uri != null ) {
				return SCHEME.equals( uri.getScheme() );
			} else {
				return false;
			}
		}

		private final org.lgna.story.SGround.SurfaceAppearance surfaceAppearance;
		private final org.lgna.story.Paint floorAppearance;
		private final org.lgna.story.Paint wallAppearance;
		private final org.lgna.story.Paint ceilingAppearance;
		//private final org.lgna.story.Color atmosphereColor;
		private final org.lgna.story.Color atmosphereColor;
		private final double fogDensity;
		private final double groundOpacity;
		private final org.lgna.story.Color aboveLightColor;
		private final org.lgna.story.Color belowLightColor;
		private final boolean isRoom;

		private Template( org.lgna.story.SGround.SurfaceAppearance surfaceAppearance, org.lgna.story.Color atmosphereColor, double fogDensity, org.lgna.story.Color aboveLightColor, org.lgna.story.Color belowLightColor, double groundOpacity ) {
			this.surfaceAppearance = surfaceAppearance;
			//this.atmosphereColor = atmosphereColor;
			this.atmosphereColor = atmosphereColor;
			this.fogDensity = fogDensity;
			this.groundOpacity = groundOpacity;
			this.aboveLightColor = aboveLightColor;
			this.belowLightColor = belowLightColor;
			this.isRoom = false;
			this.floorAppearance = null;
			this.wallAppearance = null;
			this.ceilingAppearance = null;
		}

		private Template( org.lgna.story.SGround.SurfaceAppearance surfaceAppearance, org.lgna.story.Color atmosphereColor, double fogDensity, org.lgna.story.Color aboveLightColor, org.lgna.story.Color belowLightColor ) {
			this( surfaceAppearance, atmosphereColor, fogDensity, aboveLightColor, belowLightColor, 1.0 );
		}

		private Template( org.lgna.story.SGround.SurfaceAppearance surfaceAppearance, org.lgna.story.Color atmosphereColor, double fogDensity, org.lgna.story.Color aboveLightColor ) {
			this( surfaceAppearance, atmosphereColor, fogDensity, aboveLightColor, null );
		}

		private Template( org.lgna.story.SGround.SurfaceAppearance surfaceAppearance, org.lgna.story.Color atmosphereColor, double fogDensity ) {
			this( surfaceAppearance, atmosphereColor, fogDensity, null );
		}

		private Template( org.lgna.story.SGround.SurfaceAppearance surfaceAppearance, org.lgna.story.Color atmosphereColor ) {
			this( surfaceAppearance, atmosphereColor, Double.NaN );
		}

		private Template( org.lgna.story.SGround.SurfaceAppearance surfaceAppearance ) {
			this( surfaceAppearance, null );
		}

		private Template( org.lgna.story.SGround.SurfaceAppearance surfaceAppearance, org.lgna.story.Paint floorAppearance, org.lgna.story.Paint wallAppearance, org.lgna.story.Paint ceilingAppearance ) {
			this.surfaceAppearance = surfaceAppearance;
			//this.atmosphereColor = atmosphereColor;
			this.atmosphereColor = null;
			this.fogDensity = Double.NaN;
			this.groundOpacity = 1.0;
			this.aboveLightColor = null;
			this.belowLightColor = null;
			this.isRoom = true;
			this.floorAppearance = floorAppearance;
			this.wallAppearance = wallAppearance;
			this.ceilingAppearance = ceilingAppearance;
		}

		private Template( org.lgna.story.Paint floorAppearance, org.lgna.story.Paint wallAppearance, org.lgna.story.Paint ceilingAppearance ) {
			this( null, floorAppearance, wallAppearance, ceilingAppearance );
		}

		public org.lgna.story.SGround.SurfaceAppearance getSurfaceAppearance() {
			return this.surfaceAppearance;
		}

		public org.lgna.story.Paint getFloorAppearance() {
			return this.floorAppearance;
		}

		public org.lgna.story.Paint getWallAppearance() {
			return this.wallAppearance;
		}

		public org.lgna.story.Paint getCeilingAppearance() {
			return this.ceilingAppearance;
		}

		public boolean isRoom() {
			return this.isRoom;
		}

		//		public org.lgna.story.Color getAtmosphereColor() {
		//			return this.atmosphereColor;
		//		}
		public org.lgna.story.Color getAtmospherColor() {
			return this.atmosphereColor;
		}

		public org.lgna.story.Color getAboveLightColor() {
			return this.aboveLightColor;
		}

		public org.lgna.story.Color getBelowLightColor() {
			return this.belowLightColor;
		}

		public double getFogDensity() {
			return this.fogDensity;
		}

		public double getGroundOpacity() {
			return this.groundOpacity;
		}

		public java.net.URI getUri() {
			try {
				//todo: investigate
				String schemeSpecificPart = null;//org.lgna.story.Ground.SurfaceAppearance.class.getName();
				String path = "/" + org.lgna.story.SGround.SurfaceAppearance.class.getName();
				String fragment = this.name();
				return new java.net.URI( SCHEME, schemeSpecificPart, path, fragment );
			} catch( java.net.URISyntaxException urise ) {
				throw new RuntimeException( urise );
			}
		}
	};

	private static java.net.URI[] createArray() {
		List<URI> uris = new LinkedList<URI>();

		for( Template template : Template.values() ) {
			if( !NebulousIde.nonfree.isNonFreeEnabled() && ( template == Template.ROOM ) ) {
				// pass
			} else {
				uris.add( template.getUri() );
			}
		}

		java.net.URI[] array = new java.net.URI[ uris.size() ];
		uris.toArray( array );
		return array;
	}

	private static class SingletonHolder {
		private static TemplateUriState instance = new TemplateUriState();
	}

	public static TemplateUriState getInstance() {
		return SingletonHolder.instance;
	}

	public static String getLocalizedName( Template templateValue ) {
		return getLocalizedName( templateValue.toString() );
	}

	public static String getLocalizedName( String stateName ) {
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( TemplateUriState.class.getPackage().getName() + ".templateNames" );
		if( stateName != null ) {
			try {
				stateName = resourceBundle.getString( stateName );
			} catch( MissingResourceException e ) {
				e.printStackTrace();
			}
		}
		return stateName;
	}

	private TemplateUriState() {
		super( org.lgna.croquet.Application.APPLICATION_UI_GROUP, java.util.UUID.fromString( "53c45c6f-e14f-4a88-ae90-1942ed3f3483" ), -1, org.alice.ide.croquet.codecs.UriCodec.SINGLETON, createArray() );
	}
}
