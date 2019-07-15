# Formmated with Black[https://github.com/python/black]


class StringMerger:
    def __init__(self, strings: list = []):
        self._strings: list = strings
        self._combined: str = ""

    def MergeStrings(self):
        for i in range(len(self._strings)):
            string: str = self._strings[i]
            stringSplit: list = string.split(" ")
            if len(stringSplit) >= 2:
                self._combined += stringSplit[0]  # Use the first part of the string
            else:
                self._combined += string

        return self._combined
