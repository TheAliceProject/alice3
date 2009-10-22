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
public abstract class ListPreference< E > extends Preference< java.util.List< E > > {
	private static final int LIST_VERSION = 1;
	private static final String LIST_VERSION_SUFFIX = ".list_version";
	private static final String ITEM_VERSION_SUFFIX = ".item_version";
	private static final String DATA_SUFFIX = ".data";
	public ListPreference( java.util.List< E > defaultValue ) {
		super( defaultValue );
	}
	
	protected abstract int getItemVersion();
	protected abstract void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, E item );
	private byte[] encode( java.util.List< E > value ) {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( baos );
		binaryEncoder.encode( value.size() );
		for( E item : value ) {
			this.encode( binaryEncoder, item );
		}
		binaryEncoder.flush();
		return baos.toByteArray();
	}
	protected abstract E decode( int itemVersion, edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder );
	private java.util.List< E > decode( int version, byte[] value ) {
		if( value != null ) {
			java.util.List< E > rv = new java.util.LinkedList<E>();
			java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream( value );
			edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( bais );
			final int N = binaryDecoder.decodeInt();
			for( int i=0; i<N; i++  ) {
				E item = this.decode( version, binaryDecoder);
				rv.add( item );
			}
			return rv;
		} else {
			return null;
		}
	}
	@Override
	protected java.util.List< E > getValue(java.util.prefs.Preferences utilPrefs, String key, java.util.List< E > defaultValue ) {
		int listVersion = utilPrefs.getInt( key+LIST_VERSION_SUFFIX, 0 );
		if( listVersion == 1 ) {
			int itemVersion = utilPrefs.getInt( key+ITEM_VERSION_SUFFIX, 0 );
			byte[] defaultData = this.encode(defaultValue);
			byte[] currentData = utilPrefs.getByteArray( key, defaultData );
			return this.decode( itemVersion, currentData );
		} else {
			return defaultValue;
		}
	}
	@Override
	protected void setAndCommitValue(java.util.prefs.Preferences utilPrefs, String key, java.util.List< E > nextValue) {
		utilPrefs.putInt(key+LIST_VERSION_SUFFIX, LIST_VERSION );
		utilPrefs.putInt(key+ITEM_VERSION_SUFFIX, this.getItemVersion() );
		if( nextValue != null ) {
			utilPrefs.putByteArray(key+DATA_SUFFIX, this.encode(nextValue));
		} else {
			utilPrefs.putByteArray(key+DATA_SUFFIX, null);
		}
	}
}
