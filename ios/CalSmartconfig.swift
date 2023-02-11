@objc(CalSmartconfig)
class CalSmartconfig: NSObject {

    @objc(provision:location:withResolver:withRejecter:)
    func provision(ssid: String, bssid: String, password: String, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock ) -> Void{

    }
}
