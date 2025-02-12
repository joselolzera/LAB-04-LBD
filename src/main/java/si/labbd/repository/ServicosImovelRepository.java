package si.labbd.repository;

import si.labbd.models.ServicosImovel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class ServicosImovelRepository extends BaseRepository<ServicosImovel> {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Lab04");

    public ServicosImovelRepository() {
        super(ServicosImovel.class);
    }


    public List<ServicosImovel> findByImovelComLocacaoAtiva(int imovelId) {
        EntityManager em = emf.createEntityManager();

        try {

            Long locacaoAtivaCount = em.createQuery(
                    "SELECT COUNT(l) FROM Locacao l WHERE l.imovel.id = :imovelId AND l.ativo = true",
                    Long.class)
                .setParameter("imovelId", imovelId)
                .getSingleResult();


            if (locacaoAtivaCount == 0) {
                return List.of();
            }


            List<ServicosImovel> servicos = em.createQuery(
                    "SELECT s FROM ServicosImovel s WHERE s.imovel.id = :imovelId",
                    ServicosImovel.class)
                .setParameter("imovelId", imovelId)
                .getResultList();

            return servicos;
        } catch (Exception e) {
            return List.of();
        } finally {
            em.close();
        }
    }
}
