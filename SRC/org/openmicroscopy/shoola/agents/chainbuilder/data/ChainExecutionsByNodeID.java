/*
 * org.openmicroscopy.shoola.agents.chainbuilder.data.ChainExecutionsByNodeId
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

package org.openmicroscopy.shoola.agents.chainbuilder.data;

//Java imports
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.env.data.model.ChainExecutionData;
import org.openmicroscopy.shoola.env.data.model.NodeExecutionData;
import org.openmicroscopy.shoola.env.data.model.AnalysisNodeData;

/** 
* A map that has executions for a given chain, as filtered from a larger 
* set of chain executions. Given a raw list of chain executions,
* this structure will map each module (by ID) to a list of module executions 
* corresponding to the chain executions in the list.
* 
* @author  Harry Hochheiser &nbsp;&nbsp;&nbsp;&nbsp;
* 				<a href="mailto:hsh@nih.gov">hsh@nih.gov</a>
*
* @version 2.2
* <small>
* (<b>Internal version:</b> $Revision$ $Date$)
* </small>
* @since OME2.2
*/
public class ChainExecutionsByNodeID {
	
	private HashMap map = null;
	
	public ChainExecutionsByNodeID(Collection execs) {
		if (execs == null) 
			return;
		
		map = new HashMap();
		
		ChainExecutionData exec;
		// build it up. iterate over the chain executions.
		Iterator iter = execs.iterator();
		while (iter.hasNext()) {
			exec = (ChainExecutionData) iter.next();
			addChainExecution(exec);
		}
	}
	
	/* for each execution, we find the node and therefore the module
	 * and the module execution
	 */
	private void addChainExecution(ChainExecutionData exec) {
		NodeExecutionData nodeExec;
		
		Collection nodeExecs = exec.getNodeExecutions();
		if (nodeExecs == null) 
			return;
		Iterator iter = nodeExecs.iterator();
		while (iter.hasNext()) {
			nodeExec = (NodeExecutionData) iter.next();
			addNodeExecution(nodeExec);
		}
	}
	
	/** add the individual node execution */
	private void addNodeExecution(NodeExecutionData exec) {
		AnalysisNodeData node = exec.getAnalysisNode();
		Integer id = new Integer(node.getID());
		
		// get the hash contents for this module
		Object obj = map.get(id);
		Vector nexes; // the vector that will contain the mexes for this 
					// module
		if (obj == null) 
			nexes = new Vector();
		else
			nexes = (Vector) obj;
		nexes.add(exec);
		map.put(id,nexes);		
	}
	
	/** 
	 * Get the list of mexes for a node with a given id
	 * @param id
	 * @return
	 */
	public Vector getNexes(int id) {
		if (!hasExecs())
			return null;
		Object obj = map.get(new Integer(id));
		if (obj == null)
			return null;
		return (Vector) obj;
	}
	
	/****
	 * Get the list of mexes for a given module
	 * @return
	 */
	public Vector getNexes(AnalysisNodeData node) {
		return getNexes(node.getID());
	}
	
	/** empty check */
	public boolean hasExecs() {
		return (map != null);
	}

}