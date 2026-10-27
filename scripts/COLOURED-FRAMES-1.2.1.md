# New frames and Dye Depot options (v1.2.1)

- `dyed_*` frame variants use `art/signplates-v5/frames/plate-frame-white.png` as the reference 16x16 template. Generated PNGs and vanilla/Dye Depot palette RGB values are now bundled in the source. Dye is consumed when switching to these frame types, except in Creative.
- New Dye Depot backgrounds reference `dye_depot:block/<color>_concrete`, not copied textures. Their costs resolve to the matching `dye_depot:<color>_concrete` block item.
- Dye Depot-only choices are hidden without their dye; server packet validation rejects new missing-mod styles.
- New indices are appended to each catalog to retain existing v1.2.0 numeric appearance sync.
- Added block/item layer JSONs to `targets/neoforge-1.21.1/src/designer2/resources` and regenerated shared designer variants.
- All target source updates are now present, but Gradle compilation and in-game testing are still unverified.

Emissive frame models are generated from the normal frame models by `python scripts/generate-emissive-frames.py` after recoloring. The conversion into 1.20.1/1.21.1/26.x shared resources is done by `python scripts/generate-designer-ports-assets.py`. Always run both after changing the base frame art/models.
