package List;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/* 1) by default, the capacity of arraylist is 10
 * 2) resize is to expand 1/2 initialCapacity
 * */
public class ArrayList<E> extends AbstractSequentialList<E> implements List<E> {
	private Object[] elementData;
	private int size;
	
	//constructor
	public ArrayList(int initialCapacity) {
		if (initialCapacity < 0)
			throw new IndexOutOfBoundsException(initialCapacity + " is negative.");
		elementData = new Object[initialCapacity];
		size = 0;
	}
	public ArrayList() {
		this(10);
	}
	
	public int size() {
		return size;
	}
	public boolean isEmpty() {
		return size == 0;
	}
	/*search inside the arraylist
	 * time complexity O(1)
	 * */
	public E get(int index) {
		assert rangeCheck(index);
		return elementData(index);
	}
	public E set(int index, E e) {
		rangeCheck(index);
		E oldValue = elementData(index);
		elementData[index] = e;
		return oldValue;
	}
	@SuppressWarnings("unchecked")
	public E elementData(int index) {
		return (E) elementData[index];
	}
	public boolean contains(Object o) {
		return indexOf(o) > 0;
	}
	//from start to end
	public int indexOf(Object o) {
		if (o == null) {
			for (int i = 0; i < size; i++) {
				if (elementData[i] == null)
					return i;
			}
		}
		else {
			for (int i = 0; i < size; i++) {
				if (o.equals(elementData[i]) || o == elementData)
					return i;
			}
		}
		return -1;
	}
	//from end to start: time complexity is O(n) in the worst case
	public int lastIdexOf(Object o) {
		if (o == null) {
			for (int i = size - 1; i >= 0; i--) {
				if (elementData[i] == null)
					return i;
			}
		}
		else {
			for (int i = size - 1; i >= 0; i--) {
				if (o.equals(elementData[i]) || o == elementData)
					return i;
			}
		}
		return -1;
	}
	
	/* insert a new item into the arraylist
	 * time complexity: O(n) in worst case --> move other items to get empty space for added item;
	 * */
	public boolean add(E e) {
		ensureCapacity(size + 1);
		elementData[size++] = e;
		return true;
	}
	public void add(int index, E e) {
		ensureCapacity(size + 1);
		System.arraycopy(elementData, index, elementData, index + 1, size - index);
		elementData[index] = e;
		size++;
	}
	
	/*	remove a given item from the arraylist
	 * 	time complexity: O(n) in worst case
	 * */
	public E remove(int index) {
		rangeCheck(index);
		modCount++;
		E oldVal = elementData(index);
		int moves = size - index - 1;
		System.arraycopy(elementData, index + 1, elementData, index, moves);
		elementData[--size] = null;
		return oldVal;
	}
	public boolean remove(Object o) {
		if (o == null) {
			for (int i = 0; i < size; i++) {
				if (elementData[i] == null) {
					remove(i);
					return true;
				}
			}
		}
		else {
			for (int i = 0; i < size; i++) {
				if (o.equals(elementData[i]) || o == elementData[i]) {
					remove(i);
					return true;
				}
			}
		}
		return false;
	}
	private void ensureCapacity(int minCapacity) {
		if (minCapacity > elementData.length) {
			grow(minCapacity);
			modCount++;
		}
	}
	private final int DEFAULT_MAX_SIZE = Integer.MAX_VALUE - 8;
	private void grow(int minCapacity) {
		int oldCapacity = elementData.length;
		int newCapacity = oldCapacity + (oldCapacity >> 1);
		if (newCapacity < minCapacity)
			newCapacity = minCapacity;
		if (minCapacity > DEFAULT_MAX_SIZE) {
			newCapacity = expandInLargeSize(newCapacity);
		}
		elementData = Arrays.copyOf(elementData, newCapacity);
	}
	private int expandInLargeSize(int maxSize) {
		if (maxSize < 0)
			throw new IllegalArgumentException();
		return maxSize > DEFAULT_MAX_SIZE ? DEFAULT_MAX_SIZE : maxSize;
	}
	
	private boolean rangeCheck(int index) {
		if (index >= 0 && index < size)
			return true;
		return false;
	}
	
	
	/*Iterator function below*/
	public Iterator<E> iterate() {
		return new Itr();
	}
	
	private class Itr<E> implements Iterator<E> {
		protected int cursor;
		protected int curIndex = -1;
		protected int expectedModCount;
		
		public Itr() {
			cursor = 0;
			expectedModCount = modCount;
		}
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return cursor < size;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() {
			checkForComodification();
			if (!hasNext())
				throw new NoSuchElementException();
			Object[] elementData = ArrayList.this.elementData;
			int i = cursor;
			if (i >= elementData.length)
				throw new ConcurrentModificationException();
			cursor += 1;
			return (E) elementData(i);
		}
		
		public void remove() {
			if (curIndex == -1)
				throw new NoSuchElementException();
			checkForComodification();
			try {
				ArrayList.this.remove(curIndex);
				cursor = curIndex;
				curIndex = -1;
				expectedModCount = modCount;
			}
			catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		
		final void checkForComodification() {
			if (expectedModCount != modCount)
				throw new ConcurrentModificationException();
		}
		
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class ListItr<E> extends Itr<E> implements ListIterator<E> {
		ListItr(int index) {
			super();
			cursor = index;
		}

		@Override
		public boolean hasPrevious() {
			// TODO Auto-generated method stub
			return curIndex > 0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E previous() {
			checkForComodification();
			int i = cursor - 1;
			if (i < 0)
				throw new NoSuchElementException();
			Object[] elementData = ArrayList.this.elementData;
			if (i >= elementData.length)
				throw new ConcurrentModificationException();
			cursor = i;
			return (E) elementData(i);
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
		public void set(E e) {
			// TODO Auto-generated method stub
			if (curIndex < 0)
				throw new NoSuchElementException();
			checkForComodification();
			try {
				ArrayList.this.set(curIndex, e);
				expectedModCount = modCount;
			}
			catch (IndexOutOfBoundsException ex) {
				ex.printStackTrace();
			}
			
		}

		@Override
		public void add(E e) {
			// TODO Auto-generated method stub
			if (curIndex < 0)
				throw new IllegalArgumentException();
			checkForComodification();
			try {
				int i = cursor;
				ArrayList.this.add(i, e);
				cursor = i + 1;
				curIndex = -1;
				expectedModCount = modCount;
			}
			catch (IndexOutOfBoundsException ex) {
				ex.printStackTrace();
			}	
		}
	}
}
