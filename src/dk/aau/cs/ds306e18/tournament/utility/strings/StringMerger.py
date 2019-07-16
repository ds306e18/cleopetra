# Formmated with Black[https://github.com/python/black]


class StringMerger:
    def __init__(self, strings: list = []):
        self._strings: list = strings or []
        self._combined: str = ""

    def MergeStrings(self):
        self._combined = ""
        for i in range(len(self._strings)):
            string: str = self._strings[i]
            newString: str = ""
            stringSplit: list = string.split(" ")
            if len(stringSplit) >= 2:
                newString = stringSplit[0] + " "  # Use the first part of the string
                if "bot" in newString:
                    newString = newString.replace("bot", "")
                # print(newString)
                self._combined += newString
            else:
                newString = string + " "
                if "bot" in newString:
                    newString = newString.replace("bot", "")
                # print(newString)
                self._combined += newString

        # TODO: make something to take away extra spaces at the end of the string
        return self._combined
