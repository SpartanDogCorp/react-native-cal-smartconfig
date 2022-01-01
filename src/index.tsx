import { NativeModules, Platform } from 'react-native';
import WifiManager from "react-native-wifi-reborn"

const LINKING_ERROR =
  `The package 'react-native-cal-smartconfig' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const CalSmartconfig = NativeModules.CalSmartconfig
  ? NativeModules.CalSmartconfig
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return CalSmartconfig.multiply(a, b);
}

export function connect(): Promise<null> {
  return CalSmartconfig.provision("KPSSVentures", "e0:db:d1:6a:7b:33", "10panzer");
}
