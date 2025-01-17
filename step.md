# Modules

## Module Layer

Root Layer

- bootstraplauncher
- securejarhandler

Bootstrap Layer

- modlauncher
- JarJarMetadata
- JarJarSelector
- fmlearlydisplay
- fmlloader
- accesstransformers
- coremods
- eventbus
- forgespi
- srgutils
- unsafe
- terminalconoleappender
- mergetool

Service Layer

- mods that declare services

Plugin Layer

- fmlcore
- javafmllanguage
- lowcodelanguage
- mclanguage

Game Layer

- mods

## Services

Boot Layer
- ILaunchPluginService
- ILaunchHandlerService
- INameMappingService
- ITransformerDiscoveryService
- ICoreModProvider (singleton)
- IEventBusEngine (singleton)

Service Layer
- ITransformationService
- IModLocator
- IDependencyLocator

Plugin Layer
- IModLanguageProvider

Game Layer
- IBindingsProvider
- IModStateProvider

# Launching Process

## Bootstrap

- `BootstrapLauncher.main`
    - Load library jars from classpath. Those jars are minecraft or forge utilities
    - Initialize ModuleClassLoader MC-BOOTSTRAP. Create ModuleLayer.
- `Launcher.main`
    - Log INFO: "ModLauncher running: args {}"
- `Launcher.<init>`
    - Log INFO: "ModLauncher {} starting"
- `Launcher.run`
    - `TransformationServicesHandler.discoverServices`
        - Log DEBUG: "Discovering transformation services"
        - Gather `ITransformerDiscoveryService` (ModDir, ClassPath)
        - `ITransformerDiscoveryService.earlyInitialization`
            - `ImmediateWindowHandler.load` (from ModDir)
            - Display progress **Bootstrapping Minecraft**
        - Gather `ITransformationService` (FML, Mixin)
        - Log DEBUG: "Found transformer services"
        - `TransformationServicesHandler.initializeTransformationServices`
            - Log DEBUG: "FML {} loading" ~ "Found ForgeSPI package specification"
            - Log INFO: "SpongePowered MIXIN Subsystem Version"
            - `FMLLoader.beginModScan`
                - Display progress **Discovering mod files**
                - Log DEBUG: "Scanning for mods and other resources to load"
                - Log INFO: "Found {} dependencies adding them to mods collection"
                - Display progress **Found {} mod candidates**
    - `FMLLoader.completeScan`
        - Display progress **Loading language providers**
        - Display progress **Scanning mod candidates**
        - Load all mods.toml files
    - `TransformationServicesHandler.initialiseServiceTransformers`
        - Gather coremods
    - `TransformationServicesHandler.buildTransformingClassLoader`
- `LaunchServiceHandler.launch`
    - `MixinBootstrap.doInit`
    - `MixinBootstrap.inject`
    - Log Info: "Launching target"
    - `FMLLoader.beforeStart`
        - Loading Forge module, trigger mixin stuff
        - Trigger "Error loading class" and "@Mixin target {} was not found" from mixin
        - Display progress **Launching minecraft**

## Client

Entry point: `Main.main`

- Misc initializations, such as crash report memory reserve
- Display progress **Loading bootstrap resources**
- `Bootstrap.bootStrap`
- `RenderSystem.initRenderThread`
- `Minecraft.<init>`
    - Log INFO: "Setting user"
    - Mixin: BeforeConstant stuff
    - Log INFO: "Backend library"
    - Display progress **Initializing Game Graphics** from
    - `ClientModLoader.begin`
        - Display progress **Loading Mods**
        - `ModLoader.gatherAndInitializeMods`
            - Display progress **Mod Gather {}**
            - State Transitions: GATHER
                - VALIDATE
                - CONSTRUCT
                - CREATE_REGISTRIES
                - OBJECT_HOLDERS
                - INJECT_CAPABILITIES
                - UNFREEZE_DATA
                - LOAD_REGISTRIES
        - `ResourcePackLoader.loadResourcePacks`
        - Event: `AddPackFindersEvent`
    - Register a bunch of managers as resource listener
    - Event: `RenderLevelStageEvent.RegisterStageEvent`
    - Event: `RegisterParticleProvidersEvent`
    - Init ClientHooks, firing a bunch of events
    - More vanilla client setups
    - `ReloadableResourceManager.createReload` Start
    - Display progress **Minecraft Progress**
- Render thread setup
- Start Minecraft client grand game loop

Running `ReloadableResourceManager.createReload`

- All resource listeners
- State Transitions: LOAD
    - CONFIG_LOAD
    - COMMON_SETUP
    - SIDED_SETUP
- State Transitions: COMPLETE
    - ENQUEUE_IMC
    - PROCESS_IMC
    - COMPLETE
    - FREEZE_DATA
    - NETWORK_LOCK
- State Transitions: DONE
- `Options.load`

## Server

Entry point: `Main.main`

- Misc initializations, such as crash report memory reserve
- `Bootstrap.bootStrap`
- `ServerModLoader.load`
    - State Transitions: GATHER
        - VALIDATE
        - CONSTRUCT
        - CREATE_REGISTRIES
        - OBJECT_HOLDERS
        - INJECT_CAPABILITIES
        - UNFREEZE_DATA
        - LOAD_REGISTRIES
    - State Transitions: LOAD
        - CONFIG_LOAD
        - COMMON_SETUP
        - SIDED_SETUP
    - State Transitions: COMPLETE
        - ENQUEUE_IMC
        - PROCESS_IMC
        - COMPLETE
        - FREEZE_DATA
        - NETWORK_LOCK
- World loading
