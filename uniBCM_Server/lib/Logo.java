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

import java.awt.*;
import java.net.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 * Classe che implementa uno splash screen
 * @author uniBCM Team
 *
 */
public class Logo extends JWindow {


    /**
	 * Descrittore della classe
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore di default che crea un nuovo splash screen prima
	 * dell'avvio del sistema.
	 * 
	 */
    public Logo(String hostname, String protocol) {
	
	
	JLabel label=null;
	URL img_url=null;
		if(protocol.equals("http")){
			try{
			img_url = new URL("http://"+hostname+":8001/img/logo.gif");
			}catch(MalformedURLException mue){
				System.out.println("Errore lettura immagine!");
			}

			label = new JLabel(new ImageIcon(img_url));
		}

		else
			label = new JLabel(new ImageIcon("/home/accounts/studenti/vr044794/public_html/uniBCM/img/logo.gif"));

        JPanel content = (JPanel)this.getContentPane();
        content.setBackground(Color.white);


        // Set the window's bounds, centering the window
        int width = 450;
        int height =400;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        setBounds(x,y,width,height);

        // Build the splash screen
        JLabel uniBCM = new JLabel("uniBCM System", JLabel.CENTER);
        uniBCM.setFont(new Font("Sans-Serif", Font.BOLD, 20));
        JLabel copyrt = new JLabel
                ("Copyleft 2009, A. Cordioli & A. Oboe", JLabel.CENTER);
        copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
        content.add(uniBCM, BorderLayout.NORTH);
        content.add(label, BorderLayout.CENTER);
        content.add(copyrt, BorderLayout.SOUTH);
        Color oraRed = new Color(156, 20, 20,  255);
        content.setBorder(BorderFactory.createLineBorder(oraRed, 10));

        final Runnable closerRunner = new Runnable() {
            public void run() {
                setVisible(false);
                dispose();
            }
        };
        Runnable waitRunner = new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    SwingUtilities.invokeAndWait(closerRunner);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        setVisible(true);
        Thread splashThread = new Thread(waitRunner, "SplashThread");
        splashThread.start();
    }
    
    /**
     * Nasconde il logo
     */
    public void splashHide() {
        setVisible(false);
    }

}
