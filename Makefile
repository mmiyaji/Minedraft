COMP=javac
EXEC=java
OUTFILE=Main
OUTDIR=bin
OUTPACKAGE=game
all:
	${COMP} src/game/*.java src/gui/*.java -d ${OUTDIR} #-classpath bin/
	cd ${OUTDIR}
	pwd
	${EXEC} ${OUTPACKAGE}.${OUTFILE} -classpath ${OUTDIR}/${OUTPACKAGE}
	cd ../
