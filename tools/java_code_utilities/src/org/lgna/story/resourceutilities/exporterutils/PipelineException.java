package org.lgna.story.resourceutilities.exporterutils;

public class PipelineException extends Throwable {
	
	private String data;
	private String columnName;
	private int rowNumber = -1;
	
	public PipelineException(String message) {
		super(message);
		this.data = null;
	}
	
	public PipelineException(String message, String data) {
		super(message);
		this.data = data;
	}
	
	public PipelineException(String message, String data, Throwable cause) {
		super(message, cause);
		this.data = data;
	}
	
	public PipelineException(String data, Throwable cause) {
		super(cause);
		this.data = data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public void setSpreadsheetInfo(String columnName, int rowNumber) {
		this.columnName = columnName;
		this.rowNumber = rowNumber;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	
	public String getData() {
		return this.data;
	}
	
	public String getColumnName() {
		return this.columnName;
	}
	
	public int getRowNumber() {
		return this.rowNumber;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.columnName != null) {
			sb.append("COL: "+this.columnName+", ");
		}
		if (this.rowNumber != -1) {
			sb.append("ROW: "+this.rowNumber +",");
		}
		if (this.data != null) {
			sb.append("DATA: "+this.data+", ");
		}
		sb.append(super.toString());
		return sb.toString();
	}
	
	
}
