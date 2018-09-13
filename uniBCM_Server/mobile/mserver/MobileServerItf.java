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

package mobile.mserver;

import mobile.Mobile;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Definisce i metodi per il tipo MobileServer. N.B. Tutti gli oggetti di tipo MobileServer DEVONO
 * dare un'implementazione a questa interfaccia per poter essere restituiti corretamente al client.
 * MobileServer rappresenta un raffinamento dell'interfaccia Mobile per il tipo Server
 * @author uniBCM team
 *
 */
public interface MobileServerItf extends Remote, Mobile{
		
	/**
	 * Effettua il ping al Mobile Server corrente.
	 * Il metodo restiusce l'indirizzo IP sul quale e' in esecuzione l'istanza corrente
	 * del Mobile Server
	 * @return indirizzo IP
	 * @throws RemoteException
	 */
	public String ping() throws RemoteException;
	
}
