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

package client;

import java.io.Serializable;

/**
 * Definisce i metodi comuni a tutti i tipi di client, siano essi studenti, professori
 * o amministratori
 * @author uniBCM Team
 *
 */
public interface Client extends Runnable, Serializable {

	/**
	 * Setta il server verso il quale questo client effettuerà la connessione
	 * @param server indirizzo ip del server
	 */
	public void setServer(String server);
	
	/**
	 * Setta il protocollo sul quale funzionera' il client di uniBCM team
	 * @param protocol protocollo
	 */
	public void setProtocol(String protocol);
	
	/**
	 * Setta l'indirizzo IP del client.
	 * N.B. Serve per IIOP non potendo usare la classe InetAddress per recuperare 
	 * il proprio indirizzo IP
	 * @param address
	 */
	public void setIPAddress(String address);
	
}
