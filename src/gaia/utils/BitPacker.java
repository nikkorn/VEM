package gaia.utils;

/**
 * Packs/unpacks values into/out of larger primitives.
 */
public class BitPacker {
	
	public static int pack(int target, int value, int from, int length) {
		int trimmed = unpack(value, 0, length);
		return target | (trimmed << from);
	}
	
	public static long pack(long target, long value, int from, int length) {
		long trimmed = unpack(value, 0, length);
		return target | (trimmed << from);
	}

	public static int unpack(int value, int from, int length) { 
		int trimmed = value << 31 - (from + (length - 1));
		return trimmed >>> (31 - (length - 1));
	}
	
	public static long unpack(long value, int from, int length) { 
		long trimmed = value << 63 - (from + (length - 1));
		return trimmed >>> (63 - (length - 1));
	}
}
