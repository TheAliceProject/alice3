/*
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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

package org.lgna.story;

import org.lgna.project.annotations.GetterTemplate;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.ValueTemplate;
import org.lgna.project.annotations.Visibility;

public class SRoom extends SThing implements MutableRider, VisualWithPaint {
	private static class LazyTexture extends edu.cmu.cs.dennisc.pattern.Lazy<edu.cmu.cs.dennisc.texture.Texture> {
		public LazyTexture( String textureKey ) {
			this.textureKey = textureKey;
		}

		@Override
		protected edu.cmu.cs.dennisc.texture.Texture create() {
			return new edu.cmu.cs.dennisc.nebulous.NebulousTexture( this.textureKey );
		}

		private final String textureKey;
	}

	private static boolean isNebulousTextureValid() {
		return true;
	}

	public static enum WallAppearance implements NonfreeTexturePaint {
		BLUE_STRIPE_WITH_WOOD_TRIM,
		BLUE_STRIPE_WITH_WHITE_TRIM,
		BLUE_STRIPE_WITH_WOOD_COFFER,
		BLUE_STRIPE_WITH_WHITE_COFFER,
		BLUE_STRIPE_WITH_WOOD_BEAD,
		BLUE_STRIPE_WITH_WHITE_BEAD,
		STARS,
		BRICK,
		GREY_STRIPE,
		GREY_STRIPE_WITH_COFFER,
		YELLOW_STRIPE,
		YELLOW_STRIPE_WITH_COFFER,
		YELLOW_STRIPE_WITH_BEAD,
		GOLD_FAN,
		GOLD_FAN_WITH_COFFER,
		GREEN_STRIPE,
		GREEN_STRIPE_WITH_COFFER,
		GREEN_STRIPE_WITH_BEAD,
		BUNNIES_YELLOW,
		BUNNIES_PURPLE,
		BUNNIES_PINK,
		TEAL,
		TEAL_WITH_BEAD,
		YELLOW,
		YELLOW_WITH_BEAD,
		PURPLE,
		PURPLE_WITH_BEAD,
		BLUE,
		BLUE_WITH_BEAD,
		SALMON,
		SALMON_WITH_BEAD;

		private WallAppearance( Paint fallback ) {
			this.fallback = fallback;
		}

		private WallAppearance() {
			this( Color.WHITE );
		}

		@Override
		public boolean isTextureValid() {
			return isNebulousTextureValid();
		}

		@Override
		public edu.cmu.cs.dennisc.texture.Texture getTexture() {
			return this.lazyTexture.get();
		}

		@Override
		public Paint getFallback() {
			return this.fallback;
		}

		private final LazyTexture lazyTexture = new LazyTexture( this.toString() );
		private final Paint fallback;
	}

	public static enum FloorAppearance implements NonfreeTexturePaint {
		BLACK_CHECKER,
		BLUE_CHECKER,
		BLUE_SQUARES,
		BLACK_TILES,
		WHITE_TILES,
		PAW_PRINT_CARPET,
		BLUE_STAR_CARPET,
		RED_STAR_CARPET,
		RED_CARPET,
		BLUE_CARPET,
		GOLD_CARPET,
		LEAF_CARPET,
		GREEN_TILE,
		RED_TILE,
		BLUE_TILE,
		REDWOOD,
		WOOD_ZIG_ZAG,
		WOOD_SQUARES,
		MOROCCAN_TILES,
		WOOD_DIAMOND;

		private FloorAppearance( Paint fallback ) {
			this.fallback = fallback;
		}

		private FloorAppearance() {
			this( Color.WHITE );
		}

		@Override
		public boolean isTextureValid() {
			return isNebulousTextureValid();
		}

		@Override
		public edu.cmu.cs.dennisc.texture.Texture getTexture() {
			return this.lazyTexture.get();
		}

		@Override
		public Paint getFallback() {
			return this.fallback;
		}

		private final LazyTexture lazyTexture = new LazyTexture( this.toString() );
		private final Paint fallback;
	}

	public static enum CeilingAppearance implements NonfreeTexturePaint {
		BLACK_CHECKER,
		BLUE_CHECKER,
		BLUE_SQUARES,
		BLACK_TILES,
		WHITE_TILES,
		PAW_PRINT_CARPET,
		BLUE_STAR_CARPET,
		RED_STAR_CARPET,
		RED_CARPET,
		BLUE_CARPET,
		GOLD_CARPET,
		LEAF_CARPET,
		GREEN_TILE,
		RED_TILE,
		BLUE_TILE,
		REDWOOD,
		WOOD_ZIG_ZAG,
		WOOD_SQUARES,
		MOROCCAN_TILES,
		WOOD_DIAMOND;

		private CeilingAppearance( Paint fallback ) {
			this.fallback = fallback;
		}

		private CeilingAppearance() {
			this( Color.WHITE );
		}

		@Override
		public boolean isTextureValid() {
			return isNebulousTextureValid();
		}

		@Override
		public edu.cmu.cs.dennisc.texture.Texture getTexture() {
			return this.lazyTexture.get();
		}

		@Override
		public Paint getFallback() {
			return this.fallback;
		}

		private final LazyTexture lazyTexture = new LazyTexture( this.toString() );
		private final Paint fallback;
	}

	private final org.lgna.story.implementation.RoomImp implementation = new org.lgna.story.implementation.RoomImp( this );

	@Override
	/* package-private */org.lgna.story.implementation.RoomImp getImplementation() {
		return this.implementation;
	}

	@Override
	public void setVehicle( SThing vehicle ) {
		this.getImplementation().setVehicle( vehicle != null ? vehicle.getImplementation() : null );
	}

	@Override
	@GetterTemplate( isPersistent = false )
	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public Paint getPaint() {
		return this.getImplementation().wallPaint.getValue();
	}

	@Override
	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public void setPaint( Paint paint, SetPaint.Detail... details ) {
		this.getImplementation().wallPaint.animateValue( paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@MethodTemplate( )
	@GetterTemplate( isPersistent = true )
	@ValueTemplate( detailsEnumCls = org.lgna.story.annotation.RoomFloorAppearanceDetails.class )
	public Paint getFloorPaint() {
		return this.getImplementation().floorPaint.getValue();
	}

	@MethodTemplate( )
	public void setFloorPaint( Paint paint, SetFloorPaint.Detail... details ) {
		this.getImplementation().floorPaint.animateValue( paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	//	@MethodTemplate()
	//	@Deprecated
	//	public void setFloorPaint( Paint paint, SetPaint.Detail... details ) {
	//		this.getImplementation().floorPaint.animateValue(paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal());
	//	}

	@MethodTemplate( )
	@GetterTemplate( isPersistent = true )
	@ValueTemplate( detailsEnumCls = org.lgna.story.annotation.RoomWallAppearanceDetails.class )
	public Paint getWallPaint() {
		return this.getImplementation().wallPaint.getValue();
	}

	@MethodTemplate( )
	public void setWallPaint( Paint paint, SetWallPaint.Detail... details ) {
		this.getImplementation().wallPaint.animateValue( paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	//	@MethodTemplate()
	//	@Deprecated
	//	public void setWallPaint( Paint paint, SetPaint.Detail... details ) {
	//		this.getImplementation().wallPaint.animateValue(paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal());
	//	}

	@MethodTemplate( )
	@GetterTemplate( isPersistent = true )
	@ValueTemplate( detailsEnumCls = org.lgna.story.annotation.RoomCeilingAppearanceDetails.class )
	public Paint getCeilingPaint() {
		return this.getImplementation().ceilingPaint.getValue();
	}

	@MethodTemplate( )
	public void setCeilingPaint( Paint paint, SetCeilingPaint.Detail... details ) {
		this.getImplementation().ceilingPaint.animateValue( paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	//	@MethodTemplate()
	//	@Deprecated
	//	public void setCeilingPaint( Paint paint, SetPaint.Detail... details ) {
	//		this.getImplementation().ceilingPaint.animateValue(paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal());
	//	}

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
