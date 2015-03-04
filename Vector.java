package List;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class Vector<E> extends AbstractList<E> implements List<E> {
	private Object[] elementData;
	private int size;
	protected int incrementCapacity;
	
	public Vector(int initialCapacity, int incrementCapacity) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException(initialCapacity + " is negative. ");
		elementData = new Object[initialCapacity];
		this.incrementCapacity = incrementCapacity;
		size = 0;
	}
	public Vector(int initialCapacity) {
		this(initialCapacity, 0);
	}
	public Vector() {
		this(10);
	}
	public synchronized int capacity() {
		return elementData.length;
	}
	public synchronized int size() {
		return size;
	}
	public synchronized boolean isEmpty() {
		return size == 0;
	}
	
	/*search operation
	 * time complexity is O(1)
	 * */
	public synchronized E get(int index) {
		return elementAt(index);
	}
	public synchronized E elementAt(int index) {
		rangeCheck(index);
		return elementData(index);
	}
	public synchronized E firstElement() {
		return elementData(0);
	}
	public synchronized E lastElement() {
		return elementData(size - 1);
	}
	E elementData (int index) {
		return (E) elementData[index];
	}
	public synchronized int indexOf(Object o) {
		return indexOf(0, o);
	}
	private synchronized int indexOf(int index, Object o) {
		rangeCheck(index);
		if (o == null) {
			for (int i = index; i < size; i++) {
				if (elementData[i] == null)
					return i;
			}
		}
		else {
			for (int i = index; i < size; i++) {
				if (o.equals(elementData[i]))
					return i;
			}
		}
		return -1;
	}
	
	/* insert a new element into a vector
	 * time complexity is O(n) in worst case
	 * */
	public synchronized boolean add(E e) {
		modCount++;
		ensureCapacity(size + 1);
		elementData[size++] = e;
		return true;
	}
	public synchronized void add(int index, E obj) {
		rangeCheck(index);
		insertElementAt(index, obj);
	}
	public synchronized void insertElementAt(int index, E obj) {
		modCount++;
		assert rangeCheck(index);
		ensureCapacity(size + 1);
		System.arraycopy(elementData, index, elementData, index + 1, size - index);
		elementData[index] = obj;
		size++;
	}
	
	/* remove an element from a vector
	 * time complexity: O(n) in the worst case
	 * */
	public synchronized E remove(int index) {
		modCount++;
		rangeCheck(index);
		E oldValue = elementData(index);
		int numMoved = size - index - 1;
		System.arraycopy(elementData, index + 1, elementData, index, numMoved);
		elementData[--size] = null;
		return oldValue;
	}
	public synchronized void clear() {
		removeAllElements();
		
	}
	public synchronized void removeAllElements() {
		modCount++;
		for (int i = 0; i < size; i++) {
			elementData[i] = null;
		}
		size = 0;
	}
	
	private synchronized void ensureCapacity(int minCapacity) {
		if (minCapacity >= elementData.length) {
			grow(minCapacity);
		}
	}
	private void grow(int minCapacity) {
		if (minCapacity < 0)
			throw new IndexOutOfBoundsException();
		int oldCapacity = elementData.length;
		int newCapacity = oldCapacity + (incrementCapacity > 0 ? incrementCapacity : oldCapacity);
		if (newCapacity < minCapacity) {
			newCapacity = minCapacity;
		}
		if (newCapacity > DEFAULT_MAX_SIZE) {
			newCapacity = hugeCapacity(newCapacity);
		}
		elementData = Arrays.copyOf(elementData, newCapacity);
	}
	private final int DEFAULT_MAX_SIZE = Integer.MAX_VALUE - 8;
	private int hugeCapacity(int minCapacity) {
		if (minCapacity < 0)
			throw new IndexOutOfBoundsException();
		return minCapacity > DEFAULT_MAX_SIZE ? DEFAULT_MAX_SIZE : minCapacity;
	}
	
	private boolean rangeCheck(int index) {
		if (index >= 0 && index < size)
			return true;
		return false;
	}
	public synchronized E set(int index, Object e) {
		assert rangeCheck(index);
		E oldValue = elementData(index);
		elementData[index] = e;
		return oldValue;
	}
	
	/*Iterator: iterate throw all structure*/
	public synchronized Iterator<E> iterate() {
		return new Itr();
	}
	private class Itr<E> implements Iterator<E> {
		protected int cursor;
		protected int lastReturned = -1;
		protected int expectedModCount = modCount;
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return cursor < size;
		}

		@Override
		public E next() {
			// TODO Auto-generated method stub
			synchronized(Vector.this) {
				int i = cursor;
				if (i >= size)
					throw new NoSuchElementException();
				cursor = i + 1;
				return (E) elementData(lastReturned = i);
			}
		}
		
		public void remove() {
			if (lastReturned == -1)
				throw new NoSuchElementException();
			synchronized (Vector.this) {
				checkForComodification();
				Vector.this.remove(lastReturned);
				expectedModCount = modCount;
			}
			cursor = lastReturned;
			lastReturned = -1;	
		}
		protected void checkForComodification() {
			if (expectedModCount != modCount)
				throw new ConcurrentModificationException();
		}
		
	}
	public ListIterator<E> listIterator(int index) {
		rangeCheck(index);
		return new ListItr(index);
	}
	private class ListItr extends Itr<E> implements ListIterator<E> {
		public ListItr(int index) {
			super();
			if (index < 0 || index >= size)
				throw new IllegalArgumentException();
			cursor = index;
		}
		@Override
		public boolean hasPrevious() {
			// TODO Auto-generated method stub
			return cursor > 0;
		}

		@Override
		public E previous() {
			// TODO Auto-generated method stub
			synchronized (Vector.this) {
				checkForComodification();
				int i = cursor - 1;
				if (i < 0)
					throw new NoSuchElementException();
				cursor = i;
				return elementData(lastReturned = i);
			}
		}

		@Override
		public int nextIndex() {
			// TODO Auto-generated method stub
			return cursor;
		}

		@Override
		public int previousIndex() {
			// TODO Auto-generated method stub
			return cursor - 1;
		}

		@Override
		public void set(Object e) {
			if (lastReturned == -1)
				throw new NoSuchElementException();
			synchronized (Vector.this) {
				checkForComodification();
				Vector.this.set(lastReturned, e);
			}
			
		}

		@Override
		public void add(Object e) {
			int i = cursor;
			synchronized(Vector.this) {
				checkForComodification();
				Vector.this.add(i, (E) e);
				expectedModCount = modCount;
			}
			cursor = i + 1;
			lastReturned = -1;
			
		}
		
	
		
	}
	
}
