package my.analyser.tracelog;

public class ConnTraceInvokingStackSpliter implements LogSectionSpliter {

	private static final String START_PREFIX = "connCount=";
	
	@Override
	public boolean isStartingLine(String line) {
		return line.startsWith(START_PREFIX);
	}

	@Override
	public boolean isEndLine(String line) {
		return line.length() == 0;
	}

}
