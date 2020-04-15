package DEMSUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class FileManager
{
    // Get/Create a file
    public File CreateFile(String fileName) {
        try {
            File newFile = new File(fileName);
            if (newFile.createNewFile()) {
                //System.out.println("File created: " + newFile.getName());
                _File = newFile;
            } else {
                //System.out.println("File already exists.");
                _File = null;
            }

            return _File;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    // Get/Create a file inside a folder path
    public File CreateFile(String fileName, String folder) {
        try {
            File newFile = new File(folder);
            if(newFile.mkdirs())
                return null;

            newFile = new File(folder + "/" + fileName);
            if (newFile.createNewFile()) {
                //System.out.println("File created: " + newFile.getName());
                _File = newFile;
            } else {
                //System.out.println("File already exists.");
                _File = null;
            }
            return _File;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    // Write to file
    public void WriteLineToFile(String line, String fileName) {
        try {
            FileWriter myWriter = new FileWriter(fileName, true);
            myWriter.write(line+"\n");
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Open file method
    public boolean OpenFile(String path) {
        try {
            _File = new File(path);
            _Reader = new Scanner(_File);
            return true;
        } catch (FileNotFoundException e) {
            //System.out.println("An error occurred.");
            //e.printStackTrace();
            return false;
        }
    }

    // Close current open file
    public void CloseFile()
    {
        _Reader.close();
    }

    // Read from file
    public String ReadLineFromFile()
    {
        if (_Reader.hasNextLine())
        {
            return _Reader.nextLine();
        }

        return null;
    }

    private Scanner _Reader;
    private File _File;
}