package org.alice.ide.type;

import org.lgna.project.ast.AbstractType;

public class ExtendsTypeWithNamedType extends AbstractExtendsTypeKey {
  private final String typeName;

  public ExtendsTypeWithNamedType(AbstractType<?, ?, ?> superType, String typeName) {
    super(superType);
    assert typeName != null;
    this.typeName = typeName;
  }

  @Override
  public int hashCode() {
    int rv = super.hashCode();
    rv = (37 * rv) + this.typeName.hashCode();
    return rv;
  }

  @Override
  protected boolean contentEquals(TypeKey other) {
    // super class's equals methods ensures this.getClass() == other.getClass()
    return super.contentEquals(other) && (this.typeName.equals(((ExtendsTypeWithNamedType) other).typeName));
  }
}
