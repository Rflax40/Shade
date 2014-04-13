package shade.src.util;

import java.util.Iterator;

public class Duple<T, T1> implements Iterable<Object> {

	private Object[] objs;

	public Duple(T t, T1 t1) {
		objs = new Object[2];
		objs[0] = t;
		objs[1] = t1;
	}

	@SuppressWarnings("rawtypes")
	public boolean equals(Object o) {
		if (o instanceof Duple) {
			for (int i = 0; i < objs.length; i++)
				if (!objs[i].equals(((Duple) o).objs[i]))
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

}