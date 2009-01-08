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
package org.alice.apis.moveandturn.gallery.holidays.christmas;
	
public class ChristmasTree extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public ChristmasTree() {
		super( "Holidays/Christmas/ChristmasTree" );
	}
	public enum Part {
		Branch29( "branch29" ),
		Branch55( "branch55" ),
		Branch56( "branch56" ),
		Branch53( "branch53" ),
		Branch52( "branch52" ),
		Branch51( "branch51" ),
		Branch50( "branch50" ),
		Branch49( "branch49" ),
		Branch48( "branch48" ),
		Branch47( "branch47" ),
		Branch46( "branch46" ),
		Branch24( "branch24" ),
		Branch59( "branch59" ),
		Branch58( "branch58" ),
		Branch57( "branch57" ),
		Branch42( "branch42" ),
		Branch54( "branch54" ),
		Branch40( "branch40" ),
		Branch41( "branch41" ),
		Branch39( "branch39" ),
		Branch38( "branch38" ),
		Branch35( "branch35" ),
		Branch34( "branch34" ),
		Branch33( "branch33" ),
		Branch19( "branch19" ),
		Branch37( "branch37" ),
		Branch31( "branch31" ),
		Branch30( "branch30" ),
		Branch28( "branch28" ),
		Branch32( "branch32" ),
		Branch27( "branch27" ),
		Branch26( "branch26" ),
		Branch25( "branch25" ),
		Branch45( "branch45" ),
		Branch44( "branch44" ),
		Branch43( "branch43" ),
		Branch18( "branch18" ),
		Branch16( "branch16" ),
		Branch15( "branch15" ),
		Branch14( "branch14" ),
		Branch13( "branch13" ),
		Branch12( "branch12" ),
		Branch11( "branch11" ),
		Branch10( "branch10" ),
		Branch08( "branch08" ),
		Branch17( "branch17" ),
		Branch09( "branch09" ),
		Branch07( "branch07" ),
		Branch01( "branch01" ),
		Branch05( "branch05" ),
		Branch04( "branch04" ),
		Branch03( "branch03" ),
		Branch02( "branch02" ),
		Branch06( "branch06" ),
		Branch( "branch" ),
		Branch36( "branch36" ),
		Branch23( "branch23" ),
		Branch22( "branch22" ),
		Branch21( "branch21" ),
		Branch20( "branch20" ),
		Star( "star" );
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
