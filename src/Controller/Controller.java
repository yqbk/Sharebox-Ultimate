/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.Login;
import Model.Model;
import View.Workspace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Class that react to user activities
 * @author Jakub Syrek
 */
public class Controller 
{    
    private Login login;
    private Model model;
    private Workspace workspace;    
    private String user;
    
    /**
     * Constructor of class, initiate login window, create model and add listeners
     */
    public Controller() 
    {        
        initLogin();        
        this.model = new Model();
    }
    
    /**
     * initialize login window
     */
    private void initLogin() 
    {
        this.login = new Login();
        login.setVisible(true);       			
        this.login.loginListener(new loginListener());
    }
    
    /**
     * initialize workspace window
     */
    private void initWorkspace(String loginStr)
    {
        this.user = this.login.getLogin();
        this.workspace = new Workspace(loginStr);
        login.setVisible(false);                
        workspace.setVisible(true);
        
        this.workspace.uploadListener(new UploadListener(this.user, workspace));	
        this.workspace.downloadListener(new DownloadListener(this.user));
        this.workspace.shareListener(new ShareListener(this.user));
        this.workspace.changeLoginListener(new changeLoginListener(this.user));
        this.workspace.changePasswordListener(new changePasswordListener(this.user));
        //this.workspace.shareListener(new DownloadListener(this.user));
    }
    
    /**
     * Check if user with given password is in database
     */
    class loginListener implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String loginStr = login.getLogin();
            String passwdStr = login.getPassword();
            
            if (model.checkUser(loginStr, passwdStr)) 
            {
                initWorkspace(loginStr);
            }
            else 
            {	
                login.resetTextFields();            
                JOptionPane.showMessageDialog(null,            
                        "Incorrect user or password!",                    
                        "Error",				                    
                        JOptionPane.ERROR_MESSAGE);   
            }
        }
    }

//------------------------------------------------------------------------------
    
    /**
     * Upload file if button clicked
     */
    class UploadListener implements ActionListener 
    {
        private String user;
        private Workspace workspace;
        
        public UploadListener(String user, Workspace workspace)
        {
            this.user = user;
            this.workspace = workspace;
        }
        public void actionPerformed(ActionEvent e) 
        {	
            model.upload(this.user);            
            workspace.setVisible(false);            
            initWorkspace(user);
        }	
    }
	
    /**
     * Download file if button clicked
     */
    class DownloadListener implements ActionListener 
    {
        private String user;
        
        public DownloadListener(String user)
        {
            this.user = user;
        }
        public void actionPerformed(ActionEvent e) 
        {	
            model.download(this.user);	
        }	
    }
    
    /**
     * Share file if button clicked
     */
    class ShareListener implements ActionListener 
    {
        private String user;
        
        public ShareListener(String user)
        {
            this.user = user;
        }
        public void actionPerformed(ActionEvent e) 
        {	
            model.share(this.user);	
        }	
    }
    
//------------------------------------------------------------------------------
    
    /**
     * Change login of user if button clicked
     */
    class changeLoginListener implements ActionListener 
    {
        private String user;
        
        public changeLoginListener(String user)
        {
            this.user = user;
        }
        public void actionPerformed(ActionEvent e) 
        {                    
            model.changeLogin(this.user);	
        }	
    }
    
    /**
     * Change password of user if button clicked
     */
    class changePasswordListener implements ActionListener 
    {
        private String user;
        
        public changePasswordListener(String user)
        {
            this.user = user;
        }
        public void actionPerformed(ActionEvent e) 
        {                    
            model.changePassword(this.user);	
        }	
    }
        
}
