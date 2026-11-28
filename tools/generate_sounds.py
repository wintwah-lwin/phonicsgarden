#!/usr/bin/env python3
"""
Generates the app's sound effects as small mono WAV files (original, synthesized sounds, so there
are no licensing issues). Run from the project root:

    python3 tools/generate_sounds.py

Design rules (child-friendly): soft attack, quiet peak level, no harsh or buzzing "wrong" sound.
"""
import math
import random
import struct
import wave
from pathlib import Path

RATE = 22050
OUT = Path(__file__).resolve().parent.parent / "app" / "src" / "main" / "res" / "raw"
PEAK = 0.6  # leave plenty of headroom so nothing is ever loud or harsh


def silence(seconds):
    return [0.0] * int(RATE * seconds)


def mix(parts, total_seconds):
    """Sum (start_seconds, samples) parts into one buffer."""
    buf = silence(total_seconds)
    for start, samples in parts:
        offset = int(start * RATE)
        for i, s in enumerate(samples):
            if offset + i < len(buf):
                buf[offset + i] += s
    return buf


def tone(f_start, f_end, seconds, decay, harmonic=0.0, attack=0.005):
    """A sine tone gliding from f_start to f_end with an exponential decay and soft attack."""
    n = int(RATE * seconds)
    out, phase = [], 0.0
    for i in range(n):
        t = i / RATE
        f = f_start * (f_end / f_start) ** (i / max(n - 1, 1))
        phase += 2 * math.pi * f / RATE
        s = math.sin(phase) + harmonic * math.sin(2 * phase)
        env = math.exp(-t * decay) * min(1.0, t / attack)
        out.append(s * env)
    # fade the last few milliseconds to avoid clicks
    fade = int(RATE * 0.01)
    for i in range(fade):
        out[n - 1 - i] *= i / fade
    return out


def thump(f_start, f_end, seconds, decay, noise=0.3):
    """A low 'nom' burst: gliding low tone plus a little smoothed noise."""
    rng = random.Random(7)
    base = tone(f_start, f_end, seconds, decay)
    smooth, out = 0.0, []
    for i, s in enumerate(base):
        smooth = 0.85 * smooth + 0.15 * rng.uniform(-1, 1)
        out.append(s + noise * smooth * math.exp(-(i / RATE) * decay))
    return out


def save(name, samples, gain=1.0):
    """Normalise to PEAK (times gain), with a tiny fade in and out so nothing clicks."""
    samples = list(samples)
    fade_in, fade_out = int(RATE * 0.003), int(RATE * 0.01)
    for i in range(min(fade_in, len(samples))):
        samples[i] *= i / fade_in
    for i in range(min(fade_out, len(samples))):
        samples[-1 - i] *= i / fade_out
    peak = max(abs(s) for s in samples) or 1.0
    scale = PEAK * gain / peak
    OUT.mkdir(parents=True, exist_ok=True)
    with wave.open(str(OUT / f"{name}.wav"), "wb") as w:
        w.setnchannels(1)
        w.setsampwidth(2)
        w.setframerate(RATE)
        w.writeframes(b"".join(struct.pack("<h", int(max(-1, min(1, s * scale)) * 32767)) for s in samples))
    print(f"{name}.wav  {len(samples) / RATE:.2f}s")


C5, D5, E5, G5, C6, E6 = 523.25, 587.33, 659.25, 783.99, 1046.50, 1318.51

# A bubble popping: quick downward blip.
save("pop", mix([(0, tone(1000, 450, 0.18, 18))], 0.2))

# The monster eating: two soft low "nom nom" bursts.
save("chomp", mix([(0.0, thump(200, 90, 0.14, 16)), (0.17, thump(180, 80, 0.16, 14))], 0.36))

# Correct answer: gentle rising three-note chime.
save("chime", mix([(0.0, tone(C5, C5, 0.5, 6, 0.3)), (0.13, tone(E5, E5, 0.5, 6, 0.3)),
                   (0.26, tone(G5, G5, 0.6, 6, 0.3))], 0.9))

# Not quite: a soft, low, friendly "boop" (deliberately quiet and never harsh).
save("soft", mix([(0, tone(392, 349, 0.24, 9))], 0.26), gain=0.5)

# A flower blooming: sparkly rising notes.
save("bloom", mix([(0.00, tone(E5, E5, 0.4, 7, 0.25)), (0.09, tone(G5, G5, 0.4, 7, 0.25)),
                   (0.18, tone(C6, C6, 0.45, 7, 0.25)), (0.27, tone(E6, E6, 0.6, 6, 0.25))], 0.95))

# Round complete: a short, cheerful fanfare.
save("fanfare", mix([(0.00, tone(C5, C5, 0.35, 5, 0.3)), (0.14, tone(E5, E5, 0.35, 5, 0.3)),
                     (0.28, tone(G5, G5, 0.35, 5, 0.3)),
                     (0.42, tone(C6, C6, 0.9, 3.5, 0.3)), (0.42, tone(E5, E5, 0.9, 3.5, 0.2)),
                     (0.42, tone(G5, G5, 0.9, 3.5, 0.2))], 1.4))
