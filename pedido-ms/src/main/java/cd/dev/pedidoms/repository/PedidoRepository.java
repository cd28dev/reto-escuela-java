package cd.dev.pedidoms.repository;

import cd.dev.pedidoms.model.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
