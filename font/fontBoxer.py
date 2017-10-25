import fontforge
import sys
import pdb

fname = sys.argv[1]
x = fname.split(".")
name = x[0]
ext = x[1]

font = fontforge.open(fname)

for glyph in font.glyphs():
    w = glyph.width
    (x1,y1,x2,y2) = glyph.boundingBox()
    x12 = x1 + (x2-x1) / 2
    y11 = y1 + (y2-y1) / 3
    y12 = y2 - (y2-y1) / 3
    glyph.clear()

    pen = glyph.glyphPen()
    pen.moveTo((x1,y1))
    pen.lineTo((x1,y2))
    pen.lineTo((x2,y2))
    pen.lineTo((x2,y12))
    pen.lineTo((x12,y12))
    pen.lineTo((x12,y11))
    pen.lineTo((x2,y11))
    pen.lineTo((x2,y1))
    pen.closePath()
    pen = None
    glyph.width = w

font.fontname = name + "-Box"
font.generate(name + "-Box." + ext)
