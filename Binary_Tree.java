import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class Binary_Tree<T extends Comparable<T>> {
	private TreeNode<T> root;
	
	
	public Binary_Tree(TreeNode<T> root) {
		this.root = root;
	}
	
	/**********************************************
	 * insertion operation                        *
	 * time complexity: O(n) in worst case
	 * ********************************************/
	public TreeNode<T> insert(T val) {
		return insert(root, val);
	}
	private TreeNode<T> insert(TreeNode<T> node, T val) {
		if (node == null)
			return new TreeNode<T>(val);
		if (node.val.compareTo(val) == 0)
			return node;
		else if (node.val.compareTo(val) > 0) {
			node.left = insert(node.left, val);
		}
		else {
			node.right = insert(node.right, val);
		}
		return node;
		
	}
	
	/**********************************************
	 * search operation                        *
	 * time complexity: O(n)
	 * space complexity O(1)
	 * ********************************************/
	public boolean search(T val) {
		return search(root, val);
	}
	private boolean search(TreeNode<T> node, T val) {
		//reach the end of visiting branch
		if (node == null)
			return false;
		if (node.val.compareTo(val) == 0)
			return true;
		//the current node is less than val --> go to right subtree
		else if (node.val.compareTo(val) < 0)
			return search(node.right, val);
		//the current node is larger than val --> go to left subtree
		else
			return search(node.left, val);
	}
	/**********************************************
	 * Delete operation                        *
	 * reference link: http://www.algolist.net/Data_structures/Binary_search_tree/Removal
	 * ********************************************/
	public void delete(T val) {
		delete(root, val);
	}
	private TreeNode<T> delete(TreeNode<T> node, T val) {
		if (node == null)
			throw new RuntimeException("cannot be deleted");
		if (node.val.compareTo(val) < 0) {
			node.right = delete(node.right, val);
		}
		else if (node.val.compareTo(val) > 0) {
			node.left = delete(node.left, val);
		}
		else {
			//case 1: only one brach that the current node has
			//also includes case 3: the current node is the leaf node;
			if (node.left == null)
				return node.right;
			else if (node.right == null)
				return node.left;
			//case2: two braches that the current node has
			else {
				//get the val from the left most node of the right node of the current node;
				node.val = leftMostVal(node.right);
				//remove the relative smallest node
				node.right = delete(node.right, node.val); 
			}
		}
		return node;	
	}
	private T leftMostVal(TreeNode<T> node) {
		TreeNode<T> temp = node;
		while (temp.left != null) {
			temp = temp.left;
		}
		return temp.val;
	}
	
	/**********************************************
	 * traversal operation                        *
	 * ********************************************/
	// preorder traversal
	public void preorder_traversal(TreeNode<T> node) {
		if (node == null)
			return;
		System.out.print(node.val + " ");
		preorder_traversal(node.left);
		preorder_traversal(node.right);
	}
	
	public void preorder_traversal_iterate(TreeNode<T> node) {
		if (node == null)
			return;
		Stack<TreeNode<T>> stack = new Stack<TreeNode<T>>();
		stack.push(node);
		
		while (!stack.isEmpty()) {
			TreeNode<T> cur = stack.pop();
			System.out.print(cur.val + " ");
			if (cur.right != null) {
				stack.push(cur.right);
			}
			if (cur.left != null) {
				stack.push(cur.left);
			}
		}
	}
	//inorder traversal
	public void inorder_traversal(TreeNode<T> node) {
		if (node == null)
			return;
		inorder_traversal(node.left);
		System.out.print(node.val + " ");
		inorder_traversal(node.right);
	}
	public void inorder_traversal_iterate(TreeNode<T> node) {
		if (node == null)
			return;
		Stack<TreeNode<T>> stack = new Stack<TreeNode<T>>();
		TreeNode<T> pre = node; 
		while (!stack.isEmpty() || pre != null) {
			if (pre != null) {
				stack.push(pre);
				pre = pre.left;
			}
			else {
				TreeNode<T> temp = stack.pop();
				System.out.print(temp.val + " ");
				pre = temp.right;
			}
		}
	}
	
	//postorder traversal
	public void postorder_traversal(TreeNode<T> node) {
		if (node == null)
			return;
		postorder_traversal(node.left);
		postorder_traversal(node.right);
		System.out.print(node.val + " ");
	}
	public void postorder_traversal_iterate(TreeNode<T> node) {
		if (node == null)
			return;
		Stack<TreeNode<T>> stack = new Stack<TreeNode<T>>();
		stack.push(node);
		TreeNode<T> pre = null;
		while (!stack.isEmpty()) {
			TreeNode<T> cur = stack.peek();
			if (pre == null || pre.left == cur || pre.right == cur) {
				if (cur.left != null) {
					stack.push(cur.left);
				}
				else if (cur.right != null) {
					stack.push(cur.right);
				}
			}
			else if (cur.left == pre) {
				if (cur.right != null) {
					stack.push(cur.right);
				}
				else {
					TreeNode<T> temp = stack.pop();
					System.out.print(temp.val + " ");
				}
			}
			else {
				TreeNode<T> temp = stack.pop();
				System.out.print(temp.val + " ");
			}
			pre = cur;
		}
	}
	public TreeNode<T> getRoot() {
		return root;
	}
	public boolean isEmpty() {
		return root == null;
	}
	/**********************************************
	 * tree iterator                            *
	 * ********************************************/
	public Iterator<TreeNode<T>> iterator (){
		return new Itr();
	}
	
	private class Itr implements Iterator<TreeNode<T>> {
		Stack<TreeNode<T>> stack;
		public Itr() {
			stack = new Stack<TreeNode<T>>();
			if (root != null)
				stack.push(root);
		}
		public boolean hasNext() {
			return !stack.isEmpty();
		}
		public TreeNode<T> next() { //by using preorder
			if (!hasNext())
				throw new NoSuchElementException();
			TreeNode<T> cur = stack.peek();
			if (cur.left != null)
				stack.push(cur.left);
			else {
				TreeNode<T> temp = stack.pop();
				while (temp.right == null) {
					if(stack.isEmpty())
						return cur;
					temp = stack.pop();
				}
				stack.push(temp.right);
			}
			return cur;
		}
		public void remove() {}
		
	}
	/**********************************************
	 * main() function                            *
	 * ********************************************/
	public static void main(String[] args) {
		Binary_Tree tree = new Binary_Tree(new TreeNode<Integer>(5));
		tree.insert(3);
		tree.insert(1);
		tree.insert(4);
		tree.insert(9);
		tree.insert(7);
		tree.insert(11);
		
		tree.inorder_traversal(tree.getRoot());
		System.out.println();
		tree.delete(9);
		tree.inorder_traversal(tree.getRoot());
		
	}
}

/**********************************************
 * node class                                *
 * ********************************************/
class TreeNode<T> {
	
	TreeNode<T> left, right;
	T val; 
	public TreeNode(T val) {
		this.val = val;
	}
}
