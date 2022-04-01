# Hash-Table-Practice
Calculate time complexity by using Hash Table

Tasks: 
1.	Within a Java class, write a method that creates n random strings of length 10 and inserts them in a hash table. The method should compute the average time for each insertion. 
2.	Write another method that finds n random strings in the hash table. The method should delete the string if found. It should also compute the average time of each search. 
3.	Repeat #1 and #2 with n = 2i, i = 1, …, 20. Place the numbers in a table and compare the results for Cuckoo, QuadraticProbing and SeparateChaining. Comment on the times obtained and compare them with the complexities as discussed in class. 
4.	Use the Java classes BinarySearchTree, AVLTree, RedBlackBST, SplayTree given in class. For each tree: 
a.	Insert 100,000 integer keys, from 1 to 100,000 (in that order). Find the average time for each insertion. Note: you can add the following VM arguments to your project:       -Xss16m. This will help increase the size of the recursion stack. 
b.	Do 100,000 searches of random integer keys between 1 and 100,000. Find the average time of each search. 
c.	Delete all the keys in the trees, starting from 100,000 down to 1 (in that order). Find the average time of each deletion. 
5.	For each tree: 
a.	Insert 100,000 keys between 1 and 100,000. Find the average time of each insertion. b. Repeat #4.b. 
c. Repeat #4.c but with random keys between 1 and 100,000. Note that not all the keys may be found in the tree. 
6.	Draw a table that contains all the average times found in #4 and #5. Comment on the results obtained and compare them with the worst-case and average-case running times of each operation for each tree. Which tree will you use in your implementations for real problems? Note: you decide on the format of the table (use your creativity to present the results in the best possible way). 
