package shade.src.util;

import java.util.Iterator;

public class Triple<T, T1, T2> implements Iterable<Object> {

	private Object[] objs;

	public Triple(T t, T1 t1, T2 t2) {
		objs = new Object[3];
		objs[0] = t;
		objs[1] = t1;
		objs[2] = t2;
	}

	@SuppressWarnings("rawtypes")
	public boolean equals(Object o) {
		if (o instanceof Triple) {
			for (int i = 0; i < objs.length; i++)
				if (!objs[i].equals(((Triple) o).objs[i]))
					return false;
			return true;
		}
		return false;
	}

	public Iterator<Object> iterator() {
		Iterator<Object> i = new Iterator<Object>() {

			private int index = 0;

			public boolean hasNext() {
				return index < objs.length;
			}

			public Object next() {
				return objs[index++];
			}

			public void remove() {
				objs[index - 1] = null;
			}

		};
		return i;
	}

	@SuppressWarnings("unchecked")
	public T value1() {
		return (T) objs[0];
	}

	@SuppressWarnings("unchecked")
	public T1 value2() {
		return (T1) objs[1];
	}

	@SuppressWarnings("unchecked")
	public T2 value3() {
		return (T2) objs[2];
	}

}