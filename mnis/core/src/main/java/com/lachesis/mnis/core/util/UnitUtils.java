package com.lachesis.mnis.core.util;

public class UnitUtils {
	public static final double MM_PER_INCH = 25.4;
	public static final double PT_PER_INCH = 72; // pt: poit,磅

	public static double getDpi(double screenWidth, double screenHeight,
			double screenSize) {
		return Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2))
				/ screenSize;
	}

	/**
	 * 
	 * @param mm
	 *            the length(unit:mm) to be converted to px
	 * @param screenWidth
	 *            the screen width(unit:px)
	 * @param screenHeight
	 *            the screen height(unit:px)
	 * @param screenSize
	 *            the screen size(unit:inch)
	 * @return
	 */
	public static int mmToPx(double mm, double screenWidth,
			double screenHeight, double screenSize) {
		return mmToPx(mm, getDpi(screenWidth, screenHeight, screenSize));
	}

	public static int mmToPx(double mm, double dpi) {
		return (int) (mm / MM_PER_INCH * dpi);
	}

	public static double pxToPt(int px, double dpi) {
		return px / dpi * PT_PER_INCH;
	}

	public static void main(String[] args) {
//		double dpi = getDpi(1280, 720, 14);
		double dpi = getDpi(1366, 768, 14);
		System.out.println("dpi: " + dpi);
		System.out.println("210 mm to px: " + mmToPx(210, dpi));
		System.out.println("297 mm to px: " + mmToPx(297, dpi));
		System.out.println("20 mm to px: " + mmToPx(20, dpi));
		System.out.println("10 mm to px: " + mmToPx(10, dpi));
		System.out.println("195 mm to px: " + mmToPx(195, dpi));
		System.out.println("22 px to pt: " + pxToPt(22, dpi));
	}
}
