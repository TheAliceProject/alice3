package edu.cmu.cs.dennisc.jira.rest;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Version;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import edu.cmu.cs.dennisc.issue.Attachment;
import edu.cmu.cs.dennisc.jira.JIRAReport;

public class RestUtilities {
	private static final String JIRA_USERNAME = "alice3_rest";
	private static final String JIRA_PASSWORD = "PFJyGt96)GYz(Ydb";

	private static final String STEPS_FIELD_ID = "customfield_10000";
	private static final String EXCEPTION_FIELD_ID = "customfield_10001";
	private static final String ENVIRONMENT_FIELD_ID = "environment";
	private static final ArrayList<String> EMPTY_VERSION_LIST = new ArrayList<>( 0 );

	private static final AsynchronousJiraRestClientFactory JIRA_REST_CLIENT_FACTORY = new AsynchronousJiraRestClientFactory();


	public static BasicIssue createIssue(URI jiraServer, JIRAReport jiraReport) throws IOException {
		final JiraRestClient restClient = getJiraRestClient(jiraServer);
		IssueInput issueInput = buildJiraIssueInput(jiraReport, restClient );
		try {
			return restClient.getIssueClient().createIssue( issueInput ).claim();
		} finally {
			restClient.close();
		}
	}

	public static void addAttachment( URI jiraServer, BasicIssue issue, Attachment attachment) {
		final IssueRestClient client = getJiraRestClient(jiraServer).getIssueClient();
		final Issue newIssue = client.getIssue(issue.getKey()).claim();
		byte[] bytes = attachment.getBytes();
		InputStream stream = new ByteInputStream(bytes, bytes.length);
		client.addAttachment(newIssue.getAttachmentsUri(), stream, attachment.getFileName()).claim();
	}

	private static JiraRestClient getJiraRestClient(URI jiraServer) {
		return JIRA_REST_CLIENT_FACTORY.createWithBasicHttpAuthentication(jiraServer, JIRA_USERNAME, JIRA_PASSWORD);
	}

	private static IssueInput buildJiraIssueInput( JIRAReport jiraReport, JiraRestClient restClient ) {
		return new IssueInputBuilder().setProjectKey( jiraReport.getProjectKey() )
						.setIssueTypeId( (long) jiraReport.getTypeID() )
						.setSummary( jiraReport.getTruncatedSummary() )
						.setDescription( jiraReport.getCreditedDescription() )
						.setAffectedVersionsNames( getRemoteVersionMatch( jiraReport, restClient ) )
						.setFieldValue( ENVIRONMENT_FIELD_ID, jiraReport.getEnvironment() )
						.setFieldValue( EXCEPTION_FIELD_ID, jiraReport.getException() )
						.setFieldValue( STEPS_FIELD_ID, jiraReport.getSteps() )
						.build();
	}

	private static List<String> getRemoteVersionMatch( JIRAReport jiraReport, JiraRestClient restClient ) {
		String affectsVersion = jiraReport.getAffectsVersionText();
		Iterable<Version> versions = restClient.getProjectClient().getProject( jiraReport.getProjectKey()).claim().getVersions();

		for( Version version : versions) {
			if( affectsVersion.equals( version.getName() ) ) {
				return Collections.singletonList( affectsVersion );
			}
		}
		return EMPTY_VERSION_LIST;
	}
}
