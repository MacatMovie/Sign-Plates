#!/usr/bin/env python3
"""Generate fullbright frame model variants for the existing Emissive toggle.

Regenerate after updating frames with generate-coloured-frames.py.
Then run generate-designer-ports-assets.py to publish all MC version formats.
"""
from pathlib import Path
import json
R=Path(__file__).resolve().parents[1]
models=R/'targets/neoforge-1.21.1/src/designer2/resources/assets/sign_plates/models'
frames=sorted(p for p in models.rglob('frame_*.json') if not p.stem.endswith('_glow'))
for p in frames:
    model=json.loads(p.read_text())
    for element in model.get('elements',[]):
        element['shade']=False
        for face in element.get('faces',{}).values():
            face['neoforge_data']={'block_light':15,'sky_light':15,'ambient_occlusion':False}
    model['ambientocclusion']=False
    p.with_name(p.stem+'_glow.json').write_text(json.dumps(model,indent=2)+'\n')
print(f'Generated {len(frames)} emissive frame models.')
