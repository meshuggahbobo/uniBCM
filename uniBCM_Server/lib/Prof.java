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

import java.util.Vector;

/**
 * Implementa un tipo di utente Professore
 * @author uniBCM team
 *
 */
public class Prof extends UserImpl{
	
	/**
	 * Descrittore della classe
	 */
	private static final long serialVersionUID = 1L;
	/** Lista dei corsi tenuti dal docente */
	private List courses;

	/**
	 * Restituisce la lista dei corsi tenuti dal docente nei vari anni accademici
	 * @return stringa di corsi tenuti dal docente
	 */
	public Corso[] getCourses(){
	
        Vector<Object> tmp = courses.copyToVector();
        Corso[] result = (Corso[])tmp.toArray(new Corso[tmp.size()]);
        return result;    
	}

	/**
	 * Costruisce una nuova lista di corsi associati al professore
	 */
	public void setCourses(){
		courses = new List();
	}

	/**
	 * @see UserImpl#getType()
	 */
    public String getType(){
        return "Professore";
    }
	
    /**
     * @see UserImpl#printFullInfo()
     */
	public String printFullInfo(){
        String infos = printUser();
        if(courses == null)
            infos += "\n Non è presente nessun corso.";
        else
            infos +="\nCorsi tenuti dal docente:\n"+courses.printItems();

        return infos;
	}

	/**
	 * Aggiunge un nuovo corso tenuto dal docente
	 * @param c nuovo corso
	 */
	public void addCourseProf(Corso c){
		courses.add(c);
	}
}
