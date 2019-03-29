package org.alice.serialization.tweedle;

import org.alice.serialization.EncoderDecoder;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractNode;
import org.lgna.project.code.CodeAppender;

import java.util.Set;

public class TweedleEncoderDecoder implements EncoderDecoder<String> {

	@Override public <N extends AbstractNode & CodeAppender> String encode( N node ) {
		return new Encoder().encode( node );
	}

	@Override public <N extends AbstractNode & CodeAppender> String encode( N node, Set<AbstractDeclaration> terminals ) {
		return new Encoder(terminals).encode( node );
	}

	@Override public AbstractNode decode( String document ) throws VersionNotSupportedException {
		return new Decoder().decode( document );
	}

	@Override public AbstractNode copy( String document, Set<AbstractDeclaration> terminals )
					throws VersionNotSupportedException {
		return new Decoder( terminals ).copy( document );
	}
}
