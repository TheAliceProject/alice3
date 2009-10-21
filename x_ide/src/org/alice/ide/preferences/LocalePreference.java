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
package org.alice.ide.preferences;

import edu.cmu.cs.dennisc.preference.Preference;

/**
 * @author Dennis Cosgrove
 */
public class LocalePreference extends Preference< java.util.Locale > {
	private static final int VERSION = 1;
	private static final String VERSION_SUFFIX = ".version";
	private static final String LANGUAGE_SUFFIX = ".language";
	private static final String COUNTRY_SUFFIX = ".country";
	private static final String VARIANT_SUFFIX = ".variant";
	public LocalePreference( java.util.Locale defaultValue ) {
		super( defaultValue );
	}
	@Override
	protected java.util.Locale getValue(java.util.prefs.Preferences utilPrefs, String key, java.util.Locale defaultValue ) {
		java.util.Locale rv;
		int version = utilPrefs.getInt(key+VERSION_SUFFIX,  0 );
		if( version == 1 ) {
			String language = utilPrefs.get(key+LANGUAGE_SUFFIX, null);
			if( language != null ) {
				String country = utilPrefs.get(key+COUNTRY_SUFFIX, "");
				String variant = utilPrefs.get(key+VARIANT_SUFFIX, "");
				rv = new java.util.Locale( language, country, variant );
			} else {
				rv = defaultValue;
			}
		} else {
			rv = defaultValue;
		}
		return rv;
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences utilPrefs, String key, java.util.Locale nextValue ) {
		utilPrefs.putInt(key+VERSION_SUFFIX, VERSION );
		if( nextValue != null ) {
			utilPrefs.put(key+LANGUAGE_SUFFIX, nextValue.getLanguage());
			utilPrefs.put(key+COUNTRY_SUFFIX, nextValue.getCountry());
			utilPrefs.put(key+VARIANT_SUFFIX, nextValue.getVariant());
		} else {
			utilPrefs.put(key+LANGUAGE_SUFFIX, null);
		}
	}
}
