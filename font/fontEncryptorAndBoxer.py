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

Code-box contains a binary code. Dir-box contains a box shaped as the
symbol [ .
'''

import fontforge
import sys
import unicodedata
import json

# c: unicode character.
# return: True if c is a letter (not symbol..).
def isLetter(c):
    return unicodedata.category(c) in ['Lu', 'Ll', 'Lt', 'Lm', 'Lo']

# c: unicode character.
# return: True if c is a digit.
def isDigit(c):
    return unicodedata.category(c) in ['Nd', 'Nl', 'No']

#uni: int representing a unicode code-point
#return: unicode char, A->a, symbol->space
def getAppropriateChar(uni):
    if uni < 0: return u' '
    c = unichr(uni)
    if isLetter(c): return c.lower()
    if isDigit(c): return c
    else: return u' '

#char: unicode char
def getAndSetMapping(char):
    cp = ord(char)
    if cp in MAPPINGS:
        return MAPPINGS.index(cp)
    else:
        MAPPINGS.append(cp)
        return len(MAPPINGS) - 1

# draws a block (a box) at specified row/col, where bbs is devided into
# CODE_BOX_ROWS rows and CODE_BOX_COLS columns.
#r: row
#c: col
#bbs: [xmin ymin xmax ymax] of box that drawn block is a part of.
def drawBlock(r, c, bbs, pen):
    w = (bbs[2] - bbs[0]) / float(CODE_BOX_COLS)
    h = (bbs[3] - bbs[1]) / float(CODE_BOX_ROWS)
    x1 = bbs[0] + w*c
    y1 = bbs[3] - h*r
    x2 = x1 + w
    y2 = y1 - h
    pen.moveTo((x1,y1))
    pen.lineTo((x1,y2))
    pen.lineTo((x2,y2))
    pen.lineTo((x2,y1))
    pen.closePath()

#number: an int to be encoded in glyph
#bbs: box-bounds
def encodeNumberInGlyph(number, glyph, bbs):
    temp_glyphW = glyph.width
    glyph.clear()
    pen = glyph.glyphPen()

    binary = format(number, 'b')
    if len(binary) > CODE_BOX_ROWS * CODE_BOX_COLS:
        print "Binary dosn't fit in grid-layout!"
        sys.exit(1)

    for i in range(len(binary)):
        r = i / 3
        c = i % 3
        d = binary[-(i+1)]
        if d == '1':
            drawBlock(r, c, bbs, pen)
    pen = None
    glyph.width = temp_glyphW


#also updates mappingTable
#bbs: [xmin ymin xmax ymax] of codeBox.
def replaceWithCodeBox(glyph, bbs):
    char = getAppropriateChar(glyph.unicode)
    mapping = getAndSetMapping(char)
    encodeNumberInGlyph(mapping, glyph, bbs)

#bbs: [xmin ymin xmax ymax] of dirBox
def replaceWithDirBox(glyph, bbs):
    x1,y1,x2,y2 = bbs
    x12 = x1 + (x2-x1) / 2
    y11 = y1 + (y2-y1) / 3
    y12 = y2 - (y2-y1) / 3

    temp_glyphW = glyph.width
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

def saveMappings():
    f = open(MAPPINGS_SAVE_PATH, 'w')
    f.write(json.dumps(MAPPINGS, indent=4))
    f.close()

#-------------------------------------------------------------START

CODE_BOX_ROWS = 4
CODE_BOX_COLS = 3

#maps index to unicode
MAPPINGS = []
MAPPINGS_SAVE_PATH = '../app/codeFontMappings.json'

# glpyh.width * this = boxWidth
BOX_WIDTH_FACTOR = 0.9

fname = sys.argv[1]
name = fname.split(".")[0]
ext = fname.split(".")[1]
codeFont = fontforge.open(fname)
ymin,ymax = -codeFont.descent, codeFont.ascent

for g in codeFont.glyphs():
    xmin,xmax = getXBounds(g)
    replaceWithCodeBox(g, (xmin,ymin,xmax,ymax))

    # temp_glyphW = g.width
    # g.clear()
    # pen = g.glyphPen()
    # drawBlock(3, 2, (xmin,ymin,xmax,ymax), pen)
    # pen = None
    # g.width = temp_glyphW

codeFont.fontname = name + "-Code"
codeFont.generate(name + "-Code." + ext)
codeFont.close()
saveMappings()

boxFont = fontforge.open(fname)
for g in boxFont.glyphs():
    xmin,xmax = getXBounds(g)
    replaceWithDirBox(g, (xmin,ymin,xmax,ymax))
boxFont.fontname = name + "-Box"
boxFont.generate(name + "-Box." + ext)
boxFont.close()
