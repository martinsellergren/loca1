FONT = Inconsolata-Regular.ttf

all: clean simplify box

simplify:
	python fontSimplifier.py $(FONT)

box:
	python fontBoxer.py $(FONT)

clean:
	rm -f *-Simple*
	rm -f *-Box*
