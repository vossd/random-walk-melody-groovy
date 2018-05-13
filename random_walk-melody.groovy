import javax.sound.midi.*
import java.time.LocalDateTime
import java.util.Random

//Enter movement number 1-3 ("allegro", "adagio", "presto")
def movement = 3

//Enter duration in seconds
def length = 120

def durs

switch (movement) {
    case 1:
        durs = [160, 160, 160, 160, 320, 480]
        break
    case 2:
        durs = [320, 320, 320, 320, 640, 960]
        break
    case 3:
        durs = [80, 80, 80, 80, 160, 240]
        break
}

def tenor = 64

int[] s1 = [-3, -2, 2, 3]
int[] s2 = [-6, -4, -2, 2, 4, 6]
int[] s3 = [-5, -4, -1, 1, 4, 5]
int[] s4 = [-5, -2, 2, 5]
int[] s5 = [-5, -4, -2, 2, 4, 5]
int[] s6 = [-5, -3, -2, 2, 3, 5]
int[] s7 = [-5, -4, -3, -2, 2, 3, 4, 5]
int[] s8 = [-7, -5, -4, -3, -2, 2, 3, 4, 5, 7]
int[] s9 = [-7, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7]
List stepsList = [s1, s2, s3, s4, s5, s6, s7, s8, s9]
currentSteps = []

Synthesizer synth = MidiSystem.getSynthesizer()
synth.open()
Soundbank sb = synth.getDefaultSoundbank()
synth.loadAllInstruments(sb)
Instrument[] inst = synth.getLoadedInstruments()
Patch p1 = inst[0].getPatch()
Patch p2 = inst[0].getPatch()
Patch p3 = inst[0].getPatch()
MidiChannel[] mc = synth.getChannels()
mc[1].programChange(p1.getBank(), p1.getProgram())
mc[2].programChange(p2.getBank(), p2.getProgram())
mc[3].programChange(p3.getBank(), p3.getProgram())

time = LocalDateTime.now()
Random r = new Random()

while (LocalDateTime.now().isBefore(time.plusSeconds(length))) {
    currentSteps = stepsList.get(r.nextInt(9))
    println currentSteps
    innerTime = LocalDateTime.now()
    duration = r.nextInt(9) + 4
    while (LocalDateTime.now().isBefore(innerTime.plusSeconds(duration))) {
        tenor += currentSteps[r.nextInt(currentSteps.length)]
        if (tenor < 40) {
            tenor += 12
        }
        if (tenor > 88) {
            tenor -= 12
        }
        mc[1].noteOn(tenor, 1000)
        mc[2].noteOn(tenor - 12, 1000)
        mc[3].noteOn(tenor - 24, 1000)
        sleep(durs[r.nextInt(6)])
        mc[1].noteOff(tenor, 1000)
        mc[2].noteOff(tenor - 12, 1000)
        mc[3].noteOff(tenor - 24, 1000)
        if (r.nextInt(10).equals(9)) {
            sleep(240)
        }
    }
}
mc[1].noteOn(tenor, 1000)
mc[2].noteOn(tenor - 12, 1000)
mc[3].noteOn(tenor - 24, 1000)
sleep(3000)
mc[1].noteOff(tenor, 1000)
mc[2].noteOff(tenor - 12, 1000)
mc[3].noteOff(tenor - 24, 1000)

if (LocalDateTime.now().isAfter(time.plusSeconds(length + 5))) {
    synth.close()
}

