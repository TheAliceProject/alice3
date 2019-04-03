package spreadsheet;

class TableModel extends javax.swing.table.AbstractTableModel {
  @Override
  public String getColumnName(int columnIndex) {
    switch (columnIndex) {
    case 0:
      return "n";
    case 1:
      return "Fibonacci";
    default:
      throw new AssertionError();
    }
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return Integer.class;
  }

  @Override
  public int getColumnCount() {
    return 2;
  }

  @Override
  public int getRowCount() {
    return 32;
  }

  private static int fibonacci(int n) {
    assert n >= 0;
    switch (n) {
    case 0:
      return 0;
    case 1:
      return 1;
    default:
      return fibonacci(n - 1) + fibonacci(n - 2);
    }
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex) {
    case 0:
      return rowIndex;
    case 1:
      return fibonacci(rowIndex);
    default:
      throw new AssertionError();
    }
  }
}

public class SpreadsheetApplication extends edu.cmu.cs.dennisc.croquet.Application {
  @Override
  protected edu.cmu.cs.dennisc.croquet.Component<?> createContentPane() {
    javax.swing.JTable table = new javax.swing.JTable(new TableModel());
    return new edu.cmu.cs.dennisc.croquet.ScrollPane(new edu.cmu.cs.dennisc.croquet.SwingAdapter(table));
  }

  @Override
  public edu.cmu.cs.dennisc.croquet.DropReceptor getDropReceptor(edu.cmu.cs.dennisc.croquet.DropSite dropSite) {
    return null;
  }

  @Override
  protected void handleWindowOpened(java.awt.event.WindowEvent e) {
  }

  @Override
  protected void handleAbout(java.util.EventObject e) {
  }

  @Override
  protected void handlePreferences(java.util.EventObject e) {
  }

  @Override
  protected void handleQuit(java.util.EventObject e) {
    System.exit(0);
  }

  public static void main(String[] args) {
    SpreadsheetApplication spreadsheet = new SpreadsheetApplication();
    spreadsheet.initialize(args);
    spreadsheet.getFrame().pack();
    spreadsheet.getFrame().setVisible(true);
  }
}
