package edu.iastate.cs228.hw2;

/**
 *  
 * @author Tristan Sayasit
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;


public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException {
		//
		// 
		// Conducts multiple rounds of comparison of four sorting algorithms.  Within each round, 
		// set up scanning as follows: 
		// 
		//    a) If asked to scan random points, calls generateRandomPoints() to initialize an array 
		//       of random points. 
		// 
		//    b) Reassigns to the array scanners[] (declared below) the references to four new 
		//       PointScanner objects, which are created using four different values  
		//       of the Algorithm type:  SelectionSort, InsertionSort, MergeSort and QuickSort. 
		// 
		// 	
		PointScanner[] scanners = new PointScanner[4]; 
		
		// For each input of points, do the following. 
		// 
		//     a) Initialize the array scanners[].  
		//
		//     b) Iterate through the array scanners[], and have every scanner call the scan() 
		//        method in the PointScanner class.  
		//
		//     c) After all four scans are done for the input, print out the statistics table from
		//		  section 2.
		//
		// A sample scenario is given in Section 2 of the project description. 

		Random rand = new Random();
		int trialCount = 1;
		int userNum = 0;
		System.out.println("Performances of Four Sorting Algorithms in Point Scanning");
		System.out.println("keys: 1 (random integers) 2 (file input) 3 (exit)");
		Scanner scnr = new Scanner(System.in);
		while (userNum != 3) {
			System.out.print("Trial " + trialCount + ": ");
			userNum = scnr.nextInt();
			if (userNum == 1) {
				System.out.print("Enter number of random points: ");
				Point[] pts = generateRandomPoints(scnr.nextInt(), rand);
				scanners[0] = new PointScanner(pts, Algorithm.SelectionSort);
				scanners[1] = new PointScanner(pts, Algorithm.InsertionSort);
				scanners[2] = new PointScanner(pts, Algorithm.MergeSort);
				scanners[3] = new PointScanner(pts, Algorithm.QuickSort);
				System.out.println("algorithm size  time (ns) ");
				System.out.println("----------------------------------");
				for (int i = 0; i < 4; i++) {
					scanners[i].scan();
					System.out.println(scanners[i].stats());
				}
				System.out.println("----------------------------------");
			} else if (userNum == 2) {
				System.out.print ("Points from a file \n" + "File name: ");
				scnr.nextLine();
				String fileName = scnr.nextLine();
				scanners[0] = new PointScanner(fileName, Algorithm.SelectionSort);
				scanners[1] = new PointScanner(fileName, Algorithm.InsertionSort);
				scanners[2] = new PointScanner(fileName, Algorithm.MergeSort);
				scanners[3] = new PointScanner(fileName, Algorithm.QuickSort);
				System.out.println("algorithm size  time (ns) ");
				System.out.println("----------------------------------");
				for (int i = 0; i < 4; i++) {
					scanners[i].scan();
					System.out.println(scanners[i].stats());
				}
				System.out.println("----------------------------------");
			}
			trialCount++;
		}
	}
	
	
	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] × [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException {
		if (numPts < 1) {
			throw new IllegalArgumentException();
		}

		Point[] randArr = new Point[numPts];
		int randNumX, randNumY;
		int counter = 0;

		while(numPts >= 1) {
			randNumX = rand.nextInt(101) - 50;
			randNumY = rand.nextInt(101) - 50;
			randArr[counter] = new Point(randNumX, randNumY);
			numPts--;
			counter++;
		}

		return randArr;
	}
	
}
