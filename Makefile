all: resources/node_modules
resources/node_modules: resources/package-lock.json
	cd resources/ && npm install
