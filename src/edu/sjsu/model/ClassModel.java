package edu.sjsu.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClassModel {
	
	private String className;
	private String extendz;
	private boolean isInterface = false;
	private Collection<String> interfaces = new ArrayList<String>();
	private Map<String, Variable> varMap = new HashMap<String, Variable>();
	private Map<String, Method> methodMap = new HashMap<String, Method>();
		
	public ClassModel(String className) {
		this.className = className;
	}
	
	public String getName() {
		return className;
	}
	
	public void setName(String className) {
		this.className = className;
	}
	
	public Collection<Method> getMethods() {
		return methodMap.values();
	}
	
	public void setMethods(Map<String, Method> methods) {
		methodMap = methods;
	}
	
	public void addMethod(String mName, Method method) {
		methodMap.put(mName, method);
	}
	
	public void addInterface(String name) {
		interfaces.add(name);
	}
	
	public void setExtendz(String name) {
		extendz = name;
	}

	public boolean isInterface() {
		return isInterface;
	}
	
	public void setAsInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}
	
	public String getExtendz() {
		return extendz;
	}
	public Collection<String> getInterfaces() {
		return interfaces;
	}
	
	public void addVariable(String vName, Variable variable) {
		varMap.put(vName, variable);
	}
	
	public Collection<Variable> getVariables() {
		return varMap.values();
	}
	
	public void setVariables(Map<String, Variable> variables) {
		varMap = variables;
	}

}
