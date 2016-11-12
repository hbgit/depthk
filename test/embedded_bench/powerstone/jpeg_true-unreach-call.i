# 1 "jpeg_true-unreach-call.c"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "jpeg_true-unreach-call.c"

# 1 "/usr/include/stdio.h" 1 3 4
# 27 "/usr/include/stdio.h" 3 4
# 1 "/usr/include/features.h" 1 3 4
# 374 "/usr/include/features.h" 3 4
# 1 "/usr/include/x86_64-linux-gnu/sys/cdefs.h" 1 3 4
# 385 "/usr/include/x86_64-linux-gnu/sys/cdefs.h" 3 4
# 1 "/usr/include/x86_64-linux-gnu/bits/wordsize.h" 1 3 4
# 386 "/usr/include/x86_64-linux-gnu/sys/cdefs.h" 2 3 4
# 375 "/usr/include/features.h" 2 3 4
# 398 "/usr/include/features.h" 3 4
# 1 "/usr/include/x86_64-linux-gnu/gnu/stubs.h" 1 3 4
# 10 "/usr/include/x86_64-linux-gnu/gnu/stubs.h" 3 4
# 1 "/usr/include/x86_64-linux-gnu/gnu/stubs-64.h" 1 3 4
# 11 "/usr/include/x86_64-linux-gnu/gnu/stubs.h" 2 3 4
# 399 "/usr/include/features.h" 2 3 4
# 28 "/usr/include/stdio.h" 2 3 4





# 1 "/usr/lib/gcc/x86_64-linux-gnu/4.8/include/stddef.h" 1 3 4
# 212 "/usr/lib/gcc/x86_64-linux-gnu/4.8/include/stddef.h" 3 4
typedef long unsigned int size_t;
# 34 "/usr/include/stdio.h" 2 3 4

# 1 "/usr/include/x86_64-linux-gnu/bits/types.h" 1 3 4
# 27 "/usr/include/x86_64-linux-gnu/bits/types.h" 3 4
# 1 "/usr/include/x86_64-linux-gnu/bits/wordsize.h" 1 3 4
# 28 "/usr/include/x86_64-linux-gnu/bits/types.h" 2 3 4


typedef unsigned char __u_char;
typedef unsigned short int __u_short;
typedef unsigned int __u_int;
typedef unsigned long int __u_long;


typedef signed char __int8_t;
typedef unsigned char __uint8_t;
typedef signed short int __int16_t;
typedef unsigned short int __uint16_t;
typedef signed int __int32_t;
typedef unsigned int __uint32_t;

typedef signed long int __int64_t;
typedef unsigned long int __uint64_t;







typedef long int __quad_t;
typedef unsigned long int __u_quad_t;
# 121 "/usr/include/x86_64-linux-gnu/bits/types.h" 3 4
# 1 "/usr/include/x86_64-linux-gnu/bits/typesizes.h" 1 3 4
# 122 "/usr/include/x86_64-linux-gnu/bits/types.h" 2 3 4


typedef unsigned long int __dev_t;
typedef unsigned int __uid_t;
typedef unsigned int __gid_t;
typedef unsigned long int __ino_t;
typedef unsigned long int __ino64_t;
typedef unsigned int __mode_t;
typedef unsigned long int __nlink_t;
typedef long int __off_t;
typedef long int __off64_t;
typedef int __pid_t;
typedef struct { int __val[2]; } __fsid_t;
typedef long int __clock_t;
typedef unsigned long int __rlim_t;
typedef unsigned long int __rlim64_t;
typedef unsigned int __id_t;
typedef long int __time_t;
typedef unsigned int __useconds_t;
typedef long int __suseconds_t;

typedef int __daddr_t;
typedef int __key_t;


typedef int __clockid_t;


typedef void * __timer_t;


typedef long int __blksize_t;




typedef long int __blkcnt_t;
typedef long int __blkcnt64_t;


typedef unsigned long int __fsblkcnt_t;
typedef unsigned long int __fsblkcnt64_t;


typedef unsigned long int __fsfilcnt_t;
typedef unsigned long int __fsfilcnt64_t;


typedef long int __fsword_t;

typedef long int __ssize_t;


typedef long int __syscall_slong_t;

typedef unsigned long int __syscall_ulong_t;



typedef __off64_t __loff_t;
typedef __quad_t *__qaddr_t;
typedef char *__caddr_t;


typedef long int __intptr_t;


typedef unsigned int __socklen_t;
# 36 "/usr/include/stdio.h" 2 3 4
# 44 "/usr/include/stdio.h" 3 4
struct _IO_FILE;



typedef struct _IO_FILE FILE;





# 64 "/usr/include/stdio.h" 3 4
typedef struct _IO_FILE __FILE;
# 74 "/usr/include/stdio.h" 3 4
# 1 "/usr/include/libio.h" 1 3 4
# 31 "/usr/include/libio.h" 3 4
# 1 "/usr/include/_G_config.h" 1 3 4
# 15 "/usr/include/_G_config.h" 3 4
# 1 "/usr/lib/gcc/x86_64-linux-gnu/4.8/include/stddef.h" 1 3 4
# 16 "/usr/include/_G_config.h" 2 3 4




# 1 "/usr/include/wchar.h" 1 3 4
# 82 "/usr/include/wchar.h" 3 4
typedef struct
{
  int __count;
  union
  {

    unsigned int __wch;



    char __wchb[4];
  } __value;
} __mbstate_t;
# 21 "/usr/include/_G_config.h" 2 3 4
typedef struct
{
  __off_t __pos;
  __mbstate_t __state;
} _G_fpos_t;
typedef struct
{
  __off64_t __pos;
  __mbstate_t __state;
} _G_fpos64_t;
# 32 "/usr/include/libio.h" 2 3 4
# 49 "/usr/include/libio.h" 3 4
# 1 "/usr/lib/gcc/x86_64-linux-gnu/4.8/include/stdarg.h" 1 3 4
# 40 "/usr/lib/gcc/x86_64-linux-gnu/4.8/include/stdarg.h" 3 4
typedef __builtin_va_list __gnuc_va_list;
# 50 "/usr/include/libio.h" 2 3 4
# 144 "/usr/include/libio.h" 3 4
struct _IO_jump_t; struct _IO_FILE;
# 154 "/usr/include/libio.h" 3 4
typedef void _IO_lock_t;





struct _IO_marker {
  struct _IO_marker *_next;
  struct _IO_FILE *_sbuf;



  int _pos;
# 177 "/usr/include/libio.h" 3 4
};


enum __codecvt_result
{
  __codecvt_ok,
  __codecvt_partial,
  __codecvt_error,
  __codecvt_noconv
};
# 245 "/usr/include/libio.h" 3 4
struct _IO_FILE {
  int _flags;




  char* _IO_read_ptr;
  char* _IO_read_end;
  char* _IO_read_base;
  char* _IO_write_base;
  char* _IO_write_ptr;
  char* _IO_write_end;
  char* _IO_buf_base;
  char* _IO_buf_end;

  char *_IO_save_base;
  char *_IO_backup_base;
  char *_IO_save_end;

  struct _IO_marker *_markers;

  struct _IO_FILE *_chain;

  int _fileno;



  int _flags2;

  __off_t _old_offset;



  unsigned short _cur_column;
  signed char _vtable_offset;
  char _shortbuf[1];



  _IO_lock_t *_lock;
# 293 "/usr/include/libio.h" 3 4
  __off64_t _offset;
# 302 "/usr/include/libio.h" 3 4
  void *__pad1;
  void *__pad2;
  void *__pad3;
  void *__pad4;
  size_t __pad5;

  int _mode;

  char _unused2[15 * sizeof (int) - 4 * sizeof (void *) - sizeof (size_t)];

};


typedef struct _IO_FILE _IO_FILE;


struct _IO_FILE_plus;

extern struct _IO_FILE_plus _IO_2_1_stdin_;
extern struct _IO_FILE_plus _IO_2_1_stdout_;
extern struct _IO_FILE_plus _IO_2_1_stderr_;
# 338 "/usr/include/libio.h" 3 4
typedef __ssize_t __io_read_fn (void *__cookie, char *__buf, size_t __nbytes);







typedef __ssize_t __io_write_fn (void *__cookie, const char *__buf,
     size_t __n);







typedef int __io_seek_fn (void *__cookie, __off64_t *__pos, int __w);


typedef int __io_close_fn (void *__cookie);
# 390 "/usr/include/libio.h" 3 4
extern int __underflow (_IO_FILE *);
extern int __uflow (_IO_FILE *);
extern int __overflow (_IO_FILE *, int);
# 434 "/usr/include/libio.h" 3 4
extern int _IO_getc (_IO_FILE *__fp);
extern int _IO_putc (int __c, _IO_FILE *__fp);
extern int _IO_feof (_IO_FILE *__fp) __attribute__ ((__nothrow__ , __leaf__));
extern int _IO_ferror (_IO_FILE *__fp) __attribute__ ((__nothrow__ , __leaf__));

extern int _IO_peekc_locked (_IO_FILE *__fp);





extern void _IO_flockfile (_IO_FILE *) __attribute__ ((__nothrow__ , __leaf__));
extern void _IO_funlockfile (_IO_FILE *) __attribute__ ((__nothrow__ , __leaf__));
extern int _IO_ftrylockfile (_IO_FILE *) __attribute__ ((__nothrow__ , __leaf__));
# 464 "/usr/include/libio.h" 3 4
extern int _IO_vfscanf (_IO_FILE * __restrict, const char * __restrict,
   __gnuc_va_list, int *__restrict);
extern int _IO_vfprintf (_IO_FILE *__restrict, const char *__restrict,
    __gnuc_va_list);
extern __ssize_t _IO_padn (_IO_FILE *, int, __ssize_t);
extern size_t _IO_sgetn (_IO_FILE *, void *, size_t);

extern __off64_t _IO_seekoff (_IO_FILE *, __off64_t, int, int);
extern __off64_t _IO_seekpos (_IO_FILE *, __off64_t, int);

extern void _IO_free_backup_area (_IO_FILE *) __attribute__ ((__nothrow__ , __leaf__));
# 75 "/usr/include/stdio.h" 2 3 4




typedef __gnuc_va_list va_list;
# 90 "/usr/include/stdio.h" 3 4
typedef __off_t off_t;
# 102 "/usr/include/stdio.h" 3 4
typedef __ssize_t ssize_t;







typedef _G_fpos_t fpos_t;




# 164 "/usr/include/stdio.h" 3 4
# 1 "/usr/include/x86_64-linux-gnu/bits/stdio_lim.h" 1 3 4
# 165 "/usr/include/stdio.h" 2 3 4



extern struct _IO_FILE *stdin;
extern struct _IO_FILE *stdout;
extern struct _IO_FILE *stderr;







extern int remove (const char *__filename) __attribute__ ((__nothrow__ , __leaf__));

extern int rename (const char *__old, const char *__new) __attribute__ ((__nothrow__ , __leaf__));




extern int renameat (int __oldfd, const char *__old, int __newfd,
       const char *__new) __attribute__ ((__nothrow__ , __leaf__));








extern FILE *tmpfile (void) ;
# 209 "/usr/include/stdio.h" 3 4
extern char *tmpnam (char *__s) __attribute__ ((__nothrow__ , __leaf__)) ;





extern char *tmpnam_r (char *__s) __attribute__ ((__nothrow__ , __leaf__)) ;
# 227 "/usr/include/stdio.h" 3 4
extern char *tempnam (const char *__dir, const char *__pfx)
     __attribute__ ((__nothrow__ , __leaf__)) __attribute__ ((__malloc__)) ;








extern int fclose (FILE *__stream);




extern int fflush (FILE *__stream);

# 252 "/usr/include/stdio.h" 3 4
extern int fflush_unlocked (FILE *__stream);
# 266 "/usr/include/stdio.h" 3 4






extern FILE *fopen (const char *__restrict __filename,
      const char *__restrict __modes) ;




extern FILE *freopen (const char *__restrict __filename,
        const char *__restrict __modes,
        FILE *__restrict __stream) ;
# 295 "/usr/include/stdio.h" 3 4

# 306 "/usr/include/stdio.h" 3 4
extern FILE *fdopen (int __fd, const char *__modes) __attribute__ ((__nothrow__ , __leaf__)) ;
# 319 "/usr/include/stdio.h" 3 4
extern FILE *fmemopen (void *__s, size_t __len, const char *__modes)
  __attribute__ ((__nothrow__ , __leaf__)) ;




extern FILE *open_memstream (char **__bufloc, size_t *__sizeloc) __attribute__ ((__nothrow__ , __leaf__)) ;






extern void setbuf (FILE *__restrict __stream, char *__restrict __buf) __attribute__ ((__nothrow__ , __leaf__));



extern int setvbuf (FILE *__restrict __stream, char *__restrict __buf,
      int __modes, size_t __n) __attribute__ ((__nothrow__ , __leaf__));





extern void setbuffer (FILE *__restrict __stream, char *__restrict __buf,
         size_t __size) __attribute__ ((__nothrow__ , __leaf__));


extern void setlinebuf (FILE *__stream) __attribute__ ((__nothrow__ , __leaf__));








extern int fprintf (FILE *__restrict __stream,
      const char *__restrict __format, ...);




extern int printf (const char *__restrict __format, ...);

extern int sprintf (char *__restrict __s,
      const char *__restrict __format, ...) __attribute__ ((__nothrow__));





extern int vfprintf (FILE *__restrict __s, const char *__restrict __format,
       __gnuc_va_list __arg);




extern int vprintf (const char *__restrict __format, __gnuc_va_list __arg);

extern int vsprintf (char *__restrict __s, const char *__restrict __format,
       __gnuc_va_list __arg) __attribute__ ((__nothrow__));





extern int snprintf (char *__restrict __s, size_t __maxlen,
       const char *__restrict __format, ...)
     __attribute__ ((__nothrow__)) __attribute__ ((__format__ (__printf__, 3, 4)));

extern int vsnprintf (char *__restrict __s, size_t __maxlen,
        const char *__restrict __format, __gnuc_va_list __arg)
     __attribute__ ((__nothrow__)) __attribute__ ((__format__ (__printf__, 3, 0)));

# 412 "/usr/include/stdio.h" 3 4
extern int vdprintf (int __fd, const char *__restrict __fmt,
       __gnuc_va_list __arg)
     __attribute__ ((__format__ (__printf__, 2, 0)));
extern int dprintf (int __fd, const char *__restrict __fmt, ...)
     __attribute__ ((__format__ (__printf__, 2, 3)));








extern int fscanf (FILE *__restrict __stream,
     const char *__restrict __format, ...) ;




extern int scanf (const char *__restrict __format, ...) ;

extern int sscanf (const char *__restrict __s,
     const char *__restrict __format, ...) __attribute__ ((__nothrow__ , __leaf__));
# 443 "/usr/include/stdio.h" 3 4
extern int fscanf (FILE *__restrict __stream, const char *__restrict __format, ...) __asm__ ("" "__isoc99_fscanf")

                               ;
extern int scanf (const char *__restrict __format, ...) __asm__ ("" "__isoc99_scanf")
                              ;
extern int sscanf (const char *__restrict __s, const char *__restrict __format, ...) __asm__ ("" "__isoc99_sscanf") __attribute__ ((__nothrow__ , __leaf__))

                      ;
# 463 "/usr/include/stdio.h" 3 4








extern int vfscanf (FILE *__restrict __s, const char *__restrict __format,
      __gnuc_va_list __arg)
     __attribute__ ((__format__ (__scanf__, 2, 0))) ;





extern int vscanf (const char *__restrict __format, __gnuc_va_list __arg)
     __attribute__ ((__format__ (__scanf__, 1, 0))) ;


extern int vsscanf (const char *__restrict __s,
      const char *__restrict __format, __gnuc_va_list __arg)
     __attribute__ ((__nothrow__ , __leaf__)) __attribute__ ((__format__ (__scanf__, 2, 0)));
# 494 "/usr/include/stdio.h" 3 4
extern int vfscanf (FILE *__restrict __s, const char *__restrict __format, __gnuc_va_list __arg) __asm__ ("" "__isoc99_vfscanf")



     __attribute__ ((__format__ (__scanf__, 2, 0))) ;
extern int vscanf (const char *__restrict __format, __gnuc_va_list __arg) __asm__ ("" "__isoc99_vscanf")

     __attribute__ ((__format__ (__scanf__, 1, 0))) ;
extern int vsscanf (const char *__restrict __s, const char *__restrict __format, __gnuc_va_list __arg) __asm__ ("" "__isoc99_vsscanf") __attribute__ ((__nothrow__ , __leaf__))



     __attribute__ ((__format__ (__scanf__, 2, 0)));
# 522 "/usr/include/stdio.h" 3 4









extern int fgetc (FILE *__stream);
extern int getc (FILE *__stream);





extern int getchar (void);

# 550 "/usr/include/stdio.h" 3 4
extern int getc_unlocked (FILE *__stream);
extern int getchar_unlocked (void);
# 561 "/usr/include/stdio.h" 3 4
extern int fgetc_unlocked (FILE *__stream);











extern int fputc (int __c, FILE *__stream);
extern int putc (int __c, FILE *__stream);





extern int putchar (int __c);

# 594 "/usr/include/stdio.h" 3 4
extern int fputc_unlocked (int __c, FILE *__stream);







extern int putc_unlocked (int __c, FILE *__stream);
extern int putchar_unlocked (int __c);






extern int getw (FILE *__stream);


extern int putw (int __w, FILE *__stream);








extern char *fgets (char *__restrict __s, int __n, FILE *__restrict __stream)
     ;
# 638 "/usr/include/stdio.h" 3 4
extern char *gets (char *__s) __attribute__ ((__deprecated__));


# 665 "/usr/include/stdio.h" 3 4
extern __ssize_t __getdelim (char **__restrict __lineptr,
          size_t *__restrict __n, int __delimiter,
          FILE *__restrict __stream) ;
extern __ssize_t getdelim (char **__restrict __lineptr,
        size_t *__restrict __n, int __delimiter,
        FILE *__restrict __stream) ;







extern __ssize_t getline (char **__restrict __lineptr,
       size_t *__restrict __n,
       FILE *__restrict __stream) ;








extern int fputs (const char *__restrict __s, FILE *__restrict __stream);





extern int puts (const char *__s);






extern int ungetc (int __c, FILE *__stream);






extern size_t fread (void *__restrict __ptr, size_t __size,
       size_t __n, FILE *__restrict __stream) ;




extern size_t fwrite (const void *__restrict __ptr, size_t __size,
        size_t __n, FILE *__restrict __s);

# 737 "/usr/include/stdio.h" 3 4
extern size_t fread_unlocked (void *__restrict __ptr, size_t __size,
         size_t __n, FILE *__restrict __stream) ;
extern size_t fwrite_unlocked (const void *__restrict __ptr, size_t __size,
          size_t __n, FILE *__restrict __stream);








extern int fseek (FILE *__stream, long int __off, int __whence);




extern long int ftell (FILE *__stream) ;




extern void rewind (FILE *__stream);

# 773 "/usr/include/stdio.h" 3 4
extern int fseeko (FILE *__stream, __off_t __off, int __whence);




extern __off_t ftello (FILE *__stream) ;
# 792 "/usr/include/stdio.h" 3 4






extern int fgetpos (FILE *__restrict __stream, fpos_t *__restrict __pos);




extern int fsetpos (FILE *__stream, const fpos_t *__pos);
# 815 "/usr/include/stdio.h" 3 4

# 824 "/usr/include/stdio.h" 3 4


extern void clearerr (FILE *__stream) __attribute__ ((__nothrow__ , __leaf__));

extern int feof (FILE *__stream) __attribute__ ((__nothrow__ , __leaf__)) ;

extern int ferror (FILE *__stream) __attribute__ ((__nothrow__ , __leaf__)) ;




extern void clearerr_unlocked (FILE *__stream) __attribute__ ((__nothrow__ , __leaf__));
extern int feof_unlocked (FILE *__stream) __attribute__ ((__nothrow__ , __leaf__)) ;
extern int ferror_unlocked (FILE *__stream) __attribute__ ((__nothrow__ , __leaf__)) ;








extern void perror (const char *__s);






# 1 "/usr/include/x86_64-linux-gnu/bits/sys_errlist.h" 1 3 4
# 26 "/usr/include/x86_64-linux-gnu/bits/sys_errlist.h" 3 4
extern int sys_nerr;
extern const char *const sys_errlist[];
# 854 "/usr/include/stdio.h" 2 3 4




extern int fileno (FILE *__stream) __attribute__ ((__nothrow__ , __leaf__)) ;




extern int fileno_unlocked (FILE *__stream) __attribute__ ((__nothrow__ , __leaf__)) ;
# 873 "/usr/include/stdio.h" 3 4
extern FILE *popen (const char *__command, const char *__modes) ;





extern int pclose (FILE *__stream);





extern char *ctermid (char *__s) __attribute__ ((__nothrow__ , __leaf__));
# 913 "/usr/include/stdio.h" 3 4
extern void flockfile (FILE *__stream) __attribute__ ((__nothrow__ , __leaf__));



extern int ftrylockfile (FILE *__stream) __attribute__ ((__nothrow__ , __leaf__)) ;


extern void funlockfile (FILE *__stream) __attribute__ ((__nothrow__ , __leaf__));
# 943 "/usr/include/stdio.h" 3 4

# 3 "jpeg_true-unreach-call.c" 2
int ncols = 240;
int nrows = 160;
unsigned long huffbits[] = {
  0xd3767676, 0xad0b7380, 0x08080822, 0x2235ad6e,
  0x575aecfc, 0xbd5a5667, 0x04045114, 0x104446b5,
  0xadc9eb25, 0xa38fa952, 0x5b000208, 0x38510446,
  0xb58dcaea, 0x64af87a7, 0x5a4b0009, 0x1d2852cd,
  0xf7a8888d, 0x635b93d4, 0x2c389a75, 0xdd6402b5,
  0x18d8e73f, 0x7241111b, 0x246bcf74, 0xab062e9c,
  0x25a406e5, 0x468d1c9a, 0xd6d10101, 0x30fa558b,
  0x0f4e26db, 0x408b2267, 0xa8b049b0, 0xb120892a,
  0x62746b1e, 0x16931968, 0x546e3db5, 0x7be2864d,
  0x228c324b, 0x5b45d89d, 0x18cc3d06, 0xc56c523a,
  0x72c48f8d, 0x6dd92ac3, 0x2be0baec, 0x5e806e15,
  0xf48ad8a3, 0x2b39f0ac, 0x8cb13bea, 0xc76228ed,
  0xae2ef898, 0x37c82e0a, 0x15992312, 0x44b6f950,
  0x068dc5de, 0x56e15d75, 0x7b8a0e8e, 0xab902c5b,
  0x6c802223, 0x71f70306, 0xebeb5c51, 0xfa2dc463,
  0xc6e82922, 0x88c6b9b9, 0x1b61856e, 0x4ad715da,
  0xf2c70655, 0x44b565c9, 0x22a22351, 0xd95ae187,
  0x6a5ab766, 0xd591ad63, 0x6ae75957, 0xb2410421,
  0xb98baa18, 0xb626abb3, 0xa6a8d630, 0x4c49e41b,
  0x2587b4b1, 0x0985a418, 0xd3cf26ea, 0x8232363b,
  0x04be46d6, 0x59b32aa4, 0x78ba018f, 0x3dad9731,
  0x11cd1b1e, 0x6d6d1188, 0x3992b513, 0x3f4c3265,
  0xdab51800, 0xe6b33e86, 0xa35a8ae4, 0x51133f5a,
  0x456bdcaa, 0x39051618, 0x71762263, 0x495b3306,
  0xe78a15ad, 0x0fddc47d, 0xcd5af13a, 0xd0e957df,
  0xeb87fbb5, 0x66736c9f, 0x4acfbd66, 0xb3ef59f7,
  0xacd66b35, 0x9acd66aa, 0xab155445, 0x11468d1a,
  0x35764993, 0x93c02714, 0xa2b15ac8, 0xfdc467fd,
  0xafe95071, 0x32fd697a, 0x55f7fad5, 0xfa5591ff,
  0x465e6b35, 0x9acd66b3, 0x559acd55, 0x55555551,
  0xa3468d1a, 0xbbff587e, 0xa694d2f3, 0x5acaffa2,
  0x29ff6c7f, 0x23519fde, 0xa7d697a5, 0x5f7de4e3,
  0xb5591ff4, 0x71f5acd6, 0x6b359aaa, 0xcd66b70a,
  0xf31738c8, 0xacd55562, 0xb14451a3, 0x468d1abc,
  0xff5bf89a, 0x0691b9ad, 0x5c66c4ff, 0xbe293891,
  0x7eb4a78a, 0xbefe0356, 0x27f71f8d, 0x66aaab35,
  0x52cc2307, 0xb9a96e24, 0x3c838fa1, 0xa33bff78,
  0xfe74b331, 0x3d6a3bb2, 0x8df31245, 0x4522c881,
  0x94f1558a, 0xac511468, 0xd1a346af, 0x7fd6fe26,
  0xa81ad44e, 0x6c5fea29, 0x7ef2e7d6, 0x93a0abdf,
  0xbab5627f, 0x74df5aaa, 0xaaab8b9d, 0x84a2f5ee,
  0x69a5e702, 0x9df26b35, 0x19c1a279, 0xab7b830c,
  0x808fba7a, 0x8a8019d3, 0x746370f5, 0xaf224c7d,
  0xdfd6bc89, 0x3fb87f3a, 0xf225fee7, 0xeb5f6797,
  0xfb9fad1b, 0x797fb86b, 0xecf3138d, 0x847b9e94,
  0x2c723fd6, 0x60ffb946, 0xd1039437, 0x0a1bd303,
  0x3fce9ec9, 0x51493313, 0xec13ffaf, 0x57e009ce,
  0x0e467d2a, 0xaafb9b39, 0x2810587d, 0x693eed5e,
  0x7fab5fad, 0x589f91be, 0xb55559a7, 0x6da85bd0,
  0x53ca0b13, 0xdcd7534c, 0x2b142b1c, 0x74aad22e,
  0x19a27b72, 0x46d1c814, 0x608bba7f, 0xe3c7fc6b,
  0xc98bfe79, 0xffe3c7fc, 0x6bca8ffe, 0x798fd6bc,
  0xb5fee2ff, 0xdf35b107, 0xf027fdf2, 0x2b083b20,
  0xfc056f51, 0xfc407e35, 0xe67a3feb, 0x4ccc47de,
  0x27f1abef, 0xf5bf8d0a, 0xabce6d25, 0xff76bd2a,
  0x33f28abc, 0xe621f5ab, 0x1e8ff5aa, 0xaaaba38b,
  0x76fa51eb, 0x51a64579, 0x59a10e3b, 0x50828c40,
  0x0a9531c8, 0xab072972, 0x3048c8c7, 0x148c7382,
  0x73f5a62a, 0xa3240fca, 0x9a7841e7, 0xf957da2d,
  0xfdbfef9a, 0x13c27a7f, 0x2a6b9854, 0xf24fe55f,
  0x6b8719c9, 0xfca83065, 0x0c3a119a, 0x6e957ffe,
  0xbbf3aaab, 0x9ff8f697, 0xfdd35d79, 0xa8cfcb57,
  0x5cc3f8d5, 0x91fbff85, 0x66b35553, 0x2ef89947,
  0x7145487d, 0xa7ae6861, 0x16bcddbd, 0x54d24c8d,
  0x45940a69, 0x63ce3352, 0x90ca715a, 0x747990b9,
  0xe80714bf, 0x7855c1c2, 0xa9c719a9, 0x41df9c81,
  0x5d08f98e, 0x3dea35dd, 0xc8e84f5c, 0x53c6a460,
  0xf14f091c, 0x839a807e, 0xe23cff74, 0x5374ad43,
  0xfd77e755, 0x53f36f20, 0xff64d76a, 0x8cfca2ae,
  0x7fd49fad, 0x591f99ff, 0x0aaacd54, 0xcc5530bc,
  0x13de8aab, 0xc809e5bb, 0x9a68f70e, 0x28c4d9e4,
  0x935e5e1f, 0xd2a41903, 0x06821049, 0x03f4a642,
  0xcbc8009a, 0xb3184207, 0x6e287515, 0x70bbd000,
  0x71cd3200, 0xd8624834, 0xd10ce060, 0x63be2a32,
  0x41da016f, 0x514e3804, 0xfca7fdae, 0x2a4e1720,
  0x1cfa0a80, 0xe6043eaa, 0x29ab5118, 0x941fad55,
  0x4bcc2ffe, 0xe9fe55d8, 0xd447e415, 0x71fea4d5,
  0x99f9dbe9, 0x59acd554, 0x8bb97029, 0x94231a56,
  0xaa6601b9, 0x34c578c1, 0xa038a90f, 0xa55ae029,
  0xf53cd0ea, 0x29c12054, 0xd1bbf017, 0x8a8ede40,
  0xe771dcbd, 0x813d682b, 0xaa12a837, 0x7a66bca7,
  0x700caa49, 0xf4cf4a78, 0xa43c2c78, 0xfc45440a,
  0xc4808c10, 0x318a35a9, 0x7faf1554, 0xfcc6c3fd,
  0x9346a13f, 0x20ab8ff5, 0x2d5687f7, 0xadf4aaaa,
  0xaa94618f, 0x34b5bb02, 0x9b696c9c, 0x51c678c5,
  0x06e2911a, 0x5976afd6, 0xbcb11baa, 0x8feed0eb,
  0x5dab8aaa, 0xaa346b53, 0xff5c2aa9, 0xbee9fa57,
  0xad407318, 0xa9ffd4b7, 0xd2ad3fd6, 0x9fa5566a,
  0xa914bb85, 0x1d49abe8, 0x1a090839, 0x20f43eb4,
  0x0e2b20f1, 0x5e58ea00, 0xad9ed8fa, 0x5741d6ad,
  0x118ee903, 0x32f38e0e, 0x2a4fbc0f, 0x7c550aaa,
  0xaaa268d6, 0xa9feb96b, 0x359a347a, 0x9ab73fbb,
  0x152ffaa6, 0xfa55affa, 0xefc2aaaa, 0x34691c22,
  0x0c9356f6, 0x6213bd8e, 0xe6c74f4a, 0xba884c85,
  0x1bf0f6a9, 0xe1781f0c, 0x38ec681a, 0x0f4cf48a,
  0x64638e07, 0xad5bae20, 0x603b1a90, 0x90c838c1,
  0x1542aab3, 0x52b158c9, 0x5193e951, 0x48644dc5,
  0x76d1344d, 0x6a9feb57, 0xf1aacd53, 0x7de3f5ab,
  0x63fbb152, 0x7fab6fa5, 0x5b7fae1f, 0x4aaa8e36,
  0x9182a0c9, 0x356d02c1, 0x1e072c7e, 0xf1f5aa71,
  0xebc8a922, 0x59176b00, 0x455ce9ef, 0x192d1f2b,
  0xe83a8a21, 0x8706b1eb, 0x50aed8c7, 0xbf3509f9,
  0x187bd480, 0xe50fa566, 0x81e2b359, 0xacd1359a,
  0x26844cc3, 0x2197f3ad, 0x590abc67, 0x2a739e87,
  0xe9559a1d, 0x69fefb7d, 0x6ad8feec, 0x53ffab6f,
  0xa55b9fdf, 0x2d66aded, 0xde73f2f0, 0xa3a93504,
  0x0902e147, 0x3dc9ea6b, 0x35d0f14e, 0xdc570466,
  0x88cd4f6a, 0x928ce30d, 0xea28c056, 0x6db210a0,
  0x7735226d, 0xc11f74f4, 0xa84e15be, 0xb4ec0aaf,
  0x6f6359a0, 0x78acd6ea, 0xf9bd0fe5, 0x586feeb7,
  0xe55e5ca7, 0xfe59bffd, 0xf268cabf, 0xde1f9d46,
  0x0344ac00, 0x395eb5ab, 0x758bf1fe, 0x95540d4b,
  0xfeb5fea6, 0xad8feec5, 0x37dc6fa5, 0x41feb96a,
  0xcecccb87, 0x9384ec3d, 0x6800a000, 0x300551cf,
  0xad31c753, 0x58e7ad36, 0x55b22b86, 0x191441ad,
  0x525125d1, 0x5038418f, 0xc6aca6dd, 0x6c1187dd,
  0xe0d20c6f, 0x02a56dc1, 0x0e31cf43, 0x44f150ba,
  0x0705f180, 0x3b8cd09a, 0x12c02c71, 0x9fa633f9,
  0x1af3147f, 0x747d4018, 0xa79d48c0, 0x9769f518,
  0xa8ee5570, 0x243bc67e, 0xf6cc63eb, 0x4d25baf0,
  0xc53e8456, 0x74f1fc30, 0x0ff808af, 0x3edcb6d4,
  0x75f60056, 0xb03062ff, 0x817f4acd, 0x66b3537f,
  0xae7ff78d, 0x5b7dca3f, 0x74fd2b49, 0xb132b89e,
  0x40422fdd, 0x1eb42b20, 0x7535b97d, 0x45641e86,
  0x9b06b233, 0xd69f9622, 0x836d39a7, 0x3b632c3e,
  0x957a9b64, 0xcf4ab16c, 0x48cbea33, 0x49d49f61,
  0x5201b41a, 0x6e95da8a, 0x96200a8a, 0x5fb2a709,
  0x9c9e7d6a, 0x1bc8a519, 0x071f5a77, 0x42558c9b,
  0x71e87afe, 0x146e211d, 0x587e028d, 0xdc23f89b,
  0xf2a92e2d, 0x9d70dbc8, 0xad69a33e, 0x488d9c81,
  0xbbef7e15, 0x9acd54df, 0xebdffde3, 0x56fc2541,
  0x1f9d204e, 0xddfe9512, 0x8440aa30, 0x074a67c7,
  0x03ad1392, 0x73cd0231, 0x83592a78, 0x342538c1,
  0xa246ec8e, 0xf4c73cd3, 0x7634e4e0, 0x03d2b500,
  0x36123a83, 0x5667fd24, 0x73c1159d, 0xa327a77a,
  0x949d808e, 0x99a3d2bb, 0x520249c7, 0x6e6a6ff5,
  0x7f88a831, 0x93f2e3e9, 0x46874a34, 0x6b53ff96,
  0x7f8ff4a0, 0x6b359a9f, 0xfd7bffbc, 0x6adfeed5,
  0x9c5b100e, 0xe7934cfd, 0x8566b359, 0xace2b347,
  0x8a3cae45, 0x019522b3, 0x918357c8, 0x42b77cd5,
  0xb1c5c211, 0x4c01041e, 0x86997108, 0x0beb4687,
  0x4a8fef1a, 0x9394c75a, 0x5524ed07, 0xe6eb81da,
  0x973b46ee, 0xbdebb51a, 0x35a9ffcb, 0x3fc7fa52,
  0xc27bb0fc, 0x28429ea6, 0x82c63f87, 0xf3a30db9,
  0x3930c64f, 0xa9514891, 0x29f96341, 0xf4514240,
  0x091de836, 0x79acd035, 0x555d4628, 0x77a1f7ff,
  0x0a7f9643, 0xe86ae977, 0x447b9140, 0xec901ee0,
  0xe681ca02, 0x2bcc50a5, 0x79ce68c9, 0xed42438e,
  0x94252a73, 0x8af304a8, 0x7660b7a5, 0x407f78cc,
  0xa195fa36, 0x78c53124, 0xe4f5a06a, 0x89ad4bac,
  0x7f8ff4a0
};
static unsigned int lastlong = 0;
static int bitsleft = 0;
static unsigned long *nextlong = huffbits;


int getbit()
{
  int bit;

  if (--bitsleft < 0)
    {
      if (nextlong >= &huffbits[sizeof(huffbits)])
 lastlong = 0;
      else
 {
   lastlong = *nextlong++;
   bitsleft = 31;
 }
    }
  bit = (lastlong & 0x80000000) != 0;
  lastlong <<= 1;
  return (bit);
}
unsigned char val_dc_lum[] = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
         0x07, 0x08, 0x09, 0x0A, 0x0B};
unsigned char val_ac_lum[] = {0x01, 0x02, 0x03, 0x00, 0x04, 0x11, 0x05, 0x12, 0x21,
         0x31, 0x41, 0x06, 0x13, 0x51, 0x61, 0x07, 0x22, 0x71, 0x14,
         0x32, 0x81, 0x91, 0xa1, 0x08, 0x23, 0x42, 0xb1, 0xc1, 0x15,
         0x52, 0xd1, 0xf0, 0x24, 0x33, 0x62, 0x72, 0x82, 0x09, 0x0a,
         0x16, 0x17, 0x18, 0x19, 0x1a, 0x25, 0x26, 0x27, 0x28, 0x29,
         0x2a, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3a, 0x43, 0x44,
         0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x53, 0x54, 0x55, 0x56,
         0x57, 0x58, 0x59, 0x5a, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68,
         0x69, 0x6a, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7a,
         0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89, 0x8a, 0x92, 0x93,
         0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0xa2, 0xa3,
         0xa4, 0xa5, 0xa6, 0xa7, 0xa8, 0xa9, 0xaa, 0xb2, 0xb3,
         0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xc2, 0xc3,
         0xc4, 0xc5, 0xc6, 0xc7, 0xc8, 0xc9, 0xca, 0xd2, 0xd3,
         0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xe1, 0xe2, 0xe3,
         0xe4, 0xe5, 0xe6, 0xe7, 0xe8, 0xe9, 0xea, 0xf1, 0xf2, 0xf3,
         0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 0xf9, 0xfa};
unsigned char zz_tbl[32] = {
  0, 1, 8, 16, 9, 2, 3, 10, 17, 24, 32, 25, 18, 11, 4, 5, 12, 19, 26, 33, 40, 48, 41, 34, 27,
  20, 13, 6, 7, 14, 21, 28
};
int mincode_dc[] = {
  0x0,
  0x0,
  0x0,
  0x2,
  0xe,
  0x1e,
  0x3e,
  0x7e,
  0xfe,
  0x1fe
};
int maxcode_dc[] = {
  0x0,
  -1,
  0x0,
  0x6,
  0xe,
  0x1e,
  0x3e,
  0x7e,
  0xfe,
  0x1ff
};
unsigned char valptr_dc[] = {
  0,
  0,
  0,
  1,
  6,
  7,
  8,
  9,
  10,
  11
};
int mincode_ac[] = {
  0x0,
  0x0,
  0x0,
  0x4,
  0xa,
  0x1a,
  0x3a,
  0x78,
  0xf8,
  0x1f6,
  0x3f6,
  0x7f6,
  0xff4,
  0x0,
  0x0,
  0x7fc0,
  0xff82
};
int maxcode_ac[] = {
  0x0,
  -1,
  0x1,
  0x4,
  0xc,
  0x1c,
  0x3b,
  0x7b,
  0xfa,
  0x1fa,
  0x3fa,
  0x7f9,
  0xff7,
  -1,
  -1,
  0x7fc0,
  0xffff
};
unsigned char valptr_ac[] = {
  0,
  0,
  0,
  2,
  3,
  6,
  9,
  11,
  15,
  18,
  23,
  28,
  32,
  0,
  0,
  36,
  37
};

void huff_dc_dec(int *retval)
{
  int i, s, l, p, code;

  l = 1;
  code = getbit();
  while (code > maxcode_dc[l])
    {
      l++;
      code = (code << 1) + getbit();
    }
  p = valptr_dc[l];
  p = p + code - mincode_dc[l];
  s = val_dc_lum[p];
  *retval = 0;
  for (i = 0; i < s; i++)
    *retval = (*retval << 1) + getbit();
  i = 1 << (s - 1);
  while (*retval < i)
    {
      i = (-1 << s) + 1;
      *retval = *retval + i;
    }
}

void huff_ac_dec(short *data)
{
  int i, j, icnt, ns, n, s, l, p, code, dindex;
  int temp, data_zz[64];

  dindex = 0;
  while (dindex < 63)
    {
      l = 1;
      code = getbit();
      while (code > maxcode_ac[l])
 {
   l = l + 1;
   code = (code << 1) + getbit();
 }
      p = valptr_ac[l];
      p = p + code - mincode_ac[l];
      ns = val_ac_lum[p];
      s = ns & 0x0f;
      n = ns >> 4;
      if (ns != 0x00)
 {
   if (ns != 0xF0)
     {
       for (i = 0; i < n; i++)
  {
    data_zz[dindex] = 0;
    dindex = dindex + 1;
  }
       temp = 0;
       for (i = 0; i < s; i++)
  temp = (temp << 1) + getbit();
       i = 1 << (s - 1);
       while (temp < i)
  {
    i = (-1 << s) + 1;
    temp = temp + i;
  }
       data_zz[dindex] = temp;
       dindex = dindex + 1;
     }
   else
     {
       for (i = 0; i < 16; i++)
  {
    data_zz[dindex] = 0;
    dindex = dindex + 1;
  }
     }
 }
      else
 {
   icnt = 63 - dindex;
   for (i = 0; i < icnt; i++)
     {
       data_zz[dindex] = 0;
       dindex = dindex + 1;
     }
 }
    }
  for (i = 0; i < 31; i++)
    {
      *(data + zz_tbl[i + 1]) = data_zz[i];
    }
  for (j = 31, i = 31; i > 0; i--, j++)
    {
      *(data + 63 - zz_tbl[i]) = data_zz[j];
    }
  *(data + 63) = data_zz[62];
}

fast_idct_8(short *in, int stride)
{
  int tmp10, tmp11, tmp12, tmp13;
  int tmp20, tmp21, tmp22, tmp23;
  int tmp30, tmp31;
  int tmp40, tmp41, tmp42, tmp43;
  int tmp50, tmp51, tmp52, tmp53;
  int in0, in1, in2, in3, in4, in5, in6, in7;
  int i, j;
  in0 = in[0];
  in1 = in[stride];
  in2 = in[stride * 2];
  in3 = in[stride * 3];
  in4 = in[stride * 4];
  in5 = in[stride * 5];
  in6 = in[stride * 6];
  in7 = in[stride * 7];
  tmp10 = (in0 + in4) * 46341;
  tmp11 = (in0 - in4) * 46341;
  tmp12 = in2 * 25080 - in6 * 60547;
  tmp13 = in6 * 25080 + in2 * 60547;
  tmp20 = tmp10 + tmp13;
  tmp21 = tmp11 + tmp12;
  tmp22 = tmp11 - tmp12;
  tmp23 = tmp10 - tmp13;
  tmp30 = ((((in3 + in5) * 46341) + (1L << (16 - 1 - 2))) >> (16 - 2));
  tmp31 = ((((in3 - in5) * 46341) + (1L << (16 - 1 - 2))) >> (16 - 2));
  tmp40 = ((in1) << 2) + tmp30;
  tmp41 = ((in7) << 2) + tmp31;
  tmp42 = ((in1) << 2) - tmp30;
  tmp43 = ((in7) << 2) - tmp31;
  tmp50 = tmp40 * 16069 + tmp41 * 3196;
  tmp51 = tmp40 * 3196 - tmp41 * 16069;
  tmp52 = tmp42 * 9102 + tmp43 * 13623;
  tmp53 = tmp42 * 13623 - tmp43 * 9102;
  in[0] = (((tmp20 + tmp50) + (1L << 16)) >> (16 + 1));
  in[stride] = (((tmp21 + tmp53) + (1L << 16)) >> (16 + 1));
  in[stride * 2] = (((tmp22 + tmp52) + (1L << 16)) >> (16 + 1));
  in[stride * 3] = (((tmp23 + tmp51) + (1L << 16)) >> (16 + 1));
  in[stride * 4] = (((tmp23 - tmp51) + (1L << 16)) >> (16 + 1));
  in[stride * 5] = (((tmp22 - tmp52) + (1L << 16)) >> (16 + 1));
  in[stride * 6] = (((tmp21 - tmp53) + (1L << 16)) >> (16 + 1));
  in[stride * 7] = (((tmp20 - tmp50) + (1L << 16)) >> (16 + 1));
  return 0;
}

j_rev_dct(short *data)
{
  int i, j, k, id;
  for (i = 0; i < 8; i++)
    fast_idct_8(data + i * 8, 1);
  for (i = 0; i < 8; i++)
    fast_idct_8(data + i, 8);
  return 0;
}


unsigned char qtbl_lum[] = {16, 11, 10, 16, 24, 40, 51, 61,
       12, 12, 14, 19, 26, 58, 60, 55,
       14, 13, 16, 24, 40, 57, 69, 56,
       14, 17, 22, 29, 51, 87, 80, 62,
       18, 22, 37, 56, 68, 109, 103, 77,
       24, 35, 55, 64, 81, 104, 113, 92,
       49, 64, 78, 87, 103, 121, 120, 101,
       72, 92, 95, 98, 112, 100, 103, 99};
dquantz_lum(short *data)
{
  int i;
  for (i = 0; i < 8 * 8; i++)
    *(data + i) = *(data + i) * qtbl_lum[i];
  return 0;
}

static short dct_data[240 * 160];

int main()
{
  int i, j, k, l, m;
  short *pdct;
  int prev;
  int value, npixels;
  int sum;

  lastlong = 0;
  bitsleft = 0;
  nextlong = huffbits;
  prev = 0;

  for (i = 0; i < 600; i++)
    {
      huff_dc_dec(&value);
      dct_data[i * 8 * 8] = value + prev;
      prev = dct_data[i * 8 * 8];
    }

  for (i = 0; i < 600; i++)
    huff_ac_dec(&dct_data[i * 64]);

  pdct = &dct_data[0];

  for (i = 0; i < 600; i++)
    dquantz_lum(pdct + i * 64);

  pdct = &dct_data[0];

  for (i = 0; i < 600; i++)
    j_rev_dct(pdct + i * 64);

  npixels = 600 * 64;
  sum = 0;

  for (i = m = 0; i < npixels; i = i + 8 * ncols)
    {
      for (j = 0; j < 64; j = j + 8)
 {
   for (k = 0; k < 8 * ncols; k = k + 64)
     {
       for (l = 0; l < 8; l++)
  {
    sum += dct_data[l + k + j + i] + 64;
  }
     }
 }
    }

  if (sum != 2598822)
    {
      puts("jpeg: fail\n");
    }
  else
    {
      puts("jpeg: success\n");
    }

  return 0;
}
