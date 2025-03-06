## Rotina de Parabéns
Este projeto é um sistema para envio automático de mensagens de aniversário via WhatsApp. Ele integra Spring Boot para a API, OpenAI para a geração de mensagens personalizadas e Twilio para o envio das mensagens. Além disso, o sistema lê um arquivo PDF contendo uma lista de aniversariantes e processa seus dados para disparar as mensagens de parabéns.

## Funcionalidades
Leitura de PDF: Extrai os dados dos aniversariantes (nome, data de nascimento, telefone e descrição) de um arquivo PDF.
Geração de Mensagem Personalizada: Utiliza o OpenAI para criar mensagens de parabéns com base na descrição do aniversariante.
Envio de Mensagens via WhatsApp: Integração com Twilio para envio automático das mensagens.
Rotina Automatizada: Processamento dos dados para verificar a data de aniversário e enviar as mensagens correspondentes.

## Requisitos
Java 21 (ou superior)
Maven
Conta e credenciais válidas para:
Twilio (para envio de mensagens no WhatsApp)
OpenAI (para geração das mensagens)

Configuração
Clone o repositório:
    ``git clone https://github.com/PauloRicardocn/Rotina-de-Parab-ns-.git``

Acesse o diretório do projeto:
cd Rotina-de-Parab-ns-

Configure as variáveis de ambiente ou o arquivo de propriedades:

Edite o arquivo src/main/resources/application.properties para incluir as credenciais e configurações necessárias, como as chaves de API do Twilio e do OpenAI, além do caminho do arquivo PDF com a lista de aniversariantes. Exemplo:

# Configurações do Twilio

``twilio.account.sid=SEU_ACCOUNT_SID``

``twilio.auth.token=SEU_AUTH_TOKEN``

``twilio.whatsapp.number=+14155238886``

# Configurações do OpenAI
``openai.api.key=SEU_API_KEY``

# Configuração do PDF com a lista de aniversariantes
pdf.caminho=C:/lista_aniversariantes.pdf
Compilação e Execução
Utilize o Maven para compilar e executar o projeto:

Compile o projeto:
mvn clean package

Execute a aplicação:
mvn spring-boot:run

Após iniciar a aplicação, o servidor Spring Boot estará disponível em:
``http://localhost:8080``

Para disparar o envio das mensagens de parabéns, acesse o seguinte endpoint:
``http://localhost:8080/parabens/enviar``

Esse endpoint processará o PDF, gerará as mensagens personalizadas e enviará os parabéns via WhatsApp conforme configurado.


## Contribuições
Contribuições são bem-vindas! Caso deseje colaborar, por favor, abra uma issue ou envie um pull request com melhorias ou correções.

Licença
Este projeto está licenciado sob os termos da MIT License.
