/*
 * uniBCM Project: an exams's management system for university, developed with Java RMI
 * 
 * Copyleft 2009  uniBCM Team(Alberto Cordioli, Andrea Oboe)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 *
 * E-Mail:	cordioli [dot] alberto [at] gmail [dot] com
 * 			oboe [dot] andrea [at] gmail [dot] com
 */

package lib;

import server.central.*;
import server.reg.*;
import java.io.*;

/**
 * Implementa una struttura  che contiene due stub (JRMP e IIOP)
 * @author uniBCM Team
 *
 */
public class Data_Structure implements Serializable {

	/**
	 * Descrittore della classe
	 */
	private static final long serialVersionUID = 1L;
	/** stub central server tipo jrmp */
	private Central jrmpCentral;
	/** stub central server tipo iiop */
	private Central iiopCentral;
	/** stub registration server tipo jrmp */
    private Registration jrmpReg;
   
    /**
     * Costruttore di default che setta tutto a null
     * @deprecated replaced by {@link #Data_Structure(Central, Central, Registration)}
     */
    public Data_Structure(){
		this.jrmpCentral = null;

		this.iiopCentral = null;

        this.jrmpReg = null;
   	}
    
    /**
     * Costruttore che prende di 3 stub come parametri
     * @param jrmpCentral stub al central server tipo jrmp
     * @param iiopCentral stub al central server tipo iiop
     * @param jrmpReg stub al registartion server tipo jrmp
     */
	public Data_Structure(Central jrmpCentral, Central iiopCentral, Registration jrmpReg){
		this.jrmpCentral = jrmpCentral;
		this.iiopCentral = iiopCentral;
        this.jrmpReg = jrmpReg;
   	}

   	/**
   	 * Restituisce lo stub al server centrale tipo jrmp
   	 * @return stub central server jrmp
   	 */
   	public Central getCentralDataJRMP(){
		return jrmpCentral;
   	}
   
   	/**
   	 * Restituisce lo stub al server centrale tipo iiop
   	 * @return stub central tipo iiop
   	 */
   	public Central getCentralDataIIOP(){
		return iiopCentral;
   	}

   	/**
   	 * Restituisce lo stub all server di registrazione tipo jrmp
   	 * @return stub regisration server tipo jrmp
   	 */
    public Registration getRegDataJRMP(){
        return jrmpReg;
    }
}
