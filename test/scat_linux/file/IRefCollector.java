/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import java.io.File;
import java.util.Set;
import scat_linux.common.Reference;

/**
 *
 * @author leonardo
 */
public interface IRefCollector {
    Set<Reference> getRefs() ;
    File getFile() ;
}
