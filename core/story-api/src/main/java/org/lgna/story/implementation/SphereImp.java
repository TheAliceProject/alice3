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
public class SphereImp extends ShapeImp {
	public SphereImp( org.lgna.story.SSphere abstraction ) {
		this.abstraction = abstraction;
		this.getSgVisuals()[ 0 ].geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.sgSphere } );
	}

	@Override
	public org.lgna.story.SSphere getAbstraction() {
		return this.abstraction;
	}

	@Override
	protected edu.cmu.cs.dennisc.property.InstanceProperty[] getScaleProperties() {
		return new edu.cmu.cs.dennisc.property.InstanceProperty[] { this.sgSphere.radius };
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.scale.Resizer[] getResizers() {
		return new edu.cmu.cs.dennisc.scenegraph.scale.Resizer[] { edu.cmu.cs.dennisc.scenegraph.scale.Resizer.UNIFORM };
	}

	@Override
	public double getValueForResizer( edu.cmu.cs.dennisc.scenegraph.scale.Resizer resizer ) {
		if( resizer == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.UNIFORM ) {
			return this.radius.getValue();
		} else {
			assert false : resizer;
			return Double.NaN;
		}
	}

	@Override
	public void setValueForResizer( edu.cmu.cs.dennisc.scenegraph.scale.Resizer resizer, double value ) {
		if( resizer == edu.cmu.cs.dennisc.scenegraph.scale.Resizer.UNIFORM ) {
			this.radius.setValue( value );
		} else {
			assert false : resizer;
		}
	}

	@Override
	public void setSize( edu.cmu.cs.dennisc.math.Dimension3 size ) {
		if( ( size.x != size.y ) || ( size.y != size.z ) ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "Invalid size for " + this.getClass().getSimpleName() + ": " + size );
		}
		this.radius.setValue( size.x * .5 );
	}

	private final org.lgna.story.SSphere abstraction;
	private final edu.cmu.cs.dennisc.scenegraph.Sphere sgSphere = new edu.cmu.cs.dennisc.scenegraph.Sphere();
	public final DoubleProperty radius = new DoubleProperty( SphereImp.this ) {
		@Override
		public Double getValue() {
			return SphereImp.this.sgSphere.radius.getValue();
		}

		@Override
		protected void handleSetValue( Double value ) {
			SphereImp.this.sgSphere.radius.setValue( value );
		}
	};
}
