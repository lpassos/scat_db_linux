/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scat_linux ;

import command.Command;
import scat_linux.command.MakeDatabase;
import command.main.CommandExecutor;

/**
 *
 * @author leonardo
 */
public class ScatLinuxDB {
    
    private static Command[] commands = {
        new MakeDatabase()
    } ;
    
    
    public static void main(String[] args) {
        CommandExecutor.run("scat_linux_db", commands, args);
    }
}
