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

package server.proxy;

import java.rmi.*;

/**
 * Definisce i metodi fondamentali per un server che voglia implemetare Proxy
 * @author uniBCM team
 *
 */
public interface Proxy extends Remote{
	
	/**
	 * Tenta di effettuare il login al sistema dell'utente user passato come parametro
	 * In realta' il Proxy invochera' il server di Login il quale controlloera' se l'utente e'
	 * registrato e ritornera' un oggetto di tipo Mobile. Il Proxy marshalizza l'oggetto e lo
	 * restituisce al client
	 * @param user utente da loggare
	 * @return oggetto di tipo Mobile marshallizato se l'utente e' registrato al sistema,null altrimenti
	 * @throws RemoteException
	 */
	public MarshalledObject login(MarshalledObject user) throws RemoteException;
	
	/**
	 * Crea una nuova richiesta pendente dell'utente user. In realta' il Proxy invochera'
	 * il server di Registrazione per effttuare l'operazione e restituira' una stringa di conferma
	 * dell'operazione
	 * @param user utente 
	 * @return stringa di conferma
	 * @throws RemoteException
	 */
	public String submitUser(MarshalledObject user) throws RemoteException;
}
