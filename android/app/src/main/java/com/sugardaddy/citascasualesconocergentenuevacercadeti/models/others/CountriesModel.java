package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others;


public class CountriesModel {
    public String name;
    public String code;
    private String dial_code;

    public CountriesModel(String name, String iso2, String dialCode) {
        setCode(iso2);
        setName(name);
        setDial_code(dialCode);
    }

    public String getDial_code() {
        return dial_code;
    }

    private void setDial_code(String dial_code) {
        this.dial_code = dial_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
