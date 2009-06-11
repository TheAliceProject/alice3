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

import edu.cmu.cs.dennisc.alice.annotations.MethodTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
public class StandIn extends AbstractTransformable implements edu.cmu.cs.dennisc.pattern.Reusable {
	private edu.cmu.cs.dennisc.scenegraph.StandIn m_sgStandIn = new edu.cmu.cs.dennisc.scenegraph.StandIn();
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	@Override
	public edu.cmu.cs.dennisc.scenegraph.AbstractTransformable getSGAbstractTransformable() {
		return m_sgStandIn;
	}
	@Override
	public Composite getVehicle() {
		return (Composite)getElement( m_sgStandIn.vehicle.getValue() );
	}
	public void setVehicle( Composite vehicle ) {
		m_sgStandIn.vehicle.setValue( vehicle.getSGComposite() );
	}
	protected void handleVehicleChange( Composite vehicle ) {
		throw new RuntimeException( "not intended" );
	}
}
