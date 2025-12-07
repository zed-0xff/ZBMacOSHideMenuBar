# ZBMacOSHideMenuBar

A Project Zomboid mod that fixes the macOS menu bar issue in borderless windowed mode.

## ☕ Support the Developer

If you find this mod useful, consider supporting the developer with a coffee!

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/zed_0xff)

## What It Does

The vanilla game on macOS in borderless windowed mode still displays the macOS menu bar, which hides part of the game screen. This mod fixes that by automatically hiding the menu bar when borderless windowed mode is enabled, and restoring it when disabled.

## The Problem

When playing Project Zomboid on macOS in borderless windowed mode, the macOS menu bar remains visible at the top of the screen. This causes the menu bar to overlap with the game window, hiding part of the game screen and reducing the usable display area.

## The Solution

ZBMacOSHideMenuBar automatically:
- Hides the macOS menu bar when borderless windowed mode is enabled
- Moves the game window to position (0,0) to ensure proper positioning
- Restores the menu bar when borderless windowed mode is disabled
- Only activates on macOS (no effect on other operating systems)

## Installation

1. **Prerequisites**: You must have [ZombieBuddy](https://github.com/zed-0xff/ZombieBuddy) installed and configured first
2. **Enable the mod**: Enable ZBMacOSHideMenuBar in the Project Zomboid mod manager
3. **Use borderless windowed mode**: The fix automatically applies when you enable borderless windowed mode in the game settings

## Requirements

- **Project Zomboid** (Build 42+)
- **ZombieBuddy** - Required framework for Java mods
- **macOS** - This mod only works on macOS
- **Java 17** (required by the game)

## How It Works

ZBMacOSHideMenuBar uses [ZombieBuddy](https://github.com/zed-0xff/ZombieBuddy) to patch the game's display initialization:

1. **Intercepts borderless window setting**: Patches `Display.setBorderlessWindow()` to detect when borderless mode is enabled/disabled
2. **Hides menu bar**: Uses macOS Objective-C runtime to call `NSApplication.setPresentationOptions()` with `NSApplicationPresentationHideMenuBar`
3. **Positions window**: Moves the window to (0,0) to ensure it's properly positioned
4. **Restores menu bar**: Automatically restores the menu bar when borderless mode is disabled

### Technical Details

- **Java patches**: Uses ZombieBuddy's `@Patch` annotations to intercept display methods
- **macOS Objective-C integration**: Uses LWJGL's ObjCRuntime to interact with macOS system APIs
- **Automatic detection**: Only activates on macOS (detects via `os.name` system property)
- **Safe restoration**: Properly restores menu bar when borderless mode is disabled

## Building

1. Navigate to the Java project directory:
   ```bash
   cd 42/media/java/client
   ```

2. Build the JAR:
   ```bash
   gradle build
   ```

3. The JAR will be created at `build/libs/client.jar`

## Project Structure

```
ZBMacOSHideMenuBar/
├── 42/
│   ├── mod.info
│   └── media/
│       ├── java/
│       │   └── client/
│       │       ├── build.gradle
│       │       ├── src/
│       │       │   ├── Main.java                    # Patch definitions and entry point
│       │       │   └── MacOSMenuBarHelper.java     # macOS menu bar control logic
│       │       └── build/
│       │           └── libs/
│       │               └── client.jar
│       └── lua/
│           └── client/
│               └── ZMacOSHideMenuBar.lua           # Lua integration
└── common/
```

## Troubleshooting

### Menu bar still visible

- Ensure ZombieBuddy is properly installed and working
- Check that the mod is enabled in the mod manager
- Verify the Java JAR file is built and present
- Make sure you're using borderless windowed mode (not fullscreen or windowed)
- Restart the game after enabling the mod

### Menu bar doesn't restore when disabling borderless mode

- The mod should automatically restore the menu bar when you disable borderless windowed mode
- If it doesn't restore, try toggling borderless mode off and on again
- You can also manually show the menu bar by pressing Command+Option+D (or using Mission Control)

### Mod not working

- Verify ZombieBuddy is installed and the game shows `[ZB]` in the version string
- Check that the mod is enabled
- Ensure you're on macOS (this mod only works on macOS)
- Rebuild the Java JAR file if you've made changes

## Links

- **GitHub**: https://github.com/zed-0xff/ZBMacOSHideMenuBar
- **ZombieBuddy Framework**: [ZombieBuddy](https://github.com/zed-0xff/ZombieBuddy) - The framework this mod is built on

## Related Mods

Other mods built with ZombieBuddy:

- **[ZBHelloWorld](https://github.com/zed-0xff/ZBHelloWorld)**: A simple example mod demonstrating patches-only mods and UI patching
- **[ZBetterWorkshopUpload](https://github.com/zed-0xff/ZBetterWorkshopUpload)**: Filters unwanted files from Steam Workshop uploads and provides upload previews

## License

See LICENSE file for details (if present).

## Author

zed-0xff

## Disclaimer

This mod modifies the game's display behavior on macOS. Use at your own risk.

