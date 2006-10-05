/*
 * org.openmicroscopy.shoola.env.ui.UIFactory
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

package org.openmicroscopy.shoola.env.ui;

//Java imports
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.env.Container;
import org.openmicroscopy.shoola.env.LookupNames;

/** 
 * Factory for the various windows and widgets used within the container.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:a.falconi@dundee.ac.uk">
 * 					a.falconi@dundee.ac.uk</a>
 * @version 2.2 
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */

public class UIFactory 
{	
	
	/**
	 * Creates the splash screen that is used at initialization.
	 * 
     * @param listener  A listener for {@link SplashScreenView#cancel} button.
	 * @return	        The splash screen.
	 */
	public static SplashScreen makeSplashScreen(ActionListener listener)
	{
		return new SplashScreenProxy(listener);
	}
	
	/**
	 * Creates the {@link TaskBar}.
	 * 
	 * @param c	Reference to the singleton {@link Container}.
	 * @return	The {@link TaskBar}.
	 */
	public static TaskBar makeTaskBar(Container c)
	{
		TaskBarManager tbm = new TaskBarManager(c);
		return tbm.getView();
	}
	
	/**
	 * Creates the {@link UserNotifier}.
	 *
	 * @return	The {@link UserNotifier}.
	 */
	public static UserNotifier makeUserNotifier()
	{
		return new UserNotifierImpl();
	}
	
    /**
     * Returns an array of the available servers.
     * 
     * @return See above.
     */
    public static String[] getServersAsArray()
    {
        String[] listOfServers = null;
        Properties defaultProp = new Properties();
        try {
            FileInputStream in = new FileInputStream(
                                        LookupNames.OMERO_PROPERTIES);
            defaultProp.load(in);
            in.close(); 
        } catch (Exception e) {
            // TODO: handle exception
        }
        String s = defaultProp.getProperty(LookupNames.OMERO_SERVER);
        if (s == null || s.length() == 0) {
            listOfServers = new String[1];
            listOfServers[0] = LookupNames.DEFAULT_SERVER;
        } else {
            List l = Arrays.asList(s.split(LookupNames.SERVER_NAME_SEPARATOR, 
                                            0));
            if (l == null) {
                listOfServers = new String[1];
                listOfServers[0] = LookupNames.DEFAULT_SERVER;
            } else {
                listOfServers = new String[l.size()+1];
                Iterator i = l.iterator();
                int index = 0;
                while (i.hasNext()) {
                    listOfServers[index] = ((String) i.next()).trim();
                    index++;
                }
                listOfServers[index] = LookupNames.DEFAULT_SERVER;
            }
        }   
        return listOfServers;
    }
    
}
