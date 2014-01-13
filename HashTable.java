/**
 * This class is an implementation of a java Hashtable  
 * 
 * bugs: None known
 * 
 * @author Steven Volocyk
 * 
 */

import java.io.*;
import java.util.LinkedList;

/**
 * This class implements a hashtable that using chaining for collision handling.
 * The chains are implemented using LinkedLists.  When a hashtable is created, 
 * its initial size and maximum load factor are specified. The hashtable can 
 * hold arbitrarily many items and resizes itself whenever it reaches 
 * its maximum load factor. Note that this hashtable allows duplicate entries.
 */
public class HashTable<T> {
	// Size of list, hash code, number of items.
	private static int mSize, hash, numItems = 0; 
	// load factor and comparison value for determing if resize needed.
	private static double mLoad, compareValue = 0.0;
	// hashtables
	private LinkedList<T>[] mcHashTable, newTable;
	// Linked lists for hashtables.
	private LinkedList<T> list, newList = null;
	
    /**
     * Constructs an empty hashtable with the given initial size and maximum '
     * load factor. The load factor should be a real 
     * number greater than 0.0 (not a percentage).  For example, to create a 
     * hash table with an initial size of 10 and a load factor of 0.85, one 
     * would use:
     * <dir><tt>HashTable ht = new HashTable(10, 0.85);</tt></dir>
     *
     * @param initSize The initial size of the hashtable.  If the size is less
     * than or equal to 0, an IllegalArgumentException is thrown.
     * @param loadFactor The load factor expressed as a real number.  If the
     * load factor is less than or equal to 0.0, an IllegalArgumentException is
     * thrown.
     **/
    public HashTable(int initSize, double loadFactor) {
    	if(!validation(initSize) || !validation(loadFactor)) {
    		throw new IllegalArgumentException();
    	}
    	
    	mSize = initSize;
    	mLoad = loadFactor;
    	mcHashTable = (LinkedList<T>[]) (new LinkedList[mSize]);
    	for (int i = 0; i < mcHashTable.length; i++ ) {
    		mcHashTable[i] = new LinkedList<T>();
    	}
    		
    }
    /**
     * Used for validating input on hashtable size and load factor.
     * @param test input to test
     * @return boolean true of or false if not valid.
     */
    private boolean validation(double test) { 
    	if (test <= 0.0 || test <= 0) {
    		return false;
    	}
    	else { return true; }
    }
    /**
     * Determines if the given item is in the hashtable and returns it if 
     * present.  If more than one copy of the item is in the hashtable, the 
     * first copy encountered is returned.
     *
     * @param item the item to search for in the hashtable
     * @return the item if it is found and null if not found
     **/
    public T lookup(T item) {
    	hash = item.hashCode() % mSize;
    	if (hash < 0) {
    		hash = Math.abs(hash);
    	}
    	list = mcHashTable[hash];
    	while (list != null) {
    		if (list.contains(item)) {
    			T foundItem = list.get(list.indexOf(item));
    			return foundItem;
    		}
    	}
    	return null;
    }
    
    
    /**
     * Inserts the given item into the hash table.  
     * 
     * If the load factor of the hashtable after the insert would exceed 
     * (not equal) the maximum load factor (given in the constructor), then the 
     * hashtable is resized.
     * 
     * When resizing, to make sure the size of the table is good, the new size 
     * is always 2 x <i>old size</i> + 1.  For example, size 101 would become 
     * 203.  (This  guarantees that it will be an odd size.)
     * 
     * <p>Note that duplicates <b>are</b> allowed.</p>
     *
     * @param item the item to add to the hashtable
     **/
    public void insert(T item) {
    	hash = item.hashCode() % mSize;
    	if (hash < 0) {
    		hash = Math.abs(hash);
    	}
    	list = mcHashTable[hash];
    	list.addLast(item);
    	numItems++;
    	double check = (double) numItems;
    	compareValue = check/mSize;
    	if (compareValue > mLoad) {
    		resize();
    	}
    }
    /**
     * private method that will resize the hashtable relative
     * to the load factor, followed be re-hasing, and re-inserting 
     * the items that were in the old table, into the new table at
     * new hashed positions.
     */
    private void resize() {
    	// new list 2* the old lists size + 1
    	// creates empty linked list in each array positin
    	newTable = (LinkedList<T>[]) (new LinkedList[(mSize * 2) + 1]);
    	for (int i = 0; i < newTable.length; i++ ) {
    		newTable[i] = new LinkedList<T>();
    	}
    	// following loops extract data from old list
    	// enter it into the new list via rehashing.
    	for (int i = 0; i < mcHashTable.length; i++) {
    		list = mcHashTable[i];    		
    		for (int j = 0; j < list.size(); j++) {
    			T data = list.get(j);	
    			hash = data.hashCode() % newTable.length; 
    			if (hash < 0) {
    	    		hash = Math.abs(hash);
    	    	}
    			newList = newTable[hash];
    			newList.addLast(data);
    		}	
    	}
    	mcHashTable = newTable;
    	mSize = newTable.length;
    }
    
    /**
     * Removes and returns the given item from the hashtable.  If the item is 
     * not in the hashtable, <tt>null</tt> is returned.  If more than one copy 
     * of the item is in the hashtable, only the first copy encountered is 
     * removed and returned.
     *
     * @param item the item to delete in the hashtable
     * @return the removed item if it was found and null if not found
     **/

    public T delete(T item) {
    	hash = item.hashCode() % mSize;	
    	if (hash < 0) {
    		hash = Math.abs(hash);
    	}
    	list = mcHashTable[hash];
    	if(!list.contains(item) || list == null ) {
    		return null;
    	}
    	else {
    		int pos = list.indexOf(item);
    		T foundItem = list.remove(pos); 
    		numItems--;
    		return foundItem;
    	}
    }
    
  
    /**
     * Prints statistics about the hashtable to the PrintStream supplied.
     * The statistics displayed are: 
     * <ul>
     * <li>the current table size
     * <li>the number of items currently in the table 
     * <li>the current load factor
     * <li>the length of the largest chain
     * <li>the number of chains of length 0
     * <li>the average length of the chains of length > 0
     * </ul>
     *
     * @param out the place to print all the output
     **/
    public void displayStats(PrintStream out) {
    	// for calculating the largest linked list chain
    	int longestChain = largestChain(); 
    	int emptyLists = zeroChains(); // # of empty chains.
    	double everyChain = numItems;  // total number of chains/items
    	double averageLength = allChains() / (mSize - emptyLists) ; // avg length of chains
    	double endLoad = everyChain / mSize; // load factor
    	out.println("Hastable Statistics:");
        out.println("  " + mSize);
        out.println("  " + numItems);
        out.println("  " + endLoad);
        out.println("  " + longestChain);
        out.println("  " + emptyLists);
        out.println("  " + averageLength);

    }
    /**
     * Calculates the largest Linked List chain in the hashtable
     * @return maxLength largest chain.
     */
    private int largestChain() {
    	int maxLength = 0;
    	for (int i = 0; i < mcHashTable.length; i++) {
    		list = mcHashTable[i];  
    		if (list.size() > maxLength) {
    			maxLength = list.size();
    		}
    	}	
    	return maxLength;
    }
    /**
     * Calculates the total number of chains/items in the hashtable
     * in the form of a double.
     * @return totalChains 
     */
    private double allChains() {
    	int totalChains = 0;
    	for (int i = 0; i < mcHashTable.length; i++) {
    		list = mcHashTable[i];
    		if (!(list.size() == 0)) {
    			totalChains += list.size();
    		}	
    	}	
    	return totalChains;
    }
    /**
     * Calculates the number of empty chains in the hashtable.
     * @return number of empty chains.
     */
    private int zeroChains() {
    	int emptyChains = 0;
    	for (int i = 0; i < mcHashTable.length; i++) {
    		list = mcHashTable[i];  
    		if (list.size() == 0) {
    			emptyChains++;
    		}
    	}	
    	return emptyChains;
    }
}
