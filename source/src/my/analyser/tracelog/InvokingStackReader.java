package my.analyser.tracelog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class InvokingStackReader {
	
	@SuppressWarnings("unused")
	private File logFile;
	
	private BufferedReader reader;
	
	private LogSectionSpliter spliter;
	
	private LogLineFilter lineFilter;
	
	/**
	 * 创建调用栈读取器；
	 * 
	 * @param logFile 调用栈日志文件；
	 * @param spliter 调用栈分隔器，用于捕捉调用栈的日志片段；
	 * @param lineFilter 调用栈日志行过滤器，对 spliter 识别的调用栈的日志片段中的日志行进行过滤；
	 * 
	 * @throws FileNotFoundException
	 */
	public InvokingStackReader(File logFile, LogSectionSpliter spliter, LogLineFilter lineFilter) throws FileNotFoundException {
		this.logFile = logFile;
		this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
		this.spliter = spliter;
		this.lineFilter = lineFilter;
	}
	
	public InvokingStack next() throws IOException{
		ArrayList<String> stackLines = new ArrayList<>();
		boolean started = false;
		boolean end = false;
		String line = null;
		while((line = reader.readLine()) != null){
			if (!started) {
				started = spliter.isStartingLine(line);
			}
			if (started) {
				end = spliter.isEndLine(line);
				if (end) {
					break;
				}
				if (lineFilter.isIgnore(line)) {
					continue;
				}
				stackLines.add(line);
			}
		}
		if (started && end) {
			return new InvokingStack(stackLines);
		}else{
			return null;
		}
	}
	
	public void close() throws IOException{
		this.reader.close();
	}
}
