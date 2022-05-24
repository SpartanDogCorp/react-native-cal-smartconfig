declare module 'react-native-cal-smartconfig' {
    function start(config: {
        type: 'esptouch' | 'airkiss',
        ssid: string,
        password: string,
        bssid?: string,
        timeout?: number
    }): Promise<any[]>;

    function stop(): void;
}
