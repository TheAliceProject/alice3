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
public class PreferenceStringState extends org.lgna.croquet.StringState {
	private static final String NULL_VALUE = "__null__";

	private static final String CHARSET_NAME = "UTF-8";

	private static javax.crypto.Cipher getCypher( byte[] encryptionKey, int mode ) throws java.security.InvalidKeyException, java.security.spec.InvalidKeySpecException, java.security.NoSuchAlgorithmException, javax.crypto.NoSuchPaddingException {
		final String ALGORITHM = "DES";
		javax.crypto.spec.DESKeySpec keySpec = new javax.crypto.spec.DESKeySpec( encryptionKey );
		javax.crypto.SecretKey secretKey = javax.crypto.SecretKeyFactory.getInstance( ALGORITHM ).generateSecret( keySpec );
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance( ALGORITHM );
		cipher.init( mode, secretKey );
		return cipher;
	}

	private static String getInitialValue( java.util.UUID id, String defaultInitialValue, byte[] encryptionKey ) {
		java.util.prefs.Preferences userPreferences = PreferenceManager.getUserPreferences();
		if( userPreferences != null ) {
			if( defaultInitialValue != null ) {
				//pass
			} else {
				defaultInitialValue = NULL_VALUE;
			}
			String rv = userPreferences.get( id.toString(), defaultInitialValue );
			if( encryptionKey != null ) {
				try {
					javax.crypto.Cipher cipher = getCypher( encryptionKey, javax.crypto.Cipher.DECRYPT_MODE );
					byte[] base64 = org.apache.axis.encoding.Base64.decode( rv );
					byte[] bytes = cipher.doFinal( base64 );
					rv = new String( bytes, CHARSET_NAME );
				} catch( Exception e ) {
					e.printStackTrace();
					rv = defaultInitialValue;
				}
			}
			if( NULL_VALUE.equals( rv ) ) {
				rv = null;
			}
			return rv;
		} else {
			return defaultInitialValue;
		}
	}

	private static java.util.List<PreferenceStringState> instances = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	public final static void preserveAll( java.util.prefs.Preferences userPreferences ) {
		for( PreferenceStringState state : instances ) {
			String key = state.getMigrationId().toString();
			String value = state.getValue();
			if( value != null ) {
				//pass
			} else {
				value = NULL_VALUE;
			}
			String possiblyEncriptedValue;
			if( state.encryptionKey != null ) {
				try {
					javax.crypto.Cipher cipher = getCypher( state.encryptionKey, javax.crypto.Cipher.ENCRYPT_MODE );
					byte[] bytes = cipher.doFinal( value.getBytes( CHARSET_NAME ) );
					possiblyEncriptedValue = org.apache.axis.encoding.Base64.encode( bytes );
				} catch( Exception e ) {
					possiblyEncriptedValue = null;
				}
			} else {
				possiblyEncriptedValue = value;
			}
			if( possiblyEncriptedValue != null ) {
				if( state.isStoringPreferenceDesired() ) {
					userPreferences.put( key, possiblyEncriptedValue );
				} else {
					userPreferences.remove( key );
				}
			}
		}
	}

	private final byte[] encryptionKey;

	private static byte[] getEncryptionKey( String s ) {
		try {
			return s.getBytes( CHARSET_NAME );
		} catch( java.io.UnsupportedEncodingException uee ) {
			throw new RuntimeException( CHARSET_NAME, uee );
		}
	}

	private PreferenceStringState( org.lgna.croquet.Group group, java.util.UUID id, String initialValue, byte[] encryptionKey ) {
		super( group, id, getInitialValue( id, initialValue, encryptionKey ) );
		this.encryptionKey = encryptionKey;
		assert instances.contains( this ) == false;
		instances.add( this );
	}

	public PreferenceStringState( org.lgna.croquet.Group group, java.util.UUID id, String initialValue, String encryptionText ) {
		this( group, id, initialValue, getEncryptionKey( encryptionText ) );
	}

	public PreferenceStringState( org.lgna.croquet.Group group, java.util.UUID id, String initialValue ) {
		this( group, id, initialValue, (byte[])null );
	}

	protected boolean isStoringPreferenceDesired() {
		return true;
	}
}
