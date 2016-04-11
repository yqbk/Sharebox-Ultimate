/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sharebox;

import Controller.Controller;
import View.Login;
import View.Workspace;

/**
 *  Main class of Sharebox-Ultimate project
 * @author Jakub Syrek
 */
public class ShareBox 
{
    static Controller controller;    
    
    /**
     * @param args the command line arguments
     */    
    public static void main(String[] args) 
    {        
        controller = new Controller();
    }
}
