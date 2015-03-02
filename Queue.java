package Stack_And_Queue;

public class Queue<E extends Comparable> {
	private final static int DEFAULT_CAPACITY = 5;
	private E[] queue;
	private int head, tail;
	
	//constructor
	public Queue() {
		queue = (E[]) new Comparable[DEFAULT_CAPACITY];
		head = tail = 0;
	}
	
	//check whether queue is empty
	public boolean isEmpty() {
		return head == tail;
	}
	//check whether queue is full
	public boolean isFull() {
		return tail + 1 == head;
	}
	
	/* enqueue method: insert a new item into queue
	 * if queue is full, throw a new exception with overflow
	 * time complexity is O(1)
	 * */
	public void enqueue(E newItem) {
		if (isFull())
			throw new IndexOutOfBoundsException("overflow");
		queue[tail] = newItem;
		if (tail == queue.length - 1)
			tail = 0;
		else
			tail += 1;
	}
	
	/* dequeue method: delete an item from queue
	 * if queue is empty, throw a new exception with underflow
	 * time complexity: O(1);
	 * */
	public E dequeue() {
		if (isEmpty())
			throw new IndexOutOfBoundsException("underflow");
		E tempItem = queue[head];
		queue[head] = null;
		if (head == queue.length - 1)
			head = 1;
		else
			head += 1;
		return tempItem;
	}

}
