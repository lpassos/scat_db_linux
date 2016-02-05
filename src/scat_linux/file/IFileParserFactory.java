/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

/**
 *
 * @author leonardo
 */
public interface IFileParserFactory<T> {
    IFileParser<T> createParser() ;
}
