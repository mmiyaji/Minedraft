TARGET=Main
JC=javac
EXEC=java
OUTFILE=${TARGET}
OUTDIR=bin
OUTPACKAGE=game
NATIVE=native
all:build
	export LD_LIBRARY_PATH="native"; cd ${OUTDIR}; ${EXEC} -Djava.library.path=../native:${NATIVE} ${OUTPACKAGE}.${OUTFILE} -classpath ${OUTDIR}/${OUTPACKAGE}:bin/:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/slick.jar:lib/PNGDecoder.jar; cd ../
nw:build
	export LD_LIBRARY_PATH="native"; cd ${OUTDIR}; ${EXEC} -Djava.library.path=../native:${NATIVE} ${OUTPACKAGE}.${OUTFILE} nw -classpath ${OUTDIR}/${OUTPACKAGE}:bin/:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/slick.jar:lib/PNGDecoder.jar; cd ../
build:
	mkdir -p ${OUTDIR}
	${JC} src/game/*.java src/gui/*.java -d ${OUTDIR} -classpath bin/:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/slick.jar:lib/PNGDecoder.jar

clean:
	rm -rf ${OUTDIR}/${OUTPACKAGE}/$(TARGET).{log,class}

.PHONY: clean