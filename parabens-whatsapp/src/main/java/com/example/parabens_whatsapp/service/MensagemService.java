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
    return "🏢 Oferta Exclusiva para Pessoa Jurídica – PortoBank\n\n" +
           nome + ", já pensou em comprar ou construir um imóvel de alto valor 🏡 sem entrada e com taxa de apenas 0,09% a.m?\n\n" +
           "Olha esse exemplo real: 👇\n\n" +
           "📌 Plano Pessoa Jurídica\n" +
           "💰 Crédito: R$ 1.000.000,00\n" +
           "⏳ Prazo: 200 meses\n" +
           "💳 Parcela: R$ 5.975,00\n\n" +
           "✨ Mas atenção: até a contemplação, você pode reduzir sua parcela em 50% e pagar apenas R$ 2.987,50.\n\n" +
           "🔥 Benefício Extra: clientes Grupo Porto têm 10% de desconto na taxa.\n" +
           "⏰ Promoção válida até 31/08.\n\n" +
           "📲 Garanta já essa oportunidade e invista no futuro da sua empresa!";
}


}

