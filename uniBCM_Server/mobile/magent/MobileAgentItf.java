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

package mobile.magent;

import mobile.Mobile;

import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.rmi.RemoteException;

/**
 * Definisce i metodi per il tipo MobileAgent. N.B. Tutti gli oggetti di tipo MobileAgent DEVONO
 * dare un'implementazioen a questa interfaccia per poter essere restituiti corretamente al client.
 * MobileAgent rappresenta un raffinamento dell'interfaccia Mobile per il tipo Agent
 * @author uniBCM team
 *
 */
public interface MobileAgentItf extends Mobile{
	
	/**
	 * @see Mobile#runSession()
	 */
	public void runSession() throws ClassNotFoundException, IOException, RemoteException;
 
}