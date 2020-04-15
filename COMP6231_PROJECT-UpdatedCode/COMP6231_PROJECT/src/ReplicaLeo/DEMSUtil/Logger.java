package DEMSUtil;

import DEMSUtil.FileManager;

// Logger singleton
public class Logger
{
    // Constructor
    private Logger()
    {
        if(_FileManager == null)
        {
            _FileManager = new FileManager();
        }
    }

    // Log message in file
    public void LogMessage(String msg, String file)
    {
        // Make sure file exists
        _FileManager.CreateFile(file);

        // Write to it
        _FileManager.WriteLineToFile(msg, file);
    }

    public void LogMessage(String msg, String file, String folder) {
        // Make sure file exists
        _FileManager.CreateFile(file, folder);

        // Write to it
        _FileManager.WriteLineToFile(msg, folder + "/" + file);
    }

    // Get singleton instance
    public static Logger GetInstance()
    {
        // Create new instance if its null
        if(_instance == null)
        {
            _instance = new Logger();
        }

        return _instance;
    }
    
    // Private attributes
    private static FileManager _FileManager = new FileManager();
    private static Logger _instance = new Logger(); 
}
