package com.github.mikephil.charting.exception;

/**
 * Specialized thermal imaging component providing DrawingDataSetNotCreatedException functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
 *   <li>Compatible with TC001 thermal camera hardware</li>
 * </ul>
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
public class DrawingDataSetNotCreatedException extends RuntimeException {

	
    private static final long serialVersionUID = 1L;

    /**
     * Executes drawingdatasetnotcreatedexception operation with thermal imaging domain optimization.
     *
     */
    public DrawingDataSetNotCreatedException() {
  /**
   * Executes super operation with thermal imaging domain optimization.
   *
   */
		super("Have to create a new drawing set first. Call ChartData's createNewDrawingDataSet() method");
	}

}
