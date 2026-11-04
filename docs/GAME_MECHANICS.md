# Game Mechanics

| Difficulty | XP |
|---|---:|
| Easy | 10 |
| Normal | 25 |
| Hard | 50 |
| Epic | 100 |

Level = floor(total XP / 100) + 1. XP within level = total XP modulo 100.

A completed quest cannot award XP again in the current session. Before release, enforce this transactionally in persistent storage and validate online rewards server-side. Never punish missed days.
