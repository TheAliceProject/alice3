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
package edu.cmu.cs.dennisc.scenegraph.util;

/**
 * @author Dennis Cosgrove
 */
public class BoundingBoxDecorator extends edu.cmu.cs.dennisc.scenegraph.Visual {
	private edu.cmu.cs.dennisc.scenegraph.SingleAppearance m_sgFrontAppearance = new edu.cmu.cs.dennisc.scenegraph.SingleAppearance();
	private edu.cmu.cs.dennisc.scenegraph.SingleAppearance m_sgBackAppearance = new edu.cmu.cs.dennisc.scenegraph.SingleAppearance();
	private edu.cmu.cs.dennisc.scenegraph.Box m_sgBox = new edu.cmu.cs.dennisc.scenegraph.Box();

	public BoundingBoxDecorator() {
		m_sgFrontAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.YELLOW );
		m_sgFrontAppearance.setShadingStyle( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.NONE );
		m_sgFrontAppearance.setFillingStyle( edu.cmu.cs.dennisc.scenegraph.FillingStyle.WIREFRAME );
		m_sgBackAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.DARK_GRAY );
		m_sgBackAppearance.setShadingStyle( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.NONE );
		m_sgBackAppearance.setFillingStyle( edu.cmu.cs.dennisc.scenegraph.FillingStyle.WIREFRAME );
		frontFacingAppearance.setValue( m_sgFrontAppearance );
		backFacingAppearance.setValue( m_sgBackAppearance );
		geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { m_sgBox } );
	}
	
	public void setBox( edu.cmu.cs.dennisc.math.AxisAlignedBox box ) {
		m_sgBox.set( box );
	}
	
	@Override
	public void setName( java.lang.String name ) {
		super.setName( name );
		if( name != null ) {
			//pass
		} else {
			name = "null";
		}
		m_sgFrontAppearance.setName( name + ".m_sgFrontAppearance" );
		m_sgBackAppearance.setName( name + ".m_sgBackAppearance" );
		m_sgBox.setName( name + ".m_sgBox" );
	}
}
