# loca-studies

Utils, static
    fetchMapImges(mapBasics) -> MapImage[]
        [0] - fullMapImg
        [1] - labelsImg
        [2] - letterBoxesImg

        fetchRawImage(max dim 1280*1280) -> BufferedImage
        fetchMapImage(mapBasics)
            mergeRawImages()..

MapImage
    new MapImage(BufferedImage)
    rotate(deg) -> MapImage
    extract(box) -> MapImage
    detectText(labelLayout)::string

BoxImage
    new BoxImage(letterBoxesImg::BufferedImage)
    clone()::BoxImage

    inner:
    LabelLayoutIter
        next()::LabelLayout
        hasMore()::Boolean(sets next)

        findBoxedPoint()::Point/NULL
        findNextBoxPoint(LEFT/RIGHT, startBox::Box)::Point/NULL
        findNextRowPoint(UP/DOWN, startRow:Box[])::Point/NULL
        expandToRow(boxed-Point)::Box[]
        addBoxes(LEFT/RIGHT, startBox::Box, accum(contains startBox)::Box[])::Box[]
        addRows(UP/DOWN, startRow::Box[], labelLayout(accumulator, contains startRow))::LabelLayout
        turnOffBoxedPoints(boxes::box[])::Void

        expandToBox(boxed-Point)::Box
            expandToBoxedPoints(boxed-Point)::Point[]
            getCorners(points::Point[])::Point[](0Left 1Up 2Right 3Down)
                getExtremes(Point[])::Point[](0Left 1Up 2Right 3Down)
                rotate(Point[], deg)::Point[]

MapBasics
    new MapBasics(x,y,w,h,z)
    pos
    dim
    zoom

Label
    new Label(text, layout)
    new Label(text, layout, category)
    text: String
    category: enum
    layout: LabelLayout
    fetchCategory()::category enum

    static:
    fetchCategory(text::string)::category enum

Labels
    labels Label[]
    new Labels(labelsImg, letterBoxesImg)
        auxBImg = letterBoxesImg.clone() #iter edits img
        iter = new auxBImg.LabelLayoutIter()
            labelsImg.extract(box..)
            ...

LabelLayout
    new LabelLayout(row::Box[])
    letterBoxes: Box[row][pos in row]
    addRowFirst(row::Box[])
    addRowLast(row::Box[])
    getBoxes()::Box[] (1d)
    getBox(x,y)

Box
    left,right,up,down-pos
    getRotation() -> float

Map(MapBasics)
    new Map(mapBasics, mapImg::MapImage, labelsImg(null?)::MapImage, letterBoxesImg::BoxImage)
    new Map(mapBasics, imgs::BufferedImage[]3pst)
    mapImg::MapImage
    labels::Labels
    createQuestionMap() -> QuestionMap









Example:
mapBasics = new MapBasics(5345,34543,1280,1277,14)
BufferedImage[] imgs = fetchMapImages(mapBasics)
mapImg = new MapImage(imgs[0])
labelsImg = new MapImage(imgs[1])
boxImg = new BoxImage(imgs[2])
map = new Map(mapBasics, mapImg, labelsImg, boxImg)


labels = new Labels(labelImg, boxImg):
    labels = []
    auxBImg = boxImg.clone()
    iter = new auxBImg.LabelLayoutIter()
    while (iter.hasMore())
        layout = iter.next()
        text = labelImg.detectText(layout)
        category = Label.fetchCategory(text)
        label = new Label(layout, text, category)
        labels.append(label)


iter = new auxBImg.LabelLayoutIter()
    next = NULL
    getNext() return next
    hasMore()::LabelLayout:
        p = findBoxedPoint()
        if p is NULL:
            next = NULL
            return FALSE

        startRow = expandToRow(p)
        layout = new LabelLayout(startRow)
        addRows(UP, startRow, layout)
        addRows(DOWN, startRow, layout)
        turnOffBoxedPoints(layout.getBoxes())
        next = layout
        return TRUE
