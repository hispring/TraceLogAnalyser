package my.analyser.tracelog;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;

public class AnalyserUI {

	private JFrame frame;
	private JTree tree;
	private JTextField txtLogFile;
	private JTextField txtStatus;
	
	private String lastFolder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AnalyserUI window = new AnalyserUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AnalyserUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 724, 457);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JToolBar toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openTraceLog();
			}
		});
		
		JLabel lblLogfile = new JLabel("LogFile:");
		toolBar.add(lblLogfile);
		
		txtLogFile = new JTextField();
		txtLogFile.setEditable(false);
		toolBar.add(txtLogFile);
		txtLogFile.setColumns(10);
		toolBar.add(btnOpen);
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		tree = new JTree();
		tree.setModel(null);
		tree.setRootVisible(false);
		scrollPane.setViewportView(tree);
		
		JToolBar statusBar = new JToolBar();
		frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		
		txtStatus = new JTextField();
		txtStatus.setEditable(false);
		statusBar.add(txtStatus);
		txtStatus.setColumns(10);
	}
	
	private void openTraceLog(){
		
		JFileChooser fileChooser = new JFileChooser(lastFolder);
		if (fileChooser.showOpenDialog(this.frame) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			txtLogFile.setText(selectedFile.getAbsolutePath());
			lastFolder = selectedFile.getParent();
			try {
				InvokingMap ivkMap = Analyser.analyse(selectedFile);
				InvokingTreeModel model = new InvokingTreeModel(ivkMap);
				tree.setModel(model);
				showStatus("完成日志分析！");
			} catch (Exception e) {
				showStatus("发生错误！--"+e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	private void showStatus(String msg){
		txtStatus.setText(msg);
	}

}
