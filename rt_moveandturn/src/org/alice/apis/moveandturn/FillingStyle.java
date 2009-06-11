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
public enum FillingStyle {
	SOLID(edu.cmu.cs.dennisc.scenegraph.FillingStyle.SOLID), 
	WIREFRAME(edu.cmu.cs.dennisc.scenegraph.FillingStyle.WIREFRAME), 
	POINTS(edu.cmu.cs.dennisc.scenegraph.FillingStyle.POINTS);

	private edu.cmu.cs.dennisc.scenegraph.FillingStyle m_sgFillingStyle;

	private FillingStyle( edu.cmu.cs.dennisc.scenegraph.FillingStyle sgFillingStyle ) {
		m_sgFillingStyle = sgFillingStyle;
	}

	/*package protected*/ edu.cmu.cs.dennisc.scenegraph.FillingStyle getSGFillingStyle() {
		return m_sgFillingStyle;
	}

	/* package protected */ static FillingStyle valueOf( edu.cmu.cs.dennisc.scenegraph.FillingStyle sgFillingStyle ) {
		if( sgFillingStyle == edu.cmu.cs.dennisc.scenegraph.FillingStyle.SOLID ) {
			return SOLID;
		} else if( sgFillingStyle == edu.cmu.cs.dennisc.scenegraph.FillingStyle.WIREFRAME ) {
			return WIREFRAME;
		} else if( sgFillingStyle == edu.cmu.cs.dennisc.scenegraph.FillingStyle.POINTS ) {
			return POINTS;
		} else {
			return null;
		}
	}
}
