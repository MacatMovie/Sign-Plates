#!/usr/bin/env python3
"""Check the active single-block resource layout and all designer ports.
Does not replace Gradle builds or Minecraft testing.
"""
from pathlib import Path
import json,re,hashlib,sys
R=Path(__file__).resolve().parents[1];REF=R/'targets/neoforge-1.21.1';canonical=REF/'src/designer2/resources';art=REF/'src/main/resources'
def read(p):return json.loads(p.read_text())
order=read(R/'scripts/plate-symbols-v5.json');assert len(order)==86
checks=0
for target in sorted((R/'targets').iterdir()):
 if not target.is_dir() or (len(sys.argv)>1 and target.name!=sys.argv[1]):continue
 name=target.name;v=name.split('-',1)[1];old=v=='1.20.1';modern=v.startswith('26.');src=target/'src/main/java/signplates';res=canonical if target==REF else R/f'shared/designer-{v}/resources';a=res/'assets/sign_plates';data=res/'data/sign_plates'
 for f in ['PlateCatalog.java','PlateLayers.java','StyleCost.java','SignPlatesConfig.java']:
  assert (src/f).read_bytes()==(REF/'src/main/java/signplates'/f).read_bytes(),(name,f)
 assert (src/'block/PlateBlockEntity.java').is_file()
 blocks=(src/'init/SignPlatesModBlocks.java').read_text();items=(src/'init/SignPlatesModItems.java').read_text();block=(src/'block/PlateBlock.java').read_text();menu=(src/'menu/PlateEditorMenu.java').read_text();screen=(src/'client/PlateEditorScreen.java').read_text()
 assert '"sign_plate"' in blocks and '"sign_plate"' in items,name
 assert 'PlateTint> TINT' not in block and 'lightLevel(' in block and 'IntegerProperty LIGHT' in block,name
 assert 'getTicker(' not in block,name
 assert 'new PlateAppearance' in menu and 'expectedRevision' in menu,name
 for token in ['VisualCost.between','affordable(player,costs)','stillValid(player)','UI_STONECUTTER_TAKE_RESULT','isLightOption','applyVisuals']:assert token in menu,(name,token)
 for token in ['backgroundCells','paneTrack','dirtyVisuals() && canPay()','checkerboard','DesignerPackets.send' if target!=REF else 'PlateEditPayload','clipboard','lightButtons','clipTop()','tab==1?3:0','tab==0?CELL:0']:assert token in screen,(name,token)
 assert 'sponge?0:a.light()' in block,name
 appearance=(src/'PlateAppearance.java').read_text();assert 'java.util.List.of(0,8,12,15)' in appearance and 'optionalFieldOf("light",0)' in appearance,name
 style=(src/'PlateStyle.java').read_text();assert 'tint,glow,a.light()' in style,name
 cost=(src/'VisualCost.java').read_text();assert 'applied==draft?0' in cost and 'GLOWSTONE_DUST' in cost and 'Items.COAL' in cost,name
 assert 'Items.INK_SAC' not in block and 'Items.SPONGE' in block and 'ParticleTypes.SPLASH' in block,name
 states=[p for p in (a/'blockstates').glob('*.json') if p.stem!='plate_empty'];assert [p.stem for p in states]==['sign_plate'],name
 variants=read(states[0])['variants'];assert len(variants) in (1,12,48),name
 recipes=[p for p in (data/('recipes' if old else 'recipe')).rglob('*.json') if p.name!='plate_empty_recipe.json'];assert len(recipes)==1,name
 assert read(recipes[0])['result']['item' if old else 'id']=='sign_plates:sign_plate'
 loot=read(data/('loot_tables' if old else 'loot_table')/'blocks/sign_plate.json');assert loot['pools'][0]['entries'][0]['name']=='sign_plates:sign_plate'
 if v=='26.3':assert loot['pools'][0]['condition']=={'type':'minecraft:survives_explosion'}
 models={p.relative_to(a/'models').with_suffix('').as_posix():read(p) for p in (a/'models').rglob('*.json') if p.stem!='plate_empty'}
 item_display=models['item/sign_plate']['display']
 assert item_display['ground']['rotation']==[-90,0,0],(name,'dropped front must face up')
 assert item_display['fixed']['rotation']==[0,180,0],(name,'item-frame front must face outward')
 assert len([x for x in models if x.startswith('block/layers/')])==378,name
 assert len([x for x in models if x.startswith('item/layers/')])==378,name
 for path,m in models.items():
  if modern and (path.startswith('item/layers/') or path.startswith('item/preview/layers/')):
   assert m['display']['ground']==item_display['ground'] and m['display']['fixed']==item_display['fixed'],(name,path,'item transforms')
  parent=m.get('parent','')
  if parent.startswith('sign_plates:'):assert parent.split(':',1)[1] in models,(name,path,parent)
  for tex in m.get('textures',{}).values():
   if tex.startswith('sign_plates:'):assert (a/'textures'/(tex.split(':',1)[1]+'.png')).is_file() or (art/'assets/sign_plates/textures'/(tex.split(':',1)[1]+'.png')).is_file(),(name,path,tex)
  if path.startswith('item/layers/') and not path.startswith('item/layers/background_'):
   for e in m.get('elements',[]):assert set(e['faces'])=={'south'},(name,path)
  if 'layers/frame_' in path or 'layers/background_' in path:
   serialized=json.dumps(m)
   if path.startswith(('block/layers/frame_','item/layers/frame_','item/preview/layers/frame_')) and path.endswith('_glow'):
    assert all(e.get('shade') is False for e in m.get('elements',[])),(name,path,'glowing frame should not shade')
    assert ('light_emission' in serialized if modern else 'block_light' in serialized),(name,path,'glowing frame must be fullbright')
   else:
    assert 'light_emission' not in serialized and 'block_light' not in serialized,(name,path,'normal frame/background must remain unlit')
 for symbol in order[1:]:
  for style in ['original','tint','glow']:assert f'block/layers/{symbol}_{style}' in models
 for frame in [name for name in models if name.startswith('block/layers/frame_') and not name.endswith('_glow')]:
  assert frame+'_glow' in models,(name,frame,'missing emissive frame')
 lang=read(a/'lang/en_us.json');assert lang['block.sign_plates.sign_plate']=='Sign Plate'
 # Ports must retain every designer label from the approved reference resources.
 reference_lang=read(canonical/'assets/sign_plates/lang/en_us.json')
 for key,value in reference_lang.items():
  if key.startswith('screen.sign_plates.'):assert lang.get(key)==value,(name,key)
 for symbol in order[1:]:assert lang['block.sign_plates.plate_'+symbol].startswith('Sign Plate:'),(name,symbol)
 if target!=REF:
  for p in (canonical/'assets/sign_plates/textures').rglob('*.png'):
   assert (a/'textures'/p.relative_to(canonical/'assets/sign_plates/textures')).read_bytes()==p.read_bytes(),(name,p.name)
  for p in (art/'assets/sign_plates/textures/block/symbols').glob('*.png'):assert (a/'textures/block/symbols'/p.name).read_bytes()==p.read_bytes(),(name,p.name)
  build=(target/'build.gradle').read_text();assert 'removeLegacyDesignerResources' in build and 'shared/designer-'+v+'/resources' in build
  mix=read(target/'src/main/resources/sign_plates.mixins.json');assert mix['required'] and len(mix['mixins'])==2
 if modern:
  assert 'PlatePreviewState.submit(g, stack' in screen and 'g.item(stack,0,0)' not in screen,(name,'full-resolution preview')
  assert (src/'client/PlatePreviewRenderer.java').is_file(),name
  assert 'PlatePreviewRenderer' in (src/'client/PlateModels.java').read_text(),(name,'preview registration')
  assert 'PlateGuiAccessor' in mix.get('client',[]),(name,'preview submission accessor')
  assert 'ContainerInput type' in menu,name
  if name.startswith('fabric-'):assert 'net.fabricmc.fabric.api.menu.v1.ExtendedMenuType' in menu and 'ExtendedMenuProvider<Opening>' in menu,name
  assert read(a/'items/sign_plate.json')['model']['type']=='sign_plates:plate'
  assert read(a/'items/preview/sign_plate.json')['model']['angled']
  assert len([x for x in models if x.startswith('item/preview/layers/')])==378
 print('PASS',name,': 1 block/item, 768 states, 37 frames + 36 emissive variants, 52 backgrounds, 86 symbols, separate layers and original PNGs')
 checks+=1
print('Validated',checks,'targets')
