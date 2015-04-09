CC=goto-cc
CFLAGS=

SRC=adpcm.c blit.c crc.c fir.c huff.c pocsag.c ucbqsort.c bcnt.c compress.c engine.c g3fax.c jpeg.c qurt.c v42.c
OBJ:=$(patsubst %.c, %, $(SRC))

.PHONY: all

.c.o:
	$(CC) $(CFLAGS) $*.c -o $*

all: $(OBJ)
	tar cfz powerstone.tar.gz $(OBJ)

clean:
	rm -rf $(OBJ) powerstone.tar.gz
