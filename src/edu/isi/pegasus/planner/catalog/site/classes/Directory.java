/*
 * 
 *   Copyright 2007-2008 University Of Southern California
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 */

package edu.isi.pegasus.planner.catalog.site.classes;

import java.io.IOException;
import java.io.Writer;

/**
 * The Directory class used for Site Catalog Schema version 4 onwards.
 * The type of directory is determined based on type attribute rather than having
 * separate classes for it.
 *
 * @author Karan Vahi
 *
 * @version $Revision$
 */
public class Directory extends DirectoryLayout{

    @Override
    public void accept(SiteDataVisitor visitor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    /**
     * Enumerates the new directory types supported in this schema
     */
    public enum TYPE {
        shared_scratch( "shared-scratch" ), shared_storage( "shared-storage" ),
        local_scratch( "local-scratch" ), local_storage( "local-storage" ) ;


        public static TYPE value( String name ){
            return TYPE.valueOf( name.replaceAll("-", "_") );
        }

        private String mValue;

        /**
         * The constructor
         *
         * @param value  the string value to return
         */
        TYPE( String value ){
            mValue = value;
        }

        /**
         * The value associated with the enum
         *
         * @return
         */
        private String getValue( ){
            return this.mValue;
        }

        /**
         * Override of the toString method to return
         * @return
         */
        public String toString(){
            return this.getValue();
        }


    }

    /**
     * The type of directory
     */
    private TYPE mType;


    /**
     * Set the type of directory
     *
     * @param  type   the type of directory
     */
    public void setType( String type ){
        mType = TYPE.value( type );
    }

    /**
     * Set the type of directory
     *
     * @param  type   the type of directory
     */
    public void setType( Directory.TYPE type ){
        mType =  type ;
    }

    /**
     * Set the type of directory
     *
     * @return  the type of directory
     */
    public TYPE getType(  ){
        return mType;
    }



    /**
     *
     * @param writer
     * @param indent
     * @throws IOException
     */
    public void toXML(Writer writer, String indent) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns the clone of the object.
     *
     * @return the clone
     */
    public Object clone(){
        Directory obj;
        obj = ( Directory ) super.clone();

        obj.setType( this.getType() );

        return obj;
    }

    
}