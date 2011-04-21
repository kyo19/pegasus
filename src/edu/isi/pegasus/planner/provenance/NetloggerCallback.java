/**
 *  Copyright 2007-2008 University Of Southern California
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package edu.isi.pegasus.planner.provenance;

import edu.isi.pegasus.common.logging.LogManagerFactory;
import edu.isi.pegasus.common.logging.LogManager;


import edu.isi.pegasus.planner.visualize.Callback;

import edu.isi.pegasus.planner.invocation.StatInfo;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import edu.isi.pegasus.planner.invocation.HasText;
import edu.isi.pegasus.planner.invocation.Stamp;
import edu.isi.pegasus.planner.invocation.Uname;
import edu.isi.pegasus.planner.invocation.Machine;
import edu.isi.pegasus.planner.invocation.MachineSpecific;
import edu.isi.pegasus.planner.invocation.MachineInfo;

/**
 * Implements callback interface to calculate space usage.
 *
 * @author not attributable
 * @version 1.0
 */

public class NetloggerCallback implements Callback {

  
    /**
     * The prefix for machine information keys
     */
    public static final String MACHINE_INFO_PREFIX = "job.machine.";
    
    /**
     * The logical site where the job was run.
     */
    protected String mSite;

    
    /**
     * The main job whose record is being parsed.
     */
    protected String mMainJob;

    /**
     * The handle to the logger.
     */
    protected LogManager mLogger;
    
    /**
     * The Map of key value pairs that are to be logged via Netlogger.
     */
    protected Map<String,String> mInvocationMap;
    
    
    /**
     * List of Invocation maps.
     */
    protected List<Map<String,String>> mInvocationList;
    
    /**
     * The counter to track the number of invocation records.
     */
    protected int counter;

    /**
     * The default constructor.
     */
    public NetloggerCallback() {
        mLogger =  LogManagerFactory.loadSingletonInstance();
        mInvocationList   = new LinkedList<Map<String,String>>();
        counter = 0;
    }

    /**
     * Initializes the callback.
     *
     * @param directory   the directory where all the files reside.
     * @param useStatInfo  boolean indicating whether to use stat info or not.
     */
    public void initialize( String directory , boolean useStatInfo){

    }


    /**
     * Callback for the starting of an invocation record.
     *
     * @param job      the job/file being parsed.
     * @param resource  the site id where the job was executed.
     */
    public void cbInvocationStart( String job, String resource) {
        counter ++;
        mInvocationMap    = new LinkedHashMap<String,String>();
        mInvocationMap.put( "job.resource", resource );
    }


    public void cbStdIN(List jobs, String data) {

    }


    public void cbStdOut(List jobs, String data) {
    }

    public void cbStdERR(List jobs, String data) {

    }

    /**
     * Callback function for when stat information for an input file is
     * encountered. Empty for time being.
     *
     * @param filename  the name of the file.
     * @param info      the <code>StatInfo</code> about the file.
     *
     */
    public void cbInputFile( String filename, StatInfo info ){
        
    }

    /**
     * Callback function for when stat information for an output file is
     * encountered. The size of the file is computed and stored.
     *
     * @param filename  the name of the file.
     * @param info      the <code>StatInfo</code> about the file.
     *
     */
    public void cbOutputFile( String filename, StatInfo info ){
       
    }


    /**
     * Callback signalling that an invocation record has been parsed.
     * Stores the total compute size, somewhere in the space structure
     * for the jobs.
     *
     *
     */
    public void cbInvocationEnd() {
        mInvocationList.add(mInvocationMap);
    }

    /**
     * Returns a List of Map objects where each map captures information in one
     * invocation record.
     *
     * @return List<Map<String,String>> 
     */
    public Object getConstructedObject() {
        return mInvocationList;
    }


   /**
    * Callback signalling that we are done with the parsing of the files.
    */
   public void done(){
       for( Map m : mInvocationList ){
         for( Iterator<String> it = m.keySet().iterator(); it.hasNext() ; ){
            String key = it.next(); 
            //System.out.println( key + "   "  + mInvocationMap.get(key) );
         }
       }
      
   }

   
   /**
     * Callback for the metadata retrieved from the kickstart record.
     * 
     * @param metadata
     */
    public void cbMetadata( Map metadata ){
        mInvocationMap.put( "job.counter", Integer.toString(counter) );
        mInvocationMap.put( "job.exitcode", 
                   getListValueFromMetadata( metadata, "exitcodes" ) );
        mInvocationMap.put( "job.executable",
                    getListValueFromMetadata( metadata, "executables" ) );
        mInvocationMap.put( "job.arguments",
                   getListValueFromMetadata( metadata, "arguments") );
        mInvocationMap.put( "job.directory", metadata.get( "directory" ).toString() );
        mInvocationMap.put( "job.duration", metadata.get( "duration" ).toString() );
        mInvocationMap.put( "job.hostname", metadata.get( "hostname" ).toString() );
        mInvocationMap.put( "job.hostaddress", metadata.get( "hostaddr" ).toString() );
        mInvocationMap.put( "job.user", metadata.get( "user" ).toString() );
        mInvocationMap.put( "job.starttime", metadata.get( "start" ).toString() );
       
    }

  /**
   * Callback to pass the machine information on which the job is
   * executed. Iterates through the machine info objects and puts the
   * keys and values in internal map.
   * 
   * @param machine
   */
  public void cbMachine( Machine machine )
  {
    // go through the values in a Machine
    String prefix = MACHINE_INFO_PREFIX + machine.getElementName(); 

    // stamp element
    Stamp stamp = machine.getStamp(); 
    mInvocationMap.put( prefix + "." + stamp.getElementName(), 
			stamp.getValue() ); 

    // uname element
    Uname uname = machine.getUname(); 
    String specific = prefix + "." + uname.getElementName(); 
    if ( uname instanceof HasText ) {
      mInvocationMap.put( specific, uname.getValue() );
    }
    for ( Iterator<String> ai = uname.getAttributeKeysIterator(); ai.hasNext(); ) { 
      String akey = ai.next();
      mInvocationMap.put( specific + "." + akey, uname.get(akey) ); 
    }
    
    // machine-specific group
    MachineSpecific ms = machine.getMachineSpecific(); 
    specific = prefix + "." + ms.getElementName(); 

    for ( Iterator it = ms.getMachineInfoIterator(); it.hasNext(); ) {
      MachineInfo info = (MachineInfo) it.next();
      String key = specific + "." + info.getElementName(); 
            
      if ( info instanceof HasText ) {
	mInvocationMap.put( key, ((HasText)info).getValue() );
      }
      
      key += ".";
            
      // put in all the attribute key and values
      for ( Iterator<String> ai = info.getAttributeKeysIterator(); ai.hasNext() ; ){
	String akey = ai.next();
	mInvocationMap.put( key + akey , info.get(akey) );
      }
    }
  }
    
    /**
     * Returns the first value from the List values for a key
     * 
     * @param m
     * @param key
     * 
     * @return the first value.
     */
    private String getListValueFromMetadata(Map m, String  key ) {
        Object obj =  m.get( key );
        if( obj == null ){
            return "";
        }
        
        if( !( obj instanceof List )){
            throw new RuntimeException( "Value corresponding to key is not a List "  + key );
        }
        //in case of windward there are no pre job or postjobs
        return  ((List)obj).get( 0 ).toString();
    }
}