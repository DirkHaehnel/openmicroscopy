/*
 * org.openmicroscopy.shoola.agents.hiviewer.view.HiViewerToolBar
 *
 *------------------------------------------------------------------------------
 *
 *  Copyright (C) 2004 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.agents.hiviewer.view;


//Java imports
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

//Third-party libraries

//Application-internal dependencies


/** 
 * The tool bar of {@link HiViewerWin}.
 * 
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author	Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * 	<a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $ $Date: $)
 * </small>
 * @since OME2.2
 */
class HiViewerToolBar
    extends JPanel
{

    /** Size of the horizontal box. */
    private static final Dimension HBOX = new Dimension(100, 16);
    
    /** Reference to the control. */
    private HiViewerControl controller;
    
    /** Reference to the model. */
    private HiViewerModel	model;
    
    /** 
     * Builds the toolbar hosting the various <code>View</code>s.
     * 
     * @return See above.
     */
    private JToolBar buildViewBar()
    {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.setRollover(true);
        bar.setBorder(null);
        JToggleButton b = new JToggleButton(
                controller.getAction(HiViewerControl.TREE_VIEW));
        bar.add(b);
        
        b = new JToggleButton(
                controller.getAction(HiViewerControl.CLIPBOARD_VIEW));
        b.setSelected(true);
        bar.add(b);
        return bar;
    }
    
    /** 
     * Builds the toolbar hosting the <code>View</code> controls.
     * 
     * @return See above.
     */
    private JToolBar buildManagementBar()
    {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.setRollover(true);
        bar.setBorder(null);
        ButtonGroup group = new ButtonGroup();
        JToggleButton b = new JToggleButton();
        //UIUtilities.unifiedButtonLookAndFeel(b);
        //b.setBorderPainted(true);
        b.setSelected(true);
        group.add(b);
        
        b.setAction(controller.getAction(HiViewerControl.SORT_BY_NAME));
        bar.add(b);
        b = new JToggleButton(
                controller.getAction(HiViewerControl.SORT_BY_DATE));
        group.add(b);
        //UIUtilities.unifiedButtonLookAndFeel(b);
        //b.setBorderPainted(true);
        bar.add(b);
        bar.add(new JSeparator(SwingConstants.HORIZONTAL));
        b = new JToggleButton(
                controller.getAction(HiViewerControl.ROLL_OVER));
        b.setSelected(model.isRollOver());
        bar.add(b);
        JButton button = 
        	new JButton(controller.getAction(HiViewerControl.ZOOM_IN));
        bar.add(button);
        button = new JButton(controller.getAction(HiViewerControl.ZOOM_OUT));
        bar.add(button);
        button = new JButton(controller.getAction(HiViewerControl.ZOOM_FIT));
        bar.add(button);
        return bar;
    }
    
    
    /** Builds and lays out the GUI. */
    private void buildGUI()
    {
        JPanel bars = new JPanel(), outerPanel = new JPanel();
        bars.setBorder(null);
        bars.setLayout(new BoxLayout(bars, BoxLayout.X_AXIS));
        bars.add(buildViewBar());
        bars.add(buildManagementBar());
        outerPanel.setBorder(null);
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.X_AXIS));
        outerPanel.add(bars);
        outerPanel.add(Box.createRigidArea(HBOX));
        outerPanel.add(Box.createHorizontalGlue());  
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(outerPanel);
    }
    
    /**
     * Creates a new instance.
     * 
     * @param controller    Reference to the control. 
     *                      Mustn't be <code>null</code>.
     * @param model    		Reference to the control. 
     *                      Mustn't be <code>null</code>.                 
     */
    HiViewerToolBar(HiViewerControl controller, HiViewerModel model)
    {
        if (controller == null) 
            throw new NullPointerException("No controller.");
        if (model == null) 
            throw new NullPointerException("No model.");
        this.controller = controller;
        this.model = model;
        buildGUI();
    }
    
}
