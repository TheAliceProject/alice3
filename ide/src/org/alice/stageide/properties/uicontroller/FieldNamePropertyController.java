/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.alice.stageide.properties.uicontroller;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import org.alice.ide.properties.adapter.PropertyAdapter;
import org.alice.ide.properties.uicontroller.BasicPropertyController;

import edu.cmu.cs.dennisc.croquet.Component;
import edu.cmu.cs.dennisc.croquet.Label;

public class FieldNamePropertyController extends BasicPropertyController<String>
{

    private Label label;
    
    public FieldNamePropertyController(PropertyAdapter<String, ?> propertyAdapter)
    {
        super(propertyAdapter);
    }

    
    @Override
    protected Component<?> createPropertyComponent()
    {
        this.label = new Label("", 1.2f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD)
        {
           
            @Override
            protected JLabel createAwtComponent()
            {
                return new javax.swing.JLabel() {
                    @Override
                    protected void paintComponent(java.awt.Graphics g) {
                        g.setColor( this.getBackground() );
                        if (g instanceof Graphics2D)
                        {
                           ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        }
                        g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 12, 12 );
                        super.paintComponent(g);
                    }
                };
            }
        };
        this.label.setBackgroundColor(org.alice.ide.IDE.getSingleton().getTheme().getSelectedColor());
        this.label.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        return this.label;
    }

    @Override
    public Class<?> getPropertyType()
    {
        return String.class;
    }
 
    @Override
    protected void setValueOnUI(String value)
    {
        if (value != null)
        {
            this.label.getAwtComponent().setText(value);
        }
        else
        {
            this.label.getAwtComponent().setText(BLANK_STRING);
        }
        
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        //Do Nothing
    }
    
}
