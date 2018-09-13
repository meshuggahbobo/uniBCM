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

package server.reg;

import java.io.*;
import java.lang.ClassNotFoundException;
import java.rmi.*;

import lib.User;

/**
 * Definisce i metodi fondamentali per il tipo Registration.
 * @author uniBCM team
 *
 */
public interface Registration extends Remote {
	
	/**
	 * Invia la richiesta pendente al server che la salverà su un file temporaneo fino alla
	 * sua conferma o cancellazione.
	 * @param data richiesta pendente marshalizzata
	 * @return stringa di conferma o errore
	 * @throws RemoteException
	 */
	public String submitUser(MarshalledObject data) throws RemoteException;
	
	/**
	 * Conferma o rifiuta la richiesta pendente dell'utente passato come parametro.
	 * Al termine del metodo la richiesta pendente verra' eliminata oppure confermata a seconda 
	 * del parametro accept.
	 * @param id identificativo dell'utente
	 * @param accept operazione: true conferma richiesta, false elimina richiesta
	 * @return stringa di conferma
	 * @throws RemoteException
	 */
	public String confUser(String id, boolean accept) throws RemoteException;
	
	/**
	 * Conferma tutti gli utenti che hanno effettuato una richiesta pendente.
	 * Gli utenti verranno quindi registrati al sistema.
	 * Questa chiamata ha lo stesso effetto di equivale ad eseguire confUser(user[i],true)
	 * per ogni utente che ha effettuato una richiesta pendente.
	 * @see Registration#confUser(String, boolean)
	 * @return stringa di conferma
	 * @throws RemoteException
	 */
	public String confAllUser() throws RemoteException;
	
	/**
	 * Elimina l'utente con identificativo passato come parametro dal sistema uniBCM
	 * @param id utente da eliminare
	 * @return stringa di conferma
	 * @throws RemoteException
	 */
	public String delUser(String id) throws RemoteException;

	/**
	 * Elimina tutte le richieste pendenti. Tutti gli utenti che hanno effettuato una richiesta
	 * di registrazione non ancora confermata verranno eliminati dal sistema 
	 * Questa chiamata ha lo stesso effetto di equivale ad eseguire confUser(user[i],false)
	 * per ogni utente che ha effettuato una richiesta pendente.
	 * @see Registration#confUser(String, false)
	 * @return stringa di conferma
	 * @throws RemoteException
	 */
	public String delAllPendUser() throws RemoteException;
		
	/**
	 * Restituisce la lista di tutti gli utente registrati al sistema
	 * @return array di User
	 * @throws RemoteException 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public User[] userList() throws RemoteException, ClassNotFoundException, IOException;

	/**
	 * Restituisce la lista di tutti gli utenti che hanno effettuato una richiesta pendente
	 * non ancora gestita(confermata/eliminata).
	 * @return array di User
	 * @throws RemoteException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    public User[] pendingUserList() throws RemoteException, IOException, ClassNotFoundException;
    
    /**
     * Setta lo stato dell'utente con id passato come parametro a sospeso/attivo a seconda
     * del parametro suspend
     * @param id Id dell'utente
     * @param suspend stato (true sospeso, false attivo)
     * @return stringa di conferma
     * @throws RemoteException
     */
    public String setUserState (String id, boolean suspend) throws RemoteException;
    
}
