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
package edu.cmu.cs.dennisc.image;

/**
 * @author Dennis Cosgrove
 */
public class ImageCache {
	private static java.util.Map<String, java.awt.image.BufferedImage> s_pathToImageMap = new java.util.HashMap<String, java.awt.image.BufferedImage>();
	private static java.util.Set<String> s_pathNullSet = new java.util.HashSet<String>();

	public static java.awt.image.BufferedImage getBufferedImage( String path ) {
		java.awt.image.BufferedImage image;
		if( path != null ) {
			image = s_pathToImageMap.get( path );
			if( image != null ) {
				//pass
			} else {
				if( s_pathNullSet.contains( path ) ) {
					//pass
				} else {
					image = edu.cmu.cs.dennisc.image.ImageUtilities.read( path );
					if( image != null ) {
						s_pathToImageMap.put( path, image );
					} else {
						s_pathNullSet.add( path );
					}
				}
			}
		} else {
			image = null;
		}
		return image;
	}
	
	//todo:
	//public static void releaseMap
	//public static void forgetSet
}
