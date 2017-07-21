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

package edu.cmu.cs.dennisc.scenegraph.graphics;

/**
 * @author Dennis Cosgrove
 */
public abstract class Bubble extends ShapeEnclosedText {
	public static interface Originator {
		public void calculate(
				java.awt.geom.Point2D.Float out_originOfTail,
				java.awt.geom.Point2D.Float out_bodyConnectionLocationOfTail,
				java.awt.geom.Point2D.Float out_textBoundsOffset,
				Bubble bubble,
				edu.cmu.cs.dennisc.render.RenderTarget renderTarget,
				java.awt.Rectangle actualViewport,
				edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera,
				java.awt.geom.Dimension2D textSize
				);
	}

	public enum PositionPreference
	{
		AUTOMATIC,
		TOP_LEFT,
		TOP_CENTER,
		TOP_RIGHT
	}

	public static final edu.cmu.cs.dennisc.color.Color4f DEFAULT_TEXT_COLOR = edu.cmu.cs.dennisc.color.Color4f.BLACK;
	public static final java.awt.Font DEFAULT_FONT = new java.awt.Font( null, java.awt.Font.PLAIN, 12 );
	public static final edu.cmu.cs.dennisc.color.Color4f DEFAULT_FILL_COLOR = edu.cmu.cs.dennisc.color.Color4f.WHITE;
	public static final edu.cmu.cs.dennisc.color.Color4f DEFAULT_OUTLINE_COLOR = edu.cmu.cs.dennisc.color.Color4f.BLACK;
	public static final PositionPreference DEFAULT_POSITION_PREFERENCE = PositionPreference.AUTOMATIC;

	public Bubble( Originator originator, PositionPreference positionPreference ) {
		super( DEFAULT_TEXT_COLOR, DEFAULT_FONT, DEFAULT_FILL_COLOR, DEFAULT_OUTLINE_COLOR );
		this.originator = originator;
		this.positionPreference = positionPreference;
	}

	public Bubble( Originator originator ) {
		this( originator, DEFAULT_POSITION_PREFERENCE );
	}

	public Originator getOriginator() {
		return this.originator;
	}

	public PositionPreference getPositionPreference() {
		return this.positionPreference;
	}

	//todo: better name
	public final edu.cmu.cs.dennisc.property.DoubleProperty portion = new edu.cmu.cs.dennisc.property.DoubleProperty( this, 0.0 );

	private final Originator originator;
	private final PositionPreference positionPreference;
}
