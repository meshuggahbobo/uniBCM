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

package server.login;

import mobile.Mobile;
import mobile.mserver.MobileServerItf;
import lib.*;
import java.rmi.*;

/**
 * Definisce i metodi fondamentali per un server che vuole implementare Login
 * @author uniBCM team
 *
 */
public interface Login extends Remote {
	
	/**
	 * Effettua il tentativo di connessione da parte dell'utente checkUser passato come parametro.
	 * Se l'utente e' registrato al sistema gli verra' restituito un oggetto di tipo Mobile
	 * (MobileServer se utente amministratore,MobileAgentProf se prof, MobileAgentStud se studente)
	 * @param checkUser utente da loggare
	 * @return oggetto di tipo Mobile se l'utente e' registrato al sistema, null altrimenti
	 * @throws RemoteException
	 */
	public Mobile login(User checkUser) throws RemoteException;
	
	/**
	 * Setta la referenza del MobileServer  allo stub passato come parametro.
	 * Questo server per sapere dove si e' collegato l'amministratore poiche' esso possiede 
	 * un MobileServer
	 * @param stubMS stub al mobile server dell'ammministratore
	 * @throws RemoteException 
	 */
	public void setMSRef(MobileServerItf stubMS) throws RemoteException;

}
