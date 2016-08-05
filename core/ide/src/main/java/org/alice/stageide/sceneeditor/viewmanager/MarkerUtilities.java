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
package org.alice.stageide.sceneeditor.viewmanager;

import java.util.Arrays;
import java.util.HashMap;

import org.alice.ide.name.validators.MarkerColorValidator;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.alice.stageide.sceneeditor.View;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.story.CameraMarker;
import org.lgna.story.implementation.CameraMarkerImp;

import edu.cmu.cs.dennisc.pattern.Tuple2;

/**
 * @author dculyba
 * 
 */
public class MarkerUtilities {
	private static final String[] COLOR_NAME_KEYS;
	private static final org.lgna.story.Color[] COLORS;

	private static final HashMap<CameraMarker, Tuple2<org.lgna.croquet.icon.ImageIconFactory, org.lgna.croquet.icon.ImageIconFactory>> cameraToIconMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static final HashMap<CameraMarker, View> cameraToViewMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static final HashMap<org.lgna.story.Color, javax.swing.ImageIcon> colorToObjectIconMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static final HashMap<org.lgna.story.Color, javax.swing.ImageIcon> colorToCameraIconMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static java.util.Map<CameraMarker, org.lgna.croquet.icon.IconFactory> mapCameraIconFactory = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	static
	{
		String[] colorNameKeys = {
				"red",
				"green",
				//				"blue",
				"magenta",
				"yellow",
				//				"cyan",
				"orange",
				"pink",
				"purple",
		};
		COLOR_NAME_KEYS = colorNameKeys;

		org.lgna.story.Color[] colors = {
				org.lgna.story.Color.RED,
				org.lgna.story.Color.GREEN,
				//				org.lgna.story.Color.BLUE,
				org.lgna.story.Color.MAGENTA,
				org.lgna.story.Color.YELLOW,
				//				org.lgna.story.Color.CYAN,
				org.lgna.story.Color.ORANGE,
				org.lgna.story.Color.PINK,
				org.lgna.story.Color.PURPLE
		};
		COLORS = colors;
	}

	private static String findLocalizedText( String subKey ) {
		String bundleName = MarkerUtilities.class.getPackage().getName() + ".croquet";
		try {
			java.util.ResourceBundle resourceBundle = edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getUtf8Bundle( bundleName, javax.swing.JComponent.getDefaultLocale() );
			String key = MarkerUtilities.class.getSimpleName();

			if( subKey != null ) {
				StringBuilder sb = new StringBuilder();
				sb.append( key );
				sb.append( "." );
				sb.append( subKey );
				key = sb.toString();
			}
			String rv = resourceBundle.getString( key );
			return rv;
		} catch( java.util.MissingResourceException mre ) {
			return null;
		}
	}

	private static int getColorIndexForName( String name )
	{
		String lowerName = name.toLowerCase( java.util.Locale.ENGLISH );
		for( int i = 0; i < getColorCount(); i++ )
		{
			String currentColor = getColorNameForIndex( i ).toLowerCase( java.util.Locale.ENGLISH );
			if( lowerName.endsWith( currentColor ) )
			{
				return i;
			}
		}
		return -1;
	}

	private static int getColorIndexForColor( org.lgna.story.Color color )
	{
		for( int i = 0; i < getColorCount(); i++ )
		{
			if( getColorForIndex( i ).equals( color ) )
			{
				return i;
			}
		}
		return -1;
	}

	private static int getColorCount() {
		return COLORS.length;
	}

	private static org.lgna.story.Color getColorForIndex( int i ) {
		return COLORS[ i ];
	}

	private static String getColorNameForIndex( int i ) {
		return findLocalizedText( COLOR_NAME_KEYS[ i ] );
	}

	private static String getColorFileName( org.lgna.story.Color color )
	{
		int index = getColorIndexForColor( color );
		if( index != -1 ) {
			String colorName = COLOR_NAME_KEYS[ index ];
			String properName = colorName.substring( 0, 1 ).toUpperCase() + colorName.substring( 1 );
			return properName;
		}
		return "White";
	}

	private static String getIconSuffixForMarkerColor( org.lgna.story.Color color )
	{
		String colorName = getColorFileName( color );
		return "_" + colorName + ".png";
	}

	public static String getNameForView( View view ) {
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( StorytellingSceneEditor.class.getPackage().getName() + ".cameraViews" );
		if( view != null ) {
			switch( view ) {
			case STARTING_CAMERA_VIEW:
				return resourceBundle.getString( "sceneCameraView" );
			case LAYOUT_SCENE_VIEW:
				return resourceBundle.getString( "layoutPerspectiveView" );
			case TOP:
				return resourceBundle.getString( "topOrthographicView" );
			case SIDE:
				return resourceBundle.getString( "sideOrthographicView" );
			case FRONT:
				return resourceBundle.getString( "frontOrthographicView" );
			}
		}
		return "";
	}

	public static String getNameForCameraImp( CameraMarkerImp cameraImp ) {
		return getNameForView( getViewForCameraImp( cameraImp ) );
	}

	public static String getNameForCamera( CameraMarker camera ) {
		return getNameForView( getViewForCamera( camera ) );
	}

	public static void addIconForCameraImp( CameraMarkerImp cameraImp, String iconName ) {
		assert cameraImp != null;
		addIconForCamera( cameraImp.getAbstraction(), iconName );
	}

	public static void setViewForCameraImp( CameraMarkerImp cameraImp, View view ) {
		assert cameraImp != null;
		setViewForCamera( cameraImp.getAbstraction(), view );
	}

	public static void setViewForCamera( CameraMarker camera, View view ) {
		cameraToViewMap.put( camera, view );
	}

	public static View getViewForCameraImp( CameraMarkerImp cameraImp ) {
		if( cameraImp != null ) {
			return cameraToViewMap.get( cameraImp.getAbstraction() );
		}
		return null;
	}

	public static View getViewForCamera( CameraMarker camera ) {
		return cameraToViewMap.get( camera );
	}

	public static void addIconForCamera( CameraMarker camera, String iconName ) {
		java.net.URL normalIconURL = StorytellingSceneEditor.class.getResource( "images/" + iconName + "Icon.png" );
		assert normalIconURL != null;
		javax.swing.ImageIcon normalIcon = new javax.swing.ImageIcon( normalIconURL );
		java.net.URL highlightedIconURL = StorytellingSceneEditor.class.getResource( "images/" + iconName + "Icon_highlighted.png" );
		assert highlightedIconURL != null;
		javax.swing.ImageIcon highlightedIcon = new javax.swing.ImageIcon( highlightedIconURL );

		cameraToIconMap.put( camera, Tuple2.createInstance( new org.lgna.croquet.icon.ImageIconFactory( normalIcon ), new org.lgna.croquet.icon.ImageIconFactory( highlightedIcon ) ) );
	}

	private static javax.swing.ImageIcon loadIconForObjectMarker( org.lgna.story.Color color ) {
		java.net.URL markerIconURL = StorytellingSceneEditor.class.getResource( "images/axis" + getIconSuffixForMarkerColor( color ) );
		assert markerIconURL != null : color;
		javax.swing.ImageIcon markerIcon = new javax.swing.ImageIcon( markerIconURL );
		return markerIcon;
	}

	private static javax.swing.ImageIcon loadIconForCameraMarker( org.lgna.story.Color color ) {
		java.net.URL markerIconURL = StorytellingSceneEditor.class.getResource( "images/markerIcon" + getIconSuffixForMarkerColor( color ) );
		assert markerIconURL != null;
		javax.swing.ImageIcon markerIcon = new javax.swing.ImageIcon( markerIconURL );
		return markerIcon;
	}

	public static org.lgna.croquet.icon.IconFactory getIconFactoryForObjectMarker( UserField marker )
	{
		if( marker != null ) {
			org.lgna.story.Color markerColor = getColorForMarkerField( marker );
			return org.alice.stageide.icons.IconFactoryManager.getIconFactoryForObjectMarker( markerColor );
		}
		return null;
	}

	public static org.lgna.croquet.icon.IconFactory getIconFactoryForCameraMarker( UserField marker )
	{
		if( marker != null ) {
			org.lgna.story.Color markerColor = getColorForMarkerField( marker );
			return org.alice.stageide.icons.IconFactoryManager.getIconFactoryForCameraMarker( markerColor );
		}
		return null;
	}

	public static javax.swing.ImageIcon getIconForObjectMarker( UserField marker )
	{
		if( marker != null ) {
			org.lgna.story.Color markerColor = getColorForMarkerField( marker );
			return getObjectMarkIconForColor( markerColor );
		}
		return null;
	}

	public static javax.swing.ImageIcon getIconForCameraMarker( UserField marker )
	{
		if( marker != null ) {
			org.lgna.story.Color markerColor = getColorForMarkerField( marker );
			return getCameraMarkIconForColor( markerColor );
		}
		return null;
	}

	public static javax.swing.ImageIcon getCameraMarkIconForColor( org.lgna.story.Color markerColor ) {
		if( colorToCameraIconMap.containsKey( markerColor ) ) {
			return colorToCameraIconMap.get( markerColor );
		}
		else {
			javax.swing.ImageIcon icon = loadIconForCameraMarker( markerColor );
			colorToCameraIconMap.put( markerColor, icon );
			return icon;
		}
	}

	public static javax.swing.ImageIcon getObjectMarkIconForColor( org.lgna.story.Color markerColor ) {
		if( colorToObjectIconMap.containsKey( markerColor ) ) {
			return colorToObjectIconMap.get( markerColor );
		}
		else {
			javax.swing.ImageIcon icon = loadIconForObjectMarker( markerColor );
			colorToObjectIconMap.put( markerColor, icon );
			return icon;
		}
	}

	public static javax.swing.ImageIcon getIconForMarkerField( UserField markerField ) {
		if( markerField != null ) {
			if( markerField.getValueType().isAssignableTo( org.lgna.story.CameraMarker.class ) ) {
				return getIconForCameraMarker( markerField );
			}
			else if( markerField.getValueType().isAssignableFrom( org.lgna.story.SThingMarker.class ) ) {
				return getIconForObjectMarker( markerField );
			}
		}
		return null;
	}

	private static org.lgna.croquet.icon.IconFactory getIconFactoryForCameraMarker( CameraMarker camera ) {
		assert cameraToIconMap.containsKey( camera );
		return cameraToIconMap.get( camera ).getA();
	}

	private static org.lgna.croquet.icon.IconFactory getHighlightedIconFactoryForCameraMarker( CameraMarker camera ) {
		assert cameraToIconMap.containsKey( camera );
		return cameraToIconMap.get( camera ).getB();
	}

	private static final java.awt.Dimension DEFAULT_SIZE_IF_NULL = org.lgna.croquet.icon.IconSize.MEDIUM.getSize();

	public static javax.swing.Icon getIconForCameraMarkerImp( CameraMarkerImp camera ) {
		if( camera != null ) {
			org.lgna.croquet.icon.IconFactory factory = getIconFactoryForCameraMarker( camera.getAbstraction() );
			return factory.getIcon( factory.getDefaultSize( DEFAULT_SIZE_IF_NULL ) );
		}
		return null;
	}

	public static javax.swing.Icon getHighlightedIconForCameraMarkerImp( CameraMarkerImp camera ) {
		if( camera != null ) {
			org.lgna.croquet.icon.IconFactory factory = getHighlightedIconFactoryForCameraMarker( camera.getAbstraction() );
			return factory.getIcon( factory.getDefaultSize( DEFAULT_SIZE_IF_NULL ) );
		}
		return null;
	}

	public static org.lgna.croquet.icon.IconFactory getIconFactoryForCameraMarkerImp( CameraMarkerImp camera ) {
		if( camera != null ) {
			org.lgna.croquet.icon.IconFactory factory = getIconFactoryForCameraMarker( camera.getAbstraction() );
			return factory;
		}
		return null;
	}

	public static org.lgna.croquet.icon.IconFactory getHighlightedIconFactoryForCameraMarkerImp( CameraMarkerImp camera ) {
		if( camera != null ) {
			org.lgna.croquet.icon.IconFactory factory = getHighlightedIconFactoryForCameraMarker( camera.getAbstraction() );
			return factory;
		}
		return null;
	}

	private static org.lgna.story.Color getNewMarkerColor( Class<? extends org.lgna.story.SMarker> markerCls ) {
		AbstractType<?, ?, ?> sceneType = org.alice.stageide.StageIDE.getActiveInstance().getSceneEditor().getActiveSceneField().getValueType();
		int[] colorCounts = new int[ getColorCount() ];
		Arrays.fill( colorCounts, 0 );
		java.util.List<? extends AbstractField> fields = sceneType.getDeclaredFields();
		for( AbstractField f : fields ) {
			if( f.getValueType().isAssignableTo( markerCls ) ) {
				org.lgna.story.SMarker marker = org.alice.stageide.StageIDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForField( f, markerCls );
				if( marker != null ) {
					int colorIndex = getColorIndexForColor( marker.getColorId() );
					if( colorIndex != -1 ) {
						colorCounts[ colorIndex ]++;
					}
				}
			}
		}
		int minIndex = 0;
		int minCount = Integer.MAX_VALUE;
		for( int i = 0; i < colorCounts.length; i++ ) {
			if( colorCounts[ i ] < minCount ) {
				minIndex = i;
				minCount = colorCounts[ i ];
			}
		}
		return getColorForIndex( minIndex );
	}

	public static org.lgna.story.Color getNewObjectMarkerColor() {
		return getNewMarkerColor( org.lgna.story.SThingMarker.class );
	}

	public static org.lgna.story.Color getNewCameraMarkerColor() {
		return getNewMarkerColor( org.lgna.story.CameraMarker.class );
	}

	public static org.lgna.story.Color getColorForMarkerField( UserField markerField )
	{
		org.lgna.story.SMarker marker = org.alice.stageide.StageIDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForField( markerField, org.lgna.story.SMarker.class );
		if( marker != null )
		{
			return marker.getColorId();
		}
		else {
			return org.lgna.story.Color.WHITE;
		}
	}

	public static org.lgna.story.Color getColorForMarkerName( String markerName )
	{
		int colorIndex = getColorIndexForName( markerName );
		if( colorIndex != -1 )
		{
			return getColorForIndex( colorIndex );
		}
		else
		{
			return null;
		}
	}

	private static String makeMarkerName( String baseName, org.lgna.story.Color color, int addOnNumber )
	{
		String colorName = getNameForColor( color );
		String markerName = baseName + "_" + colorName;
		if( addOnNumber > 0 )
		{
			markerName += "_" + Integer.toString( addOnNumber );
		}
		return markerName;
	}

	private static String getNameForColor( org.lgna.story.Color color ) {
		int colorIndex = getColorIndexForColor( color );
		if( colorIndex != -1 ) {
			return getColorNameForIndex( colorIndex );
		}
		else {
			return "";
		}
	}

	private static String getNameForMarker( NamedUserType ownerType, String baseMarkerName, org.lgna.story.Color color ) {
		MarkerColorValidator nameValidator = new MarkerColorValidator( ownerType );
		int addOnNumber = 0;
		String markerName = makeMarkerName( baseMarkerName, color, addOnNumber );
		while( nameValidator.getExplanationIfOkButtonShouldBeDisabled( markerName ) != null ) {
			addOnNumber++;
			markerName = makeMarkerName( baseMarkerName, color, addOnNumber );
		}
		return markerName;
	}

	public static String getNameForCameraMarker( NamedUserType ownerType, org.lgna.story.Color color ) {
		return getNameForMarker( ownerType, findLocalizedText( "defaultCameraMarkerName" ), color );
	}

	public static String getNameForObjectMarker( NamedUserType ownerType, UserField selectedField, org.lgna.story.Color color ) {
		return getNameForMarker( ownerType, selectedField.getName(), color );
	}
}
