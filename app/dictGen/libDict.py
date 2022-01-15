import copy
import libXML
import codecs

MAIN_DICT_PATH = 'slownikfreq.txt'
MAIN_DICT_FILTER_AFTER = '='
MAIN_DICT_IGNOREDLINES = 6
MAIN_DICT_CONVERTER = lambda key: key.split(MAIN_DICT_FILTER_AFTER)

HELPER_DICT_PATH = 'odm.txt'
HELPER_DICT_FILTER_AFTER = ','
HELPER_DICT_CONVERTER = lambda key: [key.split(HELPER_DICT_FILTER_AFTER)[0], 1]

MYSPELL_DICT_PATH = 'myspell/pl_PL.dic'
MYSPELL_DICT_NOUN_PATTERN = '/N'


def myspell_filter(line):
    index = line.find(MYSPELL_DICT_NOUN_PATTERN)
    if (index != -1):
        return line[:index]
    return index


def filterOffAfterSymbol(iterable, symbol='='):
    return [x.split(symbol)[0] for x in iterable]


def printHugeDict(dict, lines=100, print_vertically=False):
    if print_vertically:
        for line in dict[:lines]:
            print(line)
    else:
        print(dict[:lines])


def loadDictAsList(path, ignoreLines=0):
    f = codecs.open(path, 'r', 'utf-8')
    res = [x.strip().lower() for x in f][ignoreLines:]

    f.close()
    return res


def loadDictAsListFiltered(path, custom_filter, ignoreLines=0):
    f = codecs.open(path, 'r', 'utf-8')
    res = [custom_filter(x).lower() for x in f if custom_filter(x) != -1][ignoreLines:]

    f.close()
    return res


def list2dict(list, pair_generator):
    dict = {}
    for x in list:
        pair = pair_generator(x)
        dict[pair[0]] = pair[1]
    return dict
    # return {pair_generator(x) for x in list}
    # idk what type key:val pair is :v
    # if I did, I'd use lambda


def filterCommonPart(lhs, rhs):
    return [line for line in lhs if not rhs.get(line)]

def findCommonPart(lhs, rhs):
    return [line for line in lhs if rhs.get(line)]


def tests():
    dictionary = loadDictAsList('/home/work/Desktop/slownik/tests/test1.txt')
    comparator = loadDictAsList('/home/work/Desktop/slownik/tests/test2.txt')

    print('Common 1-2')
    printHugeDict(findCommonPart(dictionary, comparator))

    print('\ndiff 1-2')
    printHugeDict(filterCommonPart(dictionary, comparator))


def mainStuff():
    dictionary = loadDictAsList(MAIN_DICT_PATH, MAIN_DICT_IGNOREDLINES)
    print(len(dictionary))
    dictionary = list2dict(dictionary, MAIN_DICT_CONVERTER)

    comparator = loadDictAsList(HELPER_DICT_PATH)
    comparator = list2dict(comparator, HELPER_DICT_CONVERTER)

    res = findCommonPart(dictionary, comparator)
    print(len(res))
    printHugeDict(res, 1000, True)


def filterOffRareWords(dict, min_count=1000):
    copied = copy.copy(dict)
    toDeleteList = []

    for key in copied.keys():
        val = copied.get(key)
        if int(val) < min_count:
            toDeleteList.append(key)

    for key in toDeleteList:
        copied.pop(key)
    return copied


def exportAsTxt(data, path):
    f = codecs.open(path, 'w', 'utf-8')
    f.write('\n'.join(data))
    f.close()


def mainMyspell():
    dictionary = loadDictAsList(MAIN_DICT_PATH, ignoreLines=MAIN_DICT_IGNOREDLINES)
    dictionary = list2dict(dictionary, MAIN_DICT_CONVERTER)
    # print(dictionary)
    print('before:', len(dictionary))
    dictionary = filterOffRareWords(dictionary, 250)
    print('after:', len(dictionary))

    comparator = loadDictAsListFiltered(MYSPELL_DICT_PATH, myspell_filter, 0)
    comparator = list2dict(comparator, HELPER_DICT_CONVERTER)

    res = findCommonPart(dictionary, comparator)
    print(len(res))

    res = sorted(res, key=len)
    # printHugeDict(libXML.getLenIndexArray(res), 1000, True)
    printHugeDict(res, 100, True)
    libXML.exportAsXml(res, 'dictionary.xml')

if __name__ == '__main__':
    # tests()
    # mainStuff()
    mainMyspell()
