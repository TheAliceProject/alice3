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
public enum ShadingStyle {
	NONE(edu.cmu.cs.dennisc.scenegraph.ShadingStyle.NONE), 
	FLAT(edu.cmu.cs.dennisc.scenegraph.ShadingStyle.FLAT), 
	SMOOTH(edu.cmu.cs.dennisc.scenegraph.ShadingStyle.SMOOTH);

	private edu.cmu.cs.dennisc.scenegraph.ShadingStyle m_sgShadingStyle;

	private ShadingStyle( edu.cmu.cs.dennisc.scenegraph.ShadingStyle sgShadingStyle ) {
		m_sgShadingStyle = sgShadingStyle;
	}

	/*package protected*/ edu.cmu.cs.dennisc.scenegraph.ShadingStyle getSGShadingStyle() {
		return m_sgShadingStyle;
	}

	/* package protected */ static ShadingStyle valueOf( edu.cmu.cs.dennisc.scenegraph.ShadingStyle sgShadingStyle ) {
		if( sgShadingStyle == edu.cmu.cs.dennisc.scenegraph.ShadingStyle.NONE ) {
			return NONE;
		} else if( sgShadingStyle == edu.cmu.cs.dennisc.scenegraph.ShadingStyle.FLAT ) {
			return FLAT;
		} else if( sgShadingStyle == edu.cmu.cs.dennisc.scenegraph.ShadingStyle.SMOOTH ) {
			return SMOOTH;
		} else {
			return null;
		}
	}
}
