#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(CalSmartconfig, NSObject)

RCT_EXTERN_METHOD(provision:(* NSString)ssid bssid:(* NSString)bssid password:(* NSString)password
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)

@end
