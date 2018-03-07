import json
import sys


def getLayers(data, types):
    filtered = []
    for layer in data['layers']:
        if 'type' in layer and layer['type'] in types:
            filtered.append(layer)
    return filtered

def getSymbolLayers(data):
    return getLayers(data, ['symbol'])

def getNonSymbolLayers(data):
    return getLayers(data, ['fill', 'line', 'circle', 'heatmap', 'fill-extrusion', 'raster', 'background'])

def getLayersFromID(data, ids):
    filtered = []
    for layer in data['layers']:
        if 'id' in layer and layer['id'] in ids:
            filtered.append(layer)
    return filtered

def removeCreatedAndModifiedProps(data):
    if 'created' in data:
        del data['created']
    if 'modified' in data:
        del data['modified']

def setOwnerAndVisibility(data):
    data['owner'] = 'masel'
    data['visibility'] = 'private'

def setName(data, name):
    if noWrapping: name += '_noWrap'
    if noRotation: name += '_noRot'
    data['name'] = name

def setFont(data, font):
    data['glyphs'] = "mapbox://fonts/masel/{fontstack}/{range}.pbf"

    for layer in getSymbolLayers(data):
        layer['layout']['text-font'] = [font]


def setLetterSpacing(data, extraSpace=0):
    for layer in getSymbolLayers(data):
        space = extraSpace
        if 'text-letter-spacing' in layer['layout']:
            space += layer['layout']['text-letter-spacing']
        layer['layout']['text-letter-spacing'] = space

def setLineHeight(data, lineHeight):
    for layer in getSymbolLayers(data):
        layer['layout']['text-line-height'] = lineHeight


def setTextMaxAngle(data, maxAngle=45):
    for layer in getSymbolLayers(data):
        layer['layout']['text-max-angle'] = maxAngle

def setNoLabelWrapping(data):
    for layer in getSymbolLayers(data):
        layer['layout']['text-max-width'] = 1000000

def setNoLabelRotation(data):
    for layer in getSymbolLayers(data):
        layer['layout']['symbol-placement'] = 'point'

def setNoOverlap(data):
    for layer in getSymbolLayers(data):
        layer['layout']['text-allow-overlap'] = False

def setTextPadding(data, textPadding):
    for layer in getSymbolLayers(data):
        layer['layout']['text-padding'] = textPadding

def setLanguageAndFullNames(data):
    for layer in getSymbolLayers(data):
        layer['layout']['text-field'] = '{name_en}'

def noJunkLabels(data):
    for layer in getLayersFromID(data, ['road-shields-black',
                                        'road-shields-white',
                                        'motorway-junction',
                                        'housenum-label']):
        data['layers'].remove(layer)

def limitZoomOnPOI(data):
    for layer in getLayersFromID(data, ['poi-scalerank3',
                                        'poi-scalerank2',
                                        'poi-scalerank1',
                                        'poi-parks-scalerank3',
                                        'poi-parks-scalerank2',
                                        'poi-parks-scalerank1']):
        layer['minzoom'] = 8

def undecorateText(data):
    for layer in getSymbolLayers(data):
        paint = layer['paint']
        paint['icon-opacity'] = 0
        paint['text-opacity'] = 1
        paint['text-color'] = 'hsla(0, 0%, 0%, 1)'
        paint['text-halo-color'] = 'hsla(0, 0%, 0%, 0)'

def hideGraphics(data):
    for layer in getNonSymbolLayers(data):
        layer['layout']['visibility'] = 'none'

def dumpStyle(data, fileName):
    f = open(fileName + ".json", 'w')
    f.write(json.dumps(data, indent=4))
    f.close()


LOWER_CASE_FONT_IN_LABEL_IMG = True
LANGUAGE = 'name_en'
'''
name	The name (or names) used locally for the place.
name_ar	Arabic (if available, otherwise same as name)
name_en	English (if available, otherwise same as name)
name_es	Spanish (if available, otherwise same as name_en)
name_fr	French (if available, otherwise same as name_en)
name_de	German (if available, otherwise same as name_en)
name_pt	Portuguese (if available, otherwise same as name_en)
name_ru	Russian (if available, otherwise same as name)
name_zh	Chinese* (if available, otherwise same as name)
name_zh-Hans	Simplified Chinese* (if available, otherwise same as name)
'''
noWrapping = False
noRotation = False

font = 'Inconsolata Regular'#'Cousine Regular'
font_label = font + '-Simple-UPPER'
if LOWER_CASE_FONT_IN_LABEL_IMG:
    font_label = font + '-Simple-lower'
font_box = font_label + '-Box'
extraLetterSpace = 0.2
lineHeight = 1.5
textMaxAngle = 15
textPadding = 10#default 2

fileName_full = "full"
fileName_label = "label"
fileName_box = "box"

f = open(sys.argv[1], 'r')
data = json.load(f)

removeCreatedAndModifiedProps(data)
setOwnerAndVisibility(data)

setFont(data, font)
setLetterSpacing(data, extraLetterSpace)
setLineHeight(data, lineHeight)
setTextMaxAngle(data, textMaxAngle)
setNoOverlap(data)
setTextPadding(data, textPadding)

setLanguageAndFullNames(data)
noJunkLabels(data)
limitZoomOnPOI(data)

#experiment
#setNoLabelWrapping(data)
#setNoLabelRotation(data)

setName(data, fileName_full)
dumpStyle(data, fileName_full)

undecorateText(data)
hideGraphics(data)
setFont(data, font_label)
setName(data, fileName_label)
dumpStyle(data, fileName_label)

setFont(data, font_box)
setName(data, fileName_box)
dumpStyle(data, fileName_box)
