package org.alice.serialization.xml;

import org.alice.serialization.EncoderDecoder;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractNode;
import org.lgna.project.code.ProcessableNode;
import org.w3c.dom.Document;

import java.util.Set;

public class XmlEncoderDecoder implements EncoderDecoder<Document> {
  @Override
  public <N extends AbstractNode & ProcessableNode> Document encode(N node) {
    return Encoder.encode(node);
  }

  @Override
  public <N extends AbstractNode & ProcessableNode> Document encode(N node, Set<AbstractDeclaration> terminals) {
    return Encoder.encode(node, terminals);
  }

  @Override
  public AbstractNode decode(Document document) throws VersionNotSupportedException {
    return Decoder.decode(document);
  }

  @Override
  public AbstractNode copy(Document document, Set<AbstractDeclaration> terminals) throws VersionNotSupportedException {
    return Decoder.copy(document, terminals);
  }
}
