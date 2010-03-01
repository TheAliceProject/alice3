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
package edu.cmu.cs.dennisc.cryptography;

/**
 * @author Dennis Cosgrove
 */
public class CryptographyUtilities {
	private static final byte[] SALT = { (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c, (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99 };
	private static final int ITERATION_COUNT = 20;

	private CryptographyUtilities() {
		throw new AssertionError();
	}
	public static byte[] encrypt( byte[] clear, char[] password ) {
		try {
			javax.crypto.spec.PBEParameterSpec pbeParamSpec = new javax.crypto.spec.PBEParameterSpec( SALT, ITERATION_COUNT );
			javax.crypto.spec.PBEKeySpec pbeKeySpec = new javax.crypto.spec.PBEKeySpec( password );
			javax.crypto.SecretKeyFactory keyFac = javax.crypto.SecretKeyFactory.getInstance( "PBEWithMD5AndDES" );
			javax.crypto.SecretKey pbeKey = keyFac.generateSecret( pbeKeySpec );
			javax.crypto.Cipher pbeCipher = javax.crypto.Cipher.getInstance( "PBEWithMD5AndDES" );
			pbeCipher.init( javax.crypto.Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec );
			return pbeCipher.doFinal( clear );
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	public static byte[] decrypt( byte[] crypt, char[] password ) {
		try {
			javax.crypto.spec.PBEParameterSpec pbeParamSpec = new javax.crypto.spec.PBEParameterSpec( SALT, ITERATION_COUNT );
			javax.crypto.spec.PBEKeySpec pbeKeySpec = new javax.crypto.spec.PBEKeySpec( password );
			javax.crypto.SecretKeyFactory keyFac = javax.crypto.SecretKeyFactory.getInstance( "PBEWithMD5AndDES" );
			javax.crypto.SecretKey pbeKey = keyFac.generateSecret( pbeKeySpec );
			javax.crypto.Cipher pbeCipher = javax.crypto.Cipher.getInstance( "PBEWithMD5AndDES" );
			pbeCipher.init( javax.crypto.Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec );
			return pbeCipher.doFinal( crypt );
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}
}
