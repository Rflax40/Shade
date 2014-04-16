package shade.src.render;

public class FontInfo {

    public final String fontFace;
    public final short fontSize;
    public final boolean bold;
    public final boolean italic;
    public final boolean unicode;
    public final short lineHeight;
    public final short baseWidth;
    public final int charCount;
    public final short avgXAdvance;
    public final GlyphInfo[] chars;

    public FontInfo(String ff, short fs, boolean b, boolean i, boolean u, short lh, short bw, int cc, GlyphInfo[] ch) {
        fontFace = ff;
        fontSize = fs;
        bold = b;
        italic = i;
        unicode = u;
        lineHeight = lh;
        baseWidth = bw;
        charCount = cc;
        chars = ch;
        long sum = 0;
        for (GlyphInfo gi : ch) {
            sum += gi.xAdvance;
        }
        avgXAdvance = (short) (sum / ch.length);
    }

    public static class GlyphInfo {
        public final char character;
        public final short xCoord;
        public final short yCoord;
        public final short width;
        public final short height;
        public final short xOffset;
        public final short yOffset;
        public final short xAdvance;
        public final short page;

        public GlyphInfo(char c, short x, short y, short w, short h, short xo, short yo, short xa, short p) {
            character = c;
            xCoord = x;
            yCoord = y;
            width = w;
            height = h;
            xOffset = xo;
            yOffset = yo;
            xAdvance = xa;
            page = p;
        }
    }
}