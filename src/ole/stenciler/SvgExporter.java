/*
stenciler software is licensed under MIT license:

Copyright (c) 2017 ole00

Permission is hereby granted, free of charge, to any person obtaining a copy
of this hardware, software, and associated documentation files (the "Product"),
to deal in the Product without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
sell copies of the Product, and to permit persons to whom the Product is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Product.

THE PRODUCT IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE PRODUCT OR THE USE OR OTHER DEALINGS
IN THE PRODUCT.

*/

package ole.stenciler;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public class SvgExporter {
	//1 millimeter expressed in pixels
	static final float MM_IN_PX = 3.543307095f;
	
	static DataOutputStream dos;
	static Vector<RectF> rects;
	
	static float ox = Main.opt.exportOffsetX;
	static float oy = Main.opt.exportOffsetY;
	static String id;
	
	private static String STYLE = "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1";
	
	static void export(Vector<RectF> rects) {
		SvgExporter.rects = rects;
		try {
			id = Integer.toHexString((int)(System.currentTimeMillis() & 0xFFFFFF));
			ox = Main.opt.exportOffsetX * MM_IN_PX;
			oy = Main.opt.exportOffsetY * MM_IN_PX;
			String exportPath = Main.imagePath +".svg";
			SvgExporter.dos = new DataOutputStream(new FileOutputStream(exportPath));
			export();
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void export() throws IOException {
		exportHedear();
		exportBody();
		exportFooter();
	}
	
	private static void exportHedear() throws IOException {
		dos.writeBytes("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		dos.writeBytes("<svg\n");
		dos.writeBytes("\t width=\"210mm\"\n");
		dos.writeBytes("\t height=\"297mm\"\n");
		dos.writeBytes("\t viewBox=\"0 0 744.09449 1052.3622\"\n");
		dos.writeBytes("\t id=\"svg2\"\n");
		dos.writeBytes("\t version=\"1.1\"\n");
		dos.writeBytes("\t >\n");
		
		dos.writeBytes("\t <g id=\"layer1\">\n");
		
	}
	
	private static void exportBody() throws IOException {
		
		for (int i = 0; i < rects.size(); i++) {
			RectF r = rects.get(i);
			dos.writeBytes("\t\t <path id=\"path_" + id + "_" + i + "\"\n");
			dos.writeBytes("\t\t  style=\"" + STYLE + "\"\n");
			dos.writeBytes("\t\t  d=\"m ");
			exportRect(r);
			dos.writeBytes(" z\"\n");
			dos.writeBytes("\t\t />\n");
		}
	}
	
	private static void exportRect(RectF r) throws IOException {
		//final float top = 1052.3622f;
				
		dos.writeBytes("" + (r.x + ox) + "," + (r.y + oy) + " " );
		dos.writeBytes("" + r.w + ",0 " );
		dos.writeBytes("0," + r.h + " " );
		dos.writeBytes("-" + r.w + ",0" );
	}
	
	private static void exportFooter() throws IOException {
		dos.writeBytes("\t </g>\n");
		dos.writeBytes("</svg>\n");
	}
}
