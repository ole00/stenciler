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

import java.awt.image.BufferedImage;

public class ImageAnalyser {

	static final int PAD_COLOR = 0xff4d4d4d;
	
	static int padColor = PAD_COLOR; // default color of pads produced by geda PCB PNG export
	
	static Rect getRect(BufferedImage img, int x, int y) {
		if (img == null || img.getWidth() < x || img.getHeight() < y || x < 0 || y < 0) {
			return null;
		}
		
		return getPadRect(img, x, y);
	}
	
	static void setPadColor(BufferedImage img, int x, int y) {
		if (img == null || img.getWidth() < x || img.getHeight() < y || x < 0 || y < 0) {
			return;
		}
		padColor = img.getRGB(x,y);
	}
	
	static int getPadColor() {
		return padColor;
	}
	
	private static boolean pointMatches(BufferedImage img, int x, int y) {
		int color = img.getRGB(x, y);
		//System.out.println("color=" + Integer.toHexString(color));
		return color == padColor;
	}
	
	private static Rect getPadRect(BufferedImage img, int x, int y) {
		if (!pointMatches(img, x ,y)) {
			return null;
		}
		int minX = x;
		int maxX = x;
		//find min X
		for (int i  = x;  i >= 0; i--) {
			if (pointMatches(img, i, y)) {
				minX = i;
			} else {
				break;
			}
		}
		
		//find max X
		for (int i  = x;  i < img.getWidth(); i++) {
			if (pointMatches(img, i, y)) {
				maxX = i;
			} else {
				break;
			}
		}

		int minY = y;
		int maxY = y;

		//find min Y
		for (int i  = y;  i >= 0; i--) {
			if (pointMatches(img, x, i)) {
				minY = i;
			} else {
				break;
			}
		}
		
		//find max Y
		for (int i  = y;  i < img.getHeight(); i++) {
			if (pointMatches(img, x, i)) {
				maxY = i;
			} else {
				break;
			}
		}

		//one of the size of the rectangle is 0
		if (minX == maxX || minY == maxY) {
			return null;
		}
		
		return new Rect(minX, minY, maxX - minX, maxY - minY);
		
	}
}
