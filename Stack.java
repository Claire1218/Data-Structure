package Stack_And_Queue;

public class Stack<E extends Comparable> {
	private final static int DEFAULT_CAPACITY = 5;
	private E[] stack;
	private int top;
	
	//constructor
	@SuppressWarnings("unchecked")
	public Stack() {
		stack = (E[]) new Comparable[DEFAULT_CAPACITY];
		top = -1;
	}
	
	public boolean isEmpty() {
		return top == -1;
	}
	
	/* push method: insert a new item into stack
	* if stack is full, throw new exception with overflow
	* time complexity: O(1) */
	public void push(E newItem) {
		if (top == stack.length) {
			throw new IndexOutOfBoundsException("overflow");
		}
		stack[++top] = newItem;
	}
	
	/* pop method: delete an item from the stack
	 * if stack is empty, throw new exception with underflow
	 * time complexity: O(1)*/
	public E pop() {
		if (isEmpty()) {
			throw new IndexOutOfBoundsException("underflow");
		}
		E tempItem = stack[top];
		top--;
		stack[top + 1] = null;
		return tempItem;
	}
	
	/* peek method: visit the top element in the stack
	 * if stack is empty, throw new exception with underflow
	 * time complexity: O(1)*/
	public E peek() {
		if (isEmpty())
			throw new IndexOutOfBoundsException("underflow");
		return stack[top];
	}
	
	/*test method inside stack class*/
	public static void main(String[] args) {
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		stack.push(5);
//		stack.push(6); // overflow
		
		System.out.println("current pop item is " + stack.pop());
		System.out.println("current pop item is " + stack.pop());
		System.out.println("current pop item is " + stack.pop());
		System.out.println("current pop item is " + stack.pop());
		System.out.println("current pop item is " + stack.pop());
//		System.out.println("current pop item is " + stack.pop()); // underflow
	}
}
