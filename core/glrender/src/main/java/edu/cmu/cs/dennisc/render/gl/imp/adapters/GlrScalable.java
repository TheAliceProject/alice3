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
package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.PickParameters;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

/**
 * @author Dennis Cosgrove
 */
public class GlrScalable extends GlrComposite<edu.cmu.cs.dennisc.scenegraph.Scalable> {
	@Override
	public void renderOpaque( RenderContext rc ) {
		if( this.isIdentity ) {
			super.renderOpaque( rc );
		} else {
			rc.gl.glPushMatrix();
			rc.incrementScaledCount();
			try {
				rc.gl.glScaled( this.x, this.y, this.z );
				super.renderOpaque( rc );
			} finally {
				rc.decrementScaledCount();
				rc.gl.glPopMatrix();
			}
		}
	}

	@Override
	public void renderGhost( RenderContext rc, GlrGhost root ) {
		if( this.isIdentity ) {
			super.renderGhost( rc, root );
		} else {
			rc.gl.glPushMatrix();
			rc.incrementScaledCount();
			try {
				rc.gl.glScaled( this.x, this.y, this.z );
				super.renderGhost( rc, root );
			} finally {
				rc.decrementScaledCount();
				rc.gl.glPopMatrix();
			}
		}
	}

	@Override
	public void pick( PickContext pc, PickParameters pickParameters ) {
		if( this.isIdentity ) {
			super.pick( pc, pickParameters );
		} else {
			pc.gl.glPushMatrix();
			pc.incrementScaledCount();
			try {
				pc.gl.glScaled( this.x, this.y, this.z );
				super.pick( pc, pickParameters );
			} finally {
				pc.decrementScaledCount();
				pc.gl.glPopMatrix();
			}
		}
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.scale ) {
			edu.cmu.cs.dennisc.math.Dimension3 scale = owner.scale.getValue();
			this.isIdentity = ( scale.x == 1.0 ) && ( scale.y == 1.0 ) && ( scale.z == 1.0 );
			this.x = scale.x;
			this.y = scale.y;
			this.z = scale.z;
		} else {
			super.propertyChanged( property );
		}
	}

	private double x = Double.NaN;
	private double y = Double.NaN;
	private double z = Double.NaN;
	private boolean isIdentity;
}
