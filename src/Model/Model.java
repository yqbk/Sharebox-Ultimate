/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Model class of project - general logic of program
 * @author Jakub Syrek
 */
public class Model {
    
    private static final File USER_PASSWORD_UNIX = new File("server/userList.txt");	
    private static final File USER_PASSWORD_WINDOWS = new File("server\\userList.txt");
    
    /**
    * Set reader path depending on operating system
    * @return reader adjusted to OS
    */
    public BufferedReader setReader() throws IOException
    {
        if(getOS().equals("windows")) 
            return new BufferedReader(new FileReader(USER_PASSWORD_WINDOWS));				
        else 
            return new BufferedReader(new FileReader(USER_PASSWORD_UNIX));
    }

    /**
    * Set writer path depending on operating system
    * @return writer adjusted to OS
    */
    public PrintWriter setWriter() throws IOException
    {
        if(getOS().equals("windows")) 
            return new PrintWriter(new FileWriter(USER_PASSWORD_WINDOWS));				
        else 
            return new PrintWriter(new FileWriter(USER_PASSWORD_UNIX));
    }
    
    public JFileChooser setChooser(String user)
    {
        if(getOS().equals("windows"))
            return new JFileChooser("server\\" + user);
	else 
            return new JFileChooser("server/" + user);
    }
            
    
    /**
    * Check for user existence
    * @return boolean value - user with given password exist or not
    */
    public boolean checkUser(String login, String passwd) 
    {
        String[] tokens;
	try 
        {
            BufferedReader reader = setReader();
            String line = null; 
	
            while ((line = reader.readLine()) != null) 
            {
                String[] str = line.split("/"); 
		
                if(login.equals(str[0]) && passwd.equals(str[1])) 
                {	
                    reader.close();		
                    return true;		
                }		
            }
           
            reader.close();	
            return false;
	
        } 
        catch (IOException ex) 
        {   	
            JOptionPane.showMessageDialog(null,    	
                    "Error while accesing database. Terminating",    		 
                    "Error",    		 
                    JOptionPane.ERROR_MESSAGE);   
            
            System.exit(0);	
        }	
        return false;	
    }
    
    /**
    * Check for user existence
    * @param login name of searched user
    * @return boolean value user with given password exist or not
    */
    public boolean checkUserName(String login) 
    {
        String[] tokens;
	try 
        {
            BufferedReader reader = setReader();
            String line = null; 
	
            while ((line = reader.readLine()) != null) 
            {
                String[] str = line.split("/"); 
		
                if(login.equals(str[0])) 
                {	
                    reader.close();		
                    return true;		
                }		
            }
           
            reader.close();	
            return false;
	
        } 
        catch (IOException ex) 
        {   	
            JOptionPane.showMessageDialog(null,    	
                    "Error while accesing database. Terminating",    		 
                    "Error",    		 
                    JOptionPane.ERROR_MESSAGE);   
            
            System.exit(0);	
        }	
        return false;	
    }

    /**
    * Check type of Operating System
    * @return String with information about OS type
    */
    public String getOS()
    {
        String osType = System.getProperty("os.name").toLowerCase();
        if(osType.contains("windows"))                
            return "windows";
        else
            return "unix";
    }   
    
//----------------------------------
    
    /**
    * Copy file from source to destination
    * @param source - source path
    * @param target - target path
    */
    public void copy(File source , File target) throws IOException 
    {
	if(source.isDirectory())
        {
            if(!target.exists())
            {
		target.mkdir();
            }
			      
            String[] children = source.list();
            
            for(int i=0; i < children.length; i++) 
            {
		copy(new File(source, children[i]), new File(target, children[i]));
            }
	}
	else
        {            
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(target);
	
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
            
            in.close();
            out.close();		 
        }
    }
    
    /**
    * Upload file to user directory
    * @param user - logged user
    */
    public void upload(String user)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int val = chooser.showOpenDialog(null);        

        if(val == JFileChooser.APPROVE_OPTION)
        {
            try
            {        		
                File sourceFile = new File(chooser.getSelectedFile().getAbsolutePath());
                File targetFile;                
                
                if(getOS().equals("windows"))
                    targetFile = new File("server\\" + user + "\\" + chooser.getSelectedFile().getName());
                else
                    targetFile = new File("server/" + user + "/" + chooser.getSelectedFile().getName());

                copy(sourceFile, targetFile);

            } 
            catch (IOException e) 
            {	   
                JOptionPane.showMessageDialog(null, 	   	
                        "Error while uploading file. Terminating.", 	   		
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);	   		
                System.exit(0);
            }
        }
    } 
 
    /**
    * Download file to specific directory
    * @param user - logged user
    */
    public void download(String user) 
    {
	JFileChooser chooser = setChooser(user);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	
        int val = chooser.showOpenDialog(null);
	     
	if(val == JFileChooser.APPROVE_OPTION)
        {
            if(chooser.getSelectedFile().getAbsolutePath().contains("server/" + user + "/") || chooser.getSelectedFile().getAbsolutePath().contains("server\\" + user + "\\"))
            {
                JOptionPane.showMessageDialog(null, "Select destination", "Download", JOptionPane.DEFAULT_OPTION);
                JFileChooser destination = new JFileChooser();
                destination.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int valDest = destination.showOpenDialog(null);
                if(valDest == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        File sourceFile = new File(chooser.getSelectedFile().getAbsolutePath());
                        File targetFile;

                        if(getOS().equals("windows"))
                            targetFile = new File(destination.getSelectedFile().getAbsolutePath() + 
                                    "\\" + chooser.getSelectedFile().getName());
                        else 
                            targetFile = new File(destination.getSelectedFile().getAbsolutePath() + 
                                    "/" + chooser.getSelectedFile().getName());
                        
                        copy(sourceFile, targetFile);

                    } 
                    catch (IOException e) 
                    {	    	
                        JOptionPane.showMessageDialog(null, 	    		
                                    "Error during downloading process. Terminating.", 
                                    "Download Error", 	    		   	
                                    JOptionPane.ERROR_MESSAGE);

                        System.exit(0);
                    }   		 
                }	 
            }
            else
            {
                JOptionPane.showMessageDialog(null, 	    	
                            "Download error!", 	    		
                            "Download error", 	    		
                            JOptionPane.ERROR_MESSAGE);
            }   
        }
    }
    
    /**
    * Share file to other user
    * @param user - logged user
    */
    public void share(String user) 
    {
        String shareTo = (String)JOptionPane.showInputDialog(
                    null,
                    "To whom would you like to share file?",
                    "select user",
                    JOptionPane.OK_CANCEL_OPTION);
        
        if (checkUserName(shareTo) == true)
        {
            JOptionPane.showMessageDialog(
                    null,
                    shareTo,
                    shareTo + " selected",
                    JOptionPane.OK_OPTION);
        }
        else
        {
            JOptionPane.showMessageDialog(null, 	    	
                            "Share error! User not in database", 	    		
                            "Share error", 	    		
                            JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
        JFileChooser chooser = setChooser(user);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	
        int val = chooser.showOpenDialog(null);
	     
	if(val == JFileChooser.APPROVE_OPTION)
        {
            if(chooser.getSelectedFile().getAbsolutePath().contains("server/" + user + "/") || chooser.getSelectedFile().getAbsolutePath().contains("server\\" + user + "\\"))
            {
                try
                {
                    File sourceFile = new File(chooser.getSelectedFile().getAbsolutePath());
                    File targetFile;                

                    if(getOS().equals("windows"))
                        targetFile = new File("server\\" + shareTo + "\\" + chooser.getSelectedFile().getName());
                    else
                        targetFile = new File("server/" + shareTo + "/" + chooser.getSelectedFile().getName());

                    copy(sourceFile, targetFile);
                }
                catch (IOException e) 
                {	   
                    JOptionPane.showMessageDialog(null, 	   	
                            "Error while sharing file. Terminating.", 	   		
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);	   		
                    System.exit(0);
                }                
            }
            else
            {
                JOptionPane.showMessageDialog(null, 	    	
                            "share error!", 	    		
                            "share error", 	    		
                            JOptionPane.ERROR_MESSAGE);
            }   
        }
        
    }

    /**
    * Change user's login
    * @param user - logged user
    */
    public void changeLogin(String user) 
    {
        changeDatabase(0, user);        
    }

    /**
    * Change user's password
    * @param user - logged user
    */
    public void changePassword(String user) 
    {
        changeDatabase(1, user);        
    }
    
    
    /**
    * Change database file
    * @param type - type of change (user/password)
    * @param newStr - string to replace
    * @param user user name
    * @return typeStr type of changed value
    */
    public void changeDatabaseModel(String newStr, int type, String user)
    {
        String typeStr = "";
        
        switch (type)
        {
            case 0: typeStr = "login";
                    break;
            
            case 1: typeStr = "password";
                    break;
        }
        
        String[] tokens;
	try 
        {
            String line = null;
            String newLine = "";
            
            
            
            BufferedReader reader = setReader();            
            
            while ((line = reader.readLine()) != null) 
            {
                String[] str = line.split("/"); 
		
                if(user.equals(str[0])) 
                {
                    if (type == 0)
                    {
                        line = line.replace(str[0], newStr);
                        
                        File dir;
                        File newDir;
                        
                        if(getOS().equals("windows"))
                        {
                            dir = new File("server\\" + user);
                            newDir = new File("server/" + newStr);
                        }
                        else
                        {
                            dir = new File("server/" + user);   
                            newDir = new File("server/" + newStr);
                        }
                        
                        dir.renameTo(newDir);
                        
                    }
                    else
                        line = line.replace(str[1], newStr);
                }                
                
                newLine = newLine + line + "\n";                
            }
            
            PrintWriter writer = setWriter();
            writer.println(newLine);	
            writer.close();
            reader.close();
	
        }
        catch (IOException e) 
        {	                 
            JOptionPane.showMessageDialog(null,
                    "Error while changing " + typeStr + ". Terminating.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);	   
            System.exit(0);
        }
    }    
    
    /**
    * Change database file
    * @param type - type of change (user/password)
    * @param user - login of user
    */
    public void changeDatabase(int type, String user)
    {
        String typeStr = "";
        
        switch (type)
        {
            case 0: typeStr = "login";
                    break;
            
            case 1: typeStr = "password";
                    break;
        }
        
        String newStr = (String)JOptionPane.showInputDialog(
                    null,
                    "Choose new " + typeStr,
                    typeStr + " change",
                    JOptionPane.OK_CANCEL_OPTION);
        
        JOptionPane.showMessageDialog(
                    null,
                    newStr,
                    typeStr + " change",
                    JOptionPane.OK_OPTION);        
        
        changeDatabaseModel(newStr, type, user);        
    }
}