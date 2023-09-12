package edu.iastate.cs228.hw2;

/**
 *  
 * @author Tristan Sayasit
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter {
	// Other private instance variables if needed
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) {
		super(pts);
		algorithm = "MergeSort";
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort() {
		mergeSortRec(points);
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts) {
		int n = pts.length;
		if (n < 2) {
			return;
		}
		int mid = n / 2;
		Point[] l = new Point[mid];
		Point[] r = new Point[n - mid];

		for (int i = 0; i < mid; i++) {
			l[i] = pts[i];
		}
		for (int i = mid; i < n; i++) {
			r[i - mid] = pts[i];
		}
		mergeSortRec(l);
		mergeSortRec(r);

		merge(l, r, pts);
	}

	public void merge(Point B[], Point C[], Point D[]) {
		int p = B.length;
		int q = C.length;
		int i = 0;
		int j = 0;
		int k = 0;

		while (i < p && j < q) {
			if (pointComparator.compare(B[i], C[j]) <= 0) {
				D[k] = B[i];
				i++;
				k++;
			} else {
				D[k] = C[j];
				j++;
				k++;
			}
		}
		while (i < p) {
			D[k] = B[i];
			i++;
			k++;
		}
		while (j < q) {
			D[k] = C[j];
			j++;
			k++;
		}
	}
}
