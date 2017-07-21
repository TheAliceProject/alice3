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

/**
 * @author Dennis Cosgrove
 */
public class Text extends Geometry {
	private static final String DEFAULT_TEXT = "";
	private static final java.awt.Font DEFAULT_FONT = new java.awt.Font( null, java.awt.Font.PLAIN, 12 );

	public edu.cmu.cs.dennisc.glyph.GlyphVector getGlyphVector() {
		return this.glyphVector;
	}

	protected void updateUnalignedBoundingBoxIfNecessary() {
		if( this.unalignedBoundingBox.isNaN() ) {
			java.awt.geom.Rectangle2D.Float bounds = this.glyphVector.getBounds();
			this.unalignedBoundingBox.setMinimum( bounds.x, bounds.y, 0 );
			this.unalignedBoundingBox.setMaximum( bounds.x + bounds.width, bounds.y + bounds.height, depth.getValue() );
		}

		if( this.unalignedBoundingBox.isNaN() ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( this );
			this.unalignedBoundingBox.setMinimum( 0, 0, 0 );
			this.unalignedBoundingBox.setMaximum( 0, 0, 0 );
		}
	}

	//todo: cache result?
	public edu.cmu.cs.dennisc.math.Vector3 getAlignmentOffset() {
		updateUnalignedBoundingBoxIfNecessary();
		edu.cmu.cs.dennisc.math.Vector3 alignmentOffset = new edu.cmu.cs.dennisc.math.Vector3();

		LeftToRightAlignment leftToRightAlignment = this.leftToRightAlignment.getValue();
		TopToBottomAlignment topToBottomAlignment = this.topToBottomAlignment.getValue();
		FrontToBackAlignment frontToBackAlignment = this.frontToBackAlignment.getValue();

		if( leftToRightAlignment == LeftToRightAlignment.ALIGN_CENTER_OF_LEFT_AND_RIGHT ) {
			alignmentOffset.x = -( this.unalignedBoundingBox.getXMinimum() + ( this.unalignedBoundingBox.getWidth() / 2 ) );
		} else if( leftToRightAlignment == LeftToRightAlignment.ALIGN_LEFT ) {
			alignmentOffset.x = -this.unalignedBoundingBox.getXMinimum();
		} else if( leftToRightAlignment == LeftToRightAlignment.ALIGN_RIGHT ) {
			alignmentOffset.x = -this.unalignedBoundingBox.getXMaximum();
		} else {
			throw new RuntimeException();
		}

		if( topToBottomAlignment == TopToBottomAlignment.ALIGN_CENTER_OF_TOP_AND_BOTTOM ) {
			alignmentOffset.y = -( this.unalignedBoundingBox.getYMinimum() + ( this.unalignedBoundingBox.getHeight() / 2 ) );
		} else if( topToBottomAlignment == TopToBottomAlignment.ALIGN_TOP ) {
			alignmentOffset.y = -this.unalignedBoundingBox.getYMaximum();
		} else if( topToBottomAlignment == TopToBottomAlignment.ALIGN_BOTTOM ) {
			alignmentOffset.y = -this.unalignedBoundingBox.getYMinimum();
		} else if( topToBottomAlignment == TopToBottomAlignment.ALIGN_BASELINE ) {
			alignmentOffset.y = 0;
		} else if( topToBottomAlignment == TopToBottomAlignment.ALIGN_CENTER_OF_TOP_AND_BASELINE ) {
			alignmentOffset.y = -this.unalignedBoundingBox.getYMaximum() / 2;
		} else {
			throw new RuntimeException();
		}

		if( frontToBackAlignment == FrontToBackAlignment.ALIGN_CENTER_OF_FRONT_AND_BACK ) {
			alignmentOffset.z = -depth.getValue() / 2;
		} else if( frontToBackAlignment == FrontToBackAlignment.ALIGN_FRONT ) {
			alignmentOffset.z = 0;
		} else if( frontToBackAlignment == FrontToBackAlignment.ALIGN_BACK ) {
			alignmentOffset.z = -depth.getValue();
		} else {
			throw new RuntimeException();
		}

		return alignmentOffset;
	}

	@Override
	protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		updateUnalignedBoundingBoxIfNecessary();

		edu.cmu.cs.dennisc.math.Vector3 alignmentOffset = getAlignmentOffset();

		boundingBox.set( this.unalignedBoundingBox );
		boundingBox.translate( alignmentOffset );

	}

	@Override
	protected void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		//todo
	}

	@Override
	protected void updatePlane( edu.cmu.cs.dennisc.math.Vector3 forward, edu.cmu.cs.dennisc.math.Vector3 upGuide, edu.cmu.cs.dennisc.math.Point3 translation ) {
		throw new RuntimeException( "TODO" );
	}

	@Override
	public void transform( edu.cmu.cs.dennisc.math.AbstractMatrix4x4 trans ) {
		throw new RuntimeException( "TODO" );
	}

	public final edu.cmu.cs.dennisc.property.StringProperty text = new edu.cmu.cs.dennisc.property.StringProperty( this, DEFAULT_TEXT ) {
		@Override
		public void setValue( String value ) {
			markBoundsDirty();
			super.setValue( value );
			glyphVector.setText( value.toString() );
			unalignedBoundingBox.setNaN();
			fireBoundChanged();
		}
	};
	public final edu.cmu.cs.dennisc.property.InstanceProperty<java.awt.Font> font = new edu.cmu.cs.dennisc.property.InstanceProperty<java.awt.Font>( this, DEFAULT_FONT ) {
		@Override
		public void setValue( java.awt.Font value ) {
			markBoundsDirty();
			super.setValue( value );
			glyphVector.setFont( value );
			unalignedBoundingBox.setNaN();
			fireBoundChanged();
		}
	};

	public final BoundDoubleProperty depth = new BoundDoubleProperty( this, 0.25 ) {
		@Override
		public void setValue( Double value ) {
			super.setValue( value );
			unalignedBoundingBox.setNaN();
		}
	};

	public final edu.cmu.cs.dennisc.property.InstanceProperty<LeftToRightAlignment> leftToRightAlignment = new edu.cmu.cs.dennisc.property.InstanceProperty<LeftToRightAlignment>( this, LeftToRightAlignment.ALIGN_CENTER_OF_LEFT_AND_RIGHT ) {
		@Override
		public void setValue( LeftToRightAlignment value ) {
			if( edu.cmu.cs.dennisc.java.util.Objects.notEquals( value, this.getValue() ) ) {
				markBoundsDirty();
				super.setValue( value );
				unalignedBoundingBox.setNaN();
				fireBoundChanged();
			}
		}
	};
	public final edu.cmu.cs.dennisc.property.InstanceProperty<TopToBottomAlignment> topToBottomAlignment = new edu.cmu.cs.dennisc.property.InstanceProperty<TopToBottomAlignment>( this, TopToBottomAlignment.ALIGN_BASELINE ) {
		@Override
		public void setValue( TopToBottomAlignment value ) {
			if( edu.cmu.cs.dennisc.java.util.Objects.notEquals( value, this.getValue() ) ) {
				markBoundsDirty();
				super.setValue( value );
				unalignedBoundingBox.setNaN();
				fireBoundChanged();
			}
		}
	};
	public final edu.cmu.cs.dennisc.property.InstanceProperty<FrontToBackAlignment> frontToBackAlignment = new edu.cmu.cs.dennisc.property.InstanceProperty<FrontToBackAlignment>( this, FrontToBackAlignment.ALIGN_CENTER_OF_FRONT_AND_BACK ) {
		@Override
		public void setValue( FrontToBackAlignment value ) {
			if( edu.cmu.cs.dennisc.java.util.Objects.notEquals( value, this.getValue() ) ) {
				markBoundsDirty();
				super.setValue( value );
				unalignedBoundingBox.setNaN();
				fireBoundChanged();
			}
		}
	};

	private final edu.cmu.cs.dennisc.math.AxisAlignedBox unalignedBoundingBox = edu.cmu.cs.dennisc.math.AxisAlignedBox.createNaN();

	private final edu.cmu.cs.dennisc.glyph.GlyphVector glyphVector = new edu.cmu.cs.dennisc.glyph.GlyphVector( DEFAULT_TEXT, DEFAULT_FONT, -1, -1 );
}
