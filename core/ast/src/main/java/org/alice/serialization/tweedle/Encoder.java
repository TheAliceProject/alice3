package org.alice.serialization.tweedle;

import org.alice.serialization.DispatchingEncoder;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractNode;
import org.lgna.project.ast.NamedUserType;

import java.util.HashSet;
import java.util.Set;

public class Encoder implements DispatchingEncoder {

	private final StringBuilder doc = new StringBuilder();
	private final Set<AbstractDeclaration> terminalNodes;

	Encoder( Set<AbstractDeclaration> terminals ) {
		terminalNodes = terminals ;
	}

	Encoder() {
		terminalNodes = new HashSet<>();
	}

	public String encode( AbstractNode node ) {
		node.encode( this );
		return doc.toString();
	}

	@Override
	public void encodeNamedUserType( NamedUserType userType ) {
		doc.append( "class " ).append( userType.getName() )
			 .append( " extends " ).append( userType.getSuperType().getName() )

	 		 // TODO Only show for models and replace with resource identifier
		   .append( " models " ).append( userType.getName() )
			 .append( " {\n" )

				// TODO add methods and fields
			 .append( "  // fields\n" )
			 .append( "  // methods\n" )
			 .append( "\n}" );
	}
}
