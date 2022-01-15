import codecs


def exportAsXml(data, path):
    f = codecs.open(path, 'w', 'utf-8')

    xml = getXMLResourcePreambule()
    xml += getXMLResourceMinMaxLen(data)

    xml += getXMLWordLenIndexArray(data)
    xml += getXMLWordLenCountsArray(data)

    xml += getXMLWordArray(data)

    xml += "</resources>"

    f.write(xml)
    f.close()


def getXMLResourceMinMaxLen(data):
    return "    <integer name=\"min_len\">" + str(minLenInIterable(data)) + "</integer>\n" + \
           "    <integer name=\"max_len\">" + str(maxLenInIterable(data)) + "</integer>\n"


def getXMLResourcePreambule():
    return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + \
           "<resources>\n"


def getXMLWordArray(words):
    xml = "    <array name=\"dict\">\n"
    for word in words:
        xml += "        <item>" + word + "</item>\n"
    xml += "    </array>\n"

    return xml

def getXMLWordLenIndexArray(data):
    xml = "    <array name=\"len_index\">\n"

    lenIndArr = getLenIndexArray(data)
    for wordLenInd in lenIndArr:
        xml += "        <item>" + str(wordLenInd) + "</item>\n"
    xml += "    </array>\n"

    return xml


def getXMLWordLenCountsArray(data):
    lenAmounts = countLengths(data)

    xml = "    <array name=\"len_amount\">\n"
    for wordLenAmount in lenAmounts:
        xml += "        <item>" + str(wordLenAmount) + "</item>\n"
    xml += "    </array>\n"

    return xml


def maxLenInIterable(iterable):
    max = 0
    for e in iterable:
        if len(e) > max:
            max = len(e)
    return max


def minLenInIterable(iterable):
    min = 1000
    for e in iterable:
        if len(e) < min:
            min = len(e)
    return min


def countLengths(iterable):
    lengths = [0]*(maxLenInIterable(iterable)+1)
    for elem in iterable:
        lengths[len(elem)] += 1

    return lengths



def getLenIndexArray(iterable):
    # returns array of indices where lenIndex[i] is the 1st object of length "i" in array sorted by element length
    lengthCount = countLengths(iterable)
    lengthCount.insert(0, 0)  # necessary correction

    for i in range(1, len(lengthCount)):
        lengthCount[i] += lengthCount[i-1]

    return lengthCount