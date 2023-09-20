build-in-docker:
	docker run --rm -v $$PWD:/home/gradle/project -w /home/gradle/project gradle:8.3-jdk11 gradle clean jar

build:
	./gradlew jar

clean:
	./gradlew clean


.PHONY: build-in-docker build clean
