package si.labbd.service;

import si.labbd.models.Alugueis;
import si.labbd.repository.AlugueisRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PagamentoAluguelService {
    private final AlugueisRepository alugueisRepository;

    public PagamentoAluguelService() {
        this.alugueisRepository = new AlugueisRepository();
    }

    public void registrarPagamento(int locacaoId, BigDecimal valorPago, Date dataPagamento) {

        Alugueis aluguel = alugueisRepository.findByLocacaoId(locacaoId);

        if (aluguel == null) {
            throw new RuntimeException("Erro: Nenhum aluguel encontrado para a locação ID " + locacaoId);
        }


        BigDecimal multa = calcularMulta(aluguel.getDataVencimento(), dataPagamento, aluguel.getValorPago());
        BigDecimal valorFinal = aluguel.getValorPago().add(multa);


        aluguel.setDataPagamento(dataPagamento);
        aluguel.setValorPago(valorFinal);
        alugueisRepository.update(aluguel);
    }


    private BigDecimal calcularMulta(Date dataVencimento, Date dataPagamento, BigDecimal valorAluguel) {
        if (!dataPagamento.after(dataVencimento)) {
            return BigDecimal.ZERO; // Sem multa se pago no prazo
        }


        long diff = dataPagamento.getTime() - dataVencimento.getTime();
        long diasAtraso = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);


        BigDecimal taxaMultaDiaria = new BigDecimal("0.0033");
        BigDecimal multaCalculada = valorAluguel.multiply(taxaMultaDiaria).multiply(new BigDecimal(diasAtraso));
        BigDecimal multaMaxima = valorAluguel.multiply(new BigDecimal("0.20")); // Máximo 20% do valor do aluguel

        return multaCalculada.min(multaMaxima).setScale(2, RoundingMode.HALF_UP); // Ajustar para 2 casas decimais
    }
}
