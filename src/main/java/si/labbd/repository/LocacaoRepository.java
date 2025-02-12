package si.labbd.repository;

import si.labbd.models.Locacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class LocacaoRepository extends BaseRepository<Locacao> {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lab04");

    public LocacaoRepository() {
        super(Locacao.class);
    }


    public List<Locacao> findByClienteId(int clienteId) {
        EntityManager em = emf.createEntityManager();
        List<Locacao> lista = em.createQuery("SELECT l FROM Locacao l WHERE l.inquilino.id = :clienteId", Locacao.class)
                .setParameter("clienteId", clienteId)
                .getResultList();
        em.close();
        return lista;
    }


    private boolean isImovelAlugado(int imovelId) {
        EntityManager em = emf.createEntityManager();
        Long count = em.createQuery("SELECT COUNT(l) FROM Locacao l WHERE l.imovel.id = :imovelId", Long.class)
                .setParameter("imovelId", imovelId)
                .getSingleResult();
        em.close();
        return count > 0;
    }

    private boolean isImovelDisponivel(int imovelId) {
        EntityManager em = emf.createEntityManager();

        Long count = em.createQuery(
                        "SELECT COUNT(l) FROM Locacao l WHERE l.imovel.id = :imovelId AND l.ativo = true", Long.class)
                .setParameter("imovelId", imovelId)
                .getSingleResult();

        em.close();

        return count == 0;
    }


    @Override
    public void save(Locacao locacao) {
        if (!isImovelDisponivel(locacao.getImovel().getId())) {
            throw new RuntimeException("Erro: O imóvel não está disponível para locação.");
        }
        if (isImovelAlugado(locacao.getImovel().getId())) {
            throw new RuntimeException("Erro: O imóvel já está alugado.");
        }
        super.save(locacao);
    }
}
