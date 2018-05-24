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
package edu.cmu.cs.dennisc.jira.soap;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteCustomFieldValue;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemoteVersion;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import edu.cmu.cs.dennisc.issue.Attachment;
import edu.cmu.cs.dennisc.jira.JIRAReport;

import java.rmi.RemoteException;

/**
 * @author Dennis Cosgrove
 */
public class SOAPUtilities {

	private static RemoteCustomFieldValue createCustomField( int key, String value ) {
		return new RemoteCustomFieldValue( "customfield_" + key, "", new String[] { value } );
	}

	private static RemoteVersion[] getRemoteAffectsVersions( JIRAReport jiraReport, JiraSoapService service, String token, String project ) {
		String[] affectsVersions = jiraReport.getAffectsVersions();
		if( ( affectsVersions != null ) && ( affectsVersions.length > 0 ) ) {
			String affectsVersion = affectsVersions[ 0 ];
			RemoteVersion[] versions;
			try {
				versions = service.getVersions( token, project );
			} catch (RemoteException e) {
				e.printStackTrace();
				return new RemoteVersion[] {};
			}
			for( RemoteVersion version : versions ) {
				String versionName = version.getName();
				if( ( versionName != null ) && ( versionName.length() > 0 ) ) {
					if( versionName.equals( affectsVersion ) ) {
						return new RemoteVersion[] { version };
					}
				}
			}
		}
		return new RemoteVersion[] {};
	}

	private static RemoteIssue createPreparedIssue( JIRAReport jiraReport ) {
		RemoteIssue remoteIssue = new RemoteIssue();
		remoteIssue.setSummary( jiraReport.getTruncatedSummary() );
		remoteIssue.setType( Integer.toString( jiraReport.getTypeID() ) );

		String description = jiraReport.getCreditedDescription();
		remoteIssue.setDescription( description );

		RemoteCustomFieldValue steps = createCustomField( 10000, jiraReport.getSteps() );
		RemoteCustomFieldValue exception = createCustomField( 10001, jiraReport.getException() );
		remoteIssue.setCustomFieldValues( new RemoteCustomFieldValue[] { steps, exception } );

		StringBuffer environment = new StringBuffer();
		String[] affectsVersions = jiraReport.getAffectsVersions();
		if( ( affectsVersions != null ) && ( affectsVersions.length > 0 ) ) {
			environment.append( "version: " );
			environment.append( affectsVersions[ 0 ] );
			environment.append( ";\n" );
		}
		remoteIssue.setEnvironment( jiraReport.getEnvironment() );
		return remoteIssue;
	}

	public static RemoteIssue createIssue( JIRAReport jiraReport, JiraSoapService service, String token ) throws RemoteException {
		String project = jiraReport.getProjectKey();
		RemoteIssue remoteIssue = createPreparedIssue( jiraReport );
		remoteIssue.setProject( project );
		RemoteVersion[] remoteAffectsVersions = SOAPUtilities.getRemoteAffectsVersions( jiraReport, service, token, project );
		remoteIssue.setAffectsVersions( remoteAffectsVersions );
		return service.createIssue( token, remoteIssue );
	}

	public static RemoteIssue addAttachment( RemoteIssue rv, Attachment attachment, JiraSoapService service, String token ) throws RemoteException {
		String[] names = { attachment.getFileName() };
		final boolean isBase64EncodingDesired = true; // addAttachmentsToIssue is slow and uses too much memory
		if( isBase64EncodingDesired ) {
			String[] base64s = { Base64.encode( attachment.getBytes() ) };
			service.addBase64EncodedAttachmentsToIssue( token, rv.getKey(), names, base64s );
		} else {
			byte[][] data = { attachment.getBytes() };
			service.addAttachmentsToIssue( token, rv.getKey(), names, data );
		}
		return rv;
	}
}
