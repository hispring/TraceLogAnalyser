package my.analyser.tracelog;

public interface LogSectionSpliter {
	
	public boolean isStartingLine(String line);
	
	public boolean isEndLine(String line);
	
}
