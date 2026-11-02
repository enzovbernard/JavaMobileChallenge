# Clyvo - Gestão Veterinária

## Descrição do Projeto

Aplicação web desenvolvida para a disciplina de Java Advanced (FIAP), com o objetivo de gerenciar o cuidado de animais domésticos: cadastro de responsáveis, animais, veterinários, doenças e vacinas, além do agendamento de consultas e do controle de aplicação de vacinas.

O projeto evoluiu a partir da API REST Clyvo desenvolvida no semestre anterior, agora reconstruído como uma aplicação web completa com camadas de visualização (Thymeleaf), controle de versão de banco de dados (Flyway) e autenticação/autorização por perfil (Spring Security).

Nesta entrega final, a aplicação foi consolidada para atender ao desafio proposto pela Clyvo, que é acompanhar a jornada de saúde do pet de forma contínua. Foram adicionados o painel de lembretes de vacinas e consultas, o controle de doses e o histórico do animal. 

## Alinhamento com o Desafio

- **Organizar**: cadastro de responsáveis, animais, veterinários, doenças e vacinas, com agendamento de consultas e registro da aplicação de vacinas.
- **Lembrar**: painel na página inicial com as vacinas que ainda têm doses pendentes (doses aplicadas, doses restantes, próxima data e situação, atrasada ou no prazo) e as consultas dos próximos 7 dias.
- **Personalizar**: os lembretes são calculados individualmente para cada animal, a partir do seu próprio histórico de aplicações, e a idade é calculada a partir da data de nascimento.
- **Gerar dados**: a tela de detalhes do animal reúne o histórico estruturado de vacinas, consultas e doenças.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 4
- Spring MVC + Thymeleaf
- Spring Security (autenticação por sessão)
- Spring Data JPA / Hibernate
- Flyway (controle de versão do banco de dados)
- Oracle Database
- Maven

## Arquitetura

O projeto segue arquitetura em camadas:

- **Control**: controllers Spring MVC, recebem requisições e retornam `ModelAndView` para as telas Thymeleaf.
- **Model**: entidades JPA que representam as tabelas do banco de dados, e a classe `LembreteVacina`, usada apenas para exibir os dados do painel de lembretes.
- **Repository**: interfaces Spring Data JPA para acesso ao banco.
- **Security**: configuração do Spring Security, autenticação e liberação de rotas por perfil.
- **Templates (Thymeleaf)**: telas HTML da aplicação.

## Funcionalidades

### Cadastros (CRUD completo)
- Responsáveis
- Animais
- Veterinários (somente ADMIN)
- Doenças (somente ADMIN)
- Vacinas (somente ADMIN)

Todos os formulários possuem validação dos dados (campos obrigatórios, CPF, e-mail e data de nascimento).

### Fluxos de negócio
- **Agendamento de consultas**: vincula um animal a um veterinário em uma data futura, impedindo que o mesmo veterinário tenha duas consultas marcadas no mesmo dia. A tela exibe as consultas já agendadas.
- **Aplicação de vacinas**: registra a aplicação de uma vacina em um animal, impedindo a reaplicação antes do prazo definido pela frequência de doses e impedindo que sejam aplicadas mais doses do que o número previsto. A tela exibe as vacinas já aplicadas.

### Painel de lembretes
A página inicial exibe os atalhos de gerenciamento e, abaixo, os lembretes:
- Vacinas com doses pendentes, com a quantidade de doses aplicadas, as doses restantes, a data da próxima dose e a situação (atrasada ou no prazo).
- Consultas agendadas para os próximos 7 dias.

### Histórico do animal
A tela de detalhes do animal mostra a idade calculada a partir da data de nascimento e o histórico de vacinas aplicadas, consultas e doenças registradas.

### Segurança
- Login por sessão com Spring Security.
- Dois perfis de usuário: `ADMIN` (acesso completo) e `USER` (acesso a Responsáveis, Animais, Consultas e Vacinação).
- Rotas de catálogo (Veterinários, Doenças, Vacinas) restritas ao perfil ADMIN.
- Senhas armazenadas com hash BCrypt.
- Proteção CSRF ativa em todos os formulários.
- A remoção de registros é feita por requisição POST, com confirmação antes de apagar.

## Pré-requisitos

- JDK 21 instalado
- Eclipse IDE (com suporte a Maven)
- Acesso a um banco de dados Oracle 

## Configuração

### 1. Clonar o repositório

```
git clone https://github.com/enzovbernard/JavaChallengeClyvo.git
```

### 2. Importar no Eclipse

File > Import > Maven > Existing Maven Projects, selecionando a pasta clonada.

### 3. Configurar o JDK 21

Caso o projeto não reconheça o JDK 21 automaticamente: botão direito no projeto > Properties > Java Build Path > Libraries, e ajuste a JRE System Library para a versão 21. Se necessário, adicione o JDK em Window > Preferences > Java > Installed JREs antes disso.

### 4. Configurar as variáveis de ambiente do banco de dados

As credenciais do banco **não ficam no código-fonte**, por segurança. Elas são lidas de variáveis de ambiente:

| Variável | Descrição | Exemplo |
|---|---|---|
| `SPRING_DATASOURCE_URL` | URL JDBC do Oracle | `jdbc:oracle:thin:@//oracle.fiap.com.br:1521/ORCL` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do Oracle | `seu_usuario` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do Oracle | `sua_senha` |

No Eclipse: Run > Run Configurations > (selecione a configuração do projeto, ou crie uma nova do tipo "Java Application" apontando para `ClyvoJavaApplication`) > aba Environment > New, adicionando as três variáveis acima.

### 5. Executar a aplicação

Botão direito em `ClyvoJavaApplication.java` > Run As > Java Application (ou use a Run Configuration já criada no passo anterior).

Na primeira execução, o Flyway cria automaticamente todas as tabelas, popula os usuários iniciais e carrega alguns dados de demonstração. Não é necessário rodar nenhum script SQL manualmente.

### 6. Acessar a aplicação

Abra o navegador em:

```
http://localhost:8080/login
```

## Dados de Demonstração

A migration de demonstração cadastra dois responsáveis, dois veterinários, quatro animais (Thor, Mel, Bob e Pipoca), três vacinas, duas doenças, aplicações de vacinas e consultas, para que o painel de lembretes e o histórico do animal já apareçam preenchidos. As datas são calculadas a partir do dia da primeira execução.

## Usuários de Teste

| Usuário | Senha | Perfil |
|---|---|---|
| admin | 1234 | ADMIN |
| usuario | 1234 | USER |

## Perfis de Acesso

| Recurso | ADMIN | USER |
|---|---|---|
| Responsáveis | Sim | Sim |
| Animais | Sim | Sim |
| Consultas | Sim | Sim |
| Vacinação | Sim | Sim |
| Veterinários | Sim | Não |
| Doenças | Sim | Não |
| Vacinas | Sim | Não |

Os scripts SQL do banco de dados estão em `src/main/resources/db/migration` (estrutura das tabelas, usuários iniciais e dados de demonstração).

## Link do vídeo


## Integrantes

- Caio Kenzo Tayra - RM562979
- Enzo Vieira Bernardini - RM563000
- Nicolas Mota Cândido - RM561857
