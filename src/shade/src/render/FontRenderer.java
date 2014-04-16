package shade.src.render;

import org.lwjgl.opengl.GL11;
import shade.src.render.FontInfo.GlyphInfo;

import java.awt.*;

public class FontRenderer {

    public IFont font;
    private int[] glyphLists;

    public FontRenderer(IFont f) {
        font = f;
        int count = font.info().charCount;
        glyphLists = new int[count];
        int start = GL11.glGenLists(count);
        for (int i = 0; i < count; i++) {
            GlyphInfo glyph = font.info().chars[i];
            glyphLists[i] = start + i;
            GL11.glNewList(start + i, GL11.GL_COMPILE);
            font.texture(i).drawModalRect(glyph.xCoord, glyph.yCoord, glyph.width, glyph.height, 0, 0, 0, glyph.width, glyph.height);
            GL11.glEndList();
        }
    }

    public void drawCenteredString(String str, double x, double y, double z) {
        drawCenteredString(str, x, y, z, defaultSize());
    }

    public void drawCenteredString(String str, double x, double y, double z, int s) {
        drawString(str, x - lineWidth(str, s) / 2D, y + linesHigh(str) * lineHeight(s) / 2D, z, s);
    }

    public double drawChar(char c, double x, double y, double z, int s) {
        if (!Character.isWhitespace(c) && font.glyphIndex(c) >= 0) {
            return drawGlyph(font.glyphIndex(c), x, y, z, s);
        } else {
            return width(c, s);
        }
    }

    public double drawGlyph(int gi, double x, double y, double z, int s) {
        double ra = ratio(s);
        GlyphInfo glyph = font.info().chars[gi];
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glScaled(ra, ra, 0);
        GL11.glTranslated(glyph.xOffset, -glyph.yOffset, 0);
        GL11.glCallList(glyphLists[gi]);
        GL11.glPopMatrix();
        return glyph.xAdvance * ra;
    }

    public void drawString(String str, double x, double y, double z) {
        drawString(str, x, y, z, defaultSize());
    }

    public void drawString(String str, double x, double y, double z, int s) {
        double drawX = x;
        for (char c : str.toCharArray()) {
            double i = drawChar(c, drawX, y, z, s);
            if (i < 0) {
                i = 0;
            }
            // drawY -= ratio(s) * font.info().lineHeight;
            // drawX = x;
            drawX += i;
        }
    }

    public void drawStrings(String[] strs, double x, double y, double z) {
        drawStrings(strs, x, y, z, defaultSize());
    }

    public void drawStrings(String[] strs, double x, double y, double z, int s) {
        double lh = lineHeight(s);
        for (String str : strs) {
            drawString(str, x, y, z, s);
            y -= lh;
        }
    }

    public Point.Double getBounds(int size, String... strs) {
        double maxW = 0;
        double height = lineHeight(size) * strs.length;
        for (String str : strs) {
            double w = lineWidth(str, size);
            if (w > maxW) {
                maxW = w;
            }
        }
        return new Point.Double(maxW, height);
    }

    public Point.Double getBounds(String... strs) {
        return getBounds(defaultSize(), strs);
    }

    public double lineHeight() {
        return lineHeight(defaultSize());
    }

    public double lineHeight(int s) {
        return (int) (font.info().lineHeight * ratio(s) + 0.5);
    }

    private double ratio(int s) {
        return (double) s / defaultSize();
    }

    public short defaultSize() {
        return font.info().fontSize;
    }

    public int linesHigh(String s) {
        int i = 1;
        String str = s;
        while (str.contains("\n")) {
            str = str.replaceFirst("\n", "");
            i++;
        }
        return i;
    }

    public double lineWidth(String str) {
        return lineWidth(str, defaultSize());
    }

    public double lineWidth(String str, int s) {
        double w = 0;
        for (char c : str.toCharArray()) {
            double cw = width(c, s);
            w += cw;
        }
        return w;
    }

    private double width(char c, int s) {
        double ra = ratio(s);
        int in = font.glyphIndex(c);
        if (!Character.isWhitespace(c) && in >= 0) {
            return font.info().chars[in].xAdvance * ra;
        } else if (c == ' ') {
            return font.info().avgXAdvance * ra;
        } else if (c == '\t') {
            return font.info().avgXAdvance * 4 * ra;
        }
        return -1;
    }
}