package shade.src;

public interface IWindow {

	public void close();

	public int getHeight();

	public InputProvider getInput();

	public String getRenderContext();

	public int getWidth();

	public boolean hasFocus();

	public boolean isOpen();

	public void open() throws Exception;

	public void resize(int w, int h, boolean f) throws Exception;

	public void setWindowController(WindowController wc);

	public void update();
}