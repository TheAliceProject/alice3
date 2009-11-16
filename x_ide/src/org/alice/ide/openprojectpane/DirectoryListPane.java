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

/**
 * @author Dennis Cosgrove
 */
abstract class DirectoryListPane extends ListPane {
	@Override
	protected String getTextForZeroProjects() {
		String path = edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( this.getDirectory() );
		return "there are no projects in " + path;
	}
	protected abstract java.io.File getDirectory();
	@Override
	protected java.io.File[] getFiles() {
		java.io.File directory = this.getDirectory();
		java.io.File[] rv;
		if( directory != null ) {
			rv = edu.cmu.cs.dennisc.alice.io.FileUtilities.listProjectFiles( this.getDirectory() );
		} else {
			rv = new java.io.File[ 0 ];
		}
		return rv;
	}
}
