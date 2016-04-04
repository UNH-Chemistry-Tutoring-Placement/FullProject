JAVAC=javac

SOURCES := TutoringPlacementApplication.java FileIO/*.java LocalSearch/*.java Validator/*.java GUI/*.java

all:
	$(JAVAC) $(SOURCES)