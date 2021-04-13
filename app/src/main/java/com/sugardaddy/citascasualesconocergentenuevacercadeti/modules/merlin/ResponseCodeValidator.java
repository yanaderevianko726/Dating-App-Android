package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.merlin;

public interface ResponseCodeValidator {

    boolean isResponseCodeValid(int responseCode);

    class CaptivePortalResponseCodeValidator implements ResponseCodeValidator {
        @Override
        public boolean isResponseCodeValid(int responseCode) {
            return responseCode == 204;
        }
    }
}
