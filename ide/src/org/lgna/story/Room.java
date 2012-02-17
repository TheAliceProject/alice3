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

import edu.cmu.cs.dennisc.nebulous.NebulousTexture;


public class Room extends Entity implements MutableRider, Visual {
	public static enum WallAppearance implements edu.cmu.cs.dennisc.nebulous.NebulousPaint {
		COOL_WALL;
		
		private NebulousTexture nebulousTexture;
		public NebulousTexture getTexture() {
			if (this.nebulousTexture == null) {
				this.nebulousTexture = new NebulousTexture(this.toString());
			}
			return this.nebulousTexture;
		}
	}
	
	public static enum FloorAppearance implements edu.cmu.cs.dennisc.nebulous.NebulousPaint {
		COOL_WOOD_FLOOR;
		
		private NebulousTexture nebulousTexture;
		public NebulousTexture getTexture() {
			if (this.nebulousTexture == null) {
				this.nebulousTexture = new NebulousTexture(this.toString());
			}
			return this.nebulousTexture;
		}
	}
	
	public static enum CeilingAppearance implements edu.cmu.cs.dennisc.nebulous.NebulousPaint {
		CHECKERS_CEILING;
		
		private NebulousTexture nebulousTexture;
		public NebulousTexture getTexture() {
			if (this.nebulousTexture == null) {
				this.nebulousTexture = new NebulousTexture(this.toString());
			}
			return this.nebulousTexture;
		}
	}

	private final org.lgna.story.implementation.RoomImp implementation;

	public Room()
	{
		this(org.lgna.story.resources.room.Room.DEFAULT);
	}
	
	public Room( org.lgna.story.resources.RoomResource resource ) {
		this.implementation = resource.createImplementation( this );
	}
	
	@Override
	/*package-private*/org.lgna.story.implementation.RoomImp getImplementation() {
		return this.implementation;
	}
	public void setVehicle( Entity vehicle ) {
		this.getImplementation().setVehicle( vehicle != null ? vehicle.getImplementation() : null );
	}
	
	@GetterTemplate(isPersistent = false)
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public Paint getPaint() {
		return this.getImplementation().wallPaint.getValue();
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public void setPaint( Paint paint, SetPaint.Detail... details ) {
		this.getImplementation().wallPaint.animateValue(paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal());
	}
	
	@MethodTemplate()
	@GetterTemplate(isPersistent = true)
	@ValueTemplate(detailsEnumCls = org.lgna.story.annotation.RoomFloorAppearanceDetails.class)
	public Paint getFloorPaint() {
		return this.getImplementation().floorPaint.getValue();
	}
	@MethodTemplate()
	public void setFloorPaint( Paint paint, SetPaint.Detail... details ) {
		this.getImplementation().floorPaint.animateValue(paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal());
	}
	
	@MethodTemplate()
	@GetterTemplate(isPersistent = true)
	@ValueTemplate(detailsEnumCls = org.lgna.story.annotation.RoomWallAppearanceDetails.class)
	public Paint getWallPaint() {
		return this.getImplementation().wallPaint.getValue();
	}
	@MethodTemplate()
	public void setWallPaint( Paint paint, SetPaint.Detail... details ) {
		this.getImplementation().wallPaint.animateValue(paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal());
	}

	@MethodTemplate()
	@GetterTemplate(isPersistent = true)
	@ValueTemplate(detailsEnumCls = org.lgna.story.annotation.RoomCeilingAppearanceDetails.class)
	public Paint getCeilingPaint() {
		return this.getImplementation().ceilingPaint.getValue();
	}
	@MethodTemplate()
	public void setCeilingPaint( Paint paint, SetPaint.Detail... details ) {
		this.getImplementation().ceilingPaint.animateValue(paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal());
	}
	
	@MethodTemplate()
	@GetterTemplate(isPersistent = true)
	@ValueTemplate(detailsEnumCls = org.lgna.story.annotation.PortionDetails.class)
	public Double getOpacity() {
		return (double)this.getImplementation().opacity.getValue();
	}
	@MethodTemplate()
	public void setOpacity( Number opacity, SetOpacity.Detail... details ) {
		this.getImplementation().opacity.animateValue( opacity.floatValue(), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

}