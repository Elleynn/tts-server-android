package com.elleynn.tts

import com.elleynn.database.entities.systts.AudioParams

typealias ValueProvider<T> = () -> T

data class SynthesizerConfig(
    var requestTimeout: ValueProvider<Long> = { 8000 },
    var maxRetryTimes: ValueProvider<Int> = { 1 },
    var toggleTry: ValueProvider<Int> = { 1 },
    var streamPlayEnabled: ValueProvider<Boolean> = { true },
    var silenceSkipEnabled: ValueProvider<Boolean> = { false },
    var audioParams: ValueProvider<AudioParams> = { AudioParams(1f, 1f, 1f) },

    var bgmShuffleEnabled: ValueProvider<Boolean> = { false },
    var bgmVolume: ValueProvider<Float> = { 1f },
    var bgmEnabled: ValueProvider<Boolean> = { true },

    var provider: ValueProvider<Int> = { 0 },
)