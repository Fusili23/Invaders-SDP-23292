# Temp (Player & Enemy Ship Variety)
## Team Introduction
- Team Leader
    - `Seungbeom Kim`
- Team Members
    - `Donguk Kim`
    - `Inbeom Yeo`
    - `Jisung Yoo`
    - `Jungwon Choi`
    - `Seungeon Lee`
    - `Sunghum Cho`
## Team Requirements
- Gameplay HUD
- Level design system
- Items
## Detailed Requirements
### Elements
- Core Attributes
    - HP, weapon stats
    - Attack skills (cooldown, damage, range, etc.)
    - Movement speed for ships and bosses
- Enemy Ships
    - Attributes: health, attack power, defense, special skills
    - Logic: attack patterns (timing, range, randomness), movement patterns (straight, curved, evasive, etc.)
- Boss
    - Types: elite boss, final boss
    - Attributes: health, attack power, defense, special skills, strike range
    - Multi-phase patterns: splitting/merging, phase triggers (HP threshold, timer, etc.)
- Player Ship
    - Attributes: health, attack power, defense
    - Capabilities: attack skills, special skills, ship mobility (evasion, speed control)
    - Strike range: hitbox definitions per weapon

### Requirements
- Gameplay HUD
    - Health & Shields
    - Status Effects & Buffs
    - Progress
    - Performance
- Level design system
    - Wave Setup
    - Boss Phase Script
    - Difficulty Scaling
    - Map
    - Rules
    - Event Hooks
- Items
    - Item Catalog
    - Item Changes
    - Drop Rules
    - Event Hooks
