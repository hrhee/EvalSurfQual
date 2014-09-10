
//package jvx.geom;


import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import jv.object.PsConfig;
import jv.object.PsDialog;
import jv.object.PsUpdateIf;
import jvx.project.PjWorkshop_IP;

/**
 * Workshop inspector shows histogram information of geometries.
 * 
 * @see			jvx.number.PuHistogram_IP
 * @author		Konrad Polthier
 * @version		02.09.09, 2.00 moved   (kp) Moved to jvx from dev.<br>
 * 				21.05.07, 1.00 created (kp) 
 * @since 		JavaView 3.99.045 moved to jvx from dev.
 */
public class PwMyHist_IP extends PjWorkshop_IP implements ItemListener {
	protected	PwMyHist			m_pwMyHist;
	private		Panel					m_pHistogram;
	/** Set of geometry items for which histogram is available. */
	protected	Choice				m_cItemType;
	protected	Button				m_bReset;
	
	/** Constructor */
	public PwMyHist_IP() {
		super();
		setTitle(PsConfig.getMessage(true, 54000, "Histogram"));
		if (getClass() == PwMyHist_IP.class) {
			init();
		}
	}
	
	/** Initialization. */
	public void init() {
		super.init();
		setLayout(new BorderLayout());

		Panel pCheck	= new Panel(new GridLayout(1, 2));
		{
			Label	label = new Label("Selected Geometry Item");
			pCheck.add(label);
			
			m_cItemType	= new Choice();
			m_cItemType.addItemListener(this);
			pCheck.add(m_cItemType);
		}
		add(pCheck, BorderLayout.NORTH);

		m_pHistogram	= new Panel(new BorderLayout());
		add(m_pHistogram, BorderLayout.CENTER);
	}

	/** Get preferred size, for example, to ensure size of dialog. */
	public Dimension getDialogSize()		{ return new Dimension(500, 500); }
	
	/**
	 * Informational text on the usage of this workshop.
	 * @return		string with textual information.
	 */
	public String getNotice() {
		return PsConfig.getMessage(true, 58000,
			"Shows histogram of various entities of geometry."+
			" Histogram is recomputed whenever the geometry updates."+
			" Use scalar field inspector or vector field inspector to generate"+
			" additional data for inspection.");
	}
	/**
	 * Get information which bottom buttons a dialog should create
	 * when showing this info panel.
	 * <p>
	 * Subclasses may include superclass preferences by using, for example,
	 * <code>return super.getDialogButtons() | PsDialog.BUTTON_CLOSE;</code>.
	 *
	 * @return		identifier of the type of preferred buttons.
	 */
	protected int getDialogButtons()		{
		// return super.getDialogButtons() | PsDialog.BUTTON_RESET;
		return PsDialog.BUTTON_CLOSE;
	}
	
	/** Store parent noise workshop. */
	public void	setParent(PsUpdateIf parent) {
		super.setParent(parent);
		m_pwMyHist = (PwMyHist)parent;
		
		m_pHistogram.removeAll();
		m_pHistogram.add(BorderLayout.CENTER, m_pwMyHist.m_histogram.getInfoPanel());
		m_pHistogram.validate();
		
		m_cItemType.removeAll();
		for (int i=0; i<m_pwMyHist.m_itemArr.length; i++) {
			m_cItemType.add(m_pwMyHist.m_itemArr[i]);
		}
		
		validate();
	}
	/**
	 * Update the panel whenever the parent has changed somewhere else.
	 * Method is invoked from the parent or its superclasses.
	 */
	public boolean update (Object event) {
		if(event == m_pwMyHist) {
			if (m_pwMyHist.getItemType() != m_cItemType.getSelectedIndex()) {
				m_cItemType.select(m_pwMyHist.getItemType());
			}
			return true;
		} else {
			return super.update(event);
		}
	}
								 
	/** Handle Item Events invoked by Checkboxes. */
	public void itemStateChanged(ItemEvent event) {
		Object source = event.getSource();
		if (source == m_cItemType) {
			int selInd	= m_cItemType.getSelectedIndex();
			m_pwMyHist.setItemType(selInd);
			m_pwMyHist.m_histogram.setXAxisLabel(m_cItemType.getItem(selInd));
			m_pwMyHist.m_histogram.update(m_pwMyHist.m_histogram);
		}
	}
}