package swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * DOCUMENT ME!
 *
 * @author Perot Systems Noida 
 * @version $Id:$
 *
 * @created 2007
 *
  */
public class TimeLogger implements ActionListener {
    private static final String path = "C:\\Documents and Settings\\vijayana\\Desktop\\LOGGER\\Logger.txt";
    int counter = 1;
    JComboBox combo;
    JTextField txt;
    private JLabel clockLabel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm:ss");
    String currentTask;
    String newTask;
    TaskObject taskObject = null;
    private List<String> fileText = null;
    private Timer timer = new Timer(1, this);

    private TimeLogger() throws Exception {
        loadFile();
        String[] task = {"Mail","ITK", "Lunch", "Work"};
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
                        clearCounter();
                        timer.start();
                    } else if(taskObject!=null){
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
    	new TimeLogger();
    }
    private void clearCounter() {
        counter = 1;
    }
    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void actionPerformed(final ActionEvent event) {
        clockLabel.setText(dateFormat.format(new Date()));
    }
    private void loadFile() throws Exception {
    	fileText = new ArrayList<String>();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("File Does'nt exist. Creating Output file");
            f.createNewFile();
        } else {
            System.out.println("File exists. Writing Output file");
        }
        Scanner scanner = new Scanner(new FileInputStream (path));
        while(scanner.hasNextLine()){
        	fileText.add(scanner.nextLine());        	
        }        
    }
    private void storeFile() {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.out.println("File Does'nt exist. Creating Output file");
                file.createNewFile();
            } else {
                System.out.println("File exists. Writing Output file");
            }            
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            Iterator<String> iter = fileText.iterator();
            while(iter.hasNext()){
            	bufferedWriter.write(iter.next());
            	bufferedWriter.newLine();
            }
            
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (final Exception e) {;
        }
        System.out.println("The data has been written");
    }
    private void recordTime() {
        taskObject.setEndTime(new Date());
        fileText.add(taskObject.toString());
    }

    class TaskObject {
        private String taskName;
        private String taskComment;
        private Date startTime;
        private Date endTime;

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
        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            return taskName + "->" + dateFormat.format(startTime) + "->" + dateFormat.format(endTime) + "->"
            + taskComment + "\n";
        }
    }

    class winEvent extends WindowAdapter {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void windowClosing(final WindowEvent e) {
        	if(taskObject!=null){
                recordTime();
            }
            storeFile();
            System.exit(0);
        }
    }
}
