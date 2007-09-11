/*
 * ParallelPlot.java
 *
 * Created on 18. November 2001, 20:27
 *
 * Copyright 2001 by Flo Ledermann flo@subnet.at
 *
 * Licensed under GNU General Public License (GPL).
 * See http://www.gnu.org/copyleft/gpl.html
 */
package edu.psu.geovista.app.parvis.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.psu.geovista.app.parvis.file.STFDataSet;
import edu.psu.geovista.classification.ClassifierPicker;
import edu.psu.geovista.common.event.PaletteEvent;
import edu.psu.geovista.common.event.PaletteListener;
import edu.psu.geovista.data.geog.DataSetForApps;
import edu.psu.geovista.io.FileIO;
import edu.psu.geovista.ui.event.ColorArrayEvent;
import edu.psu.geovista.ui.event.ColorArrayListener;
import edu.psu.geovista.ui.event.ConditioningEvent;
import edu.psu.geovista.ui.event.ConditioningListener;
import edu.psu.geovista.ui.event.DataSetEvent;
import edu.psu.geovista.ui.event.DataSetListener;
import edu.psu.geovista.ui.event.IndicationEvent;
import edu.psu.geovista.ui.event.IndicationListener;
import edu.psu.geovista.ui.event.SelectionEvent;
import edu.psu.geovista.ui.event.SelectionListener;
import edu.psu.geovista.ui.event.SubspaceEvent;
import edu.psu.geovista.ui.event.SubspaceListener;
import edu.psu.geovista.visclass.VisualClassifier;


/**
 * A Panel containing a ParallelDisplay and the most important control features.
 * To be used in applets and applications.
 *
 * @author Flo Ledermann flo@subnet.at
 */
public class ParallelPlot extends javax.swing.JPanel implements ProgressListener,
    DataSetListener, IndicationListener, SelectionListener, ColorArrayListener,
    SubspaceListener, PaletteListener, ConditioningListener, TableModelListener {
    JToggleButton lastButton = null;
    VisualClassifier vc = null;
    int[] activeVariables = null;
    DataSetForApps dataSet = null;//contains original data set
    private int maxAxes = 6;
    private long progressstart = 0;

    private javax.swing.JPanel renderPanel;
    private javax.swing.JToggleButton reorderButton;
    private javax.swing.JButton minMaxButton;
    private javax.swing.JLabel renderLabel;
    private javax.swing.JButton minMaxAbsButton;
    private javax.swing.JButton varMinMaxButton;
    private javax.swing.JProgressBar renderProgressBar;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JToggleButton brushButton;
    private javax.swing.JButton jButton1;
    private edu.psu.geovista.app.parvis.gui.ParallelDisplay parallelDisplay;
    private javax.swing.JCheckBox numberBox;
    private javax.swing.JToggleButton scaleButton;
    private javax.swing.JToggleButton translateButton;
    private javax.swing.JPanel dragModePanel;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JPanel dummyPanel;
    private javax.swing.JPanel openPanel;
    private javax.swing.JButton zeroMaxButton;
    protected final static Logger logger = Logger.getLogger(ParallelPlot.class.getName());
    /**
     * Default Constructor. Creates a new ParallelPlot.
     */
    public ParallelPlot() {
        this.vc = new VisualClassifier();
        initComponents();

        lastButton = reorderButton;

        reorderButton.setCursor(Cursor.getPredefinedCursor(
                Cursor.E_RESIZE_CURSOR));
        scaleButton.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        translateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        parallelDisplay.addProgressListener(this);
        parallelDisplay.addIndicationListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        java.awt.GridBagConstraints gridBagConstraints;

        openPanel = new javax.swing.JPanel();


        parallelDisplay = new edu.psu.geovista.app.parvis.gui.ParallelDisplay();
        dragModePanel = new javax.swing.JPanel();
        reorderButton = new javax.swing.JToggleButton();
        scaleButton = new javax.swing.JToggleButton();
        translateButton = new javax.swing.JToggleButton();
        brushButton = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        optionsPanel = new javax.swing.JPanel();
        numberBox = new javax.swing.JCheckBox();
        zeroMaxButton = new javax.swing.JButton();
        minMaxButton = new javax.swing.JButton();
        minMaxAbsButton = new javax.swing.JButton();
        varMinMaxButton = new javax.swing.JButton();
        dummyPanel = new javax.swing.JPanel();
        renderPanel = new javax.swing.JPanel();
        renderLabel = new javax.swing.JLabel();
        renderProgressBar = new javax.swing.JProgressBar();
        timeLabel = new javax.swing.JLabel();


        //fileMenu.setText("File");
        //openFileItem.setText("Open File...");
        //fileMenu.add(openFileItem);
        //openLocationItem.setText("Open URL...");
        //fileMenu.add(openLocationItem);
        //mainMenuBar.add(fileMenu);
        //editMenu.setText("Menu");
        //mainMenuBar.add(editMenu);
        setLayout(new java.awt.GridBagLayout());

        //openPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        /*
        openPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Datasource", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));
        label.setText("url:");
        openPanel.add(label);

        urlField.setToolTipText("Type in URL to load. Press enter.");
        urlField.setText("file:///D:/Uni/visualisierung/datasets/cameras.stf");
        urlField.setPreferredSize(new java.awt.Dimension(300, 21));
        urlField.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(java.awt.event.ActionEvent evt) {
              urlFieldActionPerformed(evt);
          }
        });

        openPanel.add(urlField);

        load.setToolTipText("Press to choose a file from your local HD.");
        load.setText("open File...");
        load.setBorder(new javax.swing.border.EtchedBorder());
        load.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(java.awt.event.ActionEvent evt) {
              loadActionPerformed(evt);
          }
        });

        openPanel.add(load);
        */
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        add(openPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(parallelDisplay, gridBagConstraints);

        dragModePanel.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.LEFT));

        dragModePanel.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EtchedBorder(), "Drag Mode"));
        reorderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/psu/geovista/app/parvis/gui/reorder.gif")));
        reorderButton.setToolTipText(
            "Reorder axes by dragging them across the display.");
        reorderButton.setSelected(true);
        reorderButton.setFont(new java.awt.Font("Dialog", 0, 10));
        reorderButton.setText("order");
        reorderButton.setPreferredSize(new java.awt.Dimension(65, 27));
        reorderButton.setMaximumSize(new java.awt.Dimension(65, 27));
        reorderButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        reorderButton.setMinimumSize(new java.awt.Dimension(65, 27));
        reorderButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    reorderPressed(evt);
                }
            });

        dragModePanel.add(reorderButton);

        scaleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/psu/geovista/app/parvis/gui/scale.gif")));
        scaleButton.setToolTipText(
            "Scale axes by dragging up (zoom out) or down (zoom in).");
        scaleButton.setFont(new java.awt.Font("Dialog", 0, 10));
        scaleButton.setText("scale");
        scaleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        scaleButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    scaleButtonActionPerformed(evt);
                }
            });

        dragModePanel.add(scaleButton);

        translateButton.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/edu/psu/geovista/app/parvis/gui/move.gif")));
        translateButton.setToolTipText("Translate axes by dragging up or down.");
        translateButton.setFont(new java.awt.Font("Dialog", 0, 10));
        translateButton.setText("translate");
        translateButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        translateButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    translateButtonActionPerformed(evt);
                }
            });

        dragModePanel.add(translateButton);

        brushButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/psu/geovista/app/parvis/gui/brush.gif")));
        brushButton.setToolTipText("Translate axes by dragging up or down.");
        brushButton.setFont(new java.awt.Font("Dialog", 0, 10));
        brushButton.setText("brush");
        brushButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        brushButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    brushButtonActionPerformed(evt);
                }
            });

        dragModePanel.add(brushButton);

        jButton1.setText("gc()");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

        //dragModePanel.add(jButton1); only use garbage collection in debug mode
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(dragModePanel, gridBagConstraints);

        optionsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        optionsPanel.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EtchedBorder(), "Options"));
        numberBox.setToolTipText(
            "Display numeric tooltips when hovering over a record.");
        numberBox.setSelected(true);
        numberBox.setText("show Numbers");
        numberBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    numberBoxActionPerformed(evt);
                }
            });

        optionsPanel.add(numberBox);

        zeroMaxButton.setText("0-max scale");
        zeroMaxButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    zeroMaxButtonActionPerformed(evt);
                }
            });

        optionsPanel.add(zeroMaxButton);

        minMaxButton.setText("min-max scale");
        minMaxButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    minMaxButtonActionPerformed(evt);
                }
            });

        optionsPanel.add(minMaxButton);

        minMaxAbsButton.setText("min-max(abs) scale");

        minMaxAbsButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    minMaxAbsButtonActionPerformed(evt);
                }
            });
        optionsPanel.add(minMaxAbsButton);

        varMinMaxButton.setText("min-max variable");

        varMinMaxButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    varMinMaxButtonActionPerformed(evt);
                }
            });
        optionsPanel.add(varMinMaxButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(optionsPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        add(dummyPanel, gridBagConstraints);

        renderPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        renderPanel.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EtchedBorder(), "Visual Classifier"));
        renderPanel.add(vc);
        vc.setPreferredSize(new Dimension(600, 20));
        vc.getSymbolizationPanel().setLowColor(Color.black);

        vc.addColorArrayListener(this);
        vc.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);

        //        renderLabel.setText("progress:");
        //        renderLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        //        renderPanel.add(renderLabel);
        //
        //        renderProgressBar.setFont(new java.awt.Font("Dialog", 0, 10));
        //        renderProgressBar.setPreferredSize(new java.awt.Dimension(100, 20));
        //        renderProgressBar.setStringPainted(true);
        //        renderPanel.add(renderProgressBar);
        //
        //        timeLabel.setText("0.0 s");
        //        renderPanel.add(timeLabel);
        //
        //        previewButton.setText("preview");
        //        renderPanel.add(previewButton);
        //
        //        qualityButton.setText("quality");
        //        renderPanel.add(qualityButton);
        //
        //        progressiveButton.setSelected(true);
        //        progressiveButton.setText("progressive");
        //        renderPanel.add(progressiveButton);
        //
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(renderPanel, gridBagConstraints);
    }
//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }
//GEN-LAST:event_jButton1ActionPerformed

    private void brushButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brushButtonActionPerformed

        if (lastButton != null) {
            lastButton.setSelected(false);
        }

        lastButton = (JToggleButton) evt.getSource();
        lastButton.setSelected(true);

        parallelDisplay.setEditMode(ParallelDisplay.BRUSH);
    }
//GEN-LAST:event_brushButtonActionPerformed

    private void minMaxAbsButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        parallelDisplay.minMaxAbsScale();
    }
                                                    

    private void varMinMaxButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minMaxAbsButtonActionPerformed

        int var = this.vc.getCurrVariableIndex();
        parallelDisplay.varMinMaxScale(var);
    }
//GEN-LAST:event_minMaxAbsButtonActionPerformed

    private void minMaxButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minMaxButtonActionPerformed
        parallelDisplay.minMaxScale();
    }
//GEN-LAST:event_minMaxButtonActionPerformed

    private void zeroMaxButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zeroMaxButtonActionPerformed
        parallelDisplay.zeroMaxScale();
    }
//GEN-LAST:event_zeroMaxButtonActionPerformed

    private void numberBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberBoxActionPerformed
        parallelDisplay.setBoolPreference("hoverText", numberBox.isSelected());
    }
//GEN-LAST:event_numberBoxActionPerformed

    private void translateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_translateButtonActionPerformed

        if (lastButton != null) {
            lastButton.setSelected(false);
        }

        lastButton = (JToggleButton) evt.getSource();
        lastButton.setSelected(true);

        parallelDisplay.setEditMode(ParallelDisplay.TRANSLATE);
    }
//GEN-LAST:event_translateButtonActionPerformed

    private void scaleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scaleButtonActionPerformed

        if (lastButton != null) {
            lastButton.setSelected(false);
        }

        lastButton = (JToggleButton) evt.getSource();
        lastButton.setSelected(true);

        parallelDisplay.setEditMode(ParallelDisplay.SCALE);
    }
//GEN-LAST:event_scaleButtonActionPerformed

    private void reorderPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reorderPressed

        if (lastButton != null) {
            lastButton.setSelected(false);
        }

        lastButton = (JToggleButton) evt.getSource();
        lastButton.setSelected(true);

        parallelDisplay.setEditMode(ParallelDisplay.REORDER);
    }
//GEN-LAST:event_reorderPressed

    /**
     * @param data
     * 
     * This method is deprecated becuase it wants to create its very own pet
     * DataSetForApps. This is no longer allowed, to allow for a mutable, 
     * common data set. Use of this method may lead to unexpected
     * program behavoir. 
     * Please use setDataSet instead.
     */
    @Deprecated
    public void setDataSet(Object[] data) {
  	 this.setDataSet(new DataSetForApps(data));
      
    }    
    
    public void setDataSet(DataSetForApps dataSet) {
        this.vc.setDataSet(dataSet);

        STFDataSet ds = new STFDataSet(dataSet.getDataObjectOriginal());//hmmm.....

        parallelDisplay.setModel(ds);

        //Color[] colors = this.vc.getColorForObservations();
        this.setColors(this.vc.getColorForObservations());
    }

    /*
    * Added Frank Hardisty 19 July 2002
    */
    public void indicationChanged(IndicationEvent e) {
        Object source = e.getSource();

        if (source == this.parallelDisplay) {
            this.fireIndicationChanged(e.getIndication());
        } else {
            parallelDisplay.indicationChanged(e);
        }
    }

    /*
    * Added Frank Hardisty 19 July 2002
    */
    public void selectionChanged(SelectionEvent e) {
//        if (this.isVisible() == false){
//          return;
//        }
//        if (this.getWidth() * this.getHeight() <= 0){
//          return;
//        }
        this.parallelDisplay.selectionChanged(e);
        parallelDisplay.setEditMode(ParallelDisplay.BRUSH);

        //this.brushButtonActionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"hi"));
    }
    public void conditioningChanged(ConditioningEvent e){
      int[] conditioning = e.getConditioning();
      BasicParallelDisplayUI ui = (BasicParallelDisplayUI) this.parallelDisplay.getUI();
      ui.setConditioning(conditioning);
      this.parallelDisplay.deepRepaint = true;
        this.repaint();
    }
    public void colorArrayChanged(ColorArrayEvent e) {
        Color[] colors = e.getColors();

        if ((colors == null) || (colors.length == 0)) {
            return;
        }

        this.setColors(colors);
    }

    public void setColors(Color[] colors) {
        BasicParallelDisplayUI ui = (BasicParallelDisplayUI) this.parallelDisplay.getUI();
        ui.setColors(colors);
        this.parallelDisplay.deepRepaint = true;
        this.repaint();
    }

    public void paletteChanged(PaletteEvent e){
      this.vc.paletteChanged(e);
    }

    /*
    * Added Frank Hardisty 19 July 2002
    */
    public void dataSetChanged(DataSetEvent e) {
        //no more hacks!!! x2!!!!
        this.dataSet = e.getDataSetForApps();
        this.dataSet.addTableModelListener(this);
        int nVars = dataSet.getNumberNumericAttributes();
        int min = this.maxAxes;
        if (nVars < min){
          min = nVars;
        }
        int[] vars = new int[min];
        for (int i = 0; i < min; i++){
          vars[i] = i;
        }
        this.setSubspace(vars);
    }

    public void subspaceChanged(SubspaceEvent e) {
        int[] selectedVars = e.getSubspace();
        this.setSubspace(selectedVars);
    }

    private void setSubspace(int[] vars) {


        int nVars = this.maxAxes; //the number of variables to show
        String[] obsNames = dataSet.getObservationNames(); //gotta have these

        int nNumeric = dataSet.getNumberNumericAttributes();

        if (nNumeric < nVars) {
            nVars = nNumeric;
        }
        if (vars.length < nVars){
          nVars = vars.length;
        }

        String[] varNamesNew = new String[nVars + 1];
        Object[] newDataSet = new Object[nVars + 2]; //+1 for varNames,+1 for obsNames
        varNamesNew[0] = "name";

        for (int i = 1; i < (nVars + 1); i++) {
          int var = vars[i-1];
            if (var < nNumeric){
              varNamesNew[i] = dataSet.getNumericArrayName(var); //this is an atrocity
            }
        }

        newDataSet[0] = varNamesNew;
        newDataSet[1] = obsNames;

        for (int i = 0; i < nVars; i++) {
            int var = vars[i];
            if (var < nNumeric){
              newDataSet[i + 2] = (double[]) dataSet.getNumericDataAsDouble(var);
            }
        }

        this.setDataSet(newDataSet);
    }

    public void processProgressEvent(ProgressEvent e) {
        switch (e.getType()) {
        case ProgressEvent.PROGRESS_START:
            progressstart = e.getTimestamp();
            timeLabel.setText("0 s");

            break;

        case ProgressEvent.PROGRESS_UPDATE:
            renderProgressBar.setValue((int) (e.getProgress() * 100));
            timeLabel.setText(((e.getTimestamp() - progressstart) / 1000) +
                " s");

            break;

        case ProgressEvent.PROGRESS_FINISH:
            renderProgressBar.setValue(100);
            timeLabel.setText(((e.getTimestamp() - progressstart) / 1000) +
                " s");

            break;
        }

        renderLabel.setText(e.getMessage());

        logger.finest(e.getMessage() + ": " + ((int)(e.getProgress() * 100))+"%");
    }

    /**
     * Main method for testing purposes.
     */
    public static void main(String[] args) {
        String fileName = "C:\\geovista_old\\data\\test6.csv";

        Object[] dataSet = new Object[4];
        String[] labels = new String[] { "0", "1", "Name" };
        dataSet[0] = labels;

        try {
            FileIO fio = new FileIO(fileName, "r");

            for (int col = 0; col < 2; col++) {
                double[] doubleData = new double[7];

                for (int row = 0; row < 7; row++) {
                    doubleData[row] = fio.readDouble();
                }

                dataSet[col + 1] = doubleData;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] names = new String[7];

        for (int row = 0; row < 7; row++) {
            names[row] = "Obs " + row;
        }

        dataSet[3] = names;

        STFDataSet f = new STFDataSet(dataSet);
        logger.finest("min = " + f.getMinValue(0));
        logger.finest("max = " + f.getMaxValue(0));

        ParallelPlot mp = new ParallelPlot();
        JFrame app = new JFrame();
        app.getContentPane().add(mp);
        mp.setDataSet(dataSet);
        app.pack();
        app.setVisible(true);

        app.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
    }

    /**
     * adds an IndicationListener
     */
    public void addIndicationListener(IndicationListener l) {
        logger.finest("ParallelPlot, adding indication listener");
        listenerList.add(IndicationListener.class, l);
    }

    /**
     * removes an IndicationListener from the component
     */
    public void removeIndicationListener(IndicationListener l) {
      logger.finest("PCP, removing indication listener");
        listenerList.remove(IndicationListener.class, l);
    }

    /**
     * Notify all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    private void fireIndicationChanged(int newIndication) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        IndicationEvent e = null;

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IndicationListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new IndicationEvent(this, newIndication);
                }

                ((IndicationListener) listeners[i + 1]).indicationChanged(e);
            }
        }
         //next i
    }

    /**
    * adds an SelectionListener
    */
    public void addSelectionListener(SelectionListener l) {
        this.parallelDisplay.addSelectionListener(l);
    }

    /**
     * removes an SelectionListener from the component
     */
    public void removeSelectionListener(SelectionListener l) {
        this.parallelDisplay.removeSelectionListener(l);
    }

	public void tableChanged(TableModelEvent e) {
		this.vc.setDataSet(dataSet);
		
	}
}
