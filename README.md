# CodeToUML
<ul>
<li>CodeToUML is a parser which converts Java source code into a UML diagram. </li>
<li>This project takes input as directory path containing all the Java source code and provides output as image of Class Diagram showing all classes, their attributes, operations and relationship among objects. </li>
</ul>
<br/>
<b>Technologies:</b>
<ol><li> Java</li>
<li> Javaparser 1.0.8</li>
<li> PlantUML</li>
</ol>

<b>Installation & Execution Steps:</b>
<ol>
<li> Install Graphviz, for your respective platform : <a href="http://www.graphviz.org/Download_windows.php"> Graphviz Link</a> </li>
<li> Install Java 1.7 or above if you do not have already</li>
<li> Download <a href="https://github.com/jagrutipatil/CodeToUML/blob/master/jar/javatouml.jar"> javatouml.jar</a> </li>
<li> Execute:<br/>
     <code>java -jar javatouml.jar <input_folder_path> <output­file­name></code> <br/>
     e.g:
     <code> java -jar <a href="https://github.com/jagrutipatil/CodeToUML/blob/master/jar/javatouml.jar"> javatouml.jar</a> /home/jagruti/uml-parser-test-1 test1 </code>
     
     <p>Here output image will be generated in same folder as input folder with extension .png. You only need to provide file name what to want to give.</p>
     
</li>
</ol>

<b> Examples:</b>
<ol>
<li><a href="https://github.com/jagrutipatil/CodeToUML/blob/master/testcases/uml-parser-test-1.zip">Test Case 1:</a>
<br/>Output:<br/>
<img src="https://github.com/jagrutipatil/CodeToUML/blob/master/testcases/output/test1.png">
</li>
<li><a href="https://github.com/jagrutipatil/CodeToUML/blob/master/testcases/uml-parser-test-2.zip">Test Case 2:</a>
<br/>Output:<br/>
<img src="https://github.com/jagrutipatil/CodeToUML/blob/master/testcases/output/test2.png">
</li>
<li><a href="https://github.com/jagrutipatil/CodeToUML/blob/master/testcases/uml-parser-test-3.zip">Test Case 3:</a>
<br/>Output:<br/>
<img src="https://github.com/jagrutipatil/CodeToUML/blob/master/testcases/output/test3.png">
</li>
<li><a href="https://github.com/jagrutipatil/CodeToUML/blob/master/testcases/uml-parser-test-4.zip">Test Case 4:</a>
<br/>Output:<br/>
<img src="https://github.com/jagrutipatil/CodeToUML/blob/master/testcases/output/test4.png">
</li>
</ol>
