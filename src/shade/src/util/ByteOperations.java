package shade.src.util;

public class ByteOperations {

	public static int getValueAt(int i, int pos, int len) {
		int mask = (int) Math.pow(2, len) - 1;
		mask <<= pos;
		i &= mask;
		i >>= pos;
		return i;
	}

	public static long getValueAt(long l, int pos, int len) {
		int mask = (int) Math.pow(2, len) - 1;
		mask <<= pos;
		l &= mask;
		l >>= pos;
		return l;
	}

	public static byte sign(int i) {
		return (byte) (i & 0xFF);
	}

	public static String toBinaryString(byte b) {
		String s = Integer.toBinaryString(b);
		while (s.length() < 8)
			s = 0 + s;
		return s;
	}

	public static String toBinaryString(int i) {
		String s = Integer.toBinaryString(i);
		while (s.length() < 32)
			s = 0 + s;
		return s;
	}

	public static String toBinaryString(long l) {
		String s = Long.toBinaryString(l);
		while (s.length() < 64)
			s = 0 + s;
		return s;
	}

	public static String toBinaryString(short s) {
		String str = Integer.toBinaryString(s);
		while (str.length() < 16)
			str = 0 + str;
		return str;
	}

	public static int unsign(byte b) {
		return b & 0xFF;
	}

}