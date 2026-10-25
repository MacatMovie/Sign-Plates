#!/usr/bin/env python3
"""Regenerate only the NeoForge 1.21.1 experimental assets. No build dependency.
Run from any directory. Uses Python 3 standard library, preserves PNG bytes.
The symbol ordering is kept in plate-symbols-v5.json and PlateCatalog.java.
"""
from pathlib import Path
import json, shutil, re
root = Path(__file__).resolve().parents[1]
target = root / 'targets/neoforge-1.21.1'
res = target / 'src/main/resources'
assets = res / 'assets/sign_plates'
data = res / 'data/sign_plates'
tex = root / 'art/signplates-v5'
symbols = {p.stem.replace('Plate_', '').lower().replace('l minus','minus'): p for p in (tex / 'original symbols').glob('*.png')}
order = json.loads((root / 'scripts/plate-symbols-v5.json').read_text())
assert len(order) < 100 and set(order) == {'empty', *symbols}
# Copy unchanged root icon, pack metadata and English messages into the isolated target.
res.mkdir(parents=True,exist_ok=True)
for f in (root/'shared/common/resources').iterdir():
 if f.is_file():shutil.copy2(f,res/f.name)
lang=json.loads((root/'shared/asset-variants/resources/assets/sign_plates/lang/en_us.json').read_text())
# Models keep the established 14x14x1 body and orientation; symbols use native pixel density.
import struct
def image_width(path):
    return struct.unpack(">I", path.read_bytes()[16:20])[0]
base=json.loads((root/'shared/common/resources/assets/sign_plates/models/block/plate_sign.json').read_text())['elements'][0]
base.pop('rotation',None)
for f in base['faces'].values():f.pop('tintindex',None);f['texture']='#base'
# Item geometry is authored upright in XY, centred at Z=8. Do not inherit
# floor-mounted block transforms. Each side has readable, upright artwork.
import copy
display={
 'gui':{'rotation':[0,0,0],'translation':[0,0,0],'scale':[0.9,0.9,0.9]},
 'fixed':{'rotation':[0,0,0],'translation':[0,0,0],'scale':[0.6,0.6,0.6]},
 'ground':{'rotation':[90,0,0],'translation':[0,2,0],'scale':[0.5,0.5,0.5]}
}
# Minecraft mirrors the left-hand transform itself; both authored transforms
# must match, otherwise the right hand turns away from the player.
for context,yaw,scale in [('firstperson_righthand',-35,0.6),('firstperson_lefthand',-35,0.6),
                          ('thirdperson_righthand',-35,0.55),('thirdperson_lefthand',-35,0.55)]:
 display[context]={'rotation':[0,yaw,0],'translation':[0,3,0],'scale':[scale,scale,scale]}
item_base={'from':[1,1,7.5],'to':[15,15,8.5],'faces':{
 'north':{'uv':[1,1,15,15],'texture':'#base'},
 'south':{'uv':[1,1,15,15],'texture':'#base'},
 'east':{'uv':[15,1,16,15],'texture':'#base'},
 'west':{'uv':[0,1,1,15],'texture':'#base'},
 'up':{'uv':[1,0,15,1],'texture':'#base'},
 'down':{'uv':[1,15,15,16],'texture':'#base'}
}}
def item_symbols(element):
 size=element['to'][0]-element['from'][0]
 result=[]
 for side,z in [('north',7.49),('south',8.51)]:
  face=copy.deepcopy(element['faces']['up']);face['uv']=[0,0,16,16]
  result.append({'from':[(16-size)/2,(16-size)/2,z],
                 'to':[(16+size)/2,(16+size)/2,z],
                 'shade':element['shade'],'faces':{side:face}})
 return result
def write(p,d):p.parent.mkdir(parents=True,exist_ok=True);p.write_text(json.dumps(d,indent=2)+'\n')
for m,background in enumerate(['iron','wood-spruce','transparent']):
 if m==2:
  write(assets/'models/block/base_2.json',{'parent':'minecraft:block/block','render_type':'minecraft:cutout','textures':{'particle':'sign_plates:block/backgrounds/iron'},'elements':[]})
  continue
 dest=assets/'textures/block/backgrounds'/f'{background}.png';dest.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(tex/'backgrounds'/f'plate-background-{background}.png',dest)
 write(assets/'models/block'/f'base_{m}.json',{'parent':'minecraft:block/block','render_type':'minecraft:cutout','textures':{'base':f'sign_plates:block/backgrounds/{background}','particle':f'sign_plates:block/backgrounds/{background}'},'elements':[base]})
tints=re.findall(r'^    [A-Z_]+\("([^\"]+)"', (root/'shared/common/java/signplates/PlateTint.java').read_text(),re.M)
orientations=[]
for face,x in [('floor',0),('wall',90),('ceiling',180)]:
 for i,facing in enumerate(['north','east','south','west']):
  y=(i*90+(180 if face=='ceiling' else 0))%360;rot={}
  if x:rot['x']=x
  if y:rot['y']=y
  orientations.append(({'face':face,'facing':facing},rot))
allids=[]
for index,name in enumerate(order):
 symbol_elements={}
 if name!='empty':
  original=symbols[name];mask=next((p for p in (tex/'additional - emissive - tint - textures').glob('*.png') if p.stem.lower()==original.stem.lower()+'-glow-mask'),original)
  size=image_width(original)
  for variant,texture in [('original',original),('tint',mask),('glow',mask)]:
   texname=f'{name}'+('_mask' if texture!=original else '')
   dst=assets/'textures/block/symbols'/f'{texname}.png';dst.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(texture,dst)
   face={'uv':[16,16,0,0],'texture':'#symbol'}
   if variant!='original' or mask==original:face['tintindex']=0
   if variant=='glow':face['neoforge_data']={'block_light':15,'sky_light':15,'ambient_occlusion':False}
   element={'from':[(16-size)/2,1.01,(16-size)/2],'to':[(16+size)/2,1.01,(16+size)/2],'shade':variant!='glow','faces':{'up':face}}
   symbol_elements[variant]=(element,f'sign_plates:block/symbols/{texname}')
   write(assets/'models/block/symbols'/f'{name}_{variant}.json',{'parent':'minecraft:block/block','render_type':'minecraft:cutout','ambientocclusion':variant!='glow','textures':{'symbol':symbol_elements[variant][1],'particle':symbol_elements[variant][1]},'elements':[element]})
 for m,background in enumerate(['iron','wood-spruce','transparent']):
  id=('plate_' if m==0 else 'spruce_plate_' if m==1 else 'clear_plate_')+name;allids.append('sign_plates:'+id)
  parts=[]
  for cond,rot in orientations:
   parts.append({'when':cond,'apply':{'model':f'sign_plates:block/base_{m}',**rot}})
   if name!='empty':
    for variant,extra in [('original',{'glow':'false','tint':'none'}),('tint',{'glow':'false','tint':'|'.join(t for t in tints if t!='none')}),('glow',{'glow':'true'})]:
     parts.append({'when':{**cond,**extra},'apply':{'model':f'sign_plates:block/symbols/{name}_{variant}',**rot}})
  write(assets/'blockstates'/f'{id}.json',{'multipart':parts})
  item={'parent':'minecraft:block/block','render_type':'minecraft:cutout','gui_light':'front','textures':{'base':f'sign_plates:block/backgrounds/{background}','particle':f'sign_plates:block/backgrounds/{background}'},'elements':[] if m==2 else [item_base],'display':display}
  if m==2:item['textures']={'particle':'sign_plates:block/backgrounds/iron'}
  if name!='empty':item['elements']=item['elements']+item_symbols(symbol_elements['original'][0]);item['textures']['symbol']=symbol_elements['original'][1]
  if name!='empty':
   import copy
   overrides=[]
   for suffix,variant,predicate in [('tinted','tint','dyed'),('glowing','glow','glowing')]:
    styled=copy.deepcopy(item)
    styled['elements'][-2:]=item_symbols(symbol_elements[variant][0])
    styled['textures']['symbol']=symbol_elements[variant][1]
    write(assets/'models/item'/f'{id}_{suffix}.json',styled)
    overrides.append({'predicate':{f'sign_plates:{predicate}':1},'model':f'sign_plates:item/{id}_{suffix}'})
   item['overrides']=overrides
  write(assets/'models/item'/f'{id}.json',item)
  write(data/'loot_table/blocks'/f'{id}.json',{'type':'minecraft:block','pools':[{'rolls':1,'conditions':[{'condition':'minecraft:survives_explosion'}],'entries':[{'type':'minecraft:item','name':'sign_plates:plate_empty'}]}]})
  oldname=lang.get('block.sign_plates.plate_'+name)
  fallback={'empty':'Blank','arrowup':'Up Arrow','arrowdown':'Down Arrow','arrowleft':'Left Arrow','arrowright':'Right Arrow','facehappy':'Happy Face','facemad':'Angry Face','facesad':'Sad Face','faceneutral':'Neutral Face','facecat':'Cat Face','sandclock':'Hourglass'}
  label=fallback.get(name,name.upper() if len(name)==1 else name.title())+' Sign Plate'
  lang['block.sign_plates.'+id]=('Frameless ' if m==2 else 'Spruce ' if m else '')+label
# Alternate frame items remain registered for world/item compatibility but are
# absent from creative and both JEI/EMI indexes. Survival crafting produces only the default blank plate.
for path in (data/'recipe/stonecutting').glob('*.json'):
 path.unlink()
write(res/'data/c/tags/item/hidden_from_recipe_viewers.json',{'replace':False,'values':[id for id in allids if not id.startswith('sign_plates:plate_')]})
write(data/'tags/item/plates.json',{'replace':False,'values':allids})
recipe=root/'shared/recipes-id-result/resources/data/sign_plates/recipe/plate_empty_recipe.json';write(data/'recipe/plate_empty_recipe.json',json.loads(recipe.read_text()))
lang.update({'tooltip.sign_plates.interactions':'Right-click a placed Sign Plate to modify it.', 'tooltip.sign_plates.finish':'Can glow and be dyed. Use a sponge to clear.', 'screen.sign_plates.editor':'Sign Plate Designer','screen.sign_plates.search':'Search symbols...', 'screen.sign_plates.apply':'Apply','screen.sign_plates.cancel':'Cancel','screen.sign_plates.iron':'Iron','screen.sign_plates.spruce':'Spruce','screen.sign_plates.all':'All','screen.sign_plates.letters':'A–Z','screen.sign_plates.numbers':'0–9','screen.sign_plates.arrows':'Arrows','screen.sign_plates.faces':'Faces','screen.sign_plates.icons':'Icons','screen.sign_plates.empty':'No matching symbols','screen.sign_plates.stack':'Edits the entire held stack','screen.sign_plates.placed':'Keeps the plate’s dye and glow','screen.sign_plates.denied':'This plate cannot be edited here.'})
lang.update({
 'screen.sign_plates.frames':'Frames', 'screen.sign_plates.symbols':'Symbols',
 'screen.sign_plates.search_frames':'Search frames...',
 'screen.sign_plates.style':'Style', 'screen.sign_plates.transparent':'No frame',
 'screen.sign_plates.colour':'Colour', 'screen.sign_plates.no_colour':'No colour (original texture)',
 'screen.sign_plates.glow_on':'Glow: ON', 'screen.sign_plates.glow_off':'Glow: OFF',
 'screen.sign_plates.missing_glow':"You're missing the required glow ink sac item.",
 'screen.sign_plates.glow_remove':'Click to remove glow for free.',
 'screen.sign_plates.glow_cost':'Click to enable glow. Costs 1 glow ink sac per change.',
 'screen.sign_plates.style_cost':'Cost: 1 item per change'
})
lang.update({'screen.sign_plates.preview': 'Preview', 'screen.sign_plates.glow': 'Glow', 'screen.sign_plates.on': 'ON', 'screen.sign_plates.off': 'OFF', 'screen.sign_plates.style_preview': 'Preview only. Press Apply to save style changes.', 'screen.sign_plates.missing_materials': 'Missing required materials. You can still preview any style.', 'screen.sign_plates.apply_style': 'Apply colour and glow together. Only changed effects cost materials.', 'screen.sign_plates.leave': 'Leave without applying', 'screen.sign_plates.return_style': 'Go to Style', 'screen.sign_plates.unapplied_title': 'Unapplied style changes', 'screen.sign_plates.unapplied_message': 'Your style effects have not been applied. Leave without applying, or return to the Style tab?'})
lang['screen.sign_plates.no_style_changes']='No style changes to apply.'
lang.update({'screen.sign_plates.rotate_preview': 'Rotate preview', 'screen.sign_plates.preview_front': 'Show front view', 'screen.sign_plates.preview_angled': 'Show angled view'})
lang.update({'tooltip.sign_plates.hold_shift': 'Hold [Shift] to show more info.', 'tooltip.sign_plates.blank_edit': 'Right-click a placed Sign Plate to modify it.', 'tooltip.sign_plates.blank_style': 'Can glow and be dyed. Use a sponge to clear.', 'tooltip.sign_plates.symbol_origin': 'Made by right-clicking a placed Blank Sign Plate.'})
write(assets/'lang/en_us.json',lang)

print('Regenerated NeoForge 1.21.1 assets for', len(order), 'symbols including blank.')
