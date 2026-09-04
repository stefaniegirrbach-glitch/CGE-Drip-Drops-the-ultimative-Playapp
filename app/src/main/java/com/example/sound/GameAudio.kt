package com.example.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class GameAudio(context: Context) {
    private val appContext = context.applicationContext
    private val scope = CoroutineScope(Dispatchers.Default)

    var isSoundEnabled: Boolean = true
    var isHapticsEnabled: Boolean = true

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    // Synthesized crisp crystalline "ding" for tap
    fun playTapDing() {
        if (!isSoundEnabled) return
        scope.launch {
            try {
                val sampleRate = 44100
                val durationMs = 120
                val numSamples = sampleRate * durationMs / 1000
                val buffer = ShortArray(numSamples)
                val freq = 1320.0 // High crystal chime

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    // Exponential decay envelope
                    val envelope = Math.exp(-time * 30.0)
                    // Blend fundamental and harmonic for diamond ring
                    val sample = 0.75 * sin(2.0 * Math.PI * freq * time) +
                            0.25 * sin(2.0 * Math.PI * (freq * 2.0) * time)
                    buffer[i] = (sample * envelope * Short.MAX_VALUE * 0.45).toInt().toShort()
                }

                playBuffer(buffer, sampleRate)
            } catch (_: Exception) {}
        }
    }

    // Shimmering multi-harmonic sparkle sweep for upgrades
    fun playUpgradeSparkle() {
        if (!isSoundEnabled) return
        scope.launch {
            try {
                val sampleRate = 44100
                val durationMs = 380
                val numSamples = sampleRate * durationMs / 1000
                val buffer = ShortArray(numSamples)

                val notes = doubleArrayOf(880.0, 1108.73, 1318.51, 1661.22, 2217.46) // Sparkle arpeggio
                val noteDuration = numSamples / notes.size

                for (n in notes.indices) {
                    val noteFreq = notes[n]
                    val start = n * noteDuration
                    val end = minOf((n + 1) * noteDuration, numSamples)
                    for (i in start until end) {
                        val localTime = (i - start).toDouble() / sampleRate
                        val envelope = Math.exp(-localTime * 14.0)
                        val sample = sin(2.0 * Math.PI * noteFreq * localTime)
                        buffer[i] = (sample * envelope * Short.MAX_VALUE * 0.40).toInt().toShort()
                    }
                }

                playBuffer(buffer, sampleRate)
            } catch (_: Exception) {}
        }
    }

    // Golden fanfare for boost activation
    fun playBoostFanfare() {
        if (!isSoundEnabled) return
        scope.launch {
            try {
                val sampleRate = 44100
                val durationMs = 450
                val numSamples = sampleRate * durationMs / 1000
                val buffer = ShortArray(numSamples)
                val notes = doubleArrayOf(523.25, 659.25, 783.99, 1046.50) // C, E, G, High C
                val noteDuration = numSamples / notes.size

                for (n in notes.indices) {
                    val noteFreq = notes[n]
                    val start = n * noteDuration
                    val end = minOf((n + 1) * noteDuration, numSamples)
                    for (i in start until end) {
                        val localTime = (i - start).toDouble() / sampleRate
                        val envelope = Math.exp(-localTime * 9.0)
                        val sample = sin(2.0 * Math.PI * noteFreq * localTime)
                        buffer[i] = (sample * envelope * Short.MAX_VALUE * 0.45).toInt().toShort()
                    }
                }

                playBuffer(buffer, sampleRate)
            } catch (_: Exception) {}
        }
    }

    private fun playBuffer(buffer: ShortArray, sampleRate: Int) {
        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(buffer.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        audioTrack.write(buffer, 0, buffer.size)
        audioTrack.play()
        // Release after finish
        scope.launch {
            kotlinx.coroutines.delay(800)
            try {
                audioTrack.stop()
                audioTrack.release()
            } catch (_: Exception) {}
        }
    }

    fun vibrateTap() {
        if (!isHapticsEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(18, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(18)
            }
        } catch (_: Exception) {}
    }

    fun vibratePurchase() {
        if (!isHapticsEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(45, 220))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(45)
            }
        } catch (_: Exception) {}
    }
}
