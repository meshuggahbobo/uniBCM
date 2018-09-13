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

import java.io.*;

/**
 * Definizione di una classe astratta che implementa un generico utente del sistema uniBCM
 * @author uniBCM
 *
 */
public class UserImpl implements Serializable, User {
	
	/**
	 * Descrittore della classe
	 */
	private static final long serialVersionUID = 1L;
	/** Nome dell'utente */
	private String name;
	/** Cognome dell'utente */
	private String surname;
	/** Password di accesso al sistema dell'utente */
	private String password;
	/** Identificativo univoco dell'utente. Tale campo viene usato come username */
	private String id;
	/** Stato dell'utente true=sospeso,false=abilitato */
	private boolean suspended;
		
	/**
	 * Costruisce un nuovo oggetto di tipo User con tutti i campi a null.
	 */
	public UserImpl() {
		this.name = null;
		this.surname = null;
		this.password = null;
		this.id = null;
		this.suspended = false;
	}
	
	/**
	 * @see User#setName(String)
	 */
	public void setName(String name){
		this.name=name;
	}

	/**
	 * @see User#setSurname(String)
	 */
	public void setSurname(String surname){
		this.surname=surname;
	}
	
	/**
	 * @see User#setPassword(String)
	 */
	public void setPassword(String password) throws NullPointerException, IllegalArgumentException{
		if (password == null)	throw new NullPointerException("Password null");
		if (password.equals(""))throw new IllegalArgumentException("Password non può essere null");
		this.password=password;
	}
	
	/**
	 * @see User#setId(String)
	 */
	public void setId(String id) throws NullPointerException, IllegalArgumentException{
		if ( id == null )	throw new NullPointerException("Identificativo nullo");
		if ( id.equals("")) throw new IllegalArgumentException("id non può essere la stringa vuota");
		this.id=id;
	}
	
	/**
	 * @see User#setSuspend(boolean)
	 */
	public void setSuspend(boolean suspended){
		this.suspended = suspended;
	}
	
	/**
	 * @see User#getName()
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * @see User#getSurname()
	 */
	public String getSurname(){
		return this.surname;
	}
	
	/**
	 * @see User#getPassword()
	 */
	public String getPassword(){
		return this.password;
	}
	
	/**
	 * @see User#getId()
	 */
	public String getId(){
		return this.id;
	}
	
	/**
	 * @see User#getSuspend()
	 */
	public boolean getSuspend(){
		return this.suspended;
	}

	/**
	 * @see User#getType()
	 */
    public String getType(){
        return "Generic User";
    }

	/**
	 * @see User#printUser()
	 */
	public String printUser(){
		String infos = new String();
		infos = "\tID: " + getId() + " | Name: " + getName() + " | Surname: " + getSurname() + " | Password: " + getPassword()+ " | Suspended: "+ getSuspend();
	
		return infos;
	}
	
	/**
	 * @see User#verifyId(User)
	 */
	public boolean verifyId(User user) throws NullPointerException{
		if (user == null)	throw new NullPointerException("user null"); 
		//getId non necessario strettamente in LoginServer
		return ((user.getId().equals(this.getId())) && (user.getPassword().equals(this.getPassword())));
		
	}
	
	/**
	 * @see User#printFullInfo()
	 */
	public String printFullInfo(){
		return printUser();
	}
	
}
