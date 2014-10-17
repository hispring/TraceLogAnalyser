package my.analyser.tracelog;

public class DefaultLogLineFilter implements LogLineFilter{
	
	private static String[] IGNORE_SUFFIXES = {
		"(Unknown Source)", 
		"(Native Method)"
		};

	@Override
	public boolean isIgnore(String line) {
		for (String suffix : IGNORE_SUFFIXES) {
			if (line.endsWith(suffix)) {
				return true;
			}
		}
		if (line.length() == 0) {
			return true;
		}
		if (line.indexOf('(') <= 0) {
			return true;
		}
		if (line.indexOf(')') < 0) {
			return true;
		}
		if (line.startsWith("org.apache.catalina.")) {
			return true;
		}
		return false;
	}

}
