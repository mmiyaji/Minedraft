TARGET=Main
JC=javac
EXEC=java
OUTFILE=${TARGET}
OUTDIR=bin
OUTPACKAGE=game

all:build
	cd ${OUTDIR}; ${EXEC} ${OUTPACKAGE}.${OUTFILE} -classpath ${OUTDIR}/${OUTPACKAGE}; cd ../
build:
	${JC} src/game/*.java src/gui/*.java -d ${OUTDIR} #-classpath bin/

clean:
	rm -rf ${OUTDIR}/${OUTPACKAGE}/$(TARGET).{log,class}

.PHONY: clean