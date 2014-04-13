package shade.src.ui;

import java.util.ArrayList;

import shade.src.InputProvider;

public class View {

	protected boolean canHaveParent;
	protected View parent;
	protected boolean canHaveChildren;
	protected ArrayList<View> children;

	/**
	 * Relative to parent <tt>View</tt>
	 */
	protected int posX;

	/**
	 * Relative to parent <tt>View</tt>
	 */
	protected int posY;
	protected int width;
	protected int height;

	public View(View p) {
		canHaveParent = true;
		parent = p;
		canHaveChildren = true;
		children = new ArrayList<View>();
	}

	public int getHeight() {
		return height;
	}

	public View getParent() {
		return parent;
	}

	public int getWidth() {
		return width;
	}

	public int getX() {
		if (!hasParent())
			return posX;
		else
			return parent.getX() + posX;
	}

	public int getY() {
		if (!hasParent())
			return posY;
		else
			return parent.getY() + posY;
	}

	public boolean hasChildren() {
		return canHaveChildren && !children.isEmpty();
	}

	public boolean hasParent() {
		return canHaveParent && getParent() != null;
	}

	public View registerChild(View child) {
		if (canHaveChildren)
			children.add(child);
		return this;
	}

	public void render(int delta) {
		for (View child : children)
			child.render(delta);
	}

	public void update(int delta, InputProvider input) {
		for (View child : children)
			child.update(delta, input);
	}

}