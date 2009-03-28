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
package org.alice.stageide.personeditor;

/**
 * @author Dennis Cosgrove
 */
abstract class ArrayOfEnumConstantsListModel extends ArrayListModel {
	static Object[] to( Class<?>[] clses ) {
		java.util.List< Enum > list = new java.util.LinkedList< Enum >();
		for( Class<?> cls : clses ) {
			Class< ? extends Enum > enumCls = (Class< ? extends Enum >)cls;
			for( Enum e : enumCls.getEnumConstants() ) {
				list.add( e );
			}
		}
		return list.toArray();
	}
	public ArrayOfEnumConstantsListModel( Class<?>[] clses ) {
		super( to( clses ) );
	}
}

