 /*
 * org.openmicroscopy.shoola.agents.editor.preview.AnnotationHandler 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2009 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.agents.editor.preview;


//Java imports
import pojos.FileAnnotationData;

//Third-party libraries

//Application-internal dependencies

/** 
 * Classes that need to handle file annotations from the server should 
 * implement this interface. 
 *
 * @author  William Moore &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:will@lifesci.dundee.ac.uk">will@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since 3.0-Beta4
 */
public interface AnnotationHandler
{

	/**
	 * Handles the return of a file annotation from the server. 
	 * This is called when the file annotation is returned. 
	 * 
	 * @param fileAnnotation The file to handle. Mustn't be <code>null</code>.
	 */
	public void handleAnnotation(FileAnnotationData fileAnnotation);
	
}
