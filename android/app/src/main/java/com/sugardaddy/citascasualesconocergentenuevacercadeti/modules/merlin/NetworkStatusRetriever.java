package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.merlin;

class NetworkStatusRetriever {

    private final MerlinsBeard merlinsBeard;

    NetworkStatusRetriever(MerlinsBeard merlinsBeard) {
        this.merlinsBeard = merlinsBeard;
    }

    void fetchWithPing(EndpointPinger endpointPinger, EndpointPinger.PingerCallback pingerCallback) {
        if (merlinsBeard.isConnected()) {
            endpointPinger.ping(pingerCallback);
        } else {
            endpointPinger.noNetworkToPing(pingerCallback);
        }
    }

    NetworkStatus retrieveNetworkStatus() {
        if (merlinsBeard.isConnected()) {
            return NetworkStatus.newAvailableInstance();
        } else {
            return NetworkStatus.newUnavailableInstance();
        }
    }

}
