# Speech Rule
> Speech rules are used to segment the original text and tag it, then TTS Server matches the configuration based on tags for voice synthesis.

## Built-in rule example in the software:

```javascript 
let SpeechRuleJS = {
    name: "Narration/Dialogue",
    id: "ttsrv.multi_voice",
    author: "TTS Server",
    version: 4,
    
    // Map type, key is the tag id, value is the display name in the tag dropdown
    tags: {narration: "Narration", dialogue: "Dialogue"},

    // text is the original unprocessed text
    // Returns a List:
    // [
    //     {text: "This is a narration text", tag: "narration"}, 
    //     {text: "This is a dialogue text", tag: "dialogue"}
    // ]
    handleText(text) {
        const list = [];
        let tmpStr = "";
        let endTag = "narration";

        text.split("").forEach((char, index) => {
            tmpStr += char;

            if (char === '"') {
                endTag = "dialogue";
                list.push({text: tmpStr, tag: "narration"});
                tmpStr = "";
            } else if (char === '"') {
                endTag = "narration";
                tmpStr = tmpStr.slice(0, -1)
                list.push({text: tmpStr, tag: "dialogue"});
                tmpStr = "";
            } else if (index === text.length - 1) {
                list.push({text: tmpStr, tag: endTag});
            }
        });

        return list;
    },

    // After calling handleText, split each element text in the returned list into sentences,
    // Returns a string List
    splitText(text) {
        let separatorStr = "。？?！!;；"

        let list = []
        let tmpStr = ""
        text.split("").forEach((char, index) => {
            tmpStr += char

            if (separatorStr.includes(char)) {
                list.push(tmpStr)
                tmpStr = ""
            } else if (index === text.length - 1) {
                list.push(tmpStr);
            }
        })

        return list.filter(item =>  item.replace(/[""]/g, '').trim().length > 0);
    }

};


```

