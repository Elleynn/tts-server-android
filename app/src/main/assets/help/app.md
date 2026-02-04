version-2

[![Q Group](https://img.shields.io/badge/Q%E7%BE%A4-124841768-blue.svg)](https://jq.qq.com/?_wv=1027&k=y7WCDjEA) 
[![Issue](https://img.shields.io/badge/Github-Issue-greeb.svg)](https://github.com/Elleynn/tts-server-android/issues)
[![Dev](https://img.shields.io/github/actions/workflow/status/Elleynn/tts-server-android/test.yml?label=%E5%BC%80%E5%8F%91%E7%89%88)](https://github.com/Elleynn/tts-server-android/actions/workflows/test.yml)

# TTS Server 
This app does not provide voice synthesis services. It is just a network TTS forwarder,
calling TTS interfaces on the internet through plugin drivers.


##  1️⃣ System TTS
The following 4 interfaces all have independent import and export functions in the top right corner menu. You can also perform full backup and recovery in settings. 

### Main Interface
Configuration list for managing TTS configurations. You can use the grouping function to switch multiple configurations with one click.
- You can swap the positions of `Edit` and `Audition` buttons in settings ( <i>long press the edit button to audition, or vice versa, long press audition button to edit</i> )

### Read-Aloud Rules
Used to process read-aloud text, matching TTS configurations based on user-configured tags (e.g., narration/dialogue).
The program has built-in `Narration-Dialogue` read-aloud rules based on Chinese quotation marks, which you can use directly.

### Plugins
Used to extend TTS functionality, using JS scripts to call TTS interfaces on the internet, such as: built-in `Azure Plugin`.

### Replacement Rules
Used to replace read-aloud text for correcting pronunciation, e.g., replacing "hello" with "hi"

Advanced Example:
- Replace double quotes with 【】 for dialogues with 5 characters or less, to achieve narration-style reading.
```
(Enable regular expressions)
Replacement Rule: (")(.{1,5})(")
Replace With: 【$2】
```

## 👨‍🏫 System TTS FAQ 
### 1. Reading suddenly stops after the screen locks for a while?
> In `System Settings->Apps->Battery Optimization`, add this app and your reading app to the battery optimization whitelist.
> 
> For this app, you can click `Battery Optimization Whitelist` in the left slide menu for quick setup.
> 
> PS: For domestic systems, you may also need to lock background tasks and enable background permissions.

### 2. Long intervals between paragraphs?
> Usually due to network latency. Due to technical limitations of the Android system TTS service, audio cannot be pre-cached, so it can only be obtained synchronously each time.



## 2️⃣ TTS Forwarder
Used to convert Android system TTS to an HTTP network interface format, making it easy to call in web pages.  
**When used with the reading app's network TTS engine, it can indirectly pre-cache audio for a chapter, improving inter-paragraph smoothness.**
