/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.ide.properties.uicontroller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class DoubleTextField extends JTextField
{
	private static final java.text.NumberFormat CENTI_FORMAT = new java.text.DecimalFormat( "0.00" );
	protected boolean isDirty = false;
	protected double trueValue = Double.NaN;

	public DoubleTextField( int columns )
	{
		super( columns );
		this.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				markValueSet();
			}
		} );
		this.getDocument().addDocumentListener( new edu.cmu.cs.dennisc.javax.swing.event.SimplifiedDocumentAdapter() {
			@Override
			protected void updated( javax.swing.event.DocumentEvent e )
			{
				markValueTemporary();
				DoubleTextField.this.isDirty = true;
			}
		} );
	}

	public boolean isValueValid()
	{
		String text = this.getText();
		Double value = edu.cmu.cs.dennisc.java.lang.DoubleUtilities.parseDoubleInCurrentDefaultLocale( text );
		return Double.isNaN( value ) == false;
	}

	public void markValueTemporary()
	{
		if( isValueValid() )
		{
			DoubleTextField.this.setForeground( Color.GRAY );
		}
		else
		{
			DoubleTextField.this.setForeground( Color.RED );
		}
	}

	public void markValueSet()
	{
		if( isValueValid() )
		{
			DoubleTextField.this.setForeground( Color.BLACK );
		}
		else
		{
			DoubleTextField.this.setForeground( Color.RED );
		}
	}

	public double getValue()
	{
		if( this.isDirty )
		{
			double value = edu.cmu.cs.dennisc.java.lang.DoubleUtilities.parseDoubleInCurrentDefaultLocale( this.getText() );
			this.trueValue = value;
		}
		this.isDirty = false;
		return this.trueValue;
	}

	public void setValue( Double value )
	{
		if( value != null )
		{
			this.trueValue = value;
			this.setText( edu.cmu.cs.dennisc.java.lang.DoubleUtilities.format( this.trueValue, CENTI_FORMAT ) );
		}
		else
		{
			this.trueValue = Double.NaN;
			this.setText( "None" );
		}
		this.isDirty = false;
		this.markValueSet();
	}
}
