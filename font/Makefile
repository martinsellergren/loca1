FONT = Inconsolata-Regular

all: clean
	python fontSimplifier.py $(FONT).ttf
	find . -maxdepth 1 -name "*Simple*" -exec basename {} \; | xargs -I{} python fontBoxer.py {} $(FONT)

clean:
	rm -f *-Simple*
	rm -f *-Box*
