TARGET=Main
JC=javac
EXEC=java
OUTFILE=${TARGET}
OUTDIR=bin
OUTPACKAGE=game
NATIVE=native
all:build
	cd ${OUTDIR}; ${EXEC} -Djava.library.path=${NATIVE} ${OUTPACKAGE}.${OUTFILE} -classpath ${OUTDIR}/${OUTPACKAGE}; cd ../
build:
	mkdir -p ${OUTDIR}
	${JC} src/game/*.java src/gui/*.java -d ${OUTDIR} -classpath bin/

clean:
	rm -rf ${OUTDIR}/${OUTPACKAGE}/$(TARGET).{log,class}

.PHONY: clean