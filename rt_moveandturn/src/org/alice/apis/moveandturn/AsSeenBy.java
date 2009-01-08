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
package org.alice.apis.moveandturn;

/**
 * @author Dennis Cosgrove
 */
public enum AsSeenBy implements ReferenceFrame {
	SCENE {
		public edu.cmu.cs.dennisc.scenegraph.ReferenceFrame getSGReferenceFrame() {
			return edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE;
		}
		public org.alice.apis.moveandturn.Composite getActualReferenceFrame( org.alice.apis.moveandturn.Composite ths ) {
			return ths.getScene();
		}
	},
	PARENT {
		public edu.cmu.cs.dennisc.scenegraph.ReferenceFrame getSGReferenceFrame() {
			return edu.cmu.cs.dennisc.scenegraph.AsSeenBy.PARENT;
		}
		public org.alice.apis.moveandturn.Composite getActualReferenceFrame( org.alice.apis.moveandturn.Composite ths ) {
			if( ths instanceof AbstractTransformable ) {
				return ((AbstractTransformable)ths).getVehicle();
			} else {
				throw new RuntimeException( "todo" );
			}
		}
	},
	SELF {
		public edu.cmu.cs.dennisc.scenegraph.ReferenceFrame getSGReferenceFrame() {
			return edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SELF;
		}
		public org.alice.apis.moveandturn.Composite getActualReferenceFrame( org.alice.apis.moveandturn.Composite ths ) {
			return ths;
		}
	};
	//todo: remove?
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( ReferenceFrame other ) {
		return this.getSGReferenceFrame().getTransformation( other.getSGReferenceFrame() );
	}
}
