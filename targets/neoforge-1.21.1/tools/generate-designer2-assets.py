#!/usr/bin/env python3
"""One-block prototype resources. Does not write shared assets or other targets.
Frame/background PNGs in src/designer2/resources are supplied artwork; not generated.
"""
from pathlib import Path
import copy,json,re
T=Path(__file__).resolve().parents[1];old=T/'src/main/resources';res=T/'src/designer2/resources';A=res/'assets/sign_plates';O=old/'assets/sign_plates'
order=json.loads((T.parents[1]/'scripts/plate-symbols-v5.json').read_text())
def write(path,data):path.parent.mkdir(parents=True,exist_ok=True);path.write_text(json.dumps(data,indent=2)+'\n')
# Only remove owned, obsolete generated files; retain the legacy geometry inputs.
for relative in ['assets/sign_plates/models/block/plate_empty.json','assets/sign_plates/models/item/plate_empty.json','assets/sign_plates/blockstates/plate_empty.json','data/sign_plates/recipe/plate_empty_recipe.json','data/sign_plates/loot_table/blocks/plate_empty.json']:
 (res/relative).unlink(missing_ok=True)
base=json.loads((O/'models/block/base_0.json').read_text());body=base['elements'][0]
item=json.loads((O/'models/item/plate_empty.json').read_text());display=item['display'];itembody=item['elements'][0]
# Keep generated models aligned with the runtime choices in one catalogue.
catalog=(T/'src/main/java/signplates/PlateLayers.java').read_text()
def layers(name):
 section=re.search(r'\b'+name+r'\s*=\s*List\.of\((.*?)\);',catalog,re.S)
 assert section, 'Missing layer catalogue: '+name
 choices=dict(re.findall(r'new\s+Layer\s*\(\s*"([^"]+)"\s*,\s*"([^"]*)"\s*\)',section.group(1)))
 assert choices, 'Empty layer catalogue: '+name
 return choices
backgrounds={name:tex for name,tex in layers('BACKGROUNDS').items() if tex}
for context in ['block','item']:
 for name,tex in backgrounds.items():
  part=copy.deepcopy(body if context=='block' else itembody)
  for face in part['faces'].values():face['texture']='#layer';face.pop('tintindex',None)
  write(A/f'models/{context}/layers/background_{name}.json',{'parent':'minecraft:block/block','textures':{'layer':tex,'particle':tex},'elements':[part],'render_type':'minecraft:cutout'})
 for name,tex in layers('FRAMES').items():
  if not tex:continue
  # Keep the supplied 16px PNGs untouched. Their visible 12px frame is inset
  # by 2px; matching geometry/UVs avoids sampling the atlas sprite's edge.
  if context=='block':elements=[{'from':[2,1.015,2],'to':[14,1.015,14],'shade':True,'faces':{'up':{'texture':'#layer','uv':[14,14,2,2]}}}]
  else:elements=[{'from':[2,2,8.51],'to':[14,14,8.51],'shade':True,'faces':{'south':{'texture':'#layer','uv':[2,2,14,14]}}}]
  write(A/f'models/{context}/layers/frame_{name}.json',{'parent':'minecraft:block/block','textures':{'layer':tex,'particle':tex},'elements':elements,'render_type':'minecraft:cutout'})
 for name in order[1:]:
  for variant in ['original','tint','glow']:
   src=json.loads((O/f'models/block/symbols/{name}_{variant}.json').read_text());element=src['elements'][0]
   if name=='light' and variant in ['tint','glow']:
    src['textures']={'symbol':'sign_plates:block/symbols/light_mask','particle':'sign_plates:block/symbols/light_mask'}
   if context=='block':elements=[copy.deepcopy(element)];elements[0]['from'][1]=elements[0]['to'][1]=1.03
   else:
    itemvariant={'original':'','tint':'_tinted','glow':'_glowing'}[variant]
    source=json.loads((O/f'models/item/plate_{name}{itemvariant}.json').read_text())
    # GUI/hand item front is +Z (south). The rear must only show background.
    elements=copy.deepcopy([e for e in source['elements'][-2:] if 'south' in e['faces']])
    for e in elements:
     for point in ['from','to']:e[point][2]=8.52
   write(A/f'models/{context}/layers/{name}_{variant}.json',{'parent':'minecraft:block/block','textures':src['textures'],'elements':elements,'ambientocclusion':variant!='glow','render_type':'minecraft:cutout'})
write(A/'models/block/sign_plate.json',{'parent':'minecraft:block/block','textures':{'particle':'sign_plates:block/backgrounds/iron'},'elements':[],'render_type':'minecraft:cutout'})
write(A/'models/item/sign_plate.json',{'parent':'sign_plates:block/sign_plate','display':display,'gui_light':'front','render_type':'minecraft:cutout'})
write(A/'blockstates/sign_plate.json',{'variants':{'':{'model':'sign_plates:block/sign_plate'}}})
recipe=json.loads((old/'data/sign_plates/recipe/plate_empty_recipe.json').read_text());recipe['result']['id']='sign_plates:sign_plate'
write(res/'data/sign_plates/recipe/sign_plate_recipe.json',recipe)
write(res/'data/sign_plates/loot_table/blocks/sign_plate.json',{'type':'minecraft:block','pools':[{'rolls':1,'conditions':[{'condition':'minecraft:survives_explosion'}],'entries':[{'type':'minecraft:item','name':'sign_plates:sign_plate'}]}]})
write(res/'data/sign_plates/tags/item/plates.json',{'replace':True,'values':['sign_plates:sign_plate']})
lang=json.loads((O/'lang/en_us.json').read_text());lang.update({
 'block.sign_plates.plate_empty':'Sign Plate',
 'block.sign_plates.sign_plate':'Sign Plate',
 'tooltip.sign_plates.symbol_origin':'Made by modifying a placed Sign Plate.',
 'screen.sign_plates.background_tab':'Background','screen.sign_plates.frame_heading':'Frame','screen.sign_plates.background_heading':'Background','screen.sign_plates.search_frames':'Search frames and backgrounds...',
 'screen.sign_plates.frame_none':'No frame','screen.sign_plates.frame_iron':'Iron','screen.sign_plates.frame_copper':'Copper','screen.sign_plates.frame_gold':'Gold',
 'screen.sign_plates.background_none':'Transparent','screen.sign_plates.background_iron':'Iron','screen.sign_plates.background_spruce':'Spruce'})
for name in backgrounds:
 lang.setdefault('screen.sign_plates.background_'+name,name.replace('_',' ').title())
for symbol in order[1:]:
 key='block.sign_plates.plate_'+symbol
 lang[key]='Sign Plate: '+lang[key].removesuffix(' Sign Plate')
write(A/'lang/en_us.json',lang)
write(res/'pack.mcmeta',{'pack':{'pack_format':34,'description':'Sign Plates resources'}})
print('Generated one blockstate definition, one recipe/drop and',len(list((A/'models').rglob('*.json'))),'independent layer models.')
