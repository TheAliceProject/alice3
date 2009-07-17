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
	private static java.util.Map< PackageReflectionProxy, PackageDeclaredInJava > s_map = new java.util.HashMap< PackageReflectionProxy, PackageDeclaredInJava >();

	private PackageReflectionProxy packageReflectionProxy;

	public static PackageDeclaredInJava get( PackageReflectionProxy packageReflectionProxy ) {
		if( packageReflectionProxy != null ) {
			PackageDeclaredInJava rv = s_map.get( packageReflectionProxy );
			if( rv != null ) {
				//pass
			} else {
				rv = new PackageDeclaredInJava( packageReflectionProxy );
				s_map.put( packageReflectionProxy, rv );
			}
			return rv;
		} else {
			return null;
		}
	}
	public static PackageDeclaredInJava get( Package pckg ) {
		return get( new PackageReflectionProxy( pckg ) );
	}
	private PackageDeclaredInJava( PackageReflectionProxy packageReflectionProxy ) {
		this.packageReflectionProxy = packageReflectionProxy;
	}

	public PackageReflectionProxy getPackageReflectionProxy() {
		return this.packageReflectionProxy;
	}

	@Override
	public boolean isDeclaredInAlice() {
		return false;
	}
	@Override
	public String getName() {
		return this.packageReflectionProxy.getName();
	}
	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return null;
	}
	
	@Override
	public boolean isEquivalentTo( Object o ) {
		PackageDeclaredInJava other = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( o, PackageDeclaredInJava.class );
		if( other != null ) {
			return this.packageReflectionProxy.equals( other.packageReflectionProxy );
		} else {
			return false;
		}
	}
}
