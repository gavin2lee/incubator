package com.lachesis.mnis.core.util;

public class HtmlUtils {
	private HtmlUtils() {
	}

	public static String text2Html(String text) {
		if (text == null) {
			return "";
		}
		return text.replace(" ", "&nbsp;").replace("\n", "<br>");
	}

	public static String html2Text(String html) {
		if (html == null) {
			return "";
		}
		return html.replace("&nbsp;", " ").replace("<br>", "\n");
	}
	
	public static void main(String[] args) {
		String text = "    简要病情经过及目前主要状况简要病情经过及目前主要状况\n简要病情经过及目前主要状况简要病情经过及目前主要状况简要病情经过及目前主要状况\n    简要病情经过及目前主要状况简要病情经过及目前主要状况";
		String html = text2Html(text);
		System.out.println(html);
		String text2 = html2Text(html);
		System.out.println(text2);
		System.out.println(text.equals(text2));
	}
}
