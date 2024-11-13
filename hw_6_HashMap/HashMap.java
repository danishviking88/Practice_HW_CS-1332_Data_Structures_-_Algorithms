import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Your implementation of HashMap.
 * 
 * @author Yue Gu
 * @userid ygu65
 * @GTID 903055355
 * @version 1.0
 */
public class HashMap<K, V> implements HashMapInterface<K, V> {

    // Do not make any new instance variables.
    private MapEntry<K, V>[] table;
    private int size;

    /**
     * Create a hash map with no entries. The backing array has an initial
     * capacity of {@code INITIAL_CAPACITY}.
     *
     * Do not use magic numbers!
     *
     * Use constructor chaining.
     */
    public HashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Create a hash map with no entries. The backing array has an initial
     * capacity of {@code initialCapacity}.
     *
     * You may assume {@code initialCapacity} will always be positive.
     *
     * @param initialCapacity initial capacity of the backing array
     */
    public HashMap(int initialCapacity) {
        table = new MapEntry[initialCapacity];
        size = 0;
    }
/**
     * Adds the given key-value pair to the HashMap.
     * If an entry in the HashMap already has this key, replace the entry's
     * value with the new one passed in.
     *
     * In the case of a collision, use linear probing as your resolution
     * strategy.
     *
     * Check to see if the backing array needs to be regrown BEFORE adding. For
     * example, if my HashMap has a backing array of length 5, and 3 elements in
     * it, I should regrow at the start of the next add operation (even if it
     * is a key that is already in the hash map). This means you must account
     * for the data pending insertion when calculating the load factor.
     *
     * When regrowing, increase the length of the backing table by
     * 2 * old length + 1. Use the resizeBackingTable method.
     *
     * Return null if the key was not already in the map. If it was in the map,
     * return the old value associated with it.
     *
     * @param key key to add into the HashMap
     * @param value value to add into the HashMap
     * @throws IllegalArgumentException if key or value is null
     * @return null if the key was not already in the map.  If it was in the
     * map, return the old value associated with it
     */
    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and/or Value is null, unable to comply.");
        }

        double loadRatio = (size + 1.0) / table.length;
        if (loadRatio > MAX_LOAD_FACTOR) {
            int doubleLength = (2 * table.length) + 1;
            resizeBackingTable(doubleLength);
        }

        MapEntry<K, V> entry = new MapEntry(key, value);
        V deletedValue = addMapEntry(entry);
        if (deletedValue == null) {
            size++;
        }

        return deletedValue;
    }

    // Help to put for adding entries.
    private V addMapEntry(MapEntry<K, V> entry) {
        // Determine the starting index using the hash value.
        int index = Math.abs(entry.getKey().hashCode() % table.length);

        // This is the index that you will insert the MapEntry, which will happen in the event that the key is not already in the table.
        // You will either have an index indicating a "removed" MapEntry that you wish to overwrite, or an empty null spot where you can put the MapEntry. 
        Integer insertIndex = null;
        V value = null;

        boolean exitFlag = false;
        while (!exitFlag) {
            if (table[index] != null) {
                // If the index in question has a "removed" flag, then mark this flag for being the slot we wish to overwrite. 
                if (table[index].isRemoved() == true && insertIndex == null) {
                    insertIndex = index;
                }

                // If the index's key matches the entry key, copy the value and overwrite the index's value.
                // Otherwise, increment to the next index and try again. 
                if (table[index].getKey().equals(entry.getKey()) && table[index].isRemoved() == false) {
                    value = table[index].getValue();
                    table[index].setValue(entry.getValue());
                    return value;
                } else {
                    index = advanceIndexOnce(index);
                }
            // When you hit an empty null spot, set the insertIndex to the index, since we didn't find a matching key and there was not "removed" slot seen.
            } else {
                if (insertIndex == null) {
                    insertIndex = index;
                }
                exitFlag = true;
            }
        }

        table[insertIndex] = entry;
        return value;
    }

    /**
     * Removes the entry with a matching key from the HashMap.
     *
     * @param key the key to remove
     * @throws IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException if the key does not exist
     * @return the value previously associated with the key
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null, unable to comply.");
        } else if (!containsKey(key)) {
            throw new java.util.NoSuchElementException("Key is not present in the HashMap, unable to comply.");
        }
        
        int actualIndex = findActualIndexOfKey(key);
        V value = table[actualIndex].getValue();

        table[actualIndex].setRemoved(true);
        size--;

        return value;
    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key the key to search for
     * @throws IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException if the key is not in the map
     * @return the value associated with the given key
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null, unable to comply.");
        } else if (!this.containsKey(key)) {
            throw new java.util.NoSuchElementException("Key is not present in the HashMap, unable to comply.");
        }
        // Determine the starting index using the hash value.
        int index = Math.abs(key.hashCode() % table.length);
        
        while (table[index] != null) {
            // If the index's key equals the key, return true, else keep advancing until you find it or you hit null.
            if (table[index].getKey().equals(key) && table[index].isRemoved() == false) {
                return table[index].getValue();
            }
            index = advanceIndexOnce(index);
        }

        return null;
    }

    /**
     * Returns whether or not the key is in the map.
     *
     * @param key the key to search for
     * @throws IllegalArgumentException if key is null
     * @return whether or not the key is in the map
     */
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null, unable to comply.");
        }

        // Determine the starting index using the hash value.
        int index = Math.abs(key.hashCode() % table.length);
        int exitCounter = table.length;

        while (table[index] != null && exitCounter > 0) {
            // If the index's key equals the key, return true, else keep advancing until you find it or you hit null.
            if (table[index].getKey().equals(key) && table[index].isRemoved() == false) {
                return true;
            }
            index = advanceIndexOnce(index);
            exitCounter--;
        }

        return false;
    }

    /**
     * Clears the table and resets it to the default length.
     */
    @Override
    public void clear() {
        table = new MapEntry[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the number of elements in the map.
     *
     * @return number of elements in the HashMap
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     * Use {@code java.util.HashSet}.
     *
     * @return set of keys in this map
     */
    @Override
    public Set<K> keySet() {
        ArrayList<K> keysList = new ArrayList<K>();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && table[i].isRemoved() == false) {
                keysList.add(table[i].getKey());
            }
        }
        Set<K> keysSet = new HashSet<K>(keysList);

        return keysSet;
    }

    /**
     * Returns a List view of the values contained in this map.
     * beginning with the first index of the backing array.
     * Use any class that implements the List interface
     * This includes {@code java.util.ArrayList} and
     * {@code java.util.LinkedList}.
     *
     * @return list of values in this map
     */
    @Override
    public List<V> values() {
        ArrayList<V> valuesList = new ArrayList<V>();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && table[i].isRemoved() == false) {
                valuesList.add(table[i].getValue());
            }
        }

        return valuesList;
    }

    /**
     * Resize the backing table to {@code length}.
     *
     * After resizing, the table's load factor is permitted to exceed
     * MAX_LOAD_FACTOR. No adjustment to the backing table's length is necessary
     * should this occur.
     *
     * Remember that you cannot just simply copy the entries over to the new
     * array.
     *
     * @param length new length of the backing table
     * @throws IllegalArgumentException if length is non-positive or less than
     * the number of items in the hash map.
     */
    @Override
    public void resizeBackingTable(int length) {
        if (length < 0 || length < size) {
            throw new IllegalArgumentException("Length is a negative number or too small, unable to comply.");
        }

        MapEntry<K, V>[] oldTable = table;
        table = new MapEntry[length];        
        for (MapEntry<K,V> mapEntry : oldTable) {
            if (mapEntry != null && mapEntry.isRemoved() == false) {
                addMapEntry(mapEntry);
            }
        }
    }

    /**
     * DO NOT USE THIS METHOD IN YOUR CODE.  IT IS FOR TESTING ONLY.
     *
     * @return the backing array of the data structure, not a copy.  INCLUDE
     * EMPTY SPACES
     */
    @Override
    public MapEntry<K, V>[] getTable() {
        return table;
    }

    // Advanced the index forward once, with array wrap around taken into account. 
    private int advanceIndexOnce(int index) {
        if (index == table.length - 1) {
            return 0;
        } else {
            return index + 1;
        }
    }

    private int findActualIndexOfKey(K key) {
        if (!containsKey(key)) {
            throw new IllegalArgumentException("Key is not present in table, unable to comply.");
        }

        int index = Math.abs(key.hashCode() % table.length);

        boolean exitFlag = false;
        while (exitFlag == false) {
            if (table[index] == null || !table[index].getKey().equals(key)) {
                index = advanceIndexOnce(index);
            } else {
                exitFlag = true;
            }
        }

        return index;
    }
}
