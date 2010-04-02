from edu.cmu.cs.dennisc import alice

from AbstractStatementTemplatePane import AbstractStatementTemplatePane

class AbstractStatementWithBodyTemplatePane( AbstractStatementTemplatePane ):
	def _createActualStatementArgs( self ):
		return ( alice.ast.BlockStatement( [] ), )
