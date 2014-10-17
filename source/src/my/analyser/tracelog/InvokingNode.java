package my.analyser.tracelog;

public interface InvokingNode {
	
	public String key();
	
	public String getName();
	
	/**
	 * 本次跟踪的目标代码的执行次数；
	 * 
	 * @return
	 */
	public int getTargetInvokingCount();
	
	public InvokingNodeSet getNextSteps();
	
//	public InvokingNodeSet getPreviousSteps();
	
}
