/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import java.io.File;

/**
 *
 * @author leonardo
 */
public interface IFileParser<T> {
    T parse(File input) ;
}
