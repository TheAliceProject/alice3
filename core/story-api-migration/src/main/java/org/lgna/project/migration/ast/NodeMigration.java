package org.lgna.project.migration.ast;

import edu.cmu.cs.dennisc.pattern.Crawlable;
import org.lgna.project.migration.MigrationManager;

/**
 * An individual step in a CompoundMigration.
 * CompoundMigration handles version applicability and updating.
 * NodeMigrations describe the specific work to be on a node in the AST.
 */
public interface NodeMigration {
  void migrateNode(Crawlable node, MigrationManager manager);
}
