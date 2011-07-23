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


package org.griphyn.cPlanner.code;

import org.griphyn.cPlanner.classes.SubInfo;

import org.griphyn.cPlanner.common.PegasusProperties;

/**
 * The interface that defines the creation of a POSTSCRIPT for a job.
 * The POSTSCRIPT created is dependant on the GridStart being used to enable
 * the job.
 *
 * @author Karan Vahi vahi@isi.edu
 * @version $Revision$
 */

public interface POSTScript {

    /**
    * The version number associated with this API of GridStart.
    */
   public static final String VERSION = "1.0";


    /**
     * Initialize the POSTScript implementation.
     *
     * @param properties the <code>PegasusProperties</code> object containing all
     *                   the properties required by Pegasus.
     * @param path       the path to the POSTScript on the submit host.
     * @param submitDir  the submit directory where the submit file for the job
     *                   has to be generated.
     */
    public void initialize( PegasusProperties properties,
                            String path,
                            String submitDir );

    /**
     * Constructs the postscript that has to be invoked on the submit host
     * after the job has executed on the remote end. The postscript usually
     * works on the output generated by the executable that is used to grid
     * enable a job, and has been piped back by Condor.
     * <p>
     * The postscript should be constructed and populated as a profile
     * in the DAGMAN namespace.
     *
     *
     * @param job  the <code>SubInfo</code> object containing the job description
     *             of the job that has to be enabled on the grid.
     * @param key  the <code>DAGMan</code> profile key that has to be inserted.
     *
     * @return boolean true if postscript was generated,else false.
     */
    public boolean construct( SubInfo job, String key ) ;

    /**
     * Returns a short textual description of the implementing class.
     *
     * @return  short textual description.
     */
    public String shortDescribe();


}