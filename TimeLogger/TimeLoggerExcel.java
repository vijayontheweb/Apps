package swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * DOCUMENT ME!
 *
 * @author Perot Systems Noida
 * @version $Id:$
 *
 */
public class TimeLoggerExcel implements ActionListener {
    private static final String path = "C:\\Documents and Settings\\vijayana\\Desktop\\LOGGER\\Logger.xls";    
    Clock clock = null;    
    JComboBox combo;
    JTextField txt;
    private JLabel clockLabel;
    private SimpleDateFormat dateFormatTime = new SimpleDateFormat("h:mm:ss");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/y");
    String currentTask;
    String newTask;
    TaskObject taskObject = null;
    private List<TaskObject> fileText = null;
    private Timer timer = new Timer(1000, this);
    HSSFSheet sheet =null;
    
    private TimeLoggerExcel() throws Exception {
    	//String[] task = { "Mail", "ITK", "Lunch", "Work", "Meeting", "Training", "Research" };
    	String[] task = loadExcelFile();        
        JFrame frame = new JFrame("TIME LOGGER");
        JPanel panel = new JPanel();
        combo = new JComboBox(task);
        combo.setBackground(Color.gray);
        combo.setForeground(Color.red);
        txt = new JTextField(10);
        clockLabel = new JLabel();
        panel.add(combo);
        panel.add(txt);
        panel.add(clockLabel);
        frame.add(panel);
        frame.addWindowListener(new winEvent());
        combo.addItemListener(new ItemListener() {
                public void itemStateChanged(final ItemEvent ie) {
                    if (ie.getStateChange() == ItemEvent.SELECTED) {
                        taskObject = new TaskObject();
                        newTask = (String) combo.getSelectedItem();
                        taskObject.setTaskName(newTask);
                        taskObject.setStartTime(new Date());
                        if (timer.isRunning()) {
                            timer.stop();
                        }
                        txt.setText("Now Running ..." + newTask);
                        clearClock();
                        timer.start();
                    } else if (taskObject != null) {
                        recordTime();
                    }
                }
            });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 80);
        frame.setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(final String[] args) throws Exception {
        new TimeLoggerExcel();
    }
    private void clearClock() {
    	clock = new Clock();
    }
    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void actionPerformed(final ActionEvent event) {
    	clock.tick();
        clockLabel.setText(clock.show());
    }    
    
    
    private String[] loadExcelFile() throws Exception {    	
    	fileText = new ArrayList<TaskObject>();
    	InputStream input = new FileInputStream(path);
    	POIFSFileSystem fs = new POIFSFileSystem(input);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        sheet = wb.getSheetAt(1);
    	int rowOffest = sheet.getLastRowNum();
    	List<String> taskList = new ArrayList<String>();
    	for(int i=1;i<=rowOffest;i++){
        	HSSFRow hssfRow =  sheet.getRow(i);
        	HSSFCell cell = hssfRow.getCell(0);        	    	
        	taskList.add(cell.getStringCellValue());
    	}    	
    	return taskList.toArray(new String[taskList.size()]);
    }
    private void storeExcelFile() {
    	try{
	    	InputStream input = new FileInputStream(path);
	        POIFSFileSystem fs = new POIFSFileSystem(input);
	        HSSFWorkbook wb = new HSSFWorkbook(fs);
	        sheet = wb.getSheetAt(0);
	        HSSFFont font = wb.createFont();
	        font.setColor(HSSFColor.BLACK.index);
	    	int rowOffest = sheet.getLastRowNum();
	    	for(int i=1;i<=fileText.size();i++){       		
	    		TaskObject taskObject = fileText.get(i-1);
	    		feedData(taskObject,rowOffest+i);
	    	}
	        FileOutputStream stream = new FileOutputStream(path);
	        wb.write(stream);
	        stream.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        System.out.println("The data has been written");
    }
    
    private void feedData(TaskObject taskObject, int row){
    	HSSFRow hssfRow =  sheet.getRow(row);
    	if(hssfRow==null){
    		hssfRow = sheet.createRow(row);
    	}
    	setCell(hssfRow,0,dateFormat.format(new Date()));
    	setCell(hssfRow,1,taskObject.getTaskName());
    	setCell(hssfRow,2,dateFormatTime.format(taskObject.getStartTime()));
    	setCell(hssfRow,3,dateFormatTime.format(taskObject.getEndTime()));
    	setCell(hssfRow,4,taskObject.getElapsedTime());
    }
    
    private void setCell(HSSFRow hssfRow, int column, String message){
    	HSSFCell cell = hssfRow.getCell(column);
    	if(cell==null){
    		cell = hssfRow.createCell(column);
    	}    	
        cell.setCellValue(message);
    }
    
    
    private void feedData(HSSFSheet sheet, int row, int column, String message){
    	HSSFRow hssfRow =  sheet.getRow(row);
    	if(hssfRow==null){
    		hssfRow = sheet.createRow(row);
    	}
    	HSSFCell cell = hssfRow.getCell(column);
    	if(cell==null){
    		cell = hssfRow.createCell(column);
    	}    	
        cell.setCellValue(message);
    }
    
    
    private void recordTime() {
        taskObject.setEndTime(new Date());
        taskObject.setElapsedTime(clock.show());
        fileText.add(taskObject);
    }

    class TaskObject {
        private String taskName;
        private String taskComment;
        private Date startTime;
        private Date endTime;
        private String elapsedTime;

        private String getElapsedTime() {
			return elapsedTime;
		}
		private void setElapsedTime(String elapsedTime) {
			this.elapsedTime = elapsedTime;
		}
		private String getTaskName() {
            return taskName;
        }
        private void setTaskName(final String taskName) {
            this.taskName = taskName;
        }
        private String getTaskComment() {
            return taskComment;
        }
        private void setTaskComment(final String taskComment) {
            this.taskComment = taskComment;
        }
        private Date getStartTime() {
            return startTime;
        }
        private void setStartTime(final Date startTime) {
            this.startTime = startTime;
        }
        private Date getEndTime() {
            return endTime;
        }
        private void setEndTime(final Date endTime) {
            this.endTime = endTime;
        }
    }

    class winEvent extends WindowAdapter {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void windowClosing(final WindowEvent e) {
            if (taskObject != null) {
                recordTime();
            }
            storeExcelFile();
            System.exit(0);
        }
    }
    
    class Clock{
    	int hour =0;
    	int minute = 0;
    	int second = 0;
    	
    	Clock(){
    		this.hour = 0;
    		this.minute = 0;
    		this.second = 0;
    	}
    	
		private int getHour() {
			return hour;
		}
		private void setHour(int hour) {
			this.hour = hour;
		}
		private int getMinute() {
			return minute;
		}
		private void setMinute(int minute) {
			this.minute = minute;
		}
		private int getSecond() {
			return second;
		}
		private void setSecond(int second) {
			this.second = second;
		}
		
		private void tick(){
			this.second++;
			if(this.second==60){
				this.second=0;
				this.minute++;
				if(this.minute==60){
					this.minute=0;
					this.hour++;					
				}
			}
		}
    	
		private String show(){
			return this.hour+":"+this.minute+":"+this.second;
		}	
    }
}
