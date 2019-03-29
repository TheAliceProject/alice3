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

/**
 * @author Dennis Cosgrove
 */
public class JiraSearch {
	private static final boolean containsProjectAttachment( com.atlassian.jira.rpc.soap.client.RemoteIssue issue ) {
		return edu.cmu.cs.dennisc.java.util.Collections.newArrayList( issue.getAttachmentNames() ).contains( "currentProject.a3p" );
	}
	private static final String getVersionsText( com.atlassian.jira.rpc.soap.client.RemoteIssue issue ) {
		com.atlassian.jira.rpc.soap.client.RemoteVersion[] versions = issue.getAffectsVersions();
		StringBuilder sb = new StringBuilder();
		for( com.atlassian.jira.rpc.soap.client.RemoteVersion version : versions ) {
			sb.append( version.getName() );
		}
		return sb.toString();
	}
	public static void main(String[] args) throws Exception {
		org.alice.ide.issue.ReportSubmissionConfiguration reportSubmissionConfiguration = new org.alice.ide.issue.ReportSubmissionConfiguration();
		com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator jiraSoapServiceLocator = new com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator();
		com.atlassian.jira.rpc.soap.client.JiraSoapService service = jiraSoapServiceLocator.getJirasoapserviceV2( reportSubmissionConfiguration.getJIRAViaSOAPServer() );
		String token = service.login( args[ 0 ], args[ 1 ] );
		String filterId = "10080";
		com.atlassian.jira.rpc.soap.client.RemoteIssue[] issues = service.getIssuesFromFilter( token, filterId );
		for( com.atlassian.jira.rpc.soap.client.RemoteIssue issue : issues ) {
			if( containsProjectAttachment( issue ) ) {
				java.util.Calendar calendar = issue.getCreated();
				if( calendar.getTime().getDate() == 17 ) {
					StringBuilder sb = new StringBuilder();
					sb.append( issue.getId() );
					sb.append( " " );
					//sb.append( getVersionsText( issue ) );
					//sb.append( " " );
					//sb.append( issue.getSummary() );
					
					sb.append( calendar.getTime().getDate() );
					sb.append( " " );
					
					com.atlassian.jira.rpc.soap.client.RemoteAttachment[] attachments = service.getAttachmentsFromIssue( token, issue.getKey() );
					for( com.atlassian.jira.rpc.soap.client.RemoteAttachment attachment : attachments ) {
						if( "systemProperties.xml".contentEquals( attachment.getFilename() ) ) {
							StringBuilder sbUrl = new StringBuilder();
							sbUrl.append( "http://bugs.alice.org:8080/secure/attachment/" );
							sbUrl.append( attachment.getId() );
							sbUrl.append( "/systemProperties.xml" );
							
							sb.append( sbUrl );
							//edu.cmu.cs.dennisc.browser.BrowserUtilities.browse( sbUrl.toString() );
						}
					}
					System.out.println( sb.toString() );
				}
			}
		}
	}
}
