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
package org.alice.ide.cascade.fillerinners;

/**
 * @author Dennis Cosgrove
 */
public abstract class ResourceFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public ResourceFillerInner( Class< ? extends org.alice.virtualmachine.Resource> cls ) {
		super( cls, edu.cmu.cs.dennisc.alice.ast.ResourceExpression.class );
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.ResourceExpression createResourceExpressionIfAppropriate( org.alice.virtualmachine.Resource resource );
	protected abstract edu.cmu.cs.dennisc.cascade.FillIn< ? > createImportNewResourceFillIn();
	@Override
	public void addFillIns( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		java.util.Set< org.alice.virtualmachine.Resource > resources = ide.getResources();
		if( resources != null && resources.isEmpty() == false ) {
			synchronized( resources ) {
				for( org.alice.virtualmachine.Resource resource : resources ) {
					edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = this.createResourceExpressionIfAppropriate( resource );
					if( resourceExpression != null ) {
						blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( resourceExpression ) ); 
					}
				}
			}
			blank.addSeparator();
		}
		blank.addFillIn( this.createImportNewResourceFillIn() );
	}
}
