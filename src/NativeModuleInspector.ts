import { isNil } from "ramda";
import { NativeModules } from "react-native";

const native = NativeModules;

export type IMethodDict<Res = unknown, Args = unknown[]> = {
  result: Res;
  arguments: Args;
};

export const NativeModulesNames = {
  AppVersionModule: "AppVersionModule",
  SoftInputModeModule: "SoftInputModeModule",
};

export class NativeModuleInspector<
  MethodsDictionary extends { [key: string]: IMethodDict }
> {
  private readonly _selector: {
    [key in keyof MethodsDictionary]: (...args: unknown[]) => unknown;
  };
  private readonly _name: string;
  constructor(name: keyof typeof NativeModulesNames) {
    this._name = name;
    this._selector = native[name];
    if (isNil(native[name]) && __DEV__) {
      console.warn(
        "NativeModuleInspector Error! This Module is not exists on this platform!"
      );
    }
  }

  public async executeModule<T extends keyof MethodsDictionary>(
    fn: T,
    args: MethodsDictionary[T]["arguments"]
  ): Promise<MethodsDictionary[T]["result"] | null> {
    try {
      if (!this._selector[fn]) {
        return null;
      }
      return await this._selector[fn](...args);
    } catch (e) {
      console.log("NativeModuleInspector ex on module, ", this._name);
    }
  }
}
