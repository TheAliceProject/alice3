/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public class Text extends Geometry {
	private static final String DEFAULT_TEXT = "";
	private static final java.awt.Font DEFAULT_FONT = new java.awt.Font( null, java.awt.Font.PLAIN, 12 );
	
	public final edu.cmu.cs.dennisc.property.StringProperty text = new edu.cmu.cs.dennisc.property.StringProperty( this, DEFAULT_TEXT ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, String value) {
			super.setValue( owner, value );
			m_glyphVector.setText( value.toString() );
			m_unalignedBoundingBox.setNaN();
			fireBoundChange();
		}
	};
	public final edu.cmu.cs.dennisc.property.InstanceProperty< java.awt.Font > font = new edu.cmu.cs.dennisc.property.InstanceProperty< java.awt.Font >( this, DEFAULT_FONT ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, java.awt.Font value) {
			super.setValue( owner, value );
			m_glyphVector.setFont( value );
			m_unalignedBoundingBox.setNaN();
			fireBoundChange();
		}
	};

	public final BoundDoubleProperty depth = new BoundDoubleProperty( this, 0.25 ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, Double value) {
			super.setValue( owner, value );
			m_unalignedBoundingBox.setNaN();
		}
	};

	public final edu.cmu.cs.dennisc.property.InstanceProperty< LeftToRightAlignment > leftToRightAlignment = new edu.cmu.cs.dennisc.property.InstanceProperty< LeftToRightAlignment >( this, LeftToRightAlignment.ALIGN_CENTER_OF_LEFT_AND_RIGHT ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, LeftToRightAlignment value) {
			super.setValue( owner, value );
			m_unalignedBoundingBox.setNaN();
			fireBoundChange();
		}
	};
	public final edu.cmu.cs.dennisc.property.InstanceProperty< TopToBottomAlignment > topToBottomAlignment = new edu.cmu.cs.dennisc.property.InstanceProperty< TopToBottomAlignment >( this, TopToBottomAlignment.ALIGN_BASELINE ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, TopToBottomAlignment value) {
			super.setValue( owner, value );
			m_unalignedBoundingBox.setNaN();
			fireBoundChange();
		}
	};
	public final edu.cmu.cs.dennisc.property.InstanceProperty< FrontToBackAlignment > frontToBackAlignment = new edu.cmu.cs.dennisc.property.InstanceProperty< FrontToBackAlignment >( this, FrontToBackAlignment.ALIGN_CENTER_OF_FRONT_AND_BACK ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, FrontToBackAlignment value) {
			super.setValue( owner, value );
			m_unalignedBoundingBox.setNaN();
			fireBoundChange();
		}
	};

	private edu.cmu.cs.dennisc.math.AxisAlignedBox m_unalignedBoundingBox = new edu.cmu.cs.dennisc.math.AxisAlignedBox( Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN );

	

	private edu.cmu.cs.dennisc.glyph.GlyphVector m_glyphVector = new edu.cmu.cs.dennisc.glyph.GlyphVector( DEFAULT_TEXT, DEFAULT_FONT, -1, -1 );
	public edu.cmu.cs.dennisc.glyph.GlyphVector getGlyphVector() {
		return m_glyphVector;
	}
	

	protected void updateUnalignedBoundingBoxIfNecessary() {
		if( m_unalignedBoundingBox.isNaN() ) {
			java.awt.geom.Rectangle2D.Float bounds = m_glyphVector.getBounds();
			m_unalignedBoundingBox.setMinimum( bounds.x, bounds.y, 0 );
			m_unalignedBoundingBox.setMaximum( bounds.x+bounds.width, bounds.y+bounds.height, depth.getValue() );
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
			alignmentOffset.x = -( m_unalignedBoundingBox.getXMinimum() + m_unalignedBoundingBox.getWidth()/2 );
		} else if( leftToRightAlignment == LeftToRightAlignment.ALIGN_LEFT ) {
			alignmentOffset.x = -m_unalignedBoundingBox.getXMinimum();
		} else if( leftToRightAlignment == LeftToRightAlignment.ALIGN_RIGHT ) {
			alignmentOffset.x = -m_unalignedBoundingBox.getXMaximum();
		} else {
			throw new RuntimeException();
		}

		if( topToBottomAlignment == TopToBottomAlignment.ALIGN_CENTER_OF_TOP_AND_BOTTOM ) {
			alignmentOffset.y = -( m_unalignedBoundingBox.getYMinimum() + m_unalignedBoundingBox.getHeight()/2 );
		} else if( topToBottomAlignment == TopToBottomAlignment.ALIGN_TOP ) {
			alignmentOffset.y = -m_unalignedBoundingBox.getYMaximum();
		} else if( topToBottomAlignment == TopToBottomAlignment.ALIGN_BOTTOM ) {
			alignmentOffset.y = -m_unalignedBoundingBox.getYMinimum();
		} else if( topToBottomAlignment == TopToBottomAlignment.ALIGN_BASELINE ) {
			alignmentOffset.y = 0;
		} else if( topToBottomAlignment == TopToBottomAlignment.ALIGN_CENTER_OF_TOP_AND_BASELINE ) {
			alignmentOffset.y = -m_unalignedBoundingBox.getYMaximum()/2;
		} else {
			throw new RuntimeException();
		}
		
		if( frontToBackAlignment == FrontToBackAlignment.ALIGN_CENTER_OF_FRONT_AND_BACK ) {
			alignmentOffset.z = -depth.getValue()/2;
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

		
		boundingBox.set( m_unalignedBoundingBox );
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
}
