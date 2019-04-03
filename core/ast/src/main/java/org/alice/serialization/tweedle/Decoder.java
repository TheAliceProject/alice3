package org.alice.serialization.tweedle;

import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractNode;

import java.util.HashSet;
import java.util.Set;

public class Decoder {
  private final Set<AbstractDeclaration> terminalNodes;

  Decoder(Set<AbstractDeclaration> terminals) {
    terminalNodes = terminals;
  }

  Decoder() {
    terminalNodes = new HashSet<>();
  }

  public AbstractNode decode(String document) {
    // TODO
    return null;
  }

  public AbstractNode copy(String document) {
    // TODO
    return null;
  }
}
