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

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class Main extends WindowAdapter implements Runnable, WindowListener {
	
	static final String VERSION = "0.1";
	
	static Options opt;
	static Frame frame;
	static PcbView view;
	static String imagePath;
	static String filePath;
	
	static final String[] NAVI_TEXT= {
		"Navigation & keys:",
		" LMB + mouse move : scroll the PCB image",
		" Mouse wheel: zoom in /out",
		" Insert: adds a rectangular pad under the mouse cursor",
		" Delete: removes an existing pad under the mouse cursor",
		" F1: shows about/help dialog",
		" F2: saves pad positions to a .stn file",
		" F3: loads .stn file",
		" F10: exports the .svg stencil",
		" c: picks up the pad color under the cursor",
		" Esc: exits stenciler",
	};
	
	public static void main(String[] args) {
		Point size = getScreenSize();
		opt = new Options();
		checkArguments(args, opt);
		
		Vector<Rect> rects = null;
		if (filePath != null) {
			rects = ReadWrite.read(filePath);
		}
		
		//System.out.println(size);
		view = new PcbView(size.x, size.y, getImage());
		RectLayer layer = new RectLayer();
		if (rects != null) {
			layer.setRects(rects);
		}
		view.setRectLayer(layer);
		frame = new Frame();
		//frame.setUndecorated(true);
		frame.setSize(size.x , size.y);
		frame.add(view);
		frame.addKeyListener(view);
		frame.doLayout();
		frame.setVisible(true);
		frame.addWindowListener(new Main());
		frame.setTitle("Stenciler: " + (filePath != null ? filePath: imagePath) );

	}
	
	private static void checkArguments(String[] args, Options opt) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if ("-eox".equals(arg)) {
				opt.exportOffsetX = Integer.parseInt(args[++i]);
			} else
			if ("-eoy".equals(arg)) {
				opt.exportOffsetY = Integer.parseInt(args[++i]);
			} else
			if ("-dpi".equals(arg)) {
				opt.dpi = Integer.parseInt(args[++i]);
			} else {
				if (arg.endsWith(".stn")) {
					filePath = arg;
				} else {
					imagePath = arg;
				}
			}
		}
		
		if (filePath == null && imagePath == null) {
			printHelp();
			System.exit(-1);
		}
	}
	
	private static void printHelp() {
		System.out.println("stenciler   v." + VERSION);
		System.out.println("a naive tool to create SVG SMT templates for your PCB from a PNG image");
		System.out.println("usage: java -jar stenciler.jar file <options>");
		System.out.println("file: either .png file or .stn file (created by stenciler)");
		System.out.println("-dpi X : a dpi of the .png image (default 600)");
		System.out.println("-eox X : export offset X in mm (allows to position the stencil within the SVG)");
		System.out.println("-eox Y : export offset Y in mm (allows to position the stencil within the SVG)");
		System.out.println("Example:");
		System.out.println("java -jar stenciler.jar my_board.png -dpi 900 -eox 20 -eoy 60:");
		System.out.println("  Runs stenciler upon my_board.png image which was created with 900 dpi.");
		System.out.println("  When exported it shifts the output 20mm to the right and 60mm down.");

		//print navigation & keys info
		for (int i = 0; i < NAVI_TEXT.length; i++) {
			System.out.println(NAVI_TEXT[i]);
		}

	}
	
	public static void showAboutDialog() {
		AboutDialog ad = new AboutDialog(frame);
		ad.setVisible(true);
	}
	
	private static BufferedImage getImage()  {
		BufferedImage ii = null;
		
		try {
			ii = ImageIO.read(new File(imagePath));
/*
			//find DPI - http://www.libpng.org/pub/png/spec/1.2/PNG-Chunks.html, 4.2.4.2. pHYs Physical pixel dimensions
			ImageInputStream stream = ImageIO.createImageInputStream(new File(imagePath));
			Iterator<ImageReader> it = ImageIO.getImageReadersBySuffix("png");
			while (it.hasNext()) {
				ImageReader reader = it.next();
				reader.setInput(stream);
				IIOMetadata metadata = reader.getImageMetadata(0);
				IIOMetadataNode standardTree = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
				IIOMetadataNode dimension = (IIOMetadataNode) standardTree.getElementsByTagName("Dimension").item(0);
				System.out.println("");

			}
*/
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return ii;
	}
	
	public static Point getScreenSize() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Point result = new Point();
		result.x = gd.getDisplayMode().getWidth();
		result.y = gd.getDisplayMode().getHeight();
		return result;
		
	}
	
	
	public static void quit() {
		Thread t = new Thread(new Main());
		t.start();
	}
	//run terminating sequence;
	public void run() {
		try {
			Thread.sleep(50);
		} catch (Exception e) {
			
		}
		view.close();
		frame.setVisible(false);
		frame.dispose();
	}

	public void windowClosing(WindowEvent e) {
		quit();
	}


}
