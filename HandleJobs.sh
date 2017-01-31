javac Bwt.java

JOBCOUNT=$1;
BLOCKSIZE=$2;
BITLENGTH=$3;
WORDCOUNT=$4;

for((i = 0 ; i < JOBCOUNT ; i++))
do
	java Bwt "$BLOCKSIZE" "$BITLENGTH" "$WORDCOUNT"
done
