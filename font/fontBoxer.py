'''
usage:
python fontBoxer.py font1.ttf font2.ttf

Creates a font called fontName-Box.ttf.
Each letter in src becomes a "box" in dest. The box is formed like
the symbol [.
The box is sized and positioned so that corresponding letter
just barely fits inside. If a character is very wide the
box-width is thinner than it's underlying character.
The box-height is fixed, set to tallest character height of font2.

font1.ttf is the font that will be boxed.
font2.ttf only determines character height.

Properties of dest when the font is used:
 * No boxes touch (with safety margin).
 * Space between boxes is always less than the width of any box.
'''

import fontforge
import sys
import pdb
from random import randint

def getCharExtremeDims(font):
    wMax = -1
    hMax = -1
    for glyph in font.glyphs():
        (x1,y1,x2,y2) = glyph.boundingBox()
        (w, h) = (x2 - x1, y2 - y1)

        if w > wMax:
            wMax = w
        if h > hMax:
            hMax = h
    return (wMax, hMax)

def getCharExtremePos(font):
    xmin = 999999999
    ymin = 999999999
    xmax = -999999999
    ymax = -999999999
    for glyph in font.glyphs():
        (x1,y1,x2,y2) = glyph.boundingBox()
        if x1 < xmin: xmin = x1
        if x2 > xmax: xmax = x2
        if y1 < ymin: ymin = y1
        if y2 > ymax: ymax = y2
    return xmin,ymin,xmax,ymax


def expandW(x1,x2):
    if (x1,x2) == (0,0):
        x1 = (fontW - minBoxW) / 2
        x2 = x1 + minBoxW
        return x1, x2

    w = x2-x1
    diff = minBoxW - w
    x1_, x2_ = x1 - diff/2, x2 + diff/2
    return x1_, x2_

def shrinkW(x1,x2):
    if x1 < 0: x1 = 0
    if x2 > maxBoxW: x2 = maxBoxW
    return x1, x2

def expandH(y1,y2):
    if (y1,y2) == (0,0):
        y1 = (fontH - minBoxH) / 2
        y2 = y1 + minBoxH
        return y1, y2

    h = y2-y1
    diff = minBoxH - h

    if y1-diff/2 >= font.upos and y2+diff/2 <= maxBoxH:
        return y1-diff/2, y2+diff/2

    if y1-diff >= font.upos: y1 = y1-diff
    else: y2 = y2+diff
    return y1, y2



fname = sys.argv[1]
fname2 = sys.argv[2]
x = fname.split(".")
name = x[0]
ext = x[1]

font = fontforge.open(fname)
fontW, fontH = next(font.glyphs()).width, font.ascent
_, maxCharH = getCharExtremeDims(fontforge.open(fname2))
maxBoxW, maxBoxH = round(fontW*0.9), maxCharH
minBoxW, minBoxH = round(fontW/2 * 1.3), round(maxBoxH*0.7)
xmin,ymin,xmax,ymax = getCharExtremePos(font)
fixedHeightMinimizingFactor = 0


for glyph in font.glyphs():
    glyphW = glyph.width
    x1,y1,x2,y2 = glyph.boundingBox()
    boxW, boxH = x2-x1, y2-y1

    if boxW < minBoxW: x1,x2 = expandW(x1,x2)
    elif boxW > maxBoxW: x1,x2 = shrinkW(x1,x2)
    if boxH < minBoxH: y1,y2 = expandH(y1,y2)
    elif boxH > maxBoxH: sys.exit(-1) #never happens

    #fixed height!
    y1 = ymin + maxCharH*fixedHeightMinimizingFactor
    y2 = ymax - maxCharH*fixedHeightMinimizingFactor

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
    glyph.width = glyphW

font.fontname = name + "-Box"# + str(randint(0,1000000))
font.generate(name + "-Box." + ext)
