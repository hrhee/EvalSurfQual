//package EvalSurfQuality;


import java.awt.Color;

import jv.geom.PgElementSet;
import jv.project.PjProject;

import jvx.project.PjWorkshop;
import jvx.project.PjWorkshop_Dialog;
import jvx.project.PjWorkshop_IP;
import jvx.geom.PwHistogram;

import jv.object.PsDialog;

/**
 * Demo project for working with JavaView projects and for subclassing of PgElementSet.
 * This sample project shows how to handle timer events and supplies an own computation
 * method for creating the coordinates of an (animated) surface.
 * <p>
 * In this project a classes is embedded in a JavaView project. Nearly all applications
 * in JavaView are embedded into projects, each with a special functionality. Such
 * projects can be reused as a building blocks in other cirumstances, similar to a class.
 * In fact, a project is a subclass  of {@link jv.project.PjProject jv.project.PjProject}
 * which allows to manage a set of geometries, a display(s), and other projects, and
 * to react on animation and pick events.
 * <p>
 * This project also shows how to work with animations. Here an instance of class
 * {@link jv.anim.PsAnimation PsAnimation} is created where the current project registers
 * itself as dynamic object to be able to receive time events. Whenever the time changes
 * the animation manager invokes the method {@link #setTime(PsTimeEvent) setTime(PsTimeEvent)} of
 * this project.
 * 
 * @see			jv.anim.PsAnimation
 * @see			jv.geom.PgElementSet
 * @see			jv.project.PjProject
 * @author		Konrad Polthier
 * @version		25.02.01, 1.51 revised (kp) Obsolete call to method setAnimation() removed.<br>
 *					28.11.00, 1.50 revised (kp) Applet simplified for beginners.<br>
 *					05.08.99, 1.00 created (kp)
 */
public class PjEvalSurfQual extends PjProject {
	protected	PgElementSet	m_geom;			// Surface of geometry
	protected   PwMyHist     m_pwMyHist;
	//protected	PuInteger	m_numULines;	// Discretization along u parameter line with slider
	//protected	PuInteger	m_numVLines;	// Discretization along v parameter line with slider
	
	/**
	 * Constructor, without arguments to allow loading of project from menu.
	 */
	public PjEvalSurfQual() {
		super("PjEvalSurfQual");
		m_geom = new PgElementSet(3);				// Create an own geometry
		//m_geom.setName("");			// Optionally, give it a name

		m_pwMyHist = new PwMyHist();
		
		init();										// Call to init() is required.
	}
	/**
	 * Do initialization of data structures; method is also used to reset instances.
	 */
	public void init() {
		super.init();
		
		m_geom.setName("Torus");
		// Compute coordinates and mesh of a geometry
		m_geom.computeTorus(10, 10, 2., 1.);
		
	}
	/**
	 * Start project, e.g. start an animation. Method is called once when project is
	 * selected in PvViewer#selectProject(). Method is optional. For example, if an
	 * applet calls the start() method of PvViewer, then PvViewer tries to invoke
	 * the start() method of the currently selected project.
	 */
	public void start() {
		addGeometry(m_geom);
		selectGeometry(m_geom);


		super.start();
	}	
	
	public void startHist() {
		m_pwMyHist.setGeometry(m_geom);
		m_pwMyHist.update(m_geom);
		PsDialog dialog;
		dialog = new PjWorkshop_Dialog(false);
		dialog.setParent(m_pwMyHist);
		dialog.update(m_pwMyHist);
		dialog.setVisible(true);
	}

	/**
	 * Update method of project to react on changes in its panel or of its children.
	 * This method is optional, but required if project is parent of a child.
	 * Project becomes parent if child calls <code>child.setParent(this)</code> with
	 * this project as argument. For example, see the constructor of PjEvalSurfQual.
	 * Project must react on child events, or forward them to its superclass.
	 * <p>
	 * Catch events of integer children and recompute surface.
	 */
	public boolean update(Object event) {
		if (event == getInfoPanel()) {
//			m_geom.computeSurface(m_numULines.getValue(), m_numVLines.getValue());	// Recompute surface
//			m_geom.update(m_geom);
			return super.update(this);
		} 
//			else if (event==m_numULines || event==m_numVLines) {
//			m_geom.computeSurface(m_numULines.getValue(), m_numVLines.getValue());	// Recompute surface
//			m_geom.update(m_geom);
//			return super.update(this);
//		}
		return super.update(event);
	}
}

