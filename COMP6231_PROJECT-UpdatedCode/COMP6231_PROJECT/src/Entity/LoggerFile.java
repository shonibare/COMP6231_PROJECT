package Entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerFile {
	 public final Logger logger = Logger.getLogger(LoggerFile.class
	            .getName());
	    private FileHandler fh = null;

	    public LoggerFile() {
	        //just to make our log file nicer :)
	        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
	        try {
	            fh = new FileHandler("C:\\Users\\Shonibare\\eclipse-workspace\\EMS_Project_CORBA/logs/Mylogger"
	                + format.format(Calendar.getInstance().getTime()) + ".log");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        fh.setFormatter(new SimpleFormatter());
	        logger.addHandler(fh);
	        logger.setUseParentHandlers(false);
	    }

}
