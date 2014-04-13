package shade.src;

import java.util.*;

import org.lwjgl.input.*;

import shade.src.util.*;

public class DefaultInput extends InputProvider {

	public ArrayList<Triple<Integer, Boolean, Duple<Integer, Integer>>> mouseEvents = new ArrayList<Triple<Integer, Boolean, Duple<Integer, Integer>>>();
	public ArrayList<Integer> mousePressEvents = new ArrayList<Integer>();
	public final ArrayList<Integer> downMouseButtons = new ArrayList<Integer>();
	public ArrayList<Duple<Integer, Boolean>> keyEvents = new ArrayList<Duple<Integer, Boolean>>();
	public ArrayList<Integer> keyPressEvents = new ArrayList<Integer>();
	public final ArrayList<Integer> downKeys = new ArrayList<Integer>();

	public float scaleW = 1;
	public float scaleH = 1;
	public Duple<Integer, Integer> mousePos = new Duple<Integer, Integer>(0, 0);
	public Duple<Integer, Integer> mouseDelta = new Duple<Integer, Integer>(0, 0);
	public int mouseWheelDelta;

	public Iterator<Integer> getDownKeys() {
		return downKeys.iterator();
	}

	public Iterator<Integer> getDownMouseButtons() {
		return downMouseButtons.iterator();
	}

	public int getKey(String name) {
		int i = Keyboard.getKeyIndex(name.toUpperCase());
		if (i == 0)
			throw new RuntimeException("No such key: " + name);
		return i;
	}

	public Iterator<Duple<Integer, Boolean>> getKeyEvents() {
		return keyEvents.iterator();
	}

	public String getKeyName(int k) {
		return Keyboard.getKeyName(k);
	}

	public Iterator<Integer> getKeyPressEvents() {
		return keyPressEvents.iterator();
	}

	public Duple<Integer, Integer> getMouseDelta() {
		return mouseDelta;
	}

	public int getMouseWheelDelta() {
		return mouseWheelDelta;
	}

	public Iterator<Triple<Integer, Boolean, Duple<Integer, Integer>>> getMouseEvents() {
		return mouseEvents.iterator();
	}

	public Duple<Integer, Integer> getMousePos() {
		return mousePos;
	}

	public Iterator<Integer> getMousePressEvents() {
		return mousePressEvents.iterator();
	}

	public void init() throws Exception {
		Mouse.create();
		Keyboard.create();
	}

	public void poll() {
		Keyboard.enableRepeatEvents(false);
		keyEvents.clear();
		keyPressEvents.clear();
		mouseEvents.clear();
		mousePressEvents.clear();
		while (Keyboard.next()) {
			Integer k = Keyboard.getEventKey();
			boolean down = Keyboard.getEventKeyState();
			keyEvents.add(new Duple<Integer, Boolean>(k, down));
			if (down)
				keyPressEvents.add(k);
			if (down && !downKeys.contains(k))
				downKeys.add(k);
			else
				downKeys.remove(k);
			keyCharMap.put(k, Keyboard.getEventCharacter());
		}
		while (Mouse.next()) {
			Integer k = Mouse.getEventButton();
			boolean down = Mouse.getEventButtonState();
			Triple<Integer, Boolean, Duple<Integer, Integer>> t = new Triple<Integer, Boolean, Duple<Integer, Integer>>(
				k, down, new Duple<Integer, Integer>(scaleX(Mouse.getEventX()),
					scaleY(Mouse.getEventY())));
			mouseEvents.add(t);
			if (down)
				mousePressEvents.add(k);
			if (down && !downMouseButtons.contains(k))
				downMouseButtons.add(k);
			else
				downMouseButtons.remove(k);
		}
		mousePos = new Duple<Integer, Integer>(scaleX(Mouse.getX()), scaleX(Mouse.getY()));
		mouseDelta = new Duple<Integer, Integer>(scaleX(Mouse.getDX()), scaleY(Mouse.getDY()));
		mouseWheelDelta = Mouse.getDWheel();
	}

	private int scaleX(int x) {
		return (int) (x * scaleW);
	}

	private int scaleY(int y) {
		return (int) (y * scaleH);
	}

	public void setInputScale(float w, float h) {
		scaleW = w;
		scaleH = h;
	}

}