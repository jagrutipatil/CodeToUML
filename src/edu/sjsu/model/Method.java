package edu.sjsu.model;

import java.util.HashMap;
import java.util.Map;

public class Method {
	private String accessModifier;
	private String returnType;
	private String name;
	private Map<String, Variable> parameters = new HashMap<String, Variable>();		
	private Map<String, Variable> localvar = new HashMap<String, Variable>();
	
	public Method(String name, String returnType, String accessModifier) {
		this.name = name;
		this.returnType = returnType;
		this.accessModifier = accessModifier;
	}
	
	public String getAccessModifier() {
		return accessModifier;
	}
	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void addParameter(Variable param) {
		parameters.put(param.getName(), param);
	}

	public Map<String, Variable> getParameters() {
		return parameters;
	}

	public void addLocalVar(Variable param) {
		localvar.put(param.getName(), param);
	}

	public Map<String, Variable> getLocalvar() {
		return localvar;
	}

	
	public String toString() {
		String str = "\n" + accessModifier + " " + returnType + " " + name + " :"; 
		for (String pname : parameters.keySet()) {
			Variable param = parameters.get(pname);
			str = str + param.toString();
		}		
		return str;
	}
	
}
