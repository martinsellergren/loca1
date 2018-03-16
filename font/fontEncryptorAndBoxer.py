'''
Creates two fonts from one:
 1. Every glyph transformed into a code-box -> codeFont.
 2. Every glyph transformed into a dir-box -> boxFont.

Also creates a mapping table, code->unicode.

Code-box and dir-box have same pos and dims. All boxes have same height
(=ascent+descent=em). Box width might vary depending on glyph-width.
Code, box bad if font has thin glyphs - use mono-space-font.
Note:
"Monospace" fonts sometimes contains some incorrectly sized glyphs so
code-box/box may still very in width.

Code-box contains a binary code.. Dir-box contains a box shaped as the
symbol [ .
'''

import fontforge
import sys


#also updates mappingTable
def replaceWithCodeBox(glyph, bbs):
    pass

def replaceWithDirBox(glyph, bbs):
    temp_glyphW = glyph.width
    x1,y1,x2,y2 = bbs
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
    glyph.width = temp_glyphW


def getXBounds(glyph):
    w = glyph.width * BOX_WIDTH_FACTOR
    xmin = round((glyph.width - w) / 2)
    xmax = round(glyph.width - (glyph.width - w) / 2)
    return xmin, xmax

#-------------------------------------------------------------START

# glpyh.width * this = boxWidth
BOX_WIDTH_FACTOR = 0.9

fname = sys.argv[1]
name = fname.split(".")[0]
ext = fname.split(".")[1]
codeFont = fontforge.open(fname)
ymin,ymax = -codeFont.descent, codeFont.ascent

for g in codeFont.glyphs():
    xmin,xmax = getXBounds(g)
    #replaceWithCodeBox(g, (xmin,ymin,xmax,ymax))
codeFont.fontname = name + "-Code"
codeFont.generate(name + "-Code." + ext)
codeFont.close()

boxFont = fontforge.open(fname)
for g in boxFont.glyphs():
    xmin,xmax = getXBounds(g)
    replaceWithDirBox(g, (xmin,ymin,xmax,ymax))
boxFont.fontname = name + "-Box"
boxFont.generate(name + "-Box." + ext)
boxFont.close()
