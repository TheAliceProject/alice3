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

/**
 * @author Dennis Cosgrove
 */
public abstract class Model extends MovableTurnable implements Resizable, Visual {
	@Override
	/*package-private*/ abstract org.lgna.story.implementation.ModelImplementation getImplementation();
	public Double getOpacity() {
		return (double)this.getImplementation().getOpacity();
	}
	public void setOpacity( Number opacity ) {
		this.setOpacity( opacity, new AnimationDetails() );
	}
	public void setOpacity( java.lang.Number opacity, org.lgna.story.AnimationDetails details ) {
		this.getImplementation().animateOpacity( opacity.floatValue(), details.getDuration(), details.getStyle() );
	}
	
	public Scale getScale() {
		return Scale.createInstance( this.getImplementation().getScale() );
	}
	public void setScale( Scale scale ) {
		this.setScale( scale, new AnimationDetails() );
	}
	public void setScale( Scale scale, AnimationDetails details ) {
		this.getImplementation().animateSetScale( Scale.getInternal( scale ), details.getDuration(), details.getStyle() );
	}
	public Size getSize() {
		return Size.createInstance( this.getImplementation().getSize() );
	}
	public void setSize( Size size ) {
		this.setSize( size, new AnimationDetails() );
	}
	public void setSize( Size size, AnimationDetails details ) {
		this.getImplementation().animateSetSize( Size.getInternal( size ), details.getDuration(), details.getStyle() );
	}
	public Double getWidth() {
		return this.getImplementation().getSize().x;
	}
	public void setWidth( Number width ) {
		this.setWidth( width, new SetDimensionAnimationDetails() );
	}
	public void setWidth( Number width, SetDimensionAnimationDetails details ) {
		SetDimensionPolicy policy = details.getPolicy();
		this.getImplementation().animateSetWidth( width.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), details.getDuration(), details.getStyle() );
	}
	public Double getHeight() {
		return this.getImplementation().getSize().y;
	}
	public void setHeight( Number height ) {
		this.setHeight( height, new SetDimensionAnimationDetails() );
	}
	public void setHeight( Number height, SetDimensionAnimationDetails details ) {
		SetDimensionPolicy policy = details.getPolicy();
		this.getImplementation().animateSetHeight( height.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), details.getDuration(), details.getStyle() );
	}
	public Double getDepth() {
		return this.getImplementation().getSize().z;
	}
	public void setDepth( Number depth ) {
		this.setDepth( depth, new SetDimensionAnimationDetails() );
	}
	public void setDepth( Number depth, SetDimensionAnimationDetails details ) {
		SetDimensionPolicy policy = details.getPolicy();
		this.getImplementation().animateSetDepth( depth.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), details.getDuration(), details.getStyle() );
	}
	public void resize( Number factor ) {
		this.resize( factor, new AnimationDetails() );
	}
	public void resize( Number factor, AnimationDetails details ) {
		this.getImplementation().animateResize( factor.doubleValue(), details.getDuration(), details.getStyle() );
	}
	public void resizeWidth( Number factor ) {
		this.resizeWidth( factor, new ResizeDimensionAnimationDetails() );
	}
	public void resizeWidth( Number factor, ResizeDimensionAnimationDetails details ) {
		this.getImplementation().animateResizeWidth( factor.doubleValue(), details.isVolumePreserved(), details.getDuration(), details.getStyle() );
	}
	public void resizeHeight( Number factor ) {
		this.resizeHeight( factor, new ResizeDimensionAnimationDetails() );
	}
	public void resizeHeight( Number factor, ResizeDimensionAnimationDetails details ) {
		this.getImplementation().animateResizeHeight( factor.doubleValue(), details.isVolumePreserved(), details.getDuration(), details.getStyle() );
	}
	public void resizeDepth( Number factor ) {
		this.resizeDepth( factor, new ResizeDimensionAnimationDetails() );
	}
	public void resizeDepth( Number factor, ResizeDimensionAnimationDetails details ) {
		this.getImplementation().animateResizeDepth( factor.doubleValue(), details.isVolumePreserved(), details.getDuration(), details.getStyle() );
	}
}
