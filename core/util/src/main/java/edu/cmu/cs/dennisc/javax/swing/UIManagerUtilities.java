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
package edu.cmu.cs.dennisc.javax.swing;

import edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class UIManagerUtilities {
	public static void setDefaultFontResource( FontUIResource fontUIResource ) {
		for( Object key : UIManager.getDefaults().keySet() ) {
			Object value = UIManager.get( key );
			if( value instanceof FontUIResource ) {
				UIManager.put( key, fontUIResource );
			}
		}
	}

	public static void setDefaultFont( Font font ) {
		setDefaultFontResource( new FontUIResource( font ) );
	}

	private static void scaleFontIfApplicable( UIDefaults uiDefaults, Object key, FontUIResource prevFontUIResource, double scale ) {
		int prevSize = prevFontUIResource.getSize();
		int nextSize = (int)( Math.round( prevSize * scale ) );
		if( prevSize != nextSize ) {
			FontUIResource nextFontUIResource = new FontUIResource( prevFontUIResource.getFamily(), prevFontUIResource.getStyle(), nextSize );
			uiDefaults.put( key, nextFontUIResource );
		}
	}

	private static void scaleFontIfApplicable( UIDefaults uiDefaults, Map.Entry<Object, Object> entry, double scale ) {
		Object value = entry.getValue();
		if( value instanceof UIDefaults.ActiveValue ) {
			UIDefaults.ActiveValue activeValue = (UIDefaults.ActiveValue)value;
			value = activeValue.createValue( uiDefaults );
		}
		if( value instanceof FontUIResource ) {
			scaleFontIfApplicable( uiDefaults, entry.getKey(), (FontUIResource)value, scale );
		}
	}

	public static void scaleFont( double scale ) {
		UIDefaults uiDefaults = UIManager.getLookAndFeelDefaults();
		for( Map.Entry<Object, Object> entry : uiDefaults.entrySet() ) {
			scaleFontIfApplicable( uiDefaults, entry, scale );
		}
	}

	public static void scaleFontIAppropriate() {
		double scale = getFontScale();
		if( EpsilonUtilities.isWithinReasonableEpsilon( scale, 1.0 ) ) {
			//pass
		} else {
			scaleFont( scale );
		}
	}

	public static double getFontScale() {
		return Double.parseDouble( System.getProperty( "uimanager.fontScale", "1.0" ) );
	}

	public static int getDefaultFontSize() {
		UIDefaults uiDefaults = UIManager.getDefaults();
		Object value = uiDefaults.get( "defaultFont" );
		if( value instanceof FontUIResource ) {
			FontUIResource fontUIResource = (FontUIResource)value;
			return fontUIResource.getSize();
		} else {
			//todo?
			return 12;
		}
	}

	public static boolean setLookAndFeel( String plafName ) {
		UIManager.LookAndFeelInfo lookAndFeelInfo = PlafUtilities.getInstalledLookAndFeelInfoNamed( plafName );
		if( lookAndFeelInfo != null ) {
			try {
				UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
				return true;
			} catch( Exception e ) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}
}
