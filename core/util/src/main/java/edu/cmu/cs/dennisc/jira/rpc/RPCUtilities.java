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

import edu.cmu.cs.dennisc.java.lang.SystemProperty;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.jira.JIRAReport;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcFault;
import redstone.xmlrpc.XmlRpcStruct;

import java.util.List;
import java.util.Vector;

/**
 * @author Dennis Cosgrove
 */
public class RPCUtilities {
	private static XmlRpcStruct createCustomField( int id, String value ) {
		XmlRpcStruct rv = new XmlRpcStruct();
		Vector<String> values = new Vector<String>();
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
	public static Object logIn( XmlRpcClient client, String id, String password ) throws XmlRpcFault {
		return client.invoke( "jira1.login", new Object[] { id, password } );
	}

	public static XmlRpcStruct createIssue( JIRAReport jiraReport, XmlRpcClient client, Object token ) throws XmlRpcFault {
		String project = jiraReport.getProjectKey();
		XmlRpcStruct rv = new XmlRpcStruct();
		rv.put( "project", project );
		rv.put( "type", jiraReport.getTypeID() );
		rv.put( "summary", jiraReport.getTruncatedSummary() );

		StringBuilder sb = new StringBuilder();
		sb.append( jiraReport.getDescription() );
		sb.append( "\n\nsystem properties:\n" );
		List<SystemProperty> propertyList = SystemUtilities.getSortedPropertyList();
		for( SystemProperty property : propertyList ) {
			sb.append( property.getKey() );
			sb.append( ": " );
			sb.append( property.getValue() );
			sb.append( "\n" );
		}

		rv.put( "description", sb.toString() );

		String[] affectsVersions = jiraReport.getAffectsVersions();
		if( ( affectsVersions != null ) && ( affectsVersions.length > 0 ) ) {
			String affectsVersion = affectsVersions[ 0 ];

			List<XmlRpcStruct> versions = (List<XmlRpcStruct>)client.invoke( "jira1.getVersions", new Object[] { token, project } );
			//System.out.println( "versions: " );
			for( XmlRpcStruct version : versions ) {
				//System.out.println( "\t" + version );
				if( affectsVersion.equals( version.get( "name" ) ) ) {
					Vector<XmlRpcStruct> remoteAffectsVersions = new Vector<XmlRpcStruct>();
					XmlRpcStruct remoteAffectsVersion = new XmlRpcStruct();
					remoteAffectsVersion.put( "id", version.get( "id" ) );
					remoteAffectsVersions.add( remoteAffectsVersion );
					rv.put( "affectsVersions", remoteAffectsVersions );
					break;
				}
			}
		}
		rv.put( "environment", jiraReport.getEnvironment() );
		Vector<XmlRpcStruct> customFields = new Vector<XmlRpcStruct>();
		customFields.add( createCustomField( 10000, jiraReport.getSteps() ) );
		customFields.add( createCustomField( 10001, jiraReport.getException() ) );
		rv.put( "customFieldValues", customFields );
		return (XmlRpcStruct)client.invoke( "jira1.createIssue", new Object[] { token, rv } );
	}

	public static String getKey( XmlRpcStruct issue ) {
		return issue.getString( "key" );
	}
}
