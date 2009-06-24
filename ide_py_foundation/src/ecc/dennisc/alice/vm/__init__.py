def getInstanceInJava( instance ):
	try:
		instance = instance.getInstanceInJava()
	except:
		pass
	return instance

def isValidIdentifier( name ):
	if len( name ) > 0:
		name0 = name[ 0 ]
		if name0.isalpha() or name0 == '_':
			for c in name:
				if c.isalnum() or c == '_':
					pass
				else:
					return False
			return True
		else:
			return False
	else:
		return False

def _getConventionalName( name, cap ):	
	rv = ""
	isAlphaEncountered = False
	for c in name:
		if c.isalnum():
			if c.isdigit():
				if isAlphaEncountered:
					pass
				else:
					rv += "_"
					rv += c
					isAlphaEncountered = True
					continue
			else:
				isAlphaEncountered = True
			if cap:
				c = c.upper()
			rv += c
			cap = c.isdigit()
		else:
			cap = True
	return rv

def getConventionalInstanceName( className ):
	return _getConventionalName( className, False )

def getConventionalClassName( className ):
	return _getConventionalName( className, True )

