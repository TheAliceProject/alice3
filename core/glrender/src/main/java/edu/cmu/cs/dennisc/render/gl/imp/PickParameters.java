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

package edu.cmu.cs.dennisc.render.gl.imp;

/**
 * @author Dennis Cosgrove
 */
public class PickParameters {
	private final java.util.List<edu.cmu.cs.dennisc.render.PickResult> pickResults = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	private final edu.cmu.cs.dennisc.render.RenderTarget renderTarget;
	private final edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera;
	private final int x;
	private final int y;
	private final boolean isSubElementRequired;
	private final edu.cmu.cs.dennisc.render.PickObserver pickObserver;

	public PickParameters( edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, int x, int y, boolean isSubElementRequired, edu.cmu.cs.dennisc.render.PickObserver pickObserver ) {
		this.renderTarget = renderTarget;
		this.sgCamera = sgCamera;
		this.x = x;
		this.y = y;
		this.isSubElementRequired = isSubElementRequired;
		this.pickObserver = pickObserver;
	}

	public void addPickResult( edu.cmu.cs.dennisc.scenegraph.Component source, edu.cmu.cs.dennisc.scenegraph.Visual sgVisual, boolean isFrontFacing, edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry, int subElement, edu.cmu.cs.dennisc.math.Point3 xyzInSource ) {
		this.pickResults.add( new edu.cmu.cs.dennisc.render.PickResult( source, sgVisual, isFrontFacing, sgGeometry, subElement, xyzInSource ) );
	}

	public java.util.List<edu.cmu.cs.dennisc.render.PickResult> accessAllPickResults() {
		return this.pickResults;
	}

	public edu.cmu.cs.dennisc.render.PickResult accessFrontMostPickResult() {
		edu.cmu.cs.dennisc.render.PickResult rv;
		if( this.pickResults.isEmpty() ) {
			rv = new edu.cmu.cs.dennisc.render.PickResult( this.sgCamera );
		} else {
			rv = this.pickResults.get( 0 );
		}
		return rv;
	}

	public edu.cmu.cs.dennisc.render.RenderTarget getRenderTarget() {
		return this.renderTarget;
	}

	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCamera() {
		return this.sgCamera;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getFlippedY( java.awt.Rectangle actualViewport ) {
		return actualViewport.height - this.y;
	}

	public boolean isSubElementRequired() {
		return this.isSubElementRequired;
	}

	public edu.cmu.cs.dennisc.render.PickObserver getPickObserver() {
		return this.pickObserver;
	}
}
