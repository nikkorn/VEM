package gaia.utils;

/**
 * Packs/unpacks values into/out of larger primitives.
 */
public class BitPacker {
	
	public static int pack(int target, int value, int from, int length) {
		int trimmed = unpack(value, 31 - length, 31);
		return target | (trimmed << (31 - from));
	}

	public static long pack(long target, long value, int from, int length) {
		long trimmed = unpack(value, 63 - length, 63);
		return target | (trimmed << (63 - from));
	}

	public static int unpack(int value, int from, int to) {
		int trimmed = value << from;
		return trimmed >>> (31 - (to - from));
	}

	public static long unpack(long value, int from, int to) {
		long trimmed = value << from;
		return trimmed >>> (63 - (to - from));
	}
}
