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
	private java.util.Map<String, String> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
	private AliceFormatter() {
		super( new java.util.Locale( "en", "US", "alice" ), "Alice" );
		java.util.Locale locale = java.util.Locale.getDefault(); 
		String[] bundleNames = { "AliceFormatter", "java_lang_Functions", "org_alice_integer_Functions", "org_alice_random_Functions", "org_alice_apis_moveandturn_Procedures", "org_alice_apis_moveandturn_Functions", "org_alice_apis_moveandturn_Fields", "org_alice_apis_moveandturn_Parameters", "edu_wustl_cse_lookingglass_apis_walkandtouch_Procedures", "edu_wustl_cse_lookingglass_apis_walkandtouch_Parameters" };
		for( String bundleName : bundleNames ) {
			try {
				java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( "org.alice.ide.formatter." + bundleName, locale );
				for( java.util.Enumeration<String> e=resourceBundle.getKeys(); e.hasMoreElements();  ) {
					String key = e.nextElement();
					map.put( key, resourceBundle.getString( key ) );
				}
			} catch( java.util.MissingResourceException mre ) {
				//pass
			}
		}
	}
	
	private String getLocalizedText( String text, String rvIfNull ) {
		String rv = this.map.get( text );
		if( rv != null ) {
			return rv;
		} else {
			return rvIfNull;
		}
	}
	private String getLocalizedText( String text ) {
		return getLocalizedText( text, text );
	}
//	public String getTextForThis() {
//		return edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getStringFromSimpleNames( edu.cmu.cs.dennisc.alice.ast.ThisExpression.class, "edu.cmu.cs.dennisc.alice.ast.Templates", org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().getLocale() );
//	}
	@Override
	public String getTextForThis() {
		return this.getLocalizedText( "this" );
	}
	@Override
	public String getTextForNull() {
		return this.getLocalizedText( "null" );
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
	protected String getTextForParameterDeclaredInJava( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInJava parameterInJava ) {
		return this.getLocalizedText( parameterInJava.getName() );
	}
	@Override
	protected String getTextForMethodReflectionProxy( edu.cmu.cs.dennisc.alice.ast.MethodReflectionProxy methodReflectionProxy ) {
		return this.getLocalizedText( methodReflectionProxy.getName() );
	}
	
	@Override
	public boolean isTypeExpressionDesired() {
		return false;
	}
}
