/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.command;

import command.Command;
import git.Git;
import gsd.utils.io.Debug;
import gsd.utils.io.FileUtils;
import gsd.utils.numeric.SafeLong;
import gsd.utils.sql.ConnectionInfo;
import gsd.utils.sql.DBConnection;

import gsd.utils.sql.ScriptRunner;
import gsd.utils.ipc.ScriptUtils;
import gsd.utils.predicate.Continue;
import gsd.utils.predicate.Predicate;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Properties;
import scat_linux.env.ScatDBProperties;


import static gsd.utils.sql.DBUtils.*;
import gsd.utils.string.Separator;
import java.util.HashSet;
import java.util.Set;
import kconfig_data.common.ProjectInfo;
import scat_linux.common.LinuxProject;
import scat_linux.common.LinuxSubsystem;
import scat_linux.common.Reference;

import scat_linux.file.build.BuildFile;
import scat_linux.file.c.CFile;
import scat_linux.file.kconfig.KconfigFile;
import scat_linux.file.LinuxFile;
import scat_linux.file.kconfig.KconfigAttr;
import scat_linux.file.kconfig.KconfigEntry;
import scat_linux.file.kconfig.KconfigEntryType;
import static scat_linux.file.kconfig.KconfigEntryType.T_CHOICE;
import static scat_linux.file.kconfig.KconfigEntryType.T_CONFIG;
import static scat_linux.file.kconfig.KconfigEntryType.T_MENUCONFIG;

import static scat_linux.utils.ShellScripts.*;

/**
 *
 * @author leonardo
 */
public class MakeDatabase extends Command<String> {
    
    private String release ;
    private Debug debug ;
    private boolean revert ;
    
    public MakeDatabase() {
    }

    @Override
    public String getName() {
        return "--makedb" ;
    }

    @Override
    protected String getHelp() {
        return "creates a basic database of scattering" ;
    }

    @Override
    protected String getUsage() {
        return "[-p | -nr] <release>" ;
    }

    @Override
    public void parseArguments(String... args) throws Exception {
        
        /* By default, debug (printing debug messages) is set to false */
        debug = new Debug(false) ;
        
        /* Sets revert to default branch to true (default). */
        revert = true ;
        
        /* Verifies that the command call has at most two parameters, and 
         * at least one
         */
        if (args.length < 1 || args.length > 3)
            throw new Exception("Incorrect number of arguments") ;
                           
        /* Gets last argument (release tag, which is mandatory) */
        this.release = args[args.length - 1] ;                
        
        /* If there are pending args, check them out */        
        for(int i = args.length - 2; i >= 0; i--) {
            
            /* Ok, one more argument exists. Make sure it refers to -p */
            
            switch(args[i]) {
                case "-p":
                    debug = new Debug(true);    
                    break ;
                    
                case "-nr":
                    /* No revert */
                    revert = false ;
                    break ;
                    
                default:
                    throw new Exception("Unknown parameter " + args[i]) ;
            }                            
        }
    }    

    private Connection makeDBReady() throws Exception {                    
        
        Connection conn = null;
        
        try(Connection postgreDBConnection = 
                DBConnection.get(PostgreSQLConnectionInfo.getInstance())) {                                    
            
            if (! databaseExists(postgreDBConnection)) {
                
                try {                    
                    /* DB does not exist. Create it. */                
                    createDatabase(postgreDBConnection) ;             
                    
                    /* Ok, proceed to create schema */
                    conn = DBConnection.get(ScatDBConnectionInfo.getInstance()) ;
                    conn.setAutoCommit(false);                    
                    
                    /* Creates the DB schema */
                    createSchema(conn) ;
                                                    
                    /* Populates subsystems */
                    insertSubsystems(conn) ;  
                    conn.commit() ;                    
                    
                } catch(Exception e) {
                    
                    if (conn != null)
                        conn.close();                    
                    
                    dropDatabase(postgreDBConnection);
                    
                    throw e ;
                }
            }
            else {
                conn = DBConnection.get(ScatDBConnectionInfo.getInstance()) ;
                conn.setAutoCommit(false);
            }                        
        } 
        
        return conn ;
    }
    
    private void dropDatabase(Connection c) throws Exception {
        c.createStatement().execute("drop database " + ScatDBConnectionInfo.getInstance().getDBName());
    }    
    
    @Override
    public String run() throws Exception {               
        
        debug.printMsg("Cloning repository");
        
        Git git = LinuxProject.getInstance().getGitRepo() ;
        
        String currentBranch = git.getCurrentBranch() ;        
                
        try(Connection conn = makeDBReady()) {                        
            
            populateDB(conn, git);            
            conn.commit(); 
        
        } finally {
            
            if (revert)
                git.checkout(currentBranch, true);
        }
        
        return "Database successfully created." ;
    }
        
    private boolean databaseExists(Connection c) throws Exception {  
        return exists(c, "pg_database", "datname", dbStr(ScatDBConnectionInfo.getInstance().getDBName()));
    }
    
    private void createDatabase(Connection c) throws Exception {
        c.createStatement().execute("create database " + ScatDBConnectionInfo.getInstance().getDBName());
    }
    
    private void createSchema(Connection c) throws Exception {
        ScriptRunner script = new ScriptRunner(c, true, true) ;    
        script.setLogWriter(new PrintWriter(FileUtils.createFileInSystemTemp("makedb_out.log"))) ;
        script.setErrorLogWriter(new PrintWriter(FileUtils.createFileInSystemTemp("makedb_err.log"))) ;
        script.runScript(new FileReader(ScriptUtils.find(ScatDBProperties.getSqlScriptsDir(), this.getClass(), "createdb.sql"))) ;        
    }
    
    private void populateDB(final Connection conn, final Git git) throws Exception { 
  
        debug.printMsg("Checking out release " + release);

        git.runCommand(Git.formatCommand("clean", "-f -d")) ;
        git.checkout(release, true);            

        SafeLong releaseId = insertRelease(conn, release) ;                                    

        insertFiles(conn, git.getRepository(), releaseId);
              
        conn.commit() ;
    }    

    private SafeLong insertRelease(final Connection conn, final String release) throws Exception {
        insert(conn, "release", "name", dbStr(release));
        return new SafeLong(getLastId(conn, "release")) ;
    }        

    private void insertFiles
            (final Connection conn, final File gitRepo, 
             final SafeLong releaseId) throws Exception {
        
        final Exception[] ex = new Exception[1] ;
        
        Continue cont = new Continue() {

            @Override
            public boolean stop() {
                return ex[0] != null ;
            }
        } ;
        
        Set<File> ignore = new HashSet<>() ;
        ignore.add(new File(gitRepo.getAbsolutePath() + Separator.DIR_PATH + ".git")) ;

        
        FileUtils.findFiles(gitRepo, new Predicate<File>() {          
            
            @Override
            public boolean holds(File f) { 
                try {                                         
                    debug.printMsg("  Inserting file: " + f.getAbsolutePath());
                    insertFile(conn, releaseId, gitRepo, f) ;                                        
                } catch(Exception e) {
                    
                    // An exception has occurred. Save it and 
                    // stop traversing files.

                    ex[0] = e ;
                    
                    return false ;
                }
                
                // No exception has occurred. Continue traversing files.
                return true ;                
            }
        }, cont, ignore) ;
        
        if (ex[0] != null)
            throw ex[0] ;
    }    
        
    private void insertFile(final Connection conn, 
                       final SafeLong releaseId, 
                       final File gitRepo,
                       final File f) throws Exception {
        
        
        /* Attempts to retrieve the given file */
        SafeLong fileInfoId = getFileInfoId(conn, gitRepo, f) ;
        
        /* If there is no information to the given file, 
         * insert it. Since the lack of a file information
         * also implies the inexistent of a file record 
         * associated with the current release,
         * we should insert such a record.
         */
        if (fileInfoId == null)
            fileInfoId = insertFileInfo(conn, gitRepo, f) ;
        
            
        /* File information exists. In that case, just insert
         * a new file record and tie it to the retrieved 
         * file info.
         */            

        insertFile(conn, f, fileInfoId, releaseId) ;
           
    }         
    
    private SafeLong getFileInfoId
            (final Connection conn, 
            final File gitRepo, 
            final File f) throws Exception {
        return new SafeLong(getId(conn, "file_info", "path", dbStr(FileUtils.getRelativePath(gitRepo, f)))) ;        
    }  
    
    private SafeLong insertFileInfo
            (final Connection conn, 
             final File gitRepo, 
             final File f) throws Exception {
        
        String relativeFile = FileUtils.getRelativePath(gitRepo, f) ;
        SafeLong subsystemId = getSubsystem(conn, relativeFile) ;        
                
        insert(conn, "file_info", 
                Arrays.asList("path", "fk_subsystem"), 
                Arrays.asList(dbStr(relativeFile), subsystemId));
        
        SafeLong fileInfoId = new SafeLong(getLastId(conn, "file_info")) ;
        return fileInfoId ;
    }      
    
    private void insertFile (final Connection conn, 
                             final File f, 
                             final SafeLong fileInfoId, 
                             final SafeLong releaseId) throws Exception {      
        
        insert(conn, "file", 
               Arrays.asList("fk_file_info", "fk_release"), 
               Arrays.asList(fileInfoId,      releaseId)) ; 
        
        SafeLong fileId = new SafeLong(getLastId(conn, "file")) ;         
                
        if (CFile.matches(f)) {
                                 
            debug.printMsg("      C file found. Formating it");               
            
            CFile cFile = CFile.process(f) ;
            insert(conn, "c_source_file", 
                    Arrays.asList("fk_file", "sloc"), 
                    Arrays.asList(fileId, cFile.getSloc())) ;                                     
            
            insertRefs(conn, cFile, fileId) ;
            return ;
        }
        
        if (BuildFile.matches(f)) {
            
            debug.printMsg("      Build file found. Formating it");               
            
            BuildFile buildFile = BuildFile.process(f) ;                    
            insert(conn, "build_file", "fk_file", fileId) ;   
            
            insertRefs(conn, buildFile, fileId) ;           
            return ;
        }
        
        if (KconfigFile.matches(f)) {
            
            debug.printMsg("      Kconfig file found. Formating it");               
            
            KconfigFile kconfigFile = KconfigFile.process(f) ;            
            
            insert(conn, "kconfig_file", "fk_file", fileId) ;       
            
            insertFeatures(conn, kconfigFile, fileId);                                                   
            insertRefs(conn, kconfigFile, fileId) ;                        
            return ;
        }
        
        insert(conn, "other_file", "fk_file", fileId) ;
    }
    
    private void insertSubsystems(final  Connection conn) throws Exception {
        for(LinuxSubsystem subsystem : LinuxSubsystem.values()) {
            insert(conn, "subsystem", "name", dbStr(subsystem.toString()));
        }
    }

    private void insertFeatures
            (final Connection conn, 
             final KconfigFile kconfig, 
             final SafeLong kconfigFileId) throws Exception { 
        
        debug.printMsg("  Inserting features");        
        
        for (KconfigEntry entry : kconfig.getModel()) {            
            insertFeatures(conn, entry, kconfigFileId);
        }
           
    }
    
    private void insertFeatures(final Connection conn,
                                final KconfigEntry entry, 
                                final SafeLong kconfigFileId) throws Exception {

        
        /* Checks whether entry exports macro.
         * If so, insert it as a feature.
         */        

        if (entry.getType().equals(KconfigEntryType.T_CONFIG) ||
            entry.getType().equals(KconfigEntryType.T_MENUCONFIG) ||
            (entry.getType().equals(KconfigEntryType.T_CHOICE) &&
             entry.getName().length() > 0)) {            
            
            String featureName = entry.getName() ;
            String featureType = getEntryType(entry) ;
            String featureDataType = getEntryDataType(entry) ;            
            
            /* This is very trick...
             * If a feature misses a data type, then there must exist a 
             * feature declaration somewhere that matches the same name
             * and contains a data type definition. Since that is the 
             * original 'feature', that should be the one accounting for
             * any scattering.
             * In this case, features missing the data type are not put
             * in the database!
             * 
             * Quoting the Kconfig language manual:
             * 
             * "A config option can be defined multiple times with the same
             * name, but every definition can have only a single input prompt 
             * and the type must not conflict."
             * 
             * Example: release v2.6.12, arch/alpha/Kconfig
             */
            if (featureDataType != null &&
                ! existsFeature(conn, featureName, featureType, 
                                featureDataType, kconfigFileId)) {
                
                debug.printMsg("         Inserting feature " + featureName);
                insertFeature(conn, featureName, featureType, featureDataType, kconfigFileId) ;
            }
            
        }

        /* Traverse children */
        for(KconfigEntry child : entry.getEntries()) {
            insertFeatures(conn, child, kconfigFileId) ;
        }   
    }
    
    private String getEntryType(final KconfigEntry entry) {
            
        switch(entry.getType()) {

            case T_CHOICE:
                return "CH" ;

            case T_CONFIG:
                return "CO" ;

            case T_MENUCONFIG:
                return "MC" ;
                
            default:
                return null ;
        }          
    }
    
    private String getEntryDataType(final KconfigEntry entry) {
        
        String dataType = null ;
        
        attr_iterator:
        for(KconfigAttr attr : entry.getAttributes()) {
            
            switch(attr.getType()) {
                
                case T_BOOLEAN:
                case T_DEF_BOOLEAN:
                    dataType = "BO";
                    break attr_iterator ;

                case T_TRISTATE:                
                case T_DEF_TRISTATE:
                    dataType = "TR" ;
                    break attr_iterator ;                    

                case T_HEX:
                    dataType = "HE" ;
                    break attr_iterator ;                    
                    
                case T_INT:
                    dataType = "IN" ;
                    break attr_iterator ;                    
                    
                case T_STRING:
                    dataType = "ST" ;
                    break attr_iterator ;                    

                default:
                    continue ;               
            }
        }
        return dataType ;
    }
    
    private boolean existsFeature
            (final Connection conn, 
             final String featureName,
             final String featureType,
             final String featureDataType,
             final SafeLong kconfigFileId) throws Exception {
        
       return exists(conn, 
                    "feature", 
                    Arrays.asList("name", 
                                  "fk_kconfig_file", 
                                  "fk_feature_type", 
                                  "fk_feature_data_type"), 
                    Arrays.asList(dbStr(featureName), 
                                  kconfigFileId,
                                  dbStr(featureType),
                                  dbStr(featureDataType))) ;        
    }
    
    private void insertFeature
            (final Connection conn, 
             final String featureName, 
             final String featureType,
             final String featureDataType,            
             final SafeLong kconfigFileId) throws Exception {
        
        insert(conn, "feature", 
                Arrays.asList("name", 
                              "fk_kconfig_file", 
                              "fk_feature_type",
                              "fk_feature_data_type"), 
                Arrays.asList(dbStr(featureName), 
                               kconfigFileId, 
                               dbStr(featureType), 
                               dbStr(featureDataType)));        
    }
    

    private void insertRefs(Connection conn, LinuxFile f, SafeLong fileId) throws Exception {
        
        debug.printMsg("    Inserting refs in " + f.getFile()); 
        
        for(Reference ref : f.getRefs()) {                        
            
            insert(conn, "feature_ref", 
                    Arrays.asList("name", "context", "line"), 
                    Arrays.asList(
                        dbStr(ref.getReferredFeature()), 
                        dbStr(ref.getContext()), 
                        ref.getLine()
                    )
                  );
            
            SafeLong featureRefId = new SafeLong(getLastId(conn, "feature_ref"));

            String fileType = f.fileType() ;
            insert(conn, fileType + "_ref", 
                      Arrays.asList("fk_feature_ref", "fk_" + fileType + "_file"),
                      Arrays.asList(featureRefId, fileId)) ;                         

            if (f instanceof CFile && CFile.isIfDef(ref.getContext())) {               
                insert(conn, "ifdef_ref", "fk_c_source_ref", featureRefId) ;
            }
        }
    }


    private SafeLong getSubsystem(Connection conn, String relativeFileName) throws Exception {
        String subsystemName = mapFileToSubsystem(relativeFileName).toString() ;        
        return new SafeLong(getId(conn, "subsystem", "name", dbStr(subsystemName))) ;
    }
}
