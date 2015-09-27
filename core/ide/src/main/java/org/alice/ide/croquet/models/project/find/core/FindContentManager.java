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
package org.alice.ide.croquet.models.project.find.core;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.alice.ide.croquet.models.project.find.core.astcrawler.FindCrawler;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.ast.LocalDeclarationStatement;
import org.lgna.project.ast.NodeListProperty;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserParameter;
import org.lgna.project.ast.UserType;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.pattern.Criterion;

/**
 * @author Matt May
 */
public class FindContentManager {

	private final List<SearchResult> objectList = Lists.newArrayList();
	private final List<Object> superTypeList = Lists.newArrayList();

	public void initialize( UserType sceneType, List<Criterion> criteria ) {
		//		this.sceneType = sceneType;
		tunnelField( sceneType );
		FindCrawler crawler = new FindCrawler( criteria, objectList );
		for( SearchResult object : objectList ) {
			if( object.getDeclaration() instanceof UserMethod ) {
				UserMethod method = (UserMethod)object.getDeclaration();
				method.crawl( crawler, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY );
			}
		}
	}

	private void tunnelField( UserType<?> type ) {
		tunnelSuper( (AbstractType)type.superType.getValue() );
		for( UserField field : type.fields ) {
			if( !checkContains( field ) ) {
				objectList.add( new SearchResult( field ) );
				if( field.getValueType() instanceof UserType ) {
					tunnelField( (UserType)field.getValueType() );
				}
			}
		}
		for( UserMethod method : type.methods ) {
			if( !checkContains( method ) ) {
				objectList.add( new SearchResult( method ) );
				tunnelMethod( method );
			}
		}
	}

	private void tunnelSuper( AbstractType parent ) {
		if( ( parent != null ) && !superTypeList.contains( parent ) ) {
			//pass
		} else {
			return;
		}
		superTypeList.add( parent );
		List<AbstractField> fields = parent.getDeclaredFields();
		List<AbstractMethod> methods = parent.getDeclaredMethods();
		for( AbstractField field : fields ) {
			if( !checkContains( field ) ) {
				objectList.add( new SearchResult( field ) );
			}
		}
		for( AbstractMethod method : methods ) {
			if( !checkContains( method ) ) {
				objectList.add( new SearchResult( method ) );
			}
		}
		if( parent != null ) {
			AbstractType grandparent = parent.getSuperType();
			if( ( grandparent != null ) && grandparent.isFollowToSuperClassDesired() ) {
				tunnelSuper( grandparent );
			}
		}
	}

	private void tunnelMethod( UserMethod method ) {
		NodeListProperty<UserParameter> nodeListProperty = method.requiredParameters;
		BlockStatement blockStatement = method.body.getValue();
		for( UserParameter parameter : nodeListProperty ) {
			assert !checkContains( parameter );
			objectList.add( new SearchResult( parameter ) );
		}
		for( Statement statement : blockStatement.statements ) {
			if( statement instanceof LocalDeclarationStatement ) {
				UserLocal local = ( (LocalDeclarationStatement)statement ).local.getValue();
				assert !checkContains( local );
				objectList.add( new SearchResult( local ) );
			}
		}
	}

	private boolean checkContains( Object searchObject ) {
		return checkFind( searchObject ) != null;
	}

	private SearchResult checkFind( Object searchObject ) {
		for( SearchResult object : objectList ) {
			if( object.getDeclaration().equals( searchObject ) ) {
				return object;
			}
		}
		return null;
	}

	public List<SearchResult> getResultsForString( String nextValue ) {
		List<SearchResult> rv = Lists.newArrayList();
		String check = nextValue;
		//all these characters break regex
		check = check.replaceAll( "\\*", ".*" );
		check = check.replaceAll( "\\(", "" );
		check = check.replaceAll( "\\)", "" );
		check = check.replaceAll( "\\[", "" );
		check = check.replaceAll( "\\]", "" );
		check = check.replaceAll( "\\{", "" );
		check = check.replaceAll( "\\}", "" );
		check = check.replaceAll( "\\\\", "" );
		//		if( check.length() == 0 ) {
		//			return rv;
		//		}
		while( true ) {
			try {
				Pattern pattern = Pattern.compile( check.toLowerCase() );
				for( SearchResult o : objectList ) {
					String name = o.getName();
					Matcher matcher = pattern.matcher( name.toLowerCase() );
					if( matcher.find() ) {
						if( o.getReferences().size() > 0 ) {
							rv.add( o );
						}
					}
				}
				break;
			} catch( PatternSyntaxException pse ) {
				throw pse;
			}
		}
		rv = sortByRelevance( nextValue, rv );
		return rv;
	}

	public List<SearchResult> getResultsForField( UserField field ) {
		List<SearchResult> resultsForString = getResultsForString( field.getName() );
		List<SearchResult> rv = Lists.newArrayList();
		for( SearchResult obj : resultsForString ) {
			if( obj.getDeclaration().equals( field ) ) {
				rv.add( obj );
			}
		}
		if( rv.size() < 1 ) {
			return resultsForString;
		} else {
			return rv;
		}
	}

	private List<SearchResult> sortByRelevance( String string, List<SearchResult> searchResults ) {
		List<SearchResult> unsortedList = Lists.newArrayList( searchResults );
		List<SearchResult> rv = Lists.newArrayList();
		Map<SearchResult, Double> scoreMap = Maps.newHashMap();
		for( SearchResult obj : unsortedList ) {
			scoreMap.put( obj, score( obj, string ) );
		}
		//n^2 sort O:-)
		while( !unsortedList.isEmpty() ) {
			double max = Double.NEGATIVE_INFINITY;
			SearchResult maxObj = null;
			for( SearchResult o : unsortedList ) {
				if( scoreMap.get( o ) > max ) {
					max = scoreMap.get( o );
					maxObj = o;
				}
			}
			assert maxObj != null : unsortedList.size();
			rv.add( unsortedList.remove( unsortedList.indexOf( maxObj ) ) );
		}
		return rv;
	}

	private Double score( SearchResult obj, String string ) {
		double rv = 0;
		if( obj.getName().equals( string ) ) {
			rv += 2;
		}
		if( obj.getName().contains( string ) ) {
			rv += 1;
		}
		if( obj.getName().toLowerCase().startsWith( string.toLowerCase() ) ) {
			rv += 1;
		}
		rv += obj.getReferences().size() / 10.0;
		return rv;
	}

	public void refresh( UserType sceneType, List<Criterion> criteria ) {
		objectList.clear();
		superTypeList.clear();
		initialize( sceneType, criteria );
	}

	//	public boolean isInitialized() {
	//		return sceneType != null;
	//	}
}
