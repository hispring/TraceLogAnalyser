package my.analyser.tracelog;

public interface InvokingNode {
	
	public String key();
	
	public String getName();
	
	/**
	 * ���θ��ٵ�Ŀ������ִ�д�����
	 * 
	 * @return
	 */
	public int getTargetInvokingCount();
	
	public InvokingNodeSet getNextSteps();
	
//	public InvokingNodeSet getPreviousSteps();
	
}
