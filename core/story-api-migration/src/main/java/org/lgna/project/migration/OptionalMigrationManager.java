package org.lgna.project.migration;

import org.lgna.project.ProjectVersion;

public class OptionalMigrationManager extends AbstractMigrationManager {
  private final AstMigration[] astMigrations;

  public OptionalMigrationManager(AstMigration... astMigrations) {
    super(ProjectVersion.getCurrentVersion());
    this.astMigrations = astMigrations;
  }

  @Override
  protected TextMigration[] getTextMigrations() {
    return new TextMigration[0];
  }

  @Override
  protected AstMigration[] getAstMigrations() {
    return astMigrations;
  }
}
