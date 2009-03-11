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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public class TextChunk extends Chunk {
	private String text;
	public TextChunk( String text ) {
		this.text = text;
	}
	@Override
	public javax.swing.JComponent createComponent( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		//Label rv = new Label( "<html><h1>" + this.text + "</h1></html>" );
		zoot.ZLabel rv = new zoot.ZLabel( this.text );
		return rv;
	}
	@Override
	protected java.lang.StringBuffer updateRepr( java.lang.StringBuffer rv ) {
		rv.append( "text=" );
		rv.append( this.text );
		return rv;
	}
}
