/*
 * ome.formats.importer.gui.GuiCommonElements
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2008 University of Dundee. All rights reserved.
 *
 *
 *  This program is free software; you can redistribute it and/or modify
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

package ome.formats.importer;

import static omero.rtypes.rbool;
import static omero.rtypes.rstring;

import java.io.File;
import java.util.List;

import omero.grid.Import;
import omero.model.Annotation;
import omero.model.IObject;
import omero.model.Pixels;

public class ImportContainer
{
	private String reader;
    private String[] usedFiles;
    private Boolean isSPW;
	private File file;
    private Double[] userPixels;
    private String userSpecifiedName;
    private String userSpecifiedDescription;
    private boolean doThumbnails = true;
    private List<Annotation> customAnnotationList;
    private IObject target;

	public ImportContainer(File file,
			IObject target,
			Double[] userPixels, String reader, String[] usedFiles, Boolean isSPW)
	{
		this.file = file;
		this.target = target;
		this.userPixels = userPixels;
		this.reader = reader;
		this.usedFiles = usedFiles;
		this.isSPW = isSPW;
	}

	// Various Getters and Setters //

    /**
     * Retrieves whether or not we are performing thumbnail creation upon
     * import completion.
     * return <code>true</code> if we are to perform thumbnail creation and
     * <code>false</code> otherwise.
     * @since OMERO Beta 4.3.0.
     */
    public boolean getDoThumbnails()
    {
        return doThumbnails;
    }

    /**
     * Sets whether or not we are performing thumbnail creation upon import
     * completion.
     * @param v <code>true</code> if we are to perform thumbnail creation and
     * <code>false</code> otherwise.
     * @since OMERO Beta 4.3.0.
     */
    public void setDoThumbnails(boolean v)
    {
        doThumbnails = v;
    }

    /**
     * Retrieves the current custom image/plate name string.
     * @return As above. <code>null</code> if it has not been set.
     */
    public String getUserSpecifiedName()
    {
        return userSpecifiedName;
    }

    /**
     * Sets the custom image/plate name for import. If this value is left
     * null, the image/plate name supplied by Bio-Formats will be used.
     * @param v A custom image/plate name to use for all entities represented
     * by this container.
     */
    public void setUserSpecifiedName(String v)
    {
        userSpecifiedName = v;
    }

    /**
     * Retrieves the current custom image/plate description string.
     * @return As above. <code>null</code> if it has not been set.
     * @since OMERO Beta 4.2.1.
     */
    public String getUserSpecifiedDescription()
    {
        return userSpecifiedDescription;
    }

    /**
     * Sets the custom image/plate description for import. If this value is left
     * null, the image/plate description supplied by Bio-Formats will be used.
     * @param v A custom image/plate description to use for all images represented
     * by this container.
     * @since OMERO Beta 4.2.1.
     */
    public void setUserSpecifiedDescription(String v)
    {
        userSpecifiedDescription = v;
    }

    /**
     * The list of custom, user specified, annotations to link to all images
     * represented by this container.
     * @return See above.
     * @since OMERO Beta 4.2.1.
     */
    public List<Annotation> getCustomAnnotationList()
    {
        return customAnnotationList;
    }

    /**
     * Sets the list of custom, user specified, annotations to link to all
     * images represented by this container.
     * @param v The list of annotations to use.
     * @since OMERO Beta 4.2.1.
     */
    public void setCustomAnnotationList(List<Annotation> v)
    {
        customAnnotationList = v;
    }

    public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public String[] getUsedFiles() {
		return usedFiles;
	}

	public void setUsedFiles(String[] usedFiles) {
		this.usedFiles = usedFiles;
	}

	public Boolean getIsSPW() {
		return isSPW;
	}

	public void setIsSPW(Boolean isSPW) {
		this.isSPW = isSPW;
	}

	/**
	 * @return the File
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Package-private setter added during the 4.1 release to fix name ordering.
	 * A better solution would be to have a copy-constructor which also takes
	 * a chosen file.
	 */
	void setFile(File file)
	{
	    this.file = file;
	}

	public IObject getTarget()
	{
	    return target;
	}

	public void setTarget(IObject obj) {
	    this.target = obj;
	}

	public Double[] getUserPixels()
	{
        return userPixels;
    }

    public void setUserPixels(Double[] userPixels)
    {
        this.userPixels = userPixels;
    }

    public void fillData(Import data) {
        // TODO: These should possible be a separate option like
        // ImportUserSettings rather than mis-using ImportContainer.
        data.doThumbnails = rbool(getDoThumbnails());
        data.userSpecifiedTarget = getTarget();
        data.userSpecifiedName = rstring(getUserSpecifiedName());
        data.userSpecifiedDescription = rstring(getUserSpecifiedDescription());
        data.userSpecifiedAnnotationList = getCustomAnnotationList();
        if (getUserPixels() != null) {
            Double[] source = getUserPixels();
            double[] target = new double[source.length];
            for (int i = 0; i < source.length; i++) {
                if (source[i] == null) {
                    target = null;
                    break;
                }
                target[i] = source[i];
            }
            data.userSpecifiedPixels = target; // May be null.
        }
    }

}
