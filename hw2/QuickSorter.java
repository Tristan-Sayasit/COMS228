package edu.iastate.cs228.hw2;

/**
 *  
 * @author Tristan Sayasit
 *
 */

/**
 * 
 * This class implements the version of the quicksort algorithm presented in the lecture.   
 *
 */

public class QuickSorter extends AbstractSorter {
	
	// Other private instance variables if you need ... 
		
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *   
	 * @param pts   input array of integers
	 */
	public QuickSorter(Point[] pts) {
		super(pts);
		algorithm = "QuickSort";
	}
		

	/**
	 * Carry out quicksort on the array points[] of the AbstractSorter class.  
	 * 
	 */
	@Override 
	public void sort() {
		int last = points.length;
		quickSortRec(0, last - 1);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last) {
		if (first < last) {
			int pi = partition(first, last);

			quickSortRec(first, pi - 1);
			quickSortRec(pi + 1, last);
		}
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int first, int last) {
		Point pivot = points[last];
		int i = (first - 1);
		for (int j = first; j < last; j++) {
			if (pointComparator.compare(points[j], pivot) <= 0) {
				i++;
				swap(i,j);
			}
		}
		swap(i + 1, last);

		return i + 1;
	}
}
