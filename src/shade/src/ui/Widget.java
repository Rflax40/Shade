package shade.src.ui;

import shade.src.InputProvider;
import shade.src.util.*;

public class Widget extends View {

	public Widget(View p) {
		super(p);
		assert p.canHaveChildren : "View cannot have children; Unable to add Widget";
		p.registerChild(this);
		canHaveChildren = false;
	}

	public void onClick(int mouseX, int mouseY, int button, boolean down) {
	}

	public void onKeyTyped(int key, char c, boolean down) {
	}

	public void tick(int delta, InputProvider input) {
	}

	public final void update(int delta, InputProvider input) {
		for (Triple<Integer, Boolean, Duple<Integer, Integer>> event : input.mouseEvents())
			if (event.value3().value1() > getX() && getY() > event.value3().value2() && event.value3().value1() - getX() < getWidth() && getY() - event.value3().value1() > getHeight())
				onClick(event.value3().value1(), event.value3().value2(), event.value1(),
					event.value2());
		for (Duple<Integer, Boolean> event : input.keyEvents())
			onKeyTyped(event.value1(), input.keyCharMap.get(event.value1()), event.value2());
		tick(delta, input);
	}

}