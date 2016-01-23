package edu.sjsu.model;

public class Variable {
	
	private String name;
	private String dataType;
	private String initValue;
	private String accessModifier;
	
	public Variable(String name, String dataType, String initValue, String accessModifier) {
		this.name = name;
		this.dataType = dataType;
		this.initValue = initValue;
		this.accessModifier = accessModifier;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getInitValue() {
		return initValue;
	}
	
	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}

	public String getAccessModifier() {
		return accessModifier;
	}

	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}
	
	
	public String toString() {
		String str =  dataType + " " + name + " :" 
				+ initValue + " " + accessModifier;
		return str;
	}
		
}


