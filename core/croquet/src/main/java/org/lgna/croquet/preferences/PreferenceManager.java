/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.croquet.preferences;

/**
 * @author Dennis Cosgrove
 */
public class PreferenceManager {
	private PreferenceManager() {
		throw new AssertionError();
	}

	public static java.util.prefs.Preferences getUserPreferences() {
		org.lgna.croquet.Application application = org.lgna.croquet.Application.getActiveInstance();
		if( application != null ) {
			java.util.prefs.Preferences rv = java.util.prefs.Preferences.userNodeForPackage( application.getClass() );
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.clearAllPreferences" ) ) {
				try {
					rv.clear();
				} catch( java.util.prefs.BackingStoreException bse ) {
					throw new RuntimeException( bse );
				}
			}
			return rv;
		} else {
			return null;
		}
	}

	//	private static java.util.List< org.lgna.croquet.BooleanState > booleanStatePreferences = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	//	private static java.util.List< org.lgna.croquet.StringState > stringStatePreferences = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	//	private static java.util.List< org.lgna.croquet.ListSelectionState< ? > > listSelectionStatePreferences = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

	//	public static void registerAndInitializePreference( org.lgna.croquet.BooleanState booleanState ) {
	//		IDE ide = IDE.getActiveInstance();
	//		if( ide != null ) {
	//			java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( ide.getClass() );
	//			clearAllPreferencesIfRequested( userPreferences );
	//			java.util.UUID id = booleanState.getId();
	//			boolean value = userPreferences.getBoolean( id.toString(), booleanState.getValue() );
	//			booleanState.setValue( value );
	//			booleanStatePreferences.add( booleanState );
	//		} else {
	//			System.err.println( "registerAndInitializePreference: " + booleanState );
	//		}
	//	}
	//	public static void registerAndInitializePreference( org.lgna.croquet.StringState stringState ) {
	//		IDE ide = IDE.getActiveInstance();
	//		if( ide != null ) {
	//			java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( ide.getClass() );
	//			clearAllPreferencesIfRequested( userPreferences );
	//			java.util.UUID id = stringState.getId();
	//			String value = userPreferences.get( id.toString(), stringState.getValue() );
	//			stringState.setValue( value );
	//			stringStatePreferences.add( stringState );
	//		} else {
	//			System.err.println( "registerAndInitializePreference: " + stringState );
	//		}
	//	}
	//	public static void registerAndInitializePreference( org.lgna.croquet.ListSelectionState< ? > listSelectionState ) {
	//		IDE ide = IDE.getActiveInstance();
	//		if( ide != null ) {
	//			java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userNodeForPackage( ide.getClass() );
	//			clearAllPreferencesIfRequested( userPreferences );
	//			try {
	//				decode( listSelectionState, userPreferences );
	//			} catch( Throwable t ) {
	//				t.printStackTrace();
	//			}
	//			listSelectionStatePreferences.add( listSelectionState );
	//		} else {
	//			System.err.println( "registerAndInitializePreference: " + listSelectionState );
	//		}
	//	}

	private static java.util.List<org.lgna.croquet.SingleSelectListState<?, ?>> selectionOfListSelectionStatePreferences = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	//private static java.util.List<org.lgna.croquet.ListSelectionState<?>> dataOfListSelectionStatePreferences = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	private static java.util.List<org.lgna.croquet.data.ListData<?>> registeredListData = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

	private static String getKey( org.lgna.croquet.Model model ) {
		return model.getMigrationId().toString();
	}

	private static <T> T decodeItem( byte[] data, org.lgna.croquet.ItemCodec<T> codec ) {
		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream( data );
		edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( bais );
		return codec.decodeValue( decoder );
	}

	private static <T> byte[] encodeItem( T value, org.lgna.croquet.ItemCodec<T> codec ) {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( baos );
		codec.encodeValue( encoder, value );
		encoder.flush();
		return baos.toByteArray();
	}

	private static <T> T[] decodeArray( byte[] data, org.lgna.croquet.ItemCodec<T> codec ) {
		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream( data );
		edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( bais );
		boolean isNotNull = decoder.decodeBoolean();
		if( isNotNull ) {
			final int N = decoder.decodeInt();
			Class<T> componentType = codec.getValueClass();
			T[] rv = (T[])java.lang.reflect.Array.newInstance( componentType, N );
			for( int i = 0; i < rv.length; i++ ) {
				rv[ i ] = codec.decodeValue( decoder );
			}
			return rv;
		} else {
			return null;
		}
	}

	private static <T> byte[] encodeArray( T[] value, org.lgna.croquet.ItemCodec<T> codec ) {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( baos );
		boolean isNotNull = value != null;
		encoder.encode( isNotNull );
		if( isNotNull ) {
			encoder.encode( value.length );
			for( T element : value ) {
				codec.encodeValue( encoder, element );
			}
		}
		encoder.flush();
		return baos.toByteArray();
	}

	private static <E, D extends org.lgna.croquet.data.ListData<E>> org.lgna.croquet.SingleSelectListState<E, D> decodeSelection( org.lgna.croquet.SingleSelectListState<E, D> rv, java.util.prefs.Preferences userPreferences ) {
		org.lgna.croquet.ItemCodec<E> codec = rv.getItemCodec();
		E defaultValue = rv.getValue();
		byte[] defaultEncoding = encodeItem( defaultValue, codec );
		String key = getKey( rv );
		byte[] encoding = userPreferences.getByteArray( key, defaultEncoding );
		if( java.util.Arrays.equals( defaultEncoding, encoding ) ) {
			//pass
		} else {
			E value = decodeItem( encoding, codec );
			rv.setValueTransactionlessly( value );
		}
		return rv;
	}

	private static <E, D extends org.lgna.croquet.data.ListData<E>> org.lgna.croquet.SingleSelectListState<E, D> decodeData( org.lgna.croquet.SingleSelectListState<E, D> rv, java.util.prefs.Preferences userPreferences ) {
		org.lgna.croquet.ItemCodec<E> codec = rv.getItemCodec();
		E[] defaultValue = rv.toArray();
		byte[] defaultEncoding = encodeArray( defaultValue, codec );
		String key = getKey( rv );
		byte[] encoding = userPreferences.getByteArray( key, defaultEncoding );
		if( java.util.Arrays.equals( defaultEncoding, encoding ) ) {
			//pass
		} else {
			E[] value = decodeArray( encoding, codec );
			rv.setListData( -1, value );
		}
		return rv;
	}

	private static <E> void encodeListData( org.lgna.croquet.data.ListData<E> listData, java.util.prefs.Preferences userPreferences ) {
		org.lgna.croquet.ItemCodec<E> codec = listData.getItemCodec();
		E[] value = listData.toArray();
		byte[] encoding = encodeArray( value, codec );
		String key = listData.getPreferenceKey();
		userPreferences.putByteArray( key, encoding );
	}

	public static <E> E[] decodeListData( String key, org.lgna.croquet.ItemCodec<E> codec, E[] defaultValue ) {
		E[] rv;
		java.util.prefs.Preferences userPreferences = PreferenceManager.getUserPreferences();
		if( userPreferences != null ) {
			byte[] defaultEncoding = encodeArray( defaultValue, codec );
			byte[] encoding = userPreferences.getByteArray( key, defaultEncoding );
			if( java.util.Arrays.equals( defaultEncoding, encoding ) ) {
				rv = defaultValue;
			} else {
				rv = decodeArray( encoding, codec );
			}
		} else {
			rv = defaultValue;
		}
		return rv;
	}

	private static <E, D extends org.lgna.croquet.data.ListData<E>> void encodeSelection( org.lgna.croquet.SingleSelectListState<E, D> listSelectionState, java.util.prefs.Preferences userPreferences ) {
		org.lgna.croquet.ItemCodec<E> codec = listSelectionState.getItemCodec();
		E value = listSelectionState.getValue();
		byte[] encoding = encodeItem( value, codec );
		String key = getKey( listSelectionState );
		userPreferences.putByteArray( key, encoding );
	}

	private static <E, D extends org.lgna.croquet.data.ListData<E>> void encodeData( org.lgna.croquet.SingleSelectListState<E, D> listSelectionState, java.util.prefs.Preferences userPreferences ) {
		org.lgna.croquet.ItemCodec<E> codec = listSelectionState.getItemCodec();
		E[] value = listSelectionState.toArray();
		byte[] encoding = encodeArray( value, codec );
		String key = getKey( listSelectionState );
		userPreferences.putByteArray( key, encoding );
	}

	public static void registerListData( org.lgna.croquet.data.ListData<?> listData ) {
		if( registeredListData.contains( listData ) ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( listData );
		}
		registeredListData.add( listData );
	}

	//todo:
	public static void registerAndInitializeSelectionOnlyOfListSelectionState( org.lgna.croquet.SingleSelectListState<?, ?> listSelectionState ) {
		if( selectionOfListSelectionStatePreferences.contains( listSelectionState ) ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( listSelectionState );
		}
		java.util.prefs.Preferences userPreferences = PreferenceManager.getUserPreferences();
		if( userPreferences != null ) {
			try {
				decodeSelection( listSelectionState, userPreferences );
			} catch( Throwable t ) {
				t.printStackTrace();
			}
			selectionOfListSelectionStatePreferences.add( listSelectionState );
		} else {
			System.err.println( "registerAndInitializeSelectionOnlyOfListSelectionState: " + listSelectionState );
		}
	}

	//	//todo:
	//	public static void registerAndInitializeDataOnlyOfListSelectionState( org.lgna.croquet.ListSelectionState<?> listSelectionState ) {
	//		java.util.prefs.Preferences userPreferences = PreferenceManager.getUserPreferences();
	//		if( userPreferences != null ) {
	//			try {
	//				decodeData( listSelectionState, userPreferences );
	//			} catch( Throwable t ) {
	//				t.printStackTrace();
	//			}
	//			dataOfListSelectionStatePreferences.add( listSelectionState );
	//		} else {
	//			System.err.println( "registerAndInitializePreference: " + listSelectionState );
	//		}
	//	}

	public static void preservePreferences() throws java.util.prefs.BackingStoreException {
		java.util.prefs.Preferences userPreferences = PreferenceManager.getUserPreferences();
		if( userPreferences != null ) {
			org.lgna.croquet.preferences.PreferenceBooleanState.preserveAll( userPreferences );
			org.lgna.croquet.preferences.PreferenceStringState.preserveAll( userPreferences );
			for( org.lgna.croquet.SingleSelectListState<?, ?> listSelectionState : selectionOfListSelectionStatePreferences ) {
				try {
					encodeSelection( listSelectionState, userPreferences );
				} catch( Throwable t ) {
					t.printStackTrace();
				}
			}
			//			for( org.lgna.croquet.ListSelectionState<?> listSelectionState : dataOfListSelectionStatePreferences ) {
			//				try {
			//					encodeData( listSelectionState, userPreferences );
			//				} catch( Throwable t ) {
			//					t.printStackTrace();
			//				}
			//			}

			for( org.lgna.croquet.data.ListData<?> listData : registeredListData ) {
				encodeListData( listData, userPreferences );
			}
			userPreferences.flush();
		}
	}
}
