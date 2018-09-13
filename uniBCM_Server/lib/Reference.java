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

import java.io.Serializable;
import java.util.Date;

/**
 * Implementa una referenza remota ad un server
 * @author uniBCM team
 *
 */
public class Reference implements Serializable{
	
	/**
	 * Descrittore della classe
	 */
	private static final long serialVersionUID = 1L;
	/** Utente che detiene la referenza remota */
	private String user;
	/** Indirizzo IP dell'host che detiene la refernza */
	private String host;
	/** Data della referenza */
	private Date connDate;

	/**
	 * Costruttore di default della referenza
	 * @deprecated replaced by {@link #Reference(String, String, Date)}
	 */
	public Reference(){
		this.user="";
		this.host="";
		this.connDate=new Date();
	}
	
	/**
	 * Costruttore di una referenza
	 * @param user utente che detiene la referenza
	 * @param host host in cui è memorizzata la referenza
	 * @param connDate data della referenza
	 */
	public Reference(String user,String host,Date connDate){
		this.user=user;
		this.host=host;
		this.connDate=connDate;
	}

	/**
	 * Setta lo user della referenza al parametro passato
	 * @param user utente
	 */
	public void setUser(String user){
		this.user=user;
	}

	/**
	 * Setta l'host della referenza al parametro passato
	 * @param host ip dell'host
	 */
	public void setHost(String host){
		this.user=host;
	}

	/**
	 * Restituisce il nome utente della referenza
	 * @return user
	 */
	public String getUser(){
		return this.user;
	}

	/**
	 * Restituisce l'indirizzo IP dell'host
	 * @return ip dell'host
	 */
	public String getHost(){
		return this.host;
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object o){
		Reference ref = (Reference)o;
		return (this.user.equals(ref.getUser()) && this.host.equals(ref.getHost()) 
				&& this.connDate.equals(ref.getDate()));	
	}
	
	/**
	 * Setta la data della referenza al parametro passati
	 * @param d nuova data
	 */
	public void setDate(Date d){
		this.connDate = d;
	}
	
	/**
	 * Restituisce la data della referenza
	 * @return data
	 */
	public Date getDate(){
		return this.connDate;
	}
	
	
}
