USER_NAME = masel
TOKEN = sk.eyJ1IjoibWFzZWwiLCJhIjoiY2pkM2YwYXlvMncyZTMzczYzbXJ1Mzd3ayJ9.OBMENh5mWq6iwPZut8s0iw
STREETS_STYLE_ID = streets-v10
LABEL_TYPE_TABLE_PATH = ../app/labelTypeTable.json

FULL_STYLE_ID_LOCAL = cjf4gb4va1alg2rqkxtbld0bi
CODE_STYLE_ID_LOCAL = cjf4gbdzi154q2sk9g1xscdjv
BOX_STYLE_ID_LOCAL = cjf4gbk611afc2rnv12akrryr

FULL_STYLE_ID_EN = cjd9dx1oe9t712roe6qmxmfgr
CODE_STYLE_ID_EN = cjd9dxg7i84ev2snt9rudqcgg
BOX_STYLE_ID_EN = cjd9dxq0w9tpb2ss0ysjtiwlr





all: cleanAll fetch generate update

#generate styles from Streets.json
generate: clean
	python styleCreator.py Streets.json $(LABEL_TYPE_TABLE_PATH)

#remove generated
clean:
	rm -f full.json code.json box.json *.pyc

#also remove Streets.json
cleanAll: clean
	rm -f Streets.json

#fetch Streets.json
fetch:
	curl "https://api.mapbox.com/styles/v1/mapbox/$(STREETS_STYLE_ID)?access_token=$(TOKEN)" > Streets.json

#update styles (mirror local files (full.json, label.json, box.json) in mapbox-server)
update:
	curl -X PATCH "https://api.mapbox.com/styles/v1/$(USER_NAME)/$(FULL_STYLE_ID_LOCAL)?access_token=$(TOKEN)" --data @full-local.json --header "Content-Type:application/json"
	curl -X PATCH "https://api.mapbox.com/styles/v1/$(USER_NAME)/$(CODE_STYLE_ID_LOCAL)?access_token=$(TOKEN)" --data @code-local.json --header "Content-Type:application/json"
	curl -X PATCH "https://api.mapbox.com/styles/v1/$(USER_NAME)/$(BOX_STYLE_ID_LOCAL)?access_token=$(TOKEN)" --data @box-local.json --header "Content-Type:application/json"

	curl -X PATCH "https://api.mapbox.com/styles/v1/$(USER_NAME)/$(FULL_STYLE_ID_EN)?access_token=$(TOKEN)" --data @full-en.json --header "Content-Type:application/json"
	curl -X PATCH "https://api.mapbox.com/styles/v1/$(USER_NAME)/$(CODE_STYLE_ID_EN)?access_token=$(TOKEN)" --data @code-en.json --header "Content-Type:application/json"
	curl -X PATCH "https://api.mapbox.com/styles/v1/$(USER_NAME)/$(BOX_STYLE_ID_EN)?access_token=$(TOKEN)" --data @box-en.json --header "Content-Type:application/json"


# #create new styles (mirror local files (full.json, label.json, box.json) in mapbox-server)
# create:
# 	curl -X POST "https://api.mapbox.com/styles/v1/$(USER_NAME)?access_token=$(TOKEN)" --data @full.json --header "Content-Type:application/json" > /dev/null
# 	curl -X POST "https://api.mapbox.com/styles/v1/$(USER_NAME)?access_token=$(TOKEN)" --data @label.json --header "Content-Type:application/json" > /dev/null
# 	curl -X POST "https://api.mapbox.com/styles/v1/$(USER_NAME)?access_token=$(TOKEN)" --data @box.json --header "Content-Type:application/json" > /dev/null

info: generate
	python info.py full.json
