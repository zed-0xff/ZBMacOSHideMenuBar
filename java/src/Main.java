package me.zed_0xff.zb_mac_os_hide_menu_bar;

import me.zed_0xff.zombie_buddy.Patch;

import org.lwjglx.opengl.Display;
import org.lwjgl.glfw.GLFW;

import zombie.core.Core;
import zombie.core.opengl.RenderThread;

public class Main {
    @Patch(className = "org.lwjglx.opengl.Display", methodName = "setBorderlessWindow")
    public static class Patch_setBorderlessWindow {
        @Patch.OnExit
        public static void exit(boolean optionBorderlessWindow) {
            System.out.println("[ZBMacOsHideMenuBar] after Display.setBorderlessWindow");

            if (optionBorderlessWindow && System.getProperty("os.name").contains("OS X")) {
                // Hide menu bar when borderless window is enabled
                fixMenuBarAndPos();
            } else if (!optionBorderlessWindow && System.getProperty("os.name").contains("OS X")) {
                // Restore menu bar when borderless window is disabled
                if (Display.isCreated()) {
                    MacOSMenuBarHelper.showMenuBarIfNeeded();
                }
            }
        }
    }

    public static void fixMenuBarAndPos() {
        if (Display.isCreated()) {
            RenderThread.invokeOnRenderContext(() -> {
                MacOSMenuBarHelper.hideMenuBarIfNeeded();

                // Move window to (0,0) to avoid menu bar area
                long windowHandle = Display.getWindow();
                if (windowHandle != 0) {
                    GLFW.glfwSetWindowPos(windowHandle, 0, 0);
                    // Remove thin 1px border on macOS borderless windows (e.g. macOS 15+)
                    MacOSMenuBarHelper.disableWindowShadow(windowHandle);
                }
            });
        }
    }

    public static void main(String[] args) {
        var core = Core.getInstance();
        if (!core.isFullScreen() && core.getOptionBorderlessWindow()) {
            fixMenuBarAndPos();
        }
    }
}
