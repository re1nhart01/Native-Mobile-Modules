import {TurboModule, TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
    readonly reverseString: (input: string) => string;
    readonly getRand: () => string;
    readonly createFile: (fName: string, content: string) => boolean;
    readonly getFile: (fName: string) => string;
}

export default TurboModuleRegistry.getEnforcing<Spec>(
    'NativeSampleModule',
    );