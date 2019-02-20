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
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.UUID;

import org.alice.ide.croquet.codecs.UriCodec;
import org.alice.nonfree.NebulousIde;
import org.lgna.croquet.Application;
import org.lgna.croquet.ImmutableDataSingleSelectListState;
import org.lgna.story.Color;
import org.lgna.story.Paint;
import org.lgna.story.SGround;

/**
 * @author Dennis Cosgrove
 */
public class TemplateUriState extends ImmutableDataSingleSelectListState<URI> {
	public static final String SCHEME = "gen";

	public static enum Template {
		GRASS( SGround.SurfaceAppearance.GRASS, new Color( 150 / 255.0, 226 / 255.0, 252 / 255.0 ) ),
		SEA_FLOOR( SGround.SurfaceAppearance.OCEAN_FLOOR, new Color( 0.0, .431, .859 ), 0.3, Color.WHITE, new Color( 0, .549, .565 ) ),
		MOON( SGround.SurfaceAppearance.MOON, new Color( .11, .133, .178 ), 0, Color.WHITE, new Color( .0, .118, .396 ) ),
		MARS( SGround.SurfaceAppearance.MARS, new Color( .847, .69, .588 ), 0.25, Color.WHITE, new Color( .541, .2, .0 ) ),

		SNOW( SGround.SurfaceAppearance.SNOW, new Color( .82, .941, 1.0 ), 0.22, Color.WHITE, Color.WHITE ),
		ROOM( NebulousIde.nonfree.getFloorApperanceRedwood(), NebulousIde.nonfree.getWallApperanceYellow(), Color.WHITE ),
		WONDERLAND( SGround.SurfaceAppearance.DARK_GRASS, new Color( 0.0, .0941, .294 ), .1, Color.WHITE, new Color( .541, 0.0, .125 ) ),
		SEA_SURFACE( SGround.SurfaceAppearance.OCEAN, new Color( .659, .902, .922 ), .16, Color.WHITE, new Color( .0, .424, .761 ), .7 ),

		LAGOON_FLOOR( SGround.SurfaceAppearance.DESERT, new Color( .294, .863, 1.0 ), 0.16, Color.WHITE, new Color( 1.0, .851, 0.0 ) ),
		SWAMP( SGround.SurfaceAppearance.SWAMP, new Color( .0667, .118, .122 ), 0.27, Color.WHITE, new Color( .0, 1.0, .741 ), .7 ),
		DESERT( SGround.SurfaceAppearance.SANDY_DESERT, new Color( .886, .831, .51 ), 0.2, Color.WHITE, new Color( .322, .0745, .0 ) ),
		DIRT( SGround.SurfaceAppearance.ROCKY_BROWN, new Color( .514, .376, .278 ), 0, Color.WHITE, new Color( .384, .0, .22 ) ),

		SEA_SURFACE_NIGHT( SGround.SurfaceAppearance.OCEAN_NIGHT, new Color( .0, .11, .149 ), .2, Color.WHITE, new Color( .529, 1.0, .0 ), .5 ),
		ICE( SGround.SurfaceAppearance.ICE, new Color( .0, .278, .392 ), .15, Color.WHITE, new Color( .0, .859, 1.0 ), .7 ),
		AMAZON( SGround.SurfaceAppearance.JUNGLE, new Color( .918, 1.0, .471 ), .25, new Color( .341, .576, 1.0 ), new Color( .0, .00784, .38 ) ),
		NORTHWEST_ISLAND( SGround.SurfaceAppearance.OCEAN, new Color( .663, .718, .843 ), .15, Color.WHITE, new Color( 0, .235, .153 ), .5 ),

		NORTHWEST_FOREST( SGround.SurfaceAppearance.FOREST_FLOOR, new Color( .427, .533, .51 ), .2 ),
		MAGIC( SGround.SurfaceAppearance.SWAMP, new Color( .0667, .118, .125 ), .27, Color.WHITE, new Color( .169, .231, .29 ), .7 ),
		GRASSY_DESERT( SGround.SurfaceAppearance.DRY_GRASS, new Color( .835, .769, .518 ), .13 ),
		MARS_NIGHT( SGround.SurfaceAppearance.MARS, new Color( .11, .0824, .255 ), 0.25, Color.WHITE, new Color( .541, .2, .0 ) );
		public static Template getSurfaceAppearance( URI uri ) {
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

		public static boolean isValidUri( URI uri ) {
			if( uri != null ) {
				return SCHEME.equals( uri.getScheme() );
			} else {
				return false;
			}
		}

		private final SGround.SurfaceAppearance surfaceAppearance;
		private final Paint floorAppearance;
		private final Paint wallAppearance;
		private final Paint ceilingAppearance;
		//private final org.lgna.story.Color atmosphereColor;
		private final Color atmosphereColor;
		private final double fogDensity;
		private final double groundOpacity;
		private final Color aboveLightColor;
		private final Color belowLightColor;
		private final boolean isRoom;

		private Template( SGround.SurfaceAppearance surfaceAppearance, Color atmosphereColor, double fogDensity, Color aboveLightColor, Color belowLightColor, double groundOpacity ) {
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

		private Template( SGround.SurfaceAppearance surfaceAppearance, Color atmosphereColor, double fogDensity, Color aboveLightColor, Color belowLightColor ) {
			this( surfaceAppearance, atmosphereColor, fogDensity, aboveLightColor, belowLightColor, 1.0 );
		}

		private Template( SGround.SurfaceAppearance surfaceAppearance, Color atmosphereColor, double fogDensity, Color aboveLightColor ) {
			this( surfaceAppearance, atmosphereColor, fogDensity, aboveLightColor, null );
		}

		private Template( SGround.SurfaceAppearance surfaceAppearance, Color atmosphereColor, double fogDensity ) {
			this( surfaceAppearance, atmosphereColor, fogDensity, null );
		}

		private Template( SGround.SurfaceAppearance surfaceAppearance, Color atmosphereColor ) {
			this( surfaceAppearance, atmosphereColor, Double.NaN );
		}

		private Template( SGround.SurfaceAppearance surfaceAppearance ) {
			this( surfaceAppearance, null );
		}

		private Template( SGround.SurfaceAppearance surfaceAppearance, Paint floorAppearance, Paint wallAppearance, Paint ceilingAppearance ) {
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

		private Template( Paint floorAppearance, Paint wallAppearance, Paint ceilingAppearance ) {
			this( null, floorAppearance, wallAppearance, ceilingAppearance );
		}

		public SGround.SurfaceAppearance getSurfaceAppearance() {
			return this.surfaceAppearance;
		}

		public Paint getFloorAppearance() {
			return this.floorAppearance;
		}

		public Paint getWallAppearance() {
			return this.wallAppearance;
		}

		public Paint getCeilingAppearance() {
			return this.ceilingAppearance;
		}

		public boolean isRoom() {
			return this.isRoom;
		}

		//		public org.lgna.story.Color getAtmosphereColor() {
		//			return this.atmosphereColor;
		//		}
		public Color getAtmospherColor() {
			return this.atmosphereColor;
		}

		public Color getAboveLightColor() {
			return this.aboveLightColor;
		}

		public Color getBelowLightColor() {
			return this.belowLightColor;
		}

		public double getFogDensity() {
			return this.fogDensity;
		}

		public double getGroundOpacity() {
			return this.groundOpacity;
		}

		public URI getUri() {
			try {
				//todo: investigate
				String schemeSpecificPart = null;//org.lgna.story.Ground.SurfaceAppearance.class.getName();
				String path = "/" + SGround.SurfaceAppearance.class.getName();
				String fragment = this.name();
				return new URI( SCHEME, schemeSpecificPart, path, fragment );
			} catch( URISyntaxException urise ) {
				throw new RuntimeException( urise );
			}
		}
	};

	private static URI[] createArray() {
		List<URI> uris = new LinkedList<URI>();

		for( Template template : Template.values() ) {
			if( !NebulousIde.nonfree.isNonFreeEnabled() && ( template == Template.ROOM ) ) {
				// pass
			} else {
				uris.add( template.getUri() );
			}
		}

		URI[] array = new URI[ uris.size() ];
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
		ResourceBundle resourceBundle = ResourceBundle.getBundle( TemplateUriState.class.getPackage().getName() + ".templateNames" );
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
		super( Application.APPLICATION_UI_GROUP, UUID.fromString( "53c45c6f-e14f-4a88-ae90-1942ed3f3483" ), -1, UriCodec.SINGLETON, createArray() );
	}
}
