package br.com.cas10.pgman.utils;

import java.text.DecimalFormat;

public class SizeUtils {

	private static final String[] UNITS = new String[] { "bytes", "Kb", "Mb", "Gb", "Tb" };
	private static final long MULTIPLIER = 1024;
	private static final long THRESHOLD = 100 * MULTIPLIER;

	public static String prettyPrintSize(Long size) {
		DecimalFormat format = new DecimalFormat("#,##0");
		int unit = 0;
		if (size == null) {
			size = 0L;
		} else {
			while (size >= THRESHOLD && unit < 4) {
				size = (long) Math.ceil((double) size / (double) MULTIPLIER);
				unit++;
			}
		}
		return format.format(size) + " " + UNITS[unit];
	}

}
