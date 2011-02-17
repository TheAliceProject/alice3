/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.alice.apis.moveandturn;

import edu.cmu.cs.dennisc.alice.annotations.*; 
import edu.cmu.cs.dennisc.scenegraph.Appearance;

/**
 * @author Dennis Cosgrove
 */
public abstract class Model extends AbstractModel {
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
	
	protected edu.cmu.cs.dennisc.scenegraph.Visual m_sgVisual;
	protected edu.cmu.cs.dennisc.scenegraph.SingleAppearance m_sgAppearance;

	private java.util.List< SurfaceTexture > m_potentialSurfaceTextures = new java.util.LinkedList< SurfaceTexture >();
	

	public Model() {
        m_sgVisual = this.createSGVisual();
        m_sgAppearance = (edu.cmu.cs.dennisc.scenegraph.SingleAppearance)m_sgVisual.frontFacingAppearance.getValue();
        m_sgVisual.setParent( getSGTransformable() );
        putElement( m_sgVisual );
		putElement( m_sgAppearance );
	}
	
	protected edu.cmu.cs.dennisc.scenegraph.Visual createSGVisual() {
        edu.cmu.cs.dennisc.scenegraph.Visual rv = new edu.cmu.cs.dennisc.scenegraph.Visual();
	    rv.frontFacingAppearance.setValue( new edu.cmu.cs.dennisc.scenegraph.SingleAppearance() );
	    return rv;
	}
	
	protected abstract void createSGGeometryIfNecessary();
	protected abstract edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry();
	
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	@Override
	public edu.cmu.cs.dennisc.scenegraph.Visual getSGVisual() {
		return m_sgVisual;
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.scenegraph.SingleAppearance getSGSingleAppearance() {
		return m_sgAppearance;
	}

	
	@Override
	protected void realize() {
        createSGGeometryIfNecessary();

        edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry = getSGGeometry();
		if( sgGeometry != null ) {
			putElement( sgGeometry );
			m_sgVisual.geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { sgGeometry } );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: no geometry: ", this );
		}
		super.realize();
	}
	
	private java.util.List< org.alice.apis.moveandturn.event.MouseButtonListener > mouseButtonListeners = new java.util.LinkedList< org.alice.apis.moveandturn.event.MouseButtonListener >();

	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public void addMouseButtonListener( org.alice.apis.moveandturn.event.MouseButtonListener mouseButtonListener ) {
		synchronized( this.mouseButtonListeners ) {
			this.mouseButtonListeners.add( mouseButtonListener );
		}
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public void removeMouseButtonListener( org.alice.apis.moveandturn.event.MouseButtonListener mouseButtonListener ) {
		synchronized( this.mouseButtonListeners ) {
			this.mouseButtonListeners.remove( mouseButtonListener );
		}
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public java.util.List< org.alice.apis.moveandturn.event.MouseButtonListener > getMouseButtonListeners() {
		return this.mouseButtonListeners;
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
		return new Scale( getSGVisual().scale.getValue() );
	}
	public void setVisualScale( Scale scale ) {
	    getSGVisual().scale.setValue( scale.getInternal() );
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
		org.alice.virtualmachine.DoTogether.invokeAndWait( runnables );
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
		org.alice.virtualmachine.DoTogether.invokeAndWait( runnables );
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

	//todo: until this descends it does not make sense
	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	public Boolean isShowing() {
		return getSGVisual().isShowing.getValue();
	}
	//todo: howMuch
	public void setShowing( Boolean isShowing ) {
	    getSGVisual().isShowing.setValue( isShowing );
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
		edu.cmu.cs.dennisc.math.Matrix3x3 scale = getSGVisual().scale.getValue();
		edu.cmu.cs.dennisc.math.ScaleUtilities.applyScale( scale, axis );
		getSGVisual().scale.setValue( scale );
	}

	@Override
	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans, boolean isOriginIncluded ) {
		super.updateCumulativeBound( rv, trans, isOriginIncluded );
		if( getSGVisual().isShowing.getValue() ) {
			rv.add( getSGVisual(), trans );
		}
		return rv;
	}	
}
