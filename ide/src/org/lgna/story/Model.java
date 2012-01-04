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
public abstract class Model extends MovableTurnable implements MutableRider, Resizable, Visual {
	@Override
	/*package-private*/abstract org.lgna.story.implementation.ModelImp getImplementation();

	public void setVehicle( Entity vehicle ) {
		this.getImplementation().setVehicle( vehicle != null ? vehicle.getImplementation() : null );
	}

	@MethodTemplate()
	@GetterTemplate(isPersistent = true)
	public Paint getPaint() {
		return this.getImplementation().paint.getValue();
	}
	@MethodTemplate()
	public void setPaint( Paint paint, SetPaint.Detail... details ) {
		this.getImplementation().paint.animateValue( paint, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
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

	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public Scale getScale() {
		return Scale.createInstance( this.getImplementation().getScale() );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public void setScale( Scale scale, SetScale.Detail... details ) {
		this.getImplementation().animateSetScale( Scale.getInternal( scale ), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public Size getSize() {
		return Size.createInstance( this.getImplementation().getSize() );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public void setSize( Size size, SetSize.Detail... details ) {
		this.getImplementation().animateSetSize( Size.getInternal( size ), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}
	@MethodTemplate()
	public Double getWidth() {
		return this.getImplementation().getSize().x;
	}
	@MethodTemplate()
	public void setWidth( Number width, SetWidth.Detail... details ) {
		SetDimensionPolicy policy = SetDimensionPolicy.getValue( details );
		this.getImplementation().animateSetWidth( width.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}
	@MethodTemplate()
	public Double getHeight() {
		return this.getImplementation().getSize().y;
	}
	@MethodTemplate()
	public void setHeight( Number height, SetHeight.Detail... details ) {
		SetDimensionPolicy policy = SetDimensionPolicy.getValue( details );
		this.getImplementation().animateSetHeight( height.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}
	@MethodTemplate()
	public Double getDepth() {
		return this.getImplementation().getSize().z;
	}
	@MethodTemplate()
	public void setDepth( Number depth, SetDepth.Detail... details ) {
		SetDimensionPolicy policy = SetDimensionPolicy.getValue( details );
		this.getImplementation().animateSetDepth( depth.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}
	@MethodTemplate()
	public void resize( Number factor, Resize.Detail... details ) {
		this.getImplementation().animateResize( factor.doubleValue(), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}
	@MethodTemplate()
	public void resizeWidth( Number factor, ResizeWidth.Detail... details ) {
		this.getImplementation().animateResizeWidth( factor.doubleValue(), IsVolumePreserved.getValue( details ), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}
	@MethodTemplate()
	public void resizeHeight( Number factor, ResizeHeight.Detail... details ) {
		this.getImplementation().animateResizeHeight( factor.doubleValue(), IsVolumePreserved.getValue( details ), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}
	@MethodTemplate()
	public void resizeDepth( Number factor, ResizeDepth.Detail... details ) {
		this.getImplementation().animateResizeDepth( factor.doubleValue(), IsVolumePreserved.getValue( details ), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}
	

//	@MethodTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
//	public void addMouseButtonListener( org.lgna.story.event.MouseButtonListener mouseButtonListener ) {
//		this.getImplementation().addMouseButtonListener( mouseButtonListener );
//	}
//	@MethodTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
//	public void removeMouseButtonListener( org.lgna.story.event.MouseButtonListener mouseButtonListener ) {
//		this.getImplementation().removeMouseButtonListener( mouseButtonListener );
//	}
}
