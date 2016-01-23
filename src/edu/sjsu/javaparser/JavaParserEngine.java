package edu.sjsu.javaparser;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import edu.sjsu.model.ClassModel;
import edu.sjsu.model.Method;
import edu.sjsu.model.Relationship;
import edu.sjsu.model.Variable;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import net.sourceforge.plantuml.SourceStringReader;

public class JavaParserEngine {

	String outputDir;
	String classpath;
	
	Map<String, ClassModel> classes = new HashMap<String, ClassModel>();
	Map<String, Set<Relationship>> classRelations = new HashMap<String, Set<Relationship>>();

	public JavaParserEngine(String classpath, String outputDir) {
		if (classpath == null || outputDir == null || classpath.equals("") || outputDir.equals("")) {
			usage();
		}
		this.classpath = classpath;
		this.outputDir = outputDir;
	
	}
	
	public void usage() {
		System.out.println("Usage:\njava -jar <classpath> <outputfilename>");
	}
	
	public void parseClasses() throws FileNotFoundException, ParseException, IOException {
		File config = new File(classpath);
		File[] fileset = config.listFiles();
		if (fileset != null) {
			for (File javaFile : fileset) {
				if (javaFile.isDirectory()) {
					continue;
				} else if (!(javaFile.getAbsolutePath().endsWith(".java"))) {
					continue;
				}
				ClassModel javaClass = getCUnit(new FileInputStream(javaFile));
				classes.put(javaClass.getName(), javaClass);
			}
		}
	}

	public void getClassDiagram(String grammer) throws IOException {
		ByteArrayOutputStream boutStram = new ByteArrayOutputStream();
		SourceStringReader reader = new SourceStringReader(grammer);
		String gdesc = reader.generateImage(boutStram);
		byte[] byteArray = boutStram.toByteArray();
		InputStream input = new ByteArrayInputStream(byteArray);
		BufferedImage img = ImageIO.read(input);
		ImageIO.write(img, "png", new File(classpath + "/" + outputDir + ".png"));
		System.out.println(gdesc);
	}

	public String getPackageGrammer() {
		String grammer = "@startuml" + "\n" + "skinparam classAttributeIconSize 0" + "\n";

		for (ClassModel classModel : classes.values()) {
			grammer = grammer + "\n\n" + getClassGrammer(classModel);
		}

		//

		String associations = "\n";
		for (String className : classRelations.keySet()) {
			for (Relationship reln : classRelations.get(className)) {
				// if (classes.containsKey(relatedClass)) {
				//
				// } else {
				// //TODO add attribute compartment, clarify if we need to show
				// the class even if it does not exist in the package
				// }
				if (reln.isUses()) {
					associations = associations + className + " " + "\"uses\"" + " ..> " + " " + reln.getToClassName()
							+ "\n";
				}

				if (!reln.getFromClassName().equals(reln.getToClassName()) && classes.containsKey(reln.getToClassName())
						&& classes.containsKey(reln.getFromClassName()) && !reln.isUses()) {
					if (reln.getrFrom().equals("") && reln.getrTo().equals("")) {
						associations = associations + className + " -- " + reln.getToClassName() + "\n";
					} else if (reln.getrFrom().equals("")) {
						associations = associations + className + " -- " + "\"" + reln.getrTo() + "\"" + " "
								+ reln.getToClassName() + "\n";
					} else if (reln.getrTo().equals("")) {
						associations = associations + className + " " + "\"" + reln.getrFrom() + "\"" + " -- "
								+ reln.getToClassName() + "\n";
					} else {
						associations = associations + className + " " + "\"" + reln.getrFrom() + "\"" + " -- " + "\""
								+ reln.getrTo() + "\"" + " " + reln.getToClassName() + "\n";
					}
				}

			}
		}

		System.out.println(associations);
		grammer = grammer + associations;

		grammer = grammer + "\n@enduml";
		return grammer;
	}

	public String getClassGrammer(ClassModel classModel) {
		String grammer = "";
		if (classModel.isInterface()) {
			grammer = grammer + "interface " + classModel.getName();
		} else {
			grammer = grammer + "class " + classModel.getName();
		}

		
		
		grammer = grammer + getExtendzGrammer(classModel) + getInterfaceGrammer(classModel) + " {\n"
				+ getVariableGrammer(classModel) + getMethodGrammer(classModel) + "\n }";

		System.out.println(grammer);
		return grammer;
	}

	
	public String getInterfaceGrammer(ClassModel classModel) {
		String grammer = "";

		if (classModel.getInterfaces() != null && classModel.getInterfaces().size() > 0) {
			int i = 0;
			grammer = grammer + " implements ";
			for (String interf : classModel.getInterfaces()) {
				grammer = grammer + interf;
				i++;
				if (i != classModel.getInterfaces().size()) {
					grammer = grammer + ", ";
				}
			}
		}
		return grammer;
	}

	public String getExtendzGrammer(ClassModel classModel) {
		String grammer = "";

		if (classModel.getExtendz() != null) {
			grammer = grammer + " extends " + classModel.getExtendz();
		}
		return grammer;
	}

	public String getMethodGrammer(ClassModel classModel) {
		String grammer = "";
		for (Method method : classModel.getMethods()) {
			String mModifier = "", mGrammer = "";
			if (method.getAccessModifier() != null) {
				if (classModel.isInterface()) {
					mModifier = "+";
				} else if (method.getAccessModifier().equalsIgnoreCase("public")
						|| method.getAccessModifier().contains("public")) {
					mModifier = "+";
				} else if (method.getAccessModifier().equalsIgnoreCase("private")
						|| method.getAccessModifier().contains("private")) {
					mModifier = "-";
				} else if (method.getAccessModifier().equalsIgnoreCase("protected")
						|| method.getAccessModifier().contains("protected")) {
					mModifier = "#";
				} else {
					mModifier = "~";
				}
			}
			if (mModifier.equals("+")) {
				mGrammer = mModifier + method.getName() + "(";
				if (method.getParameters().values() != null) {
					int i = 0;
					for (Variable pVar : method.getParameters().values()) {
						String pVariable = pVar.getName() + ":" + pVar.getDataType();
						i++;
						if (method.getParameters().values().size() != i) {
							pVariable = pVariable + ",";
						}
						mGrammer = mGrammer + pVariable;
						if (!classModel.isInterface()) {
							boolean found = false;
							//TODO uncomment for uses and association (to remove double arrows)
							//-------------------------------
//							if (classModel.getExtendz() != null) {
//								if(pVar.getDataType().equals(classModel.getExtendz())) {
//									System.out.println(classModel.getName() + "extends "+ classModel.getExtendz());
//									found = true;
//								}
//							} 
//							if (classModel.getInterfaces() != null) {
//								for (String interf: classModel.getInterfaces()) {
//									if (pVar.getDataType().equals(interf)) {
//										System.out.println(classModel.getName() + "implements "+ interf);
//										found = true;
//									}
//								}
//							}
							//-------------------------------------
							if (!found) {
								getRelationships(pVar, classModel, true);
							}
						}
					}

					mGrammer = mGrammer + ")" + ":" + method.getReturnType() + "\n";

					if (method.getLocalvar().values() != null) {
						for (Variable lVar : method.getLocalvar().values()) {
							if (!classModel.isInterface()) {
								boolean found = false;
								//TODO to avoid double arrows remove this
								//-----------------------------------
//								if (classModel.getExtendz() != null) {
//									if(!lVar.getDataType().equals(classModel.getExtendz())) {
//										System.out.println(classModel.getName() + " extends "+ classModel.getExtendz());
//										found = true;
//									}
//								}
//								if (classModel.getInterfaces() != null) {
//									for (String interf: classModel.getInterfaces()) {
//										if (lVar.getDataType().equals(interf)) {
//											System.out.println(classModel.getName() + " implements "+ interf);
//											found = true;
//										}
//									}
//								}
								//----------------------------------
								if (!found) {
									getRelationships(lVar, classModel, true);
								}
							}
						}
					}
				}
				grammer = grammer + mGrammer;
			}
		}
		return grammer;
	}

	public String getVariableGrammer(ClassModel classModel) {
		String grammer = "";
		for (Variable cVar : classModel.getVariables()) {
			String vModifier = "", vGrammer = "";
			if (cVar.getAccessModifier() != null) {
				if (cVar.getAccessModifier().equalsIgnoreCase("public")
						|| cVar.getAccessModifier().contains("public")) {
					vModifier = "+";
				} else if (cVar.getAccessModifier().equalsIgnoreCase("private")
						|| cVar.getAccessModifier().contains("private")) {
					vModifier = "-";
				} else if (cVar.getAccessModifier().equalsIgnoreCase("protected")
						|| cVar.getAccessModifier().contains("protected")) {
					vModifier = "#";
				} else {
					vModifier = "~";
				}
			}
			
			if(ParserUtility.isPrimitive(cVar.getDataType()) && (vModifier.equals("-") || vModifier.equals("+"))) {			
				vGrammer = vModifier + cVar.getName() + ":" + cVar.getDataType() + "\n";
			}
			
			boolean found = false;
			//TODO to avoid double arrows remove this
			//------------------------------------------
//			if (classModel.getExtendz() != null) {
//				if(cVar.getDataType().equals(classModel.getExtendz())) {
//					found = true;
//				}
//			} 
//			if (classModel.getInterfaces() != null) {
//				for (String interf: classModel.getInterfaces()) {
//					if (cVar.getDataType().equals(interf)) {
//						found = true;
//					}
//				}
//			}
			//------------------------------------------------
			if (!found) {
				getRelationships(cVar, classModel, false);
			}
			
			grammer = grammer + vGrammer;
		}
		return grammer;
	}

	private void getRelationships(Variable cVar, ClassModel classModel, boolean uses) {
		if (!ParserUtility.isPrimitive(cVar.getDataType())) {
			String fromClassName = classModel.getName();
			String toClassName = ParserUtility.extractDataType(cVar.getDataType());

			if (classRelations.containsKey(fromClassName)) {
				Set<Relationship> setFrom = classRelations.get(fromClassName);
				if (!isBackwordDependancy(toClassName, fromClassName, null , ParserUtility.containsList(cVar.getDataType())) && !isDuplicate(fromClassName, toClassName, ParserUtility.containsList(cVar.getDataType()), uses)) {
					Relationship rel = new Relationship(fromClassName, toClassName, "", ParserUtility.containsList(cVar.getDataType()), uses);
					setFrom.add(rel);
				}
			} else {
				if (!isBackwordDependancy(toClassName, fromClassName, ParserUtility.containsList(cVar.getDataType()), null)) {			
					Set<Relationship> setFrom = new HashSet<Relationship>();
					Relationship rel = new Relationship(fromClassName, toClassName, "", ParserUtility.containsList(cVar.getDataType()), uses);
					setFrom.add(rel);
					classRelations.put(fromClassName, setFrom);
				}
			}
		}
	}

	private boolean isDuplicate(String fromClassName, String toClassName, String multi, boolean uses) {
		if (classRelations.containsKey(fromClassName)) {
			Set<Relationship> setFrom = classRelations.get(fromClassName);
			for (Relationship rel : setFrom) {
				if (rel.getToClassName().equals(toClassName)) {
					if (multi.equalsIgnoreCase("*")) {
						rel.setrTo(multi);
					} 
					
//					if (uses)  {
//						rel.setUses(uses);
//					}
					return true;
				}
			}
		}
		return false;
	}

	private boolean isBackwordDependancy(String toClassName, String fromClassName , String mA, String mB) {
		Relationship mRel;
		if (classRelations.containsKey(toClassName)) {
			Set<Relationship> setTo = classRelations.get(toClassName);
			for (Relationship rel : setTo) {
				if (rel.getToClassName().equals(fromClassName)) {
					if (mA != null && !rel.getrFrom().equals("*")) {
						rel.setrFrom(mA);
					}
					if (mB != null && !rel.getrFrom().equals("*")) {
						rel.setrFrom(mB);
					}
					return true;
				}
			}
		}
		return false;
	}


	public ClassModel getCUnit(FileInputStream input) throws ParseException, IOException {
		try {
			CompilationUnit unit = JavaParser.parse(input);
			ClassModel classModel = new ClassModel("");
			new ClassVisitor().visit(unit, classModel);
			return classModel;
		} finally {
			input.close();
		}
	}

	// public void printVariable() {
	// System.out.println("All Variables:\n");
	// for (String var : varMap.keySet()) {
	// System.out.println(varMap.get(var).toString());
	// }
	//
	// System.out.println("All Methods:\n");
	// for (String method : methodMap.keySet()) {
	// System.out.println(methodMap.get(method).toString());
	// }
	// }

	public static void main(String args[]) {
		JavaParserEngine engine = new JavaParserEngine(args[0], args[1]);
		try {
			engine.parseClasses();
			String grammer = engine.getPackageGrammer();
			engine.getClassDiagram(grammer);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class ClassVisitor extends VoidVisitorAdapter {
		@Override
		public void visit(ClassOrInterfaceDeclaration n, Object arg) {
			
			if (arg instanceof ClassModel) {
				
				ClassModel jClass = (ClassModel) arg;
				String vType, vName, initValue = null;
				jClass.setName(n.getName());
				jClass.setAsInterface(n.isInterface());

				List<ClassOrInterfaceType> extendz = n.getExtends();
				if (extendz != null) {
					for (int i = 0; i < extendz.size(); i++) {
						jClass.setExtendz(extendz.get(i).getName());
					}
				}

				List<ClassOrInterfaceType> implementz = n.getImplements();
				if (implementz != null) {
					for (int i = 0; i < implementz.size(); i++) {
						jClass.addInterface(implementz.get(i).getName());
					}
				}

				List<BodyDeclaration> bDeclrs = n.getMembers();
				for (BodyDeclaration bDeclr : bDeclrs) {
					if (bDeclr instanceof ConstructorDeclaration) {
						ConstructorDeclaration constr = (ConstructorDeclaration) bDeclr;
						
						Method method = new Method(constr.getName(), "", Modifier.toString(constr.getModifiers()));
						jClass.addMethod(method.getName(), method);
				
						if (constr.getParameters() != null) {
							for (Parameter param : constr.getParameters()) {
								method.addParameter(new Variable(param.getId().getName(), param.getType().toString(), null, null));
							}
						}
						
						BlockStmt blockStmnt = constr.getBlock();
						if (blockStmnt != null) {
							blockStmnt.accept(new ClassVisitor(), method);
						}
					}
					
					if (bDeclr instanceof FieldDeclaration) {
						FieldDeclaration var = (FieldDeclaration) bDeclr;
						vType = var.getType().toString(); // getType
						vName = var.getVariables().get(0).getId().getName(); // variableName

						if (var.getVariables().get(0).getInit() != null)
							initValue = var.getVariables().get(0).getInit().toString();

						jClass.addVariable(vName, new Variable(vName, vType, initValue, Modifier.toString(var.getModifiers())));
					}

					if (bDeclr instanceof MethodDeclaration) {
						MethodDeclaration jmethod = (MethodDeclaration) bDeclr;
						
						Method method = new Method(jmethod.getName(), jmethod.getType().toString(),
								Modifier.toString(jmethod.getModifiers()));
						if (!ParserUtility.isGetSetter(jClass, method)) {
							jClass.addMethod(method.getName(), method);
							if (jmethod.getParameters() != null) {
								for (Parameter param : jmethod.getParameters()) {
									method.addParameter(new Variable(param.getId().getName(), param.getType().toString(), null, null));
								}
							}
						}
						
						BlockStmt blockStmnt = jmethod.getBody();
						if (blockStmnt != null) {
							blockStmnt.accept(new ClassVisitor(), method);
						}				
					}
				}
			
			}
		}
	
		@Override
		public void visit(VariableDeclarationExpr n, Object arg) {   
			Method method = null;
			if (arg instanceof Method) {
			   method = (Method)arg;	
			   
			   List <VariableDeclarator> myVars = n.getVars();
		        for (VariableDeclarator vars: myVars) {
		        	method.addLocalVar(new Variable(vars.getId().getName(), n.getType().toString(), null, null));
		            System.out.println("Variable Name: "+ vars.getId().getName() + "   DataType: " +  n.getType().toString());
		        }
			}								    
		}	
	}
}
