package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.merlin;

class DisconnectCallbackManager extends MerlinCallbackManager<Disconnectable> implements Disconnectable {

    DisconnectCallbackManager(Register<Disconnectable> register) {
        super(register);
    }

    @Override
    public void onDisconnect() {
        Logger.d("onDisconnect");
        for (Disconnectable disconnectable : registerables()) {
            disconnectable.onDisconnect();
        }
    }
}
