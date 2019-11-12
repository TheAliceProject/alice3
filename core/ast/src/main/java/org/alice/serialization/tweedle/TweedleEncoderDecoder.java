package org.alice.serialization.tweedle;

import org.alice.serialization.EncoderDecoder;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractNode;
import org.lgna.project.code.ProcessableNode;

import java.util.Set;

public class TweedleEncoderDecoder implements EncoderDecoder<String> {

  @Override
  public <N extends AbstractNode & ProcessableNode> String encode(N node) {
    return encodeProcessable(node);
  }

  public <N extends ProcessableNode> String encodeProcessable(N node) {
    return new Encoder().encode(node);
  }

  @Override
  public <N extends AbstractNode & ProcessableNode> String encode(N node, Set<AbstractDeclaration> terminals) {
    return new Encoder(terminals).encode(node);
  }

  @Override
  public AbstractNode decode(String document) throws VersionNotSupportedException {
    return new Decoder().decode(document);
  }

  @Override
  public AbstractNode copy(String document, Set<AbstractDeclaration> terminals) throws VersionNotSupportedException {
    return new Decoder(terminals).copy(document);
  }
}
