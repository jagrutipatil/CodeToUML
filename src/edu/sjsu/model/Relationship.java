package edu.sjsu.model;

public class Relationship {
	
	private String fromClassName;
	
	private String toClassName;
	
	private String rFrom;
	
	private String rTo;
	
	private boolean uses;
	
	public Relationship(String fromClassName, String toClassName, String rFrom, String rTo, boolean uses) {
		this.fromClassName = fromClassName;
		this.toClassName = toClassName;
		this.rFrom = rFrom;
		this.rTo = rTo;
		this.uses = uses;
	}

	public String getFromClassName() {
		return fromClassName;
	}

	public void setFromClassName(String fromClassName) {
		this.fromClassName = fromClassName;
	}

	public String getToClassName() {
		return toClassName;
	}

	public void setToClassName(String toClassName) {
		this.toClassName = toClassName;
	}

	public String getrFrom() {
		return rFrom;
	}

	public void setrFrom(String rFrom) {
		this.rFrom = rFrom;
	}

	public String getrTo() {
		return rTo;
	}

	public void setrTo(String rTo) {
		this.rTo = rTo;
	}

	public boolean isUses() {
		return uses;
	}

	public void setUses(boolean uses) {
		this.uses = uses;
	}
		
}
