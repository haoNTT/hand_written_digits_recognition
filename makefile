JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Knn.java\
	Regression.java\
	DataSet.java\
	Distance.java\
	RRModel.java\
	nnregression

default: knn

regression:
	java Regression.java

nnregression:
	java nnregression.java

knn: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
