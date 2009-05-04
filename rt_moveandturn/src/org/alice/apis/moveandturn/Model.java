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

package org.alice.apis.moveandturn;

import edu.cmu.cs.dennisc.alice.annotations.*; 

/**
 * @author Dennis Cosgrove
 */
public abstract class Model extends Transformable {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Scale > VISUAL_SCALE_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Scale >( Model.class, "VisualScale" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Color > COLOR_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Color >( Model.class, "Color" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > OPACITY_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( Model.class, "Opacity" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< FillingStyle > FILLING_STYLE_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< FillingStyle >( Model.class, "FillingStyle" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< ShadingStyle > SHADING_STYLE_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< ShadingStyle >( Model.class, "ShadingStyle" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Boolean > IS_SHOWING_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Boolean >( Model.class, "IsShowing" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Boolean > IS_BOUNDING_BOX_SHOWING_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Boolean >( Model.class, "IsBoundingBoxShowing" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Boolean > IS_BOUNDING_SPHERE_SHOWING_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Boolean >( Model.class, "IsBoundingSphereShowing" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< SurfaceTexture > SURFACE_TEXTURE_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< SurfaceTexture >( Model.class, "SurfaceTexture" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< SurfaceTexture[] > POTENTIAL_SURFACE_TEXTURES_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< SurfaceTexture[] >( Model.class, "PotentialSurfaceTextures" );
	
	private edu.cmu.cs.dennisc.scenegraph.Visual m_sgVisual = new edu.cmu.cs.dennisc.scenegraph.Visual();
	private edu.cmu.cs.dennisc.scenegraph.SingleAppearance m_sgAppearance = new edu.cmu.cs.dennisc.scenegraph.SingleAppearance();

	private edu.cmu.cs.dennisc.math.Matrix3x3 m_originalScale = edu.cmu.cs.dennisc.math.Matrix3x3.createIdentity();
	private java.util.List< SurfaceTexture > m_potentialSurfaceTextures = new java.util.LinkedList< SurfaceTexture >();
	

	public Model() {
		putElement( m_sgVisual );
		putElement( m_sgAppearance );

		m_sgVisual.frontFacingAppearance.setValue( m_sgAppearance );
		m_sgVisual.setParent( getSGTransformable() );
	}
	
	protected abstract void createSGGeometryIfNecessary();
	protected abstract edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry();
	
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.scenegraph.Visual getSGVisual() {
		return m_sgVisual;
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.scenegraph.SingleAppearance getSGSingleAppearance() {
		return m_sgAppearance;
	}

	
	@Override
	protected void realize() {
		super.realize();
		m_originalScale.setValue( m_sgVisual.scale.getValue() );
		createSGGeometryIfNecessary();
		edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry = getSGGeometry();
		if( sgGeometry != null ) {
			putElement( sgGeometry );
			m_sgVisual.geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { sgGeometry } );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: no geometry: ", this );
		}
	}
	
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public Double getResizeWidthAmount() { 
		return m_sgVisual.scale.getValue().right.x / m_originalScale.right.x;
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public Double getResizeHeightAmount() { 
		return m_sgVisual.scale.getValue().up.y / m_originalScale.up.y;
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public Double getResizeDepthAmount() { 
		return m_sgVisual.scale.getValue().backward.z / m_originalScale.backward.z;
	}

	private Model getPartNamed( String name ) {
		assert name != null;
		for( Transformable component : getComponentIterable() ) {
			if( component instanceof Model ) {
				if( name.equalsIgnoreCase( component.getName() ) ) {
					return (Model)component;
				}
			}
		}
		return null;
	}
	private Model getPart( String[] path, int index ) {
		Model part = getPartNamed( path[ index ] );
		if( index == path.length -1 ) {
			return part;
		} else {
			return part.getPart( path, index+1 );
		}
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public Model getPart( String... path ) {
		return getPart( path, 0 );
	}


	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	public Scale getVisualScale() {
		return new Scale( m_sgVisual.scale.getValue() );
	}
	public void setVisualScale( Scale scale ) {
		m_sgVisual.scale.setValue( scale.getInternal() );
	}

	@Override
	public void setName( String name ) {
		super.setName( name );
		m_sgVisual.setName( name + ".m_sgVisual" );
		m_sgAppearance.setName( name + ".m_sgAppearance" );
//		if( m_sgITA != null ) {
//			m_sgITA.setName( m_name + ".m_sgITA" );
//		}
	}

	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Color getColor() {
		return new Color( m_sgAppearance.diffuseColor.getValue() );
	}

	private HowMuch adjustHowMuchIfNecessary( HowMuch howMuch ) {
		//todo
		return howMuch;
	}
	public void setColor( final Color color, Number duration, final Style style, HowMuch howMuch ) {
		final double actualDuration = adjustDurationIfNecessary( duration );
		HowMuch actualHowMuch = adjustHowMuchIfNecessary( howMuch );
		
		java.util.List< Model > models = findAllMatches( actualHowMuch, Model.class );
		Runnable[] runnables = new Runnable[ models.size() ];
		int i = 0;
		for( final Model model : models ) {
			runnables[ i++ ] = new Runnable() { 
				public void run() {
					if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( actualDuration, RIGHT_NOW ) ) {
						model.m_sgAppearance.setDiffuseColor( color.getInternal() );
					} else {
						perform( new edu.cmu.cs.dennisc.color.animation.Color4fAnimation( actualDuration, style, getColor().getInternal(), color.getInternal() ) {
							@Override
							protected void updateValue( edu.cmu.cs.dennisc.color.Color4f color ) {
								model.m_sgAppearance.setDiffuseColor( color );
							}
						} );
					}
				}
			};
		}
		edu.cmu.cs.dennisc.alice.virtualmachine.DoTogether.invokeAndWait( runnables );
	}
	public void setColor( Color color, Number duration, Style style ) {
		setColor( color, duration, style, DEFAULT_HOW_MUCH );
	}
	public void setColor( Color color, Number duration ) {
		setColor( color, duration, DEFAULT_STYLE );
	}
	public void setColor( Color color ) {
		setColor( color, DEFAULT_DURATION );
	}

	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Double getOpacity() {
		return (double)m_sgAppearance.opacity.getValue();
	}

	public void setOpacity( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=Portion.class )
			final Number opacity,
			Number duration, 
			final Style style, 
			HowMuch howMuch 
		) {
		final double actualDuration = adjustDurationIfNecessary( duration );
		HowMuch actualHowMuch = adjustHowMuchIfNecessary( howMuch );
		
		java.util.List< Model > models = findAllMatches( actualHowMuch, Model.class );
		Runnable[] runnables = new Runnable[ models.size() ];
		int i = 0;
		for( final Model model : models ) {
			runnables[ i++ ] = new Runnable() { 
				public void run() {
					if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( actualDuration, RIGHT_NOW ) ) {
						model.m_sgAppearance.setOpacity( opacity.floatValue() );
					} else {
						perform( new edu.cmu.cs.dennisc.animation.interpolation.FloatAnimation( actualDuration, style, getOpacity().floatValue(), opacity.floatValue() ) {
							@Override
							protected void updateValue( Float opacity ) {
								model.m_sgAppearance.setOpacity( opacity );
							}
						} );
					}
				}
			};
		}
		edu.cmu.cs.dennisc.alice.virtualmachine.DoTogether.invokeAndWait( runnables );
	}
	public void setOpacity( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=Portion.class )
			Number opacity,
			Number duration, 
			Style style 
		) {
		setOpacity( opacity, duration, style, DEFAULT_HOW_MUCH );
	}
	public void setOpacity( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=Portion.class )
			Number opacity,
			Number duration 
		) {
		setOpacity( opacity, duration, DEFAULT_STYLE );
	}
	public void setOpacity( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=Portion.class )
			Number opacity
		) {
		setOpacity( opacity, DEFAULT_DURATION );
	}
		
	private SurfaceTexture m_surfaceTexture;
	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	public SurfaceTexture getSurfaceTexture() {
		return m_surfaceTexture;
	}
	public void setSurfaceTexture( SurfaceTexture surfaceTexture ) {
		m_surfaceTexture = surfaceTexture;
		//todo: listen to changes on surfaceTexture
		if( surfaceTexture != null ) {
			m_sgAppearance.setDiffuseColorTexture( m_surfaceTexture.getBufferedImageTexture() );
		} else {
			m_sgAppearance.setDiffuseColorTexture( null );
		}
	}

	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	public FillingStyle getFillingStyle() {
		return FillingStyle.valueOf( m_sgAppearance.fillingStyle.getValue() );
	}
	//todo: howMuch
	public void setFillingStyle( FillingStyle fillingStyle ) {
		m_sgAppearance.setFillingStyle( fillingStyle.getSGFillingStyle() );
	}

	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	public ShadingStyle getShadingStyle() {
		return ShadingStyle.valueOf( m_sgAppearance.shadingStyle.getValue() );
	}
	//todo: howMuch
	public void setShadingStyle( ShadingStyle shadingStyle ) {
		m_sgAppearance.setShadingStyle( shadingStyle.getSGShadingStyle() );
	}

	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Boolean isShowing() {
		return m_sgVisual.isShowing.getValue();
	}
	//todo: howMuch
	public void setShowing( Boolean isShowing ) {
		m_sgVisual.isShowing.setValue( isShowing );
	}

	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void addPotentialSurfaceTexture( SurfaceTexture surfaceTexture ) {
		assert surfaceTexture != null;
		synchronized( m_potentialSurfaceTextures ) {
			m_potentialSurfaceTextures.add( surfaceTexture );
		}
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void removePotentialSurfaceTexture( SurfaceTexture surfaceTexture ) {
		assert surfaceTexture != null;
		synchronized( m_potentialSurfaceTextures ) {
			assert m_potentialSurfaceTextures.contains( surfaceTexture );
			m_potentialSurfaceTextures.remove( surfaceTexture );
		}
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public Iterable< SurfaceTexture > getPotentialSurfaceTextureIterable() {
		return m_potentialSurfaceTextures;
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void clearPotentialSurfaceTextures() {
		synchronized( m_potentialSurfaceTextures ) {
			java.util.ListIterator< SurfaceTexture > listIterator = m_potentialSurfaceTextures.listIterator();
			while( listIterator.hasNext() ) {
				removePotentialSurfaceTexture( listIterator.next() );
			}
		}
	}
	private SurfaceTexture[] getPotentialSurfaceTextures( SurfaceTexture[] rv ) {
		synchronized( m_potentialSurfaceTextures ) {
			return m_potentialSurfaceTextures.toArray( rv );
		}
	}
	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	public SurfaceTexture[] getPotentialSurfaceTextures() {
		return getPotentialSurfaceTextures( new SurfaceTexture[ m_potentialSurfaceTextures.size() ] );
	}
	public void setPotentialSurfaceTextures( SurfaceTexture... surfaceTextures ) {
		synchronized( m_potentialSurfaceTextures ) {
			clearPotentialSurfaceTextures();
			for( SurfaceTexture surfaceTexture : surfaceTextures ) {
				addPotentialSurfaceTexture( surfaceTexture );
			}
		}
	}

	@Override
	protected void applyScale( edu.cmu.cs.dennisc.math.Vector3 axis, boolean isScootDesired ) {
		super.applyScale( axis, isScootDesired );
		edu.cmu.cs.dennisc.math.Matrix3x3 scale = m_sgVisual.scale.getValue();
		edu.cmu.cs.dennisc.math.ScaleUtilities.applyScale( scale, axis );
		m_sgVisual.scale.setValue( scale );
	}

	@Override
	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans, boolean isOriginIncluded ) {
		super.updateCumulativeBound( rv, trans, isOriginIncluded );
		if( m_sgVisual.isShowing.getValue() ) {
			rv.add( m_sgVisual, trans );
		}
		return rv;
	}	


	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resize( Number amount, Number duration, HowMuch howMuch, Style style ) {
		applyScale( new edu.cmu.cs.dennisc.math.Vector3( amount.doubleValue(), amount.doubleValue(), amount.doubleValue() ), duration, howMuch, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resize( Number amount, Number duration, HowMuch howMuch ) {
		resize( amount, duration, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resize( Number amount, Number duration ) {
		resize( amount, duration, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resize( Number amount ) {
		resize( amount, DEFAULT_DURATION );
	}
	
	public static final ResizePolicy DEFAULT_RESIZE_POLICY = ResizePolicy.PRESERVE_VOLUME;
	public static final SetSizePolicy DEFAULT_SET_SIZE_POLICY = SetSizePolicy.PRESERVE_ASPECT_RATIO;

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resizeWidth( Number amount, Number duration, ResizePolicy resizePolicy, HowMuch howMuch, Style style ) {
		applyScale( Dimension.LEFT_TO_RIGHT.getResizeAxis( amount.doubleValue(), resizePolicy ), duration, howMuch, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeWidth( Number amount, Number duration, ResizePolicy resizePolicy, HowMuch howMuch ) {
		resizeWidth( amount, duration, resizePolicy, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeWidth( Number amount, Number duration, ResizePolicy resizePolicy ) {
		resizeWidth( amount, duration, resizePolicy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeWidth( Number amount, Number duration ) {
		resizeWidth( amount, duration, DEFAULT_RESIZE_POLICY );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeWidth( Number amount ) {
		resizeWidth( amount, DEFAULT_DURATION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resizeHeight( Number amount, Number duration, ResizePolicy resizePolicy, HowMuch howMuch, Style style ) {
		applyScale(  Dimension.TOP_TO_BOTTOM.getResizeAxis( amount.doubleValue(), resizePolicy ), duration, howMuch, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeHeight( Number amount, Number duration, ResizePolicy resizePolicy, HowMuch howMuch ) {
		resizeHeight( amount, duration, resizePolicy, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeHeight( Number amount, Number duration, ResizePolicy resizePolicy ) {
		resizeHeight( amount, duration, resizePolicy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeHeight( Number amount, Number duration ) {
		resizeHeight( amount, duration, DEFAULT_RESIZE_POLICY );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeHeight( Number amount ) {
		resizeHeight( amount, DEFAULT_DURATION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void resizeDepth( Number amount, Number duration, ResizePolicy resizePolicy, HowMuch howMuch, Style style ) {
		applyScale( Dimension.FRONT_TO_BACK.getResizeAxis( amount.doubleValue(), resizePolicy ), duration, howMuch, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeDepth( Number amount, Number duration, ResizePolicy resizePolicy, HowMuch howMuch ) {
		resizeDepth( amount, duration, resizePolicy, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeDepth( Number amount, Number duration, ResizePolicy resizePolicy ) {
		resizeDepth( amount, duration, resizePolicy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeDepth( Number amount, Number duration ) {
		resizeDepth( amount, duration, DEFAULT_RESIZE_POLICY );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void resizeDepth( Number amount ) {
		resizeDepth( amount, DEFAULT_DURATION );
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void setWidth( Number amount, Number duration, SetSizePolicy setSizePolicy, HowMuch howMuch, Style style ) {
		double factor = amount.doubleValue() / getWidth();
		assert Double.isNaN( factor ) == false;
		if( setSizePolicy.isAspectRatioPreserved() ) {
			resize( factor, duration, howMuch, style );
		} else {
			resizeWidth( factor, duration, setSizePolicy.getResizePolicy(), howMuch, style );
		}
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setWidth( Number amount, Number duration, SetSizePolicy setSizePolicy, HowMuch howMuch ) {
		setWidth( amount, duration, setSizePolicy, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setWidth( Number amount, Number duration, SetSizePolicy setSizePolicy ) {
		setWidth( amount, duration, setSizePolicy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setWidth( Number amount, Number duration ) {
		setWidth( amount, duration, DEFAULT_SET_SIZE_POLICY );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setWidth( Number amount ) {
		setWidth( amount, DEFAULT_DURATION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void setHeight( Number amount, Number duration, SetSizePolicy setSizePolicy, HowMuch howMuch, Style style ) {
		double factor = amount.doubleValue() / getHeight();
		assert Double.isNaN( factor ) == false;
		if( setSizePolicy.isAspectRatioPreserved() ) {
			resize( factor, duration, howMuch, style );
		} else {
			resizeHeight( factor, duration, setSizePolicy.getResizePolicy(), howMuch, style );
		}
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setHeight( Number amount, Number duration, SetSizePolicy setSizePolicy, HowMuch howMuch ) {
		setHeight( amount, duration, setSizePolicy, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setHeight( Number amount, Number duration, SetSizePolicy setSizePolicy ) {
		setHeight( amount, duration, setSizePolicy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setHeight( Number amount, Number duration ) {
		setHeight( amount, duration, DEFAULT_SET_SIZE_POLICY );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setHeight( Number amount ) {
		setHeight( amount, DEFAULT_DURATION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void setDepth( Number amount, Number duration, SetSizePolicy setSizePolicy, HowMuch howMuch, Style style ) {
		double factor = amount.doubleValue() / getDepth();
		assert Double.isNaN( factor ) == false;
		if( setSizePolicy.isAspectRatioPreserved() ) {
			resize( factor, duration, howMuch, style );
		} else {
			resizeDepth( factor, duration, setSizePolicy.getResizePolicy(), howMuch, style );
		}
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setDepth( Number amount, Number duration, SetSizePolicy setSizePolicy, HowMuch howMuch ) {
		setDepth( amount, duration, setSizePolicy, howMuch, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setDepth( Number amount, Number duration, SetSizePolicy setSizePolicy ) {
		setDepth( amount, duration, setSizePolicy, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setDepth( Number amount, Number duration ) {
		setDepth( amount, duration, DEFAULT_SET_SIZE_POLICY );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void setDepth( Number amount ) {
		setDepth( amount, DEFAULT_DURATION );
	}
}
