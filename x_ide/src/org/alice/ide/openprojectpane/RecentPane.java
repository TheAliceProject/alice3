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

package org.alice.ide.openprojectpane;

class RecentPane extends ListPane {
	@Override
	public String getTabTitleText() {
		return "Recent";
	}
	@Override
	protected String getTextForZeroProjects() {
		return "there are no recent projects";
	}
	@Override
	protected java.net.URI[] getURIs() {
		java.util.List< String > paths = org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths.getValue();
		java.net.URI[] rv;
		if( paths != null ) {
			final int N = paths.size();
			rv = new java.net.URI[ N ];
			for( int i=0; i<N; i++ ) {
				rv[ i ] = new java.io.File( paths.get( i ) ).toURI();
			}
		} else {
			rv = new java.net.URI[ 0 ];
		}
		return rv;
	}
}

