
SRC_DIR := src/ole/stenciler
FILES := \
	${SRC_DIR}/ImageAnalyser.java \
	${SRC_DIR}/Options.java \
	${SRC_DIR}/ReadWrite.java \
	${SRC_DIR}/Rect.java \
	${SRC_DIR}/SvgExporter.java \
	${SRC_DIR}/PcbView.java \
	${SRC_DIR}/RectF.java \
	${SRC_DIR}/RectLayer.java \
	${SRC_DIR}/Main.java \
	${SRC_DIR}/AboutDialog.java \


all: stenciler.jar

stenciler.jar : compile package


compile:
	rm -rf bin
	mkdir -p bin
	javac -target 1.6 -source 1.6 -d bin ${FILES}

package:
	@rm -f stenciler.jar
	@printf "Main-Class: ole.stenciler.Main\n" >/tmp/stenciler-mf
	jar  cfm stenciler.jar /tmp/stenciler-mf -C bin .
	@rm /tmp/stenciler-mf
