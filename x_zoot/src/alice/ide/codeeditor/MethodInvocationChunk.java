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
package alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public class MethodInvocationChunk extends Chunk {
	private String methodName;
	public MethodInvocationChunk( String methodNamePlusParens ) {
		this.methodName = methodNamePlusParens.substring( 0, methodNamePlusParens.length()-2 );
	}
	private Object invoke( Object instance ) {
		java.lang.reflect.Method mthd = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getMethod( instance.getClass(), this.methodName );
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.invoke( instance, mthd );
	}
	@Override
	public javax.swing.JComponent createComponent( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		javax.swing.JComponent rv;
		if( owner instanceof edu.cmu.cs.dennisc.alice.ast.Node && methodName.equals( "getName" ) ) {
			edu.cmu.cs.dennisc.alice.ast.Node node = (edu.cmu.cs.dennisc.alice.ast.Node)owner;
			alice.ide.ast.NodeNameLabel label = new alice.ide.ast.NodeNameLabel( node );
			if( node instanceof edu.cmu.cs.dennisc.alice.ast.AbstractMethod ) {
				label.scaleFont( 1.5f );
			}
			rv = label;
		} else if( owner instanceof edu.cmu.cs.dennisc.alice.ast.Argument && methodName.equals( "getParameterNameText" ) ) {
			edu.cmu.cs.dennisc.alice.ast.Argument argument = (edu.cmu.cs.dennisc.alice.ast.Argument)owner;
			rv = new alice.ide.ast.NodeNameLabel( argument.parameter.getValue() );
		} else {
			Object o = this.invoke( owner );
			String s;
			if( o != null ) {
				if( o instanceof edu.cmu.cs.dennisc.alice.ast.AbstractType ) {
					s = ((edu.cmu.cs.dennisc.alice.ast.AbstractType)o).getName();
				} else {
					s = o.toString();
				}
			} else {
				s = null;
			}
			//s = "<html><h1>" + s + "</h1></html>";
			rv = new zoot.ZLabel( s );
		}
		return rv;
	}
	@Override
	protected java.lang.StringBuffer updateRepr( java.lang.StringBuffer rv ) {
		rv.append( "methodName=" );
		rv.append( this.methodName );
		return rv;
	}
}
