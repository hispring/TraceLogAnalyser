package my.analyser.tracelog;

/**
 * 调用点是调用栈中的一行；
 * 
 * @author haiq
 *
 */
public class InvokingPoint {
	
	private String clazzName;
	
	private String methodName;
	
	private String javaFileName;
	
	private int lineNumber;
	
	private String fullStamp;
	
	/**
	 * @param clazzName 调用的类全名，包含包名；该名称可能是一个匿名类，因此与 javaFileName 并不一定相同；
	 * @param methodName 调用的方法名；
	 * @param javaFileName java文件名，不带路径和 .java 扩展名；
	 * @param lineNumber 行号；
	 */
	public InvokingPoint(String clazzName, String methodName, String javaFileName, int lineNumber) {
		this.clazzName = clazzName;
		this.methodName = methodName;
		this.javaFileName = javaFileName;
		this.lineNumber = lineNumber;
		this.fullStamp = String.format("%s.%s(%s:%s)", clazzName,methodName,javaFileName,lineNumber);
	}

	public String getClazzName() {
		return clazzName;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getJavaFileName() {
		return javaFileName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getFullStamp() {
		return fullStamp;
	}
	
}
