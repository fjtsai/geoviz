/*------------------------------------------------------------------------------
 * Java source file for the class DataSetForApps
 *
 * Copyright (c), 2002, GeoVISTA Center
 *
 * Original Authors: Xiping Dai, xpdai@psu.edu and Frank Hardisty hardisty@geog.psu.edu
 * $Author: xpdai $
 * $Date: 2005/07/28 22:47:48 $
 * $Id: DataSetForApps.java,v 1.24 2005/07/28 22:47:48 xpdai Exp $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */

package edu.psu.geovista.data.geog;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * This class takes a set of Java arrays of type double[], int[], boolean[], or
 * String[], plus optional spatial (or other) data.
 * 
 * This data is passed in as an Object[]. If the Object[] passed is instantiated
 * and named "dataSet", it is expected that: dataSet[0] = String[] where the
 * length of the string array is the number of attributes to follow. These are
 * the attribute names. dataSet[1] = double[], int[], boolean[], or String[]
 * dataSet[2] = double[], int[], boolean[], or String[] dataSet[...] = double[],
 * int[], boolean[], or String[] dataSet[n] = double[], int[], boolean[], or
 * String[] (optional) dataSet[n + 1] = Other data. Possiblities include Shape[]
 * or DataSetForAppsRecord[]. (optional) dataSet[n + ...] = Other data.
 * 
 * Spatial data in the n+1 (or higher) place will be interpreted as follows:
 * java.awt.geom.GeneralPath => SPATIAL_TYPE_POLYGON
 * edu.psu.geovista.data.geog.GeneralPathLine => SPATIAL_TYPE_LINE
 * java.awt.geom.Point2D => SPATIAL_TYPE_POINT Then the spatial data type will
 * be correctly set, and the spatial data will then be accessible through the
 * getShapeData() etc. methods. Spatial data in the n+1 (or higher) place that
 * is not of one of those class types will not be correctly handled in the
 * current version.
 * 
 * 
 * @version $Revision: 1.24 $
 * @author Xiping Dai and Frank Hardisty
 */
public class DataSetForApps implements TableModel {

	public static final int TYPE_NONE = -1;
	public static final int TYPE_NAME = 0;
	public static final int TYPE_DOUBLE = 1;
	public static final int TYPE_INTEGER = 2;
	public static final int TYPE_BOOLEAN = 3;

	public static final int SPATIAL_TYPE_NONE = -1;
	public static final int SPATIAL_TYPE_POINT = 0;
	public static final int SPATIAL_TYPE_LINE = 1;
	public static final int SPATIAL_TYPE_POLYGON = 2;
	public static final int SPATIAL_TYPE_RASTER = 3;

	private transient int spatialType = DataSetForApps.SPATIAL_TYPE_NONE;

	private transient Object[] dataObjectOriginal;
	private transient Object[] dataSetNumericAndSpatial;
	private transient Object[] dataSetAttAndNumeric;
	private transient Object[] dataSetNumeric;
	private transient Object[] dataSetFull;
	private transient String[] attributeNames;
	private transient String[] attributeNamesNumeric;
	private transient String[] observationNames;
	private transient String[] attributeDescriptions;
	private transient int[] conditionArray = null;
	private transient int numNumericAttributes;
	private transient int numObservations;
	private transient int[] dataType;
	private transient EventListenerList listenerList;
	final static Logger logger = Logger.getLogger(DataSetForApps.class.getName());
	public DataSetForApps() {
		listenerList = new EventListenerList();
	}

	/**
	 * This constructor is equivalent to calling setDataObject(data) with the
	 * array passed in.
	 * 
	 * Note: if this DSA is meant to simulate a previously constructed DSA
	 * then the listenerList from the prevoius DSA will need to be set after 
	 * this. 
	 */

	public DataSetForApps(Object[] data) {
		listenerList = new EventListenerList();
		this.setDataObject(data);
	}




	/**
	 * This method accepts the input Object[] and intializes all member
	 * variables. In general, member variables do not change after
	 * initialization. Before this method is called, all member variables are
	 * null.
	 * 
	 * Any spatial data passed in is assumed to be polygonal if the spatial data
	 * is of type Shape[], and assumed to be point if the spatial data is of
	 * type Point2D[]. For line types, setDataObject(Object[] data, int
	 * spatialType) should be used, or setSpatialType(int spatialType).
	 */
	@Deprecated
	public void setDataObject(Object[] data) {
		this.dataObjectOriginal = data;
		init(this.dataObjectOriginal);
	}

	/**
	 * Returns exactly what was passed in to setDataObject(Object[]).
	 */
	public Object[] getDataObjectOriginal() {
		return this.dataObjectOriginal;
	}

	/**
	 * Retuns all data, including observationNames String[] data at the end.
	 */
	public Object[] getDataSetFull() {
		return this.dataSetFull;
	}

	public Object[] getNamedArrays() {
		Object[] namedArrays = new Object[this.attributeNames.length];
		for (int i = 0; i < namedArrays.length; i++) {
			namedArrays[i] = this.dataObjectOriginal[i + 1];

		}
		return namedArrays;
	}

	/**
	 * Returns the object array with only numerical variables (double[], int[],
	 * and String[]) from the attribute arrays, plus any other attached objects.
	 */
	public Object[] getDataSetNumericAndSpatial() {
		return this.dataSetNumericAndSpatial;
	}

	/**
	 * @return the object array with only numerical variables (double[], int[],
	 *         and String[]) from the attribute arrays
	 */
	public Object[] getDataSetNumeric() {
		return this.dataSetNumeric;
	}

	public Object[] getDataSetNumericAndAtt() {
		this.dataSetAttAndNumeric = new Object[this.dataSetNumeric.length + 1];
		this.dataSetAttAndNumeric[0] = this.attributeNamesNumeric;
		for (int i = 0; i < this.dataSetNumeric.length; i++) {
			this.dataSetAttAndNumeric[i + 1] = this.dataSetNumeric[i];
		}
		return this.dataSetAttAndNumeric;
	}

	/**
	 * Returns the attribute names for all input arrays.
	 */
	public String[] getAttributeNamesOriginal() {
		return this.attributeNames;
	}

	/**
	 * Returns the names of only the numerical variables (double[], int[], and
	 * String[]) from the attribute arrays.
	 * 
	 */
	public String[] getAttributeNamesNumeric() {
		return this.attributeNamesNumeric;
	}

	/**
	 * Returns the appropriate array, counting only the numerical variables
	 * (double[], int[], and boolean[]).
	 * 
	 * This first index is zero, the next one, and so on, the last being
	 * getNumberNumericAttributes() -1
	 * 
	 */
	public Object getAttributeNumeric(int numericIndex) {
		// if (numericIndex >= this.numNumericAttributes) {

		// }
		return this.dataSetNumericAndSpatial[numericIndex + 1]; // skip
																// attribute
																// names
	}

	/**
	 * Returns the names of the observations, if any names were attached. The
	 * names are determined by the first attribute name which ends in "name",
	 * case insenstive.
	 * 
	 */
	public String[] getObservationNames() {
		return this.observationNames;
	}

	/**
	 * Returns the name of the observations, at observation "obs", if any names
	 * were attached. The names are determined by the first attribute name which
	 * ends in "name", case insenstive.
	 * 
	 */

	public String getObservationName(int obs) {
		return this.observationNames[obs];
	}

	/**
	 * In case the data set has already been stripped of non-numeric items.
	 * 
	 */
	public void setObservationNames(String[] observationNames) {
		this.observationNames = observationNames;
	}

	public void setSpatialType(int spatialType) {
		this.spatialType = spatialType;
	}

	/**
	 * Returns a new int[] of the same length as the number of observations.
	 * 
	 */
	public int[] getConditionArray() {
		this.conditionArray = new int[this.numObservations];
		return this.conditionArray;
	}

	/**
	 * Returns the number of numerical variables (double[], int[], and String[])
	 */
	public int getNumberNumericAttributes() {
		return this.numNumericAttributes;
	}

	/**
	 * Returns the data type of each attribute array ...??
	 */
	public int[] getDataTypeArray() {
		return this.dataType;
	}

	/**
	 * Returns the number of observations in the data set, for which there are
	 * attribute names.
	 */
	public int getNumObservations() {
		return this.numObservations;

	}

	/**
	 * Returns the type of spatial data in the data set.
	 */
	public int getSpatialType() {
		return this.spatialType;

	}

	/**
	 * Returns data after the named arrays. Could be a zero-length array.
	 */
	public Object[] getOtherData() {
		int numOtherObjects = this.dataObjectOriginal.length
				- this.attributeNames.length - 1;

		Object[] otherData = new Object[numOtherObjects];
		if (otherData.length == 0) {
			return otherData;
		}

		for (int i = attributeNames.length + 1; i < this.dataObjectOriginal.length; i++) {
			otherData[i - attributeNames.length-1] = this.dataObjectOriginal[i];//fah just added a -1 here
		}
		return otherData;

	}

	public String[] getAttributeDescriptions() {
		// XXX hack alert on the second part of this condition
		// not a safe set of assumptions
		if (this.dataObjectOriginal[this.dataObjectOriginal.length - 1] == null
				|| !(this.dataObjectOriginal[this.dataObjectOriginal.length - 1] instanceof String[])) {
			this.attributeDescriptions = null;
		} else {
			this.attributeDescriptions = (String[]) this.dataObjectOriginal[this.dataObjectOriginal.length - 1];
		}
		return this.attributeDescriptions;
	}

	/**
	 * Returns the first instance of Shape[] found in the indicated place, or
	 * else null if the spatial data type is not Shape[] or does not exist.
	 */
	public Shape[] getShapeData() {

		int i = this.getShapeDataPlace();
		if (i > 0) {
			return (Shape[]) this.dataObjectOriginal[i];
		}
		return null;
	}

	/**
	 * Returns the first instance of a GeneralPath[] found in the data set,
	 * searching from last to first, or else null if none exists.
	 */
	public GeneralPath[] getGeneralPathData() {
		for (int i = this.dataObjectOriginal.length - 1; i > -1; i--) {
			if (this.dataObjectOriginal[i] instanceof GeneralPath[]) {
				return (GeneralPath[]) this.dataObjectOriginal[i];
			}
		}
		return null;
	}

	/**
	 * Returns the place of thefirst instance of a Shape[] found in the data
	 * set, searching from last to first, or else -1 if none exists.
	 */

	public int getShapeDataPlace() {
		for (int i = this.dataObjectOriginal.length - 1; i > -1; i--) {
			if (this.dataObjectOriginal[i] instanceof Shape[]) {
				return i;
			}
		}
		return -1;

	}

	/**
	 * Returns the first instance of a Point2D[] found in the data set,
	 * searching from last to first, or else null if none exists.
	 */
	public Point2D[] getPoint2DData() {
		for (int i = this.dataObjectOriginal.length - 1; i > -1; i--) {
			if (this.dataObjectOriginal[i] instanceof Point2D[]) {
				return (Point2D[]) this.dataObjectOriginal[i];
			}
		}
		return null;
	}

	// /**
	// * Returns the first instance of a ShapeFile found in the data set,
	// * searching from last to first, or else null if none exists.
	// */
	// public ShapeFile getShapeFileData(){
	// for (int i = this.dataObjectOriginal.length - 1; i > -1; i--) {
	// if(this.dataObjectOriginal[i] instanceof ShapeFile) {
	// return (ShapeFile)this.dataObjectOriginal[i];
	// }
	// }
	// return null;
	// }

	/**
	 * Returns the first instance of a ShapeFile found in the data set,
	 * searching from last to first, or else null if none exists.
	 * 
	 * This first index is zero, the next one, and so on, the last being
	 * getNumberNumericAttributes() -1
	 */
	public double[] getNumericDataAsDouble(int numericArrayIndex) {
		Object dataNumeric = this.dataSetNumericAndSpatial[numericArrayIndex + 1];
		// because it is a string array of variable names
		double[] doubleData = null;
		if (dataNumeric instanceof double[]) {
			doubleData = (double[]) dataNumeric;
		} else if (dataNumeric instanceof int[]) {
			int[] intData = (int[]) dataNumeric;
			doubleData = new double[intData.length];
			for (int i = 0; i < intData.length; i++) {
				if (intData[i] == Integer.MIN_VALUE) {
					doubleData[i] = Double.NaN;
				} else {
					doubleData[i] = intData[i];
				}
			} // next i
		} else {
			throw new IllegalArgumentException(
					"Unable to parse values in column " + numericArrayIndex
							+ " as a number");
		}
		return doubleData;
	}

	/**
	 * Returns a double where the arrayIndex is the nth array in
	 * the data set, and obs is the nth observation in that array.
	 * 
	 * note: this is a look into the "raw" data, and does not skip the 
	 * variable names array
	 */

	public double getValueAsDouble(int arrayIndex, int obs) {
		Object dataNumeric = this.dataObjectOriginal[arrayIndex]; 
		double[] doubleData = null;
		double doubleVal = Double.NaN;
		if (dataNumeric instanceof double[]) {
			doubleData = (double[]) dataNumeric;
			doubleVal = doubleData[obs];
		} else if (dataNumeric instanceof int[]) {
			int[] intData = (int[]) dataNumeric;
			doubleVal = intData[obs];
		} else {
			throw new IllegalArgumentException(
					"Unable to parse values in column " + arrayIndex
							+ " as a number");
		}
		return doubleVal;
	}
	/**
	 * Returns a double where the numericArrayIndex is the nth numeric array in
	 * the data set, and obs is the nth observation in that array.
	 */

	public double getNumericValueAsDouble(int numericArrayIndex, int obs) {
		Object dataNumeric = this.dataSetNumericAndSpatial[numericArrayIndex + 1]; // we
																					// skip
																					// the
																					// first
																					// one
		double[] doubleData = null;
		double doubleVal = Double.NaN;
		if (dataNumeric instanceof double[]) {
			doubleData = (double[]) dataNumeric;
			doubleVal = doubleData[obs];
		} else if (dataNumeric instanceof int[]) {
			int[] intData = (int[]) dataNumeric;
			doubleVal = intData[obs];
		} else {
			throw new IllegalArgumentException(
					"Unable to parse values in column " + numericArrayIndex
							+ " as a number");
		}
		return doubleVal;
	}

	/**
	 * Returns the name of the nth numeric array.
	 */

	public String getNumericArrayName(int arrayPlace) {
		String[] names = (String[]) this.dataSetNumericAndSpatial[0];
		return names[arrayPlace];
	}

	/**
	 * All initialization work should be done here.
	 */
	private void init(Object[] data) {
		if (data == null) {
			return;
		}
		if (!(data[0] instanceof String[])) {
			throw new IllegalArgumentException(
					"Data sets passed to DataSetForApps "
							+ "must begin with String[], with the "
							+ "length of the array equal to the "
							+ "number of attribute arrays that follow");
		}

		this.attributeNames = (String[]) data[0];
		this.dataSetFull = new Object[this.dataObjectOriginal.length + 1]; // plus
																			// one
																			// for
																			// the
																			// attribute
																			// names
																			// place
		this.dataSetFull[0] = attributeNames;

		int len = attributeNames.length;
		dataType = new int[len];
		numNumericAttributes = 0;
		for (int i = 0; i < len; i++) {

			if (data[i + 1] instanceof String[]) {
				String attrName = this.attributeNames[i].toLowerCase();
				if (attrName.endsWith("name")) {
					dataType[i] = DataSetForApps.TYPE_NAME;
					this.observationNames = (String[]) data[i + 1];
				}
			} else if (data[i + 1] instanceof double[]) {
				dataType[i] = DataSetForApps.TYPE_DOUBLE;
				numNumericAttributes++;
			} else if (data[i + 1] instanceof int[]) {
				dataType[i] = DataSetForApps.TYPE_INTEGER;
				numNumericAttributes++;
			} else if (data[i + 1] instanceof boolean[]) {
				dataType[i] = DataSetForApps.TYPE_BOOLEAN;
				numNumericAttributes++;
			} else {
				dataType[i] = DataSetForApps.TYPE_NONE;
			}

			this.dataSetFull[i + 1] = data[i + 1];
		}

		for (int i = 0; i < data.length; i++) {
			if (data[i] instanceof Shape[]) {
				Shape[] temp = ((Shape[]) data[i]);
				
				if (temp[0] instanceof GeneralPathLine) {
					this.spatialType = DataSetForApps.SPATIAL_TYPE_LINE;
				} else {
					this.spatialType = DataSetForApps.SPATIAL_TYPE_POLYGON; // the
																			// default
					break;
				}
			} else if (data[i] instanceof Point2D[]) {
				this.spatialType = DataSetForApps.SPATIAL_TYPE_POINT;

				break;
			}
		}

		int otherInfo = data.length - 1 - len; // Info objects are arrays
												// besides attribute object,
												// data objects and observ name
												// object.
		this.dataSetNumericAndSpatial = new Object[numNumericAttributes + 2
				+ otherInfo];
		this.dataSetNumeric = new Object[numNumericAttributes];
		if (otherInfo > 0) {
			for (int i = 0; i < otherInfo; i++) {
				dataSetNumericAndSpatial[numNumericAttributes + 2 + i] = data[len
						+ 1 + i];
				dataSetFull[len + 2 + i] = data[len + 1 + i];
			}
		}
		attributeNamesNumeric = new String[numNumericAttributes];
		int dataTypeIndex = 0;
		for (int i = 0; i < numNumericAttributes; i++) {
			while ((dataType[dataTypeIndex]) < 1) {
				dataTypeIndex++;
			}
			this.dataSetNumericAndSpatial[i + 1] = data[dataTypeIndex + 1];
			this.dataSetNumeric[i] = data[dataTypeIndex + 1];
			this.attributeNamesNumeric[i] = attributeNames[dataTypeIndex];
		
			dataTypeIndex++;
		}
		// The first object in dataObject array is attribute names.
		dataSetNumericAndSpatial[0] = this.attributeNamesNumeric;
		// Reserve an object in array for obervation names. The position is
		// after
		// all other data, including spatial data.
		if (this.observationNames != null) {
			dataSetNumericAndSpatial[numNumericAttributes + 1] = observationNames;
			dataSetFull[len + 1] = observationNames;
		} else {
			dataSetNumericAndSpatial[numNumericAttributes + 1] = null;
			dataSetFull[len + 1] = observationNames;
		}

		// set the number of observations
		if (dataType[0] == DataSetForApps.TYPE_NAME) {
			this.numObservations = ((String[]) dataObjectOriginal[1]).length;
		} else if (dataType[0] == DataSetForApps.TYPE_DOUBLE) {
			this.numObservations = ((double[]) dataSetNumericAndSpatial[1]).length;
		} else if (dataType[0] == DataSetForApps.TYPE_INTEGER) {
			this.numObservations = ((int[]) dataSetNumericAndSpatial[1]).length;
		} else if (dataType[0] == DataSetForApps.TYPE_BOOLEAN) {
			this.numObservations = ((boolean[]) dataSetNumericAndSpatial[1]).length;
		}

	}

	public DataSetForApps appendDataSet(DataSetForApps newData) {
		DataSetForApps returnDataSetForApps = null;
		if (this.dataObjectOriginal == null) {
			returnDataSetForApps = new DataSetForApps();
			returnDataSetForApps.init(newData.getDataObjectOriginal());
			return returnDataSetForApps;
		}
		String[] newNames = newData.getAttributeNamesOriginal();
		String[] oldNames = this.getAttributeNamesOriginal();
		String[] concatNames = new String[newNames.length + oldNames.length];
		// get the names
		for (int i = 0; i < oldNames.length; i++) {
			concatNames[i] = oldNames[i];

		}
		for (int i = oldNames.length; i < concatNames.length; i++) {
			concatNames[i] = newNames[i - oldNames.length];
		}
		// get the named arrays
		Object[] newObjects = newData.getNamedArrays();
		Object[] oldObjects = this.getNamedArrays();
		Object[] concatObjects = new Object[newObjects.length
				+ oldObjects.length];
		for (int i = 0; i < oldObjects.length; i++) {
			concatObjects[i] = oldObjects[i];

		}
		for (int i = oldObjects.length; i < concatObjects.length; i++) {
			concatObjects[i] = newObjects[i - oldObjects.length];
		}
		// get the "other" objects (spatial etc.)

		Object[] newOtherObjects = newData.getOtherData();

		Object[] oldOtherObjects = this.getOtherData();
		Object[] concatOtherObjects = new Object[newOtherObjects.length
				+ oldOtherObjects.length];
		for (int i = 0; i < oldOtherObjects.length; i++) {
			concatOtherObjects[i] = oldOtherObjects[i];

		}
		for (int i = oldOtherObjects.length; i < concatOtherObjects.length; i++) {
			concatOtherObjects[i] = newOtherObjects[i - oldOtherObjects.length];
		}

		// full up a return array
		Object[] returnArray = new Object[1 + concatObjects.length
				+ concatOtherObjects.length];

		returnArray[0] = concatNames;
		for (int i = 1; i < concatObjects.length + 1; i++) {
			returnArray[i] = concatObjects[i - 1];
		}
		for (int i = 1 + concatObjects.length; i < returnArray.length; i++) {
			returnArray[i] = concatOtherObjects[i - 1 - concatObjects.length];
		}

		returnDataSetForApps = new DataSetForApps(returnArray);
		return returnDataSetForApps;

	}
	public DataSetForApps prependDataSet(DataSetForApps newData) {
		DataSetForApps returnDataSetForApps = null;
		if (this.dataObjectOriginal == null) {
			returnDataSetForApps = new DataSetForApps();
			returnDataSetForApps.init(newData.getDataObjectOriginal());
			return returnDataSetForApps;
		}
		String[] newNames = newData.getAttributeNamesOriginal();
		String[] oldNames = this.getAttributeNamesOriginal();
		String[] concatNames = new String[newNames.length + oldNames.length];
		// get the names
		for (int i = 0; i < newNames.length; i++) {
			concatNames[i] = newNames[i];

		}
		for (int i = newNames.length; i < concatNames.length; i++) {
			concatNames[i] = oldNames[i - newNames.length];
		}
		// get the named arrays
		Object[] newObjects = newData.getNamedArrays();
		Object[] oldObjects = this.getNamedArrays();
		Object[] concatObjects = new Object[newObjects.length
				+ oldObjects.length];
		for (int i = 0; i < newObjects.length; i++) {
			concatObjects[i] = newObjects[i];

		}
		for (int i = newObjects.length; i < concatObjects.length; i++) {
			concatObjects[i] = oldObjects[i - newObjects.length];
		}
		// get the "other" objects (spatial etc.)

		Object[] newOtherObjects = newData.getOtherData();

		Object[] oldOtherObjects = this.getOtherData();
		Object[] concatOtherObjects = new Object[newOtherObjects.length
				+ oldOtherObjects.length];
		for (int i = 0; i < newOtherObjects.length; i++) {
			concatOtherObjects[i] = newOtherObjects[i];

		}
		for (int i = newOtherObjects.length; i < concatOtherObjects.length; i++) {
			concatOtherObjects[i] = oldOtherObjects[i - newOtherObjects.length];
		}

		// full up a return array
		Object[] returnArray = new Object[1 + concatObjects.length
				+ concatOtherObjects.length];

		returnArray[0] = concatNames;
		for (int i = 1; i < concatObjects.length + 1; i++) {
			returnArray[i] = concatObjects[i - 1];
		}
		for (int i = 1 + concatObjects.length; i < returnArray.length; i++) {
			returnArray[i] = concatOtherObjects[i - 1 - concatObjects.length];
		}

		returnDataSetForApps = new DataSetForApps(returnArray);
		return returnDataSetForApps;

	}

	/***************************************************************************
	 * Following methods are added by Diansheng.
	 * 
	 **************************************************************************/
	// Different from the above simiar method only in: numericArrayIndex -->
	// numericArrayIndex+1
	public double getNumericValueAsDoubleSkipColNames(int numericColumnIndex,
			int row) {
		Object dataNumeric = this.dataSetNumericAndSpatial[numericColumnIndex + 1];
		double[] doubleData = null;
		double doubleVal = Double.NaN;
		if (dataNumeric instanceof double[]) {
			doubleData = (double[]) dataNumeric;
			doubleVal = doubleData[row];
		} else if (dataNumeric instanceof int[]) {
			int[] intData = (int[]) dataNumeric;
			doubleVal = intData[row];
		} else {
			String temp = ( (String[]) dataNumeric)[row];
			logger.finest("\n" + dataNumeric.getClass() + "=" + temp +  ", col=" + (numericColumnIndex + 1) + "(d=" + this.numNumericAttributes + "), row=" + row + "\n");
			throw new IllegalArgumentException(
					"Unable to parse values in column " + numericColumnIndex
							+ " as a number");
		}
		return doubleVal;
	}

	public void setNumericValueAsDoubleSkipColNames(int numericColumnIndex,
			int row, double value) {
		Object dataNumeric = this.dataSetNumericAndSpatial[numericColumnIndex + 1];
		if (dataNumeric instanceof double[]) {
			((double[]) dataNumeric)[row] = value;
		} else if (dataNumeric instanceof int[]) {
			((int[]) dataNumeric)[row] = (int) value;
		} else {
			throw new IllegalArgumentException(
					"Unable to set values in column " + numericColumnIndex
							+ " as a number");
		}
	}

	public String[] makeUniqueNames(String[] inputNames) {
		String[] outputNames = new String[inputNames.length];
		// let's use an ArrayList
		// this is not super efficient, but I don't expect this to be called
		// very often
		// i.e. it might be best to call this method once per data set, not once
		// per
		// component
		ArrayList nameList = new ArrayList();
		for (int i = 0; i < inputNames.length; i++) {
			if (nameList.contains(inputNames[i])) {
				outputNames[i] = outputNames[i] + "_2";
			} else {
				outputNames[i] = inputNames[i];
			}
		}

		return outputNames;

	}

	public String makeUniqueName(String duplicatedName, ArrayList nameList) {
		return null;
	}

	public void addColumn(String columnName, float[] columnData) {
		// XXX placeholder
		throw new UnsupportedOperationException(
				"DataSetForApps is being extended to support this method, but it's not there yet, sorry!");

	}

	public void addColumn(String columnName, double[] columnData) {
		//I guess we add the new data in at the end....
		//note that clients with refrences to the primitve arrays will
		//not experience disruption if this method is called, 
		//but those with a reference to a derived array wouldn't 
		//be happy.
		String[] name = new String[1];
		name[0] = columnName;
		Object[] allData = new Object[2];
		allData[0] = name;
		allData[1] = columnData;
		
		DataSetForApps dataSet = new DataSetForApps(allData);
		DataSetForApps newDataSet = this.prependDataSet(dataSet);
		this.setDataObject(newDataSet.getDataObjectOriginal());
		this.fireTableChanged();

	}
	  /**
	   * Notify all listeners that have registered interest for
	   * notification on this event type. The event instance
	   * is lazily created using the parameters passed into
	   * the fire method.
	   * @see EventListenerList
	   * 
	   * note: at this point, always fires an insertion
	   */
	  public void fireTableChanged() {
		  if (logger.isLoggable(Level.FINEST)){
			  logger.finest("DataSetForApps, firing table changed");
		  }

		// Guaranteed to return a non-null array
	    Object[] listeners = listenerList.getListenerList();
	    TableModelEvent e = null;

	    // Process the listeners last to first, notifying
	    // those that are interested in this event
	    for (int i = listeners.length - 2; i >= 0; i -= 2) {
	      if (listeners[i] == TableModelListener.class) {
	        // Lazily create the event:
	        if (e == null) {
	        	//TableModelEvent(TableModel source, int firstRow, int lastRow, int column, int type) 
	          e = new TableModelEvent(this,0,this.getNumObservations(),this.getObservationNames().length,TableModelEvent.INSERT);
	        }

	        ( (TableModelListener) listeners[i + 1]).tableChanged(e);
	      }
	    } //next i
	  }


	public Object getId(int index) {
		return new Integer(index);
	}

	public Vector getIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getIndexById(Object id) {
		// TODO Auto-generated method stub
		return 0;
	}
	public void addTableModelListener(TableModelListener l) {
		this.listenerList.add(TableModelListener.class, l);
		
	}


	public void removeTableModelListener(TableModelListener l) {
		this.listenerList.remove(TableModelListener.class, l);
		
	}
//start TableModel methods
	public Class getColumnClass(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isCellEditable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setValueAt(Object arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}



	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}
//	end TableModel events	

	/**
	 * @return the listenerList
	 */
	public EventListenerList getListenerList() {
		return listenerList;
	}

	/**
	 * @param listenerList the listenerList to set
	 */
	public void setListenerList(EventListenerList listenerList) {
		this.listenerList = listenerList;
	}

}
