#!/usr/bin/env python3
"""Validate the player-tested single-block reference target."""
import subprocess,sys
from pathlib import Path
raise SystemExit(subprocess.call([sys.executable,str(Path(__file__).with_name('validate-designer-ports.py')),'neoforge-1.21.1']))
