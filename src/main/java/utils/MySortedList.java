package utils;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.util.*;
import java.util.function.*;

public class MySortedList<K extends Comparable<K>, V> implements Iterable<V>
{
    private static final String KEY_EXTRACTOR_NOT_FOUND = "Key extractor not found.";
    private ArrayList<V> values;
    private ArrayList<K> keys;

    private transient boolean hasExtractor;
    private transient Function<V, K> keyExtractor;

    private transient Comparator<K> keyComparator;


    public MySortedList()
    {
        this(null, null);
    }

    public MySortedList(Function<V, K> keyExtractor)
    {
        this(keyExtractor, null);
    }

    public MySortedList(Comparator<K> keyComparator)
    {
        this(null, keyComparator);
    }

    public MySortedList(Function<V, K> keyExtractor, Comparator<K> keyComparator)
    {
        values = new ArrayList<>();
        hasExtractor = keyExtractor != null;
        if (!hasExtractor)
            keys = new ArrayList<>();

        if (!hasExtractor)
        {
            this.keyExtractor = v -> {
                throw new UnsupportedOperationException(KEY_EXTRACTOR_NOT_FOUND);
            };
        }
        else
            this.keyExtractor = keyExtractor;
        this.keyComparator = keyComparator;
        if (keyComparator == null)
            this.keyComparator = Comparator.naturalOrder();

    }

    private int binarySearchKey(K key)
    {
        if (!hasExtractor)
        {
            return Collections.binarySearch(keys, key, keyComparator);
        }
        else
        {
            int low = 0;
            int high = values.size() - 1;

            while (low <= high)
            {
                int mid = (low + high) >>> 1;
                V midVal = values.get(mid);
                int cmp = keyComparator.compare(keyExtractor.apply(midVal), key);

                if (cmp < 0)
                    low = mid + 1;
                else if (cmp > 0)
                    high = mid - 1;
                else
                    return mid; // key found
            }
            return ~low;  // key not found
        }
    }

    public boolean add(K key, V value)
    {
        int index = binarySearchKey(key);
        if (index >= 0)
            return false;
        values.add(~index, value);
        if (!hasExtractor)
            keys.add(~index, key);
        return true;
    }

    public boolean addValue(V value)
    {
        return add(keyExtractor.apply(value), value);
    }

    public boolean remove(K key)
    {
        int index = binarySearchKey(key);
        if (index < 0)
            return false;
        values.remove(index);
        if (!hasExtractor)
            keys.remove(index);
        return true;
    }

    public boolean removeValue(V value)
    {
        return remove(keyExtractor.apply(value));
    }

    public int indexOf(K key)
    {
        int index = binarySearchKey(key);
        if (index < 0)
            return -1;
        return index;
    }

    public int indexOfValue(V value)
    {
        return indexOf(keyExtractor.apply(value));
    }

    public V get(K key)
    {
        return values.get(binarySearchKey(key));
    }

    public V getByIndex(int index)
    {
        return values.get(index);
    }

    public K getKeyByIndex(int index)
    {
        if (hasExtractor)
            return keyExtractor.apply(getByIndex(index));
        else
            return keys.get(index);
    }

    /**
     * Gives a sublist of current list using a comparator.
     *
     * @param key
     * @param comparator Should not give an opposite order.
     * @return
     */
    public MySortedList<K, V> getRange(K key, Comparator<K> comparator)
    {
        int first = findFirstIndex(key, comparator);
        if (first < 0)
            return new MySortedList<>();
        int last = findLastIndex(key, comparator);

        MySortedList<K, V> retVal;
        if (hasExtractor)
            retVal = new MySortedList<>(keyExtractor, keyComparator);
        else
            retVal = new MySortedList<>(keyComparator);

        retVal.values = new ArrayList<>(values.subList(first, last + 1));
        if (!hasExtractor)
            retVal.keys = new ArrayList<>(keys.subList(first, last + 1));

        return retVal;
    }

    public int findFirstIndex(K key, Comparator<K> comparator)
    {
        int low = 0, high = size() - 1;
        while (low <= high)
        {
            int mid = (low + high) >>> 1;
            K midKey = getKeyByIndex(mid);

            int cmp = comparator.compare(midKey, key);
            if (cmp == 0 && (mid == 0 || comparator.compare(key, getKeyByIndex(mid - 1)) > 0))
                return mid;
            else if (cmp < 0)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return ~low;
    }

    public int findLastIndex(K key, Comparator<K> comparator)
    {
        int low = 0, high = size() - 1;
        while (low <= high)
        {
            int mid = (low + high) >>> 1;
            K midKey = getKeyByIndex(mid);

            int cmp = comparator.compare(midKey, key);
            if (cmp == 0 && (mid == size() - 1 || comparator.compare(key, getKeyByIndex(mid + 1)) < 0))
                return mid;
            else if (cmp > 0)
                high = mid - 1;
            else
                low = mid + 1;
        }
        return ~low;
    }

    public List<K> getKeys()
    {
        return Collections.unmodifiableList(keys);
    }

    public List<V> getValues()
    {
        return Collections.unmodifiableList(values);
    }

    public V getMin()
    {
        if (size() == 0)
            return null;
        return values.get(0);
    }

    public V getMax()
    {
        if (size() == 0)
            return null;
        return values.get(values.size() - 1);
    }

    public int size()
    {
        return values.size();
    }

    @Override
    public Iterator<V> iterator()
    {
        return values.iterator();
    }

    @Override
    public void forEach(Consumer<? super V> action)
    {
        values.forEach(action);
    }
}
