package com.example.demo.models.entities;
import com.example.demo.models.dto.EnderecoDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enderecos")
@Getter
@Setter
@NoArgsConstructor
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    @JsonIgnore
    private Usuario usuario;



    public Endereco(String rua, String numero, String bairro,
                    String cidade, String estado, String cep, Usuario usuario) {
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.usuario = usuario;
    }

    public EnderecoDTO toDTO() {
        return new EnderecoDTO(rua, numero, bairro, cidade, estado, cep);
    }
}
