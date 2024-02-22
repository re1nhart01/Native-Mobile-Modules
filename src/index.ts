import type { IMethodDict } from "./NativeModuleInspector";
import { NativeModuleInspector } from "./NativeModuleInspector";

export type AppVersionModuleDict = {
  hasUpdates: IMethodDict<Promise<{ result: boolean }>, []>;
  checkForUpdates: IMethodDict<Promise<boolean>, []>;
  resumeUpdates: IMethodDict<Promise<boolean>, []>;
};

export type KeyboardModeModuleDict = {
  updateMode: IMethodDict<Promise<boolean>, [number]>;
};

export enum SoftInputMode {
  UNSPECIFIED = 0,
  RESIZE = 16,
  PAN = 32,
  NOTHING = 48,
}

export const AppVersionNative = new NativeModuleInspector<AppVersionModuleDict>(
  "AppVersionModule"
);

export const KeyboardModeNative =
  new NativeModuleInspector<KeyboardModeModuleDict>("SoftInputModeModule");
