package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node.  Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {
  /**
   * Default number of elements that may be stored in each node.
   */
  private static final int DEFAULT_NODESIZE = 4;
  
  /**
   * Number of elements that can be stored in each node.
   */
  private final int nodeSize;
  
  /**
   * Dummy node for head.  It should be private but set to public here only  
   * for grading purpose.  In practice, you should always make the head of a 
   * linked list a private instance variable.  
   */
  public Node head;
  
  /**
   * Dummy node for tail.
   */
  private Node tail;
  
  /**
   * Number of elements in the list.
   */
  private int size;
  
  /**
   * Constructs an empty list with the default node size.
   */
  public StoutList() {
    this(DEFAULT_NODESIZE);
  }

  /**
   * Constructs an empty list with the given node size.
   * @param nodeSize number of elements that may be stored in each node, must be 
   *   an even number
   */
  public StoutList(int nodeSize) {
    if (nodeSize <= 0 || nodeSize % 2 != 0) throw new IllegalArgumentException();
    
    // dummy nodes
    head = new Node();
    tail = new Node();
    head.next = tail;
    tail.previous = head;
    this.nodeSize = nodeSize;
  }
  
  /**
   * Constructor for grading only.  Fully implemented. 
   * @param head
   * @param tail
   * @param nodeSize
   * @param size
   */
  public StoutList(Node head, Node tail, int nodeSize, int size) {
	  this.head = head; 
	  this.tail = tail; 
	  this.nodeSize = nodeSize; 
	  this.size = size; 
  }

  @Override
  public int size() {
      return size;
  }

  public class NodeInfo {
      public Node node;
      public int offset;

      public NodeInfo(Node node, int offset) {
          this.node = node;
          this.offset = offset;
      }
  }

  public NodeInfo find(int pos) {
      Node temp = head;
      int currentIndex = 0;
      int numOfNodes = 0;
      while (currentIndex <= pos) {
          temp = temp.next;
          currentIndex = temp.count;
          numOfNodes++;
      }
      int totalBefore = 0;
      Node temp2 = head.next;
      while (temp2 != temp) {
          totalBefore += temp2.count;
          temp2 = temp2.next;
      }
      int offset = pos - totalBefore;
      return new NodeInfo(temp, offset);
  }
  
  @Override
  public boolean add(E item) {
      if (size == 0) {
          Node newNode = new Node(tail, head);
          newNode.previous.next = newNode;
          newNode.next.previous = newNode;
          newNode.addItem(item);
          size++;
      } else {
          Node temp = head.next;
          while (temp.next != tail) {
              temp = temp.next;
          }
          if (temp.data[nodeSize - 1] != null) {
              Node newNode = new Node(tail, temp);
              newNode.previous.next = newNode;
              newNode.next.previous = newNode;
              newNode.addItem(item);
              size++;
          } else {
              temp.addItem(item);
              size++;
          }
      }
      return true;
  }

  public void conditionAdder(NodeInfo t, E item) {
      if (t.node.count == DEFAULT_NODESIZE) {
          Node newNode = new Node();
          newNode.addItem(t.node.data[2]);
          newNode.addItem(t.node.data[3]);
          t.node.removeItem(DEFAULT_NODESIZE / 2);
          t.node.removeItem(DEFAULT_NODESIZE / 2);
          t.node.next.previous = newNode;
          newNode.next = t.node.next;
          t.node.next = newNode;
          newNode.previous = t.node;
          if (t.offset <= DEFAULT_NODESIZE / 2) {
              newNode.previous.addItem(t.offset, item);
          } else {
              newNode.addItem((t.offset - (DEFAULT_NODESIZE / 2)), item);
          }
      } else {
          t.node.addItem(t.offset, item);
      }
  }

  @Override
  public void add(int pos, E item)
  {
      if (pos >= 0 && pos <= size) {
          if (size == 0) {
              Node newNode = new Node();
              head.next = newNode;
              newNode.previous = head;
              newNode.next = tail;
              tail.previous = newNode;
              newNode.addItem(item);
              size++;
          } else {
              NodeInfo t = find(pos);
              if (t.offset == 0) {
                  if (t.node.previous.count < nodeSize && t.node.previous != head) {
                      t.node.previous.addItem(item);
                  } else if (t.node == tail) {
                      Node newNode = new Node();
                      newNode.next = tail;
                      newNode.previous = tail.previous;
                      tail.previous = newNode;
                      newNode.addItem(item);
                  } else {
                      conditionAdder(t, item);
                  }
              } else {
                  conditionAdder(t, item);
              }
              size++;
          }
      }
  }

  @Override
  public E remove(int pos) {
      NodeInfo t = find(pos);
      E tor = t.node.data[t.offset];
      if (t.node.next == tail && t.node.count == 1) {
          t.node.next.previous = t.node.previous;
          t.node.previous.next = tail;
      } else if (t.node.next == tail) {
          t.node.removeItem(t.offset);
      } else if (t.node.count > DEFAULT_NODESIZE / 2) {
          t.node.removeItem(t.offset);
      } else if (t.node.next.count > DEFAULT_NODESIZE / 2) {
          t.node.removeItem(t.offset);
          t.node.addItem(t.node.next.data[0]);
          t.node.next.removeItem(0);
      } else {
          t.node.removeItem(t.offset);
          t.node.addItem(t.node.next.data[0]);
          t.node.addItem(t.node.next.data[1]);
          t.node.next.previous = t.node;
          t.node.next = t.node.next.next;
      }
      size--;
      return tor;
  }

  private class EComparator implements Comparator<E> {
        /**
         *
         * @param left the first object to be compared.
         * @param right the second object to be compared.
         * @return
         */
        @Override
        public int compare(E left, E right) {
            if (left.compareTo(right) > 0) {
                return 1;
            } else if (left.compareTo(right) < 0) {
                return -1;
            } else {
                return 0;
            }
        }
  }

  /**
   * Sort all elements in the stout list in the NON-DECREASING order. You may do the following. 
   * Traverse the list and copy its elements into an array, deleting every visited node along 
   * the way.  Then, sort the array by calling the insertionSort() method.  (Note that sorting 
   * efficiency is not a concern for this project.)  Finally, copy all elements from the array 
   * back to the stout list, creating new nodes for storage. After sorting, all nodes but 
   * (possibly) the last one must be full of elements.  
   *  
   * Comparator<E> must have been implemented for calling insertionSort().    
   */
  public void sort() {
      Node newNode = head;
      E[] arr = (E[]) new Comparable[size];
      int current = 0;
      while (newNode != tail) {
          newNode = newNode.next;
          for (int i = 0; i < newNode.count; i++) {
              arr[current] = newNode.data[i];
              current++;
          }
      }
      while (size != 0) {
          remove(0);
      }
      insertionSort(arr, new EComparator());
      for (int i = 0; i < arr.length; i++) {
          add(arr[i]);
      }
  }
  
  /**
   * Sort all elements in the stout list in the NON-INCREASING order. Call the bubbleSort()
   * method.  After sorting, all but (possibly) the last nodes must be filled with elements.  
   *  
   * Comparable<? super E> must be implemented for calling bubbleSort(). 
   */
  public void sortReverse() {
      Node newNode = head;
      E[] arr = (E[]) new Comparable[size];
      int current = 0;
      while (newNode != tail) {
          newNode = newNode.next;
          for (int i = 0; i < newNode.count; i++) {
              arr[current] = newNode.data[i];
              current++;
          }
      }
      while (size != 0) {
          remove(0);
      }
      bubbleSort(arr);
      for (int i = 0; i < arr.length; i++) {
          add(arr[i]);
      }
  }
  
  @Override
  public Iterator<E> iterator() {
      return new StoutListIterator();
  }

  @Override
  public ListIterator<E> listIterator() {
      return new StoutListIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index) {
      return new StoutListIterator(index);
  }
  
  /**
   * Returns a string representation of this list showing
   * the internal structure of the nodes.
   */
  public String toStringInternal() {
    return toStringInternal(null);
  }

  /**
   * Returns a string representation of this list showing the internal
   * structure of the nodes and the position of the iterator.
   *
   * @param iter
   *            an iterator for this list
   */
  public String toStringInternal(ListIterator<E> iter) {
      int count = 0;
      int position = -1;
      if (iter != null) {
          position = iter.nextIndex();
      }

      StringBuilder sb = new StringBuilder();
      sb.append('[');
      Node current = head.next;
      while (current != tail) {
          sb.append('(');
          E data = current.data[0];
          if (data == null) {
              sb.append("-");
          } else {
              if (position == count) {
                  sb.append("| ");
                  position = -1;
              }
              sb.append(data.toString());
              ++count;
          }

          for (int i = 1; i < nodeSize; ++i) {
             sb.append(", ");
              data = current.data[i];
              if (data == null) {
                  sb.append("-");
              } else {
                  if (position == count) {
                      sb.append("| ");
                      position = -1;
                  }
                  sb.append(data.toString());
                  ++count;

                  // iterator at end
                  if (position == size && count == size) {
                      sb.append(" |");
                      position = -1;
                  }
             }
          }
          sb.append(')');
          current = current.next;
          if (current != tail)
              sb.append(", ");
      }
      sb.append("]");
      return sb.toString();
  }


  /**
   * Node type for this list.  Each node holds a maximum
   * of nodeSize elements in an array.  Empty slots
   * are null.
   */
  private class Node {
    /**
     * Array of actual data elements.
     */
    // Unchecked warning unavoidable.
    public E[] data = (E[]) new Comparable[nodeSize];
    
    /**
     * Link to next node.
     */
    public Node next;
    
    /**
     * Link to previous node;
     */
    public Node previous;
    
    /**
     * Index of the next available offset in this node, also 
     * equal to the number of elements in this node.
     */
    public int count;

    public Node(Node next, Node previous) {
        this.next = next;
        this.previous = previous;
        count = 0;
    }

    public Node() {
        this.next = null;
        this.previous = null;
        count = 0;
    }

    /**
     * Adds an item to this node at the first available offset.
     * Precondition: count < nodeSize
     * @param item element to be added
     */
    void addItem(E item)
    {
      if (count >= nodeSize)
      {
        return;
      }
      data[count++] = item;
      //useful for debugging
      //      System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
    }
  
    /**
     * Adds an item to this node at the indicated offset, shifting
     * elements to the right as necessary.
     * 
     * Precondition: count < nodeSize
     * @param offset array index at which to put the new element
     * @param item element to be added
     */
    void addItem(int offset, E item) {
      if (count >= nodeSize)
      {
    	  return;
      }
      for (int i = count - 1; i >= offset; --i)
      {
        data[i + 1] = data[i];
      }
      ++count;
      data[offset] = item;
      //useful for debugging 
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
    }

    /**
     * Deletes an element from this node at the indicated offset, 
     * shifting elements left as necessary.
     * Precondition: 0 <= offset < count
     * @param offset
     */
    void removeItem(int offset) {
      E item = data[offset];
      for (int i = offset + 1; i < nodeSize; ++i)
      {
        data[i - 1] = data[i];
      }
      data[count - 1] = null;
      --count;
    }    
  }
 
  private class StoutListIterator implements ListIterator<E> {
	// constants you possibly use ...   
	  
	// instance variables ...

      public Node cursor;
      public int lastJumped;
      public int pos;
      public int nodePos;
      public static final int PREV = 1;
      public static final int NEXT = 0;
	  
    /**
     * Default constructor 
     */
    public StoutListIterator() {
        this.cursor = head.next;
        this.pos = 0;
        this.lastJumped = -1;
        this.nodePos = 0;
    }

    /**
     * Constructor finds node at a given position.
     * @param pos
     */
    public StoutListIterator(int pos) {
        int counter = 0;
        if (size != 0) {
            if (pos >= 0 || pos < size) {
                Node temp = head.next;
                while (temp.next != null) {
                    for (int i = 0; i < nodeSize; i++) {
                        if (temp.data[i] != null) {
                            if (pos == counter) {
                                this.pos = pos;
                                this.cursor = temp;
                                this.nodePos = temp.count;
                            }
                        }
                        i++;
                        counter++;
                    }
                    temp = temp.next;
                }
            }
        }
    }

    @Override
    public boolean hasNext() {
        return pos != size;
    }

    @Override
    public E next() {
        if (hasNext()) {
            E nextData;
            if (nodePos <= cursor.count - 1) {
                nextData = (E) cursor.data[nodePos];
                pos++;
                nodePos++;
                lastJumped = NEXT;
            } else {
                pos++;
                nodePos = 0;
                lastJumped = NEXT;
                cursor = cursor.next;
                nextData = (E) cursor.data[nodePos++];
            }
            return nextData;
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean hasPrevious() {
        return pos != 0;
    }

    @Override
    public E previous() {
        E prevData;
        if (head.next != tail) {
            if (hasPrevious()) {
                if (nodePos > 0) {
                    nodePos--;
                    prevData = (E) cursor.data[nodePos];
                    pos--;
                    lastJumped = PREV;
                } else {
                    nodePos = cursor.previous.count - 1;
                    pos--;
                    lastJumped = PREV;
                    cursor = cursor.previous;
                    prevData = (E) cursor.data[nodePos];
                }
                return prevData;
            }
            throw new NoSuchElementException();
        }
        throw new NoSuchElementException();
    }

    @Override
    public int nextIndex() {
        return pos;
    }

    @Override
    public int previousIndex() {
        return pos - 1;
    }

    @Override
    public void remove() {
        if (lastJumped == PREV) {
            StoutList.this.remove(pos);
        } else if (lastJumped == NEXT) {
            StoutList.this.remove(pos - 1);
            nodePos--;
            pos--;
        }
        lastJumped = -1;
    }

    @Override
    public void set(E e) {
        if (lastJumped == PREV) {
            NodeInfo t = find(pos);
            t.node.data[t.offset] = e;
        } else if (lastJumped == NEXT) {
            NodeInfo t = find(pos - 1);
            t.node.data[t.offset] = e;
        }
    }

    @Override
    public void add(E e) {
        StoutList.this.add(pos, e);
        pos++;
    }
    
    // Other methods you may want to add or override that could possibly facilitate 
    // other operations, for instance, addition, access to the previous element, etc.
    // 
    // ...
    // 
  }
  

  /**
   * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order. 
   * @param arr   array storing elements from the list 
   * @param comp  comparator used in sorting 
   */
  private void insertionSort(E[] arr, Comparator<? super E> comp) {
      int n = arr.length;

      for (int i = 1; i < n; i++) {
          E temp = arr[i];
          int j;
          for (j = i - 1; j > -1 && arr[j].compareTo(temp) > 0; j--) {
              arr[j + 1] = arr[j];
          }
          arr[j + 1] = temp;
      }
  }
  
  /**
   * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
   * description of bubble sort please refer to Section 6.1 in the project description. 
   * You must use the compareTo() method from an implementation of the Comparable 
   * interface by the class E or ? super E. 
   * @param arr  array holding elements from the list
   */
  private void bubbleSort(E[] arr) {
      int n = arr.length;
      for (int i = 0; i < n - 1; i++) {
          for (int j = 0; j < n - i - 1; j++) {
              if (arr[j].compareTo(arr[j + 1]) < 0) {
                  E temp = arr[j];
                  arr[j] = arr[j + 1];
                  arr[j + 1] = temp;
              }
          }
      }
  }
 

}