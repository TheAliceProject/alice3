package org.lgna.project.migration.ast;

import edu.cmu.cs.dennisc.pattern.Crawlable;
import org.lgna.project.ast.NamedUserType;
import org.lgna.story.resourceutilities.ResourceTypeHelper;

import java.util.Set;

/**
 * An individual step in a CompoundMigration.
 * CompoundMigration handles version applicability and updating.
 * NodeMigrations describe the specific work to be on a node in the AST.
 */
public interface NodeMigration {
  void migrateNode(Crawlable node, ResourceTypeHelper typeHelper, Set<NamedUserType> typeCache);
}
