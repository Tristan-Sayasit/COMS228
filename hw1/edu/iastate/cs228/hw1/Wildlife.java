package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.util.Scanner; 

/**
 *  
 * @author Tristan  Sayasit
 *
 */

/**
 * 
 * The Wildlife class performs a simulation of a grid plain with
 * squares inhabited by badgers, foxes, rabbits, grass, or none. 
 *
 */
public class Wildlife {
	/**
	 * Update the new plain from the old plain in one cycle. 
	 * @param pOld  old plain
	 * @param pNew  new plain
	 */
	public static void updatePlain(Plain pOld, Plain pNew) {
		for (int i = 0; i < pOld.getWidth(); i++) {
			for (int j = 0; j < pOld.getWidth(); j++) {
				pNew.grid[i][j] = pOld.grid[i][j].next(pNew);
			}
		}
	}
	
	/**
	 * Repeatedly generates plains either randomly or from reading files. 
	 * Over each plain, carries out an input number of cycles of evolution. 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(System.in);
		Plain even = null;
		Plain odd = null;
		int trials = 1;
		int input = 0;
		int width = 0;
		int cycles = 0;
		boolean evenCycle = false;
		System.out.println("Simulation of Wildlife of the Plain\r\n" + "keys: 1 (random plain) 2 (file input) 3 (exit)");
		while (input != 3) {
			System.out.print("Trial " + trials + ": ");
			input = scan.nextInt();

			if (input == 1) {
				System.out.println("Random plain");
				System.out.print("Enter grid width: ");
				width = scan.nextInt();

				while (width < 1) {
					System.out.print("Enter grid width: ");
					width = scan.nextInt();
				}
				odd = new Plain(width);
				even = new Plain(odd.getWidth());
				odd.randomInit();
			}

			if (input == 2) {
				System.out.println("Plain input from a file");
				System.out.print("File name: ");
				String file = scan.next();
				odd = new Plain(file);
				even = new Plain(odd.getWidth());
			}

			if (input == 3) {
				scan.close();
				break;
			}
			System.out.print("Enter number of cycles: ");
			cycles = scan.nextInt();

			while(cycles < 1) {
				System.out.print("Enter number of cycles: ");
				cycles = scan.nextInt();
			}
			System.out.println("Initial plain: \n" + odd);
			for (int i = 0; i < cycles; i++) {

				if (i % 2 == 0) {
					updatePlain(odd, even);
					evenCycle = false;
				}

				if (i % 2 != 0) {
					updatePlain(even, odd);
					evenCycle = true;
				}
			}
			if (evenCycle) {
				System.out.println("Final plain: \n" + odd);
			} else if (!evenCycle) {
				System.out.println("Final plain: \n" + even);
			}
			trials++;
		}
	}
}
