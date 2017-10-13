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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class PcbView extends Canvas implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	
	private int screenW;
	private int screenH;
	private BufferedImage imageFull;
	private BufferedImage imageZoom;
	private boolean forceRepaint;
	private boolean repaintBackground;
	private int paintX;
	private int paintY;
	private int dragX;
	private int dragY;
	private int dragPaintX;
	private int dragPaintY;
	private int mouseX;
	private int mouseY;
	private RectLayer layer;
	private int zoom = 2;
	
	
	public PcbView(int width, int height, BufferedImage image) {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		this.screenW = width;
		this.screenH = height;
		setSize(width, height);
		this.imageFull= image;
		imageZoom = scaleImage();
		repaintBackground = true;
	}
	
	private BufferedImage scaleImage() {
		int w2 = imageFull.getWidth() / 2;
		int h2 = imageFull.getHeight() / 2;
		BufferedImage result = new BufferedImage(w2, h2, BufferedImage.TYPE_4BYTE_ABGR_PRE);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.drawImage(imageFull, 0, 0, w2, h2, 0, 0, imageFull.getWidth(), imageFull.getHeight(), null);
		return result;
	}
	
	public void setRectLayer(RectLayer l) {
		layer = l;
	}
	
	public void paint(Graphics g) {
		if (forceRepaint) {
			forceRepaint = false;
		}
		
		if (imageFull == null) {
			return;
		}

		BufferedImage image = zoom == 1 ? imageFull : imageZoom;
		

		//screen is wider than picture width -> center
		if (screenW - image.getWidth() > 0) {
			paintX = (screenW - image.getWidth()) / 2;
		} 
		//picture is wider than the screen -> allow scrolling
		else {
			if (paintX > 0) {
				paintX = 0;
			} else
			if (paintX < (screenW - image.getWidth())) {
				paintX = (screenW - image.getWidth());
			}
		}

		//screen is taller than picture height - > center
		if (screenH - image.getHeight() > 0) {
			paintY = (screenH - image.getHeight()) / 2;
		} 
		//picture is tall than the screen -> allow scrolling
		else {
			if (paintY > 0) {
				paintY = 0;
			} else
			if (paintY < (screenH - image.getHeight())) {
				paintY = (screenH - image.getHeight());
			}
		}		
	
		if (repaintBackground) {
			repaintBackground = false;
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}		
		g.drawImage(image, paintX, paintY, null);
		if (layer != null) {
			layer.paint(g, paintX, paintY, zoom);
		}
	}
	
	public void update(Graphics g) {
		forceRepaint = true;
		paint(g);
	}

	public void close() {
		System.out.println("closing...");
	}
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		int mods = e.getModifiers();
		if (key == KeyEvent.VK_ESCAPE) {
			Main.quit();
		}
		if (key == KeyEvent.VK_SPACE) {
			doRepaint();
		}
		
		if (key == KeyEvent.VK_DELETE) {
			if (layer != null && layer.deleteRect((mouseX - paintX) * zoom,  (mouseY - paintY) * zoom)) {
				doRepaint();
			}
		}

		if (key == KeyEvent.VK_INSERT) {
			if (layer != null && layer.findRect((mouseX - paintX) * zoom,  (mouseY - paintY) * zoom) == null) {
				Rect r = ImageAnalyser.getRect(imageFull, (mouseX - paintX) * zoom,  (mouseY - paintY) * zoom);
				if (layer.addRect(r)) { 
					doRepaint();
				}
			}
		}
		
		if (key == KeyEvent.VK_C) {
			if (layer != null) {
				ImageAnalyser.setPadColor(imageFull, (mouseX - paintX) * zoom,  (mouseY - paintY) * zoom);
			}
		}
		
		if (key == KeyEvent.VK_F10) {
			if (layer != null) {
				
				SvgExporter.export(layer.getRectsInPx(Main.opt));
			}
		}
		if (key == KeyEvent.VK_F1) { 
			Main.showAboutDialog();
		}
		
		if ((key == KeyEvent.VK_F2) || (key == KeyEvent.VK_S && (mods & KeyEvent.CTRL_MASK) != 0 )) {
			if (layer != null) {
				ReadWrite.save(layer.getRects());
			}
		}
		
		if ((key == KeyEvent.VK_F3) || (key == KeyEvent.VK_R && (mods & KeyEvent.CTRL_MASK) != 0 )) {
			if (layer != null) {
				Vector<Rect> rects = ReadWrite.read(null);
				if (layer.setRects(rects)) {
					doRepaint();
				}
			}
		}

	}

	private void doRepaint() {
		forceRepaint = true;
		repaint();
	}

	
	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public void mouseDragged(MouseEvent e) {
		int dx = e.getX() - dragX;
		int dy = e.getY() - dragY;
		paintX = dragPaintX + dx;
		paintY = dragPaintY + dy;
		doRepaint();
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		dragX = e.getX();
		dragY = e.getY();
		dragPaintX = paintX;
		dragPaintY = paintY;
		
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		int z = zoom;
		final int r = e.getWheelRotation();
		if (r < 0 && zoom > 1) {
			zoom = 1;
			paintX *= 3;
			paintY *= 3;
		} else
		if (r > 0 && zoom == 1) {
			zoom = 2;
			paintX /= 3;
			paintY /= 3;
		}
		
		if (z != zoom) {
			repaintBackground = true;
			doRepaint();
		}
	}

}
