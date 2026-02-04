# TTS Plugin
> The plugin language is Javascript, using Rhino as the JS parser

```javascript
let PluginJS = {
    "name": "Plugin name",
    "id": "Unique plugin ID", // Also serves as the relative directory for fs file operations /Android/data/com.github.jing332.tts_server_android/caches/pluginID
    "author": "Author",
    "iconUrl": "https://cn.bing.com/favicon.ico",
    "version": 1, // Version number, must be an integer

    // When TTS is stopped
    "onStop": function () {
 
    },
    
    /**
    "getAudio": function (text, locale, voice, speed, volume, pitch) {
        // Supported return types:
        // Strings starting with http:// and https://
        // InputStream Java input stream
        // ByteArray Java byte array
        // ArrayBuffer
        // Uint8Array
        
    }
    */

    // Choose either getAudio or getAudioV2 based on your needs
    "getAudioV2": function (request, callback) {
        let rate = (request.rate * 2) - 100
        let pitch = request.pitch - 50
        let voice = request.voice
        let volume = request.volume
        let text = request.text

        callback.write(bytes)   // Write byte array
        callback.close()        // Call after all writes are complete
        callback.error(string)  // Call on error
    },
}


let EditorJS = {
    // Audio sample rate, called when saving TTS configuration
    "getAudioSampleRate": function (locale, voice) {
        // return 24000
        
        // Automatically request audio for detection
        let audio = PluginJS.getAudio('test', locale, voice, 50, 50, 50)
        return ttsrv.getAudioSampleRate(audio)
    },

    // Language dropdown
    "getLocales": function () {
        return ['zh-CN', 'en-US']
    },

    // Voice dropdown
    "getVoices": function (locale) {
        // Simple usage: key as the voice parameter for getAudio, value as the display name
        return { 'xiaoxiao': 'Xiaoxiao' }
        
        // Advanced usage: specify icon
        return { 
            'xiaoxiao': {
                name: 'Xiaoxiao',
                icon: 'male' // Options: male / female / icon URL
            }
        }
      
    },

    // Load voice data
    "onLoadData": function () {
        let jsonStr = ''
        if (fs.exists('voices.json')) {
            jsonStr = fs.readText('voices.json')
        } else {
            let url = 'https://speech.platform.bing.com/consumer/speech/synthesize/readaloud/voices/list?trustedclienttoken=' + token
            jsonStr = http.get(url).text()
            fs.writeFile('voices.json', jsonStr)
        }

        voices = JSON.parse(jsonStr)
    },

    "onLoadUI": function (ctx, linerLayout) {

    },

    "onVoiceChanged": function (locale, voiceCode) {

    }
}```

