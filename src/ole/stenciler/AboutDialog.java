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

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//credit: https://www.tutorialspoint.com/awt/awt_dialog.htm
public class AboutDialog extends Dialog{
	
	public AboutDialog(Frame parent){
        super(parent, true);         
        setBackground(Color.white);
        setLayout(new BorderLayout());
        Panel panel = new Panel();
        panel.add(new Button("Close"));
        add("South", panel);
        setSize(500,400);
        setLocationByPlatform(true);
        setTitle("About stenciler");

        addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent windowEvent){
              dispose();
           }
        });
     }

     public boolean action(Event evt, Object arg){
        if(arg.equals("Close")){
           dispose();
           return true;
        }
        return false;
     }

     public void paint(Graphics g){
        g.setColor(Color.black);
        g.drawString("Stenciler v." + Main.VERSION, 40, 70);
        g.setColor(Color.gray);
        g.drawLine(40, 80, 460, 80);
        String[] text = Main.NAVI_TEXT;
        for (int i = 0; i < text.length; i++) {
            g.drawString(text[i], 40, 100 + (20 * i));        	
        }
     }

}
