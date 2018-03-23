import json
import sys
from styleCreator import getLabelLayers

data = json.load(open(sys.argv[1], 'r'))

for layer in getLabelLayers(data):
    print '{} min:{} max:{}'.format(layer.get('source-layer'),
                                    layer.get('minzoom'),
                                    layer.get('maxzoom'))
