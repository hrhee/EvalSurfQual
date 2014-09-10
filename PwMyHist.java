
//package jvx.geom;

import jv.geom.PgElementSet;
import jv.geom.PgVectorField;
import jv.object.PsConfig;
import jv.object.PsDebug;
import jv.vecmath.PdVector;
import jvx.project.PjWorkshop;

import jvx.geom.PgUtil;
import jvx.number.PuHistogram;

/**
 * Workshop shows histogram information of geometries.
 * 
 * @see			jvx.number.PuHistogram
 * @author		Konrad Polthier
 * @version		02.09.09, 2.00 moved   (kp) Moved to jvx from dev.<br>
 * 				27.05.07, 1.10 revised (kp) Adjusted to modified histogram.<br>
 *					21.05.07, 1.00 created (kp) 
 * @since 		JavaView 3.99.045 moved to jvx from dev.
 */
public class PwMyHist extends PjWorkshop {
	public		final static int		ITEM_EDGE_LENGTH			= 0;
	public		final static int		ITEM_ELEMENT_AREA			= 1;
	public		final static int		ITEM_VERTEX_ANGLE			= 2;
	public		final static int		ITEM_SCALAR_FIELD			= 3;
	public		final static int		ITEM_NORM_VECTOR_FIELD	= 4;
	protected	String []				m_itemArr = new String [] {
		"Edge Length", "Element Area", "Vertex Angle", "First Scalar Field", "Norm of Vector Field" };

	/** Base geometry. */
	protected	PgElementSet			m_elemSet;
	/** Vector containing the samples. */
	protected	PuHistogram				m_histogram;
	/** Item type determines the displayed data. */
	protected	int						m_itemType;
	/** Vector containing the samples. */
	protected	PdVector					m_samples;

	/** Constructor */
	public PwMyHist() {
		super(PsConfig.getMessage(true, 50000, "Histogram"));
		
		m_samples	= new PdVector();
	
		m_histogram	= new PuHistogram("Histogram", this);
		
		if (getClass() == PwMyHist.class) {
			init();
		}
	}
	/** Initialization */
	public void init() {
		super.init();

		m_histogram.setClassType(PuHistogram.NUMBINS_USER);
		m_histogram.setEnabledShowAxisLabels(true);
		m_histogram.setEnabledShowAxisTicks(true);
		m_histogram.setEnabledShowCounts(true);
		setItemType(ITEM_ELEMENT_AREA);
	}
	/**
	 * Reset workshop and geometry to original state.
	 */
	public void reset() {
		super.reset();
	}
	/**
	 * Close workshop and remove workshop from geometry as update listener.
	 * Instance variables of this workshop are nulled.
	 */
	public void close() {
		if (m_elemSet != null)
			m_elemSet.removeUpdateListener(this);
		m_elemSet	= null;
	}
	/**
	 * Assign geometry to be analyzed.
	 * @param 	geom		geometry for analysis
	 */
	public void setGeometry(PgElementSet geom) {
		super.setGeometry(geom);
		m_elemSet	= geom;
		m_elemSet.addUpdateListener(this);
		m_histogram.setTitle("Histogram of "+geom.getName());
	}

	public boolean update(Object event) {
		if (event == m_elemSet) {
			computeSamples();
			m_histogram.update(m_histogram);
			return true;
		} else if (event == m_histogram) {
			return super.update(this);
		}
		return false;
	}

	/** Compute samples based on current itemType. */
	private void computeSamples() {
		if (m_itemType == ITEM_EDGE_LENGTH) {
			PgUtil.getEdgeLengths(m_elemSet, m_samples, false);
		} else if (m_itemType == ITEM_ELEMENT_AREA) {
			int noe	= m_elemSet.getNumElements();
			m_samples.setSize(noe);
			for (int i=0; i<noe; i++) {
				m_samples.setEntry(i, m_elemSet.getAreaOfElement(i));
			}
		} else if (m_itemType == ITEM_VERTEX_ANGLE) {
			PgUtil.getVertexAngles(m_elemSet, m_samples, false);
		} else if (m_itemType == ITEM_SCALAR_FIELD) {
			boolean bFound	= false;
			int numVfs		= m_elemSet.getNumVectorFields();
			for (int i=0; i<numVfs; i++) {
				PgVectorField vf	= m_elemSet.getVectorField(i);
				if (vf.getDimOfVectors() != 1)
					continue;
				PdVector [] vec	= vf.getVectors();
				if (vec == null)
					continue;
				int numVec			= vec.length;
				m_samples.setSize(numVec);
				for (int j=0; j<numVec; j++) {
					m_samples.setEntry(j, vec[j].getEntry(0));
				}
				bFound	= true;
				break;
			}
			if (!bFound) {
				if (PsDebug.WARNING) PsDebug.warning("missing scalar field");
				m_samples.setSize(0);
			}
		} else if (m_itemType == ITEM_NORM_VECTOR_FIELD) {
			boolean bFound	= false;
			int numVfs		= m_elemSet.getNumVectorFields();
			for (int i=0; i<numVfs; i++) {
				PgVectorField vf	= m_elemSet.getVectorField(i);
				PdVector [] vec	= vf.getVectors();
				if (vec == null)
					continue;
				int numVec			= vec.length;
				m_samples.setSize(numVec);
				for (int j=0; j<numVec; j++) {
					m_samples.setEntry(j, vec[j].length());
				}
				bFound	= true;
				break;
			}
			if (!bFound) {
				if (PsDebug.WARNING) PsDebug.warning("missing vector field");
				m_samples.setSize(0);
			}
				
		}
		m_histogram.setSamples(m_samples);
	}
	/**
	 * Get the item type which determines the displayed data.
	 * Possible types are like {@link PwMyHist#ITEM_EDGE_LENGTH PwMyHist#ITEM_EDGE_LENGTH} etc.
	 * @return 		the current item type
	 */
	public int getItemType() {
		return m_itemType;
	}
	/**
	 * Set the item type which determines the displayed data.
	 * Possible types are like {@link PwMyHist#ITEM_EDGE_LENGTH PwMyHist#ITEM_EDGE_LENGTH} etc.
	 * @param 	itemType 		the new item type
	 */
	public void setItemType(int itemType) {
		m_itemType = itemType;
		m_histogram.setXAxisLabel(m_itemArr[itemType]);
	}
}