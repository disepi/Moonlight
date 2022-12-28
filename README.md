<h3 align="center">🌙</h3>
<h1 align="center">moonlight</h1>

#### The public Moonlight repository has been archived. There is still ongoing development within the private repository. If you have any inquiries about this, please contact me on Discord: disepi#7182
#### This public source is built for 1.19.20 Nukkit non-auth-input servers

Moonlight is an anticheat for Nukkit designed to prevent cheating. It is still in early stages of development and, at the moment, alot of checks are not implemented or do not work properly.

#### Currently, Moonlight is only for experimental purposes. You can still use it, but expect to see bugs as it is not fully finished yet!

# Purpose
Moonlight is made to be lightweight while also detecting cheats fast and efficiently. The cheat checks in Moonlight are carefully made to avoid detecting any legitimate players while also detecting cheating players almost instantly.

# Checks
#### Flight
- [x] Vertical velocity prediction check
- `Detects just about any vertical movement hack!`
- [x] Abnormal downwards velocity check
- `Further detects any methods to bypass the prediction check!`
#### Example
![flight](https://user-images.githubusercontent.com/54753631/167266467-64758286-1982-40a9-99dc-0f79c3ff84f1.gif)
---
#### Speed
- [x] Abnormal speed distance check
- `Total limit of movement speed to 13 blocks per second`
- [x] Off-ground friction check
- `Checks movement friction off-ground. Detects most Bunnyhops!`
- [x] Off-ground total speed limit
- `Checks the off-ground speed for abnormal high values and limits to sprint speed, further detecting Bunnyhops!`
- [x] On-ground total speed limit
- `Checks the on-ground speed for abnormal high values and limits them. Detects vanilla ground-speeds!`
- [x] Jump height check
- `Checks the jump height for abnormal low values. Detects low-hops and Y-ports!`
- [x] Strafe movement check
- `Checks the strafe movement for abornmal high values. Detects most Bunnyhops!`
#### Example
![speed](https://user-images.githubusercontent.com/54753631/167266602-5dea84e4-e3d8-4033-9800-1f793f2313f3.gif)
---
#### Timer
- [x] Tick time balance check
- `Detects 1% timer manipulation, Blink, etc!`
#### Example
![timer](https://user-images.githubusercontent.com/54753631/167266663-f778c94d-8391-4863-bda4-5ce30484feec.gif)
---
#### Killaura
- [x] Rotation angles check
- `Detects Killauras with no silent rotations and also Hitbox!`
- [x] False entity check
- `Detects almost every Killaura!`
#### Example
![killaura](https://user-images.githubusercontent.com/54753631/167266772-8e479732-deb5-43aa-8727-51868ee78941.gif)
---
#### BadPackets
- [x] Pitch rotation check
- `Detects bad rotations from modules such as Killaura and Scaffold!`
- [x] Duplicate MovePlayerPacket check
- `Detects bad rotations and movement cheats such as Killaura and Fly!`
- [x] Self-hit check
- `Detects any player attemping to hit themselves (self-hitting is a method of bypassing anticheat checks)!`
---
#### NoSwing
- [x] Combat swing check
- `Verifies that the client follows vanilla protocol while attacking entities. Detects some Killauras!`
- [x] World swing check
- `Verifies that the client follows vanilla protocol while destroying blocks.`
