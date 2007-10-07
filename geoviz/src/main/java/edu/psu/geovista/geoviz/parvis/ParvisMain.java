/*
 * ParvisMain.java
 *
 * Created on 17. November 2001, 15:37
 *
 * Copyright 2001 Flo Ledermann flo@subnet.at
 *
 * Licensed under GNU General Public License (GPL).
 * See http://www.gnu.org/copyleft/gpl.html
 *
 */

package edu.psu.geovista.geoviz.parvis;

import javax.swing.UIManager;

import edu.psu.geovista.geoviz.parvis.gui.MainFrame;

/**
 * Launcher Class for unsing Parvis as a standalone application.
 *
 * @author Flo Ledermann flo@subnet.at
 * @version 0.1
 */
public class ParvisMain {


    /**
    * Main method which is called by the java interpreter. Basically displays the window and returns.
    *
    * @param args the command line arguments (currently none available)
    */
    public static void main (String args[]) {
        UIManager.put("geovista.geoviz.parvis.gui.ParallelDisplayUI", "geovista.geoviz.parvis.gui.BasicParallelDisplayUI");
        new MainFrame().setVisible(true);
    }

}
