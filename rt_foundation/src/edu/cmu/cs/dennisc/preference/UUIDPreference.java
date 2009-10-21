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
package edu.cmu.cs.dennisc.preference;

/**
 * @author Dennis Cosgrove
 */
public class UUIDPreference extends Preference< java.util.UUID > {
	public UUIDPreference( java.util.UUID defaultValue ) {
		super( defaultValue );
	}
	@Override
	protected java.util.UUID getValue(java.util.prefs.Preferences utilPrefs, String key, java.util.UUID defaultValue) {
		java.util.UUID rv;
		String s = utilPrefs.get( key, null );
		if( s != null ) {
			rv = java.util.UUID.fromString(s);
		} else {
			rv = defaultValue;
		}
		return rv;
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences utilPrefs, String key, java.util.UUID nextValue) {
		String s;
		if( nextValue != null ) {
			s = nextValue.toString();
		} else {
			s = null;
		}
		utilPrefs.put(key, s);
	}
}
