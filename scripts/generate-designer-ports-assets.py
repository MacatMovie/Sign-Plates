#!/usr/bin/env python3
"""Generate all other resource formats from the approved NeoForge 1.21.1 designer.
Original PNGs are copied byte-for-byte. No merged symbol/background textures.
"""
from pathlib import Path
import json,shutil,copy
R=Path(__file__).resolve().parents[1];T=R/'targets/neoforge-1.21.1';SRC=T/'src/designer2/resources';ART=T/'src/main/resources'
def write(p,d):p.parent.mkdir(parents=True,exist_ok=True);p.write_text(json.dumps(d,indent=2)+'\n')
def convert(d,modern):
 if isinstance(d,list):return [convert(x,modern) for x in d]
 if not isinstance(d,dict):return d
 d={k:convert(v,modern) for k,v in d.items()}
 if 'neoforge_data' in d:
  d['light_emission' if modern else 'forge_data']=15 if modern else d['neoforge_data'];del d['neoforge_data']
 if modern:
  d.pop('render_type',None)
  if 'faces' in d:
   emits=[f.pop('light_emission',0) for f in d['faces'].values()]
   if any(emits):d['light_emission']=15
 return d
for v in ['1.20.1','1.21.1','26.1','26.2','26.3']:
 modern=v.startswith('26.');dst=R/'shared'/('designer-'+v)/'resources'
 if dst.exists():shutil.rmtree(dst)
 shutil.copytree(SRC,dst,ignore=shutil.ignore_patterns('META-INF','sign_plates.mixins.json','pack.mcmeta'))
 for pattern in ['logo.png','assets/sign_plates/textures/block/symbols/**/*','assets/sign_plates/textures/gui/**/*']:
  for p in ART.glob(pattern):
   if p.is_file():f=dst/p.relative_to(ART);f.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(p,f)
 # Ignore stale aliases that may remain when a full archive overlays an old checkout.
 for old in ['assets/sign_plates/blockstates/plate_empty.json','assets/sign_plates/models/block/plate_empty.json','assets/sign_plates/models/item/plate_empty.json','data/sign_plates/recipe/plate_empty_recipe.json','data/sign_plates/loot_table/blocks/plate_empty.json']:(dst/old).unlink(missing_ok=True)
 a=dst/'assets/sign_plates' 
 display=json.loads((SRC/'assets/sign_plates/models/item/sign_plate.json').read_text())['display']
 for p in list((a/'models').rglob('*.json')):
  d=convert(json.loads(p.read_text()),modern)
  if modern and p.parent.name=='layers' and '/item/' in p.as_posix():
   d['display']=copy.deepcopy(display);d['gui_light']='front'
   preview=copy.deepcopy(d);preview['display']['gui']['rotation']=[30,-45,0]
   write(a/'models/item/preview/layers'/p.name,preview)
  write(p,d)
 if modern:
  for angled in [False,True]:write(a/'items'/('preview/sign_plate.json' if angled else 'sign_plate.json'),{'model':{'type':'sign_plates:plate','angled':angled}})
 if v=='1.20.1':
  for ns in (dst/'data').iterdir():
   for singular,plural in [('recipe','recipes'),('loot_table','loot_tables')]:
    if (ns/singular).exists():(ns/singular).rename(ns/plural)
   if (ns/'tags/item').exists():(ns/'tags/item').rename(ns/'tags/items')
  for p in (dst/'data').rglob('recipes/*.json'):
   d=json.loads(p.read_text())
   if 'id' in d.get('result',{}):d['result']['item']=d['result'].pop('id')
   write(p,d)
 if modern:
  for p in (dst/'data').rglob('recipe/*.json'):
   d=json.loads(p.read_text())
   if 'ingredients' in d:d['ingredients']=[x['item'] if isinstance(x,dict) else x for x in d['ingredients']]
   write(p,d)
  if v=='26.3':
   for p in (dst/'data').rglob('loot_table/**/*.json'):
    d=json.loads(p.read_text())
    for pool in d.get('pools',[]):
     conditions=pool.pop('conditions',[])
     if conditions:pool['condition']={'type':conditions[0]['condition']}
    write(p,d)
 pack={'pack_format':15 if v=='1.20.1' else 34,'description':'Sign Plates resources'}
 if modern:
  # Resource pack versions in the official version metadata.
  versions={'26.1':84,'26.2':88,'26.3':97};pack={'description':'Sign Plates resources','min_format':[versions[v],0],'max_format':[{'26.1':101,'26.2':107,'26.3':121}[v],999]}
 write(dst/'pack.mcmeta',{'pack':pack})
 print(v,len(list((a/'models').rglob('*.json'))),'models')
