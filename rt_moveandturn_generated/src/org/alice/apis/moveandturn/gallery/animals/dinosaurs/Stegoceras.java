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
package org.alice.apis.moveandturn.gallery.animals.dinosaurs;
	
public class Stegoceras extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Stegoceras() {
		super( "Animals/Dinosaurs/stegoceras" );
	}
	public enum Part {
		Rleg_Rshin_Rf_Rsf_Rc01_Rc02_Rc03( "rleg", "rshin", "rf", "rsf", "rc01", "rc02", "rc03" ),
		Rleg_Rshin_Rf_Rsf_Rc01_Rc02( "rleg", "rshin", "rf", "rsf", "rc01", "rc02" ),
		Rleg_Rshin_Rf_Rsf_Rc01( "rleg", "rshin", "rf", "rsf", "rc01" ),
		Rleg_Rshin_Rf_Rsf( "rleg", "rshin", "rf", "rsf" ),
		Rleg_Rshin_Rf( "rleg", "rshin", "rf" ),
		Rleg_Rshin( "rleg", "rshin" ),
		Rleg( "rleg" ),
		A_B_C( "a", "b", "c" ),
		A_B( "a", "b" ),
		A( "a" ),
		Lleg_Lshin_Lf_Lsf_Lc01_Lc02_Lc03( "lleg", "lshin", "lf", "lsf", "lc01", "lc02", "lc03" ),
		Lleg_Lshin_Lf_Lsf_Lc01_Lc02( "lleg", "lshin", "lf", "lsf", "lc01", "lc02" ),
		Lleg_Lshin_Lf_Lsf_Lc01( "lleg", "lshin", "lf", "lsf", "lc01" ),
		Lleg_Lshin_Lf_Lsf( "lleg", "lshin", "lf", "lsf" ),
		Lleg_Lshin_Lf( "lleg", "lshin", "lf" ),
		Lleg_Lshin( "lleg", "lshin" ),
		Lleg( "lleg" ),
		Botneck_Topneck_Head_Reye_Reyelid( "botneck", "topneck", "head", "reye", "reyelid" ),
		Botneck_Topneck_Head_Reye( "botneck", "topneck", "head", "reye" ),
		Botneck_Topneck_Head_Jaw( "botneck", "topneck", "head", "jaw" ),
		Botneck_Topneck_Head_Leye_Leyelid( "botneck", "topneck", "head", "leye", "leyelid" ),
		Botneck_Topneck_Head_Leye( "botneck", "topneck", "head", "leye" ),
		Botneck_Topneck_Head( "botneck", "topneck", "head" ),
		Botneck_Topneck( "botneck", "topneck" ),
		Botneck( "botneck" ),
		Rarm_Rforearm_Rhand( "rarm", "rforearm", "rhand" ),
		Rarm_Rforearm( "rarm", "rforearm" ),
		Rarm( "rarm" ),
		Larm_Lforearm_Lhand( "larm", "lforearm", "lhand" ),
		Larm_Lforearm( "larm", "lforearm" ),
		Larm( "larm" );
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
