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
package org.alice.ide.formatter;

/**
 * @author Dennis Cosgrove
 */
public class AliceFormatter extends Formatter {
	private static class SingletonHolder {
		private static AliceFormatter instance = new AliceFormatter();
	}

	public static AliceFormatter getInstance() {
		return SingletonHolder.instance;
	}

	private java.util.Map<String, String> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private AliceFormatter() {
		super( "Alice" );
		java.util.Locale locale = java.util.Locale.getDefault();
		String[] bundleNames = {
				"AliceFormatter",

				"java-lang-Math-Functions",

				"org-lnga-common-IntegerUtilities-Functions",
				"org-lnga-common-Random-Functions",

				"org-lgna-story-AnimationStyle-EnumConstants",
				"org-lgna-story-Color-PublicStaticFinalFields",
				"org-lgna-story-Functions",
				"org-lgna-story-MoveDirection-EnumConstants",
				"org-lgna-story-MultipleEventPolicy-EnumConstants",
				"org-lgna-story-PathStyle-EnumConstants",
				"org-lgna-story-Procedures",
				"org-lgna-story-Properties",
				"org-lgna-story-RollDirection-EnumConstants",
				"org-lgna-story-SetDimensionPolicy-EnumConstants",
				"org-lgna-story-SpatialRelation-EnumConstants",
				"org-lgna-story-TurnDirection-EnumConstants",

				//todo
				"org-alice-apis-moveandturn-HowMuch-EnumConstants",
				"org-alice-apis-moveandturn-Parameters",
				"edu-wustl-cse-lookingglass-apis-walkandtouch-Amount-EnumConstants",
				"edu-wustl-cse-lookingglass-apis-walkandtouch-ExitDirection-EnumConstants",
				"edu-wustl-cse-lookingglass-apis-walkandtouch-FallDirection-EnumConstants",
				"edu-wustl-cse-lookingglass-apis-walkandtouch-LookDirection-EnumConstants",
				"edu-wustl-cse-lookingglass-apis-walkandtouch-Parameters",
				"edu-wustl-cse-lookingglass-apis-walkandtouch-SitDirection-EnumConstants",
		};
		for( String bundleName : bundleNames ) {
			try {
				java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( "org.alice.ide.formatter." + bundleName, locale );
				for( java.util.Enumeration<String> e = resourceBundle.getKeys(); e.hasMoreElements(); ) {
					String key = e.nextElement();
					map.put( key, resourceBundle.getString( key ) );
				}
			} catch( java.util.MissingResourceException mre ) {
				//edu.cmu.cs.dennisc.java.util.logging.Logger.errln( bundleName );
				//pass
			}
		}
	}

	@Override
	public String getHeaderTextForCode( org.lgna.project.ast.UserCode code ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "declare " );
		if( code instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)code;
			if( method.isProcedure() ) {
				sb.append( "procedure " );
			} else {
				sb.append( "</getReturnType()/> " );
				sb.append( "function " );
			}
			sb.append( "</getName()/> " );
		} else {
			sb.append( "constructor " );
		}
		sb.append( "</getParameters()/>" );
		return sb.toString();
	}

	@Override
	public String getTrailerTextForCode( org.lgna.project.ast.UserCode code ) {
		return null;
	}

	private String getLocalizedText( String text, String rvIfNull ) {
		if( text != null ) {
			String rv = this.map.get( text );
			if( rv != null ) {
				return rv;
			} else {
				if( text.startsWith( "get" ) ) {
					rv = this.map.get( text.substring( 3 ) );
					if( rv != null ) {
						return this.getTextForGet() + rv;
					}
				}
				if( text.startsWith( "set" ) ) {
					rv = this.map.get( text.substring( 3 ) );
					if( rv != null ) {
						return this.getTextForSet() + rv;
					}
				}
				return rvIfNull;
			}
		} else {
			return rvIfNull;
		}
	}

	private String getLocalizedText( String text ) {
		return getLocalizedText( text, text );
	}

	//	public String getTextForThis() {
	//		return edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getStringFromSimpleNames( org.lgna.project.ast.ThisExpression.class, "org.lgna.project.ast.Templates", org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().getLocale() );
	//	}
	@Override
	public String getTextForThis() {
		return this.getLocalizedText( "this" );
	}

	@Override
	public String getTextForNull() {
		return this.getLocalizedText( "null" );
	}

	public String getTextForGet() {
		return this.getLocalizedText( "get" );
	}

	public String getTextForSet() {
		return this.getLocalizedText( "set" );
	}

	@Override
	protected String getTextForCls( Class<?> cls ) {
		if( cls != null ) {
			return this.getLocalizedText( cls.getName(), cls.getSimpleName() );
		} else {
			return this.getTextForNull();
		}
	}

	@Override
	protected String getTextForJavaParameter( org.lgna.project.ast.JavaParameter javaParameter ) {
		return this.getLocalizedText( javaParameter.getName() );
	}

	@Override
	protected String getTextForMethodReflectionProxy( org.lgna.project.ast.MethodReflectionProxy methodReflectionProxy ) {
		return this.getLocalizedText( methodReflectionProxy.getName() );
	}

	@Override
	public boolean isTypeExpressionDesired() {
		return false;
	}

	@Override
	protected String getNameForField( java.lang.reflect.Field fld ) {
		return this.getLocalizedText( fld.getName() );
	}

	@Override
	public String getFinalText() {
		return "constant";
	}
}
