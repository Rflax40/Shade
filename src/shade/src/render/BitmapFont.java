package shade.src.render;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import shade.src.render.FontInfo.GlyphInfo;
import shade.src.resource.Resource;

import java.io.InputStream;
import java.util.HashMap;

public class BitmapFont implements IFont {

    public static final String INFO = "info";
    public static final String INFO_FACE = "face";
    public static final String INFO_SIZE = "size";
    public static final String INFO_BOLD = "bold";
    public static final String INFO_ITALIC = "italic";
    public static final String INFO_UNICODE = "unicode";
    public static final String COMMON = "common";
    public static final String COMMON_LINE_HEIGHT = "lineHeight";
    public static final String COMMON_BASE = "base";
    public static final String COMMON_PAGES = "pages";
    public static final String PAGE = "page";
    public static final String PAGE_ID = "id";
    public static final String PAGE_FILE = "file";
    public static final String CHARS = "chars";
    public static final String CHARS_COUNT = "count";
    public static final String CHAR = "char";
    public static final String CHAR_ID = "id";
    public static final String CHAR_X = "x";
    public static final String CHAR_Y = "y";
    public static final String CHAR_WIDTH = "width";
    public static final String CHAR_HEIGHT = "height";
    public static final String CHAR_X_OFFSET = "xoffset";
    public static final String CHAR_Y_OFFSET = "yoffset";
    public static final String CHAR_X_ADVANCE = "xadvance";
    public static final String CHAR_PAGE = "page";
    private FontInfo info;
    private Texture[] tex;
    private HashMap<Character, Integer> glyphIndexMap;

    public BitmapFont(String f) throws Exception {
        this(f, f.substring(0, f.lastIndexOf('/')));
    }

    public BitmapFont(String f, String d) throws Exception {
        this(Resource.getResource(f).getAsStream(), d);
    }

    public BitmapFont(InputStream in, String d) throws Exception {
        glyphIndexMap = new HashMap<>();
        createFontInfoXML(in, d);
        in.close();
    }

    private void createFontInfoXML(InputStream in, final String dir) throws Exception {
        XMLReader parser = XMLReaderFactory.createXMLReader();
        DefaultHandler handler = new DefaultHandler() {
            int cc = 0;
            String ff = "";
            short s = 0;
            boolean bo = false;
            boolean it = false;
            boolean un = true;
            short lh = 0;
            short bw = 0;
            short pages = 1;
            GlyphInfo[] glyphs = new GlyphInfo[0];

            public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes atts) throws SAXException {
                switch (localName) {
                    case INFO:
                        ff = atts.getValue(INFO_FACE);
                        s = Short.valueOf(atts.getValue(INFO_SIZE));
                        bo = atts.getValue(INFO_BOLD).equals("1");
                        it = atts.getValue(INFO_ITALIC).equals("1");
                        un = atts.getValue(INFO_UNICODE).equals("1");
                        break;
                    case COMMON:
                        lh = Short.parseShort(atts.getValue(COMMON_LINE_HEIGHT));
                        bw = Short.parseShort(atts.getValue(COMMON_BASE));
                        pages = Short.parseShort(atts.getValue(COMMON_PAGES));
                        tex = new Texture[pages];
                        break;
                    case PAGE:
                        Texture t = Texture.getTexture(dir + '/' + atts.getValue(PAGE_FILE));
                        tex[Short.parseShort(atts.getValue(PAGE_ID))] = t;
                        break;
                    case CHARS:
                        glyphs = new GlyphInfo[Integer.parseInt(atts.getValue(CHARS_COUNT))];
                        break;
                    case CHAR:
                        short id = Short.parseShort(atts.getValue(CHAR_ID));
                        short x = Short.parseShort(atts.getValue(CHAR_X));
                        short y = Short.parseShort(atts.getValue(CHAR_Y));
                        short w = Short.parseShort(atts.getValue(CHAR_WIDTH));
                        short h = Short.parseShort(atts.getValue(CHAR_HEIGHT));
                        short xo = Short.parseShort(atts.getValue(CHAR_X_OFFSET));
                        short yo = Short.parseShort(atts.getValue(CHAR_Y_OFFSET));
                        short xa = Short.parseShort(atts.getValue(CHAR_X_ADVANCE));
                        short p = Short.parseShort(atts.getValue(CHAR_PAGE));
                        glyphIndexMap.put((char) id, cc);
                        glyphs[cc++] = new GlyphInfo((char) id, x, y, w, h, xo, yo, xa, p);
                        break;
                }
            }

            public void endElement(String uri, String localName, String qname) {
                if (localName.equals("font")) {
                    info = new FontInfo(ff, s, bo, it, un, bw, lh, cc, glyphs);
                }
            }
        };
        parser.setContentHandler(handler);
        parser.parse(new InputSource(in));
    }

    public int glyphIndex(char c) {
        Integer in = glyphIndexMap.get(c);
        if (in == null) {
            return -1;
        } else {
            return in;
        }
    }

    public FontInfo info() {
        return info;
    }

    public Texture texture(int gi) {
        return tex[info.chars[gi].page];
    }
}