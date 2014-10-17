package my.analyser.tracelog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 调用栈包含了一次调用的所有调用点；
 * 
 * 处在最上面(最前面)的是最浅的调用；
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
	 * 返回此调用栈记录的最大深度；
	 * 
	 * @return
	 */
	public int getMaxLevel(){
		return invokingPoints.length;
	}
	
	/**
	 * 返回指定深度的调用点；
	 * 
	 * @param level 调用栈深度；从 0 开始，小于 maxLevel;
	 * @return
	 */
	public InvokingPoint getInvokingPoint(int level){
		return invokingPoints[level];
	}
}
