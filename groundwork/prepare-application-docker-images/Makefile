.PHONY: default
default: stop start

.PHONY: start
start:
	- docker-compose up --build

.PHONY: stop
stop:
	- docker-compose down --remove-orphans

.PHONY: build
build:
	- cd e2e-sample-delivery && docker build -t e2e-sample-delivery .
	- cd e2e-sample-order && docker build -t e2e-sample-order .