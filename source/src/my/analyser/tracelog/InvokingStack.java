package my.analyser.tracelog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ����ջ������һ�ε��õ����е��õ㣻
 * 
 * ����������(��ǰ��)������ǳ�ĵ��ã�
 * 
 * @author haiq
 *
 */
public class InvokingStack {
	private InvokingPoint[] invokingPoints;
	public InvokingStack(List<String> stackLines) {
		ArrayList<InvokingPoint> ivkPts = new ArrayList<InvokingPoint>();
		for (String stackline : stackLines) {
			ivkPts.add(buildInvokingPoint(stackline));
		}
		Collections.reverse(ivkPts);
		invokingPoints = ivkPts.toArray(new InvokingPoint[ivkPts.size()]);
	}
	
	
	private InvokingPoint buildInvokingPoint(String stackline){
		int idxLeftBracket = stackline.indexOf('(');
		int idxRightBracket = stackline.indexOf(')');
		String fileInfo = stackline.substring(idxLeftBracket+1, idxRightBracket);
		int idxExtenName = fileInfo.indexOf('.');
		String javafileName = fileInfo.substring(0, idxExtenName);
		int idxColon = fileInfo.indexOf(':');
		int lineNumber = Integer.parseInt(fileInfo.substring(idxColon+1, fileInfo.length()));
		
		String methodInfo = stackline.substring(0, idxLeftBracket);
		int idxLastDot = methodInfo.lastIndexOf('.');
		String methodName = methodInfo.substring(idxLastDot+1, methodInfo.length());
		String clazzName = methodInfo.substring(0, idxLastDot);
		
		InvokingPoint invokingPoint = new InvokingPoint(clazzName, methodName, javafileName, lineNumber);
		return invokingPoint;
	}
	
	/**
	 * ���ش˵���ջ��¼�������ȣ�
	 * 
	 * @return
	 */
	public int getMaxLevel(){
		return invokingPoints.length;
	}
	
	/**
	 * ����ָ����ȵĵ��õ㣻
	 * 
	 * @param level ����ջ��ȣ��� 0 ��ʼ��С�� maxLevel;
	 * @return
	 */
	public InvokingPoint getInvokingPoint(int level){
		return invokingPoints[level];
	}
}
