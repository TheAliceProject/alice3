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

package org.alice.stageide.openprojectpane.models;

/**
 * @author Dennis Cosgrove
 */
public class TemplateUriSelectionState extends org.alice.ide.openprojectpane.models.UriSelectionState {
	public static Template getSurfaceAppearance( java.net.URI uri ) {
		return Template.valueOf( uri.getFragment() );
	}
	public static final String SCHEME = "gen";
	public static enum Template {
		GRASS( org.lgna.story.Ground.SurfaceAppearance.GRASS, new org.lgna.story.Color(150/255.0, 226/255.0, 252/255.0) ),
		WONDERLAND( org.lgna.story.Ground.SurfaceAppearance.GRASS, new org.lgna.story.Color(0/255.0, 24/255.0, 75/255.0), 0, org.lgna.story.Color.WHITE, new org.lgna.story.Color(25/255.0, 0/255.0, 0/255.0)),
		MOON( org.lgna.story.Ground.SurfaceAppearance.MOON, org.lgna.story.Color.BLACK ),
		SNOW( org.lgna.story.Ground.SurfaceAppearance.SNOW ),
		SEA_SURFACE( org.lgna.story.Ground.SurfaceAppearance.WATER ),
		SEA_FLOOR( org.lgna.story.Ground.SurfaceAppearance.OCEAN_FLOOR, new org.lgna.story.Color(66/255.0, 195/255.0, 252/255.0), 0.25, org.lgna.story.Color.WHITE, org.lgna.story.Color.CYAN ),
		LAGOON_FLOOR( org.lgna.story.Ground.SurfaceAppearance.SAND, new org.lgna.story.Color(75/255.0, 220/255.0, 255/255.0), 0.2, org.lgna.story.Color.WHITE, new org.lgna.story.Color(0/255.0, 26/255.0, 60/255.0) ),
		DESERT( org.lgna.story.Ground.SurfaceAppearance.SAND ),
		MARS( org.lgna.story.Ground.SurfaceAppearance.MARS, new org.lgna.story.Color(40/255.0, 0/255.0, 0/255.0), 0.3, org.lgna.story.Color.WHITE, new org.lgna.story.Color(11/255.0, 0/255.0, 24/255.0) ),
		DIRT( org.lgna.story.Ground.SurfaceAppearance.DIRT ),
		ROOM( org.lgna.story.Room.FloorAppearance.WOOD_ZIG_ZAG, org.lgna.story.Room.WallAppearance.GREEN_STRIPE_WITH_BEAD, org.lgna.story.Room.CeilingAppearance.WHITE_TILES);
		private final org.lgna.story.Ground.SurfaceAppearance surfaceAppearance;
		private final org.lgna.story.Room.FloorAppearance floorAppearance;
		private final org.lgna.story.Room.WallAppearance wallAppearance;
		private final org.lgna.story.Room.CeilingAppearance ceilingAppearance;
		//private final org.lgna.story.Color atmosphereColor;
		private final org.lgna.story.Color atmosphereColor;
		private final double fogDensity;
		private final org.lgna.story.Color aboveLightColor;
		private final org.lgna.story.Color belowLightColor;
		private final boolean isRoom;
		private Template( org.lgna.story.Ground.SurfaceAppearance surfaceAppearance, org.lgna.story.Color atmosphereColor, double fogDensity, org.lgna.story.Color aboveLightColor, org.lgna.story.Color belowLightColor) {
			this.surfaceAppearance = surfaceAppearance;
			//this.atmosphereColor = atmosphereColor;
			this.atmosphereColor = atmosphereColor;
			this.fogDensity = fogDensity;
			this.aboveLightColor = aboveLightColor;
			this.belowLightColor = belowLightColor;
			this.isRoom = false;
			this.floorAppearance = null;
			this.wallAppearance = null;
			this.ceilingAppearance = null;
		}
		private Template( org.lgna.story.Ground.SurfaceAppearance surfaceAppearance, org.lgna.story.Color atmosphereColor, double fogDensity, org.lgna.story.Color aboveLightColor ) {
			this( surfaceAppearance, atmosphereColor, fogDensity, aboveLightColor, null );
		}
		private Template( org.lgna.story.Ground.SurfaceAppearance surfaceAppearance, org.lgna.story.Color atmosphereColor, double fogDensity ) {
			this( surfaceAppearance, atmosphereColor, fogDensity, null );
		}
		private Template( org.lgna.story.Ground.SurfaceAppearance surfaceAppearance, org.lgna.story.Color atmosphereColor ) {
			this( surfaceAppearance, atmosphereColor, Double.NaN );
		}
		private Template( org.lgna.story.Ground.SurfaceAppearance surfaceAppearance ) {
			this( surfaceAppearance, null );
		}
		private Template( org.lgna.story.Room.FloorAppearance floorAppearance, org.lgna.story.Room.WallAppearance wallAppearance, org.lgna.story.Room.CeilingAppearance ceilingAppearance ) {
			this.surfaceAppearance = null;
			//this.atmosphereColor = atmosphereColor;
			this.atmosphereColor = null;
			this.fogDensity = Double.NaN ;
			this.aboveLightColor = null;
			this.belowLightColor = null;
			this.isRoom = true;
			this.floorAppearance = floorAppearance;
			this.wallAppearance = wallAppearance;
			this.ceilingAppearance = ceilingAppearance;
		}
		public org.lgna.story.Ground.SurfaceAppearance getSurfaceAppearance() {
			return this.surfaceAppearance;
		}
		public org.lgna.story.Room.FloorAppearance getFloorAppearance() {
			return this.floorAppearance;
		}
		public org.lgna.story.Room.WallAppearance getWallAppearance() {
			return this.wallAppearance;
		}
		public org.lgna.story.Room.CeilingAppearance getCeilingAppearance() {
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
		public java.net.URI getUri() {
			try {
				//todo: investigate
				String schemeSpecificPart = null;//org.lgna.story.Ground.SurfaceAppearance.class.getName();
				String path = "/" + org.lgna.story.Ground.SurfaceAppearance.class.getName();
				String fragment = this.name();
				return new java.net.URI( SCHEME, schemeSpecificPart, path, fragment );
			} catch( java.net.URISyntaxException urise ) {
				throw new RuntimeException( urise );
			}
		}
	};
	private static class SingletonHolder {
		private static TemplateUriSelectionState instance = new TemplateUriSelectionState();
	}
	public static TemplateUriSelectionState getInstance() {
		return SingletonHolder.instance;
	}
	private final java.net.URI[] array;
	private TemplateUriSelectionState() {
		super( java.util.UUID.fromString( "53c45c6f-e14f-4a88-ae90-1942ed3f3483" ) );
		Template[] values = Template.values();
		this.array = new java.net.URI[ values.length ];
		for( int i=0; i<this.array.length; i++ ) {
			this.array[ i ] = values[ i ].getUri();
		}
	}
	@Override
	protected java.net.URI[] createArray() {
		return this.array;
	}
}
