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
public abstract class Model extends MovableTurnable implements Resizable, Visual {
	@Override
	/*package-private*/ abstract org.lgna.story.implementation.ModelImplementation getImplementation();
	@org.lgna.project.annotations.GetterTemplate(isPersistent=true)
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getOpacity() {
		return (double)this.getImplementation().opacity.getValue();
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setOpacity( 
			@ParameterTemplate(detailsEnumCls=org.lgna.story.annotation.PortionDetails.class)
			Number opacity 
	) {
		this.setOpacity( opacity, new AnimationDetails() );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void setOpacity( 
			@ParameterTemplate(detailsEnumCls=org.lgna.story.annotation.PortionDetails.class)
			Number opacity, 
			org.lgna.story.AnimationDetails details 
	) {
		this.getImplementation().opacity.animateValue( opacity.floatValue(), details.getDuration(), details.getStyle() );
	}
	
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public Scale getScale() {
		return Scale.createInstance( this.getImplementation().getScale() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setScale( Scale scale ) {
		this.setScale( scale, new AnimationDetails() );
	}
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public void setScale( Scale scale, AnimationDetails details ) {
		this.getImplementation().animateSetScale( Scale.getInternal( scale ), details.getDuration(), details.getStyle() );
	}

	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public Size getSize() {
		return Size.createInstance( this.getImplementation().getSize() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setSize( Size size ) {
		this.setSize( size, new AnimationDetails() );
	}
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public void setSize( Size size, AnimationDetails details ) {
		this.getImplementation().animateSetSize( Size.getInternal( size ), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getWidth() {
		return this.getImplementation().getSize().x;
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setWidth( Number width ) {
		this.setWidth( width, new SetDimensionAnimationDetails() );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void setWidth( Number width, SetDimensionAnimationDetails details ) {
		SetDimensionPolicy policy = details.getPolicy();
		this.getImplementation().animateSetWidth( width.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getHeight() {
		return this.getImplementation().getSize().y;
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setHeight( Number height ) {
		this.setHeight( height, new SetDimensionAnimationDetails() );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void setHeight( Number height, SetDimensionAnimationDetails details ) {
		SetDimensionPolicy policy = details.getPolicy();
		this.getImplementation().animateSetHeight( height.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public Double getDepth() {
		return this.getImplementation().getSize().z;
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setDepth( Number depth ) {
		this.setDepth( depth, new SetDimensionAnimationDetails() );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void setDepth( Number depth, SetDimensionAnimationDetails details ) {
		SetDimensionPolicy policy = details.getPolicy();
		this.getImplementation().animateSetDepth( depth.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resize( Number factor ) {
		this.resize( factor, new AnimationDetails() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resize( Number factor, AnimationDetails details ) {
		this.getImplementation().animateResize( factor.doubleValue(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resizeWidth( Number factor ) {
		this.resizeWidth( factor, new ResizeDimensionAnimationDetails() );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resizeWidth( Number factor, ResizeDimensionAnimationDetails details ) {
		this.getImplementation().animateResizeWidth( factor.doubleValue(), details.isVolumePreserved(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeHeight( Number factor ) {
		this.resizeHeight( factor, new ResizeDimensionAnimationDetails() );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resizeHeight( Number factor, ResizeDimensionAnimationDetails details ) {
		this.getImplementation().animateResizeHeight( factor.doubleValue(), details.isVolumePreserved(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeDepth( Number factor ) {
		this.resizeDepth( factor, new ResizeDimensionAnimationDetails() );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resizeDepth( Number factor, ResizeDimensionAnimationDetails details ) {
		this.getImplementation().animateResizeDepth( factor.doubleValue(), details.isVolumePreserved(), details.getDuration(), details.getStyle() );
	}
}
