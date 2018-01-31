USER_NAME = "masel"
TOKEN = sk.eyJ1IjoibWFzZWwiLCJhIjoiY2pkM2YwYXlvMncyZTMzczYzbXJ1Mzd3ayJ9.OBMENh5mWq6iwPZut8s0iw
STREETS_STYLE_ID = streets-v10
FULL_STYLE_ID = cjd3drfc848nz2tmoos1fysia
LABEL_STYLE_ID = cjd3drv7d48cu2ss0oos7mcsb
BOX_STYLE_ID = cjd3ds6gu48jy2skaohiit3hd

all: cleanAll fetch generate update

#generate styles from Streets.json
generate: clean
	python styleCreator.py Streets.json

#remove generated
clean:
	rm -f full.json label.json box.json

#also remove Streets.json
cleanAll: clean
	rm -f Streets.json

#fetch Streets.json
fetch:
	curl "https://api.mapbox.com/styles/v1/mapbox/$(STREETS_STYLE_ID)?access_token=$(TOKEN)" > Streets.json

#mirror local files (full.json, label.json, box.json) in mapbox-server
update:
	curl -X PATCH "https://api.mapbox.com/styles/v1/$(USER_NAME)/$(FULL_STYLE_ID)?access_token=$(TOKEN)" --data @full.json --header "Content-Type:application/json" > /dev/null
	curl -X PATCH "https://api.mapbox.com/styles/v1/$(USER_NAME)/$(LABEL_STYLE_ID)?access_token=$(TOKEN)" --data @label.json --header "Content-Type:application/json" > /dev/null
	curl -X PATCH "https://api.mapbox.com/styles/v1/$(USER_NAME)/$(BOX_STYLE_ID)?access_token=$(TOKEN)" --data @box.json --header "Content-Type:application/json" > /dev/null
