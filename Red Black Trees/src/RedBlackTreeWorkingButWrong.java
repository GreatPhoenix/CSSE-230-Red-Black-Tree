import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class RedBlackTreeWorkingButWrong<T extends Comparable<? super T>> implements Iterable<RedBlackTree.BinaryNode> {
	public enum Color {
		RED, BLACK
	}

	public BinaryNode root;
	private int size;
	private int numberOfRotations;

	public boolean insert(T value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		if (this.size == 0) {
			this.root = new BinaryNode(value);
			this.size += 1;
			this.root.nodeColor = Color.BLACK;
			return true;
		}
		if (value.compareTo(root.element) == 1) {
			if (root.rightChild == null) {
				root.rightChild = (new BinaryNode(value));
				this.size += 1;
				return true;
			} else {
				boolean sizeValue = root.insert(root, value);
				
				if (sizeValue == true){
					this.size += 1;
				}
				return sizeValue;
			}
		} else if (value.compareTo(root.element) == -1) {
			if (root.leftChild == null) {
				root.leftChild = (new BinaryNode(value));
				this.size += 1;
				return true;
			} else {
				boolean sizeValue = root.insert(root, value);
				if (sizeValue == true){
					this.size += 1;
				}
				return sizeValue;
				//return insertHelper(root, value);
			}

		}
		colorCheak(this.root);
		return false;
	}

	private boolean insertHelper(BinaryNode currentNode, T value) {
		colorCheak(currentNode);
		
		
		if (value.compareTo(currentNode.element) == -1) {
			if (currentNode.leftChild == null) {
				currentNode.leftChild = (new BinaryNode(value));
				this.size += 1;
				return true;
			}
			else {
				boolean FinalAnswer = insertHelper(currentNode.leftChild, value);
				BalencedHandler(currentNode);
				return FinalAnswer;
			}
		}

		else if (value.compareTo(currentNode.element) == 1) {
			if (currentNode.rightChild == null) {
				currentNode.rightChild = new BinaryNode(value);
				BalencedHandler(currentNode);
				this.size += 1;
				return true;
			}
			
			else {

				boolean FinalAnswer = insertHelper(currentNode.rightChild, value);
				BalencedHandler(currentNode);
				return FinalAnswer;
			}

		}
		else if (value.compareTo(currentNode.element)== 0){
			return false;
		}
		
		
		return false;

	}

	private void rightRotationHandler(BinaryNode currentNode) {
		BinaryNode savedRightLeftChild = null;
		BinaryNode heldRightChild = currentNode.rightChild;
		BinaryNode heldLeftChild = currentNode.leftChild;
		T savedElement = currentNode.element;

		if (currentNode.rightChild.leftChild != null && currentNode.rightChild.rightChild == null) {

			currentNode.rightChild.rightChild = new BinaryNode(currentNode.rightChild.leftChild.element);
			currentNode.rightChild.rightChild.element = currentNode.rightChild.element;
			currentNode.rightChild.element = currentNode.rightChild.leftChild.element;

			currentNode.rightChild.leftChild = null;
			this.numberOfRotations += 1;

			heldRightChild = currentNode.rightChild;
			heldLeftChild = currentNode.leftChild;
			savedElement = currentNode.element;
		}

		if (currentNode.rightChild.leftChild != null) {
			savedRightLeftChild = currentNode.rightChild.leftChild;

		}
		currentNode.element = currentNode.rightChild.element;
		currentNode.rightChild = currentNode.rightChild.rightChild;
		currentNode.leftChild = new BinaryNode(savedElement);
		currentNode.leftChild.leftChild = heldLeftChild;
		if (savedRightLeftChild != null) {
			currentNode.leftChild.rightChild = savedRightLeftChild;
		}

		this.numberOfRotations += 1;

	}

	private void leftRotationHandler(BinaryNode currentNode) {
		BinaryNode savedLeftRightChild = null;
		BinaryNode heldRightChild = currentNode.rightChild;
		BinaryNode heldLeftChild = currentNode.leftChild;
		T savedElement = currentNode.element;

		if (currentNode.leftChild.rightChild != null && currentNode.leftChild.leftChild == null) {

			currentNode.leftChild.leftChild = new BinaryNode(currentNode.leftChild.rightChild.element);
			currentNode.leftChild.leftChild.element = currentNode.leftChild.element;
			currentNode.leftChild.element = currentNode.leftChild.rightChild.element;

			currentNode.leftChild.rightChild = null;
			this.numberOfRotations += 1;

			heldRightChild = currentNode.rightChild;
			heldLeftChild = currentNode.leftChild;
			savedElement = currentNode.element;
		}

		if (currentNode.leftChild.rightChild != null) {
			savedLeftRightChild = currentNode.leftChild.rightChild;

		}
		currentNode.element = currentNode.leftChild.element;
		currentNode.leftChild = currentNode.leftChild.leftChild;
		currentNode.rightChild = new BinaryNode(savedElement);
		currentNode.rightChild.rightChild = heldRightChild;

		if (savedLeftRightChild != null) {
			currentNode.rightChild.leftChild = savedLeftRightChild;
		}
		this.numberOfRotations += 1;
	}

	
	public ArrayList<Integer> toArrayList() {
		ArrayList<Integer> items = new ArrayList();
		InOrderTreeItorator tree = new InOrderTreeItorator(this.root);
		while (tree.hasNext()) {
			items.add((Integer) tree.next());
		}

		return items;
	}

	public Object[] toArray() {
		TreeItorator tree = new TreeItorator(this.root);
		Object[] items = new Object[this.size];
		for (int i = 0; i < this.size; i++) {
			items[i] = tree.next();
		}

		return items;
	}

	public String toString() {
		ArrayList<Integer> treeArray = toArrayList();

		return treeArray.toString();

	}
	private boolean colorCheak(BinaryNode currentNode){
		if (currentNode.nodeColor == Color.BLACK) {
			if (currentNode.rightChild != null && currentNode.leftChild != null){
				if (currentNode.rightChild.nodeColor == Color.RED && currentNode.leftChild.nodeColor == Color.RED){
					currentNode.nodeColor = Color.RED;
					currentNode.rightChild.nodeColor = Color.BLACK;
					currentNode.leftChild.nodeColor = Color.BLACK;
					if (this.root.nodeColor == Color.RED){
						currentNode.nodeColor = Color.BLACK;
						
						
					}
					return true;
				}
			}
		}
		return false;
	}

	private boolean BalencedHandler(BinaryNode currentNode) {

		if (currentNode.nodeColor == Color.RED) {
			
			return false;
		}

		if (currentNode.rightChild != null) {

			if (currentNode.rightChild.nodeColor == Color.RED) {
				if(currentNode.rightChild.rightChild != null){
					if (currentNode.rightChild.rightChild.nodeColor == Color.RED){
						rightRotationHandler(currentNode);
					}
				}
				if (currentNode.rightChild.leftChild != null){
					if (currentNode.rightChild.leftChild.nodeColor == Color.RED){
						rightRotationHandler(currentNode);
					}
				}
			}
		}
		if (currentNode.leftChild != null) {
			
			if (currentNode.leftChild.nodeColor == Color.RED) {
				if (currentNode.leftChild.leftChild != null){
					if(currentNode.leftChild.leftChild.nodeColor == Color.RED){
						leftRotationHandler(currentNode);
					}
				}
				if (currentNode.leftChild.rightChild != null){
					if (currentNode.leftChild.rightChild.nodeColor == Color.RED){
						leftRotationHandler(currentNode);
					}
				}

				
			}
		}
		return true;

	}

	public class InOrderTreeItorator<T> implements Iterator<T> {
		Object itemToRemove;
		boolean needToRemove = false;
		boolean nextRun = false;
		BinaryNode roott;
		BinaryNode itemToPop;
		Stack<BinaryNode> lazyStack = new Stack<BinaryNode>();

		public InOrderTreeItorator(BinaryNode root) {
			this.roott = root;

			while (this.roott != null) {
				if (this.needToRemove) {
					if (this.roott.getElement().equals(this.itemToRemove)) {
						this.needToRemove = false;
						this.roott = roott.getLeftChild();

					}
				}

				lazyStack.push(roott);
				this.roott = roott.getLeftChild();

			}
		}

		@Override
		public boolean hasNext() {
			return !this.lazyStack.isEmpty();
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			nextRun = true;

			itemToPop = lazyStack.pop();
			if (itemToPop.getRightChild() != null) {
				BinaryNode theRightChild = itemToPop.getRightChild();

				while (theRightChild != null) {
					lazyStack.push(theRightChild);
					theRightChild = theRightChild.getLeftChild();

				}
			}

			return (T) itemToPop.getElement();
		}

		public void remove() {
			if (!hasNext()) {
				throw new IllegalStateException();
			}
			if (!nextRun) {
				throw new IllegalStateException();
			}

			RedBlackTreeWorkingButWrong.this.remove(itemToPop.getElement());

			this.nextRun = false;

		}

	}

	public class TreeItorator implements Iterator<RedBlackTree.BinaryNode> {
		Stack<BinaryNode> lazyStack = new Stack<BinaryNode>();
		BinaryNode root;
		boolean isNull;
		boolean nextRun = false;
		BinaryNode itemPop;

		public TreeItorator(BinaryNode root) {

			this.root = root;
			this.lazyStack.push(this.root);
			this.isNull = this.root == null;
		}

		@Override
		public boolean hasNext() {

			return !this.lazyStack.isEmpty() && !this.isNull;
		}

		@Override
		public RedBlackTree.BinaryNode next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			nextRun = true;

			itemPop = lazyStack.pop();

			if (itemPop.rightChild != null) {
				this.lazyStack.push(itemPop.rightChild);

			}
			if (itemPop.leftChild != null) {
				this.lazyStack.push(itemPop.leftChild);
			}

			return (itemPop);

		}

		public void remove() {

			if (!nextRun) {
				throw new IllegalStateException();
			}
			RedBlackTreeWorkingButWrong.this.remove(this.itemPop.element);
			this.lazyStack.clear();

			this.lazyStack.push(RedBlackTreeWorkingButWrong.this.root);

			this.nextRun = false;

		}

	}

	public class BinaryNode {
		protected T element;
		protected BinaryNode leftChild;
		protected BinaryNode rightChild;
		protected Color nodeColor;

		public BinaryNode(T element) {
			this.element = element;
			this.leftChild = null;
			this.rightChild = null;
			this.nodeColor = Color.RED;
		}

		public void setLeftChild(BinaryNode leftChild) {
			this.leftChild = leftChild;
		}

		public void setRightChild(BinaryNode rightChild) {
			this.rightChild = rightChild;
		}

		public BinaryNode getLeftChild() {
			return leftChild;
		}

		public BinaryNode getRightChild() {
			return rightChild;
		}

		public T getElement() {
			return element;
		}

		public int getheight() {

			int lHeight = -1;
			if (leftChild != null) {
				lHeight = leftChild.getheight();
			}

			int rHeight = -1;
			if (rightChild != null) {
				rHeight = rightChild.getheight();
			}

			if (lHeight > rHeight) {
				return lHeight + 1;
			}
			return rHeight + 1;

		}

		public Color getColor() {
			return this.nodeColor;
		}
		
		private boolean insert(BinaryNode currentNode, T value) {
			colorCheak(currentNode);
			
			
			if (value.compareTo(currentNode.element) == -1) {
				if (currentNode.leftChild == null) {
					currentNode.leftChild = (new BinaryNode(value));
					//this.size += 1;
					return true;
				}
				else {
					boolean FinalAnswer = insertHelper(currentNode.leftChild, value);
					BalencedHandler(currentNode);
					return FinalAnswer;
				}
			}

			else if (value.compareTo(currentNode.element) == 1) {
				if (currentNode.rightChild == null) {
					currentNode.rightChild = new BinaryNode(value);
					BalencedHandler(currentNode);
					//this.size += 1;
					return true;
				}
				
				else {

					boolean FinalAnswer = insertHelper(currentNode.rightChild, value);
					BalencedHandler(currentNode);
					return FinalAnswer;
				}

			}
			else if (value.compareTo(currentNode.element)== 0){
				return false;
			}
			
			
			return false;

		}


	}

	@Override
	public Iterator<RedBlackTree.BinaryNode> iterator() {
		// TODO Auto-generated method stub.
		return new TreeItorator(this.root);
	}

	public void remove(T element) {
		// TODO Auto-generated method stub.

	}

	public int getRotationCount() {
		return this.numberOfRotations;
	}

	public Color getColor() {
		return this.root.nodeColor;
	}

	public int height() {
		if (root == null) {
			return -1;
		}
		return root.getheight();

	}
	public int size() {
		return this.size;
	}

}
