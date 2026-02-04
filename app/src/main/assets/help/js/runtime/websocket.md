# Websocket

Websocket requests are supported after the 2025 version, internally based on OkHttp implementation

``` javascript
let ws = Websocket("wss://echo.websocket.org", {"User-Agent": "TTS Server"})
ws.on('close', function(code, reason){ // Int, String
    
})

ws.on('error', function(err, response){ // String, NativeResponse
    
})

ws.on('binary', function(buf){ // Buffer
    
})

ws.on('text', function(msg){ // String
    
})

ws.on('open', function(){
    ws.send("Hello world!")
})

// Close
// ws.close(1007, "close~~")

// Force disconnect
// ws.cancel()

```

## Working with plugin TTS getAudioV2

```javascript
var ws = null
let PluginJS = {
    // ...
    
    "onStop": function () {
        if (ws != null) {
            ws.cancel()
        }
    },

    "getAudioV2": function (request, callback) {
        // request is of json type
        // request.text /voice / locale / rate / volume / pitch
        // e.g. request.voice 
                
    
        callback.write(bytes)   // Write byte array
        callback.close()        // Call after all writes are complete
        callback.error(string)  // Call on error
    }
    
}
// ...
    

```
