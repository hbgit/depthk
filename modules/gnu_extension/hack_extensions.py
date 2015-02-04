#!/usr/bin/env python
# -*- coding: latin1 -*-
from itertools import count

import sys
import re

_super_hack = '''typedef union
{
  struct __pthread_mutex_s
  {
    int __lock;
    unsigned int __count;
    int __owner;
    int __kind;
    unsigned int __nusers;
    __extension__ union
    {
      int __spins;
      __pthread_slist_t __list;
    };
  } __data;
  char __size[24];
  long int __align;
} pthread_mutex_t;'''


def countwhiledelimiter(_list, _index):
    delimiter = ";"
    count = _index
    # print(count,"===========================")
    while not re.search(r";$", _list[count]):
        count += 1
    # print(count,"===========================")
    #_index += count

    return count+1


## data = output of C pre processor ##
def make_pycparser_compatible( data ):

    # kill white space #
    _d = ''
    for line in data.splitlines():
        #if line.strip():
        _d += line + '\n'

    #print(_d)


    #f = open('/tmp/xxx.c','wb')
    #f.write(_d); f.close()
    if _super_hack in _d:
        #print("Here")
        _d = _d.replace( _super_hack, 'typedef union pthread_mutexattr_t;\n' )
    #else:
    #    print("Here")
        #raise SystemError
    #    sys.exit()


    data = _d
    d = ''
    # TYPEDEF_HACKS = [
    #     ('signed', '__signed'),
    #     ('signed', '__signed__'),
    #     ('char *', '__builtin_va_list'),
    #     #('const', '__const'),
    #     ('const', '__const__'),
    #     #('restrict', '__restrict'),
    # ]
    #for type, name in TYPEDEF_HACKS: d += 'typedef %s %s;\n' %(type,name)

    list2delete = []
    listdata = data.splitlines()
    counti = 0
    #for num, line in enumerate(data.splitlines()):
    while counti < len(listdata):
        # TODO: Improve this match by use regular expressions


        # TODO: remove __gnuc_va_list
        # functions that use __gnuc_va_list
        # typedef __builtin_va_list __gnuc_va_list;
        if re.search(r"typedef[ ]+__builtin_va_list[ ]+__gnuc_va_list;", listdata[counti]):
            listdata[counti] = ""
        # extern int _IO_vfscanf
        if listdata[counti].startswith('extern int _IO_vfscanf'):
            counti = countwhiledelimiter(listdata, counti)
        # extern int _IO_vfprintf
        if listdata[counti].startswith('extern int _IO_vfprintf'):
            counti = countwhiledelimiter(listdata, counti)
        # typedef __gnuc_va_list va_list;
        if re.search(r"typedef[ ]+__gnuc_va_list[ ]+va_list;", listdata[counti]):
            listdata[counti] = ""
        # extern int vfprintf
        if listdata[counti].startswith('extern int vfprintf'):
            counti = countwhiledelimiter(listdata, counti)
        # extern int vprintf
        if listdata[counti].startswith('extern int vprintf'):
            counti = countwhiledelimiter(listdata, counti)
        # extern int vsprintf
        if listdata[counti].startswith('extern int vsprintf'):
            counti = countwhiledelimiter(listdata, counti)
        # extern int vsnprintf
        if listdata[counti].startswith('extern int vsnprintf'):
            counti = countwhiledelimiter(listdata, counti)
        # extern int vdprintf
        if listdata[counti].startswith('extern int vdprintf'):
            # print(listdata[counti],"-----------------------------------", counti)
            counti = countwhiledelimiter(listdata, counti)
            # print(listdata[counti],"-----------------------------------", counti)
        # extern int vfscanf
        if listdata[counti].startswith('extern int vfscanf'):
            counti = countwhiledelimiter(listdata, counti)
        # extern int vscanf
        if listdata[counti].startswith('extern int vscanf'):
            counti = countwhiledelimiter(listdata, counti)
        # extern int vsscanf
        if listdata[counti].startswith('extern int vsscanf'):
            counti = countwhiledelimiter(listdata, counti)
        # extern int vfscanf
        if listdata[counti].startswith('extern int vfscanf'):
            counti = countwhiledelimiter(listdata, counti)
        # extern int vscanf
        if listdata[counti].startswith('extern int vscanf'):
            counti = countwhiledelimiter(listdata, counti)
        # extern int vsscanf
        if listdata[counti].startswith('extern int vsscanf'):
            counti = countwhiledelimiter(listdata, counti)
        #


        # printf -- stdio.h
        matchincludeio = re.search(r"(#include[ ]*<stdio\.h>)", listdata[counti])
        if matchincludeio:
            listdata[counti] = listdata[counti].replace(matchincludeio.group(1),"//"+matchincludeio.group(1))

        matchprintf = re.search(r"(printf\(.*\)[ ]*;)", listdata[counti])
        if matchprintf:
            #print("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
            # identify only the part with printf
            txtlist = listdata[counti].strip().split(";")
            for txt in txtlist:
                matchprintfstart = re.search(r"(^printf\(.*\))", txt)
                if matchprintfstart:
                    listdata[counti] = listdata[counti].replace(matchprintfstart.group(1), "")



        if '((__malloc__));' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__malloc__));', ';')   # stdio.h:225
        if '((__malloc__))' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__malloc__))', '')

        #__attribute__((__malloc__))
        match_malloc = re.search(r"[ ]*__attribute__[ ]*\(\(__malloc__\)\)[ ]*([;]*)", listdata[counti])
        if match_malloc:
            if match_malloc.group(1):
                listdata[counti] = re.sub(r"[ ]*__attribute__[ ]*\(\(__malloc__\)\)[ ]*[;]*", " ;", listdata[counti])
            else:
                listdata[counti] = re.sub(r"[ ]*__attribute__[ ]*\(\(__malloc__\)\)[ ]*[;]*", " ", listdata[counti])


        #__const
        # match_ext = re.search(r"(__extension__)* extern", listdata[counti])
        # if match_ext or listdata[counti].startswith('extern'):
        match_const = re.search(r"__const[^_]", listdata[counti])
        if match_const:
            listdata[counti] = listdata[counti].replace('__const', 'const')

            #print(listdata[counti])
            #sys.exit()

        #print(">>>>>> "+ listdata[counti])



        if '__attribute__((visibility("default")))' in listdata[counti].split():
            listdata[counti] = listdata[counti].replace('__attribute__((visibility("default")))', '')

        #
        if '__attribute__((noinline))' in listdata[counti].split():  listdata[counti] = listdata[counti].replace('__attribute__((noinline))', '')
        if '__attribute__((noinline));' in listdata[counti].split():  listdata[counti] = listdata[counti].replace('__attribute__((noinline));', ';')

        #
        if '__attribute__((packed))' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('__attribute__((packed))', '')
        if '__attribute__((packed));' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('__attribute__((packed));', ';')


        # __WAIT_STATUS
        if '__WAIT_STATUS' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('__WAIT_STATUS', '')
        if '((__transparent_union__))' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__transparent_union__))', '')
        if '((__transparent_union__));' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__transparent_union__));', ';')
        if '((__transparent_union__)) ;' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__transparent_union__)) ;', ';')

        #
        if '((__pure__))' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__pure__))', '')
        if '((__pure__)) ;' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__pure__)) ;', ';')

        #
        if '((__warn_unused_result__))' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__warn_unused_result__))', '')
        if '((__warn_unused_result__)) ;' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__warn_unused_result__)) ;', ';')
        if '((__warn_unused_result__));' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__warn_unused_result__));', ';')


        #if '__attribute__' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('__attribute__', '')
        matchattribute = re.search(r"[ ]*__attribute__[ ]*", listdata[counti])
        if matchattribute:
            listdata[counti] = re.sub(r"[ ]*__attribute__[ ]*", " ", listdata[counti])

        # __nothrow__ and __leaf__
        match_nl = re.search(r"\(\(__nothrow__[ ]*,[ ]*__leaf__\)\)", listdata[counti])
        if match_nl:
            #listdata[counti] = listdata[counti].replace('((__nothrow__ , __leaf__))', '')
            listdata[counti] = re.sub(r"\(\(__nothrow__[ ]*,[ ]*__leaf__\)\)", "", listdata[counti])

        # __attribute__((__nothrow__, __noreturn__))
        matchnr = re.search(r"\(\(__nothrow__[ ]*,[ ]*__noreturn__\)\)", listdata[counti])
        if matchnr:
            listdata[counti] = re.sub(r"\(\(__nothrow__[ ]*,[ ]*__noreturn__\)\)", "", listdata[counti])


        # ((__format__ (__printf__, 3, 4)));
        matchformatp = re.search(r"\(\(__format__[ ]*\(__printf__[ ]*,[ ]*[0-9]+,[ ]*[0-9]+\)\)\)", listdata[counti])
        if matchformatp:
            listdata[counti] = re.sub(r"\(\(__format__[ ]*\(__printf__[ ]*,[ ]*[0-9]+,[ ]*[0-9]+\)\)\)",
                          "",
                          listdata[counti])

        # ((__format__(__scanf__, 2, 0)));
        matchformats = re.search(r"\(\(__format__[ ]*\(__scanf__[ ]*,[ ]*[0-9]+,[ ]*[0-9]+\)\)\)", listdata[counti])
        if matchformats:
            listdata[counti] = re.sub(r"\(\(__format__[ ]*\(__scanf__[ ]*,[ ]*[0-9]+,[ ]*[0-9]+\)\)\)",
                          "",
                          listdata[counti])

        #
        match_nnull = re.search(r"\(\(__nonnull__[ ]*[\(]([0-9]*,[ ]*)*([0-9]*)[\)]\)\)", listdata[counti])
        if match_nnull:
            listdata[counti] = re.sub(r"\(\(__nonnull__[ ]*[\(]([0-9]*,[ ]*)*([0-9]*)[\)]\)\)", "", listdata[counti])
            #listdata[counti] = listdata[counti].replace('((__nonnull__ (1)))', '')



        if '__extension__' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('__extension__','')
        if '__THROW' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('__THROW', '')

        if '__inline__' in listdata[counti].split(): listdata[counti] = listdata[counti].replace( '__inline__', '' )
        if '__inline' in listdata[counti].split(): listdata[counti] = listdata[counti].replace( '__inline', '' )

        if '((__nothrow__))' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__nothrow__))', '')    # inttypes.h
        if '((__nothrow__));' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__nothrow__));', ';')

        matchnothrow = re.search(r"\(\(__nothrow__[ ]*\)\)[ ]*[;]*", listdata[counti])
        if matchnothrow:
            listdata[counti] = re.sub(r"\(\(__nothrow__[ ]*\)\)[ ]*[;]*", "", listdata[counti])

        if '((__const__))' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__const__))', '')
        if '((__const__));' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('((__const__));', ';')

        if '**__restrict' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('**__restrict', '**')
        if '*__restrict' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('*__restrict', '*' )
        if '__restrict' in listdata[counti].split(): listdata[counti] = listdata[counti].replace('__restrict', '' )

        if listdata[counti].strip().startswith('((__format__ ('):       # stdio.h:385
            _s = listdata[counti].strip()
            if _s.endswith( '))) ;' ) or _s.endswith(')));'): listdata[counti] = ';'

        if listdata[counti].startswith('extern') and '((visibility("default")))' in listdata[counti].split():       # SDL_cdrom.h
            listdata[counti] = listdata[counti].replace( '((visibility("default")))', '' )


        #__attribute__ ((__nothrow__ , __leaf__)) __attribute__ ((__noreturn__));
        if listdata[counti].startswith('extern'):
            if '((__noreturn__))'  in listdata[counti].split():
                listdata[counti] = listdata[counti].replace( '((__noreturn__))', '' )
            if '((__noreturn__));' in listdata[counti].split():
                listdata[counti] = listdata[counti].replace( '((__noreturn__));', ';' )

        if '((__noreturn__))'  in listdata[counti].split():
                listdata[counti] = listdata[counti].replace( '((__noreturn__))', '' )
        if '((__noreturn__));' in listdata[counti].split():
            listdata[counti] = listdata[counti].replace( '((__noreturn__));', ';' )

        # Replace __VERIFIER_error() by out implementation
        # if listdata[counti].startswith('extern') and '__VERIFIER_error()' in listdata[counti].split():       # SVCOMP
        #     listdata[counti] = listdata[counti].replace( '__VERIFIER_error()', '__VERIFIER_error(int numline)' )
        # if listdata[counti].startswith('extern') and '__VERIFIER_error(void);' in listdata[counti].split():       # SVCOMP
        #     listdata[counti] = listdata[counti].replace( '__VERIFIER_error(void);', '__VERIFIER_error(int numline);' )


        # types.h #
        list_mode = ['((__mode__ (__QI__)));' , '((__mode__(__QI__)));' ,
                     '((__mode__ (__HI__)));' , '((__mode__(__HI__)));' ,
                     '((__mode__ (__SI__)));' , '((__mode__(__SI__)));' ,
                     '((__mode__ (__DI__)));' , '((__mode__(__DI__)));' ,
                     '((__mode__ (__word__)));' , '((__mode__(__word__)));']
        for ugly in list_mode:
            if listdata[counti].endswith( ugly ): listdata[counti] = listdata[counti].replace(ugly, ';')

        # if '__asm__' in listdata[counti].split() and '__isoc99_' in listdata[counti]:
        #     if listdata[counti].strip().endswith(';'): listdata[counti] = listdata[counti].split('__asm__')[0] + ';'
        #     else: listdata[counti] = listdata[counti].split('__asm__')[0]

        #
        match_asm = re.search(r"__asm__[ ]*\(\"\" \"__xpg_strerror_r\"\)", listdata[counti])
        if match_asm:
            #listdata[counti] = listdata[counti].replace('((__nothrow__ , __leaf__))', '')
            listdata[counti] = re.sub(r"__asm__[ ]*\(\"\" \"__xpg_strerror_r\"\)", "", listdata[counti])

        match_asm_generic = re.search(r"__asm__\(.*\)[ ]*([;]*)", listdata[counti])
        if match_asm_generic:
            if match_asm_generic.group(1):
                listdata[counti] = re.sub(r"__asm__\(.*\)[ ]*([;]*)", ";", listdata[counti])
            else:
                listdata[counti] = re.sub(r"__asm__\(.*\)[ ]*([;]*)", "", listdata[counti])

        #if '*__const' in listdata[counti].split(): # sys_errlist.h
        #   pass

        d += listdata[counti] + '\n'
        counti += 1

    #print(d)
    return d


# -------------------------------------------------
# Main python program
# -------------------------------------------------

if __name__ == "__main__":
    #print(sys.argv[1])
    file = open(sys.argv[1], 'r')
    print(make_pycparser_compatible(file.read()))
    file.close()
