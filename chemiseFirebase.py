from firebase import firebase
from evdev import *

firebase = firebase.FirebaseApplication('https://testakkk.firebaseio.com', None)


dev = InputDevice('/dev/input/event0')

print(dev)

i=0

speed_dial=['KEY_INSERT','KEY_HOME','KEY_PAGEDOWN','KEY_PAGEUP','KEY_DELETE','KEY_END'] #0
answer_call=['KEY_NUMLOCK','KEY_KPSLASH','KEY_KPASTERISK','KEY_KP7','KEY_KP8','KEY_KP9'] #1
end_call=['KEY_KP1','KEY_KP2','KEY_KP3','KEY_KP0','KEY_KPDOT'] #2
play_pause=['KEY_R','KEY_T','KEY_Y','KEY_U','KEY_F','KEY_G','KEY_H','KEY_C','KEY_V','KEY_B','KEY_N'] #3
next_track=['KEY_O','KEY_P','KEY_LEFTBRACE','KEY_L','KEY_SEMICOLON','KEY_APOSTROPHE','KEY_COMMA','KEY_DOT','KEY_SLASH','KEY_K'] #4
prev_track=['KEY_TAB','KEY_Q','KEY_W','KEY_CAPSLOCK','KEY_A','KEY_S','KEY_LEFTSHIFT','KEY_Z',] #5
vol_up=['KEY_F9','KEY_F10','KEY_F11','KEY_F12'] #6
vol_down=['KEY_F5','KEY_F6','KEY_F7','KEY_F8'] #7
vol_mute=['KEY_F1','KEY_F2','KEY_F3','KEY_4'] #8


for event in dev.read_loop():
    if event.type == ecodes.EV_KEY:
       button=categorize(event)
       if button.keystate==1:
           if button.keycode in vol_mute:
               op='8'
           elif button.keycode in vol_down:
               op='7'
           elif button.keycode in vol_up:
               op='6'
           elif button.keycode in prev_track:
               op='5'
           elif button.keycode in next_track:
               op='4'
           elif button.keycode in play_pause:
               op='3'
           elif button.keycode in end_call:
               op='2'
           elif button.keycode in answer_call:
               op='1'
           elif button.keycode in speed_dial:
               op='0'
           else:
               op=''
           if not op=='':
               firebase.put('/','data',op+' '+str(i))
               i=(i+1)%2


