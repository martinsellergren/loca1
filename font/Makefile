FONT = RobotoMono-Regular.ttf

all: clean
	python fontEncryptorAndBoxer.py $(FONT)

# all: clean
# 	python fontSimplifier.py $(FONT).ttf
# 	find . -maxdepth 1 -name "*Simple*" -exec basename {} \; | xargs -tI{} python fontBoxer.py {} $(FONT).ttf

clean:
	rm -f *-Code*
	rm -f *-Box*
