/*
 * RenderListener.java
 *
 * Created on 09. Februar 2002, 20:06
 *
 * Licensed under GNU General Public License (GPL).
 * See http://www.gnu.org/copyleft/gpl.html
 */

package edu.psu.geovista.app.parvis.gui;

/**
 *
 * @author  flo
 * @version
 */
public interface ProgressListener extends java.util.EventListener {

    public void processProgressEvent(ProgressEvent e);

}
