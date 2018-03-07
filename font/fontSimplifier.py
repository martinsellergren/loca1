import fontforge
import sys
import unidecode
import unicodedata

# c: unicode character.
# return: True if c is letter (not symbol..).
def isLetter(c):
    return unicodedata.category(c) in ['Lu', 'Ll', 'Lt', 'Lm', 'Lo']

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
#  If LOWER_CASE is true, returns lower case chars,
#   otherwise UPPER case.
def simplify(l):
    cs = unicode(unidecode.unidecode(l))
    for c in cs:
        if isLetter(c):
            if LOWER_CASE:
                return c.lower()
            else:
                return c.upper()
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



LOWER_CASE = False

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

    if isLetter(c):
        c_simple = simplify(c)
        glyph_simple = findGlyph(c_simple, font)

        if glyph_simple is None:
            giveSpaceLayout(glyph)
        else:
            replaceLayout(glyph_simple, glyph)
    else:
        giveSpaceLayout(glyph)


if LOWER_CASE:
    font.fontname = name + "-Simple-lower"# + str(randint(0,1000000))
    font.generate(name + "-Simple-lower." + ext)
else:
    font.fontname = name + "-Simple-UPPER"
    font.generate(name + "-Simple-UPPER." + ext)
