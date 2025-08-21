package com.exemplo.service;

import org.springframework.stereotype.Service;

@Service
public class MensagemService {

    public String mensagemAniversario(String nome) {
        return "🎈 Feliz Aniversário, " + nome + "! Hoje é seu dia e queremos celebrar com você! 🎂 "
             + "Aproveite 15% OFF em qualquer compra hoje com o cupom ANIVER15. Esperamos por você! 🥳🎁";
    }

    public String mensagemPosCompra(String nome) {
        return "Olá " + nome + ",\n" +
               "Agradecemos por escolher a Ótica Gracinha Currais Novos. Sua satisfação é nossa prioridade.\n\n" +
               "Esperamos que seus novos óculos tragam ainda mais estilo e conforto ao seu dia a dia. Conte conosco sempre que precisar!\n\n" +
               "Esperamos vê-lo(a) novamente em breve para conferir nossas novidades e promoções exclusivas!\n\n" +
               "*Com carinho, equipe Ótica Gracinha Currais Novos. ❤️✨*";
    }

    public String mensagemFeedback(String nome) {
        return "Olá " + nome + ", tudo bem? 😊\n" +
               "Ficamos muito felizes em ter você como cliente da Ótica Gracinha.\n" +
               "Se você puder dedicar 1 minutinho para avaliar nosso atendimento, vai nos ajudar muito a continuar melhorando!\n\n" +
               "👉 É só clicar no link abaixo e deixar sua opinião:\n" +
               "https://bit.ly/43as35u\n\n" +
               "Desde já, agradecemos de coração! 💙";
    }

public String mensagemVideo(String nome) {
    return nome + ", " +
           "Você já pensou em trocar de *carro* 🚘 ou comprar um *imóvel* 🏡 pagando menos da metade da parcela de um financiamento? 🤔\n\n" +
           "Quer que eu te mostre como funciona?";
}

}

