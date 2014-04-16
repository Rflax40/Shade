package shade.src.render;

public interface IFont {

    public int glyphIndex(char c);

    public FontInfo info();

    public Texture texture(int gi);
}