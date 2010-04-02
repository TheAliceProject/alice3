import java
import javax

from edu.cmu.cs.dennisc import alice

class SelectFieldOperation( alice.ide.AbstractOperation ):
	def __init__( self, field ):
		alice.ide.AbstractOperation.__init__( self )
		self._field = field
		self.putValue( javax.swing.Action.NAME, self._field.getName() )
	def prepare(self, e, observer):
		return alice.ide.Operation.PreparationResult.PERFORM
	def perform(self):
		self.getIDE().setFieldSelection( self._field )
