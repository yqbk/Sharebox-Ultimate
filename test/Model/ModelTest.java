/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jakub Syrek
 */
public class ModelTest {
    
    /**
     * Test of checkUser method, of class Model.
     */
    @Test
    public void testCheckUser() 
    {
        System.out.println("checkUser");
        String login = "wrong";
        String passwd = "user";
        Model instance = new Model();
        boolean expResult = false;
        boolean result = instance.checkUser(login, passwd);
        
        assertEquals(expResult, result); 
    }


    /**
     * Test of getOS method, of class Model.
     * annotation: Works only on windows - for unix need to change test
     */
    @Test
    public void testGetOS() 
    {
        System.out.println("getOS");
        Model instance = new Model();
        String expResult = "windows"; //->change to "unix" on unix system
        String result = instance.getOS();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of changeDatabase method, of class Model.
     */
    @Test    
    public void changeDatabase() 
    {
        System.out.println("changeDatabase");
        Model instance = new Model();
        String test = "test";
        instance.changeDatabaseModel(test, 0, "kate");
        
        assertTrue(instance.checkUserName(test));
    }
    
    /**
     * Cleaning after tests
     */
    @After
    public void tearDown() 
    {
        Model instance = new Model();
        String test = "kate";
        instance.changeDatabaseModel(test, 0, "test");
    }
}
