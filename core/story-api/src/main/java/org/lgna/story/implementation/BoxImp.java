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

/**
 * @author Dennis Cosgrove
 */
public class BoxImp extends ShapeImp {
	public BoxImp( org.lgna.story.SBox abstraction ) {
		this.abstraction = abstraction;
		this.getSgVisuals()[ 0 ].geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.sgBox } );
		this.sgBox.yMinimum.setValue( 0.0 );
		this.sgBox.yMaximum.setValue( 1.0 );
	}

	@Override
	public org.lgna.story.SBox getAbstraction() {
		return this.abstraction;
	}

	@Override
	protected edu.cmu.cs.dennisc.property.InstanceProperty[] getScaleProperties() {
		return new edu.cmu.cs.dennisc.property.InstanceProperty[] { this.sgBox.xMaximum, this.sgBox.yMaximum, this.sgBox.zMaximum };
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.scale.Resizer[] getResizers() {
		return new edu.cmu.cs.dennisc.scenegraph.scale.Resizer[] { edu.cmu.cs.dennisc.scenegraph.scale.Resizer.UNIFORM, edu.cmu.cs.dennisc.scenegraph.scale.Resizer.X_AXIS, edu.cmu.cs.dennisc.scenegraph.scale.Resizer.Y_AXIS, edu.cmu.cs.dennisc.scenegraph.scale.Resizer.Z_AXIS };
	}

	@Override
	public double getValueForResizer( edu.cmu.cs.dennisc.scenegraph.scale.Resizer resizer ) {
		if( resizer == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.UNIFORM ) {
			return this.sgBox.yMaximum.getValue();
		} else if( resizer == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.X_AXIS ) {
			return this.sgBox.xMaximum.getValue();
		} else if( resizer == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.Y_AXIS ) {
			return this.sgBox.yMaximum.getValue();
		} else if( resizer == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.Z_AXIS ) {
			return this.sgBox.zMaximum.getValue();
		} else {
			assert false : resizer;
			return Double.NaN;
		}
	}

	@Override
	public void setValueForResizer( edu.cmu.cs.dennisc.scenegraph.scale.Resizer resizer, double value ) {
		if( value > 0.0 ) {
			if( resizer == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.UNIFORM ) {
				double prevValue = this.sgBox.yMaximum.getValue();
				double ratio = value / prevValue;
				double x = this.sgBox.xMaximum.getValue() * ratio;
				double z = this.sgBox.zMaximum.getValue() * ratio;

				this.sgBox.yMaximum.setValue( value );
				this.sgBox.xMaximum.setValue( x );
				this.sgBox.xMinimum.setValue( -x );
				this.sgBox.zMaximum.setValue( z );
				this.sgBox.zMinimum.setValue( -z );

			} else if( resizer == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.X_AXIS ) {
				this.sgBox.xMaximum.setValue( value );
				this.sgBox.xMinimum.setValue( -value );
			} else if( resizer == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.Y_AXIS ) {
				this.sgBox.yMaximum.setValue( value );
			} else if( resizer == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.Z_AXIS ) {
				this.sgBox.zMaximum.setValue( value );
				this.sgBox.zMinimum.setValue( -value );
			} else {
				assert false : resizer;
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, value );
		}
	}

	@Override
	public void setSize( edu.cmu.cs.dennisc.math.Dimension3 size ) {
		double x = size.x * 0.5;
		double z = size.z * 0.5;
		this.sgBox.xMinimum.setValue( -x );
		this.sgBox.xMaximum.setValue( +x );
		this.sgBox.yMaximum.setValue( size.y );
		this.sgBox.zMinimum.setValue( -z );
		this.sgBox.zMaximum.setValue( +z );
	}

	private final org.lgna.story.SBox abstraction;
	private final edu.cmu.cs.dennisc.scenegraph.Box sgBox = new edu.cmu.cs.dennisc.scenegraph.Box();
}
