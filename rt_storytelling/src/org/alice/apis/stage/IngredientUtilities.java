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
package org.alice.apis.stage;

public abstract class IngredientUtilities<E extends Ingredient> {
	private java.util.Map< Class< ? extends E >, Class< ? extends E >[] > mapInterfaceClsToImplementingClses = new java.util.HashMap< Class<? extends E>, Class<? extends E>[] >();

	protected void add( Class< ? extends E > interfaceCls, Class< ? extends E >... implementingClses ) {
		this.mapInterfaceClsToImplementingClses.put( interfaceCls, implementingClses );
	}

	protected abstract Class< ? extends E > getUnisexIntefaceClass( LifeStage lifeStage );
	protected abstract Class< ? extends E > getGenderedIntefaceClass( LifeStage lifeStage, Gender gender );
	public Class< ? extends E >[] getImplementingClasses( LifeStage lifeStage, Gender gender ) {
		Class< ? extends E > interfaceClsUnisex = this.getUnisexIntefaceClass( lifeStage );
		Class< ? extends E > interfaceClsGendered = this.getGenderedIntefaceClass( lifeStage, gender );

		Class< ? extends E >[] enumClsesUnisex = this.mapInterfaceClsToImplementingClses.get( interfaceClsUnisex );
		Class< ? extends E >[] enumClsesGendered = this.mapInterfaceClsToImplementingClses.get( interfaceClsGendered );

		Class< ? extends E >[] rv = enumClsesGendered;

		return rv;
	}
	public Class< ? extends E > getRandomClass( LifeStage lifeStage, Gender gender ) {
		return edu.cmu.cs.dennisc.random.RandomUtilities.getRandomValueFrom( getImplementingClasses( lifeStage, gender ) );
	}
	public E getRandomEnumConstant( LifeStage lifeStage, Gender gender ) {
		while( true ) {
			Class enumCls = getRandomClass( lifeStage, gender );
			E rv = (E)edu.cmu.cs.dennisc.random.RandomUtilities.getRandomEnumConstant( enumCls );
			if( rv != null ) {
				return rv;
			}
		}
	}
	
	public boolean isApplicable( E e, LifeStage lifeStage, Gender gender ) {
		assert e != null;
		assert lifeStage != null;
		assert gender != null;
		Class< ? extends E > interfaceClsUnisex = this.getUnisexIntefaceClass( lifeStage );
		Class< ? extends E > interfaceClsGendered = this.getGenderedIntefaceClass( lifeStage, gender );
		Class<?> eCls = e.getClass();
		//todo?
		return /*interfaceClsUnisex.isAssignableFrom( eCls ) ||*/ interfaceClsGendered.isAssignableFrom( eCls );
	}
	
}
