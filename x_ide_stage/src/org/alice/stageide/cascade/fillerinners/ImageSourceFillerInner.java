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
package org.alice.stageide.cascade.fillerinners;

/**
 * @author Dennis Cosgrove
 */
public class ImageSourceFillerInner extends org.alice.ide.cascade.fillerinners.InstanceCreationFillerInner {
	public ImageSourceFillerInner() {
		super( org.alice.apis.moveandturn.ImageSource.class );
	}
	@Override
	public void addFillIns( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		Iterable< org.alice.virtualmachine.Resource > resources = ide.getResources();
		assert resources != null;
		synchronized( resources ) {
			for( org.alice.virtualmachine.Resource resource : resources ) {
				if( resource instanceof org.alice.virtualmachine.resources.ImageResource ) {
					org.alice.virtualmachine.resources.ImageResource imageResource = (org.alice.virtualmachine.resources.ImageResource)resource;
					edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( org.alice.apis.moveandturn.ImageSource.class, org.alice.virtualmachine.resources.ImageResource.class );
					edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = new edu.cmu.cs.dennisc.alice.ast.ResourceExpression( org.alice.virtualmachine.resources.ImageResource.class, imageResource );
					edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter0 = constructor.getParameters().get( 0 );
					edu.cmu.cs.dennisc.alice.ast.Argument argument0 = new edu.cmu.cs.dennisc.alice.ast.Argument( parameter0, resourceExpression );
					blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.InstanceCreation( constructor, argument0 ) ) ); 
				}
			}
		}
		blank.addSeparator();
		blank.addFillIn( new org.alice.stageide.cascade.customfillin.ImportNewImageSourceFillIn() );
	}
}
