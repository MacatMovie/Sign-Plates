#!/usr/bin/env python3
"""Create crisp editable vanilla-style designer PNGs. Requires Pillow.
Run only to reset the supplied artwork; overwrites manual edits to these PNGs.
"""
from pathlib import Path
from PIL import Image, ImageDraw
root=Path(__file__).resolve().parents[1]
out=root/'targets/neoforge-1.21.1/src/main/resources/assets/sign_plates/textures/gui/designer/vanilla'
out.mkdir(parents=True,exist_ok=True)
panel=Image.new('RGBA',(16,16));d=ImageDraw.Draw(panel)
d.rounded_rectangle((0,0,15,15),radius=3,fill='#000000')
d.rounded_rectangle((1,1,14,14),radius=2,fill='#FFFFFF')
d.polygon([(14,3),(14,13),(13,14),(3,14),(2,13),(13,13),(13,2)],fill='#555555')
d.rectangle((3,3,12,12),fill='#C6C6C6')
panel.save(out/'panel.png')
def inset(size):
 im=Image.new('RGBA',size,'#8B8B8B');d=ImageDraw.Draw(im);w,h=size
 d.line((0,0,w-2,0),fill='#373737');d.line((0,0,0,h-2),fill='#373737')
 d.line((1,h-1,w-1,h-1),fill='#FFFFFF');d.line((w-1,1,w-1,h-1),fill='#FFFFFF')
 return im
inset((8,8)).save(out/'inset.png')
inset((22,22)).save(out/'slot.png')
thumb=Image.new('RGBA',(6,15),'#C6C6C6');d=ImageDraw.Draw(thumb)
d.line((0,0,4,0),fill='#FFFFFF');d.line((0,0,0,13),fill='#FFFFFF')
d.line((1,14,5,14),fill='#555555');d.line((5,1,5,14),fill='#555555')
thumb.save(out/'scrollbar.png')
print('Created 4 editable vanilla-style designer textures:',out)
