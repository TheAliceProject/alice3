/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.jira;

/**
 * @author Dennis Cosgrove
 */
public class JIRAUtilities {
	private static int getType( edu.cmu.cs.dennisc.issue.Issue.Type type ) {
		if( type == edu.cmu.cs.dennisc.issue.Issue.Type.BUG ) {
			return 1;
		} else if( type == edu.cmu.cs.dennisc.issue.Issue.Type.NEW_FEAURE ) {
			return 2;
		} else if( type == edu.cmu.cs.dennisc.issue.Issue.Type.IMPROVEMENT ) {
			return 4;
		} else {
			throw new RuntimeException();
		}
	}
	private static redstone.xmlrpc.XmlRpcStruct createCustomField( int id, String value ) {
	    redstone.xmlrpc.XmlRpcStruct rv = new redstone.xmlrpc.XmlRpcStruct();
	    java.util.Vector< String > values = new java.util.Vector< String >();
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
	
	public static redstone.xmlrpc.XmlRpcStruct createIssue( edu.cmu.cs.dennisc.issue.Issue issue, String project ) {
		redstone.xmlrpc.XmlRpcStruct rv = new redstone.xmlrpc.XmlRpcStruct();
		rv.put( "project", project );
		rv.put( "type", getType( issue.getType() ) );
		rv.put( "summary", issue.getSummary() );
		rv.put( "description", issue.getDescription() );
		rv.put( "environment", issue.getEnvironment() );
	    java.util.Vector< redstone.xmlrpc.XmlRpcStruct > customFields = new java.util.Vector< redstone.xmlrpc.XmlRpcStruct >();
	    customFields.add( createCustomField( 10000, issue.getSteps() ) );
	    customFields.add( createCustomField( 10001, issue.getException() ) );
	    rv.put( "customFieldValues", customFields );
	    return rv;
	}
}
