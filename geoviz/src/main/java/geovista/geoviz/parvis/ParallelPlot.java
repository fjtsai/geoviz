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
package geovista.geoviz.parvis;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import geovista.common.classification.ClassifierPicker;
import geovista.common.data.DataSetForApps;
import geovista.common.data.DescriptiveStatistics;
import geovista.common.event.ColorArrayEvent;
import geovista.common.event.ColorArrayListener;
import geovista.common.event.ConditioningEvent;
import geovista.common.event.ConditioningListener;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.PaletteEvent;
import geovista.common.event.PaletteListener;
import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;
import geovista.geoviz.visclass.VisualClassifier;
import geovista.readers.FileIO;

/**
 * A Panel containing a ParallelDisplay and the most important control features.
 * To be used in applets and applications.
 * 
 * @author Flo Ledermann flo@subnet.at
 */
public class ParallelPlot extends JPanel implements ProgressListener,
		DataSetListener, IndicationListener, SelectionListener,
		ColorArrayListener, SubspaceListener, PaletteListener,
		ConditioningListener, TableModelListener {
	JToggleButton lastButton = null;
	VisualClassifier vc = null;
	int[] activeVariables = null;
	DataSetForApps dataSet = null;// contains original data set
	int[] savedSelection;
	private final int maxAxes = 6;
	private long progressstart = 0;

	private JPanel renderPanel;
	private JToggleButton reorderButton;
	private JButton minMaxButton;
	private JLabel renderLabel;
	private JButton minMaxAbsButton;
	private JButton varMinMaxButton;
	private JProgressBar renderProgressBar;
	private JPanel optionsPanel;
	private JToggleButton brushButton;
	private JButton jButton1;
	private geovista.geoviz.parvis.ParallelDisplay parallelDisplay;
	private JCheckBox numberBox;
	private JToggleButton scaleButton;
	private JToggleButton translateButton;
	private JPanel dragModePanel;
	private JLabel timeLabel;
	private JPanel dummyPanel;
	private JPanel openPanel;
	private JButton zeroMaxButton;
	protected final static Logger logger = Logger.getLogger(ParallelPlot.class
			.getName());

	/**
	 * Default Constructor. Creates a new ParallelPlot.
	 */
	public ParallelPlot() {

		vc = new VisualClassifier();
		initComponents();

		lastButton = reorderButton;

		reorderButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		scaleButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
		translateButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));

		parallelDisplay.addProgressListener(this);
		parallelDisplay.addIndicationListener(this);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {// GEN-BEGIN:initComponents

		java.awt.GridBagConstraints gridBagConstraints;

		openPanel = new JPanel();

		parallelDisplay = new ParallelDisplay();
		dragModePanel = new JPanel();
		reorderButton = new JToggleButton();
		scaleButton = new JToggleButton();
		translateButton = new JToggleButton();
		brushButton = new JToggleButton();
		jButton1 = new JButton();
		optionsPanel = new JPanel();
		numberBox = new JCheckBox();
		zeroMaxButton = new JButton();
		minMaxButton = new JButton();
		minMaxAbsButton = new JButton();
		varMinMaxButton = new JButton();
		dummyPanel = new JPanel();
		renderPanel = new JPanel();
		renderLabel = new JLabel();
		renderProgressBar = new JProgressBar();
		timeLabel = new JLabel();

		// fileMenu.setText("File");
		// openFileItem.setText("Open File...");
		// fileMenu.add(openFileItem);
		// openLocationItem.setText("Open URL...");
		// fileMenu.add(openLocationItem);
		// mainMenuBar.add(fileMenu);
		// editMenu.setText("Menu");
		// mainMenuBar.add(editMenu);
		setLayout(new java.awt.GridBagLayout());

		// openPanel.setLayout(new
		// java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
		/*
		 * openPanel.setBorder(new border.TitledBorder(new
		 * border.EtchedBorder(), "Datasource",
		 * border.TitledBorder.DEFAULT_JUSTIFICATION, border.TitledBorder.TOP));
		 * label.setText("url:"); openPanel.add(label);
		 * 
		 * urlField.setToolTipText("Type in URL to load. Press enter.");
		 * urlField.setText("file:///D:/Uni/visualisierung/datasets/cameras.stf");
		 * urlField.setPreferredSize(new java.awt.Dimension(300, 21));
		 * urlField.addActionListener(new java.awt.event.ActionListener() {
		 * public void actionPerformed(java.awt.event.ActionEvent evt) {
		 * urlFieldActionPerformed(evt); } });
		 * 
		 * openPanel.add(urlField);
		 * 
		 * load.setToolTipText("Press to choose a file from your local HD.");
		 * load.setText("open File..."); load.setBorder(new
		 * border.EtchedBorder()); load.addActionListener(new
		 * java.awt.event.ActionListener() { public void
		 * actionPerformed(java.awt.event.ActionEvent evt) {
		 * loadActionPerformed(evt); } });
		 * 
		 * openPanel.add(load);
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

		dragModePanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Drag Mode"));
		reorderButton.setIcon(new ImageIcon(getClass().getResource(
				"reorder.gif")));
		reorderButton
				.setToolTipText("Reorder axes by dragging them across the display.");
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

		scaleButton.setIcon(new ImageIcon(getClass().getResource("scale.gif")));
		scaleButton
				.setToolTipText("Scale axes by dragging up (zoom out) or down (zoom in).");
		scaleButton.setFont(new java.awt.Font("Dialog", 0, 10));
		scaleButton.setText("scale");
		scaleButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
		scaleButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				scaleButtonActionPerformed(evt);
			}
		});

		dragModePanel.add(scaleButton);

		translateButton.setIcon(new ImageIcon(getClass()
				.getResource("move.gif")));
		translateButton
				.setToolTipText("Translate axes by dragging up or down.");
		translateButton.setFont(new java.awt.Font("Dialog", 0, 10));
		translateButton.setText("translate");
		translateButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
		translateButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				translateButtonActionPerformed(evt);
			}
		});

		dragModePanel.add(translateButton);

		brushButton.setIcon(new ImageIcon(getClass().getResource("brush.gif")));
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

		// dragModePanel.add(jButton1); only use garbage collection in debug
		// mode
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(dragModePanel, gridBagConstraints);

		optionsPanel
				.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		optionsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Options"));
		numberBox
				.setToolTipText("Display numeric tooltips when hovering over a record.");
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

		renderPanel
				.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
		renderPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Visual Classifier"));
		renderPanel.add(vc);
		vc.setPreferredSize(new Dimension(600, 20));
		vc.getSymbolizationPanel().setLowColor(Color.black);

		vc.addColorArrayListener(this);
		vc
				.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);

		// renderLabel.setText("progress:");
		// renderLabel.setFont(new java.awt.Font("Dialog", 0, 12));
		// renderPanel.add(renderLabel);
		//
		// renderProgressBar.setFont(new java.awt.Font("Dialog", 0, 10));
		// renderProgressBar.setPreferredSize(new java.awt.Dimension(100, 20));
		// renderProgressBar.setStringPainted(true);
		// renderPanel.add(renderProgressBar);
		//
		// timeLabel.setText("0.0 s");
		// renderPanel.add(timeLabel);
		//
		// previewButton.setText("preview");
		// renderPanel.add(previewButton);
		//
		// qualityButton.setText("quality");
		// renderPanel.add(qualityButton);
		//
		// progressiveButton.setSelected(true);
		// progressiveButton.setText("progressive");
		// renderPanel.add(progressiveButton);
		//
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(renderPanel, gridBagConstraints);
	}

	// GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

	}

	// GEN-LAST:event_jButton1ActionPerformed

	private void brushButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_brushButtonActionPerformed

		if (lastButton != null) {
			lastButton.setSelected(false);
		}

		lastButton = (JToggleButton) evt.getSource();
		lastButton.setSelected(true);

		parallelDisplay.setEditMode(ParallelDisplay.BRUSH);
	}

	// GEN-LAST:event_brushButtonActionPerformed

	private void minMaxAbsButtonActionPerformed(java.awt.event.ActionEvent evt) {
		parallelDisplay.minMaxAbsScale();
	}

	private void varMinMaxButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_minMaxAbsButtonActionPerformed

		int var = vc.getCurrVariableIndex();
		parallelDisplay.varMinMaxScale(var);
	}

	// GEN-LAST:event_minMaxAbsButtonActionPerformed

	private void minMaxButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_minMaxButtonActionPerformed
		parallelDisplay.minMaxScale();
	}

	// GEN-LAST:event_minMaxButtonActionPerformed

	private void zeroMaxButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_zeroMaxButtonActionPerformed
		parallelDisplay.zeroMaxScale();
	}

	// GEN-LAST:event_zeroMaxButtonActionPerformed

	private void numberBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_numberBoxActionPerformed
		parallelDisplay.setBoolPreference("hoverText", numberBox.isSelected());
	}

	// GEN-LAST:event_numberBoxActionPerformed

	private void translateButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_translateButtonActionPerformed

		if (lastButton != null) {
			lastButton.setSelected(false);
		}

		lastButton = (JToggleButton) evt.getSource();
		lastButton.setSelected(true);

		parallelDisplay.setEditMode(ParallelDisplay.TRANSLATE);
	}

	// GEN-LAST:event_translateButtonActionPerformed

	private void scaleButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_scaleButtonActionPerformed

		if (lastButton != null) {
			lastButton.setSelected(false);
		}

		lastButton = (JToggleButton) evt.getSource();
		lastButton.setSelected(true);

		parallelDisplay.setEditMode(ParallelDisplay.SCALE);
	}

	// GEN-LAST:event_scaleButtonActionPerformed

	private void reorderPressed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_reorderPressed

		if (lastButton != null) {
			lastButton.setSelected(false);
		}

		lastButton = (JToggleButton) evt.getSource();
		lastButton.setSelected(true);

		parallelDisplay.setEditMode(ParallelDisplay.REORDER);
	}

	// GEN-LAST:event_reorderPressed

	/**
	 * @param data
	 * 
	 * This method is deprecated becuase it wants to create its very own pet
	 * DataSetForApps. This is no longer allowed, to allow for a mutable, common
	 * data set. Use of this method may lead to unexpected program behavoir.
	 * Please use setDataSet instead.
	 */
	@Deprecated
	public void setDataSet(Object[] data) {
		this.setDataSet(new DataSetForApps(data));

	}

	public void setDataSet(DataSetForApps dataSet) {
		vc.setDataSet(dataSet);

		STFDataSet ds = new STFDataSet(dataSet.getDataObjectOriginal());// hmmm.....

		parallelDisplay.setModel(ds);

		// Color[] colors = this.vc.getColorForObservations();
		setColors(vc.getColorForObservations());
		if (savedSelection != null
				&& DescriptiveStatistics.max(savedSelection) < dataSet
						.getNumObservations()) {
			// XXX should remember slections, but getting
			// arrayindexoutofboundselection
			// selectionChanged(new SelectionEvent(this, savedSelection));

		}
	}

	/*
	 * Added Frank Hardisty 19 July 2002
	 */
	public void indicationChanged(IndicationEvent e) {
		Object source = e.getSource();

		if (source == parallelDisplay) {
			fireIndicationChanged(e.getIndication());
		} else {
			parallelDisplay.indicationChanged(e);
		}
		if (
		// !selectionApplied
		// &&
		savedSelection != null
				&& DescriptiveStatistics.max(savedSelection) < dataSet
						.getNumObservations()) {
			// selectionChanged(new SelectionEvent(this, savedSelection));

		}
	}

	/*
	 * Added Frank Hardisty 19 July 2002
	 */
	public void selectionChanged(SelectionEvent e) {
		savedSelection = e.getSelection();
		// if (this.isVisible() == false){
		// return;
		// }
		if (getWidth() * getHeight() <= 0) {
			logger.info("ParallelPlot got selection when size was zero");

			return;
		}
		parallelDisplay.selectionChanged(e);
		parallelDisplay.setEditMode(ParallelDisplay.BRUSH);

		// this.brushButtonActionPerformed(new
		// ActionEvent(this,ActionEvent.ACTION_PERFORMED,"hi"));
	}

	public SelectionEvent getSelectionEvent() {
		return new SelectionEvent(this, parallelDisplay.savedSelection);
	}

	public void conditioningChanged(ConditioningEvent e) {
		int[] conditioning = e.getConditioning();
		BasicParallelDisplayUI ui = (BasicParallelDisplayUI) parallelDisplay
				.getUI();
		ui.setConditioning(conditioning);
		parallelDisplay.deepRepaint = true;
		this.repaint();
	}

	public void colorArrayChanged(ColorArrayEvent e) {
		Color[] colors = e.getColors();

		if ((colors == null) || (colors.length == 0)) {
			return;
		}

		setColors(colors);
	}

	public void setColors(Color[] colors) {
		BasicParallelDisplayUI ui = (BasicParallelDisplayUI) parallelDisplay
				.getUI();
		ui.setColors(colors);
		parallelDisplay.deepRepaint = true;
		this.repaint();
	}

	public void paletteChanged(PaletteEvent e) {
		vc.paletteChanged(e);
	}

	/*
	 * Added Frank Hardisty 19 July 2002
	 */
	public void dataSetChanged(DataSetEvent e) {

		// no more hacks!!! x2!!!! x3!!!!!
		dataSet = e.getDataSetForApps();
		dataSet.addTableModelListener(this);
		int nVars = dataSet.getNumberNumericAttributes();
		int min = maxAxes;
		if (nVars < min) {
			min = nVars;
		}
		int[] vars = new int[min];
		for (int i = 0; i < min; i++) {
			vars[i] = i;
		}
		setSubspace(vars);
		// we set selection here in case we got a selection before the data
		if (savedSelection == null) {
			return;
		}
		int maxN = DescriptiveStatistics.max(savedSelection);
		if (dataSet.getNumObservations() > maxN) {
			selectionChanged(new SelectionEvent(this, savedSelection));
		}

	}

	public void subspaceChanged(SubspaceEvent e) {
		int[] selectedVars = e.getSubspace();
		setSubspace(selectedVars);
	}

	private void setSubspace(int[] vars) {

		int nVars = maxAxes; // the number of variables to show
		String[] obsNames = dataSet.getObservationNames(); // gotta have these

		int nNumeric = dataSet.getNumberNumericAttributes();

		if (nNumeric < nVars) {
			nVars = nNumeric;
		}
		if (vars.length < nVars) {
			nVars = vars.length;
		}

		String[] varNamesNew = new String[nVars + 1];
		Object[] newDataSet = new Object[nVars + 2]; // +1 for varNames,+1
		// for obsNames
		varNamesNew[0] = "name";

		for (int i = 1; i < (nVars + 1); i++) {
			int var = vars[i - 1];
			if (var < nNumeric) {
				varNamesNew[i] = dataSet.getNumericArrayName(var); // this is
				// an
				// atrocity
			}
		}

		newDataSet[0] = varNamesNew;
		newDataSet[1] = obsNames;

		for (int i = 0; i < nVars; i++) {
			int var = vars[i];
			if (var < nNumeric) {
				newDataSet[i + 2] = dataSet.getNumericDataAsDouble(var);
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
			timeLabel.setText(((e.getTimestamp() - progressstart) / 1000)
					+ " s");

			break;

		case ProgressEvent.PROGRESS_FINISH:
			renderProgressBar.setValue(100);
			timeLabel.setText(((e.getTimestamp() - progressstart) / 1000)
					+ " s");

			break;
		}

		renderLabel.setText(e.getMessage());

		logger.finest(e.getMessage() + ": " + ((int) (e.getProgress() * 100))
				+ "%");
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
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
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
		// next i
	}

	/**
	 * adds an SelectionListener
	 */
	public void addSelectionListener(SelectionListener l) {
		parallelDisplay.addSelectionListener(l);
	}

	/**
	 * removes an SelectionListener from the component
	 */
	public void removeSelectionListener(SelectionListener l) {
		parallelDisplay.removeSelectionListener(l);
	}

	public void tableChanged(TableModelEvent e) {
		vc.setDataSet(dataSet);

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
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
