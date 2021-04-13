package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.merlin;

import java.util.List;

class MerlinCallbackManager<T extends Registerable> {

    private final Register<T> register;

    MerlinCallbackManager(Register<T> register) {
        this.register = register;
    }

    List<T> registerables() {
        return register.registerables();
    }

}
