package org.alice.serialization;

import org.lgna.project.ast.NamedUserType;

public interface DispatchingEncoder {
	void encodeNamedUserType( NamedUserType userType);
}
