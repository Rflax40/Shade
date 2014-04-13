package shade.src;

import java.util.*;

import shade.src.util.*;

public abstract class InputProvider {

	public final HashMap<Integer, Character> keyCharMap = new HashMap<Integer, Character>();

	@SuppressWarnings("unchecked")
	public Iterable<Integer> downKeys() {
		return makeIterable(getDownKeys());
	}

	@SuppressWarnings("unchecked")
	public Iterable<Integer> downMouseButtons() {
		return makeIterable(getDownMouseButtons());
	}

	protected abstract Iterator<Integer> getDownKeys();

	protected abstract Iterator<Integer> getDownMouseButtons();

	/**
	 * @param s
	 *            - Name of a key
	 * @return The <tt>int</tt> representation of the key
	 */
	public abstract int getKey(String s);

	public Duple<Integer, Boolean> getKeyEvent(int k) {
		for (Duple<Integer, Boolean> d : keyEvents())
			if (d.value1().intValue() == k)
				return d;
		return null;
	}

	public Duple<Integer, Boolean> getKeyEvent(String s) {
		return getKeyEvent(getKey(s));
	}

	protected abstract Iterator<Duple<Integer, Boolean>> getKeyEvents();

	/**
	 * @param k
	 *            - <tt>int</tt> code of a key
	 * @return The <tt>String</tt> representation of the key
	 */
	public abstract String getKeyName(int k);

	protected abstract Iterator<Integer> getKeyPressEvents();

	public abstract Duple<Integer, Integer> getMouseDelta();

	public abstract int getMouseWheelDelta();

	public Triple<Integer, Boolean, Duple<Integer, Integer>> getMouseEvent(int k) {
		for (Triple<Integer, Boolean, Duple<Integer, Integer>> t : mouseEvents())
			if (t.value1().intValue() == k)
				return t;
		return null;
	}

	protected abstract Iterator<Triple<Integer, Boolean, Duple<Integer, Integer>>> getMouseEvents();

	public abstract Duple<Integer, Integer> getMousePos();

	protected abstract Iterator<Integer> getMousePressEvents();

	/**
	 * Does any setup before mouse and keyboard input can be polled
	 * 
	 * @throws Exception
	 */
	public abstract void init() throws Exception;

	public boolean keyDown(int k) {
		for (int i : downKeys())
			if (i == k)
				return true;
		return false;
	}

	public boolean keyDown(String s) {
		return keyDown(getKey(s));
	}

	@SuppressWarnings("unchecked")
	public Iterable<Duple<Integer, Boolean>> keyEvents() {
		return makeIterable(getKeyEvents());
	}

	public boolean keyPressed(int k) {
		for (int i : keyPressEvents())
			if (i == k)
				return true;
		return false;
	}

	public boolean keyPressed(String s) {
		return keyPressed(getKey(s));
	}

	@SuppressWarnings("unchecked")
	public Iterable<Integer> keyPressEvents() {
		return makeIterable(getKeyPressEvents());
	}

	@SuppressWarnings("rawtypes")
	private Iterable makeIterable(Iterator i) {
		final Iterator it = i;
		return new Iterable() {

			public Iterator iterator() {
				return it;
			}

		};
	}

	public boolean mouseButtonDown(int k) {
		for (int i : downMouseButtons())
			if (i == k)
				return true;
		return false;
	}

	public boolean mouseButtonPressed(int k) {
		for (int i : mousePressEvents())
			if (i == k)
				return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public Iterable<Triple<Integer, Boolean, Duple<Integer, Integer>>> mouseEvents() {
		return makeIterable(getMouseEvents());
	}

	@SuppressWarnings("unchecked")
	public Iterable<Integer> mousePressEvents() {
		return makeIterable(getMousePressEvents());
	}

	public abstract void poll();

	public abstract void setInputScale(float w, float h);

}