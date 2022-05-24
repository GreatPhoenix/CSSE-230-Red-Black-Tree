import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class RedBlackTree<T extends Comparable<? super T>> implements Iterable<RedBlackTree.BinaryNode> {
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
				boolean sizeValue = root.insertHelper(root, value, root, root);

				if (sizeValue == true) {
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
				boolean sizeValue = root.insertHelper(root, value, root, root);
				if (sizeValue == true) {
					this.size += 1;
				}
				return sizeValue;
				// return insertHelper(root, value);
			}

		}
		colorCheak(this.root);
		return false;
	}

	private void rightRotationHandler(BinaryNode currentNode) {
		BinaryNode savedRightLeftChild = null;
		BinaryNode heldRightChild = currentNode.rightChild;
		BinaryNode heldLeftChild = currentNode.leftChild;
		T savedElement = currentNode.element;

		// if (currentNode.rightChild.leftChild != null &&
		// currentNode.rightChild.rightChild != null &&
		// currentNode.rightChild.leftChild.leftChild != null){
		// if (currentNode.rightChild.leftChild != null &&
		// currentNode.rightChild.rightChild != null){
		// leftRotationHandler(currentNode);
		// heldRightChild = currentNode.rightChild;
		// heldLeftChild = currentNode.leftChild;
		// savedElement = currentNode.element;
		// return;
		// }

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
		if (currentNode.leftChild != null) {

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

	private boolean colorCheak(BinaryNode currentNode) {
		if (currentNode.nodeColor == Color.BLACK) {
			if (currentNode.rightChild != null && currentNode.leftChild != null) {
				if (currentNode.rightChild.nodeColor == Color.RED && currentNode.leftChild.nodeColor == Color.RED) {
					currentNode.nodeColor = Color.RED;
					currentNode.rightChild.nodeColor = Color.BLACK;
					currentNode.leftChild.nodeColor = Color.BLACK;
					if (this.root.nodeColor == Color.RED) {
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
				if (currentNode.rightChild.rightChild != null) {
					if (currentNode.rightChild.rightChild.nodeColor == Color.RED) {
						rightRotationHandler(currentNode);
					}
				}
				if (currentNode.rightChild.leftChild != null) {
					if (currentNode.rightChild.leftChild.nodeColor == Color.RED) {
						leftRotationHandler(currentNode.rightChild);
						rightRotationHandler(currentNode);
					}
				}
			}
		}
		if (currentNode.leftChild != null) {

			if (currentNode.leftChild.nodeColor == Color.RED) {
				if (currentNode.leftChild.leftChild != null) {
					if (currentNode.leftChild.leftChild.nodeColor == Color.RED) {
						leftRotationHandler(currentNode);
					}
				}
				if (currentNode.leftChild.rightChild != null) {
					if (currentNode.leftChild.rightChild.nodeColor == Color.RED) {
						rightRotationHandler(currentNode.leftChild);
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

			RedBlackTree.this.remove(itemToPop.getElement());

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
			RedBlackTree.this.remove(this.itemPop.element);
			this.lazyStack.clear();

			this.lazyStack.push(RedBlackTree.this.root);

			this.nextRun = false;

		}

	}

	public class BinaryNode {
		protected T element;
		protected BinaryNode leftChild;
		protected BinaryNode rightChild;
		protected Color nodeColor;
		protected char removeLetter;

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

		private boolean insertHelper(BinaryNode currentNode, T value, BinaryNode parrent, BinaryNode grandParent) {
			if (colorCheak(currentNode)) {
				BalencedHandler(grandParent);
			}

			BalencedHandler(currentNode);

			if (value.compareTo(currentNode.element) < 0) {
				if (currentNode.leftChild == null) {
					currentNode.leftChild = (new BinaryNode(value));
					BalencedHandler(parrent);

					return true;
				}

				BalencedHandler(parrent);
				// if (currentNode == parrent){
				// return insertHelper(currentNode.leftChild,
				// value,currentNode,currentNode);
				// }
				//
				return insertHelper(currentNode.leftChild, value, currentNode, parrent);

			}

			else if (value.compareTo(currentNode.element) > 0) {
				if (currentNode.rightChild == null) {
					currentNode.rightChild = new BinaryNode(value);
					BalencedHandler(parrent);
					return true;

				}

				BalencedHandler(parrent);
				boolean FinalAnswer = insertHelper(currentNode.rightChild, value, currentNode, parrent);

				return FinalAnswer;

			} else if (value.compareTo(currentNode.element) == 0) {
				BalencedHandler(parrent);
				return false;
			}

			return false;

		}

		private boolean step2(T element) {
			if (this.leftChild != null) {
				if (this.leftChild.removeLetter == 'X') {
					if (this.leftChild.leftChild != null && this.leftChild.rightChild != null) {
						if (this.leftChild.leftChild.nodeColor == Color.BLACK
								&& this.leftChild.rightChild.nodeColor == Color.BLACK) {
							return step2a(element);
						}

						if (this.leftChild.leftChild.nodeColor == Color.RED
								|| this.leftChild.rightChild.nodeColor == Color.RED) {
							return step2b(element);
						}
					}
					if (this.leftChild.rightChild == null && this.leftChild.leftChild == null) {
						return step2a(element);
					}
				}
			}
			if (this.rightChild != null) {
				if (this.rightChild.removeLetter == 'X') {
					if (this.rightChild.rightChild != null && this.rightChild.leftChild != null) {
						if (this.rightChild.rightChild.nodeColor == Color.BLACK
								&& this.rightChild.leftChild.nodeColor == Color.BLACK) {
							return step2a(element);
						}
						if (this.rightChild.leftChild.nodeColor == Color.RED
								|| this.rightChild.rightChild.nodeColor == Color.RED) {
							return step2b(element);
						}
					}
					if (this.rightChild.rightChild == null && this.rightChild.leftChild == null) {
						return step2a(element);
					}
				}
			}

			return false;

		}

		private boolean step2a(T element) {

			if (this.leftChild.removeLetter == 'T') {
				if (this.leftChild.leftChild != null && this.leftChild.leftChild.nodeColor == Color.BLACK) {
					return step2a1(element);
				}
				if (this.leftChild.rightChild == null && this.leftChild.leftChild == null) {
					return step2a1(element);
				}
				if (this.leftChild.leftChild != null) {
					if (this.leftChild.rightChild == null && this.leftChild.leftChild.nodeColor == Color.BLACK) {
						return step2a1(element);
					}
				}
				if (this.leftChild.rightChild != null) {
					if (this.leftChild.leftChild == null && this.leftChild.rightChild.nodeColor == Color.BLACK) {
						return step2a1(element);
					}
				}

				if (this.leftChild.rightChild != null && this.leftChild.rightChild.nodeColor == Color.BLACK) {
					return step2a1(element);
				}
				if (this.leftChild.rightChild.nodeColor == Color.BLACK
						&& this.leftChild.leftChild.nodeColor == Color.BLACK) {
					return step2a1(element);
				}

				if (this.leftChild.leftChild == null) {
					if (this.leftChild.rightChild.nodeColor == Color.RED) {
						return step2a2(element);
					}

				}
				if (this.leftChild.rightChild == null) {
					if (this.leftChild.leftChild.nodeColor == Color.RED) {
						return step2a3(element);
					}
				}

				if (this.leftChild.rightChild.nodeColor == Color.RED
						&& this.leftChild.leftChild.nodeColor == Color.RED) {
					return step2a4(element);
				}

			}

			if (this.rightChild.removeLetter == 'T') {
				if (this.rightChild.rightChild == null && this.rightChild.leftChild == null) {
					return step2a1(element);
				}
				if (this.rightChild.leftChild != null) {
					if (this.rightChild.rightChild == null && this.rightChild.leftChild.nodeColor == Color.BLACK) {
						return step2a1(element);
					}
				}
				if (this.rightChild.rightChild != null) {
					if (this.rightChild.leftChild == null && this.rightChild.rightChild.nodeColor == Color.BLACK) {
						return step2a1(element);
					}
				}
				if (this.rightChild.leftChild != null && this.rightChild.rightChild != null) {
					if (this.rightChild.rightChild.nodeColor == Color.BLACK
							&& this.rightChild.leftChild.nodeColor == Color.BLACK) {
						return step2a1(element);
					}

					if (this.rightChild.rightChild.nodeColor == Color.RED
							&& this.rightChild.leftChild.nodeColor == Color.RED) {
						return step2a4(element);
					}
				}
				if (this.rightChild.rightChild != null) {

					if (this.rightChild.rightChild.nodeColor == Color.RED) {
						return step2a3(element);
					}
				}
				if (this.rightChild.leftChild.nodeColor == Color.RED) {
					return step2a2(element);
				}
			}

			return false;
		}

		private boolean step2a1(T element) {
			this.nodeColor = Color.BLACK;
			this.rightChild.nodeColor = Color.RED;
			this.leftChild.nodeColor = Color.RED;

			if (this.leftChild.removeLetter == 'X') {
				if (this.leftChild.element.compareTo(element) == 0) {
					return step3(element, this);
				}

				this.leftChild.removeLetter = 'P';
				if (this.leftChild.leftChild == null && this.leftChild.rightChild == null){
					return false;
				}
				moveDown(this.leftChild, element);

				return this.leftChild.step2(element);

			}
			if (this.rightChild.removeLetter == 'X') {
				if (this.rightChild.element.compareTo(element) == 0) {
					return step3(element, this);
				}

				this.rightChild.removeLetter = 'P';
				if (this.rightChild.rightChild == null && this.rightChild.leftChild == null){
					return false;
				}
				moveDown(this.rightChild, element);
				return this.rightChild.step2(element);

			}

			return false;
		}

		private boolean step2a2(T element) {
			if (this.rightChild.removeLetter == 'X') {
				leftRotationHandler(this);
				rightRotationHandler(this);
				T savedElement = this.element;
				BinaryNode aRightNode = this.rightChild;
				this.element = this.leftChild.element;
				this.rightChild = new BinaryNode(savedElement);
				this.rightChild.rightChild = aRightNode;
				this.leftChild = this.leftChild.leftChild;

				this.rightChild.nodeColor = Color.BLACK;
				this.leftChild.nodeColor = Color.BLACK;
				this.rightChild.rightChild.nodeColor = Color.RED;
				if (this.rightChild.rightChild.element.compareTo(element) == 0) {
					return step3(element, this.rightChild);
				}
				return this.rightChild.step2(element);
			}
			if (this.leftChild.removeLetter == 'X') {
				rightRotationHandler(this);
				leftRotationHandler(this);

				T savedElement = this.element;
				BinaryNode aLeftNode = this.leftChild;
				this.element = this.rightChild.element;
				this.leftChild = new BinaryNode(savedElement);
				this.leftChild.leftChild = aLeftNode;
				this.rightChild = this.rightChild.rightChild;

				this.rightChild.nodeColor = Color.BLACK;
				this.leftChild.nodeColor = Color.BLACK;
				this.leftChild.leftChild.nodeColor = Color.RED;
				if (this.leftChild.leftChild.element.compareTo(element) == 0) {
					return step3(element, this.leftChild);
				}
				return this.leftChild.step2(element);
			}

			return false;
		}

		private boolean step2a3(T element) {
			if (this.leftChild.removeLetter == 'X') {
				leftRotationHandler(this);
				this.leftChild.leftChild.nodeColor = Color.RED;
				this.leftChild.nodeColor = Color.BLACK;
				this.nodeColor = Color.RED;
				this.rightChild.nodeColor = Color.BLACK;
				if (this.leftChild.leftChild.removeLetter == 'X') {
					return step3(element, this.leftChild);
				}
				return this.leftChild.step2(element);

			}
			if (this.rightChild.removeLetter == 'X') {
				rightRotationHandler(this);
				this.rightChild.rightChild.nodeColor = Color.RED;
				this.rightChild.nodeColor = Color.BLACK;
				this.nodeColor = Color.RED;
				this.leftChild.nodeColor = Color.BLACK;
				if (this.rightChild.rightChild.removeLetter == 'X') {
					return step3(element, this.rightChild);
				}
				return this.rightChild.step2(element);
			}

			return false;
		}

		private boolean step2a4(T element) {
			return step2a3(element);
		}

		private boolean step2b(T element) {
			if (this.rightChild.removeLetter == 'X') {
				if (this.rightChild.element.compareTo(element) == 0) {
					return step3(element, this);
				}
				moveDown(this.rightChild, element);

				if (this.rightChild.nodeColor == Color.RED) {
					return this.rightChild.step2b1(element);
				}
				return this.rightChild.step2b2(element);

			}
			if (this.leftChild.removeLetter == 'X') {
				if (this.leftChild.element.compareTo(element) == 0) {
					return step3(element, this);
				}
				moveDown(this.leftChild, element);

				if (this.leftChild.nodeColor == Color.RED) {
					return this.leftChild.step2b1(element);
				}
				return this.leftChild.step2b2(element);

			}
			
			if (this.element.compareTo(element) > 0){
				this.leftChild.removeLetter= 'X';
				this.rightChild.removeLetter = 'T';
				
				if (this.leftChild.nodeColor == Color.RED){
					return step2b1(element);
				}
				if (this.leftChild.nodeColor == Color.BLACK){
					return step2b2(element);
				}
				
			}
			if (this.element.compareTo(element) < 0){
				this.rightChild.removeLetter = 'X';
				this.leftChild.removeLetter = 'T';
				if (this.rightChild.nodeColor == Color.RED){
					return step2b1(element);
				}
				if (this.rightChild.nodeColor == Color.BLACK){
					return step2b2(element);
				}
			}
			
			
			return false;
		}

		private boolean step2b1(T element) {
			if (this.leftChild.removeLetter == 'X') {
				if (this.leftChild.element.compareTo(element) == 0) {
					return step3(element, this);
				}
				moveDown(this.leftChild, element);
				return this.leftChild.step2(element);
			}
			if (this.rightChild.removeLetter == 'X') {
				if (this.rightChild.element.compareTo(element) == 0) {
					return step3(element, this);
				}
				moveDown(this.rightChild, element);
				return this.rightChild.step2(element);

			}
			return false;
		}

		private boolean step2b2(T element) {
			if (this.leftChild.removeLetter == 'X') {
				leftRotationHandler(this);
				this.nodeColor = Color.BLACK;
				this.leftChild.nodeColor = Color.RED;
				return this.leftChild.step2(element);
			}
			if (this.rightChild.removeLetter == 'X') {
				rightRotationHandler(this);
				this.nodeColor = Color.BLACK;
				this.rightChild.nodeColor = Color.RED;

				return this.rightChild.step2(element);
			}
			return false;
		}

		private boolean step3(T element, BinaryNode previousNode) {
			if (previousNode.leftChild != null) {
				if (previousNode.leftChild.leftChild != null && previousNode.leftChild.rightChild != null) {
					BinaryNode v = step3LargestRemoveHelper(previousNode.leftChild.leftChild);
					BinaryNode x = previousNode.leftChild;
					if (previousNode.leftChild.nodeColor == Color.RED){
						previousNode.leftChild = v;
						previousNode.leftChild.leftChild = x;
						return previousNode.leftChild.step2(element);
					}
				}
			}
			if (previousNode.rightChild != null) {
				if (previousNode.rightChild.leftChild != null && previousNode.rightChild.rightChild != null) {
					BinaryNode v = step3LargestRemoveHelper(previousNode.rightChild.leftChild);
					BinaryNode x = previousNode.leftChild;
					if (previousNode.rightChild.nodeColor == Color.RED){
						previousNode.rightChild = v;
						previousNode.rightChild.leftChild = x;
						return previousNode.rightChild.step2(element);
					}
				}
				
			}

			if (previousNode.rightChild != null) {
				if (previousNode.rightChild.removeLetter == 'X') {
					previousNode.rightChild = null;
					return true;
				}
			}
			if (previousNode.leftChild != null) {
				if (previousNode.leftChild.removeLetter == 'X') {
					previousNode.leftChild = null;
					return true;
				}
			}
			return false;
		}
		
		private BinaryNode step3LargestRemoveHelper(BinaryNode startingPoint){
			if (startingPoint.rightChild != null){
				return step3LargestRemoveHelper(startingPoint.rightChild);
			}
			
			return startingPoint;
		}

		private void moveDown(BinaryNode nodeToBeP, T element) {
			if (nodeToBeP.element.compareTo(element) > 0) {
				nodeToBeP.removeLetter = 'P';
				nodeToBeP.leftChild.removeLetter = 'X';
				nodeToBeP.rightChild.removeLetter = 'T';
			}
			if (nodeToBeP.element.compareTo(element) < 0) {
				nodeToBeP.removeLetter = 'P';
				nodeToBeP.rightChild.removeLetter = 'X';
				nodeToBeP.leftChild.removeLetter = 'T';
			}
		}

	}

	@Override
	public Iterator<RedBlackTree.BinaryNode> iterator() {
		// TODO Auto-generated method stub.
		return new TreeItorator(this.root);
	}

	public boolean remove(T element) {

		if (this.root == null) {
			return false;
		}
		if (this.root.rightChild != null & this.root.leftChild != null) {
			if (this.root.rightChild.nodeColor == Color.BLACK && this.root.leftChild.nodeColor == Color.BLACK) {
				this.root.nodeColor = Color.RED;
				if (root.element.compareTo(element) == 0) {
					return root.step3(element, this.root);
				}

				this.root.removeLetter = 'P';
				if (this.root.element.compareTo(element) > 0) {
					this.root.leftChild.removeLetter = 'X';
					this.root.rightChild.removeLetter = 'T';
					return root.step2(element);
				}
				if (this.root.element.compareTo(element) < 0) {
					this.root.rightChild.removeLetter = 'X';
					this.root.leftChild.removeLetter = 'T';
					return root.step2(element);
				}
			}
		}
		this.root.removeLetter = 'X';
		
		return root.step2b(element);

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
