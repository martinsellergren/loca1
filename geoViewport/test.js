var SphericalMercator = (function(){

// SphericalMercator constructor
function SphericalMercator(options) {
    var size = options.size;
    Bc = [];
    Cc = [];
    zc = [];
    Ac = [];
    for (var d = 0; d < 30; d++) {
        Bc.push(size / 360);
        Cc.push(size / (2 * Math.PI));
        zc.push(size / 2);
        Ac.push(size);
        size *= 2;
    }

    this.Bc = Bc;
    this.Cc = Cc;
    this.zc = zc;
    this.Ac = Ac;
};

// Convert lon lat to screen pixel value
//
// - `ll` {Array} `[lon, lat]` array of geographic coordinates.
// - `zoom` {Number} zoom level.
SphericalMercator.prototype.px = function(ll, zoom) {
    var d = this.zc[zoom];
    var f = Math.min(Math.max(Math.sin(Math.PI/180 * ll[1]), -0.9999), 0.9999);
    var x = Math.round(d + ll[0] * this.Bc[zoom]);
    var y = Math.round(d + 0.5 * Math.log((1 + f) / (1 - f)) * (-this.Cc[zoom]));
    (x > this.Ac[zoom]) && (x = this.Ac[zoom]);
    (y > this.Ac[zoom]) && (y = this.Ac[zoom]);
    //(x < 0) && (x = 0);
    //(y < 0) && (y = 0);
    return [x, y];
};

// Convert screen pixel value to lon lat
//
// - `px` {Array} `[x, y]` array of geographic coordinates.
// - `zoom` {Number} zoom level.
SphericalMercator.prototype.ll = function(px, zoom) {
    var g = (px[1] - this.zc[zoom]) / (-this.Cc[zoom]);
    var lon = (px[0] - this.zc[zoom]) / this.Bc[zoom];
    var lat = 180/Math.PI * (2 * Math.atan(Math.exp(g)) - 0.5 * Math.PI);
    return [lon, lat];
};

return SphericalMercator;

})();


function bounds(viewport, zoom, dimensions, tileSize) {
    var merc = new SphericalMercator({ size: tileSize });
    var px = merc.px(viewport, zoom);
    var tl = merc.ll([
        px[0] - (dimensions[0] / 2),
        px[1] - (dimensions[1] / 2)
    ], zoom);
    var br = merc.ll([
        px[0] + (dimensions[0] / 2),
        px[1] + (dimensions[1] / 2)
    ], zoom);
    return [tl[0], br[1], br[0], tl[1]];
}

//var merc = new SphericalMercator({ size: 256 });
//document.write(merc.Bc, merc.Cc, merc.zc, merc.Ac);
var b = bounds([-75.03, 35.25], 14, [600, 400], 256);
var width = Math.abs(b[0]-b[2]);
var height = Math.abs(b[1]-b[3]);
document.write(width, ", ", height);
