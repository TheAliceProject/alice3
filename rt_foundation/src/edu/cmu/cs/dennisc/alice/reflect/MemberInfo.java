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
package edu.cmu.cs.dennisc.alice.reflect;

import edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities;

/**
 * @author Dennis Cosgrove
 */
public abstract class MemberInfo implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	private ClassInfo declaringClassInfo;
	public MemberInfo( ClassInfo declaringClassInfo ) {
		this.declaringClassInfo = declaringClassInfo;
	}
	public MemberInfo( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		decode( binaryDecoder );
	}
	protected Class<?> getDeclaringCls() {
		return this.declaringClassInfo.getCls();
	}
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.declaringClassInfo = ClassInfo.forName( binaryDecoder.decodeString() );
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.declaringClassInfo.getClsName() );
	}
}
