JAVAC=javac

SOURCES := TutoringPlacementApplication.java FileIO/*.java LocalSearch/*.java Validator/*.java

all:
	$(JAVAC) $(SOURCES)