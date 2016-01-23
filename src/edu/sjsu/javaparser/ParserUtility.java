package edu.sjsu.javaparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.sjsu.model.ClassModel;
import edu.sjsu.model.Method;
import edu.sjsu.model.Variable;

public class ParserUtility {

	public static boolean isGetSetter(ClassModel classModel, Method method) {
		for (Variable cVar: classModel.getVariables()) {
			if(method.getName().equalsIgnoreCase("get" + cVar.getName()) || method.getName().equalsIgnoreCase("set" + cVar.getName())) {
				cVar.setAccessModifier("public");
				return true;
			}
		}
		return false;
	}
	
	public static String containsList(String dataType) {
		// TODO write pattern to detect the list, collection, arraylist, [],
		// Array
		if (containsPatternList(dataType)) {
			return "*";
		} else if (dataType.contains("Set")) {
			return "*";
		} else if (dataType.contains("List")) {
			return "*";
		} else if (dataType.toUpperCase().contains("ArrayList")) {
			return "*";
		} else if (dataType.toUpperCase().contains("LinkedList")) {
			return "*";
		} else if (dataType.toUpperCase().contains("SortedSet")) {
			return "*";
		} else if (dataType.toUpperCase().contains("TreeSet")) {
			return "*";
		}
		// TODO add maps
		return "1";
	}

	public static boolean containsPatternList(String dataType) {
		Pattern pattern = Pattern.compile("<(.*?)>");
		Matcher matcher = pattern.matcher(dataType);
		String extracted = dataType;

		if (matcher.find()) {
			return true;
		} else {
			pattern = Pattern.compile("[(.*?)]");
			matcher = pattern.matcher(dataType);
			if (matcher.find()) {
				return true;
			}
		}

		// TODO what if its not able to fetch and is complext type like []
		return false;
	}

	public static String extractDataType(String dataType) {
		Pattern pattern = Pattern.compile("<(.*?)>");
		Matcher matcher = pattern.matcher(dataType);
		String extracted = dataType;

		if (matcher.find()) {
			extracted = matcher.group(1);
		} else {
			pattern = Pattern.compile("[(.*?)]");
			matcher = pattern.matcher(dataType);
			if (matcher.find()) {
				extracted = matcher.group(1);
			}
		}

		// TODO what if its not able to fetch and is complext type like []
		return extracted;
	}

	public static boolean isPrimitive(String dataType) {

		if (dataType.equalsIgnoreCase(Integer.class.getSimpleName())) {
			return true;
		} else if (dataType.equalsIgnoreCase(Long.class.getSimpleName())) {
			return true;
		} else if (dataType.equalsIgnoreCase(Boolean.class.getSimpleName())) {
			return true;
		} else if (dataType.equalsIgnoreCase(Character.class.getSimpleName())) {
			return true;
		} else if (dataType.equalsIgnoreCase(Byte.class.getSimpleName())) {
			return true;
		} else if (dataType.equalsIgnoreCase(Short.class.getSimpleName())) {
			return true;
		} else if (dataType.equalsIgnoreCase(Void.class.getSimpleName())) {
			return true;
		} else if (dataType.equalsIgnoreCase(Float.class.getSimpleName())) {
			return true;
		} else if (dataType.equalsIgnoreCase(Double.class.getSimpleName())) {
			return true;
		} else if (dataType.equalsIgnoreCase(String.class.getSimpleName())) {
			return true;
		} else if (dataType.contains("int[]")) {
			return true;
		} else if (dataType.contains("String[]")) {
			return true;
		} else if (dataType.contains("float[]")) {
			return true;
		} else if (dataType.contains("double[]")) {
			return true;
		} else if (dataType.contains("long[]")) {
			return true;
		} else if (dataType.contains("int")) {
			return true;
		} else if (dataType.contains("float")) {
			return true;
		} else if (dataType.contains("double")) {
			return true;
		} else if (dataType.contains("long")) {
			return true;
		} else if (dataType.contains("boolean")) {
			return true;
		} else if (dataType.contains("String")) {
			return true;
		} else if (dataType.contains("char")) {
			return true;
		} else {
			Class<?> clazz;
			try {
				clazz = Class.forName(dataType);
			} catch (ClassNotFoundException e) {
				return false;
			}
			return clazz.isPrimitive();
		}
	}

}
