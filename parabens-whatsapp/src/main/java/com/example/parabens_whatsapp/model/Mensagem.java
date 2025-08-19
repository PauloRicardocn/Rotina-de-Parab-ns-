    package com.exemplo.model;

    import jakarta.persistence.*;
    import java.util.Date;

    @Entity
    public class Mensagem {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String nome;


        private String telefone;

        private String descricao;
        private String mensagemGerada;
        private Date dataEnvio;

        // Getters e Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }


        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getMensagemGerada() {
            return mensagemGerada;
        }

        public void setMensagemGerada(String mensagemGerada) {
            this.mensagemGerada = mensagemGerada;
        }

        public Date getDataEnvio() {
            return dataEnvio;
        }

        public void setDataEnvio(Date dataEnvio) {
            this.dataEnvio = dataEnvio;
        }
    }