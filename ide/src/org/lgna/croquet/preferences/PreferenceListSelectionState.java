/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.lgna.croquet.preferences;

/**
 * @author Dennis Cosgrove
 */
public class PreferenceListSelectionState< T > extends org.lgna.croquet.DefaultListSelectionState< T > {
	private static String getKey( org.lgna.croquet.Model model ) {
		return model.getId().toString();
	}
	
	private static <T> T decode( byte[] data, org.lgna.croquet.ItemCodec< T > codec ) {
		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream( data );
		edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( bais );
		return codec.decodeValue( decoder );
	}
	private static <T> byte[] encode( T value, org.lgna.croquet.ItemCodec< T > codec ) {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( baos );
		codec.encodeValue( encoder, value );
		encoder.flush();
		return baos.toByteArray();
	}
	private static <E> org.lgna.croquet.ListSelectionState< E > decode( org.lgna.croquet.ListSelectionState< E > rv, java.util.prefs.Preferences userPreferences ) {
		org.lgna.croquet.ItemCodec< E > codec = rv.getItemCodec();
		E defaultValue = rv.getSelectedItem();
		byte[] defaultEncoding = encode( defaultValue, codec );
		String key = getKey( rv );
		byte[] encoding = userPreferences.getByteArray( key, defaultEncoding );
		if( java.util.Arrays.equals( defaultEncoding, encoding ) ) {
			//pass
		} else {
			E value = decode( encoding, codec );
			rv.setSelectedItem( value );
		}
		return rv;
	}
	private static <E> void encode( org.lgna.croquet.ListSelectionState< E > listSelectionState, java.util.prefs.Preferences userPreferences ) {
		org.lgna.croquet.ItemCodec< E > codec = listSelectionState.getItemCodec();
		E value = listSelectionState.getSelectedItem();
		byte[] encoding = encode( value, codec );
		String key = getKey( listSelectionState );
		userPreferences.putByteArray( key, encoding );
	}

	
	private static int getInitialSelectionIndex( java.util.UUID id, int defaultInitialSelectionIndex ) {
		//todo
		return defaultInitialSelectionIndex;
	}
	private static <T> T[] getInitialData( java.util.UUID id, org.lgna.croquet.ItemCodec< T > codec, T[] defaultData ) {
		//todo
		return defaultData;
	}
	private static java.util.List< PreferenceListSelectionState<?> > instances = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public final static void preserveAll( java.util.prefs.Preferences userPreferences ) {
		for( PreferenceListSelectionState<?> state : instances ) {
			try {
				encode( state, userPreferences );
			} catch( Throwable t ) {
				t.printStackTrace();
			}
		}
	}
	public PreferenceListSelectionState( org.lgna.croquet.Group group, java.util.UUID id, org.lgna.croquet.ItemCodec< T > codec, int selectionIndex, T... data ) {
		super( group, id, codec, getInitialSelectionIndex( id, selectionIndex ), getInitialData( id, codec, data ) );
		
		//todo: remove
		decode( this, PreferenceManager.getUserPreferences() );
		
		assert instances.contains( this ) == false;
		instances.add( this );
	}
}

//public static void registerAndInitializePreference( org.lgna.croquet.ListSelectionState< ? > listSelectionState ) {
//	IDE ide = IDE.getActiveInstance();
//	if( ide != null ) {
//		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( ide.getClass() );
//		clearAllPreferencesIfRequested( userPreferences );
//		try {
//			decode( listSelectionState, userPreferences );
//		} catch( Throwable t ) {
//			t.printStackTrace();
//		}
//		listSelectionStatePreferences.add( listSelectionState );
//	} else {
//		System.err.println( "registerAndInitializePreference: " + listSelectionState );
//	}
//}
