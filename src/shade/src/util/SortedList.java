package shade.src.util;

import java.util.*;

public class SortedList<E extends Comparable<E>> extends ArrayList<E> implements SortedSet<E> {

	private static final long serialVersionUID = 1L;

	public boolean add(E n) {
		int i = insertionIndex(n);
		super.add(i, n);
		return get(i).equals(n);
	}

	public Comparator<E> comparator() {
		return null;
	}

	public E first() {
		return get(0);
	}

	public SortedSet<E> headSet(E toElement) {
		int end = indexOf(toElement);
		while (end - 1 >= 0 && get(end).compareTo(get(end - 1)) > 0)
			end--;
		SortedList<E> s = new SortedList<E>();
		for (int i = 0; i < end; i++)
			s.add(get(i));
		return s;
	}

	private int insertionIndex(E n) {
		int low = 0;
		int high = size();
		while (low < high) {
			int med = low + (high - low) / 2;
			// -1, 0, 1
			// <, =, >
			int com = n.compareTo(get(med));
			if (com > 0)
				low = med + 1;
			else if (com < 0)
				high = med;
			else {
				low = med;
				while (com == 0 && low > 0)
					com = n.compareTo(get(--low));
				return low + 1;
			}
		}
		return low;
	}

	public E last() {
		return get(size() - 1);
	}

	public E pop(int index) {
		E e = get(index);
		remove(index);
		return e;
	}

	public SortedSet<E> subSet(E fromElement, E toElement) {
		int start = indexOf(fromElement);
		int end = indexOf(toElement);
		SortedList<E> s = new SortedList<E>();
		for (int i = start; i < end; i++)
			s.add(get(i));
		return s;
	}

	public SortedSet<E> tailSet(E fromElement) {
		int start = indexOf(fromElement);
		while (start + 1 < size() && comparator().compare(get(start), get(start + 1)) > 0)
			start++;
		SortedList<E> s = new SortedList<E>();
		for (int i = start; i < size(); i++)
			s.add(get(i));
		return s;
	}

}