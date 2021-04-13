
var form_validation = function() {
    var e = function() {
            jQuery(".form-valide").validate({
                ignore: [],
                errorClass: "invalid-feedback animated fadeInDown",
                errorElement: "div",
                errorPlacement: function(e, a) {
                    jQuery(a).parents(".form-group > div").append(e)
                },
                highlight: function(e) {
                    jQuery(e).closest(".form-group").removeClass("is-invalid").addClass("is-invalid")
                },
                success: function(e) {
                    jQuery(e).closest(".form-group").removeClass("is-invalid"), jQuery(e).remove()
                },
                rules: {
                    "val-endereco": {
                        required: !0,
                        minlength: 10
                    },
                    "val-fullname": {
                        required: !0,
                        minlength: 3
                    },
                    "val-username": {
                        required: !0,
                        minlength: 3
                    },
                    "val-email": {
                        required: !0,
                        email: !0
                    },
                    "val-password": {
                        required: !0,
                        minlength: 5
                    },
                    "val-confirm-password": {
                        required: !0,
                        equalTo: "#val-password"
                    },
                    "val-select2": {
                        required: !0
                    },
                    "val-select2-multiple": {
                        required: !0,
                        minlength: 2
                    },
                    "val-suggestions": {
                        required: !0,
                        minlength: 5
                    },
                    "val-skill": {
                        required: !0
                    },
                    "val-currency": {
                        required: !0,
                        currency: ["$", !0]
                    },
                    "val-website": {
                        required: !0,
                        url: !0
                    },
                    "val-site": {
                        required: !0,
                        minlength: 7,
                        url: !0
                    },
                    "val-phoneao": {
                        required: !0,
                        phoneUS: !0
                    },
                    "val-digits": {
                        required: !0,
                        digits: !0
                    },
                    "val-number": {
                        required: !0,
                        number: !0
                    },
                    "val-range": {
                        required: !0,
                        range: [1, 5]
                    },
                    "val-terms": {
                        required: !0
                    }
                },
                messages: {
                    "val-fullname": {
                        required: "Por favor insira um nome de escola de condução",
                        minlength: "O nome da escola de condução deve consistir em pelo menos 3 carateres"
                    },
                    "val-site": {
                        required: "Coloque o website desta escola de condução",
                        minlength: "O website da escola de condução deve consistir em pelo menos 7 carateres",
                        url: "Por favor, insira um telefone de Angola!"
                    },
                    "val-username": {
                        required: "Por favor coloque um nome de usuário",
                        minlength: "Seu nome de usuário deve conter pelo menos três caracteres"
                    },
                    "val-email": "Por favor insira um endereço de e-mail válido",
                    "val-password": {
                        required: "Por favor, forneça uma senha",
                        minlength: "Sua senha deve ter pelo menos 5 caracteres"
                    },
                    "val-confirm-password": {
                        required: "Por favor, forneça uma senha",
                        minlength: "Sua senha deve ter pelo menos 5 caracteres",
                        equalTo: "Por favor, digite a mesma senha acima"
                    },
                    "val-endereco": {
                        required: "Onde esta localizado esta escola de condução ?",
                        minlength: "Por favor, insira um endereço valido"
                    },
                    "val-select2": "Please select a value!",
                    "val-select2-multiple": "Please select at least 2 values!",
                    "val-suggestions": "What can we do to become better?",
                    "val-skill": "Por favor escolhe alguma coisa na lista",
                    "val-currency": "Please enter a price!",
                    "val-website": "Website valido começa com http://",
                    "val-phoneao": "Por favor, insira um telefone de Angola!",
                    "val-digits": "Please enter only digits!",
                    "val-number": "Introduza o valor pago em KZ!",
                    "val-range": "Please enter a number between 1 and 5!",
                    "val-terms": "You must agree to the service terms!"
                }
            })
        }
    return {
        init: function() {
            e(), a(), jQuery(".js-select2").on("change", function() {
                jQuery(this).valid()
            })
        }
    }
}();
jQuery(function() {
    form_validation.init()
});