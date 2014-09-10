//package EvalSurfQuality;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jv.object.PsDebug;
import jv.object.PsUpdateIf;
import jv.project.PjProject_IP;

/**
 * Info panel of tutorial project. Each project, as well as each geometry, may have
 * an info panel for inspecting and steering the instance. Modifications in the panel
 * should result in a call to <code>project.update(Object)</code>.
 * <p>
 * Panel adds simple sliders for modifying integer values of the project. The sliders
 * are children of the project and send <code>update(Object)</code> whenever they change
 * by user interaction.
 * <p>
 * This info panel is optional and can be removed without any changes in the project.
 * Each info panel must follow special naming conventions such that JavaView is able to
 * show it automatically, or on request. The class name must be identical to the class
 * name of the corresponding class with the suffix '_IP' attached. IP means InfoPanel.
 * 
 * @see			jv.project.PjProject
 * @author		Konrad Polthier
 * @version		28.11.00, 1.50 revised (kp) Applet simplified for beginners.<br>
 *					20.03.00, 1.10 revised (kp) Sliders moved to project.<br>
 *					05.08.99, 1.00 created (kp)
 */
public class PjEvalSurfQual_IP extends PjProject_IP implements ActionListener {
	protected	PjEvalSurfQual		PjEvalSurfQual;
	//protected	Panel				sliderPanel;
	//protected	Label				lArea;
	protected	Button			    bHist;
	protected	Button			    bReset;

	public PjEvalSurfQual_IP() {
		super();
		// Add title label which will later display the title of the project.
		addTitle("");

		// Panel to contain sliders of u-lines and v-lines.
		/*sliderPanel = new Panel();
		sliderPanel.setLayout(new GridLayout(2, 1));
		add(sliderPanel);*/

		// Panel with label to display the area of the current surface.
		//Panel pArea = new Panel();
		//pArea.setLayout(new GridLayout(1, 2));
		//pArea.add(new Label("Surface Area"));
		//lArea = new Label();
		//pArea.add(lArea);
		//add(pArea);
		
		// Add histogram button at bottom
		Panel histPanel = new Panel();
		histPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		bHist = new Button("Histogram");
		bHist.addActionListener(this);
		histPanel.add(bHist);
		add(histPanel);
		
		// Add reset button at bottom
		Panel buttonPanel = new Panel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		bReset = new Button("Reset");
		bReset.addActionListener(this);
		buttonPanel.add(bReset);
		add(buttonPanel);

		if (getClass() == PjEvalSurfQual_IP.class)
			init();
	}
	public void init() {
		super.init();
	}
	public void setParent(PsUpdateIf parent) {
		super.setParent(parent);
		PjEvalSurfQual = (PjEvalSurfQual)parent;
		setTitle(PjEvalSurfQual.getName());
		// Add info panels of integers to panel of sliders.
		/*sliderPanel.add(PjEvalSurfQual.m_numULines.getInfoPanel());
		sliderPanel.add(PjEvalSurfQual.m_numVLines.getInfoPanel());*/
	}
	/**
	* Here we arrive from outside world of this panel, e.g. if
	* project has changed somewhere else and must update its panel. Such an update
	* is automatically by superclasses of PjProject.
	*/
	public boolean update(Object event) {
		if (PjEvalSurfQual==null) {
			if (PsDebug.WARNING) PsDebug.warning("missing parent, setParent not called");
			return false;
		}
		/*if (event == PjEvalSurfQual) {
			lArea.setText(String.valueOf(PjEvalSurfQual.m_geom.getArea()));
			return true;
		}*/
		return super.update(event);
	}
	/**
	 * Handle action events invoked from buttons, menu items, text fields.
	 */
	public void actionPerformed(ActionEvent event) {
		if (PjEvalSurfQual==null)
			return;
		Object source = event.getSource();
		if (source == bReset) {
			PjEvalSurfQual.init();
			PjEvalSurfQual.update(this);
			update(this);
		}
		if (source == bHist) {
			PjEvalSurfQual.startHist();
			PjEvalSurfQual.update(this);
		}
	}
}
