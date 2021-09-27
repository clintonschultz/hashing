/*
Name: Clinton J. Schultz
Professor: Dr. Jeff Ward
Assignment: HW6 - Hashing
Date: November 28, 2020

This java program executes the steps necessary for numbers to be entered into a table
using Quadratic Probing with open addressing. MyQuadraticHashSet.java implements MySet.java,
which uses linear probing, so the methods have been overridden and fitted to be used for
quadratic probing.
 */

public class MyQuadraticHashSet<E> implements MySet<E> {

    private int tableSize;
    private double loadFactorThreshold;
    private int size = 0;
    private int[] primeTableSize;
    private int i = 0;
    private int numRemoved = 0;
    private Object[] hashTable;

    private final static Object REMOVED = new Object();

    public MyQuadraticHashSet(double loadFactorThreshold, int[] primesForQuadProbing) {
        this.loadFactorThreshold = loadFactorThreshold;
        primeTableSize = primesForQuadProbing;
        tableSize = primeTableSize[i];
        hashTable = new Object[tableSize];
    }

    @Override
    public boolean contains(Object e) {
        long modifier = 0;
        int indexContains = probeIndex(e.hashCode(), modifier, tableSize);
        if (hashTable[indexContains] == null) {
            return false;
        }
        else if (hashTable[indexContains].equals(e)) {
            return true;
        }
        else {
            while (hashTable[indexContains] != null) {
                if (hashTable[indexContains].equals(e)) {
                    return true;
                }
                modifier++;
                indexContains = probeIndex(e.hashCode(), modifier, tableSize);
            }
        }
        return false;
    }

    @Override
    public void clear() {
        size = 0;
        numRemoved = 0;
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = null;
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(Object e) {
        long modifier = 0;
        if (contains(e)) {
            return false;
        }
        if ((size + numRemoved) > (tableSize * loadFactorThreshold)) {
            resize();
        }
        int addIndex = probeIndex(e.hashCode(), modifier, tableSize);
        if (hashTable[addIndex] == null) {
            hashTable[addIndex] = e;
            size++;
            return true;
        }
        else {
            while (hashTable[addIndex] != null && hashTable[addIndex] != REMOVED) {
                modifier++;
                addIndex = probeIndex(e.hashCode(), modifier, tableSize);
            }
            hashTable[addIndex] = e;
            size++;
            return true;
        }
    }

    @Override
    public boolean remove(Object e) {
        long modifier = 0;
        if (!contains(e)) {
            return false;
        }
        else {
            int removeIndex = probeIndex(e.hashCode(), modifier, tableSize);
            if (hashTable[removeIndex] != null || hashTable[removeIndex] == REMOVED) {
                while (!hashTable[removeIndex].equals(e)) {
                    modifier++;
                    removeIndex = probeIndex(e.hashCode(), modifier, tableSize);
                }
            }
            hashTable[removeIndex] = REMOVED;
            numRemoved++;
            size--;
            return true;
        }
    }

    @Override
    public int size() {
        return size;
    }

    private static int probeIndex(int hashCode, long modifier, int tableSize) {
        return (int)((hashCode % tableSize + modifier * modifier) % tableSize);
    }

    private void resize() {
        Object[] prevTable = hashTable;
        i+=1;
        tableSize = primeTableSize[i];
        hashTable = new Object[tableSize];
        size = 0;

        for (Object o: prevTable) {
            if (o != null && o != REMOVED) {
                add(o);
            }
        }
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
}