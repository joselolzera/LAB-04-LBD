package si.labbd.repository;

import si.labbd.models.Alugueis;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class AlugueisRepository extends BaseRepository<Alugueis> {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lab04");

    public AlugueisRepository() {
        super(Alugueis.class);
    }

    public List<Alugueis> findByInquilinoId(int inquilinoId) {
        EntityManager em = emf.createEntityManager();
        List<Alugueis> lista = em.createQuery(
                        "SELECT a FROM Alugueis a WHERE a.locacao.inquilino.id = :inquilinoId", Alugueis.class)
                .setParameter("inquilinoId", inquilinoId)
                .getResultList();
        em.close();
        return lista;
    }

    public List<Alugueis> findByInquilinoNome(String nome) {
        EntityManager em = emf.createEntityManager();
        List<Alugueis> lista = em.createQuery(
                        "SELECT a FROM Alugueis a WHERE a.locacao.inquilino.nome LIKE :nome", Alugueis.class)
                .setParameter("nome", "%" + nome + "%") // Busca parcial pelo nome
                .getResultList();
        em.close();
        return lista;
    }

    public List<Alugueis> findByValorAluguelMenorQue(double valorMaximo) {
        EntityManager em = emf.createEntityManager();
        List<Alugueis> lista = em.createQuery(
                        "SELECT a FROM Alugueis a WHERE a.locacao.imovel.valorAluguelSugerido <= :valorMaximo",
                        Alugueis.class)
                .setParameter("valorMaximo", valorMaximo)
                .getResultList();
        em.close();
        return lista;
    }

    public List<Alugueis> findAlugueisComAtraso() {
        EntityManager em = emf.createEntityManager();
        List<Alugueis> lista = em.createQuery(
                        "SELECT a FROM Alugueis a WHERE a.dataPagamento > a.dataVencimento", Alugueis.class)
                .getResultList();
        em.close();
        return lista;
    }

    public Alugueis findByLocacaoId(int locacaoId) {
        EntityManager em = emf.createEntityManager();
        Alugueis aluguel = null;

        try {
            aluguel = em.createQuery(
                            "SELECT a FROM Alugueis a WHERE a.locacao.id = :locacaoId", Alugueis.class)
                    .setParameter("locacaoId", locacaoId)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println("Nenhum aluguel encontrado para a locação ID " + locacaoId);
        } finally {
            em.close();
        }

        return aluguel;
    }
}
