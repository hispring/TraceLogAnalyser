package my.analyser.tracelog;

public interface LogLineFilter {
	
	/**
	 * 是否忽略指定行；
	 * 
	 * @param line
	 * @return
	 */
	public boolean isIgnore(String line);
	
}
