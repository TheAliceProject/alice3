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

package org.lgna.story;

import org.lgna.project.annotations.*;

/**
 * @author Dennis Cosgrove
 */
public class Ground extends Entity implements MutableRider, Visual {
	public static enum SurfaceAppearance implements ImagePaint {
		GRASS("grass"), 
		DIRT("dirt"), 
		SAND("sand"), 
		SNOW("snow"), 
		WATER("water"), 
		MOON("moon");
		private String resourceName;

		SurfaceAppearance( String resourceName ) {
			this.resourceName = resourceName;
		}
		public java.net.URL getResource() {
			return Ground.class.getResource( "resources/grounds/" + this.resourceName + ".png" );
		}
	}

	private final org.lgna.story.implementation.GroundImp implementation = new org.lgna.story.implementation.GroundImp( this );

	@Override
	/*package-private*/org.lgna.story.implementation.GroundImp getImplementation() {
		return this.implementation;
	}
	public void setVehicle( Entity vehicle ) {
		this.getImplementation().setVehicle( vehicle != null ? vehicle.getImplementation() : null );
	}
	@MethodTemplate()
	@GetterTemplate(isPersistent = true)
	public Paint getPaint() {
		return this.getImplementation().paint.getValue();
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void setPaint( Paint paint ) {
		this.setPaint( paint, new SetPropertyDetails.Value() );
	}
	@MethodTemplate()
	public void setPaint( Paint paint, SetPropertyDetails.Value details ) {
		this.getImplementation().paint.animateValue( paint, details.getDuration(), details.getStyle() );
	}

	@MethodTemplate()
	@GetterTemplate(isPersistent = true)
	@ValueTemplate(detailsEnumCls = org.lgna.story.annotation.PortionDetails.class)
	public Double getOpacity() {
		return (double)this.getImplementation().opacity.getValue();
	}
	@MethodTemplate( isFollowedByLongerMethod = true )
	public void setOpacity( Number opacity ) {
		this.setOpacity( opacity, new SetPropertyDetails.Value() );
	}
	@MethodTemplate()
	public void setOpacity( Number opacity, SetPropertyDetails.Value details ) {
		this.getImplementation().opacity.animateValue( opacity.floatValue(), details.getDuration(), details.getStyle() );
	}
}
