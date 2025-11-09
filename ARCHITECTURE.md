
---

## 🤔 Por que Java?

Meu conhecimento em Java e .NET é praticamente o mesmo.
No entanto, nos últimos projetos tenho trabalhado mais com Java, o que me deixa mais preparado no momento, mesmo sem ter atuado diretamente com Spring Boot antes.
---

## 🎯 Decisões Arquiteturais

A aplicação segue uma **arquitetura em camadas**, separando responsabilidades de forma clara:

* **Controller** → Responsável por receber as requisições HTTP e orquestrar as chamadas de serviço.
* **Service** → Contém as regras de negócio e validações.
* **Repository (DAO)** → Faz a interface com o banco de dados, utilizando o JPA/Hibernate.

Outras decisões importantes:


* **ORM: JPA/Hibernate**
  Adotado pela produtividade e abstração do SQL, mas com abertura para consultas nativas via `@Query` quando necessário.

* **Migrations: Flyway**
  Utilizado para versionar e manter a consistência do banco de dados.

* **DTO Pattern**
  Implementado para separar modelos internos dos modelos expostos pela API, permitindo evolução independente das camadas.

---

## ⚖️ Trade-offs Considerados

| Decisão                                    | Vantagem                                | Desvantagem                          |
| ------------------------------------------ | --------------------------------------- | ------------------------------------ |
| Validação no Service em vez de annotations | Mais controle e flexibilidade           | Mais código e manutenção             |
| Enums com conversão manual                 | Maior segurança de tipo (*type safety*) | Requer código adicional de conversão |
| Logging síncrono                           | Garantia de persistência imediata       | Leve impacto na performance          |
| Simplicidade inicial                       | Entrega mais rápida e consistente       | Menor nível de otimização no início  |

Essas escolhas foram feitas priorizando **clareza, estabilidade e manutenibilidade** neste estágio inicial do projeto.

---

## 📈 Escalabilidade – Rumo a 100k requisições/dia

A arquitetura foi planejada para crescer de forma sustentável.
Para escalar a aplicação, a estratégia seria evoluir em etapas:

1. **Cache Redis**
   Armazenar dados pouco voláteis (ex: feriados, unidades consumidoras) para reduzir carga no banco.

2. **Read Replicas no PostgreSQL**
   Separar leitura e escrita, melhorando a performance em consultas de histórico.

3. **Processamento Assíncrono de Logs**
   Migrar o logging para uma fila (ex: Kafka ou RabbitMQ) para aliviar a carga da aplicação principal.

   

