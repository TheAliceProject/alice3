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
	/*package-private*/abstract org.lgna.story.implementation.ModelImp getImplementation();
	@MethodTemplate()
	@GetterTemplate(isPersistent = true)
	@ValueTemplate(detailsEnumCls = org.lgna.story.annotation.PortionDetails.class)
	public Double getOpacity() {
		return (double)this.getImplementation().opacity.getValue();
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void setOpacity( Number opacity ) {
		this.setOpacity( opacity, new SetPropertyDetails.Value() );
	}
	@MethodTemplate()
	public void setOpacity( Number opacity, SetPropertyDetails.Value details ) {
		this.getImplementation().opacity.animateValue( opacity.floatValue(), details.getDuration(), details.getStyle() );
	}

	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public Scale getScale() {
		return Scale.createInstance( this.getImplementation().getScale() );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY, isFollowedByLongerMethod = true)
	public void setScale( Scale scale ) {
		this.setScale( scale, new SetPropertyDetails.Value() );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public void setScale( Scale scale, SetPropertyDetails.Value details ) {
		this.getImplementation().animateSetScale( Scale.getInternal( scale ), details.getDuration(), details.getStyle() );
	}

	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public Size getSize() {
		return Size.createInstance( this.getImplementation().getSize() );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY, isFollowedByLongerMethod = true)
	public void setSize( Size size ) {
		this.setSize( size, new SetPropertyDetails.Value() );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public void setSize( Size size, SetPropertyDetails.Value details ) {
		this.getImplementation().animateSetSize( Size.getInternal( size ), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate()
	public Double getWidth() {
		return this.getImplementation().getSize().x;
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void setWidth( Number width ) {
		this.setWidth( width, new SetDimensionDetails.Value() );
	}
	@MethodTemplate()
	public void setWidth( Number width, SetDimensionDetails.Value details ) {
		SetDimensionPolicy policy = details.getPolicy();
		this.getImplementation().animateSetWidth( width.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate()
	public Double getHeight() {
		return this.getImplementation().getSize().y;
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void setHeight( Number height ) {
		this.setHeight( height, new SetDimensionDetails.Value() );
	}
	@MethodTemplate()
	public void setHeight( Number height, SetDimensionDetails.Value details ) {
		SetDimensionPolicy policy = details.getPolicy();
		this.getImplementation().animateSetHeight( height.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate()
	public Double getDepth() {
		return this.getImplementation().getSize().z;
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void setDepth( Number depth ) {
		this.setDepth( depth, new SetDimensionDetails.Value() );
	}
	@MethodTemplate()
	public void setDepth( Number depth, SetDimensionDetails.Value details ) {
		SetDimensionPolicy policy = details.getPolicy();
		this.getImplementation().animateSetDepth( depth.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void resize( Number factor ) {
		this.resize( factor, new SetPropertyDetails.Value() );
	}
	@MethodTemplate()
	public void resize( Number factor, SetPropertyDetails.Value details ) {
		this.getImplementation().animateResize( factor.doubleValue(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void resizeWidth( Number factor ) {
		this.resizeWidth( factor, new ResizeDimensionDetails.Value() );
	}
	@MethodTemplate()
	public void resizeWidth( Number factor, ResizeDimensionDetails.Value details ) {
		this.getImplementation().animateResizeWidth( factor.doubleValue(), details.isVolumePreserved(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void resizeHeight( Number factor ) {
		this.resizeHeight( factor, new ResizeDimensionDetails.Value() );
	}
	@MethodTemplate()
	public void resizeHeight( Number factor, ResizeDimensionDetails.Value details ) {
		this.getImplementation().animateResizeHeight( factor.doubleValue(), details.isVolumePreserved(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void resizeDepth( Number factor ) {
		this.resizeDepth( factor, new ResizeDimensionDetails.Value() );
	}
	@MethodTemplate()
	public void resizeDepth( Number factor, ResizeDimensionDetails.Value details ) {
		this.getImplementation().animateResizeDepth( factor.doubleValue(), details.isVolumePreserved(), details.getDuration(), details.getStyle() );
	}
}
