//package comp2402a8;
import java.util.Arrays;
import java.util.Queue;
import java.util.LinkedList;
/**
 * The TwoFourTree class is an implementation of the 2-4 tree from notes/textbook.
 * The tree will store Strings as values. 
 * It extends the (modified) sorted set interface (for strings).
 * It implements the LevelOrderTraversal interface. 
 */
public class TwoFourTree extends StringSSet implements LevelOrderTraversal{

		/* your class MUST have a zero argument constructor. All testing will 
		   use this ocnstructor to create a 2-3 tree.
   		 */
		int size;
		Node root;

		public TwoFourTree(){
			size = 0;
			root = new Node();
		}
		public int size(){
			return size;
		}

		public String levelOrder(){
			Queue<Node> q = new LinkedList<Node>();
			Queue<Integer> depth = new LinkedList<Integer>();
			String s = "";
			if (root != NIL){
				q.add(root);
				depth.add(0);
			}
			while (!q.isEmpty()) {
				Node u = q.remove();
				int actualDepth = depth.remove();
				for(int i = 0; i < u.children.length; i ++){
					if(u.children[i] != NIL){
						q.add(u.children[i]);
						depth.add(actualDepth+1);
					}
				}
				for(int i = 0; i < u.data.length; i++){
					if(u.data[i] != EMPTY){
						s += u.data[i];
					}
					if(i != u.data.length-1 && u.data[i+1] != EMPTY){
						s += ",";
					}
				}
				if(!q.isEmpty() && actualDepth == depth.peek()){
					s += ":";
				}else{
					if(!q.isEmpty()){
						s+= "|";
					}
				}
			}
			return s;
		}

		public String find(String x){
			Node curNode = root;
			String largest = null;
			while(true){
				if(curNode == NIL){
					return largest;
				}
				for(int i = getNumItems(curNode)-1; i >= 0; i--){
					if(x.equals(curNode.data[i])){
						return curNode.data[i];
					}
					if(x.compareTo(curNode.data[i]) < 0){
						largest = curNode.data[i];
					}
				}
				curNode = curNode.children[findIndex(curNode, x)];
				/*if(curNode.children[0] == NIL){//no leafs
					for(int i = 0; i < getNumItems(curNode); i++){
						if(x.equals(curNode.data[i])){
							return curNode.data[i];
						}else{
							if(x.compareTo(curNode.data[i]) < 0){
								return curNode.data[i];
							}
						}
					}
					return largest;
					//return curNode.data[getNumItems(curNode)-1];
				}else{
					for(int i = 0; i < getNumItems(curNode); i++){
						if(x.equals(curNode.data[i])){
							return curNode.data[i];
						}else{
							if(x.compareTo(curNode.data[i]) < 0){
								for(int j = 3; j >= 0; j--) {
									if (curNode.children[j].data[0] != EMPTY) {
										if (curNode.children[j].data[getNumItems(curNode.children[j])-1].compareTo(x) > 0){
											largest = curNode.children[j].data[getNumItems(curNode.children[j])-1];
											//break;
										}//else{
											//return curNode.data[i];
										//}
									}
								}
							}
						}
					}
					curNode = curNode.children[findIndex(curNode, x)];
				}*/
			}

		}

	public Node findNode(String x){
		Node curNode = root;
		Node largest = NIL;
		while(true){
			if(curNode == NIL){
				return largest;
			}
			for(int i = getNumItems(curNode)-1; i >= 0; i--){
				if(x.equals(curNode.data[i])){
					return curNode;
				}
				if(x.compareTo(curNode.data[i]) < 0){
					largest = curNode;
				}
			}
			curNode = curNode.children[findIndex(curNode, x)];
		}

	}

		public int findIndex(Node node, String x){
			for(int i = 0; i < 3; i++){
				if(node.data[i] != EMPTY && x.compareTo(node.data[i]) < 0){
					return i;
				}
				if(node.data[i] == EMPTY){
					return i;
				}
			}
			return node.data.length;
		}

		public boolean insert(Node node, String x){
			String arr [] = new String[getNumItems(node)+1];
			if(node.data[node.data.length-1] != EMPTY){
				return true;
			}
			for(int i = 0; i < arr.length-1; i++) {
				if (node.data[i] != EMPTY) {
					arr[i] = node.data[i];
				}
			}
			arr[arr.length-1] = x;
			Arrays.sort(arr);
			for(int i = 0; i < arr.length; i++){
				node.data[i] = arr[i];
			}
			return false;
		}

		public void split(Node node, String x, Node child){
			String arr [] = new String[4]; //it's full
			for(int i = 0; i < node.data.length; i ++){
				arr[i] = node.data[i];
			}
			arr[3] = x; //add last element
			Arrays.sort(arr); //sort
			//adding corresponding data to the right nodes
			Node newNode = new Node();
			node.data[0] = arr[0];
			node.data[1] = arr[1];
			node.data[2] = EMPTY;
		//	node.data[3] = EMPTY;
			newNode.data[0] = arr[3];
			if(child != null){
				if(child.data[0].compareTo(node.children[3].data[0]) < 0){
					Node temp = node.children[3];
					node.children[3] = child;
					child = temp;
					sortChildren(node.children);
				}
				newNode.children[0] = child;
				child.parent = newNode;
			}
			/*for(int i = 0; i < node.data.length; i ++){
				System.out.println("NEWNODE: " + newNode.data[i]);
				System.out.println("NODE: " + node.data[i]);
			}*/

			//moving right most child to the newNode if there are more than 1
			if(node.children[1] != NIL){
				int lastChild = 0;
				for(int i = 3; i >= 0; i--){
					if(node.children[i] != NIL){
						lastChild++;
					}
					/*if(node.children[i] != NIL && node.children[i].data[0] != EMPTY){
						newNode.children[0] = node.children[i];
						node.children[i] = NIL;
						break;
					}*/
				}
				//System.out.println("newNode" + node.children[lastChild-1].data[0]);
				for(int j = 0; j < 4; j++){
					if(newNode.children[j] == NIL){
						newNode.children[j] = node.children[lastChild-1];
						node.children[lastChild-1].parent = newNode;
						sortChildren(newNode.children);
						break;
					}
				}
				node.children[lastChild-1] = NIL;
			}

			if(node.parent == NIL){
				Node newParent = new Node();
				root = newParent;
				newParent.data[0] = arr[2];
				node.parent = newParent;
				newNode.parent = newParent;
				newParent.children[0] = node;
				newParent.children[1] = newNode;
				//System.out.println("newNode: " +newNode.data[0]);
			}else{
				newNode.parent = node.parent;
				//System.out.println(node.data[0]);
				//System.out.println("parent " + node.parent.data[0]);
				if(node.parent.children[3] != NIL){//full
					if(insert(node.parent, arr[2])){
						if(node.parent.children[3] == NIL){
							//System.out.println("PPP " + newNode.data[0]);
							//System.out.println("parent1 "  + newNode.parent.data[0]);
							split(node.parent, arr[2], null);
						}else{
							//System.out.println("PP " + newNode.data[0]);
							//System.out.println("parent1 "  + newNode.parent.data[0]);
							split(node.parent, arr[2], newNode);
						}
						/*Node temp = split(node.parent, arr[2]);
						for(int i = 0; i < 4; i++){
							if(temp.children[i] == NIL){
								temp.children[i].data[0] = newNode.data[0];
							}
						}
						System.out.println("P1 " + node.parent.parent.children[1].children[0].data[0]);

						System.out.println("P " + newNode.data[0]);*/
						return;
					}
					//adding new node to parent's child
					for(int i = 0; i < node.parent.children.length; i++){
						if(node.parent.children[i] == NIL){
							node.parent.children[i] = newNode;
							node.parent.children = sortChildren(node.parent.children);
							break;
						}
					}
					return;
				}
				for(int i = 0; i < node.parent.children.length; i++){
					if(node.parent.children[i] == NIL){
						node.parent.children[i] = newNode;
						node.parent.children = sortChildren(node.parent.children);
						break;
					}
				}
				if(insert(node.parent, arr[2])){
					split(node.parent, arr[2], null);
				}
			}
			return;
		}

		public Node[] sortChildren (Node [] node){
			int count = 0;
			for(int i = 0; i < node.length; i++){
				if(node[i].data[0] != EMPTY){
					count++;
				}
			}
			String [] arr = new String[count];
			for(int i = 0; i < arr.length; i++){
				if(node[i].data[0] != EMPTY){
					arr[i] = node[i].data[0];
				}
				if(node[i].data[0] == EMPTY){
					break;
				}
			}
			Arrays.sort(arr);
			Node [] temp = new Node[count];
			for(int i = 0; i < arr.length; i++){
				for(int j = 0; j < arr.length; j++){
					if(arr[i].equals(node[j].data[0])){
						temp[i] = node[j];
					}
				}
			}
			for(int i = 0; i < node.length; i++){
				if(i >= temp.length){
					node[i] = NIL;
				}else{
					node[i] = temp[i];
				}
			}
			return node;
		}

		public boolean add (String x){
			Node curNode = root;
			//System.out.println("adding " + x);
			while(true){
				// checking for duplicates
				for(int i = 0; i < 3; i++) {
					if(curNode.data[i] == EMPTY){
						break;
					}
					if(curNode.data[i].equals(x)) {
						return false;
					}
				}
				//System.out.println("M " + curNode.data[0]);
				//System.out.println("N "+ curNode.parent.data[0]);
				/*for(int i = 0; i < curNode.children.length; i++){
					System.out.println(i + ": " + curNode.children[i]);
				}*/
				if(curNode.children[0] == NIL){ //if it's a leaf
					if(getNumItems(curNode) < 3){ //if it's not full
						//insert the data in order
						insert(curNode, x);
						size++;
						return true;
					}
					else{
						split(curNode, x, null);
						size++;
						return true;
					}
				}
				else{
					//missing something
					curNode = curNode.children[findIndex(curNode, x)];
				}
			}
		}

		public int getNumItems(Node curNode){
			int numItems = 0;
			for(int i = 0; i < curNode.data.length; i++){
				if(curNode.data[i] != EMPTY){
					numItems++;
				}
			}
			return numItems;
		}

		public boolean remove(String x){
			if(find(x) == null)
				return false;
			Node current = findNode(x);
			if(current.children[0] == NIL){//LEAF
				if(current.data[1] != null){//ENOUGH DATA
					System.out.println("heyy");
					delete(current, x);
					return true;
				}
				else{//TRANSFER
					delete(current, x);
					if(getNumItems(current) < 1){//NO ITEMS
						if(getNumItems(getSibling(current)) < 2){
							fuse(current);
						}
						else{
							transfer(current);
						}
					}else{
						return true;
					}
				}
			}
			else if(current == root){//ROOT
				Node temp = root;
				Node n = findPredecessor(root);
				System.out.println("ppredecessor " + n.data[getNumItems(n)-1]);
				root.data[findIndex(root, x)-1] = n.data[getNumItems(n)-1];
				sortData(root);
				n.data[getNumItems(n)-1] = x;
				delete(n, x);
				sortData(n);
				if(getNumItems(n) < 1){//NO ITEMS
					if(getNumItems(getSibling(n)) < 1){
						fuse(n);
					}
					else{
						transfer(n);
					}
				}else{
					return true;
					//transfer(n);
				}
			}
			return true;
		}

		public boolean fuse (Node current){//ONE ITEM IN NODE, AFTER HAVING REMOVED IT
			Node sibling = getSibling(current);
			Node p = current.parent;
			if(p == NIL){//ROOT
				p = getSibling(current);
			}
			System.out.println("current data " + current.data[0]);
			current.data[0] = sibling.data[0];
			sortChildren(p.children);
			p.children[0].data[getNumItems(p.children[0])] = delete(p, p.data[0]);
			if(getNumItems(p) < 1)
				root = p.children[0];
			if(getNumItems(p) > 0){
				return fuse(p);
			}

			return true;

		}

		public boolean transfer (Node current){//MULTIPLE ITEMS in SIBLING
			Node p = current.parent;
			int i;
			for(i = 0; i < 4; i++){
				if(p.children[i] == current){
					break;
				}
			}
			current.data[0] = p.data[i-1];
			sortData(p);
			Node sibling = getSibling(current);
			p.data[getNumItems(p)-1] = sibling.data[getNumItems(sibling)-1];
			delete(sibling, sibling.data[getNumItems(sibling)-1]);
			return true;

		}

		public void sortData(Node node){
			String [] temp = new String[getNumItems(node)];
			int x = 0;
			for(int i = 0; i < node.data.length; i++){
				if(node.data[i] != null){
					temp[x] = node.data[i];
					x++;
				}
			}
			for(int i = 0; i < node.data.length; i++){
				if(i < x)
					node.data[i] = temp[i];
				else
					node.data[i] = null;
			}
		}

		public String delete(Node current, String x){
			for(int i = 0 ; i < getNumItems(current); i++){
				if(current.data[i].equals(x)){
					current.data[i] = null;
					sortData(current);
					/*for(int j = i; j < getNumItems(current)-1; j++){
						current.data[j] = current.data[j+1];
					}
					System.out.println("i: " + i);
					if(i == 0 || i == 2)
						current.data[i] = null;
					else
						current.data[2] = null;*/
					return x;
				}
			}
			return null;
		}

		public Node findPredecessor(Node current){
			current = current.children[0];
			System.out.println("children " + current.data[0]);
			while(current.children[0] != NIL){
				current = current.children[getNumItems(current)];
				System.out.println("children " + current.data[0]);
			}
			return current;
		}

		public Node getSibling(Node current){
			Node parent = current.parent;
			for(int i = 0; i < 4; i++){
				if(parent.children[i] == current){
					if(i == 0){
						if(parent.children[i+1] == NIL){
							return current;
						}
						return parent.children[i+1];
					}
					return parent.children[i-1];
				}
			}
			return current;
		}

		public void clear(){
			size = 0;
			root = new Node();
		}

}
