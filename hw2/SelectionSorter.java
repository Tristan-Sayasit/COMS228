package edu.iastate.cs228.hw2;

/**
 *  
 * @author Tristan Sayasit
 *
 */

/**
 * 
 * This class implements selection sort.   
 *
 */

public class SelectionSorter extends AbstractSorter {
	// Other private instance variables if you need ...

	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also
	 * set the instance variables algorithm in the superclass.
	 *
	 * @param pts
	 */
	public SelectionSorter(Point[] pts) {
		super(pts);
		algorithm = "SelectionSort";
	}


	/**
	 * Apply selection sort on the array points[] of the parent class AbstractSorter.
	 *
	 */
	@Override
	public void sort() {
		int n = points.length;

		for (int i = 0; i < n - 1; i++) {
			int min_ele = i;
			for (int j = i + 1; j < n; j++) {
				if (pointComparator.compare(points[j], points[min_ele]) < 0) {
					min_ele = j;
				}

				swap(min_ele, i);
			}
		}
	}
}
