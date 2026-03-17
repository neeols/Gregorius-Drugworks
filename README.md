# Gregorius Drugworks

Gregorius Drugworks is a workspace for GregTech-adjacent drug, trip, and medical gameplay systems.

The most actively developed gameplay module in this repo is the 1.12.2 Forge addon **Gregorius Drugworks Persistence**, located at [`gregorius-drugworks-workspace/gtpersistence`](./gregorius-drugworks-workspace/gtpersistence). It is backed by a shared [`common`](./gregorius-drugworks-workspace/common) module and sits alongside additional workspace modules under active development.

## Current State

- Minecraft target: **1.12.2**
- Main active addon: **Gregorius Drugworks Persistence**
- Runtime focus: pills, staged trips, vapes, applicators, payloads, trigger bundles, visual profiles, and custom synced use animations
- Scripting: **optional GroovyScript compatibility** for registering new pills, vapes, payloads, payload sources, trips, antidotes, trigger bundles, visual profiles, and applicator helpers
- Project status: active development and iteration-heavy

## Repo Layout

- [`gregorius-drugworks-workspace/common`](./gregorius-drugworks-workspace/common): shared trip, payload, visual, and registry model code
- [`gregorius-drugworks-workspace/gtpersistence`](./gregorius-drugworks-workspace/gtpersistence): the current 1.12.2 gameplay addon and content implementation
- [`gregorius-drugworks-workspace/gtmodern`](./gregorius-drugworks-workspace/gtmodern): additional workspace module for newer-side work

## Gameplay Systems In `gtpersistence`

- Pill items with custom consumption flow, smoother local rendering, and trip integration
- Vape items with staged inhale / hold / exhale timing, per-phase effects, smoke particles, lingering plumes, durability, and spent-item replacement
- Medical applicators with payload loading, custom self-injection animation, and empty / loaded state handling
- A staged trip runtime with antidotes, particles, sounds, trigger hooks, and visual effects
- Payload and payload-source registries that let applicators deliver reusable scripted behaviors
- Trigger bundles and visual profiles for reusable effect pipelines

## Optional GroovyScript Support

GroovyScript support is optional. If the `GroovyScript` mod is installed, Gregorius Drugworks Persistence exposes the aliases `mods.gdw` and `mods.gdwpersistence`.

Docs and examples:

- [`gregorius-drugworks-workspace/gtpersistence/docs/GROOVYSCRIPT.md`](./gregorius-drugworks-workspace/gtpersistence/docs/GROOVYSCRIPT.md)
- [`gregorius-drugworks-workspace/gtpersistence/examples/groovy/preInit/gregorius_drugworks_showcase.groovy`](./gregorius-drugworks-workspace/gtpersistence/examples/groovy/preInit/gregorius_drugworks_showcase.groovy)

The showcase script is designed for GitHub readers: it documents the API in-place and uses assets that already exist in this repo.

## Building

This repo is not built from the top level. Build from the module you are working on.

For the active persistence addon:

```powershell
cd .\gregorius-drugworks-workspace\gtpersistence
.\gradlew.bat compileJava
```

The `gtpersistence` Gradle project is currently configured around:

- Forge / RFG for **Minecraft 1.12.2**
- a Java toolchain specified in [`gtpersistence/gradle.properties`](./gregorius-drugworks-workspace/gtpersistence/gradle.properties)
- optional `GroovyScript` integration at compile time and runtime

## License

This project is licensed under the **GNU General Public License v3.0**.

See [`LICENSE`](./LICENSE) for details.

## Author

Created by **Wurtzitane (neeols)**.

## Contributing

Issues, suggestions, and pull requests are welcome.

## Disclaimer

This project is a work of fiction created for gameplay, atmosphere, and technical experimentation.
