package si.labbd.models;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "profissionais")
public class Profissionais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(nullable = false, length = 100)
    private String nome;

    @NonNull
    @Column(nullable = false, length = 100)
    private String profissao;

    @NonNull
    @Column(nullable = false, length = 12)
    private String telefone;

    @Column(length = 12)
    private String telefone2;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor_hora;

    @Column(columnDefinition = "TEXT")
    private String obs;

}