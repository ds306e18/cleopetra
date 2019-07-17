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
                newString = stringSplit[0] + "-"  # Use the first part of the string
                if "bot" in newString:
                    newString = newString.replace("bot", "")
                elif "Bot" in newString:
                    newString = newString.replace("Bot", "")
                elif "BOT" in newString:
                    newString = newString.replace("BOT", "")
                # print(newString)
                self._combined += newString
            else:
                newString = string + "-"
                if "bot" in newString:
                    newString = newString.replace("bot", "")
                elif "Bot" in newString:
                    newString = newString.replace("Bot", "")
                elif "BOT" in newString:
                    newString = newString.replace("BOT", "")
                # print(newString)
                self._combined += newString

        # This is so the trailing whitespace and '-' will be removed
        self._combined = self._combined.strip()[:-1]
        return self._combined
