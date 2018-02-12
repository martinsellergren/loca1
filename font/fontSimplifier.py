import fontforge
import sys
import unidecode
import unicodedata

# c: unicode character.
# return: True if c is letter (not symbol..).
def isLetter(c):
    return False

# Replace layout of dest with layout of src, preserving dims.
# src: Source glyph.
# dest: Destination glyph.
def replaceLayout(src, dest):
    pass

# Update layout of glyph, remove all.
# g: Glyph.
def giveSpaceLayout(g):
    pass

# c: Unicode character.
# return: Simplified unicode character (i.e ae->a).
def simplify(c):
    cs = unicode(unidecode.unidecode(c))
    for c in cs:
        if isLetter(c): return c
    sys.exit(-1)

# c: Unicode character.
# font: Font with glyphs.
# return: Glyph in font of the c-character.
def findGlyph(c, font):
    index = ord(c)
    return None



fname = sys.argv[1]
x = fname.split(".")
name = x[0]
ext = x[1]
font = fontforge.open(fname)

for glyph in font.glyphs():
    if glyph.unicode == -1:
        giveSpaceLayout(glyph);
        continue;

    c = unichr(glyph.unicode)

    if isLetter(c):
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
