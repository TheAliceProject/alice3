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

package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector4;
import edu.cmu.cs.dennisc.property.CopyableArrayProperty;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.RenderTarget;

import java.awt.Point;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractCamera extends Leaf {
	public Point transformToAWT( Point rv, Vector4 xyzw, RenderTarget renderTarget ) {
		return transformToAWT( rv, xyzw, renderTarget, this );
	}

	public Vector4 transformFromAWT( Vector4 rv, Point p, double z, RenderTarget renderTarget ) {
		return transformFromAWT( rv, p, z, renderTarget, this );
	}

	public Point transformToAWT_New( Vector4 xyzw, RenderTarget renderTarget ) {
		return transformToAWT_New( xyzw, renderTarget, this );
	}

	public Point transformToAWT_New( Point3 xyz, RenderTarget renderTarget ) {
		return transformToAWT_New( xyz, renderTarget, this );
	}

	public Vector4 transformFromAWT_NewVectorD4( Point p, double z, RenderTarget renderTarget ) {
		return transformFromAWT_NewVectorD4( p, z, renderTarget, this );
	}

	public Point3 transformFromAWT_NewPointD3( Point p, double z, RenderTarget renderTarget ) {
		return transformFromAWT_NewPointD3( p, z, renderTarget, this );
	}

	public final InstanceProperty<Background> background = new InstanceProperty<Background>( this, null );
	//public final edu.cmu.cs.dennisc.property.ListProperty< Layer > postRenderLayers = new edu.cmu.cs.dennisc.property.ListProperty< Layer >( this );
	public final CopyableArrayProperty<Layer> postRenderLayers = new CopyableArrayProperty<Layer>( this, new Layer[ 0 ] ) {
		@Override
		protected Layer[] createArray( int length ) {
			return new Layer[ length ];
		}

		@Override
		protected Layer createCopy( Layer src ) {
			//todo?
			return src;
		}
	};
}
