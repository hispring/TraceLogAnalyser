package my.analyser.tracelog;

public interface LogLineFilter {
	
	/**
	 * �Ƿ����ָ���У�
	 * 
	 * @param line
	 * @return
	 */
	public boolean isIgnore(String line);
	
}
