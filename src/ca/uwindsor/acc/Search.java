package ca.uwindsor.acc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import csv.CsvCreator;

import java.util.Map.Entry;

import hashTable.CuckooHashTable;
import hashTable.QuadraticProbingHashTable;
import hashTable.SeparateChainingHashTable;
import hashTable.StringHashFamily;
import searchtrees.AVLTree;
import searchtrees.BinarySearchTree;
import searchtrees.RedBlackBST;
import searchtrees.RedBlackTree;
import searchtrees.SplayTree;

//import org.apache.commons.lang3.RandomStringUtils;

public class Search {

	private static double normalSearchAvgInsert, normalSearchAvgSearch, quadraticProbingAvgInsert,
			quadraticProbingAvgSearch, cuckooHashingAvgInsert, cuckooHashingAvgSearch, separateChainingSearchAvgInsert,
			separateChainingSearchAvgSearch;

	public static void main(String[] args) {
		long n = 1000;

		Hashtable<Double, String> hashTable = new Hashtable<Double, String>();
		double total = 0;

		total = task1(hashTable, n, false);
		System.out.println("\t Task 1 => Avg Insert Each: " + (total / n));

		total = task2(hashTable, n, false);
		System.out.println("\t Task 2 => Avg Search Each: " + (total / n));

		task3();
		task4a();
		task4b();
		task4c();
		task5c();

	}

	private static double task1(Hashtable<Double, String> hashtable, double n, boolean innerTask) {
		if (!innerTask) {
			System.out.println("Task 1:");
			System.out.println("--------");
		}
		long start = System.nanoTime();
		for (double i = 0; i < n; i++) {
			hashtable.put(i, createRandomStrOf10());
		}

		return (System.nanoTime() - start);
	}

	@SuppressWarnings("unlikely-arg-type")
	private static double task2(Hashtable<Double, String> hashTable, double n, boolean innerTask) {
		if (!innerTask) {
			System.out.println("Task 2:");
			System.out.println("--------");
		}
		long totalSearch = 0, start;
		for (int i = 0; i < n; i++) {
			start = System.nanoTime();
			String searchText = createRandomStrOf10();
			start = System.nanoTime();

			if (hashTable.contains(searchText))
				hashTable.remove(searchText);

			totalSearch += System.nanoTime() - start;
		}
		return totalSearch;
	}

	private static void task3() {
		System.out.println("Task 3:");
		System.out.println("--------");

		quadraticProbing();
		cuckooHashing();
		separateChainingSearch();
		//hashTableNormalSearch();// It takes a lot of time so I commented it. Please uncomment it and then run it
								// if you want check the result
	}

	public static void findRandomStringAndRemove(Hashtable<Double, String> hashtable, String searchStr) {
		long totalSearch = 0, totalSearchWithRemove = 0, start;

		for (Entry<Double, String> entry : hashtable.entrySet()) {
			String value = entry.getValue();

			start = System.nanoTime();
			boolean found = value.equals(searchStr);
			long current = System.nanoTime() - start;
			totalSearch += current;
			if (found) {
				start = System.nanoTime();
				hashtable.remove(entry.getKey());
				totalSearchWithRemove += current + (System.nanoTime() - start);
				break;
			}
		}

		System.out.println("Average Time used for Each Search: " + (totalSearch / hashtable.size()));
		System.out.println(
				"Average Time used for Each Search with Remove: " + (totalSearchWithRemove / hashtable.size()));
	}

	private static void hashTableNormalSearch() {
		double totalInsert = 0, totalSearch = 0;
		int counter = 0;
		for (int i = 1; i <= 20; i++) {
			double n = Math.pow(2, i);
			counter += n;
			System.out.println(" ");
			System.out.println("\t i= " + i + ", n= " + n);
			System.out.println("\t-----------------------------------");
			Hashtable<Double, String> hashTable = new Hashtable<Double, String>();
			totalInsert += task1(hashTable, n, true);
			totalSearch += task2(hashTable, n, true);
		}
		normalSearchAvgInsert = totalInsert / counter;
		normalSearchAvgSearch = totalSearch / counter;
		System.out.println("\t Avg Insert (nanoseconds) = " + normalSearchAvgInsert + ", Avg Search (nanoseconds) = "
				+ normalSearchAvgSearch);
	}

	private static void quadraticProbing() {
		long startInsert, totalInsert = 0, startSearch, totalSearch = 0;
		int i, counter = 0;
		double n;
		System.out.println("Quadratic Probing...");
		for (i = 1; i <= 20; i++) {
			n = Math.pow(2, i);
			counter += n;
			var nGeneratedRandomStr = generateRandomStringArrayForSearch(n);

			QuadraticProbingHashTable<String> qph = new QuadraticProbingHashTable<>();
			startInsert = System.nanoTime();
			for (int c = 0; c < nGeneratedRandomStr.size(); c++) {
				qph.insert(nGeneratedRandomStr.get(c).toString() + c);
			}
			totalInsert += System.nanoTime() - startInsert;

			startSearch = System.nanoTime();
			for (int c = 0; c < nGeneratedRandomStr.size(); c++) {
				boolean found = qph.contains(nGeneratedRandomStr.get(c).toString() + c);
				totalSearch += System.nanoTime() - startSearch;
				if (found) {
					qph.remove(nGeneratedRandomStr.get(c).toString() + c);
				}
			}
		}

		quadraticProbingAvgInsert = totalInsert / counter;
		quadraticProbingAvgSearch = totalSearch / counter;
		System.out.println("\t Avg Insert (nanoseconds) = " + quadraticProbingAvgInsert
				+ ", Avg Search (nanoseconds) = " + quadraticProbingAvgSearch);
	}

	private static void cuckooHashing() {
		long startInsert, totalInsert = 0, startSearch, totalSearch = 0;
		int i, counter = 0;
		double n;
		System.out.println("Cuckoo Hashing...");
		for (i = 1; i <= 20; i++) {
			n = Math.pow(2, i);
			counter += n;
			var nGeneratedRandomStr = generateRandomStringArrayForSearch(n);

			CuckooHashTable<String> ckh = new CuckooHashTable<>(new StringHashFamily(3), 2000);
			startInsert = System.nanoTime();
			for (int c = 0; c < nGeneratedRandomStr.size(); c++) {
				ckh.insert(nGeneratedRandomStr.get(c).toString() + c);
			}
			totalInsert += System.nanoTime() - startInsert;

			startSearch = System.nanoTime();
			for (int c = 0; c < nGeneratedRandomStr.size(); c++) {
				boolean found = ckh.contains(nGeneratedRandomStr.get(c).toString() + c);
				totalSearch += System.nanoTime() - startSearch;
				if (found) {
					ckh.remove(nGeneratedRandomStr.get(c).toString() + c);
				}
			}
		}

		cuckooHashingAvgInsert = totalInsert / counter;
		cuckooHashingAvgSearch = totalSearch / counter;
		System.out.println("\t Avg Insert (nanoseconds) = " + cuckooHashingAvgInsert + ", Avg Search (nanoseconds) = "
				+ cuckooHashingAvgSearch);
	}

	private static void separateChainingSearch() {
		long startInsert, totalInsert = 0, startSearch, totalSearch = 0;
		int i, counter = 0;
		double n;
		System.out.println("SeparateChaining Hashing...");
		for (i = 1; i <= 20; i++) {
			n = Math.pow(2, i);
			counter += n;
			var nGeneratedRandomStr = generateRandomStringArrayForSearch(n);

			SeparateChainingHashTable<String> sch = new SeparateChainingHashTable<>();
			startInsert = System.nanoTime();
			for (int c = 0; c < nGeneratedRandomStr.size(); c++) {
				sch.insert(nGeneratedRandomStr.get(c).toString() + c);
			}
			totalInsert += System.nanoTime() - startInsert;

			startSearch = System.nanoTime();
			for (int c = 0; c < nGeneratedRandomStr.size(); c++) {
				boolean found = sch.contains(nGeneratedRandomStr.get(c).toString() + c);
				totalSearch += System.nanoTime() - startSearch;
				if (found) {
					sch.remove(nGeneratedRandomStr.get(c).toString() + c);
				}
			}
		}

		separateChainingSearchAvgInsert = totalInsert / counter;
		separateChainingSearchAvgSearch = totalSearch / counter;
		System.out.println("\t Avg Insert (nanoseconds) = " + separateChainingSearchAvgInsert
				+ ", Avg Search (nanoseconds) = " + separateChainingSearchAvgSearch);
	}

	public static void task4a() {
		int n = 100000;

		CsvCreator csv = new CsvCreator(null, "BinarySearchTree", "AVLTree", "RedBlackTree", "SplayTree");

		long avgBST, avgAVLT, avgRBT, avgST;

		// 4.a Insertion----------------
		System.out.println("4.a Insertion----------------");
		System.out.println("(1 to " + n + ") - Started BinarySearchTree ...");
		avgBST = calculateBinarySearchTree(n);
		System.out.println("");
		System.out.println("\tAvg Insert (nanoseconds): " + avgBST);

		System.out.println("(1 to " + n + ") - Started AVLTree ...");
		avgAVLT = calculateAVLTree(n);
		System.out.println("");
		System.out.println("\tAvg Insert (nanoseconds): " + avgAVLT);

		System.out.println("(1 to " + n + ") - Started RedBlackTree ...");
		avgRBT = calculateRedBlackTree(n);
		System.out.println("");
		System.out.println("\tAvg Insert (nanoseconds): " + avgRBT);

		System.out.println("(1 to " + n + ") - Started SplayTree ...");
		avgST = calculateSplayTree(n);
		System.out.println("");
		System.out.println("\tAvg Insert (nanoseconds): " + avgST);

		csv.newRow(avgBST, avgAVLT, avgRBT, avgST);

		csv.close();

	}

	private static long calculateAVLTree(long n) {
		AVLTree<Integer> avt = new AVLTree<>();
		long start = System.nanoTime();
		for (int i = 1; i <= n; i++)
			avt.insert(i);
		return (System.nanoTime() - start) / n;
	}

	private static long calculateBinarySearchTree(long n) {
		BinarySearchTree<Integer> bst = new BinarySearchTree<>();
		long start = System.nanoTime();
		for (int i = 1; i <= n; i++)
			bst.insert(i);

		return (System.nanoTime() - start) / n;
	}

	private static long calculateRedBlackTree(long n) {
		RedBlackTree<Integer> rbt = new RedBlackTree<>();
		long start = System.nanoTime();
		for (int i = 1; i <= n; i++)
			rbt.insert(i);

		return (System.nanoTime() - start) / n;
	}

	private static long calculateSplayTree(long n) {
		SplayTree<Integer> stt = new SplayTree<Integer>();
		long start = System.nanoTime();
		for (int i = 1; i <= n; i++)
			stt.insert(i);

		return (System.nanoTime() - start) / n;
	}

	public static void task4b() {

		long start;
		int i, n = 100000;
		long binarySearchTotal = 0, avlTreeTotal = 0, redBlackTreeTotal = 0, splayTreeTotal = 0;

		BinarySearchTree<Integer> bst = new BinarySearchTree<>();
		AVLTree<Integer> avt = new AVLTree<>();
		RedBlackTree<Integer> rbt = new RedBlackTree<>();
		SplayTree<Integer> stt = new SplayTree<Integer>();

		// 4.b ----------------
		System.out.println("");
		System.out.println("");
		System.out.println("4.b search ----------------");
		System.out.println("");
		int a[] = (new Random()).ints(n, 1, n).toArray();
		for (i = 0; i < n; i++) {
			// BinarySearchTree -----------------
			start = System.nanoTime();
			bst.contains(a[i]);
			binarySearchTotal += System.nanoTime() - start;
		}
		for (i = 0; i < n; i++) {
			// AVLTree-------------------
			start = System.nanoTime();
			avt.contains(a[i]);
			avlTreeTotal += System.nanoTime() - start;
		}
		for (i = 0; i < n; i++) {
			// RedBlackTree-------------------
			start = System.nanoTime();
			rbt.contains(a[i]);
			redBlackTreeTotal += System.nanoTime() - start;
		}
		for (i = 0; i < n; i++) {
			// SplayTree-------------------
			start = System.nanoTime();
			stt.contains(a[i]);
			splayTreeTotal += System.nanoTime() - start;
		}

		System.out.println("All in nanoseconds => ");
		System.out.println("\tBinarySearchTree Avg Time:\t" + (binarySearchTotal / n));
		System.out.println("\tAVLTree Avg Time:\t\t" + (avlTreeTotal / n));
		System.out.println("\tRedBlackTree Avg Time:\t\t" + (redBlackTreeTotal / n));
		System.out.println("\tSplayTree Avg Time:\t\t" + (splayTreeTotal / n));

	}

	public static void task4c() {
		// 4.c ----------------
		System.out.println("");
		System.out.println("");
		System.out.println("4.c Delete ----------------");
		System.out.println("");
		int n = 100000;
		System.out.println("Showcase BinarySearchTree:");
		long binarySearchTreeDelAvg = deleteBinarySearchTree(n);
		System.out.println("Showcase AVLTree:");
		long avlTreeDelAvg = deleteAVLTree(n);
		System.out.println("Showcase BinarySearchTree:");
		long splayTreeAvg = deleteSplayTree(n);
		System.out.println("Showcase RedBlackBST:");
		long redBlackTreeDelAvg = deleteRedBlackBST(n);

		System.out.println("All in nanoseconds => ");
		System.out.println("\tBinarySearchTree Avg Time:\t" + binarySearchTreeDelAvg);
		System.out.println("\tAVLTree Avg Time:\t\t" + avlTreeDelAvg);
		System.out.println("\tRedBlackBST Avg Time:\t\t" + splayTreeAvg);
		System.out.println("\tSplayTree Avg Time:\t\t" + redBlackTreeDelAvg);
	}

	public static void task5c() {
		// 4.c ----------------
		System.out.println("");
		System.out.println("");
		System.out.println("5.c Delete ----------------");
		System.out.println("");
		int n = 100000;
		System.out.println("Showcase BinarySearchTree:");
		long binarySearchTreeDelAvg = deleteBinarySearchTreeRandom(n);
		System.out.println("Showcase AVLTree:");
		long avlTreeDelAvg = deleteAVLTreeRandom(n);
		System.out.println("Showcase BinarySearchTree:");
		long splayTreeAvg = deleteSplayTreeRandom(n);
		System.out.println("Showcase RedBlackBST:");
		long redBlackTreeDelAvg = deleteRedBlackBSTRandom(n);

		System.out.println("All in nanoseconds => ");
		System.out.println("\tBinarySearchTree Avg Time:\t" + binarySearchTreeDelAvg);
		System.out.println("\tAVLTree Avg Time:\t\t" + avlTreeDelAvg);
		System.out.println("\tRedBlackBST Avg Time:\t\t" + splayTreeAvg);
		System.out.println("\tSplayTree Avg Time:\t\t" + redBlackTreeDelAvg);
	}

	private static long deleteBinarySearchTree(int n) {
		long start;
		int i;
		BinarySearchTree<Integer> bst = new BinarySearchTree<>();
		System.out.println("Inserting " + n + " items...");
		for (i = 1; i <= n; i++)
			bst.insert(i);

		System.out.println("Deleting " + n + " items...");
		start = System.nanoTime();
		for (i = n; i > 0; i--)
			bst.remove(i);

		return (System.nanoTime() - start) / n;
	}

	private static long deleteAVLTree(int n) {
		long start;
		int i;
		AVLTree<Integer> avt = new AVLTree<>();
		System.out.println("Inserting " + n + " items...");

		for (i = 1; i <= n; i++)
			avt.insert(i);

		System.out.println("Deleting " + n + " items...");
		start = System.nanoTime();
		for (i = n; i > 0; i--)
			avt.remove(i);

		return (System.nanoTime() - start) / n;
	}

	private static long deleteSplayTree(int n) {
		long start;
		int i;
		SplayTree<Integer> st = new SplayTree<>();
		System.out.println("Inserting " + n + " items...");

		for (i = 1; i <= n; i++)
			st.insert(i);

		System.out.println("Deleting " + n + " items...");
		start = System.nanoTime();
		for (i = n; i > 0; i--)
			st.remove(i);

		return (System.nanoTime() - start) / n;
	}

	private static long deleteRedBlackBST(int n) {
		long start;
		int i;
		RedBlackBST<Integer, Integer> bst = new RedBlackBST<Integer, Integer>();
		System.out.println("Inserting " + n + " items...");

		for (i = 1; i <= n; i++)
			bst.put(i, i);

		System.out.println("Deleting " + n + " items...");
		start = System.nanoTime();
		for (i = n; i > 0; i--)
			bst.delete(i);

		return (System.nanoTime() - start) / n;
	}

	private static long deleteBinarySearchTreeRandom(int n) {
		long start;
		int i;
		BinarySearchTree<Integer> bst = new BinarySearchTree<>();
		int a[] = (new Random()).ints(n, 1, n).toArray();

		System.out.println("Inserting " + n + " items...");
		for (i = 0; i < n; i++)
			bst.insert(a[i]);

		System.out.println("Deleting " + n + " items...");
		start = System.nanoTime();
		for (i = n; i > 0; i--) {
			if (bst.contains(n)) {
				bst.remove(i);
			}
		}

		return (System.nanoTime() - start) / n;
	}

	private static long deleteAVLTreeRandom(int n) {
		long start;
		int i;
		AVLTree<Integer> avt = new AVLTree<>();
		int a[] = (new Random()).ints(n, 1, n).toArray();

		System.out.println("Inserting " + n + " items...");
		for (i = 0; i < n; i++)
			avt.insert(a[i]);

		System.out.println("Deleting " + n + " items...");
		start = System.nanoTime();
		for (i = n; i > 0; i--) {
			if (avt.contains(n)) {
				avt.remove(i);
			}
		}

		return (System.nanoTime() - start) / n;
	}

	private static long deleteSplayTreeRandom(int n) {
		long start;
		int i;
		SplayTree<Integer> st = new SplayTree<>();
		int a[] = (new Random()).ints(n, 1, n).toArray();

		System.out.println("Inserting " + n + " items...");
		for (i = 0; i < n; i++)
			st.insert(a[i]);

		System.out.println("Deleting " + n + " items...");
		start = System.nanoTime();
		for (i = n; i > 0; i--) {
			if (st.contains(n)) {
				st.remove(i);
			}
		}

		return (System.nanoTime() - start) / n;
	}

	private static long deleteRedBlackBSTRandom(int n) {
		long start;
		int i;
		RedBlackBST<Integer, Integer> bst = new RedBlackBST<Integer, Integer>();
		int a[] = (new Random()).ints(n, 1, n).toArray();

		System.out.println("Inserting " + n + " items...");
		for (i = 0; i < n; i++)
			bst.put(a[i], a[i]);

		System.out.println("Deleting " + n + " items...");
		start = System.nanoTime();
		for (i = n; i > 0; i--) {
			if (bst.contains(n)) {
				bst.delete(i);
			}
		}

		return (System.nanoTime() - start) / n;
	}

	public static String createRandomStrOf10() {

		String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(10);
		for (int i = 0; i < 10; i++) {

			int index = (int) (alphaNumericString.length() * Math.random());

			sb.append(alphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	public static List generateRandomStringArrayForSearch(double countOfGereration) {
		List searchArray = new ArrayList();
		for (int i = 0; i < countOfGereration; i++) {
			searchArray.add(createRandomStrOf10());
		}
		return searchArray;
	}
}
