package me.zed_0xff.zb_mac_os_hide_menu_bar;

import org.lwjgl.system.JNI;
import org.lwjgl.system.macosx.ObjCRuntime;
import org.lwjglx.opengl.Display;

import zombie.debug.DebugLog;

public class MacOSMenuBarHelper {
    private static final boolean IS_MACOS = System.getProperty("os.name").contains("OS X");

    // NSApplicationPresentationOptions constants
    private static final int NSApplicationPresentationDefault = 0;
    private static final int NSApplicationPresentationHideMenuBar = 2;
    private static final int NSApplicationPresentationHideDock = 4;

    private static long nsApp = 0;
    private static long setPresentationOptionsSel = 0;
    private static long objc_msgSend = 0;
    private static boolean initialized = false;

    private static void initialize() {
        if (initialized || !IS_MACOS) {
            return;
        }

        try {
            // Get objc_msgSend function address
            objc_msgSend = ObjCRuntime.getLibrary().getFunctionAddress("objc_msgSend");
            if (objc_msgSend == 0) {
                DebugLog.log("Failed to get objc_msgSend function address");
                return;
            }

            // Get NSApplication class
            long nsApplicationClass = ObjCRuntime.objc_getClass("NSApplication");
            if (nsApplicationClass == 0) {
                DebugLog.log("Failed to get NSApplication class");
                return;
            }

            // Get sharedApplication selector
            long sharedAppSel = ObjCRuntime.sel_getUid("sharedApplication");
            if (sharedAppSel == 0) {
                DebugLog.log("Failed to get sharedApplication selector");
                return;
            }

            // Call [NSApplication sharedApplication]
            // objc_msgSend(nsApplicationClass, sharedAppSel)
            nsApp = JNI.invokePPP(nsApplicationClass, sharedAppSel, objc_msgSend);
            if (nsApp == 0) {
                DebugLog.log("Failed to get NSApplication shared instance");
                return;
            }

            // Get setPresentationOptions: selector
            setPresentationOptionsSel = ObjCRuntime.sel_registerName("setPresentationOptions:");
            if (setPresentationOptionsSel == 0) {
                DebugLog.log("Failed to get setPresentationOptions: selector");
                return;
            }

            initialized = true;
        } catch (Exception e) {
            DebugLog.log("Failed to initialize macOS menu bar helper: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void hideMenuBar() {
        if (!IS_MACOS || !Display.isCreated()) {
            return;
        }

        initialize();

        if (!initialized || nsApp == 0 || setPresentationOptionsSel == 0 || objc_msgSend == 0) {
            return;
        }

        try {
            int options = NSApplicationPresentationHideMenuBar | NSApplicationPresentationHideDock;
            // Call [NSApp setPresentationOptions:options]
            // objc_msgSend(nsApp, setPresentationOptionsSel, options)
            // Signature: invokePPPI(receiver, selector, intArg, functionAddress)
            JNI.invokePPPI(nsApp, setPresentationOptionsSel, options, objc_msgSend);
        } catch (Exception e) {
            DebugLog.log("Failed to hide macOS menu bar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void showMenuBar() {
        if (!IS_MACOS || !Display.isCreated()) {
            return;
        }

        initialize();

        if (!initialized || nsApp == 0 || setPresentationOptionsSel == 0 || objc_msgSend == 0) {
            return;
        }

        try {
            // Call [NSApp setPresentationOptions:NSApplicationPresentationDefault]
            JNI.invokePPPI(nsApp, setPresentationOptionsSel, NSApplicationPresentationDefault, objc_msgSend);
        } catch (Exception e) {
            DebugLog.log("Failed to show macOS menu bar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void hideMenuBarIfNeeded() {
        if (IS_MACOS && Display.isCreated()) {
            hideMenuBar();
        }
    }

    public static void showMenuBarIfNeeded() {
        if (IS_MACOS && Display.isCreated()) {
            showMenuBar();
        }
    }
}
