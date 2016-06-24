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
package org.alice.stageide.modelresource;

import java.awt.image.BufferedImage;
import java.util.Locale;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;

/**
 * @author Dennis Cosgrove
 */
public class IdeAliceResourceUtilities extends org.lgna.story.implementation.alice.AliceResourceUtilties {
	public static java.net.URL getThumbnailURL( ResourceKey key )
	{
		return getThumbnailURLInternal( getClassFromKey( key ), getEnumNameFromKey( key ) );
	}

	public static Class<?> getClassFromKey( ResourceKey key ) {
		if( key instanceof ClassResourceKey ) {
			ClassResourceKey clsKey = (ClassResourceKey)key;
			return clsKey.getModelResourceCls();
		}
		else if( key instanceof EnumConstantResourceKey ) {
			EnumConstantResourceKey enumKey = (EnumConstantResourceKey)key;
			return enumKey.getEnumConstant().getDeclaringClass();
		}
		return null;
	}

	public static String getEnumNameFromKey( ResourceKey key ) {
		if( key instanceof ClassResourceKey ) {
			return null;
		}
		else if( key instanceof EnumConstantResourceKey ) {
			EnumConstantResourceKey enumKey = (EnumConstantResourceKey)key;
			return enumKey.getEnumConstant().name();
		}
		return null;
	}

	public static BufferedImage getThumbnail( ResourceKey key )
	{
		return getThumbnailInternal( getClassFromKey( key ), getEnumNameFromKey( key ) );
	}

	private static String getKey( ResourceKey key ) {
		return getKey( getClassFromKey( key ), getEnumNameFromKey( key ) );
	}

	public static org.lgna.story.resourceutilities.ModelResourceInfo getModelResourceInfo( ResourceKey key ) {
		if( key == null ) {
			return null;
		}
		return getModelResourceInfo( getClassFromKey( key ), getEnumNameFromKey( key ) );
	}

	public static AxisAlignedBox getBoundingBox( ResourceKey key ) {
		return getBoundingBox( getClassFromKey( key ), getEnumNameFromKey( key ) );
	}

	public static boolean getPlaceOnGround( ResourceKey key ) {
		return getPlaceOnGround( getClassFromKey( key ), getEnumNameFromKey( key ) );
	}

	public static String getModelName( ResourceKey key ) {
		return getModelName( getClassFromKey( key ), getEnumNameFromKey( key ), null );
	}

	public static String getModelName( ResourceKey key, Locale locale ) {
		return getModelName( getClassFromKey( key ), getEnumNameFromKey( key ), locale );
	}

	public static String getModelClassName( ResourceKey key ) {
		return getModelClassName( getClassFromKey( key ), getEnumNameFromKey( key ), null );
	}

	public static String getModelClassName( ResourceKey key, Locale locale ) {
		return getModelClassName( getClassFromKey( key ), getEnumNameFromKey( key ), locale );
	}

	public static String getCreator( ResourceKey key ) {
		return getCreator( getClassFromKey( key ), getEnumNameFromKey( key ) );
	}

	public static int getCreationYear( ResourceKey key )
	{
		return getCreationYear( getClassFromKey( key ), getEnumNameFromKey( key ) );
	}

	public static String[] getTags( ResourceKey key )
	{
		return getTags( getClassFromKey( key ), getEnumNameFromKey( key ), null );
	}

	public static String[] getTags( ResourceKey key, Locale locale )
	{
		return getTags( getClassFromKey( key ), getEnumNameFromKey( key ), locale );
	}

	public static String[] getGroupTags( ResourceKey key )
	{
		return getGroupTags( getClassFromKey( key ), getEnumNameFromKey( key ), null );
	}

	public static String[] getGroupTags( ResourceKey key, Locale locale )
	{
		return getGroupTags( getClassFromKey( key ), getEnumNameFromKey( key ), locale );
	}

	public static String[] getThemeTags( ResourceKey key )
	{
		return getThemeTags( getClassFromKey( key ), getEnumNameFromKey( key ), null );
	}

	public static String[] getThemeTags( ResourceKey key, Locale locale )
	{
		return getThemeTags( getClassFromKey( key ), getEnumNameFromKey( key ), locale );
	}

}
