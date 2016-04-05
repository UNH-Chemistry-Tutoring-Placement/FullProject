JAVAC=javac

SOURCES := TutoringPlacementApplication.java FileIO/*.java LocalSearch/*.java Validator/*.java GUI/*.java

all:
	$(JAVAC) -d . $(SOURCES)

jar:
	jar -cmf manifest.mf TeamSchedulingTool.jar .