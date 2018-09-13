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

import java.io.*;
import java.net.*;
import java.rmi.server.*;
import client.Client;

public class ClientIIOPBootstrap {
	//codebase (file o http)
  	static String codebase;
	//classe da caricare
  	static final String clientClass = "client.ClientIIOP";

  	public static void main(String[] args) throws Exception {

		//Installo un nuovo SecurityManager
       		System.setSecurityManager(new SecurityManager());
		//Recuper indirizzo ip del server...
		String server = args[0];
		//...e protocollo sul quale lavora
		String protocol = args[1];
		//...e il mio indirizzo IP
		//questo perchè iiop non supporta getLocalAddress()
		String ip = args[2];

		//setto il codabase
		codebase = (protocol.equals("file")) ? "file://_SERVERHOME/public_html/uniBCM/" : "http://"+server+":8001/";

		//carico la classe
       		Class classClient = RMIClassLoader.loadClass(codebase,clientClass);
       		Client clientIIOP = (Client)classClient.newInstance();
		
		clientIIOP.setServer(server);
		clientIIOP.setProtocol(protocol);
		clientIIOP.setIPAddress(ip);

       		clientIIOP.run();
 	}
}
