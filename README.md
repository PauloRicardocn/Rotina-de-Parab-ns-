# 🎉 Rotina de Parabéns

Este projeto é um sistema para envio automático de mensagens de aniversário via **WhatsApp**.  
Ele integra **Spring Boot** para a API, **OpenAI** para a geração de mensagens personalizadas e **Z-API** para o envio das mensagens.  
Além disso, o sistema lê um arquivo **PDF** contendo uma lista de aniversariantes e processa seus dados para disparar as mensagens de parabéns.

---

## 🚀 Funcionalidades

- **Leitura de PDF**: Extrai os dados dos aniversariantes (nome, data de nascimento, telefone e descrição) de um arquivo PDF.
- **Geração de Mensagem Personalizada**: Utiliza a **OpenAI** para criar mensagens de parabéns com base na descrição do aniversariante.
- **Envio de Mensagens via WhatsApp**: Integração com a **Z-API** para envio automático das mensagens.
- **Rotina Automatizada**: Processamento dos dados para verificar a data de aniversário e enviar as mensagens correspondentes.

---

## ✅ Requisitos

- Java **21** (ou superior)
- Maven
- Conta e credenciais válidas para:
- Z-API (para envio de mensagens no WhatsApp)
- OpenAI (para geração das mensagens)

---

## 📝 Como criar conta na Z-API
Acesse o site oficial da Z-API: https://z-api.io/

Clique em "Criar Conta".

Preencha os dados solicitados (nome, e-mail, senha).

Após confirmar sua conta, faça login.

Crie uma nova Instância para obter:

INSTANCE_ID

TOKEN

CLIENT_TOKEN (Na aba "Segurança", localize o módulo "Token de Segurança da Conta".
Clique na opção "Configurar Agora". Isso gerará um token, que inicialmente estará desabilitado para evitar interrupções na operação da sua aplicação.)

Configure seu WhatsApp escaneando o QR Code fornecido.

Use as credenciais no seu arquivo application.properties.

Para mais detalhes, veja a documentação oficial:
https://docs.z-api.io/

---

## 📝 Como criar conta na OpenAI

Para gerar mensagens personalizadas, é necessário ter uma conta e chave de API da OpenAI.
Passo a passo:

Acesse o site oficial: https://platform.openai.com/

Clique em "Sign Up" (caso não tenha conta) ou "Log In" (se já tiver).

Finalize o cadastro utilizando seu e-mail ou conta Google/Microsoft.

Após login, vá até a aba de API Keys:

https://platform.openai.com/account/api-keys

Clique em "Create new secret key".

Copie a chave gerada (API Key) e salve em local seguro.

---

## ⚙️ Configuração

Clone o repositório:
    ``git clone https://github.com/PauloRicardocn/Rotina-de-Parab-ns-.git``

Acesse o diretório do projeto:
cd Rotina-de-Parab-ns-

Configure as variáveis de ambiente ou o arquivo de propriedades:

Edite o arquivo src/main/resources/application.properties para incluir as credenciais e configurações necessárias, como as chaves de API do Z-API e do OpenAI, além do caminho do arquivo PDF com a lista de aniversariantes. Exemplo:

## Configurações do Z-API

``zapi.session.id=SEU_SESSION_ID``

``zapi.tokenn=SEU_TOKEN``

``zapi.client.token= ``

## Configurações do OpenAI
``openai.api.key=SEU_API_KEY``

## Configuração do PDF com a lista de aniversariantes
pdf.caminho="Caminho/da/Pasta"

## Compilação e Execução
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

## 🧰 Exemplo no Postman:
Abra o Postman.
Crie uma nova requisição.
Método: GET
URL: http://localhost:8080/parabens/enviar
Clique em Send.
Verifique a resposta e acompanhe os envios no seu console/log.

---
## Contribuições
Contribuições são bem-vindas! Caso deseje colaborar, por favor, abra uma issue ou envie um pull request com melhorias ou correções.

Licença
Este projeto está licenciado sob os termos da MIT License.
