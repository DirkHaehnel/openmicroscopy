/*
 * org.openmicroscopy.shoola.util.ui.slider.OneKnobSliderUI
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
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.util.ui.slider;

//Java imports
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.util.ui.IconManager;
import org.openmicroscopy.shoola.util.ui.TipDialog;

/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 	<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author	Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * 	<a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME2.2
 */
public class OneKnobSliderUI
	extends BasicSliderUI
{
	
	/** Width of the slider's thumb. */
	private static final int 	THUMB_WIDTH = 16;
	
	/** Height of the slider's thumb. */
	private static final int 	THUMB_HEIGHT = 16;
	
	/** Height of the arrow. */
	private static final int	ARROW_HEIGHT = 12;
	
	/** Width of the arrow. */
	private static final int 	ARROW_WIDTH = 12;
	
	/** Spacing between the arrow and the end of the slider track. */
	private static final int 	ARROW_SPACE = 1;
	
	/** The starting color of the gradient used in the track. */
    private static final Color 	TRACK_GRADIENT_START = 
    									new Color(76, 76, 76, 255);
    
    /** The final color of the gradient used in the track. */
    private static final Color 	TRACK_GRADIENT_END = 
    									new Color(176, 176, 176, 255);
    
    /** Offset to the left of the mouse used for placing tooltip. */
    private static final int 	TOOLTIP_OFFSET = 25;
    
    
    /** Image used for the thumb. */
    private  Image 				thumbImage;
        

    /** Image used for the thumb when the component is disabled. */
    private  Image 				disabledThumbImage;
    
    /** Image used for the up arrow. */
    private  Image 				upArrowImage;
    
    /** Image used for the up arrow when the component is disabled. */
    private  Image 				upArrowDisabledImage;
    
    /** Image used for the down arrow. */
    private  Image 				downArrowImage;
    
    /** Image used for the down arrow when the component is disabled. */
    private  Image 				downArrowDisabledImage;
    
    /** Image used for the left arrow. */
    private  Image 				leftArrowImage;
    
    /** Image used for the left arrow when the component is disabled. */
    private  Image 				leftArrowDisabledImage;
        
    /** Image used for the right arrow. */
    private  Image 				rightArrowImage;
    
    /** Image used for the right arrow when the component is disabled. */
    private  Image 				rightArrowDisabledImage;
    
    /** Show arrow at the side of the slider track. */
    private  boolean			showArrows;
    
    /** Area in which the min arrow will reside. */
    private  Rectangle			minArrowRect;
    
    /** Area in which the max arrow will reside. */
    private  Rectangle			maxArrowRect;
    
    /** Dialog used to display tooltip containing the position of the slider. */
    private  TipDialog			tipDialog;
    
    /** The end label displayed at top, or left of slider. */
    private  String				endLabel;
    
    /** Show the end label if set. */
    private  boolean			showEndLabel;
    
    /** Show the tip label ovet the thumb when slider moved. */
    private boolean 			showTipLabel;
    
    /** The rect holding the location of the end label. */
    private Rectangle			endLabelRect;
    
    /** The height of the end label. */
    private int					labelHeight;
    
    /** The width of the end label. */
    private	int 				labelWidth;
    
	/** Load the thumb and arrow images. */
	private void loadThumbArrowImage()
	{
		IconManager icons = IconManager.getInstance();
    	
		ImageIcon img = icons.getImageIcon(IconManager.THUMB);
		thumbImage = img.getImage();
		img = icons.getImageIcon(IconManager.THUMB_DISABLED);
		disabledThumbImage = img.getImage();
		img = icons.getImageIcon(IconManager.UP_ARROW_DISABLED);
		upArrowDisabledImage = img.getImage();
		img = icons.getImageIcon(IconManager.DOWN_ARROW_DISABLED);
		downArrowDisabledImage = img.getImage();
		img = icons.getImageIcon(IconManager.LEFT_ARROW_DISABLED);
		leftArrowDisabledImage = img.getImage();
		img = icons.getImageIcon(IconManager.RIGHT_ARROW_DISABLED);
		rightArrowDisabledImage = img.getImage();
		img = icons.getImageIcon(IconManager.UP_ARROW);
		upArrowImage = img.getImage();
		img = icons.getImageIcon(IconManager.DOWN_ARROW);
		downArrowImage = img.getImage();
		img = icons.getImageIcon(IconManager.LEFT_ARROW);
		leftArrowImage = img.getImage();
		img = icons.getImageIcon(IconManager.RIGHT_ARROW);
		rightArrowImage = img.getImage();
	}	


	/**
	 * This method calculates the size and positon of the arrows used displayed
	 * in the trackRect.  
	 */
	private void calculateArrowRect()
	{
		if (slider.getOrientation() == JSlider.HORIZONTAL)
		{
			int offsetY = trackRect.height/2-ARROW_HEIGHT/2-1;
			minArrowRect = new Rectangle(trackRect.x-(ARROW_WIDTH+THUMB_WIDTH/2+
					ARROW_SPACE),trackRect.y+offsetY, ARROW_WIDTH, 
					ARROW_HEIGHT);
			maxArrowRect = new Rectangle(trackRect.x+trackRect.width+
					ARROW_SPACE+THUMB_WIDTH/2, trackRect.y+offsetY, ARROW_WIDTH, 
					ARROW_HEIGHT);
		}
		else
		{
			int offsetX = trackRect.width/2-ARROW_WIDTH/2;
			maxArrowRect = new Rectangle(trackRect.x+offsetX, trackRect.y-
					(ARROW_HEIGHT+THUMB_HEIGHT/2+ARROW_SPACE), ARROW_WIDTH, 
					ARROW_HEIGHT);
			minArrowRect = new Rectangle(trackRect.x+offsetX, trackRect.y+
					trackRect.height+ARROW_SPACE+THUMB_HEIGHT/2, ARROW_WIDTH, 
					ARROW_HEIGHT);
		}
	}

	/**
	 * This method calculates the size and positon of the endlabel displayed
	 * in the trackRect.  
	 */
	private void calculateEndLabelRect()
	{
		if (slider.getOrientation() == JSlider.HORIZONTAL)
		{
			int offsetY = trackRect.height/2-labelHeight/2-1;
			endLabelRect = new Rectangle(trackRect.x-(minArrowRect.width+
					labelWidth), offsetY, labelWidth, labelHeight);
		}
		else
		{
			int offsetX = trackRect.width/2-labelWidth/2+1;
			endLabelRect = new Rectangle(offsetX, trackRect.y-
					(minArrowRect.height+labelHeight),labelWidth, labelHeight);
		}
	}
	
    /**
     * Paints the vertical track, and arrows if selected, this method is called
     * from the {@link #paintTrack(Graphics)} method. 
     * 
     * @param g Graphics context.
     */
    private void paintVerticalTrack(Graphics2D g)
    {
        Color gradientStart = TRACK_GRADIENT_START;
        Color gradientEnd = TRACK_GRADIENT_END;
        
        Paint paint = new GradientPaint(trackRect.x+trackRect.width/2-2, 
                trackRect.y, gradientStart, trackRect.x+trackRect.width/2+2,
                trackRect.y, gradientEnd, false);
        g.setPaint(paint);
        g.fillRoundRect(trackRect.x+trackRect.width/2-2, trackRect.y, 4, 
                trackRect.height, 4, 4);
        g.setPaint(Color.black);
        if (showArrows)
        {
            if (slider.isEnabled())
            {
                g.drawImage(downArrowImage, minArrowRect.x, minArrowRect.y, 
                    minArrowRect.width, minArrowRect.height, null);
                g.drawImage(upArrowImage, maxArrowRect.x, maxArrowRect.y, 
                    maxArrowRect.width, maxArrowRect.height, null);
            }
            else
            {
                g.drawImage(downArrowDisabledImage, minArrowRect.x, 
                        minArrowRect.y, minArrowRect.width, minArrowRect.height,
                        null);
                g.drawImage(upArrowDisabledImage, maxArrowRect.x,
                        maxArrowRect.y, maxArrowRect.width, maxArrowRect.height, 
                        null);
            }           
        }
        if(showEndLabel && endLabel != null)
        {
        	g.drawString(endLabel, endLabelRect.x, endLabelRect.y);
        }
    }

    /**
     * Paints the Horizontal track, and arrows if selected, this method is 
     * called from the {@link #paintTrack(Graphics)} method. 
     * 
     * @param g Graphics context.
     */
    private void paintHorizontalTrack(Graphics2D g)
    {
        Color gradientStart = TRACK_GRADIENT_START;
        Color gradientEnd = TRACK_GRADIENT_END;
        
        Paint paint = new GradientPaint(0, trackRect.y+thumbRect.height/2-3, 
              gradientStart, 0, trackRect.y+thumbRect.height/2+2, gradientEnd, 
              false);
        g.setPaint(paint);
        g.fillRoundRect(trackRect.x, trackRect.y+thumbRect.height/2-3, 
                trackRect.width, 4, 4, 4);
        g.setPaint(Color.black);
        if (showArrows)
        {
            if (slider.isEnabled())
            {
                g.drawImage(leftArrowImage, minArrowRect.x, minArrowRect.y, 
                    minArrowRect.width, minArrowRect.height, null);
                g.drawImage(rightArrowImage, maxArrowRect.x, maxArrowRect.y, 
                    maxArrowRect.width, maxArrowRect.height, null);
            }
            else
            {
                g.drawImage(leftArrowDisabledImage, minArrowRect.x, 
                        minArrowRect.y, minArrowRect.width, minArrowRect.height,
                        null);
                g.drawImage(rightArrowDisabledImage, maxArrowRect.x,
                        maxArrowRect.y, maxArrowRect.width, maxArrowRect.height, 
                        null);
            }
        }
        if(showEndLabel && endLabel != null)
        {
        	g.drawString(endLabel, endLabelRect.x, endLabelRect.y);
        }
    }

    /**
     * Creates a new instance.
     * 
     * @param slider parent slider component.
     */
    OneKnobSliderUI(OneKnobSlider slider) 
    {
        super(slider);
        showArrows = true;
        loadThumbArrowImage();    
        showTipLabel = false;
        showEndLabel = false;
    }
    
    /** 
     * Sets the end label. 
     * 
     * @param endLabel
     */
    void setEndLabel(String endLabel)
    {
      	this.endLabel = endLabel;
      	labelWidth = 6;
      	labelHeight = 12;
    }
    
    /**
     * Will show the end tip label which hovers over the thumb if set.
     *  
     * @param show see above.
     */
    void setShowTipLabel(boolean show)
    {
    	showTipLabel = show;
    	if(showTipLabel)
    		tipDialog = new TipDialog(endLabel);
    }
    
    /**
     * Show the end label if set.
     * 
     * @param show see above.
     */
    void setShowEndLabel(boolean show)
    {
    	showEndLabel = show;
    }
    
    /**
     * Show or hide the arrows on the track.
     * 
     * @param isShow See above.
     */
    void setShowArrows(boolean isShow)
    {
        showArrows = isShow;
        this.calculateGeometry();
    }
    
	/**
	 * Extends the {@link #calculateTrackBuffer()} to allow the extra space 
	 * required to display the arrows on the track. 
     * @see BasicSliderUI#calculateTrackBuffer()
	 */
	protected void calculateTrackBuffer()
	{
		super.calculateTrackBuffer();
		if (showArrows)
			if (slider.getOrientation() == JSlider.HORIZONTAL)
				trackBuffer += ARROW_WIDTH+ARROW_SPACE;
			else
				trackBuffer += ARROW_HEIGHT+ARROW_SPACE; 
		if	(showEndLabel)
			if (slider.getOrientation() == JSlider.HORIZONTAL)
				trackBuffer += labelWidth;
			else
				trackBuffer += labelHeight;
	}
	
    /**
     * Overriddent to get the size of the thumb.
     * @see BasicSliderUI#getThumbSize()
     */
    protected Dimension getThumbSize() 
    {
        return new Dimension(THUMB_WIDTH, THUMB_HEIGHT);
    }
    
	/**
	 * Overridden to calculate the size of the thumb rect. 
     * @see BasicSliderUI#calculateThumbSize()
	 */
	public void calculateThumbSize()
	{
		this.thumbRect = new Rectangle(0, 0, THUMB_WIDTH, THUMB_HEIGHT);
	}

	/**
	 * Overridden to calculate the size of the thumb rect. 
     * @see BasicSliderUI#paintFocus(Graphics g)
	 */
	public void paintFocus(Graphics g) {}

	/**
	 * Overridden this method will paint the gradient on the slider track
	 * @see BasicSliderUI#paintTrack(Graphics)
	 */
	public void paintTrack(Graphics og)
	{
		if (slider.getOrientation() == JSlider.HORIZONTAL)
			paintHorizontalTrack((Graphics2D) og);
		else
			paintVerticalTrack((Graphics2D) og);
	}

	/**
	 * Overridden to paint thumb on slider. 
	 * @see BasicSliderUI#paintThumb(java.awt.Graphics)
	 */
	public void paintThumb(Graphics og)
	{
		Graphics2D g = (Graphics2D) og;
		if (slider.isEnabled())
			g.drawImage(thumbImage, thumbRect.x, thumbRect.y, thumbRect.width, 
					thumbRect.height, null);
		else
			g.drawImage(disabledThumbImage, thumbRect.x, thumbRect.y, 
					thumbRect.width, thumbRect.height, null);
	}

    /**
     * Overridden to calculate the geometry of the slider, this calls the 
     * {@link BasicSliderUI#calculateGeometry} and to add extra calculations to 
     * calculate the <code>ArrowRect</code> if showArrows is <code>true</code>. 
     * @see BasicSliderUI#calculateGeometry()
     */
    public void calculateGeometry()
    {
        super.calculateGeometry();
        if(showArrows) calculateArrowRect();
        if(showEndLabel) calculateEndLabelRect();
    }
    
    /**
     * Assign the new overloaded trackListener to the slider. 
     * 
     * @param slider Parent slider.
     * @return TrackListner New listener.
     * @see BasicSliderUI#createTrackListener(JSlider)
     */
    protected TrackListener createTrackListener(JSlider slider) 
    {
        return new TrackListener2();
    }
    
	/** 
	 * Overridden TrackListener class, as we wish to extend the functionality
	 * of the on track click events.  
	 */
	public class TrackListener2 
        extends TrackListener 
	{

		/**
		 * This variable is set to <code>true</code> if user dragging thumb.
		 */
		protected boolean isDragging;
		
		/**
		 * Overridden to determine when a drag event ends. 
         * This method will also determine when the tooltip Dialog should 
         * stop showing. 
		 * @see TrackListener#mouseReleased(MouseEvent)
		 */
		public void mouseReleased(MouseEvent event)
		{
			super.mouseReleased(event);
			if (showTipLabel && tipDialog != null)
			{
				if (tipDialog.isVisible()) tipDialog.setVisible(false);
			}	
		}
		
        /**
         * This method will detect a click on the track or min/max arrows and 
         * behave accordingly.
         * If the user clicks in the track then the thumb is moved to that 
         * position and the value updated. If the user clicks the arrows then
         * the value is incremented or decremented by one depending on which 
         * slider was clicked. 
         * 
         * @param event mouseEvent.
         */
        public void mouseClicked(MouseEvent event)
        {   
            // Check to see that the slider is enabled before proceeeding.
            if (!slider.isEnabled())
                return;

            // Get mouse x, y positions.
            currentMouseX = event.getX();
            currentMouseY = event.getY();
    
            int value; 
            scrollTimer.stop();
                
            if (showArrows)
	        {
				if (minArrowRect.contains(currentMouseX, currentMouseY))
	            {
					value = slider.getValue();
	                if (value > slider.getMinimum())
	                {
	                	slider.setValue(value-1);
	                    slider.repaint();
	                }
	                isDragging = false;
	                return;     
	            }
	            if (maxArrowRect.contains(currentMouseX, currentMouseY))
	            {
	            	value = slider.getValue();
	                if (value < slider.getMaximum())
	                {
	                	slider.setValue(value+1);
	                    slider.repaint();
	                }
	                isDragging = false;
	                return;
	            }
	        }
            
            if (trackRect.contains(currentMouseX, currentMouseY))
            {
            // Depending on the slider orientation lets move the thumb to the 
            // position clicked by the user. 
            	switch (slider.getOrientation()) {
                	case JSlider.HORIZONTAL:
                    	value = valueForXPosition(currentMouseX);
                    	slider.setValue(value);
                    break;
                	case JSlider.VERTICAL:
                    	value = valueForYPosition(currentMouseY);
                    	slider.setValue(value);
                    break;
            	}
            }
        }
        
		/**
		 * Overloaded the <code>mousePressed</code> event in the TrackListener.
         * @see TrackListener#mousePressed(MouseEvent)
		 */
		public void mousePressed(MouseEvent event) 
		{
			// Check to see that the slider is enabled before proceeeding. 
			if (!slider.isEnabled())
				return;

			// Get mouse x, y positions.
			currentMouseX = event.getX();
			currentMouseY = event.getY();

			// If the slider has {@link #setFocusEnabled} true then 
			// request focus.
			if (slider.isRequestFocusEnabled())
				slider.requestFocus();

			// Check to see if the thumb was clicked. 
			if (thumbRect.contains(currentMouseX, currentMouseY)) 
			{
				// Depending on orientation of the mouse. 
				switch (slider.getOrientation()) {
    				case JSlider.HORIZONTAL:
    					offset = currentMouseX-thumbRect.x;
    					break;
    				case JSlider.VERTICAL:
    					offset = currentMouseY-thumbRect.y;
				}
				isDragging = true;
				return;
			}
			
			if (showArrows)
	        {
				if (minArrowRect.contains(currentMouseX, currentMouseY))
	            {
					int value = slider.getValue();
	                if (value > slider.getMinimum())
	                {
	                	scrollTimer.stop();
	                	scrollListener.setScrollByBlock(false);
	                    scrollListener.setDirection(OneKnobSliderUI.NEGATIVE_SCROLL);
	                    scrollTimer.start();
	                    slider.repaint();
	                }
	                isDragging = false;
	                return;     
	            }
	            if (maxArrowRect.contains(currentMouseX, currentMouseY))
	            {
	            	int value = slider.getValue();
	                if (value < slider.getMaximum())
	                {
	                	scrollTimer.stop();
	                	scrollListener.setScrollByBlock(false);
	                    scrollListener.setDirection(
                                OneKnobSliderUI.POSITIVE_SCROLL);
	                    scrollTimer.start();
	                    slider.repaint();
	                }
	                isDragging = false;
	                return;
	            }
	        }
			
			// We have clicked outside of the thumb and onto the track. 
			isDragging = false;
			slider.repaint();
		}

		/**
		 * Overridden function of the slider track listener method mouseDragged
		 * as this method relied on the private member of isDragged in sliderUI.
		 * Has to override this as we could not set the isDragging variable in
		 * the basicSliderUI. :-( *Why private??!*
		 * @see TrackListener#mouseDragged(MouseEvent) 
		 */
		public void mouseDragged(MouseEvent event) 
		{
			// Return if slider disabled or we're not dragging slider.
			if (!slider.isEnabled() || !isDragging)
				return;

			currentMouseX = event.getX();
			currentMouseY = event.getY();

			slider.setValueIsAdjusting(true);

			int thumbMiddle;
			int thumbPosition;
			switch (slider.getOrientation()) {
				case JSlider.VERTICAL:
					thumbMiddle = thumbRect.height/2;
					thumbPosition = event.getY()-offset;
					
					int trackTop = trackRect.y;
					int trackBottom = trackRect.y+(trackRect.height-1);
					int MaxYValue = yPositionForValue(slider.getMaximum()
							-slider.getExtent());

					if (drawInverted()) trackBottom = MaxYValue;
					else trackTop = MaxYValue;
	
					thumbPosition = Math.max(thumbPosition, 
							trackTop-thumbMiddle);
					thumbPosition = Math.min(thumbPosition, 
							trackBottom-thumbMiddle);

					setThumbLocation(thumbRect.x, thumbPosition);

					thumbMiddle = thumbPosition+thumbMiddle;
					slider.setValue(valueForYPosition(thumbMiddle));
					break;
				case JSlider.HORIZONTAL:
					thumbMiddle = thumbRect.width/2;
					thumbPosition = event.getX()-offset;
					
					int trackLeft = trackRect.x;
					int trackRight = trackRect.x+(trackRect.width-1);
					int MaxXValue = xPositionForValue(slider.getMaximum()
							- slider.getExtent());

					if (drawInverted()) trackLeft = MaxXValue;
					else trackRight = MaxXValue;
					
					thumbPosition = Math.max(thumbPosition, 
							trackLeft-thumbMiddle);
					thumbPosition = Math.min(thumbPosition, 
							trackRight-thumbMiddle);

					setThumbLocation(thumbPosition, thumbRect.y);

					thumbMiddle = thumbPosition+thumbMiddle;
					slider.setValue(valueForXPosition(thumbMiddle));
					break;
			}

			if (showTipLabel && tipDialog != null && endLabel != null)
			{
			
				Point location = slider.getLocationOnScreen();
				location.x += thumbRect.x+TOOLTIP_OFFSET;
				location.y += thumbRect.y;
					
				tipDialog.setTipString(
						endLabel+" : " + slider.getValue());
				tipDialog.setLocation(location);
				tipDialog.setVisible(true);
			}
			
		}
	}
    
}
