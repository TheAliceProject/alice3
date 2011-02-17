/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */
package edu.cmu.cs.dennisc.nebulous;

import org.alice.ide.IDE;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext;

/**
 * @author Dennis Cosgrove
 */
public class PersonAdapter extends ModelAdapter< Person > 
{
    
    @Override
    protected void renderGeometry(RenderContext rc)
    {
        super.renderGeometry(rc);
        //DEBUG RENDERING
        if (SystemUtilities.isPropertyTrue(IDE.DEBUG_DRAW_PROPERTY_KEY))
        {
            m_element.debugRender(rc);
        }
    }
}
