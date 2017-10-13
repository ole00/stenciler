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

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

public class RectLayer {
	Vector<Rect> rects;
	Color color;
	
	public RectLayer() {
		rects = new Vector<Rect>();
		color = new Color(0, 200, 255, 128);
	}

	
	void paint(Graphics g, int paintX, int paintY, int zoom) {
		final int max = rects.size();
		g.setColor(color);
		for (int i = 0; i < max; i++) {
			Rect r = rects.get(i);
			g.fillRect(paintX + (r.x / zoom), paintY + (r.y / zoom), r.w / zoom, r.h / zoom);
		}
	}
	
	boolean addRect(Rect r) {
		if (r == null) {
			return false;
		}
		//check rect already exists
		Rect e = findRect(r.x + r.w / 2, r.y + r.h / 2);
		if (e == null) {
			rects.add(r);
			return true;
		}
		return false;
	}
	
	boolean deleteRect(int x, int y) {
		Rect r = findRect(x, y);
		if (r != null) {
			return rects.remove(r);
		}
		return false;
	}
	
	Rect findRect(int x, int y) {
		final int max = rects.size();
		for (int i = 0; i < max; i++) {
			Rect r = rects.get(i);
			if (r.x <= x && r.y <= y && (r.x + r.w) > x && (r.y + r.h) > y) {
				return r;
			}

		}
		return null;
	}
	
	Vector<Rect> getRects() {
		return rects;
	}
	
	boolean setRects(Vector<Rect> r) {
		if (r == null) {
			return false;
		}
		rects = r;
		return true;
	}
	
	Vector<RectF> getRectsInMm(Options opts) {
		Vector<RectF> res = new Vector<RectF>();
		
		final int max = rects.size();
		for (int i = 0; i < max; i++) {
			Rect r = rects.get(i);
			res.add(new RectF(
				pixToMm(r.x, opts), pixToMm(r.y, opts),
				pixToMm(r.w, opts), pixToMm(r.h, opts)
				)
			);
		}
		
		return res;
	}
	
	Vector<RectF> getRectsInPx(Options opts) {
		Vector<RectF> res = new Vector<RectF>();
		
		final int max = rects.size();
		for (int i = 0; i < max; i++) {
			Rect r = rects.get(i);
			res.add(new RectF(
				pixToPx(r.x, opts), pixToPx(r.y, opts),
				pixToPx(r.w, opts), pixToPx(r.h, opts)
				)
			);
		}
		
		return res;
	}
	
	private float pixToMm(int p, Options opts) {
		return (p * 25.4f) / (float) opts.dpi;
	}
	
	private float pixToPx(int p, Options opts) {
		return (p * 90.0f) / (float) opts.dpi;
	}
}
