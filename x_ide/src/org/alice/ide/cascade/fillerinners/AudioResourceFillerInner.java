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
public class AudioResourceFillerInner extends ResourceFillerInner {
	public AudioResourceFillerInner() {
		super( org.alice.virtualmachine.resources.AudioResource.class );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.ResourceExpression createResourceExpressionIfAppropriate( org.alice.virtualmachine.Resource resource ) {
		if( resource instanceof org.alice.virtualmachine.resources.AudioResource ) {
			return new edu.cmu.cs.dennisc.alice.ast.ResourceExpression( org.alice.virtualmachine.resources.AudioResource.class, (org.alice.virtualmachine.resources.AudioResource)resource );
		} else {
			return null;
		}
	}
	@Override
	protected edu.cmu.cs.dennisc.cascade.FillIn< ? > createImportNewResourceFillIn() {
		return new org.alice.ide.cascade.customfillin.ImportNewAudioResourceFillIn();
	}
}
