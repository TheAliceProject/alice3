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

package edu.cmu.cs.dennisc.alice.ast;

/**
 * @author Dennis Cosgrove
 */
public class PackageDeclaredInJava extends AbstractPackage {
	private static java.util.Map< Package, PackageDeclaredInJava > s_map;
	/*package-private*/ static PackageDeclaredInJava get( Package pckg ) {
		if( pckg != null ) {
			if( s_map != null ) {
				//pass
			} else {
				s_map = new java.util.HashMap< Package, PackageDeclaredInJava >();				
			}
			PackageDeclaredInJava rv = s_map.get( pckg );
			if( rv != null ) {
				//pass
			} else {
				rv = new PackageDeclaredInJava( pckg );
			}
			return rv;
		} else {
			return null;
		}
	}

	private Package m_pckg;
	private PackageDeclaredInJava( Package pckg ) {
		m_pckg = pckg;
	}
	@Override
	public boolean isDeclaredInAlice() {
		return false;
	}
	@Override
	public String getName() {
		return m_pckg.getName();
	}
	
	@Override
	public boolean isEquivalentTo( Object other ) {
		if( other instanceof PackageDeclaredInJava ) {
			return m_pckg.equals( ((PackageDeclaredInJava)other).m_pckg );
		} else {
			return false;
		}
	}
}
