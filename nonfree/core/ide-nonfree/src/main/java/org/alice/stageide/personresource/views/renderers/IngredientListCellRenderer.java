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
package org.alice.stageide.personresource.views.renderers;

/**
 * @author Dennis Cosgrove
 */
public abstract class IngredientListCellRenderer<E> extends edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer<E> {

	private static final java.io.File GALLERY_ROOT = org.lgna.story.resourceutilities.StorytellingResources.getGalleryRootDirectory();
	private static final java.io.File IMAGE_ROOT = new java.io.File( GALLERY_ROOT, "ide/person" );
	private static final String DEFAULT_SKIN_TONE_NAME = "GRAY";

	private static final java.util.Map<java.net.URL, javax.swing.Icon> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static final java.util.Map<String, java.net.URL> pathMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static final java.util.Map<String, Boolean> skinToneIconMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static boolean hasIconForPath( String path ) {
		java.net.URL urlForIcon = getURLForPath( path );
		return urlForIcon != null;
	}

	public static java.net.URL getURLForPath( String path ) {
		java.net.URL urlForIcon = null;
		if( pathMap.containsKey( path ) ) {
			urlForIcon = pathMap.get( path );
		}
		if( urlForIcon == null ) {
			java.io.File file = new java.io.File( IMAGE_ROOT, path );
			if( file.exists() ) {
				try {
					urlForIcon = file.toURL();
				} catch( java.net.MalformedURLException murle ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( murle, file );
					urlForIcon = null;
				}
			} else {
				//edu.cmu.cs.dennisc.java.util.logging.Logger.errln( file );
				urlForIcon = null;
			}
			//			if( urlForIcon != null ) {
			//				//pass
			//			} else {
			//				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( path );
			//			}
			pathMap.put( path, urlForIcon );
		}
		return urlForIcon;
	}

	public static javax.swing.Icon getIconForPath( int width, int height, String path ) {
		java.net.URL urlForIcon = getURLForPath( path );
		javax.swing.Icon rv = map.get( urlForIcon );
		if( rv != null ) {
			//pass
		} else {
			rv = new edu.cmu.cs.dennisc.javax.swing.UrlAsynchronousIcon( width, height, urlForIcon );
			map.put( urlForIcon, rv );
		}
		return rv;
		//return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( urlForIcon );
	}

	private javax.swing.border.Border border = javax.swing.BorderFactory.createEmptyBorder( 2, 2, 2, 2 );

	public IngredientListCellRenderer( int width, int height ) {
		this.width = width;
		this.height = height;
	}

	public boolean ACCEPTABLE_HACK_AT_THIS_TIME_FOR_LIST_DATA_hasValidImageFor( E value, org.lgna.story.resources.sims2.SkinTone baseSkinTone ) {
		Object v = getValue( value );
		String clsName = v.getClass().getSimpleName();
		clsName = this.modifyClsNameIfNecessary( clsName, org.alice.stageide.personresource.PersonResourceComposite.getInstance().getIngredientsComposite().getLifeStageState().getValue(), org.alice.stageide.personresource.PersonResourceComposite.getInstance().getIngredientsComposite().getGenderState().getValue() );
		String enumConstantName = v.toString();
		String path = this.getValidIconPath( baseSkinTone, clsName, enumConstantName );

		return path != null;
	}

	protected abstract String getSubPath();

	private org.lgna.story.resources.sims2.SkinTone getSkinTone() {
		return org.alice.stageide.personresource.PersonResourceComposite.getInstance().getIngredientsComposite().getClosestBaseSkinTone();
	}

	private String getIngredientResourceName( String skinToneString, String clsName, String enumConstantName ) {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getSubPath() );
		sb.append( "/" );
		sb.append( skinToneString );
		sb.append( "/" );
		sb.append( clsName );
		sb.append( "." );
		sb.append( enumConstantName );
		sb.append( ".png" );
		return sb.toString();
	}

	private String getIngredientResourceName( org.lgna.story.resources.sims2.SkinTone skinTone, String clsName, String enumConstantName ) {
		return getIngredientResourceName( skinTone.toString(), clsName, enumConstantName );
	}

	protected String modifyClsNameIfNecessary( String clsName, org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender ) {
		return clsName;
	}

	protected Object getValue( E value ) {
		return value;
	}

	protected String getValidIconPath( org.lgna.story.resources.sims2.SkinTone baseSkinTone, String clsName, String enumConstantName ) {
		String path = null;

		boolean lookForSkinToneIcon = true;
		if( skinToneIconMap.containsKey( clsName ) ) {
			lookForSkinToneIcon = skinToneIconMap.get( clsName );
		}
		if( ( baseSkinTone != null ) && lookForSkinToneIcon ) {
			path = this.getIngredientResourceName( baseSkinTone, clsName, enumConstantName );
			//If the clsName isn't in the map, then check to see if there's an icon for this path and remember the results by recording it in the map
			if( !skinToneIconMap.containsKey( clsName ) ) {
				if( !hasIconForPath( path ) ) {
					skinToneIconMap.put( clsName, false );
					path = this.getIngredientResourceName( DEFAULT_SKIN_TONE_NAME, clsName, enumConstantName );
				}
				else {
					skinToneIconMap.put( clsName, true );
				}
			}
		} else {
			path = this.getIngredientResourceName( DEFAULT_SKIN_TONE_NAME, clsName, enumConstantName );
		}
		if( hasIconForPath( path ) ) {
			return path;
		}
		return null;
	}

	@Override
	protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, E val, int index, boolean isSelected, boolean cellHasFocus ) {
		assert rv != null;
		Object v = getValue( val );
		if( v != null ) {
			String clsName = v.getClass().getSimpleName();
			clsName = this.modifyClsNameIfNecessary( clsName, org.alice.stageide.personresource.PersonResourceComposite.getInstance().getIngredientsComposite().getLifeStageState().getValue(), org.alice.stageide.personresource.PersonResourceComposite.getInstance().getIngredientsComposite().getGenderState().getValue() );
			String enumConstantName = v.toString();

			rv.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER );
			rv.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM );

			rv.setOpaque( isSelected );
			org.lgna.story.resources.sims2.SkinTone baseSkinTone = this.getSkinTone();
			javax.swing.Icon icon;
			String path = getValidIconPath( baseSkinTone, clsName, enumConstantName );
			icon = getIconForPath( this.width, this.height, path );
			if( icon != null ) {
				rv.setIcon( icon );
				rv.setText( "" );
				if( isSelected ) {
					rv.setBackground( org.alice.stageide.personresource.views.IngredientsView.SELECTED_COLOR );
				}
			} else {
				rv.setText( "image not found" );
			}
			rv.setBorder( this.border );
		} else {
			rv.setText( "null" );
		}
		return rv;
	}

	private final int width;
	private final int height;
}
