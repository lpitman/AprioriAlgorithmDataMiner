JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        AssociationRuleMining.java \
        AVList.java \
        DataBase.java \
        Item.java \
				Itemset.java \
				KItemset.java \
				Rule.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
