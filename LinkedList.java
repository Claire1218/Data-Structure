package List;

import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LinkedList<E> extends AbstractSequentialList<E> implements List<E> {
	private int size;
	private Node<E> head;
	private Node<E> tail;
	
	public LinkedList() {
		size = 0;
		head = tail = null;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}
	/*insertion operation*/
	@Override
	public boolean add(E e) {
		linkLast(e);
		return true;
	}
	public boolean addFirst(E e) {
		linkFirst(e);
		return true;
	}
	public boolean addLast(E e) {
		linkLast(e);
		return true;
	}
	public boolean addAll(Collection<? extends E> c) {
		return addAll(size, c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		assert checkIndex(index);
		Object[] arr = c.toArray();
		if (arr.length == 0)
			return false;
		Node<E> pred;
		Node<E> succ;
		if (index == size) {
			succ = null;
			pred = tail;
		}
		else {
			succ = node(index);
			pred = succ.pre;
		}
		
		for (Object temp: arr) {
			@SuppressWarnings("unchecked")
			E e = (E) temp;
			Node<E> newNode = new Node<E>(e);
			newNode.pre = pred;
			if (pred == null) {
				head = newNode;
			}
			else {
				pred.next = newNode;
			}
			pred = newNode;
		}
		if (succ == null)
			tail = pred;
		else {
			pred.next = succ;
			succ.pre = pred;
		}
		size += arr.length;
		modCount++;
		return true;
	}
	
	/* linkFirst: insert element E into the head of this list
	 * time complexity is O(1)
	 * */
	private void linkFirst(E e) {
		final Node<E> newNode = new Node<E>(e);
		final Node<E> temp = head;
		head = newNode;
		if (tail == null) {
			tail = newNode; 
		}
		else {
			temp.pre = newNode;
			newNode.next = temp;
		}
		size++;
		modCount++;
	}
	
	/* linkLast: insert an element into the last of list
	 * time complexity: O(1)
	 * */
	private void linkLast(E e) {
		final Node<E> newNode = new Node<E>(e);
		final Node<E> temp = tail;
		tail = newNode;
		if (head == null) {
			head = newNode;
		}
		else {
			temp.next = newNode;
			newNode.pre = temp;
		}
		size++;
		modCount++;
	}
	
	void linkBefore (E e, Node<E> succ) {
		final Node<E> pred = succ.pre;
		final Node<E> newNode = new Node<E>(e);
		newNode.pre = pred;
		newNode.next = succ;
		if (pred == null) {
			head = newNode;
		}
		else {
			pred.next = newNode;
		}
		size++;
		modCount++;
	}
	/*delete operation*/
	public boolean remove(Object o) {
		if (o == null) {
			for (Node<E> x = head; x != null; x = x.next) {
				if (x.val == null) {
					unlink(x);
					return true;
				}
			}
		}
		else {
			for(Node<E> x = head; x != null; x = x.next) {
				if (o.equals(x.val) || o == x) {
					unlink(x);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean removeAll(Collection<?> c) {
		Object[] arr = c.toArray();
		if (arr.length == 0)
			return true;
		for (Object o: c) {
			remove(o);
		}
		return true;
	}
	@Override
	public void clear() {
		for (Node<E> x = head; x != null;) {
			Node<E> next = x.next;
			x.val = null;
			x.pre = null;
			x.next = null;
			x = next;
		}
		head = tail = null;
		size = 0;
		modCount++;
		
	}
	/* unlinkFirst: delete the first element with e in this list
	 * time complexity: O(1)
	 * */
	public E unlinkFirst(Node<E> f) {
		final E val = f.val;
		final Node<E> after = f.next;
		f.next = null;
		f.val = null;
		head = after;
		if (after == null) {
			tail = null;
		}
		else {
			after.pre = null;
		}
		size--;
		modCount++;
		return val;
	}
	/* unlink last: delete the last element from the list
	 * time complexity: O(1)
	 * */
	public E unlinkLast(Node<E> l) {
		final E val = l.val;
		final Node<E> before = l.pre;
		l.pre = null;
		l.val = null;
		tail = before;
		if (before == null) {
			head = null;
		}
		else {
			before.next = null;
		}
		size--;
		modCount++;
		return val;
	}
	/* unlink a given node in the middle of the list
	 * time complexity: O(1)
	 * */
	public E unlink(Node<E> curNode) {
		final E val = curNode.val;
		final Node<E> after = curNode.next;
		final Node<E> before = curNode.pre;
		curNode.val = null;
		if (after == null) {
			tail = before;
		}
		else {
			after.pre = before;
			curNode.pre = null;
		}
		
		if (before == null) {
			head = after;
		}
		else {
			before.next = after;
			curNode.next = null;
		}
		size--;
		modCount++;
		return val;
 	}
	
	public E get(int index) {
		assert checkIndex(index);
		return node(index).val;
	}
	
	public E set(int index, E newVal) {
		assert checkIndex(index);
		Node<E> x = node(index);
		E oldVal = x.val;
		x.val = newVal;
		return oldVal;
	}
	
	public Node<E> node(int index) {
		// index is at the first-half list
		if (index < (size >> 1)) {
			Node<E> x = head;
			for (int i = 1; i <= index; i++) {
				x = x.next;
			}
			return x;
		}
		else {
			Node<E> x = tail;
			for (int i = 1; i <= index; i++) {
				x = x.pre;
			}
			return x;
		}
	}
	
	public boolean checkIndex(int index) {
		if (index >= 0 && index < size)
			return true;
		return false;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ListIterator<E> listIterator(int index) {
		assert checkIndex(index);
		return new ListItr(index);
	}
	
	@SuppressWarnings("hiding")
	private class ListItr<E> implements ListIterator<E> {
		private Node<E> curNode;
		private Node<E> nextNode;
		private int nextIndex;
		private int expectedModCount = modCount;
		
		@SuppressWarnings("unchecked")
		public ListItr(int index) {
			assert checkIndex(index);
			curNode = null;
			nextIndex = index;
			nextNode = (Node<E>) ((nextIndex == size) ? null : node(nextIndex));
		}
		public boolean hasNext() {
			return nextIndex < size;
		}

		@Override
		public E next() {
			checkForComodification();
			if (!hasNext())
				throw new NoSuchElementException();
			curNode = nextNode;
			nextNode = nextNode.next;
			nextIndex++;
			return curNode.val;
		}

		@Override
		public boolean hasPrevious() {
			return nextIndex > 0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E previous() {
			checkForComodification();
			if (!hasPrevious())
				throw new NoSuchElementException();
			nextIndex--;
			curNode = nextNode = (Node<E>) ((nextNode == null) ? tail : nextNode.pre);
			return curNode.val;
		}

		@Override
		public int nextIndex() {
			// TODO Auto-generated method stub
			return nextIndex;
		}

		@Override
		public int previousIndex() {
			// TODO Auto-generated method stub
			return nextIndex - 1;
		}

		@Override
		public void remove() {
			checkForComodification();
			if (curNode == null)
				return;
			Node<E> temp = curNode.next;
			unlink(curNode);
			if (nextNode == curNode) {
				nextNode = temp;
			}
			else {
				nextIndex--;
			}
			curNode = null;
			expectedModCount = modCount;
			
		}

		@Override
		public void set(E e) {
			// TODO Auto-generated method stub
			if (curNode == null)
				throw new NoSuchElementException();
			checkForComodification();
			curNode.val = e;
			
		}

		@Override
		public void add(E e) {
			// TODO Auto-generated method stub
			checkForComodification();
			curNode = null;
			if (nextNode == null) {
				linkLast(e);
			}
			else {
				linkBefore(e, nextNode);
			}
			nextIndex++;
			expectedModCount = modCount;
			
		}
		
		private void checkForComodification() {
			if (expectedModCount != modCount)
				throw new ConcurrentModificationException();
		}
		
	}	

	private static class Node<E> extends Object{
		E val;
		Node<E> pre;
		Node<E> next;
		
		public Node(E val) {
			this.val = val;
			pre = null;
			next = null;
		}
	}
	
}




