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
public class Scene extends Composite {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Color > AMBIENT_LIGHT_BRIGHTNESS_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Color >( Scene.class, "AmbientLightBrightness" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Color > AMBIENT_LIGHT_COLOR_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Color >( Scene.class, "AmbientLightColor" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Color > ATMOSPHERE_COLOR_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Color >( Scene.class, "AtmosphereColor" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > GLOBAL_BRIGHTNESS_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( Scene.class, "GlobalBrightness" );

	private edu.cmu.cs.dennisc.scenegraph.Scene m_sgScene = new edu.cmu.cs.dennisc.scenegraph.Scene();
	private edu.cmu.cs.dennisc.scenegraph.Background m_sgBackground = new edu.cmu.cs.dennisc.scenegraph.Background();
	private edu.cmu.cs.dennisc.scenegraph.AmbientLight m_sgAmbientLight = new edu.cmu.cs.dennisc.scenegraph.AmbientLight();
	public Scene() {
		putElement( m_sgScene );
		
		m_sgBackground.color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 0.5f, 0.5f, 1, 1 ) );
		m_sgScene.background.setValue( m_sgBackground );

	    m_sgAmbientLight.setParent( m_sgScene );
	    //m_sgAmbientLight.setColor( new edu.cmu.cs.dennisc.color.Color4f( 0.2f, 0.2f, 0.2f, 1 ) );
	    m_sgAmbientLight.color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 1.0f, 1.0f, 1.0f, 1.0f ) );
	    m_sgAmbientLight.brightness.setValue( 0.3f );

        //getSGReferenceFrame().setParent( m_scene );
        
//		Axes axes = new Axes( 10 );
//		axes.setParent( m_sgScene );
	}

	@Override
	public void setName( String name ) {
		super.setName( name );
		m_sgScene.setName( name + ".m_sgScene" );
		m_sgBackground.setName( name + ".m_sgBackground" );
		m_sgAmbientLight.setName( name + ".m_sgAmbientLight" );
	}

	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( ReferenceFrame other ) {
		return other.getSGReferenceFrame().getInverseAbsoluteTransformation();
	}
	
	//todo?

	//todo: reduce visibility to protected?
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	@Override
	public edu.cmu.cs.dennisc.scenegraph.Composite getSGComposite() {
		return m_sgScene;
	}
		
	@Override
	public Scene getScene() {
		return this;
	}

	private SceneOwner m_owner;
	@Override
	public SceneOwner getOwner() {
		return m_owner;
	}
	public void setOwner( SceneOwner owner ) {
		if( m_owner != owner ) {
			//handleOwnerChange( null );
			m_owner = owner;
			handleOwnerChange( m_owner );
		}
	}
	
	
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Color getAmbientLightColor() {
		return new Color( m_sgAmbientLight.color.getValue() );
	}
	public void setAmbientLightColor( Color color, Number duration, Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgAmbientLight.color.setValue( color.getInternal() );
		} else {
			perform( new edu.cmu.cs.dennisc.color.animation.Color4fAnimation( duration, style, getAmbientLightColor().getInternal(), color.getInternal() ) {
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.color.Color4f c ) {
					m_sgAmbientLight.color.setValue( c );
				}
			} );
		}
	}
	public void setAmbientLightColor( Color color, Number duration ) {
		setAmbientLightColor( color, duration, DEFAULT_STYLE );
	}
	public void setAmbientLightColor( Color color ) {
		setAmbientLightColor( color, DEFAULT_DURATION );
	}
	
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Double getAmbientLightBrightness() {
		return (double)m_sgAmbientLight.brightness.getValue();
	}
	public void setAmbientLightBrightness( Number brightness, Number duration, Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgAmbientLight.brightness.setValue( brightness.floatValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( duration, style, getAmbientLightBrightness(), brightness.doubleValue() ) {
				@Override
				protected void updateValue( Double d ) {
					m_sgAmbientLight.brightness.setValue( d.floatValue() );
				}
			} );
		}
	}
	public void setAmbientLightBrightness( Number brightness, Number duration ) {
		setAmbientLightBrightness( brightness, duration, DEFAULT_STYLE );
	}
	public void setAmbientLightBrightness( Number brightness ) {
		setAmbientLightBrightness( brightness, DEFAULT_DURATION );
	}

	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Color getAtmosphereColor() {
		return new Color( m_sgBackground.color.getValue() );
	}
	public void setAtmosphereColor( Color color, Number duration, Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgBackground.color.setValue( color.getInternal() );
		} else {
			perform( new edu.cmu.cs.dennisc.color.animation.Color4fAnimation( duration, style, getAtmosphereColor().getInternal(), color.getInternal() ) {
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.color.Color4f c ) {
					m_sgBackground.color.setValue( c );
				}
			} );
		}
	}
	public void setAtmosphereColor( Color color, Number duration ) {
		setAtmosphereColor( color, duration, DEFAULT_STYLE );
	}
	public void setAtmosphereColor( Color color ) {
		setAtmosphereColor( color, DEFAULT_DURATION );
	}


	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Double getGlobalBrightness() {
		return (double)m_sgScene.globalBrightness.getValue();
	}
	public void setGlobalBrightness( Number brightness, Number duration, Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgScene.globalBrightness.setValue( brightness.floatValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( duration, style, getGlobalBrightness(), brightness.doubleValue() ) {
				@Override
				protected void updateValue( Double d ) {
					m_sgScene.globalBrightness.setValue( d.floatValue() );
				}
			} );
		}
	}
	public void setGlobalBrightness( Number brightness, Number duration ) {
		setGlobalBrightness( brightness, duration, DEFAULT_STYLE );
	}
	public void setGlobalBrightness( Number brightness ) {
		setGlobalBrightness( brightness, DEFAULT_DURATION );
	}
	private static boolean isGround( org.alice.apis.moveandturn.Model model ) {
		String simpleName = model.getClass().getSimpleName();
		return simpleName.endsWith( "Ground" ) || simpleName.equals( "MoonSurface" ) || simpleName.equals( "SeaSurface" );
	}
	private void putBonusDataFor( org.alice.apis.moveandturn.Transformable transformable ) {
		edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = transformable.getSGTransformable();
		if( transformable instanceof org.alice.apis.moveandturn.Model ) {
			org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)transformable;
			if( Scene.isGround( model ) ) {
				sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.GROUND );
			} else {
				sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS );
				sgTransformable.putBonusDataFor( org.alice.interact.GlobalDragAdapter.BOUNDING_BOX_KEY, model.getAxisAlignedMinimumBoundingBox() );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( sgTransformable.getBonusDataFor( GlobalDragAdapter.BOUNDING_BOX_KEY ) );
			}
		} else if( transformable instanceof org.alice.apis.moveandturn.Light ) {
			//org.alice.apis.moveandturn.Light light = (org.alice.apis.moveandturn.Light)transformable;
			sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.LIGHT );
		} else if( transformable instanceof org.alice.apis.moveandturn.AbstractCamera ) {
			//org.alice.apis.moveandturn.AbstractCamera camera = (org.alice.apis.moveandturn.AbstractCamera)transformable;
			sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.CAMERA );
		}
	}

	private org.alice.interact.GlobalDragAdapter globalDragAdapter;
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void addDefaultModelManipulation() {
		if( this.globalDragAdapter != null ) {
			//pass
		} else {
			this.globalDragAdapter = new org.alice.interact.GlobalDragAdapter();
			this.globalDragAdapter.setOnscreenLookingGlass( this.getOwner().getOnscreenLookingGlass() );
			//this.globalDragAdapter.setAnimator( ((Program)this.getOwner()).getAnimator() );
			for( Transformable transformable : this.getComponents() ) {
				this.putBonusDataFor( transformable );
			}
		}
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void removeDefaultModelManipulation() {
		if( this.globalDragAdapter != null ) {
			this.globalDragAdapter.setOnscreenLookingGlass( null );
			this.globalDragAdapter = null;
		}
	}
}
