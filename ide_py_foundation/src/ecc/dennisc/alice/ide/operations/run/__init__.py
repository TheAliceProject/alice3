import java
import javax

from edu.cmu.cs.dennisc import alice

class _RunIcon( javax.swing.Icon ):
	def getIconWidth(self):
		return 18
	def getIconHeight(self):
		return 18
	def paintIcon(self, c, g, x, y):
		g.setPaint( java.awt.Color.GREEN.darker() )
		w = self.getIconWidth()
		h = self.getIconHeight()
		g.fillOval( x, y, w, h )

		offset = w / 5
		x0 = x + offset * 2
		x1 = x + w - offset

		y0 = y + offset
		y1 = y + h - offset 
		yC = ( y0 + y1 ) / 2

		xs = ( x0, x1, x0 )
		ys = ( y0, yC, y1 )

		g.setPaint( java.awt.Color.WHITE )
		g.fillPolygon( xs, ys, 3 );
		

class AbstractRunOperation(alice.ide.AbstractOperation):
	def __init__(self):
		alice.ide.AbstractOperation.__init__(self)
		self.putValue( javax.swing.Action.NAME, "Run..." )
		self.putValue( javax.swing.Action.SMALL_ICON, _RunIcon() )
	def prepare(self, e, observer):
		return alice.ide.Operation.PreparationResult.PERFORM
	def perform(self):
		raise "Override"
