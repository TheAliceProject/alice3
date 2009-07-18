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
package edu.cmu.cs.dennisc.jira;

/**
 * @author Dennis Cosgrove
 */
public class JIRAUtilities {
	public static int getType( edu.cmu.cs.dennisc.jira.JIRAReport.Type type ) {
		if( type == edu.cmu.cs.dennisc.jira.JIRAReport.Type.BUG ) {
			return 1;
		} else if( type == edu.cmu.cs.dennisc.jira.JIRAReport.Type.NEW_FEAURE ) {
			return 2;
		} else if( type == edu.cmu.cs.dennisc.jira.JIRAReport.Type.IMPROVEMENT ) {
			return 4;
		} else {
			throw new RuntimeException();
		}
	}
	public static String getPriority() {
		return "6"; //Unspecified
	}
	public static String ensureStringWithinLimit( String s, int limit ) {
		if( s.length() > limit ) {
			return s.substring( 0, limit+1 );
		} else {
			return s;
		}
	}
}
