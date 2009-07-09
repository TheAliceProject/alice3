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

package edu.cmu.cs.dennisc.login;

public class AccountManager {
	private static java.util.Map< String, AccountInformation > map = new java.util.HashMap< String, AccountInformation >();
	
	public static AccountInformation get( String key ) {
		return map.get( key );
	}
	public static void logIn( String key, AccountInformation accountInformation ) {
		map.put( key, accountInformation );
	}
	public static void logIn( String key, String id, String password, String fullName ) {
		logIn( key, new AccountInformation( id, password, fullName ) );
	}
	public static void logOut( String key ) {
		map.remove( key );
	}
}
