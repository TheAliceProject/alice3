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
package org.alice.apis.moveandturn.gallery.fantasy;
	
public class Wheely extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Wheely() {
		super( "Fantasy/Wheely" );
	}
	public enum Part {
		Arm_Elbow_Forearm_Wrist_Hand( "Arm", "Elbow", "Forearm", "Wrist", "Hand" ),
		Arm_Elbow_Forearm_Wrist( "Arm", "Elbow", "Forearm", "Wrist" ),
		Arm_Elbow_Forearm( "Arm", "Elbow", "Forearm" ),
		Arm_Elbow( "Arm", "Elbow" ),
		Arm( "Arm" ),
		WheelyBody( "WheelyBody" ),
		Neck01( "Neck01" ),
		Neck02( "Neck02" ),
		Neck03_Head_EyelidRight( "Neck03", "Head", "EyelidRight" ),
		Neck03_Head_EyelidLeft( "Neck03", "Head", "EyelidLeft" ),
		Neck03_Head_EyeLeft( "Neck03", "Head", "EyeLeft" ),
		Neck03_Head_EyeRight( "Neck03", "Head", "EyeRight" ),
		Neck03_Head( "Neck03", "Head" ),
		Neck03( "Neck03" ),
		Axle_WheelRight( "Axle", "WheelRight" ),
		Axle_WheelLeft( "Axle", "WheelLeft" ),
		Axle( "Axle" );
		private String[] m_path;
		Part( String... path ) {
			m_path = path;
		}
		public String[] getPath() {
			return m_path;
		}
	}
	public org.alice.apis.moveandturn.Model getPart( Part part ) {
		return getDescendant( org.alice.apis.moveandturn.Model.class, part.getPath() );
	}

}
