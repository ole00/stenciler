stenciler
=========

A naive tool to create .svg stencils for PCB from a .png image. Cheap SMT stencils made of plastic 
foil can be bought online, but require to upload an .svg or .dxf format with the paths for laser cutter.
Some cad software allows you to export the data of the pads for SMT stencils, some don't and that's when
this tool might come handy if you can export the board design into high resolution image (.png). 

Stenciler allows you to display a .png file you've exported from geda pcb program (or other designer app)
and define pads that will be cut out of the stencil foil. Once you've defined all pads you can either
save or/and export the .svg file. If you need a .dxf file use inkscape program to open your .svg and
save it to .dxf.

compilation
===========
* requires java sdk 1.5 or later
* requires gnu make
* to compile stenciler run 'make', it should produce stenciler.jar file

running
=======
to run type:

<pre>
java -jar stenciler.jar my_board.png <options>
</pre>
options:

* -dpi X : a dpi of the .png image (default 600)
* -eox X : export offset X in mm (allows to position the stencil within the SVG)
* -eox Y : export offset Y in mm (allows to position the stencil within the SVG)

navigation & keys
=================

* LMB + mouse move : scroll the PCB image
* Mouse wheel: zoom in /out
* Insert: adds a rectangular pad under the mouse cursor
* Delete: removes an existing pad under the mouse cursor
* F1: shows about/help dialog
* F2: saves pad positions to a .stn file
* F3: loads .stn file
* F10: exports the .svg stencil
* c: picks up the pad color under the cursor
* Esc: exits stenciler

example
=======
This example is for free geda pcb desgin app:
1) open your pcb design in geda pcb app
2) show the top layer and hide the bottom layer (if your pads are on the top layer),
3) ensure pins/pads layer is visible
4) in menu select: File->Export... then select [png] button
5) in the export dialog set the dpi to 600,
6) ensure format is specified as PNG
7) ensure all check boxes are empty except 'as-shown' which needs to be checked
8) export the image by clicking the [OK] button. 
9) run stenciler 'java -jar stenciler.jar my_board.png'
10) position mouse cursor on top of a pad and press Insert key, the pad should be 
auto-filled with cyan color. If it is not, then press 'c' on top the pad to pick-up its color
and then again press Insert. Note: pad colors need to be distinct from the track colors in
the .png image (geda pcb does that OK).
11) move the mouse cursor to another pad and press Insert key, do the same for the
rest of the pads that you want to cut out of the stencil. Press and hold the Left mouse button
then move the mouse to scroll the board. Use mouse wheel to zoom in / out (only 2 levels exist).
12) delete a pad by moving mouse cursor on top of it, then press Delete key. This only deletes
the pad definition from the stencil, not from the .png image itself.
13) press F2 to save work in progres
14) press F10 to export the svg
15) press Esc to exit
16) the exported file wil have the .svg extension, so open it in your favourite viewer 
and inspect it. If the dimensions don't match ensure your PNG file is exported in 600 
dpi, or alternatively set the dpi to stenciler via parameter -dpi X. 
17) If you need .dxf format then open the exported .svg in inkscape and save it as 'Desktop cutting Plotter
(AutoCAD DXF R14)' file.
18) you can open the .stn file by stenciler by passing it as parameter instead of the .png 
file
19) stenciler has no undo feature, so make sure you press F2 often. You can reload the last saved
result by pressing F3.

