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
package org.alice.apis.stage;

import org.alice.apis.stage.FemaleAdultFullBodyOutfit;

/**
 * @author Dennis Cosgrove
 */
public class FemaleAdult extends Adult implements Female {
	private boolean m_isPregnant = false;
	public FemaleAdult() {
		super( Gender.FEMALE );
	}
	@Override
	public Boolean isPregnant() {
		return m_isPregnant;
	}

	//todo
//	public void requestPregnant( Boolean isPregnant ) {
//		if( m_isPregnant != isPregnant ) {
//			m_isPregnant = isPregnant;
//			handleIsPregnantChange();
//		}
//	}
	public void setOutfit( FemaleAdultFullBodyOutfit outfit ) {
		super.setOutfit( outfit );
	}
}
