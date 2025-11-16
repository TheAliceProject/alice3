package edu.cmu.cs.dennisc.jira.rest;

import edu.cmu.cs.dennisc.jira.JIRAReport;
import net.rcarz.jiraclient.*;

import java.net.URI;
import java.util.Collections;

public class RestUtilities {
  // TODO Align this account with our server so it works and move the values out the code base.
  private static final String JIRA_USERNAME = "alice3_rest";
  private static final String JIRA_PASSWORD = "PFJyGt96)GYz(Ydb";

  private static final String STEPS_FIELD_ID = "customfield_10000";
  private static final String EXCEPTION_FIELD_ID = "customfield_10001";
  private static final String ENVIRONMENT_FIELD_ID = "environment";

  public static Issue createIssue(URI jiraServer, JIRAReport jiraReport) {
    BasicCredentials creds = new BasicCredentials(JIRA_USERNAME, JIRA_PASSWORD);
    JiraClient jira = new JiraClient(jiraServer.toString(), creds);

    try {
      Version ver = Version.get(jira.getRestClient(), jiraReport.getAffectsVersionText());
      return jira.createIssue(jiraReport.getProjectKey(), jiraReport.getType().toString())
          .field(Field.SUMMARY, jiraReport.getTruncatedSummary())
          .field(Field.DESCRIPTION, jiraReport.getCreditedDescription())
          .field(Field.VERSIONS, Collections.singletonList(ver))
          .field(ENVIRONMENT_FIELD_ID, jiraReport.getEnvironment())
          .field(EXCEPTION_FIELD_ID, jiraReport.getException())
          .field(STEPS_FIELD_ID, jiraReport.getSteps())
          .execute();
    } catch (JiraException e) {
      throw new RuntimeException(e);
    }
  }
}
