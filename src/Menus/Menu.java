/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Menus;

import java.sql.SQLException;
import vistas.*;

/**
 *
 * @author del
 */
public class Menu {
    
    public static void main(String [] args) throws SQLException
    {
        Login2 lg = new Login2();
        lg.setVisible(true);
    }
}
