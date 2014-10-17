package my.analyser.tracelog;

/**
 * ���õ��ǵ���ջ�е�һ�У�
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
	 * @param clazzName ���õ���ȫ�������������������ƿ�����һ�������࣬����� javaFileName ����һ����ͬ��
	 * @param methodName ���õķ�������
	 * @param javaFileName java�ļ���������·���� .java ��չ����
	 * @param lineNumber �кţ�
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
