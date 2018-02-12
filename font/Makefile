FONT = Inconsolata-Regular.ttf

all: clean simplify box

simplify:
	python fontSimplifier.py $(FONT)

box:
	python fontBoxer.py $(basename $(FONT))-Simple$(suffix $(FONT))

clean:
	rm -f *-Simple*
	rm -f *-Box*
