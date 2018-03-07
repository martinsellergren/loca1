FONT = Inconsolata-Regular

all: clean
	python fontSimplifier.py $(FONT).ttf
	find . -maxdepth 1 -name "*Simple*" -exec basename {} \; | xargs -tI{} python fontBoxer.py {} $(FONT).ttf

clean:
	rm -f *-Simple*
	rm -f *-Box*
