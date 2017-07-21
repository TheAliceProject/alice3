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

package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.math.Dimension3;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author Dennis Cosgrove
 */
public class AxesImp extends VisualScaleModelImp {
	public AxesImp( org.lgna.story.SAxes abstraction ) {
		this.abstraction = abstraction;
		for( Visual v : this.getSgVisuals() ) {
			this.putInstance( v );
		}
		this.sgAxes.setParent( this.getSgComposite() );
	}

	@Override
	public org.lgna.story.SAxes getAbstraction() {
		return this.abstraction;
	}

	@Override
	protected edu.cmu.cs.dennisc.property.InstanceProperty[] getScaleProperties() {
		return new edu.cmu.cs.dennisc.property.InstanceProperty[] { this.sgAxes.getScaleProperty() };
	}

	@Override
	protected void setSgVisualsScale( edu.cmu.cs.dennisc.math.Matrix3x3 m ) {
		sgAxes.setScale( m );
	}

	@Override
	protected edu.cmu.cs.dennisc.math.Matrix3x3 getSgVisualsScale() {
		return this.sgAxes.getScale();
	}

	@Override
	protected void applyScale( edu.cmu.cs.dennisc.math.Vector3 axis, boolean isScootDesired ) {
		if( isScootDesired ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getSgComposite().localTransformation.getValue();
			m.translation.multiply( axis );
			this.getSgComposite().localTransformation.setValue( m );
		}
		edu.cmu.cs.dennisc.math.Matrix3x3 scale = sgAxes.getScale();
		edu.cmu.cs.dennisc.math.ScaleUtilities.applyScale( scale, axis );
		setSgVisualsScale( scale );
	}

	//	@Override
	//	public edu.cmu.cs.dennisc.scenegraph.Transformable getSgComposite() {
	//		return this.sgAxes;
	//	}

	@Override
	protected SimpleAppearance[] getSgPaintAppearances() {
		return new SimpleAppearance[ 0 ];
	}

	@Override
	protected SimpleAppearance[] getSgOpacityAppearances() {
		return sgAxes.getSgOpacityAppearances();
	}

	@Override
	public Visual[] getSgVisuals() {
		return sgAxes.getSgVisuals();
	}

	@Override
	public void setSize( Dimension3 size ) {
		this.setScale( getScaleForSize( size ) );
	}

	private final org.lgna.story.SAxes abstraction;
	private final edu.cmu.cs.dennisc.scenegraph.util.ExtravagantAxes sgAxes = new edu.cmu.cs.dennisc.scenegraph.util.ExtravagantAxes( 1.0, 1.0, 2.0 );
}
