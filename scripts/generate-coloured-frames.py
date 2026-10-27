#!/usr/bin/env python3
"""Recreate v1.2.1 colored frame PNGs from the original supplied 16x16 texture.

Use script after updating the original texture or the PlateTint palette; then run
`python scripts/generate-designer-ports-assets.py` to sync other versions.
"""
from pathlib import Path
from PIL import Image
import re, json
R=Path(__file__).resolve().parents[1]
colors=['white','orange','magenta','light_blue','yellow','lime','pink','gray','light_gray','cyan','purple','blue','brown','green','red','black','maroon','rose','coral','indigo','navy','slate','olive','amber','beige','teal','mint','aqua','verdant','forest','ginger','tan']
depot=colors[16:]
template=Image.open(R/'art/signplates-v5/frames/plate-frame-white.png').convert('RGBA')
assert template.size==(16,16), 'Expected a 16x16 frame sprite'
txt=(R/'shared/common/java/signplates/PlateTint.java').read_text()
colors_rgb={name:int(rgb,16) for name,rgb in re.findall(r'\("([^"]+)",\s*(?:DyeColor\.\w+|null),\s*0x([0-9a-fA-F]{6})',txt)}
assets=R/'targets/neoforge-1.21.1/src/designer2/resources/assets/sign_plates'
for color in colors:
 rgb=colors_rgb[color];r,g,b=(rgb>>16)&255,(rgb>>8)&255,rgb&255
 png=Image.new('RGBA',(16,16))
 png.putdata([(int(r*(pr/255)),int(g*(pr/255)),int(b*(pr/255)),a) for pr,pg,pb,a in template.getdata()])
 png.save(assets/f'textures/block/frames/dyed_{color}.png',optimize=True)
 for section in ['block','item']:
  model_path=assets/f'models/{section}/layers/frame_dyed_{color}.json'
  model=json.loads((assets/f'models/{section}/layers/frame_iron.json').read_text())
  model['textures']={k:(f'sign_plates:block/frames/dyed_{color}' if k in ('layer','particle') else v) for k,v in model['textures'].items()}
  model_path.write_text(json.dumps(model,indent=2)+'\n')
for color in depot:
 for section in ['block','item']:
  model=json.loads((assets/f'models/{section}/layers/background_red_concrete.json').read_text())
  model['textures']={k:(f'dye_depot:block/{color}_concrete' if k in ('layer','particle') else v) for k,v in model['textures'].items()}
  (assets/f'models/{section}/layers/background_dye_depot_{color}_concrete.json').write_text(json.dumps(model,indent=2)+'\n')
print(f'Regenerated {len(colors)} colored frame textures and {len(depot)} optional Dye Depot backgrounds')
