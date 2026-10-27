# Sign Plates v1.2.0 — NeoForge 1.21.1 prototype

Only this target has the new architecture and Background tab. All nine other
targets, shared assets and build launchers retain their previous versions.

## Build and install

Use **Build Selected → E**, or run the existing build script for
`neoforge-1.21.1`. Java 21 and the existing pinned Gradle version are required.
The output remains `SignPlates-NeoForge-1.21.1-v1.2.0.jar`.

The full source ZIP has the project contents at its root. Extract it into the
multi-version project folder. It includes the GUI artwork available in this
workspace; preserve any additional local PNG edits before replacing files.

## Preview and EMI polish

- EMI now fills missing default symbol stacks without a removal predicate.
  EMI's removal predicates also affect future additions, which previously hid
  every plate. Existing creative-index variants are kept without duplicates.
- Item/preview models have front-only frames and symbols, with back-face
  culling enabled. Their background still has a real front, back and thin sides.
- Frame geometry and UVs cover the visible 12px region inside the original
  16px PNG. This avoids sampling neighbouring atlas pixels at sprite borders,
  addressing the thin line seen beside the copper frame. PNGs are unchanged.
- The no-colour choice uses the same checkerboard as no frame/background.
- The base item/block is called **Sign Plate**. Symbol items retain their
  symbol names; generic block overlays such as Jade use the base block name.
- Cached block geometry preserves and rotates the background's `cullface`
  groups. Vanilla chunk rendering can omit its backing face against a solid
  neighbour, without an optimization mod or a block entity renderer.

Polish checks passed: actual model assembler backing-face grouping and cache
reuse for all 12 orientations; item background front/back preservation; EMI
registration against empty, registered, partial and creative index fixtures,
including repeated registration; browser-plugin compilation against EMI's
1.1.24 API; model compilation against the mapped game/NeoForge API fixtures;
all 522 resource models and their texture bindings; front-only symbol/frame
models and unchanged supplied PNGs. In-game rendering still needs testing.

## ID, symbol glow and naming polish

- The block and item now use `sign_plates:sign_plate`. Previous
  `sign_plates:plate_empty` stacks/chunk palettes migrate before registry
  decoding. Existing appearance components and block entities are preserved.
  The block entity type remains `sign_plates:plate` for save compatibility.
- The Emission toggle makes both the symbol and frame emissive. Plates no longer emit level-13 block
  light, which also lit their frame/background. Migrated chunks are marked for
  relighting so old cached block light can be removed.
- Symbol item names now read **Sign Plate: Fire**, **Sign Plate: Light**, etc.
  The blank base item and generic block name remain **Sign Plate**.
- The supplied Light emission PNG is stored unchanged as `light_mask.png`.
  Light's tinted/glowing layers use it, matching the Fire/Water mask approach.
- Obsolete generated resources for the previous ID are removed by the
  generator and excluded from resource processing if they remain after ZIP
  extraction over an older source folder.

Checks passed against the mapped Minecraft 1.21.1 API: 68,112 appearance
NBT/component roundtrips, network codecs, the 258 older item variants, packed
chunk palettes, and 2,064 customized prototype-item ID conversions. Additional
cases preserve existing block entities, recover missing ones, retain placement,
request relighting, and verify migration idempotence. Resource checks cover all
model references, the 85 renamed symbol choices, both Light mask variants and
the absence of emissive data on frames/backgrounds. Actual world migration and
dark-scene rendering still need in-game testing; use a copy of the test world.

## What changed

- One registered block (`sign_plates:sign_plate`), one registered item and one
  block entity type. The 48 block states describe placement, water and glow.
- A non-ticking block entity stores symbol, frame, background, colour and glow.
  Chunk geometry is assembled from independent baked layers and cached. There
  is no block entity renderer and no per-frame rendering work for placed plates.
- **Background** contains two independent grids and scrollbars: **Frame** and
  **Background**. Both apply immediately and preserve each other's selection.
- Frames: None, Iron, Copper, Gold. Backgrounds: Transparent, Iron, all 11
  vanilla wood types available in 1.21.1, all 16 concrete colours and seven
  brick materials (Nether, Stone, Deepslate, Mud, normal Bricks, End Stone,
  Prismarine). Wood uses log/stem tops, or bamboo block top, matching Spruce.
  The four supplied PNGs are used unchanged; Spruce uses vanilla
  `minecraft:block/spruce_log_top`.
- Creative, JEI and EMI show the same 86 default symbol stacks. They share one
  item ID, with different appearance components. Neither browser is required.
- Style drafts, material costs, Apply sound, close confirmation, GUI themes,
  camera toggle, dye compatibility and placed-block interactions are retained.
- Pick Block copies appearance. Normal drops are blank plates.

## Resources and extension

Active models, data, language and new PNGs live in `src/designer2/resources`.
`src/main/resources` supplies only the existing symbol/GUI PNGs and logo to the
build. Its old blockstates and combination models are not packaged.
Resource processing clears previous output so older variants cannot leak into
the new JAR when extracting this patch over an existing checkout.

The normal Light symbol PNG has been replaced with the corrected supplied
artwork. Its emission mask remains unchanged.

`PlateLayers.java` declares the independent frame/background lists. Stable
string IDs are persisted; existing IDs should not be renamed. The generator
`tools/generate-designer2-assets.py` regenerates the independent layer models
from the retained source geometry and reads both catalogues directly from that
Java file, keeping model lists aligned with runtime choices. It never generates
or modifies PNG artwork.
The older root asset/port generators still describe the previous architecture;
do not use them to port this prototype yet.

The expanded designer uses network protocol `designer3`, because the additional
background choices move the following Style command numbers. Use this build on
both client and server; older designer clients are rejected rather than having
their commands interpreted as different selections. Release version remains
v1.2.0.

## Verification completed here

- All target Java sources plus the shared config registration compile in an
  API check using official Minecraft mappings and the exact NeoForge 21.1.176
  API, with temporary fixtures for NeoForge's patched methods/access levels.
  These fixtures are not included in this source or any mod JAR.
- 817,344 appearance NBT/component codec roundtrips; all 258 legacy item
  variants; packed chunk palettes at 4-, 5- and 6-bit widths, including negative
  coordinates; migration idempotence and unrelated component preservation.
- All 12 placement rotations match collision bounds and front-face normals.
- 39,204 style cost cases and the existing config/theme checks pass.
- All 588 independent models and their local/vanilla texture references resolve.
  Every vanilla background texture exists in the Minecraft 1.21.1 client assets.
  Supplied PNGs are byte-identical. One blockstate definition and one crafting
  recipe are active; there are no stonecutter recipes.
- 13,209 files outside this target remain byte-identical to the starting source.

A full Gradle build could not complete here: NeoForm Runtime failed in its
`extractServer` preparation step because this environment does not expose the
Java process command through `ProcessHandle`. This happened before mod
compilation. A normal Gradle build and in-game testing are still required.

## In-game checks before porting

1. Build E, launch without JEI/EMI, craft and place a blank plate.
2. Test all frame/background combinations, transparent options, every mounting
   face, both preview rotations and both GUI themes. Check hand/item orientation.
3. Change symbols and bases with an unapplied Style draft, then apply or close.
   Verify dye/glow costs, sponge clearing, light updates and held-item use.
4. Save/reload and rejoin multiplayer; verify other players see changes. Check
   Creative Pick Block and placement of its appearance-bearing stack.
5. Test JEI and EMI separately: default symbols should be distinguishable and
   searchable, without all frame/background combinations cluttering the index.
6. Test a **copy** of an older world. Migration rewrites legacy chunk palettes
   and item NBT before registry decoding. Old iron plates become Iron/Iron;
   old spruce plates become No frame/Spruce; clear plates become No frame/
   Transparent. Symbols, tint, glow, placement and waterlogging are preserved.
   Actual world upgrade and missing-registry handling still need in-game testing.
