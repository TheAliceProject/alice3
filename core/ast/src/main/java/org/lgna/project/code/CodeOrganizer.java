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
package org.lgna.project.code;

import org.lgna.project.ast.Getter;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.Setter;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;

/**
 * @author dculyba
 */
public class CodeOrganizer {

	public static class CodeOrganizerDefinition {
		private final java.util.LinkedHashMap<String, String[]> codeSections = new java.util.LinkedHashMap<String, String[]>();
		private final java.util.HashMap<String, Boolean> shouldCollapseSection = new java.util.HashMap<String, Boolean>();

		public CodeOrganizerDefinition() {
		}

		public void addSection( String sectionKey, String... itemKeys ) {
			addSection( sectionKey, false, itemKeys );
		}

		public void addSection( String sectionKey, Boolean shouldCollapse, String... itemKeys ) {
			codeSections.put( sectionKey, itemKeys );
			shouldCollapseSection.put( sectionKey, shouldCollapse );
		}
	}

	private final java.util.LinkedHashMap<String, String[]> codeSections;
	private final java.util.HashMap<String, Boolean> shouldCollapseSection;
	private final java.util.Map<String, java.util.List<CodeAppender>> itemLists = new java.util.HashMap<String, java.util.List<CodeAppender>>();

	public static final String ALL_METHODS = "ALL_METHODS";
	public static final String NON_STATIC_METHODS = "NON_STATIC_METHODS";
	public static final String STATIC_METHODS = "STATIC_METHODS";
	public static final String FIELDS = "FIELDS";
	public static final String GETTERS_AND_SETTERS = "GETTERS_AND_SETTERS";
	public static final String GETTERS = "GETTERS";
	public static final String SETTERS = "SETTERS";
	public static final String CONSTRUCTORS = "CONSTRUCTORS";

	private static final String DEFAULT = "DEFAULT";

	public CodeOrganizer( CodeOrganizerDefinition codeOrganizerDefinition ) {
		codeSections = codeOrganizerDefinition.codeSections;
		shouldCollapseSection = codeOrganizerDefinition.shouldCollapseSection;
	}

	private boolean hasItemKey( String itemKey ) {
		for( java.util.Map.Entry<String, String[]> entry : codeSections.entrySet() ) {
			for( String item : entry.getValue() ) {
				if( itemKey.equalsIgnoreCase( item ) ) {
					return true;
				}
			}
		}
		return false;
	}

	private void addItem( CodeAppender toAdd, String... keyOptions ) {
		//Use the list of keyOptions to search the codeSections map to see where to put this item
		//The keyOptions list is a prioritized list, so the first match we find is where we put the item
		for( String itemKeyOption : keyOptions ) {
			if( hasItemKey( itemKeyOption ) ) {
				if( !itemLists.containsKey( itemKeyOption ) ) {
					itemLists.put( itemKeyOption, new java.util.LinkedList<CodeAppender>() );
				}
				itemLists.get( itemKeyOption ).add( toAdd );
				return;
			}
		}
		//Put the item in the DEFAULT list if it doesn't match a list
		if( !itemLists.containsKey( DEFAULT ) ) {
			itemLists.put( DEFAULT, new java.util.LinkedList<CodeAppender>() );
		}
		itemLists.get( DEFAULT ).add( toAdd );
	}

	public void addConstructor( NamedUserConstructor constructor ) {
		addItem( constructor, constructor.getName(), CONSTRUCTORS );
	}

	public void addStaticMethod( UserMethod method ) {
		addItem( method, method.getName(), STATIC_METHODS, ALL_METHODS );
	}

	public void addNonStaticMethod( UserMethod method ) {
		addItem( method, method.getName(), NON_STATIC_METHODS, ALL_METHODS );
	}

	public void addGetter( Getter getter ) {
		addItem( getter, getter.getName(), GETTERS, GETTERS_AND_SETTERS );
	}

	public void addSetter( Setter setter ) {
		addItem( setter, setter.getName(), SETTERS, GETTERS_AND_SETTERS );
	}

	public void addField( UserField field ) {
		addItem( field, field.getName(), FIELDS );
	}

	public boolean shouldCollapseSection( String sectionKey ) {
		if( this.shouldCollapseSection.containsKey( sectionKey ) ) {
			return this.shouldCollapseSection.get( sectionKey );
		}
		return false;
	}

	public java.util.LinkedHashMap<String, java.util.List<CodeAppender>> getOrderedSections() {
		java.util.LinkedHashMap<String, java.util.List<CodeAppender>> orderedMap = new java.util.LinkedHashMap<String, java.util.List<CodeAppender>>();
		for( java.util.Map.Entry<String, String[]> entry : codeSections.entrySet() ) {
			java.util.List<CodeAppender> orderedAppenders = new java.util.LinkedList<CodeAppender>();
			for( String itemKey : entry.getValue() ) {
				if( itemLists.containsKey( itemKey ) ) {
					orderedAppenders.addAll( itemLists.get( itemKey ) );
				}
			}
			orderedMap.put( entry.getKey(), orderedAppenders );
		}
		if( itemLists.containsKey( DEFAULT ) && !itemLists.get( DEFAULT ).isEmpty() ) {
			orderedMap.put( DEFAULT, itemLists.get( DEFAULT ) );
		}
		return orderedMap;
	}

}
