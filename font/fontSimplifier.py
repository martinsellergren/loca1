import fontforge
import sys
import unidecode
import unicodedata

# c: unicode character.
# return: True if c is a letter (not symbol..).
def isLetter(c):
    return unicodedata.category(c) in ['Lu', 'Ll', 'Lt', 'Lm', 'Lo']

# c: unicode character.
# return: True if c is a number, 1-9.
def isDigit(c):
    return unicodedata.category(c) in ['Nd']

# Replace layout of dest with layout of src, preserving dims.
# src: Source glyph.
# dest: Destination glyph.
def replaceLayout(src, dest):
    if src.unicode == dest.unicode: return
    w = dest.width
    dest.clear()
    dest.background = src.background.dup()
    dest.foreground = src.foreground.dup()
    dest.width = w

# Update layout of glyph, remove all.
# g: Glyph.
def giveSpaceLayout(g):
    w = g.width
    g.clear()
    g.width = w

# l: Unicode letter.
# return: Simplified unicode character (i.e ae->a).
def simplify(l):
    cs = unicode(unidecode.unidecode(l))
    for c in cs:
        if isLetter(c) or isDigit(c):
            return c.lower()
    return None

# c: Unicode character.
# font: Font with glyphs.
# return: Glyph in font of the c-character.
def findGlyph(c, font):
    if c is None: return None
    index = ord(c)
    for g in font.glyphs():
        if g.unicode == index: return g
    return None

def unlinkReferences(font):
    for g in font.glyphs():
        g.unlinkRef()



fname = sys.argv[1]
x = fname.split(".")
name = x[0]
ext = x[1]
font = fontforge.open(fname)
unlinkReferences(font)

for glyph in font.glyphs():
    if glyph.unicode == -1:
        giveSpaceLayout(glyph);
        continue;

    c = unichr(glyph.unicode)

    if isLetter(c) or isDigit(c):
        c_simple = simplify(c)
        glyph_simple = findGlyph(c_simple, font)

        if glyph_simple is None:
            giveSpaceLayout(glyph)
        else:
            replaceLayout(glyph_simple, glyph)
    else:
        giveSpaceLayout(glyph)


font.fontname = name + "-Simple"# + str(randint(0,1000000))
font.generate(name + "-Simple." + ext)
