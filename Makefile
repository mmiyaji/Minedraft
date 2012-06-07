COMP=javac
EXEC=java
OUTFILE=Main

all:
	${COMP} src/*.java src/gui/*.java
	mv src/Main.class bin/
	# cd bin/
	# ${EXEC} ${OUTFILE}
	# cd ../
