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
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Vector;

public class ReadWrite {
	static DataOutputStream dos;
	static Vector<Rect> rects;
	static String filePath; 
	
	static Vector<Rect> read(String path) {
		ReadWrite.rects = new Vector<Rect>();
		try {
			filePath = (path == null) ? Main.imagePath +".stn" : path ;
			FileReader fr = new FileReader(filePath);
			LineNumberReader reader = new LineNumberReader(fr);
			String line = reader.readLine();
			while (line != null) {
				readLine(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			ReadWrite.rects = null;
			return null;
		}
		Vector<Rect> r = ReadWrite.rects;
		ReadWrite.rects = null;
		return r;
		
	}
	
	static void readLine(String line) {
		if (line == null) {
			return;
		}
		line = line.trim();
		
		if (line.startsWith("#")) {
			return;
		}
		String[] bits = line.split(" ");
		if (bits.length < 2) {
			return;
		}
		
		if ("i". equals(bits[0])) {
			Main.imagePath = bits[1];
		} else
		if ("d". equals(bits[0])) {
			int dpi = Integer.decode(bits[1]);
			if (dpi != 0) {
				Main.opt.dpi = dpi;
			}
		} else
		if ("r". equals(bits[0])) {
			decodeRectangle(bits);
		} else
		if ("c". equals(bits[0])) {
			System.out.println("pad color: " + bits[1]);
			ImageAnalyser.padColor = (int)(Long.decode(bits[1]) & 0xFFFFFFFF);
		}
	}
	
	static void decodeRectangle(String[] bits) {
		if (bits.length != 5) {
			return;
		}
		int x = Integer.decode(bits[1]);
		int y = Integer.decode(bits[2]);
		int w = Integer.decode(bits[3]);
		int h = Integer.decode(bits[4]);
		
		ReadWrite.rects.add(new Rect(x, y, w, h));
	}
	
	static void save(Vector<Rect> rects) {
		ReadWrite.rects = rects;
		try {
			filePath = Main.imagePath +".stn";
			ReadWrite.dos = new DataOutputStream(new FileOutputStream(filePath));
			save();
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void save() throws IOException {
		dos.writeBytes("# Stenciler " + Main.VERSION + "\n");
		dos.writeBytes("i " + Main.imagePath + "\n");
		dos.writeBytes("d " + Main.opt.dpi + "\n");
		dos.writeBytes("c 0x" + Integer.toHexString(ImageAnalyser.getPadColor()) + "\n");
		
		for (int i = 0; i < rects.size(); i++) {
			Rect r = rects.get(i);
			dos.writeBytes("r " + r.x + " " + r.y + " " + r.w + " " + r.h + "\n");
		}
	}
}
