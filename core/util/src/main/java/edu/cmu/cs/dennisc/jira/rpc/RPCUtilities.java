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
package edu.cmu.cs.dennisc.jira.rpc;

import redstone.xmlrpc.XmlRpcStruct;

/**
 * @author Dennis Cosgrove
 */
public class RPCUtilities {
	private static redstone.xmlrpc.XmlRpcStruct createCustomField( int id, String value ) {
		redstone.xmlrpc.XmlRpcStruct rv = new redstone.xmlrpc.XmlRpcStruct();
		java.util.Vector<String> values = new java.util.Vector<String>();
		values.add( value );
		rv.put( "customfieldId", "customfield_" + id );
		rv.put( "values", values );
		return rv;
	}

	//	private static redstone.xmlrpc.XmlRpcStruct createCustomField( int id, byte[]... binaries ) {
	//	    redstone.xmlrpc.XmlRpcStruct rv = new redstone.xmlrpc.XmlRpcStruct();
	//	    java.util.Vector< byte[] > values = new java.util.Vector< byte[] >();
	//	    for( byte[] binary : binaries ) {
	//		    values.add( binary );
	//	    }
	//	    rv.put( "customfieldId", "customfield_" + id );
	//	    rv.put( "values", values );
	//	    return rv;
	//	}
	public static Object logIn( redstone.xmlrpc.XmlRpcClient client, String id, String password ) throws redstone.xmlrpc.XmlRpcFault {
		return client.invoke( "jira1.login", new Object[] { id, password } );
	}

	public static redstone.xmlrpc.XmlRpcStruct createIssue( edu.cmu.cs.dennisc.jira.JIRAReport jiraReport, redstone.xmlrpc.XmlRpcClient client, Object token ) throws redstone.xmlrpc.XmlRpcFault {
		String project = jiraReport.getProjectKey();
		redstone.xmlrpc.XmlRpcStruct rv = new redstone.xmlrpc.XmlRpcStruct();
		rv.put( "project", project );
		rv.put( "type", jiraReport.getTypeID() );
		rv.put( "summary", jiraReport.getTruncatedSummary() );

		StringBuilder sb = new StringBuilder();
		sb.append( jiraReport.getDescription() );
		sb.append( "\n\nsystem properties:\n" );
		java.util.List<edu.cmu.cs.dennisc.java.lang.SystemProperty> propertyList = edu.cmu.cs.dennisc.java.lang.SystemUtilities.getSortedPropertyList();
		for( edu.cmu.cs.dennisc.java.lang.SystemProperty property : propertyList ) {
			sb.append( property.getKey() );
			sb.append( ": " );
			sb.append( property.getValue() );
			sb.append( "\n" );
		}

		rv.put( "description", sb.toString() );

		String[] affectsVersions = jiraReport.getAffectsVersions();
		if( ( affectsVersions != null ) && ( affectsVersions.length > 0 ) ) {
			String affectsVersion = affectsVersions[ 0 ];

			java.util.List<redstone.xmlrpc.XmlRpcStruct> versions = (java.util.List<redstone.xmlrpc.XmlRpcStruct>)client.invoke( "jira1.getVersions", new Object[] { token, project } );
			//System.out.println( "versions: " );
			for( redstone.xmlrpc.XmlRpcStruct version : versions ) {
				//System.out.println( "\t" + version );
				if( affectsVersion.equals( version.get( "name" ) ) ) {
					java.util.Vector<redstone.xmlrpc.XmlRpcStruct> remoteAffectsVersions = new java.util.Vector<redstone.xmlrpc.XmlRpcStruct>();
					redstone.xmlrpc.XmlRpcStruct remoteAffectsVersion = new redstone.xmlrpc.XmlRpcStruct();
					remoteAffectsVersion.put( "id", version.get( "id" ) );
					remoteAffectsVersions.add( remoteAffectsVersion );
					rv.put( "affectsVersions", remoteAffectsVersions );
					break;
				}
			}
		}
		rv.put( "environment", jiraReport.getEnvironment() );
		java.util.Vector<redstone.xmlrpc.XmlRpcStruct> customFields = new java.util.Vector<redstone.xmlrpc.XmlRpcStruct>();
		customFields.add( createCustomField( 10000, jiraReport.getSteps() ) );
		customFields.add( createCustomField( 10001, jiraReport.getException() ) );
		rv.put( "customFieldValues", customFields );
		return (redstone.xmlrpc.XmlRpcStruct)client.invoke( "jira1.createIssue", new Object[] { token, rv } );
	}

	public static String getKey( XmlRpcStruct issue ) {
		return issue.getString( "key" );
	}
}
