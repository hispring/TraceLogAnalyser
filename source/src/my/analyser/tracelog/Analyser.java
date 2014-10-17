package my.analyser.tracelog;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 对BTrace脚本的输出日志进行分析；
 * 
 * @author haiq
 *
 */
public class Analyser {
	
	public static final String ARG_TRACE_LOG = "trace.log";
	
	public static void run(Map<String, String> args){
		if (!args.containsKey(ARG_TRACE_LOG)) {
			System.out.println("未指定参数 " + ARG_TRACE_LOG + " !");
			return;
		}
		File logFile = new File(args.get(ARG_TRACE_LOG));
		try {
			InvokingMap invokingMap = analyse(logFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static InvokingMap analyse(File logFile) throws IOException{
		InvokingStackReader stackReader = null;
		try {
			stackReader = new InvokingStackReader(logFile, new ConnTraceInvokingStackSpliter(), new DefaultLogLineFilter());
			
			InvokingMap invokingMap = new InvokingMap();
			InvokingStack stack = null;
			while ((stack = stackReader.next()) != null) {
				invokingMap.addInvokingStack(stack);
			}
			
			return invokingMap;
		}finally{
			if (stackReader != null) {
				stackReader.close();
			}
		}
	}

}
